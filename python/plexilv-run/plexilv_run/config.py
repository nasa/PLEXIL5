"""Configuration management for plexilv."""

import os
from dataclasses import dataclass
from pathlib import Path

from plexilv_run.exceptions import ConfigurationError


@dataclass
class Config:
    """Configuration for plexilv execution.

    Attributes:
        plan_path: Path to the .plx plan file
        script_path: Path to the .psx script file
        semantics_path: Path to the plexil-v.maude file
        plx2maude_path: Path to plx2maude executable
        psx2maude_path: Path to psx2maude executable
        maude_path: Path to maude executable
        maude_opts: Additional arguments for maude
        maude_timeout: Timeout for maude execution (seconds)
        conversion_timeout: Timeout for conversion tools (seconds)
        work_dir: Custom workspace directory (None = auto-generate)
        log_file: Path to log file (None = stdout)
        debug: Enable debug mode
        cleanup: Remove workspace on success
        force_cleanup: Remove workspace always
        relative_paths: Use relative paths in run.maude
    """

    plan_path: Path
    script_path: Path
    semantics_path: Path
    plx2maude_path: str
    psx2maude_path: str
    maude_path: str
    maude_opts: list[str]
    maude_timeout: int
    conversion_timeout: int
    work_dir: Path | None
    log_file: Path | None
    debug: bool
    cleanup: bool
    force_cleanup: bool
    relative_paths: bool

    @staticmethod
    def resolve_semantics_path(
        semantics_module: str | None,
        plexilv_home: str | None,
    ) -> Path:
        """
        Resolve the path to plexil-v.maude.

        Priority order:
        1. semantics_module (direct path)
        2. plexilv_home (path to repo root)
        3. PLEXILV_HOME environment variable
        4. Error if none set

        Args:
            semantics_module: Direct path to plexil-v.maude (optional)
            plexilv_home: Path to PLEXIL-V repository root (optional)

        Returns:
            Resolved Path to plexil-v.maude

        Raises:
            ConfigurationError: If path cannot be resolved or file doesn't exist
        """
        # Priority 1: Direct semantics module path
        if semantics_module:
            path = Path(semantics_module)
            if not path.exists():
                raise ConfigurationError(f"Semantics file not found: {path}")
            if not os.access(path, os.R_OK):
                raise ConfigurationError(f"Cannot read semantics file: {path}")
            return path.resolve()

        # Priority 2: plexilv_home option
        # Priority 3: PLEXILV_HOME environment variable
        home = plexilv_home or os.getenv("PLEXILV_HOME")

        if not home:
            raise ConfigurationError(
                "PLEXILV_HOME not set. Please either:\n"
                "  - Set PLEXILV_HOME environment variable, or\n"
                "  - Use --plexilv-home option, or\n"
                "  - Use --semantics-module option"
            )

        # Construct path: <home>/semantics/src/plexil-v.maude
        path = Path(home) / "semantics" / "src" / "plexil-v.maude"

        if not path.exists():
            raise ConfigurationError(
                f"Semantics file not found: {path}\n"
                f"PLEXILV_HOME is set to: {home}\n"
                "Please verify your PLEXILV_HOME points to the repository root"
            )

        if not os.access(path, os.R_OK):
            raise ConfigurationError(f"Cannot read semantics file: {path}")

        return path.resolve()

    def validate(self) -> None:
        """
        Validate configuration consistency.

        Raises:
            ConfigurationError: If configuration is invalid
        """
        # Check timeouts are positive
        if self.maude_timeout <= 0:
            raise ConfigurationError("Maude timeout must be positive")

        if self.conversion_timeout <= 0:
            raise ConfigurationError("Conversion timeout must be positive")

        # Check cleanup flags are not both set
        if self.cleanup and self.force_cleanup:
            raise ConfigurationError(
                "Cannot specify both --cleanup and --force-cleanup"
            )

        # Check log file doesn't exist if specified
        if self.log_file and self.log_file.exists():
            raise ConfigurationError(
                f"Log file already exists: {self.log_file}\n"
                "Please choose a different file or remove the existing one"
            )

        # Check work_dir doesn't exist if specified
        if self.work_dir and self.work_dir.exists():
            raise ConfigurationError(
                f"Workspace directory already exists: {self.work_dir}\n"
                "Please choose a different directory or remove the existing one"
            )