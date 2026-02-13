"""Pipeline orchestration for plexilv."""

import sys
from pathlib import Path

from plexilv_run.config import Config
from plexilv_run.conversion import ConversionRunner
from plexilv_run.exceptions import (
    ConfigurationError,
    PlexilvRunError,
    ValidationError,
)
from plexilv_run.maude import MaudeRunner
from plexilv_run.tools import check_all_tools
from plexilv_run.validation import validate_plan_file, validate_script_file
from plexilv_run.workspace import Workspace


class Pipeline:
    """Orchestrates the complete plexilv execution pipeline.

    Attributes:
        config: Configuration for the pipeline
        workspace: Workspace instance (created during run)
        errors: List of error messages collected during execution
    """

    def __init__(self, config: Config):
        """
        Initialize pipeline.

        Args:
            config: Configuration instance
        """
        self.config = config
        self.workspace: Workspace | None = None
        self.errors: list[str] = []

    def run(self) -> int:
        """
        Run the complete pipeline.

        Returns:
            Exit code (0 for success, non-zero for failure)

        Pipeline steps:
        1. Validation - Check tools, config, input files
        2. Workspace creation
        3. Conversion - Run plx2maude and psx2maude
        4. Generate run.maude file
        5. Execute Maude
        6. Cleanup (if configured)
        """
        success = False
        exit_code = 1

        try:
            # Step 1: Validation
            self._validate()

            # Step 2: Create workspace
            self._create_workspace()

            # Step 3: Conversion
            if not self._convert():
                print("Conversion failed. Aborting.", file=sys.stderr)
                return 1

            # Step 4: Generate run.maude
            self._generate_run_file()

            # Step 5: Execute Maude
            exit_code = self._execute_maude()
            success = (exit_code == 0)

            return exit_code

        except PlexilvRunError as e:
            print(f"Error: {e}", file=sys.stderr)
            return 1

        except KeyboardInterrupt:
            print("\nInterrupted by user", file=sys.stderr)
            return 130

        except Exception as e:
            print(f"Unexpected error: {e}", file=sys.stderr)
            if self.config.debug:
                import traceback
                traceback.print_exc()
            return 1

        finally:
            # Step 6: Cleanup
            if self.workspace:
                self._cleanup(success)

    def _validate(self) -> None:
        """
        Validation phase: check tools, config, and input files.

        Raises:
            ValidationError: If validation fails
            ConfigurationError: If configuration is invalid
            ToolNotFoundError: If required tools are missing
        """
        # Validate configuration
        self.config.validate()

        # Check tool availability
        check_all_tools(
            plx2maude_path=self.config.plx2maude_path,
            psx2maude_path=self.config.psx2maude_path,
            maude_path=self.config.maude_path,
        )

        # Validate input files
        validate_plan_file(self.config.plan_path)
        validate_script_file(self.config.script_path)

    def _create_workspace(self) -> None:
        """
        Workspace creation phase.

        Raises:
            WorkspaceError: If workspace creation fails
        """
        self.workspace = Workspace(
            base_dir=self.config.work_dir,
            debug=self.config.debug,
        )
        self.workspace.create()

    def _convert(self) -> bool:
        """
        Conversion phase: run plx2maude and psx2maude.

        Returns:
            True if both conversions succeeded, False otherwise

        Note:
            Both conversions are attempted even if one fails,
            to provide complete error information to the user.
        """
        assert self.workspace is not None, "Workspace must be created first"

        runner = ConversionRunner(self.workspace, self.config)

        # Try both conversions
        plx_success = runner.run_plx2maude()
        psx_success = runner.run_psx2maude()

        # Return True only if both succeeded
        return plx_success and psx_success

    def _generate_run_file(self) -> None:
        """
        Generate run.maude file.

        Raises:
            Exception: If file generation fails
        """
        assert self.workspace is not None, "Workspace must be created first"

        runner = MaudeRunner(self.workspace, self.config)

        plan_stem = self.config.plan_path.stem
        script_stem = self.config.script_path.stem

        runner.generate_run_file(plan_stem, script_stem)

    def _execute_maude(self) -> int:
        """
        Execute Maude interpreter.

        Returns:
            Exit code from Maude

        Raises:
            MaudeError: If Maude execution fails
            MaudeTimeoutError: If Maude times out
        """
        assert self.workspace is not None, "Workspace must be created first"

        runner = MaudeRunner(self.workspace, self.config)
        return runner.execute()

    def _cleanup(self, success: bool) -> None:
        """
        Cleanup phase: remove workspace if configured.

        Args:
            success: Whether the pipeline succeeded
        """
        if not self.workspace:
            return

        try:
            # Determine if we should cleanup based on config and success
            should_cleanup = self.config.force_cleanup or (self.config.cleanup and success)

            if should_cleanup:
                self.workspace.cleanup()
        except Exception as e:
                print(f"Warning: Cleanup failed: {e}", file=sys.stderr)