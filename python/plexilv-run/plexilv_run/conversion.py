"""Conversion tool execution for plexilv."""

import subprocess
from pathlib import Path

from plexilv_run.config import Config
from plexilv_run.exceptions import ConversionError, ConversionTimeoutError
from plexilv_run.utils import format_command, get_last_n_lines
from plexilv_run.workspace import Workspace


class ConversionRunner:
    """Handles execution of plx2maude and psx2maude conversion tools.

    Attributes:
        workspace: Workspace for output files and logs
        config: Configuration with tool paths and settings
    """

    def __init__(self, workspace: Workspace, config: Config):
        """
        Initialize conversion runner.

        Args:
            workspace: Workspace instance
            config: Configuration instance
        """
        self.workspace = workspace
        self.config = config

    def run_plx2maude(self) -> bool:
        """
        Run plx2maude to convert plan file.

        Returns:
            True if conversion succeeded, False otherwise

        Note:
            Errors are printed to stderr but function returns False
            rather than raising exceptions to allow checking other conversions.
        """
        plan_stem = self.config.plan_path.stem
        output_path = self.workspace.get_plan_maude_path(plan_stem)

        return self._run_converter(
            tool_name="plx2maude",
            tool_path=self.config.plx2maude_path,
            input_path=self.config.plan_path,
            output_path=output_path,
        )

    def run_psx2maude(self) -> bool:
        """
        Run psx2maude to convert script file.

        Returns:
            True if conversion succeeded, False otherwise

        Note:
            Errors are printed to stderr but function returns False
            rather than raising exceptions to allow checking other conversions.
        """
        script_stem = self.config.script_path.stem
        output_path = self.workspace.get_script_maude_path(script_stem)

        return self._run_converter(
            tool_name="psx2maude",
            tool_path=self.config.psx2maude_path,
            input_path=self.config.script_path,
            output_path=output_path,
        )

    def _run_converter(
        self,
        tool_name: str,
        tool_path: str,
        input_path: Path,
        output_path: Path,
    ) -> bool:
        """
        Run a conversion tool and capture output.

        Args:
            tool_name: Name of the tool (for error messages)
            tool_path: Path to the tool executable
            input_path: Path to input file
            output_path: Path to output .maude file

        Returns:
            True if conversion succeeded, False otherwise
        """
        # Build command with absolute paths
        cmd = [tool_path, str(input_path.resolve())]

        try:
            # Run the converter
            result = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                timeout=self.config.conversion_timeout,
                cwd=self.workspace.path,
                stdin=subprocess.DEVNULL,
            )

            # Save logs if debug mode
            if self.config.debug:
                stdout_log, stderr_log = self.workspace.get_tool_log_paths(tool_name)
                stdout_log.write_text(result.stdout)
                stderr_log.write_text(result.stderr)

            # Check if conversion succeeded
            if result.returncode != 0:
                error_msg = self._format_error(
                    tool_name=tool_name,
                    cmd=cmd,
                    result=result,
                    is_timeout=False,
                )
                print(error_msg, file=__import__('sys').stderr)
                return False

            # Write stdout to output file
            output_path.write_text(result.stdout)
            return True

        except subprocess.TimeoutExpired as e:
            # Handle timeout
            stdout = e.stdout.decode() if e.stdout else ""
            stderr = e.stderr.decode() if e.stderr else ""

            # Save logs if debug mode
            if self.config.debug:
                stdout_log, stderr_log = self.workspace.get_tool_log_paths(tool_name)
                stdout_log.write_text(stdout)
                stderr_log.write_text(stderr)

            error_msg = self._format_timeout_error(
                tool_name=tool_name,
                cmd=cmd,
                timeout=self.config.conversion_timeout,
                stdout=stdout,
                stderr=stderr,
            )
            print(error_msg, file=__import__('sys').stderr)
            return False

    def _format_error(
        self,
        tool_name: str,
        cmd: list[str],
        result: subprocess.CompletedProcess,
        is_timeout: bool,
    ) -> str:
        """
        Format error message for conversion failure.

        Args:
            tool_name: Name of the tool
            cmd: Command that was executed
            result: Result from subprocess.run
            is_timeout: Whether this was a timeout error

        Returns:
            Formatted error message
        """
        if not self.config.debug:
            # Simple error message
            return f"Error: {tool_name} failed with exit code {result.returncode}\nstderr output:\n{result.stderr}"

        # Debug mode: detailed error message
        lines = [
            f"Error: {tool_name} failed with exit code {result.returncode}",
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
        stdout_log, stderr_log = self.workspace.get_tool_log_paths(tool_name)
        lines.extend([
            "Full logs saved to:",
            f"  stdout: {stdout_log}",
            f"  stderr: {stderr_log}",
        ])

        return "\n".join(lines)

    def _format_timeout_error(
        self,
        tool_name: str,
        cmd: list[str],
        timeout: int,
        stdout: str,
        stderr: str,
    ) -> str:
        """
        Format error message for timeout.

        Args:
            tool_name: Name of the tool
            cmd: Command that was executed
            timeout: Timeout value in seconds
            stdout: Captured stdout (may be empty)
            stderr: Captured stderr (may be empty)

        Returns:
            Formatted error message
        """
        if not self.config.debug:
            # Simple error message
            return f"Error: {tool_name} execution timed out after {timeout} seconds"

        # Debug mode: detailed error message
        lines = [
            f"Error: {tool_name} execution timed out after {timeout} seconds",
            "",
            f"Command: {format_command(cmd)}",
            f"Working directory: {self.workspace.path}",
            "",
        ]

        # Add log file locations
        stdout_log, stderr_log = self.workspace.get_tool_log_paths(tool_name)
        lines.extend([
            "Full logs saved to:",
            f"  stdout: {stdout_log}",
            f"  stderr: {stderr_log}",
        ])

        return "\n".join(lines)