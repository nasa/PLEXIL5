"""PLEXIL interpreter execution management."""

from pathlib import Path
from typing import Optional, Tuple
from dataclasses import dataclass
from concurrent.futures import ThreadPoolExecutor, as_completed
from plumbum import local
from plumbum.commands.processes import ProcessTimedOut

from .exceptions import ExecutionError, TimeoutError as DiffPlexilvTimeoutError


@dataclass
class ExecutionResult:
    """Result of running a PLEXIL interpreter.

    Attributes:
        log_file: Path to the log file containing stdout/stderr
        returncode: Exit code from the interpreter
        success: Whether execution succeeded (returncode == 0)
        stdout: Standard output (for debugging, not normally captured to file)
        stderr: Standard error (for debugging, not normally captured to file)
    """
    log_file: Path
    returncode: int
    success: bool
    stdout: str = ""
    stderr: str = ""


class Executor:
    """Handles PLEXIL interpreter execution.

    Runs plexiltest and plexilv-run interpreters with PLEXIL plans and test scripts.

    Attributes:
        plexiltest_path: Path to plexiltest executable
        plexilv_run_path: Path to plexilv-run executable
        execution_timeout: Timeout for execution in seconds
        plexiltest_flags: Additional flags for plexiltest
        plexilv_flags: Additional flags for plexilv
    """

    def __init__(
        self,
        plexiltest_path: str = "plexiltest",
        plexilv_run_path: str = "plexilv-run",
        execution_timeout: int = 300,
        plexiltest_flags: str = "",
        plexilv_run_flags: str = ""
    ):
        """Initialize executor.

        Args:
            plexiltest_path: Path to plexiltest executable
            plexilv_run_path: Path to plexilv-run executable
            execution_timeout: Execution timeout in seconds
            plexiltest_flags: Space-separated additional flags for plexiltest
            plexilv_run_flags: Space-separated additional flags for plexilv-run
        """
        self.plexiltest_path = plexiltest_path
        self.plexiltest = local[plexiltest_path]
        self.plexilv_run_path = plexilv_run_path
        self.plexilv_run = local[plexilv_run_path]
        self.execution_timeout = execution_timeout
        self.plexiltest_flags = self._parse_flags(plexiltest_flags)
        self.plexilv_run_flags = self._parse_flags(plexilv_run_flags)

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

    def run_plexiltest(
        self,
        plan_file: Path,
        script_file: Path,
        debug_config: Path,
        test_dir: Path,
        plan_name: str
    ) -> ExecutionResult:
        """Run plexiltest interpreter.

        Executes from test_dir and captures stdout/stderr to <plan_name>.plexil.log

        Args:
            plan_file: Path to compiled PLEXIL plan (.plx)
            script_file: Path to compiled script (.psx)
            debug_config: Path to debug.cfg file
            test_dir: Directory to execute from (test subdirectory)
            plan_name: Base name of plan (for log filename)

        Returns:
            ExecutionResult with log file path and status

        Raises:
            ExecutionError: If execution fails unexpectedly
            TimeoutError: If execution times out
        """
        log_file = test_dir / f"{plan_name}.plexil.log"

        # Build command: plexiltest -p <plan> -s <script> -d <debug>
        args = self.plexiltest_flags + [
            "-p", str(plan_file),
            "-s", str(script_file),
            "-d", str(debug_config)
        ]

        try:
            # Run from test directory
            with local.cwd(test_dir):
                retcode, stdout, stderr = self.plexiltest[args].run(
                    timeout=self.execution_timeout,
                    retcode=None  # Don't raise on non-zero exit
                )

            # Write stdout and stderr to log file
            try:
                with open(log_file, 'w') as f:
                    f.write(stdout)
                    if stderr:
                        f.write("\n=== STDERR ===\n")
                        f.write(stderr)
            except (PermissionError, OSError) as e:
                raise ExecutionError(
                    f"Failed to write log file: {log_file}",
                    interpreter="plexiltest",
                    stderr=str(e)
                ) from e

            return ExecutionResult(
                log_file=log_file,
                returncode=retcode,
                success=(retcode == 0),
                stdout=stdout,
                stderr=stderr
            )

        except ProcessTimedOut:
            # Still try to write a log file noting the timeout
            try:
                log_file.write_text(f"EXECUTION TIMED OUT after {self.execution_timeout}s\n")
            except:
                pass

            raise DiffPlexilvTimeoutError(
                f"plexiltest execution timed out after {self.execution_timeout}s",
                timeout_seconds=self.execution_timeout
            )
        except ExecutionError:
            raise
        except DiffPlexilvTimeoutError:
            raise
        except Exception as e:
            raise ExecutionError(
                f"Unexpected error running plexiltest: {e}",
                interpreter="plexiltest"
            ) from e

    def run_plexilv(
        self,
        plan_file: Path,
        script_file: Path,
        test_dir: Path,
        plan_name: str
    ) -> ExecutionResult:
        """Run plexilv interpreter.

        Executes from test_dir and captures stdout/stderr to <plan_name>.plexilv.log

        Args:
            plan_file: Path to compiled PLEXIL plan (.plx)
            script_file: Path to compiled script (.psx)
            test_dir: Directory to execute from (test subdirectory)
            plan_name: Base name of plan (for log filename)

        Returns:
            ExecutionResult with log file path and status

        Raises:
            ExecutionError: If execution fails unexpectedly
            TimeoutError: If execution times out
        """
        log_file = test_dir / f"{plan_name}.plexilv.log"

        # Build command: plexilv -p <plan> -s <script>
        args = self.plexilv_run_flags + [
            "-p", str(plan_file),
            "-s", str(script_file)
        ]

        try:
            # Run from test directory
            with local.cwd(test_dir):
                retcode, stdout, stderr = self.plexilv_run[args].run(
                    timeout=self.execution_timeout,
                    retcode=None  # Don't raise on non-zero exit
                )

            # Write stdout and stderr to log file
            try:
                with open(log_file, 'w') as f:
                    f.write(stdout)
                    if stderr:
                        f.write("\n=== STDERR ===\n")
                        f.write(stderr)
            except (PermissionError, OSError) as e:
                raise ExecutionError(
                    f"Failed to write log file: {log_file}",
                    interpreter="plexilv",
                    stderr=str(e)
                ) from e

            return ExecutionResult(
                log_file=log_file,
                returncode=retcode,
                success=(retcode == 0),
                stdout=stdout,
                stderr=stderr
            )

        except ProcessTimedOut:
            # Still try to write a log file noting the timeout
            try:
                log_file.write_text(f"EXECUTION TIMED OUT after {self.execution_timeout}s\n")
            except:
                pass

            raise DiffPlexilvTimeoutError(
                f"plexilv execution timed out after {self.execution_timeout}s",
                timeout_seconds=self.execution_timeout
            )
        except ExecutionError:
            raise
        except DiffPlexilvTimeoutError:
            raise
        except Exception as e:
            raise ExecutionError(
                f"Unexpected error running plexilv: {e}",
                interpreter="plexilv"
            ) from e

    def run_both(
        self,
        plan_file: Path,
        script_file: Path,
        debug_config: Path,
        test_dir: Path,
        plan_name: str,
        parallel: bool = True
    ) -> Tuple[ExecutionResult, ExecutionResult]:
        """Run both plexiltest and plexilv interpreters.

        Args:
            plan_file: Path to compiled PLEXIL plan (.plx)
            script_file: Path to compiled script (.psx)
            debug_config: Path to debug.cfg file
            test_dir: Directory to execute from (test subdirectory)
            plan_name: Base name of plan (for log filenames)
            parallel: If True, run both interpreters in parallel

        Returns:
            Tuple of (plexiltest_result, plexilv_result)

        Raises:
            ExecutionError: If execution fails unexpectedly
            TimeoutError: If execution times out
        """
        if parallel:
            # Run both interpreters in parallel
            with ThreadPoolExecutor(max_workers=2) as executor:
                future_plexiltest = executor.submit(
                    self.run_plexiltest,
                    plan_file, script_file, debug_config, test_dir, plan_name
                )
                future_plexilv = executor.submit(
                    self.run_plexilv,
                    plan_file, script_file, test_dir, plan_name
                )

                # Wait for both to complete
                plexiltest_result = future_plexiltest.result()
                plexilv_result = future_plexilv.result()
        else:
            # Run sequentially
            plexiltest_result = self.run_plexiltest(
                plan_file, script_file, debug_config, test_dir, plan_name
            )
            plexilv_result = self.run_plexilv(
                plan_file, script_file, test_dir, plan_name
            )

        return plexiltest_result, plexilv_result