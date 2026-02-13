"""Main pipeline orchestration for plexilv-diff."""

from pathlib import Path
from typing import List, Tuple, Optional, Callable
from dataclasses import dataclass, field

from .config import Config
from .workspace import Workspace
from .compiler import Compiler
from .executor import Executor, ExecutionResult
from .differ import Differ, DiffResult
from .exceptions import (
    CompilationError,
    ExecutionError,
    DiffError,
    TimeoutError as DiffPlexilvTimeoutError,
    WorkspaceError
)


@dataclass
class TestResult:
    """Result of running a single test.

    Attributes:
        test_number: Test number (1-based)
        test_name: Original test script filename
        compiled_script: Path to compiled script in workspace
        plexiltest_result: Result from plexiltest execution (None if failed)
        plexilv_run_result: Result from plexilv-run execution (None if failed)
        diff_result: Result from log comparison (None if not compared)
        success: Whether test completed successfully
        error_message: Error message if test failed
    """
    __test__ = False  # Tell pytest this is not a test class

    test_number: int
    test_name: str
    compiled_script: Optional[Path] = None
    plexiltest_result: Optional[ExecutionResult] = None
    plexilv_result: Optional[ExecutionResult] = None
    diff_result: Optional[DiffResult] = None
    success: bool = False
    error_message: Optional[str] = None


@dataclass
class PipelineResult:
    """Overall result of pipeline execution.

    Attributes:
        workspace_path: Path to workspace directory
        compilation_success: Whether compilation phase succeeded
        compilation_errors: List of compilation error messages
        test_results: List of TestResult for each test
        summary: Human-readable summary text
        exit_code: Exit code (0=success no diffs, 1=success with diffs, 2=errors)
    """
    workspace_path: Path
    compilation_success: bool = True
    compilation_errors: List[str] = field(default_factory=list)
    test_results: List[TestResult] = field(default_factory=list)
    summary: str = ""
    exit_code: int = 0


# Type alias for progress callback
# Signature: callback(phase: str, current: int, total: int, description: str)
ProgressCallback = Callable[[str, int, int, str], None]


