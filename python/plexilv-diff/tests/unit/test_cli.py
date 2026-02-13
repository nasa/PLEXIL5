"""Tests for command-line interface."""

import pytest
from pathlib import Path
from click.testing import CliRunner
from unittest.mock import patch, MagicMock

from plexilv_diff.cli import main


@pytest.fixture
def sample_files(tmp_path):
    """Create sample input files."""
    plan = tmp_path / "plan.ple"
    plan.write_text("// Sample PLEXIL plan")

    script1 = tmp_path / "test1.pst"
    script1.write_text("// Test script 1")

    script2 = tmp_path / "test2.pst"
    script2.write_text("// Test script 2")

    return {
        'plan': plan,
        'script1': script1,
        'script2': script2
    }


def test_cli_help():
    """Test that --help works."""
    runner = CliRunner()
    result = runner.invoke(main, ['--help'])

    assert result.exit_code == 0
    assert 'Compare PLEXIL interpreter outputs' in result.output
    assert '--plan' in result.output
    assert '--script' in result.output


def test_cli_missing_plan(tmp_path):
    """Test error when plan is not provided."""
    # Create a real script file so Click doesn't complain about it first
    script = tmp_path / "test.pst"
    script.write_text("// test")

    runner = CliRunner()
    result = runner.invoke(main, ['-s', str(script)])

    assert result.exit_code != 0
    assert 'Missing option' in result.output or 'required' in result.output.lower()


def test_cli_missing_script(tmp_path):
    """Test error when script is not provided."""
    # Create a real plan file so Click doesn't complain about it first
    plan = tmp_path / "plan.ple"
    plan.write_text("// plan")

    runner = CliRunner()
    result = runner.invoke(main, ['-p', str(plan)])

    assert result.exit_code != 0
    assert 'Missing option' in result.output or 'required' in result.output.lower()


def test_cli_plan_not_exists():
    """Test error when plan file doesn't exist."""
    runner = CliRunner()
    result = runner.invoke(main, ['-p', 'nonexistent.ple', '-s', 'test.pst'])

    assert result.exit_code != 0
    # Click will show "does not exist" error


def test_cli_multiple_scripts(sample_files):
    """Test accepting multiple -s flags."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        # Mock pipeline to avoid actual execution
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=0,
            summary="All tests passed"
        )
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '-s', str(sample_files['script2']),
            '--quiet'
        ])

        # Should successfully parse multiple scripts
        assert mock_pipeline.called


def test_cli_custom_executables(sample_files):
    """Test custom executable paths."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=0,
            summary="All tests passed"
        )
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--plexilc', '/custom/plexilc',
            '--plexiltest', '/custom/plexiltest',
            '--plexilv-run', '/custom/plexilv-run',
            '--quiet'
        ])

        # Check that config was created with custom paths
        call_args = mock_pipeline.call_args
        config = call_args[0][0]
        assert config.plexilc == '/custom/plexilc'
        assert config.plexiltest == '/custom/plexiltest'
        assert config.plexilv_run == '/custom/plexilv-run'


def test_cli_flags(sample_files):
    """Test additional flags for tools."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=0,
            summary="All tests passed"
        )
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--plexilc-flags', '--quiet --debug',
            '--quiet'
        ])

        call_args = mock_pipeline.call_args
        config = call_args[0][0]
        assert config.plexilc_flags == '--quiet --debug'


def test_cli_timeouts(sample_files):
    """Test custom timeout values."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=0,
            summary="All tests passed"
        )
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--compilation-timeout', '60',
            '--execution-timeout', '600',
            '--quiet'
        ])

        call_args = mock_pipeline.call_args
        config = call_args[0][0]
        assert config.compilation_timeout == 60
        assert config.execution_timeout == 600


def test_cli_cleanup_flags(sample_files):
    """Test cleanup flags."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=0,
            summary="All tests passed"
        )
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--cleanup',
            '--quiet'
        ])

        call_args = mock_pipeline.call_args
        config = call_args[0][0]
        assert config.cleanup is True
        assert config.force_cleanup is False


def test_cli_behavior_flags(sample_files):
    """Test behavior flags."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=0,
            summary="All tests passed"
        )
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--fail-fast',
            '--failproof',
            '--quiet'
        ])

        call_args = mock_pipeline.call_args
        config = call_args[0][0]
        assert config.fail_fast is True
        assert config.failproof is True
        assert config.quiet is True


def test_cli_exit_code_success(sample_files):
    """Test exit code 0 on success with no diffs."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=0,
            summary="All tests passed"
        )
        mock_pipeline_instance.workspace = MagicMock()
        mock_pipeline_instance.workspace.path = Path("/tmp/workspace")
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--quiet'
        ])

        assert result.exit_code == 0


def test_cli_exit_code_diffs(sample_files):
    """Test exit code 1 when differences found."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=True,
            exit_code=1,
            summary="Differences found"
        )
        mock_pipeline_instance.workspace = MagicMock()
        mock_pipeline_instance.workspace.path = Path("/tmp/workspace")
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--quiet'
        ])

        assert result.exit_code == 1


def test_cli_exit_code_error(sample_files):
    """Test exit code 2 on execution errors."""
    runner = CliRunner()

    with patch('plexilv_diff.cli.Pipeline') as mock_pipeline:
        mock_pipeline_instance = MagicMock()
        mock_pipeline_instance.run.return_value = MagicMock(
            workspace_path=Path("/tmp/workspace"),
            compilation_success=False,
            exit_code=2,
            compilation_errors=["Compilation failed"],
            summary="Errors occurred"
        )
        mock_pipeline_instance.workspace = MagicMock()
        mock_pipeline_instance.workspace.path = Path("/tmp/workspace")
        mock_pipeline.return_value = mock_pipeline_instance

        result = runner.invoke(main, [
            '-p', str(sample_files['plan']),
            '-s', str(sample_files['script1']),
            '--quiet'
        ])

        assert result.exit_code == 2