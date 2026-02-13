"""Command-line interface for plexilv-diff."""

import sys
from pathlib import Path
import click
from rich.console import Console
from rich.progress import Progress, SpinnerColumn, TextColumn, BarColumn, TaskProgressColumn

from .config import Config
from .pipeline import Pipeline, PipelineResult, TestResult
from .exceptions import (
    ConfigurationError,
    ValidationError,
    WorkspaceError,
    DiffPlexilvError
)

console = Console()


@click.command()
@click.option(
    '-p', '--plan',
    required=True,
    type=click.Path(exists=True, path_type=Path),
    help='Path to PLEXIL plan file (.ple or .plx)'
)
@click.option(
    '-s', '--script',
    'scripts',
    required=True,
    multiple=True,
    type=click.Path(exists=True, path_type=Path),
    help='Path to test script file (.pst or .psx) [multiple allowed]'
)
@click.option(
    '--plexilc',
    default='plexilc',
    help='Path to plexilc compiler [default: plexilc]'
)
@click.option(
    '--plexiltest',
    default='plexiltest',
    help='Path to plexiltest executable [default: plexiltest]'
)
@click.option(
    '--plexilv-run',
    default='plexilv-run',
    help='Path to plexilv-run executable [default: plexilv-run]'
)
@click.option(
    '--plexilog-diff',
    default='plexilog-diff',
    help='Path to plexilog-diff executable [default: plexilog-diff]'
)
@click.option(
    '--plexilc-flags',
    default='',
    help='Additional flags for plexilc'
)
@click.option(
    '--plexiltest-flags',
    default='',
    help='Additional flags for plexiltest'
)
@click.option(
    '--plexilv-run-flags',
    default='',
    help='Additional flags for plexilv-run'
)
@click.option(
    '--plexilog-diff-flags',
    default='',
    help='Additional flags for plexilog-diff'
)
@click.option(
    '--debug-config',
    type=click.Path(exists=True, path_type=Path),
    help='Custom debug config file [default: auto-generated]'
)
@click.option(
    '--work-dir',
    type=click.Path(path_type=Path),
    help='Working directory [default: auto-generated with timestamp]'
)
@click.option(
    '--compilation-timeout',
    default=30,
    type=int,
    help='Compilation timeout in seconds [default: 30]'
)
@click.option(
    '--execution-timeout',
    default=300,
    type=int,
    help='Test execution timeout in seconds [default: 300]'
)
@click.option(
    '--cleanup',
    is_flag=True,
    help='Delete workspace on success only'
)
@click.option(
    '--force-cleanup',
    is_flag=True,
    help='Delete workspace always (success or failure)'
)
@click.option(
    '--fail-fast',
    is_flag=True,
    help='Stop on first test failure'
)
@click.option(
    '--failproof',
    is_flag=True,
    help='Validate all tools before starting'
)
@click.option(
    '--quiet',
    is_flag=True,
    help='Minimal output (only workspace location)'
)
def main(
    plan,
    scripts,
    plexilc,
    plexiltest,
    plexilv_run,
    plexilog_diff,
    plexilc_flags,
    plexiltest_flags,
    plexilv_run_flags,
    plexilog_diff_flags,
    debug_config,
    work_dir,
    compilation_timeout,
    execution_timeout,
    cleanup,
    force_cleanup,
    fail_fast,
    failproof,
    quiet
):
    """Compare PLEXIL interpreter outputs across multiple test scripts.

    plexilv-diff runs both plexiltest and plexilv interpreters on the same
    PLEXIL plan with multiple test scripts, then compares their outputs using
    plexilog-diff.

    Examples:

        # Basic usage
        plexilv-diff -p plan.ple -s test1.pst -s test2.pst

        # With custom executables
        plexilv-diff -p plan.ple -s test.pst --plexilv-run /path/to/plexilv-run

        # Cleanup workspace on success
        plexilv-diff -p plan.ple -s test.pst --cleanup
    """
    try:
        # Create configuration
        config = Config(
            plan=plan,
            scripts=list(scripts),
            plexilc=plexilc,
            plexiltest=plexiltest,
            plexilv_run=plexilv_run,
            plexilog_diff=plexilog_diff,
            plexilc_flags=plexilc_flags,
            plexiltest_flags=plexiltest_flags,
            plexilv_run_flags=plexilv_run_flags,
            plexilog_diff_flags=plexilog_diff_flags,
            debug_config=debug_config,
            work_dir=work_dir,
            compilation_timeout=compilation_timeout,
            execution_timeout=execution_timeout,
            cleanup=cleanup,
            force_cleanup=force_cleanup,
            fail_fast=fail_fast,
            failproof=failproof,
            quiet=quiet
        )

    except ConfigurationError as e:
        console.print(f"[bold red]Configuration Error:[/bold red] {e}")
        sys.exit(2)

    # Run pipeline
    if quiet:
        result, pipeline = _run_quiet(config)
    else:
        result, pipeline = _run_verbose(config)

    # Handle cleanup
    if config.force_cleanup:
        if pipeline and pipeline.workspace:
            pipeline.workspace.cleanup()
    elif config.cleanup and result.exit_code in [0, 1]:
        # Only cleanup on success (no execution errors)
        if pipeline and pipeline.workspace:
            pipeline.workspace.cleanup()

    # Show workspace location or cleanup notice
    if quiet:
        if result.workspace_path.exists():
            console.print(f"Workspace: {result.workspace_path}")
        else:
            console.print("Workspace was cleaned up")
    elif not result.workspace_path.exists():
        console.print("\n[dim]Workspace was cleaned up[/dim]")

    sys.exit(result.exit_code)


