"""Configuration management for plexilv-diff application."""

from dataclasses import dataclass, field
from pathlib import Path
from typing import Optional, List
import shutil

from .exceptions import ValidationError, ConfigurationError


@dataclass
class Config:
    """Configuration for plexilv-diff execution.

    Attributes:
        plan: Path to PLEXIL plan file (.ple or .plx)
        scripts: List of paths to test script files (.pst or .psx)
        plexilc: Path to plexilc compiler executable
        plexiltest: Path to plexiltest executable
        plexilv_run: Path to plexilv-run executable
        plexilog_diff: Path to plexilog-diff executable
        plexilc_flags: Additional flags for plexilc
        plexiltest_flags: Additional flags for plexiltest
        plexilv_run_flags: Additional flags for plexilv-run
        plexilog_diff_flags: Additional flags for plexilog-diff
        debug_config: Path to debug config file (None = auto-generate)
        work_dir: Working directory path (None = auto-generate)
        compilation_timeout: Timeout for compilation in seconds
        execution_timeout: Timeout for test execution in seconds
        cleanup: Delete workspace on success only
        force_cleanup: Delete workspace always (success or failure)
        fail_fast: Stop on first test failure
        failproof: Validate all tools before starting
        quiet: Minimal output (only workspace location)
    """

    # Required arguments
    plan: Path
    scripts: List[Path]

    # Tool paths
    plexilc: str = "plexilc"
    plexiltest: str = "plexiltest"
    plexilv_run: str = "plexilv-run"
    plexilog_diff: str = "plexilog-diff"

    # Tool flags
    plexilc_flags: str = ""
    plexiltest_flags: str = ""
    plexilv_run_flags: str = ""
    plexilog_diff_flags: str = ""

    # Configuration
    debug_config: Optional[Path] = None
    work_dir: Optional[Path] = None

    # Timeouts
    compilation_timeout: int = 30
    execution_timeout: int = 300

    # Cleanup options
    cleanup: bool = False
    force_cleanup: bool = False

    # Behavior options
    fail_fast: bool = False
    failproof: bool = False
    quiet: bool = False

    def __post_init__(self):
        """Validate configuration after initialization."""
        # Convert strings to Paths if needed
        if isinstance(self.plan, str):
            self.plan = Path(self.plan)

        if isinstance(self.scripts, (str, Path)):
            self.scripts = [Path(self.scripts)]
        else:
            self.scripts = [Path(s) if isinstance(s, str) else s for s in self.scripts]

        if self.debug_config and isinstance(self.debug_config, str):
            self.debug_config = Path(self.debug_config)

        if self.work_dir and isinstance(self.work_dir, str):
            self.work_dir = Path(self.work_dir)

        # Validate required files exist
        if not self.plan.exists():
            raise ConfigurationError(f"Plan file does not exist: {self.plan}")

        for script in self.scripts:
            if not script.exists():
                raise ConfigurationError(f"Script file does not exist: {script}")

        # Validate plan extension
        if self.plan.suffix not in ['.ple', '.plx']:
            raise ConfigurationError(
                f"Plan file must have .ple or .plx extension, got: {self.plan.suffix}"
            )

        # Validate script extensions
        for script in self.scripts:
            if script.suffix not in ['.pst', '.psx']:
                raise ConfigurationError(
                    f"Script file must have .pst or .psx extension, got: {script.suffix}"
                )

        # Validate at least one script
        if not self.scripts:
            raise ConfigurationError("At least one script file is required")

        # Validate timeouts
        if self.compilation_timeout <= 0:
            raise ConfigurationError(
                f"Compilation timeout must be positive, got: {self.compilation_timeout}"
            )

        if self.execution_timeout <= 0:
            raise ConfigurationError(
                f"Execution timeout must be positive, got: {self.execution_timeout}"
            )

        # Validate debug config exists if provided
        if self.debug_config and not self.debug_config.exists():
            raise ConfigurationError(
                f"Debug config file does not exist: {self.debug_config}"
            )

        # Validate work_dir doesn't exist if provided
        if self.work_dir and self.work_dir.exists():
            raise ConfigurationError(
                f"Working directory already exists: {self.work_dir}"
            )

    def validate_tools(self) -> None:
        """Validate that all tool executables exist and are executable.

        This is called when --failproof flag is enabled.

        Raises:
            ValidationError: If any tool is not found or not executable.
        """
        tools = {
            'plexilc': self.plexilc,
            'plexiltest': self.plexiltest,
            'plexilv-run': self.plexilv_run,
            'plexilog-diff': self.plexilog_diff,
        }

        for name, path in tools.items():
            # Check if tool is in PATH
            tool_path = shutil.which(path)
            if tool_path is None:
                raise ValidationError(
                    f"Tool '{name}' not found: {path}\n"
                    f"Please ensure it is installed and in your PATH, "
                    f"or specify the full path using --{name.lower()} option."
                )

    def needs_plan_compilation(self) -> bool:
        """Check if the plan file needs compilation (.ple -> .plx)."""
        return self.plan.suffix == '.ple'

    def needs_script_compilation(self, script: Path) -> bool:
        """Check if a script file needs compilation (.pst -> .psx)."""
        return script.suffix == '.pst'

    def get_plan_name(self) -> str:
        """Get the plan base name without extension."""
        return self.plan.stem

    def parse_tool_flags(self, flags: str) -> List[str]:
        """Parse tool flags string into list of arguments.

        Args:
            flags: Space-separated flags string (e.g., "--flag1 --flag2 value")

        Returns:
            List of flag arguments
        """
        if not flags or not flags.strip():
            return []
        return flags.split()