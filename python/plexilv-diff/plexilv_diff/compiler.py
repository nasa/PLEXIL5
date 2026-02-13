"""PLEXIL compilation management."""

from pathlib import Path
from typing import List, Optional
from plumbum import local
from plumbum.commands.processes import ProcessTimedOut

from .exceptions import CompilationError, TimeoutError as DiffPlexilvTimeoutError


class Compiler:
    """Handles PLEXIL compilation using plexilc.

    Compiles .ple files to .plx (plans) and .pst files to .psx (scripts).

    Attributes:
        plexilc_path: Path to the plexilc executable
        timeout: Compilation timeout in seconds
        additional_flags: Additional flags to pass to plexilc
        output_log: Path to capture stdout/stderr output
    """

    def __init__(
        self,
        plexilc_path: str = "plexilc",
        timeout: int = 30,
        additional_flags: str = "",
        output_log: Optional[Path] = None
    ):
        """Initialize compiler.

        Args:
            plexilc_path: Path to plexilc executable
            timeout: Compilation timeout in seconds
            additional_flags: Space-separated additional flags
            output_log: Path to file for capturing stdout/stderr
        """
        self.plexilc_path = plexilc_path
        self.plexilc = local[plexilc_path]
        self.timeout = timeout
        self.additional_flags = self._parse_flags(additional_flags)
        self.output_log = output_log

    def _parse_flags(self, flags: str) -> List[str]:
        """Parse space-separated flags into list.

        Args:
            flags: Space-separated flags string

        Returns:
            List of flag strings
        """
        if not flags or not flags.strip():
            return []
        return flags.split()

    def _log_output(self, stdout: str, stderr: str) -> None:
        """Log stdout and stderr to output log file.

        Args:
            stdout: Standard output content
            stderr: Standard error content
        """
        if not self.output_log:
            return

        try:
            with open(self.output_log, 'a') as f:
                if stdout:
                    f.write("=== STDOUT ===\n")
                    f.write(stdout)
                    f.write("\n")
                if stderr:
                    f.write("=== STDERR ===\n")
                    f.write(stderr)
                    f.write("\n")
                f.write("=" * 50 + "\n")
        except (PermissionError, OSError):
            # Silently fail if we can't write to log
            # Don't want logging failures to break compilation
            pass

    def compile_file(
        self,
        input_file: Path,
        working_dir: Optional[Path] = None
    ) -> Path:
        """Compile a PLEXIL file (.ple or .pst).

        Args:
            input_file: Path to input file (.ple or .pst)
            working_dir: Directory to run plexilc from. If None, uses input file's directory.

        Returns:
            Path to the compiled output file (.plx or .psx)

        Raises:
            CompilationError: If compilation fails
            TimeoutError: If compilation times out
        """
        if not input_file.exists():
            raise CompilationError(
                f"Input file does not exist: {input_file}",
                file_path=str(input_file)
            )

        # Determine output file extension
        if input_file.suffix == '.ple':
            output_ext = '.plx'
        elif input_file.suffix == '.pst':
            output_ext = '.psx'
        else:
            raise CompilationError(
                f"Invalid input file extension: {input_file.suffix}. Expected .ple or .pst",
                file_path=str(input_file)
            )

        output_file = input_file.with_suffix(output_ext)

        # Determine working directory
        if working_dir is None:
            working_dir = input_file.parent

        # Build command arguments
        args = self.additional_flags + [str(input_file)]

        try:
            # Run plexilc from the working directory
            with local.cwd(working_dir):
                retcode, stdout, stderr = self.plexilc[args].run(
                    timeout=self.timeout,
                    retcode=None  # Don't raise on non-zero exit
                )

            # Log output
            self._log_output(stdout, stderr)

            # Check for compilation failure
            if retcode != 0:
                raise CompilationError(
                    f"Compilation failed with exit code {retcode}: {input_file.name}",
                    file_path=str(input_file),
                    stderr=stderr
                )

            # Verify output file was created
            if not output_file.exists():
                raise CompilationError(
                    f"Compilation succeeded but output file not created: {output_file}",
                    file_path=str(input_file),
                    stderr=stderr
                )

            return output_file

        except ProcessTimedOut:
            raise DiffPlexilvTimeoutError(
                f"Compilation timed out after {self.timeout}s: {input_file.name}",
                timeout_seconds=self.timeout
            )
        except CompilationError:
            raise
        except DiffPlexilvTimeoutError:
            raise
        except Exception as e:
            raise CompilationError(
                f"Unexpected error during compilation: {input_file.name}\n{e}",
                file_path=str(input_file)
            ) from e

    def compile_plan(
        self,
        plan_file: Path,
        workspace_dir: Path
    ) -> Path:
        """Compile a PLEXIL plan file (.ple -> .plx).

        This is a convenience wrapper around compile_file() for plans.
        Plans are compiled from the workspace directory.

        Args:
            plan_file: Path to plan file (.ple or .plx)
            workspace_dir: Workspace directory to run compilation from

        Returns:
            Path to compiled .plx file (or original if already .plx)

        Raises:
            CompilationError: If compilation fails
            TimeoutError: If compilation times out
        """
        if plan_file.suffix == '.plx':
            # Already compiled
            return plan_file

        if plan_file.suffix != '.ple':
            raise CompilationError(
                f"Invalid plan file extension: {plan_file.suffix}. Expected .ple or .plx",
                file_path=str(plan_file)
            )

        return self.compile_file(plan_file, working_dir=workspace_dir)

    def compile_script(
        self,
        script_file: Path,
        test_dir: Path
    ) -> Path:
        """Compile a PLEXIL script file (.pst -> .psx).

        This is a convenience wrapper around compile_file() for scripts.
        Scripts are compiled from their test directory.

        Args:
            script_file: Path to script file (.pst or .psx)
            test_dir: Test directory to run compilation from

        Returns:
            Path to compiled .psx file (or original if already .psx)

        Raises:
            CompilationError: If compilation fails
            TimeoutError: If compilation times out
        """
        if script_file.suffix == '.psx':
            # Already compiled
            return script_file

        if script_file.suffix != '.pst':
            raise CompilationError(
                f"Invalid script file extension: {script_file.suffix}. Expected .pst or .psx",
                file_path=str(script_file)
            )

        return self.compile_file(script_file, working_dir=test_dir)