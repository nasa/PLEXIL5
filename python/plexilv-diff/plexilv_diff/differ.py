"""PLEXIL log comparison management."""

from pathlib import Path
from typing import Optional
from dataclasses import dataclass
from plumbum import local
from plumbum.commands.processes import ProcessTimedOut

from .exceptions import DiffError, TimeoutError as DiffPlexilvTimeoutError


@dataclass
class DiffResult:
    """Result of comparing two PLEXIL logs.

    Attributes:
        diff_file: Path to the file containing diff output
        identical: Whether logs are identical (exit code 0)
        differ: Whether logs differ (exit code 1)
        error: Whether diff tool encountered an error (exit code 2+)
        returncode: Exit code from plexilog-diff
        output: Diff output content
    """
    diff_file: Path
    identical: bool
    differ: bool
    error: bool
    returncode: int
    output: str


class Differ:
    """Handles PLEXIL log comparison using plexilog-diff.

    Compares logs from plexiltest and plexilv interpreters.

    Attributes:
        plexilog_diff_path: Path to plexilog-diff executable
        additional_flags: Additional flags to pass to plexilog-diff
        timeout: Timeout for diff operation in seconds
    """

    def __init__(
        self,
        plexilog_diff_path: str = "plexilog-diff",
        additional_flags: str = "",
        timeout: int = 60
    ):
        """Initialize differ.

        Args:
            plexilog_diff_path: Path to plexilog-diff executable
            additional_flags: Space-separated additional flags
            timeout: Timeout for diff operation in seconds
        """
        self.plexilog_diff_path = plexilog_diff_path
        self.plexilog_diff = local[plexilog_diff_path]
        self.additional_flags = self._parse_flags(additional_flags)
        self.timeout = timeout

    def _parse_flags(self, flags: str) -> list[str]:
        """Parse space-separated flags into list.

        Args:
            flags: Space-separated flags string

        Returns:
            List of flag strings
        """
        if not flags or not flags.strip():
            return []
        return flags.split()

    def compare_logs(
        self,
        log1: Path,
        log2: Path,
        output_file: Path
    ) -> DiffResult:
        """Compare two PLEXIL log files.

        Args:
            log1: Path to first log file (typically plexiltest output)
            log2: Path to second log file (typically plexilv output)
            output_file: Path to write diff output

        Returns:
            DiffResult with comparison status and output

        Raises:
            DiffError: If diff tool encounters an error (exit code 2+)
            TimeoutError: If diff operation times out
        """
        if not log1.exists():
            raise DiffError(f"First log file does not exist: {log1}")

        if not log2.exists():
            raise DiffError(f"Second log file does not exist: {log2}")

        # Build command: plexilog-diff <log1> <log2>
        args = self.additional_flags + [str(log1), str(log2)]

        try:
            # Run plexilog-diff
            retcode, stdout, stderr = self.plexilog_diff[args].run(
                timeout=self.timeout,
                retcode=None  # Don't raise on non-zero exit
            )

            # Write output to file
            try:
                output_file.write_text(stdout)
            except (PermissionError, OSError) as e:
                raise DiffError(
                    f"Failed to write diff output to {output_file}",
                    stderr=str(e)
                ) from e

            # Interpret exit codes:
            # 0 = logs are identical
            # 1 = logs differ
            # 2+ = error occurred
            if retcode >= 2:
                raise DiffError(
                    f"plexilog-diff encountered an error (exit code {retcode})",
                    stderr=stderr
                )

            return DiffResult(
                diff_file=output_file,
                identical=(retcode == 0),
                differ=(retcode == 1),
                error=False,
                returncode=retcode,
                output=stdout
            )

        except ProcessTimedOut:
            # Try to write a note about the timeout
            try:
                output_file.write_text(f"DIFF OPERATION TIMED OUT after {self.timeout}s\n")
            except:
                pass

            raise DiffPlexilvTimeoutError(
                f"plexilog-diff timed out after {self.timeout}s",
                timeout_seconds=self.timeout
            )
        except DiffError:
            raise
        except DiffPlexilvTimeoutError:
            raise
        except Exception as e:
            raise DiffError(
                f"Unexpected error running plexilog-diff: {e}"
            ) from e