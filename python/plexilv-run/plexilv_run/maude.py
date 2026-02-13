"""Maude execution and run.maude generation for plexilv."""

import os
import subprocess
from pathlib import Path

from plexilv_run.config import Config
from plexilv_run.exceptions import MaudeError, MaudeTimeoutError
from plexilv_run.utils import format_command, get_last_n_lines
from plexilv_run.workspace import Workspace


class MaudeRunner:
    """Handles Maude execution and run.maude generation.

    Attributes:
        workspace: Workspace for files and logs
        config: Configuration with paths and settings
    """

    def __init__(self, workspace: Workspace, config: Config):
        """
        Initialize Maude runner.

        Args:
            workspace: Workspace instance
            config: Configuration instance
        """
        self.workspace = workspace
        self.config = config

    def compute_plan_module_name(self, plan_stem: str) -> str:
        """
        Compute the plan module name from plan filename.

        Transforms: replace underscores with dashes, add -PLAN suffix.

        Args:
            plan_stem: Plan filename without extension (e.g., 'test_plan_01')

        Returns:
            Module name (e.g., 'test-plan-01-PLAN')

        Examples:
            >>> runner.compute_plan_module_name('MyPlan')
            'MyPlan-PLAN'
            >>> runner.compute_plan_module_name('test_plan_01')
            'test-plan-01-PLAN'
        """
        return plan_stem.replace("_", "-") + "-PLAN"

    def get_load_path(self, target_path: Path) -> str:
        """
        Get the load path for use in run.maude.

        Returns absolute path by default, or relative path if configured.

        Args:
            target_path: Path to the file to load

        Returns:
            Path string (absolute or relative to workspace)
        """
        if self.config.relative_paths:
            # Return path relative to workspace
            try:
                return os.path.relpath(target_path, self.workspace.path)
            except ValueError:
                # If relative path computation fails (e.g., different drives on Windows),
                # fall back to absolute path
                return str(target_path.resolve())
        else:
            # Return absolute path
            return str(target_path.resolve())

    def generate_run_file(self, plan_stem: str, script_stem: str) -> None:
        """
        Generate the run.maude file.

        Args:
            plan_stem: Plan filename without extension
            script_stem: Script filename without extension
        """
        # Compute paths
        semantics_path = self.get_load_path(self.config.semantics_path)
        plan_maude_path = self.get_load_path(
            self.workspace.get_plan_maude_path(plan_stem)
        )
        script_maude_path = self.get_load_path(
            self.workspace.get_script_maude_path(script_stem)
        )

        # Compute module name
        plan_module = self.compute_plan_module_name(plan_stem)

        # Generate run.maude content
        content = f"""load {semantics_path}
load {plan_maude_path}
load {script_maude_path}

set print attribute on .
set print color on .

mod PLAN-SIMULATION is
    protecting {plan_module} .
    protecting INPUT .
endm

rew run(compile(rootNode,input)) .
q
"""

        # Write to workspace
        run_maude_path = self.workspace.get_run_maude_path()
        run_maude_path.write_text(content)

    def execute(self) -> int:
        """
        Execute Maude interpreter.

        Returns:
            Exit code from Maude execution

        Raises:
            MaudeError: If Maude execution fails
            MaudeTimeoutError: If Maude execution times out
        """
        # Build command
        cmd = [
            self.config.maude_path,
            "-no-ansi-color",
            "-no-wrap",
            "-no-banner",
            "-no-advise",
            "-print-to-stderr",
        ]

        # Add user-provided options
        if self.config.maude_opts:
            # Split each option string by spaces
            for opts in self.config.maude_opts:
                cmd.extend(opts.split())

        # Add run.maude file with absolute path
        run_maude_path = self.workspace.get_run_maude_path()
        cmd.append(str(run_maude_path.resolve()))

        try:
            # Run Maude
            result = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                timeout=self.config.maude_timeout,
                cwd=self.workspace.path,
                stdin=subprocess.DEVNULL,
            )

            # Save logs if debug mode
            if self.config.debug:
                stdout_log, stderr_log = self.workspace.get_tool_log_paths("maude")
                stdout_log.write_text(result.stdout)
                stderr_log.write_text(result.stderr)

            # Handle output based on config
            if self.config.log_file:
                # Write stderr to log file
                self.config.log_file.write_text(result.stderr)
            else:
                # Write stderr to stdout (default)
                print(result.stderr, end="")

            # Check exit code
            if result.returncode != 0:
                error_msg = self._format_error(cmd, result, is_timeout=False)
                print(error_msg, file=__import__('sys').stderr)
                raise MaudeError(result.returncode, result.stderr)

            return result.returncode

        except subprocess.TimeoutExpired as e:
            # Handle timeout
            stdout = e.stdout.decode() if e.stdout else ""
            stderr = e.stderr.decode() if e.stderr else ""

            # Save logs if debug mode
            if self.config.debug:
                stdout_log, stderr_log = self.workspace.get_tool_log_paths("maude")
                stdout_log.write_text(stdout)
                stderr_log.write_text(stderr)

            error_msg = self._format_timeout_error(cmd, self.config.maude_timeout)
            print(error_msg, file=__import__('sys').stderr)
            raise MaudeTimeoutError(self.config.maude_timeout)

    def _format_error(
        self,
        cmd: list[str],
        result: subprocess.CompletedProcess,
        is_timeout: bool,
    ) -> str:
        """
        Format error message for Maude execution failure.

        Args:
            cmd: Command that was executed
            result: Result from subprocess.run
            is_timeout: Whether this was a timeout error

        Returns:
            Formatted error message
        """
        if not self.config.debug:
            # Simple error message
            return f"Error: Maude execution failed with exit code {result.returncode}\nstderr output:\n{result.stderr}"

        # Debug mode: detailed error message
        lines = [
            f"Error: Maude execution failed with exit code {result.returncode}",
            "",
            f"Command: {format_command(cmd)}",
            f"Working directory: {self.workspace.path}",
            f"Exit code: {result.returncode}",
            "",
        ]

        # Add last 10 lines of stdout if not empty
        if result.stdout:
            stdout_last = get_last_n_lines(result.stdout, 10)
            lines.extend([
                "stdout output (last 10 lines):",
                stdout_last,
                "",
            ])

        # Add last 10 lines of stderr
        stderr_last = get_last_n_lines(result.stderr, 10)
        lines.extend([
            "stderr output (last 10 lines):",
            stderr_last,
            "",
        ])

        # Add log file locations
        stdout_log, stderr_log = self.workspace.get_tool_log_paths("maude")
        lines.extend([
            "Full logs saved to:",
            f"  stdout: {stdout_log}",
            f"  stderr: {stderr_log}",
        ])

        return "\n".join(lines)

    def _format_timeout_error(self, cmd: list[str], timeout: int) -> str:
        """
        Format error message for Maude timeout.

        Args:
            cmd: Command that was executed
            timeout: Timeout value in seconds

        Returns:
            Formatted error message
        """
        if not self.config.debug:
            # Simple error message
            return f"Error: Maude execution timed out after {timeout} seconds"

        # Debug mode: detailed error message
        lines = [
            f"Error: Maude execution timed out after {timeout} seconds",
            "",
            f"Command: {format_command(cmd)}",
            f"Working directory: {self.workspace.path}",
            "",
        ]

        # Add log file locations
        stdout_log, stderr_log = self.workspace.get_tool_log_paths("maude")
        lines.extend([
            "Full logs saved to:",
            f"  stdout: {stdout_log}",
            f"  stderr: {stderr_log}",
        ])