def _run_quiet(config: Config):
    """Run pipeline in quiet mode.

    Args:
        config: Application configuration

    Returns:
        Tuple of (PipelineResult, Pipeline)
    """
    try:
        pipeline = Pipeline(config)
        result = pipeline.run()

        # In quiet mode, only show workspace path (or cleanup notice)
        # Errors are always shown (even in quiet mode)
        if not result.compilation_success or result.exit_code == 2:
            console.print(f"[bold red]Error:[/bold red] Execution failed")
            if result.compilation_errors:
                for error in result.compilation_errors:
                    console.print(f"  {error}")

        return result, pipeline

    except ValidationError as e:
        console.print(f"[bold red]Validation Error:[/bold red] {e}")
        sys.exit(2)
    except WorkspaceError as e:
        console.print(f"[bold red]Workspace Error:[/bold red] {e}")
        sys.exit(2)
    except Exception as e:
        console.print(f"[bold red]Unexpected Error:[/bold red] {e}")
        sys.exit(2)


def _run_verbose(config: Config):
    """Run pipeline in verbose mode with progress bars.

    Args:
        config: Application configuration

    Returns:
        Tuple of (PipelineResult, Pipeline)
    """
    try:
        pipeline = Pipeline(config)

        # Calculate total steps
        num_files_to_compile = 0
        if config.needs_plan_compilation():
            num_files_to_compile += 1

        for script in config.scripts:
            if config.needs_script_compilation(script):
                num_files_to_compile += 1

        total_steps = (
            1 +  # Setup workspace
            num_files_to_compile +  # Compilation
            len(config.scripts) * 2  # Execution + Diffing per test
        )

        with Progress(
            SpinnerColumn(),
            TextColumn("[progress.description]{task.description}"),
            BarColumn(),
            TaskProgressColumn(),
            TextColumn("• {task.elapsed:.0f}s"),
            console=console
        ) as progress:
            overall_task = progress.add_task(
                "[cyan]Overall progress",
                total=total_steps
            )

            def progress_callback(phase: str, current: int, total: int, description: str):
                """Update progress bar."""
                progress.update(overall_task, completed=current, description=f"[yellow]{description}")

                # Print special messages
                if phase == 'setup' and 'created' in description.lower():
                    console.print(f"[bold blue]Workspace:[/bold blue] {pipeline.workspace.path}")
                elif phase == 'compile' and 'successful' in description.lower():
                    console.print("[bold green]✓[/bold green] Compilation successful")
                elif phase == 'compile' and 'failed' in description.lower():
                    console.print("[bold red]✗[/bold red] Compilation failed")

            # Run pipeline with progress callback
            result = pipeline.run(progress_callback=progress_callback)

            # Ensure progress is complete
            progress.update(overall_task, completed=total_steps)

            # Display summary
            console.print()
            console.print("[bold cyan]" + "=" * 70 + "[/bold cyan]")
            console.print("[bold cyan]SUMMARY[/bold cyan]")
            console.print("[bold cyan]" + "=" * 70 + "[/bold cyan]")
            console.print()

            if result.compilation_success:
                console.print("[bold green]✓[/bold green] Compilation: Success")
            else:
                console.print("[bold red]✗[/bold red] Compilation: Failed")

            if result.test_results:
                passed = sum(1 for r in result.test_results if r.success)
                failed = len(result.test_results) - passed

                console.print(f"[bold]Tests:[/bold] {passed}/{len(result.test_results)} passed")

                if failed > 0:
                    console.print(f"[bold red]  {failed} failed[/bold red]")

            console.print()
            console.print(result.summary)

            return result, pipeline

    except ValidationError as e:
        console.print(f"[bold red]Validation Error:[/bold red] {e}")
        sys.exit(2)
    except WorkspaceError as e:
        console.print(f"[bold red]Workspace Error:[/bold red] {e}")
        sys.exit(2)
    except Exception as e:
        console.print(f"[bold red]Unexpected Error:[/bold red] {e}")
        import traceback
        traceback.print_exc()
        sys.exit(2)


if __name__ == '__main__':
    main()