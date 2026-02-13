"""Workspace management for plexilv-diff application."""

from pathlib import Path
from datetime import datetime
import shutil
from typing import Optional, List

from .exceptions import WorkspaceError


class Workspace:
    """Manages the temporary working directory for test execution.

    The workspace contains:
    - Compiled PLEXIL plan files at root
    - debug.cfg at root
    - Numbered subdirectories (001/, 002/, ...) for each test
    - Compilation logs
    - Summary report

    Attributes:
        path: Path to the workspace directory
        _auto_created: Whether the workspace was auto-generated
    """

    DEBUG_CONFIG_CONTENT = "PlexilExec:step\nPlexilExec:cycle\n"

    def __init__(self, work_dir: Optional[Path] = None):
        """Initialize workspace.

        Args:
            work_dir: Optional custom workspace path. If None, auto-generates
                     with timestamp format: diff-YYYY-MM-DD-HH-MM-SS

        Raises:
            WorkspaceError: If directory already exists or can't be created.
        """
        if work_dir:
            self.path = work_dir
            self._auto_created = False
        else:
            timestamp = datetime.now().strftime("%Y-%m-%d-%H-%M-%S")
            self.path = Path.cwd() / f"diff-{timestamp}"
            self._auto_created = True

        self._create()

    def _create(self) -> None:
        """Create the workspace directory.

        Raises:
            WorkspaceError: If directory already exists or can't be created.
        """
        if self.path.exists():
            raise WorkspaceError(
                f"Working directory already exists: {self.path}\n"
                f"Please remove it or specify a different directory with --work-dir"
            )

        try:
            self.path.mkdir(parents=True, exist_ok=False)
        except PermissionError as e:
            raise WorkspaceError(
                f"Permission denied creating workspace: {self.path}"
            ) from e
        except OSError as e:
            raise WorkspaceError(
                f"Failed to create workspace: {self.path}\n"
                f"Error: {e}"
            ) from e

    def copy_file(self, src: Path, dest_name: Optional[str] = None) -> Path:
        """Copy a file into the workspace root.

        Args:
            src: Source file path
            dest_name: Optional destination filename. If None, uses source filename.

        Returns:
            Path to the copied file in workspace

        Raises:
            WorkspaceError: If file doesn't exist or copy fails
        """
        if not src.exists():
            raise WorkspaceError(f"Source file does not exist: {src}")

        dest = self.path / (dest_name if dest_name else src.name)

        if dest.exists():
            raise WorkspaceError(
                f"Destination file already exists: {dest}\n"
                f"This shouldn't happen with a clean workspace."
            )

        try:
            shutil.copy2(src, dest)
        except (PermissionError, OSError) as e:
            raise WorkspaceError(
                f"Failed to copy {src} to {dest}\n"
                f"Error: {e}"
            ) from e

        return dest

    def create_test_directory(self, test_number: int) -> Path:
        """Create a numbered test subdirectory.

        Args:
            test_number: Test number (1-based, will be formatted as 001, 002, etc.)

        Returns:
            Path to the created test directory

        Raises:
            WorkspaceError: If directory already exists or can't be created
        """
        dir_name = f"{test_number:03d}"
        test_dir = self.path / dir_name

        if test_dir.exists():
            raise WorkspaceError(
                f"Test directory already exists: {test_dir}\n"
                f"This shouldn't happen with a clean workspace."
            )

        try:
            test_dir.mkdir(parents=False, exist_ok=False)
        except (PermissionError, OSError) as e:
            raise WorkspaceError(
                f"Failed to create test directory: {test_dir}\n"
                f"Error: {e}"
            ) from e

        return test_dir

    def copy_file_to_test_dir(self, src: Path, test_number: int,
                              dest_name: Optional[str] = None) -> Path:
        """Copy a file into a test subdirectory.

        Args:
            src: Source file path
            test_number: Test number (directory must already exist)
            dest_name: Optional destination filename

        Returns:
            Path to the copied file in test directory

        Raises:
            WorkspaceError: If test directory doesn't exist or copy fails
        """
        test_dir = self.path / f"{test_number:03d}"

        if not test_dir.exists():
            raise WorkspaceError(
                f"Test directory does not exist: {test_dir}\n"
                f"Call create_test_directory() first."
            )

        if not src.exists():
            raise WorkspaceError(f"Source file does not exist: {src}")

        dest = test_dir / (dest_name if dest_name else src.name)

        if dest.exists():
            raise WorkspaceError(
                f"Destination file already exists: {dest}\n"
                f"This shouldn't happen with a clean workspace."
            )

        try:
            shutil.copy2(src, dest)
        except (PermissionError, OSError) as e:
            raise WorkspaceError(
                f"Failed to copy {src} to {dest}\n"
                f"Error: {e}"
            ) from e

        return dest

    def create_debug_config(self) -> Path:
        """Create debug.cfg file in workspace root.

        Returns:
            Path to the created debug.cfg file

        Raises:
            WorkspaceError: If file can't be created
        """
        debug_cfg = self.path / "debug.cfg"

        if debug_cfg.exists():
            raise WorkspaceError(
                f"debug.cfg already exists: {debug_cfg}\n"
                f"This shouldn't happen with a clean workspace."
            )

        try:
            debug_cfg.write_text(self.DEBUG_CONFIG_CONTENT)
        except (PermissionError, OSError) as e:
            raise WorkspaceError(
                f"Failed to create debug.cfg: {debug_cfg}\n"
                f"Error: {e}"
            ) from e

        return debug_cfg

    def get_test_directory(self, test_number: int) -> Path:
        """Get path to a test subdirectory.

        Args:
            test_number: Test number

        Returns:
            Path to the test directory (may not exist yet)
        """
        return self.path / f"{test_number:03d}"

    def write_summary(self, content: str) -> Path:
        """Write summary report to workspace root.

        Args:
            content: Summary content to write

        Returns:
            Path to the created summary.txt file

        Raises:
            WorkspaceError: If file can't be written
        """
        summary_file = self.path / "summary.txt"

        try:
            summary_file.write_text(content)
        except (PermissionError, OSError) as e:
            raise WorkspaceError(
                f"Failed to write summary: {summary_file}\n"
                f"Error: {e}"
            ) from e

        return summary_file

    def cleanup(self) -> None:
        """Remove the workspace directory and all its contents.

        Raises:
            WorkspaceError: If cleanup fails
        """
        if not self.path.exists():
            return  # Already cleaned up

        try:
            shutil.rmtree(self.path)
        except (PermissionError, OSError) as e:
            raise WorkspaceError(
                f"Failed to cleanup workspace: {self.path}\n"
                f"Error: {e}\n"
                f"You may need to manually remove this directory."
            ) from e

    def __str__(self) -> str:
        """String representation of workspace."""
        return str(self.path)

    def __repr__(self) -> str:
        """Developer representation of workspace."""
        return f"Workspace(path={self.path}, auto_created={self._auto_created})"