class Pipeline:
    """Main orchestration pipeline for plexilv-diff.

    Coordinates workspace setup, compilation, execution, and diffing.
    """

    def __init__(self, config: Config):
        """Initialize pipeline with configuration.

        Args:
            config: Application configuration
        """
        self.config = config
        self.workspace: Optional[Workspace] = None
        self.compiler: Optional[Compiler] = None
        self.executor: Optional[Executor] = None
        self.differ: Optional[Differ] = None

    def run(self, progress_callback: Optional[ProgressCallback] = None) -> PipelineResult:
        """Run the complete pipeline.

        Args:
            progress_callback: Optional callback for progress reporting.
                             Called with (phase, current, total, description)

        Returns:
            PipelineResult with complete execution results
        """
        result = PipelineResult(workspace_path=Path())

        try:
            # Calculate total steps for progress
            num_compile = sum([
                1 if self.config.needs_plan_compilation() else 0
            ] + [
                1 if self.config.needs_script_compilation(s) else 0
                for s in self.config.scripts
            ])

            total_steps = 1 + num_compile + len(self.config.scripts) * 2
            current_step = 0

            # Step 1: Setup workspace
            if progress_callback:
                progress_callback('setup', current_step, total_steps, 'Setting up workspace...')

            self._setup_workspace()
            result.workspace_path = self.workspace.path
            current_step += 1

            if progress_callback:
                progress_callback('setup', current_step, total_steps, f'Workspace created: {self.workspace.path}')

            # Step 2: Validate tools if --failproof enabled
            if self.config.failproof:
                if progress_callback:
                    progress_callback('validate', current_step, total_steps, 'Validating tools...')
                self.config.validate_tools()

            # Step 3: Initialize components
            if progress_callback:
                progress_callback('init', current_step, total_steps, 'Initializing components...')

            self._initialize_components()

            # Step 4: Compile plan and scripts
            if progress_callback:
                progress_callback('compile', current_step, total_steps, 'Compiling files...')

            compiled_plan, compilation_errors = self._compile_all()
            current_step += num_compile

            if progress_callback:
                if compilation_errors:
                    progress_callback('compile', current_step, total_steps, 'Compilation failed')
                else:
                    progress_callback('compile', current_step, total_steps, 'Compilation successful')

            if compilation_errors:
                result.compilation_success = False
                result.compilation_errors = compilation_errors
                result.exit_code = 2
                result.summary = self._generate_compilation_error_summary(compilation_errors)
                self.workspace.write_summary(result.summary)
                return result

            result.compilation_success = True

            # Step 5: Execute tests
            if progress_callback:
                progress_callback('execute', current_step, total_steps, 'Executing tests...')

            result.test_results = self._execute_tests(compiled_plan, progress_callback, current_step, total_steps)
            current_step += len(self.config.scripts) * 2

            # Step 6: Generate summary
            if progress_callback:
                progress_callback('summary', current_step, total_steps, 'Generating summary...')

            result.summary = self._generate_summary(result.test_results)
            self.workspace.write_summary(result.summary)

            # Step 7: Determine exit code
            result.exit_code = self._determine_exit_code(result.test_results)

            if progress_callback:
                progress_callback('complete', total_steps, total_steps, 'Pipeline complete')

            return result

        except WorkspaceError as e:
            # Workspace creation failed
            result.exit_code = 2
            result.summary = f"Workspace error: {e}"
            return result

        except Exception as e:
            # Unexpected error
            result.exit_code = 2
            result.summary = f"Unexpected error: {e}"
            if self.workspace:
                self.workspace.write_summary(result.summary)
            return result

    def _setup_workspace(self) -> None:
        """Create and setup workspace directory."""
        self.workspace = Workspace(work_dir=self.config.work_dir)

        # Create debug.cfg
        self.workspace.create_debug_config()

        # Copy plan file to workspace
        self.workspace.copy_file(self.config.plan)

    def _initialize_components(self) -> None:
        """Initialize compiler, executor, and differ components."""
        # Setup compiler
        self.compiler = Compiler(
            plexilc_path=self.config.plexilc,
            timeout=self.config.compilation_timeout,
            additional_flags=self.config.plexilc_flags,
            output_log=self.workspace.path / "plexilc.output.log"
        )

        # Setup executor
        self.executor = Executor(
            plexiltest_path=self.config.plexiltest,
            plexilv_run_path=self.config.plexilv_run,
            execution_timeout=self.config.execution_timeout,
            plexiltest_flags=self.config.plexiltest_flags,
            plexilv_run_flags=self.config.plexilv_run_flags
        )

        # Setup differ
        self.differ = Differ(
            plexilog_diff_path=self.config.plexilog_diff,
            additional_flags=self.config.plexilog_diff_flags,
            timeout=60  # Default 60 second timeout for diff
        )

    def _compile_all(self) -> Tuple[Path, List[str]]:
        """Compile plan and all scripts.

        Returns:
            Tuple of (compiled_plan_path, list_of_error_messages)
        """
        errors = []

        # Compile plan
        plan_in_workspace = self.workspace.path / self.config.plan.name
        try:
            compiled_plan = self.compiler.compile_plan(
                plan_in_workspace,
                self.workspace.path
            )
        except (CompilationError, DiffPlexilvTimeoutError) as e:
            errors.append(f"Plan compilation failed: {self.config.plan.name}\n  {e}")
            return None, errors

        # Compile each script
        for i, script in enumerate(self.config.scripts, start=1):
            try:
                # Create test directory
                test_dir = self.workspace.create_test_directory(i)

                # Copy script to test directory
                script_in_test = self.workspace.copy_file_to_test_dir(script, i)

                # Compile if needed
                self.compiler.compile_script(script_in_test, test_dir)

            except (CompilationError, DiffPlexilvTimeoutError) as e:
                errors.append(f"Script compilation failed: {script.name}\n  {e}")
            except WorkspaceError as e:
                errors.append(f"Workspace error for {script.name}: {e}")

        if errors:
            return None, errors

        return compiled_plan, []

    def _execute_tests(
        self,
        compiled_plan: Path,
        progress_callback: Optional[ProgressCallback] = None,
        base_step: int = 0,
        total_steps: int = 0
    ) -> List[TestResult]:
        """Execute all tests.

        Args:
            compiled_plan: Path to compiled plan file
            progress_callback: Optional progress callback
            base_step: Current step number (for progress)
            total_steps: Total steps (for progress)

        Returns:
            List of TestResult for each test
        """
        test_results = []
        debug_config = self.workspace.path / "debug.cfg"
        plan_name = self.config.get_plan_name()

        for i, script in enumerate(self.config.scripts, start=1):
            if progress_callback:
                progress_callback(
                    'execute',
                    base_step + (i - 1) * 2,
                    total_steps,
                    f'Running test {i}/{len(self.config.scripts)}: {script.name}'
                )

            test_result = TestResult(
                test_number=i,
                test_name=str(script)
            )

            test_dir = self.workspace.get_test_directory(i)

            # Get compiled script path
            if script.suffix == '.pst':
                compiled_script = test_dir / script.with_suffix('.psx').name
            else:
                compiled_script = test_dir / script.name

            test_result.compiled_script = compiled_script

            try:
                # Run both interpreters
                plexiltest_result, plexilv_result = self.executor.run_both(
                    compiled_plan,
                    compiled_script,
                    debug_config,
                    test_dir,
                    plan_name,
                    parallel=True
                )

                test_result.plexiltest_result = plexiltest_result
                test_result.plexilv_result = plexilv_result

                # Check if both succeeded
                if not plexiltest_result.success:
                    test_result.success = False
                    test_result.error_message = f"plexiltest failed with exit code {plexiltest_result.returncode}"
                    test_results.append(test_result)

                    # Check fail-fast
                    if self.config.fail_fast:
                        break
                    continue

                if not plexilv_result.success:
                    test_result.success = False
                    test_result.error_message = f"plexilv failed with exit code {plexilv_result.returncode}"
                    test_results.append(test_result)

                    # Check fail-fast
                    if self.config.fail_fast:
                        break
                    continue

                # Both succeeded, compare logs
                if progress_callback:
                    progress_callback(
                        'diff',
                        base_step + (i - 1) * 2 + 1,
                        total_steps,
                        f'Comparing logs for test {i}'
                    )

                try:
                    diff_output_file = test_dir / f"{plan_name}.log.diff"
                    diff_result = self.differ.compare_logs(
                        plexiltest_result.log_file,
                        plexilv_result.log_file,
                        diff_output_file
                    )

                    test_result.diff_result = diff_result
                    test_result.success = True

                except (DiffError, DiffPlexilvTimeoutError) as e:
                    test_result.success = False
                    test_result.error_message = f"Diff failed: {e}"

            except (ExecutionError, DiffPlexilvTimeoutError) as e:
                test_result.success = False
                test_result.error_message = f"Execution error: {e}"

                # Check fail-fast
                if self.config.fail_fast:
                    test_results.append(test_result)
                    break

            test_results.append(test_result)

        return test_results

    def _generate_compilation_error_summary(self, errors: List[str]) -> str:
        """Generate summary for compilation errors.

        Args:
            errors: List of error messages

        Returns:
            Formatted summary text
        """
        lines = []
        lines.append("=" * 70)
        lines.append("COMPILATION FAILED")
        lines.append("=" * 70)
        lines.append("")

        for error in errors:
            lines.append(f"✗ {error}")
            lines.append("")

        lines.append(f"Total errors: {len(errors)}")
        lines.append("")
        lines.append("Execution aborted due to compilation failures.")
        lines.append("=" * 70)

        return "\n".join(lines)

    def _generate_summary(self, test_results: List[TestResult]) -> str:
        """Generate summary report.

        Args:
            test_results: List of test results

        Returns:
            Formatted summary text
        """
        lines = []
        lines.append("=" * 70)
        lines.append("DIFF-PLEXILV SUMMARY")
        lines.append("=" * 70)
        lines.append("")

        # Count statistics
        total_tests = len(test_results)
        successful_tests = sum(1 for r in test_results if r.success)
        failed_tests = total_tests - successful_tests

        # Count diffs
        tests_with_diffs = sum(
            1 for r in test_results
            if r.success and r.diff_result and r.diff_result.differ
        )
        tests_identical = sum(
            1 for r in test_results
            if r.success and r.diff_result and r.diff_result.identical
        )

        lines.append(f"Total tests: {total_tests}")
        lines.append(f"Successful: {successful_tests}")
        lines.append(f"Failed: {failed_tests}")
        lines.append(f"Identical logs: {tests_identical}")
        lines.append(f"Different logs: {tests_with_diffs}")
        lines.append("")
        lines.append("-" * 70)
        lines.append("TEST DETAILS")
        lines.append("-" * 70)
        lines.append("")

        # List each test
        for result in test_results:
            if result.success:
                if result.diff_result:
                    if result.diff_result.identical:
                        status = "✓ PASS (no differences)"
                    else:
                        status = "✓ PASS (differences found)"
                else:
                    status = "✓ PASS"
            else:
                status = f"✗ FAIL: {result.error_message}"

            lines.append(f"Test {result.test_number:03d}: {result.test_name}")
            lines.append(f"  {status}")
            lines.append("")

        lines.append("=" * 70)

        return "\n".join(lines)

    def _determine_exit_code(self, test_results: List[TestResult]) -> int:
        """Determine exit code based on test results.

        Args:
            test_results: List of test results

        Returns:
            Exit code (0=success no diffs, 1=success with diffs, 2=errors)
        """
        # Check if any tests failed
        if any(not r.success for r in test_results):
            return 2

        # All tests succeeded - check for differences
        if any(r.diff_result and r.diff_result.differ for r in test_results):
            return 1

        # All tests succeeded with no differences
        return 0

    def cleanup(self, force: bool = False) -> None:
        """Cleanup workspace if configured.

        Args:
            force: If True, cleanup regardless of config
        """
        if not self.workspace:
            return

        should_cleanup = force or self.config.force_cleanup

        if not should_cleanup and self.config.cleanup:
            # Only cleanup on success (exit code 0 or 1)
            # This requires knowing the result, so caller must pass it
            pass

        if should_cleanup:
            self.workspace.cleanup()