"""Command-line interface for plexilv."""

import sys
from pathlib import Path

import click

from plexilv_run.config import Config
from plexilv_run.pipeline import Pipeline


@click.command()
@click.option(
    "-p",
    "--plan",
    required=True,
    type=click.Path(exists=True, path_type=Path),
    help="Path to PLEXIL plan file (.plx)",
)
@click.option(
    "-s",
    "--script",
    required=True,
    type=click.Path(exists=True, path_type=Path),
    help="Path to PLEXIL script file (.psx)",
)
@click.option(
    "--plexilv-home",
    type=click.Path(exists=True, file_okay=False, dir_okay=True, path_type=Path),
    help="Path to PLEXIL-V repository root (overrides $PLEXILV_HOME)",
)
@click.option(
    "--semantics-module",
    type=click.Path(exists=True, file_okay=True, dir_okay=False, path_type=Path),
    help="Direct path to plexil-v.maude file",
)
@click.option(
    "--plx2maude-path",
    type=str,
    help="Path to plx2maude executable (default: search in PATH)",
)
@click.option(
    "--psx2maude-path",
    type=str,
    help="Path to psx2maude executable (default: search in PATH)",
)
@click.option(
    "--maude-path",
    type=str,
    help="Path to Maude executable (default: search in PATH)",
)
@click.option(
    "--maude-opts",
    multiple=True,
    help="Additional arguments to pass to maude (space-separated, can be used multiple times)",
)
@click.option(
    "--maude-timeout",
    type=int,
    default=300,
    help="Timeout for Maude execution in seconds (default: 300)",
)
@click.option(
    "--maude-relative-paths",
    is_flag=True,
    help="Use relative paths in run.maude instead of absolute",
)
@click.option(
    "--conversion-timeout",
    type=int,
    default=30,
    help="Timeout for plx2maude/psx2maude execution in seconds (default: 30)",
)
@click.option(
    "--log-file",
    type=click.Path(path_type=Path),
    help="Redirect Maude stderr to file (fails if file already exists)",
)
@click.option(
    "--debug",
    is_flag=True,
    help="Verbose output with full command details, captures both stdout and stderr, creates log files",
)
@click.option(
    "--work-dir",
    type=click.Path(path_type=Path),
    help="Specify workspace directory (fails if exists)",
)
@click.option(
    "--cleanup",
    is_flag=True,
    help="Remove workspace on success only (exit code 0)",
)
@click.option(
    "--force-cleanup",
    is_flag=True,
    help="Remove workspace always (regardless of exit code)",
)
def main(
    plan: Path,
    script: Path,
    plexilv_home: Path | None,
    semantics_module: Path | None,
    plx2maude_path: str | None,
    psx2maude_path: str | None,
    maude_path: str | None,
    maude_opts: tuple[str, ...],
    maude_timeout: int,
    maude_relative_paths: bool,
    conversion_timeout: int,
    log_file: Path | None,
    debug: bool,
    work_dir: Path | None,
    cleanup: bool,
    force_cleanup: bool,
) -> None:
    """Execute PLEXIL plan using the formal semantics of PLEXIL-V written in Maude.

    This is the 'run' (or 'test') subcommand of the plexilv CLI tool.

    The tool orchestrates the execution of PLEXIL plans by:
    1. Converting plan (.plx) and script (.psx) files to Maude representations
    2. Generating a run.maude file with the formal semantics
    3. Executing Maude to interpret the plan with the given script

    Examples:

        # As subcommand (when used with plexilv-cli)
        plexilv run -p MyPlan.plx -s test_script.psx
        plexilv test -p MyPlan.plx -s test_script.psx

        # Standalone (for development/testing)
        plexilv-run -p MyPlan.plx -s test_script.psx

        # With debug and logging
        plexilv run -p MyPlan.plx -s test_script.psx --debug --log-file output.log

        # With custom workspace
        plexilv run -p MyPlan.plx -s test_script.psx --work-dir my-run --cleanup
    """
    try:
        # Resolve semantics path
        semantics_path = Config.resolve_semantics_path(
            semantics_module=str(semantics_module) if semantics_module else None,
            plexilv_home=str(plexilv_home) if plexilv_home else None,
        )

        # Create configuration
        config = Config(
            plan_path=plan,
            script_path=script,
            semantics_path=semantics_path,
            plx2maude_path=plx2maude_path or "plx2maude",
            psx2maude_path=psx2maude_path or "psx2maude",
            maude_path=maude_path or "maude",
            maude_opts=list(maude_opts),
            maude_timeout=maude_timeout,
            conversion_timeout=conversion_timeout,
            work_dir=work_dir,
            log_file=log_file,
            debug=debug,
            cleanup=cleanup,
            force_cleanup=force_cleanup,
            relative_paths=maude_relative_paths,
        )

        # Create and run pipeline
        pipeline = Pipeline(config)
        exit_code = pipeline.run()

        sys.exit(exit_code)

    except Exception as e:
        click.echo(f"Error: {e}", err=True)
        sys.exit(1)


if __name__ == "__main__":
    main()