"""Unit tests for command-line interface."""

from pathlib import Path
from unittest.mock import Mock, patch

import pytest
from click.testing import CliRunner

from plexilv_run.cli import main


@pytest.fixture
def cli_runner():
    """Create a Click CLI runner."""
    return CliRunner()


@pytest.fixture
def sample_files(tmp_path):
    """Create sample input files with proper directory structure."""
    plan = tmp_path / "test_plan.plx"
    plan.touch()
    script = tmp_path / "test_script.psx"
    script.touch()

    # Create proper PLEXILV_HOME structure
    plexilv_home = tmp_path / "plexilv"
    semantics_dir = plexilv_home / "semantics" / "src"
    semantics_dir.mkdir(parents=True)
    semantics = semantics_dir / "plexil-v.maude"
    semantics.touch()

    return {
        "plan": plan,
        "script": script,
        "semantics": semantics,
        "plexilv_home": plexilv_home,
    }


@pytest.mark.unit
class TestCLIBasic:
    """Test basic CLI functionality."""

    def test_no_arguments(self, cli_runner):
        """Running without arguments should show error."""
        result = cli_runner.invoke(main, [])
        assert result.exit_code != 0
        assert "Missing option" in result.output or "Error" in result.output

    def test_help_option(self, cli_runner):
        """Help option should display usage."""
        result = cli_runner.invoke(main, ["--help"])
        assert result.exit_code == 0
        assert "Execute PLEXIL plan" in result.output
        assert "--plan" in result.output
        assert "--script" in result.output

    def test_missing_plan(self, cli_runner, sample_files):
        """Missing plan option should show error."""
        result = cli_runner.invoke(main, ["-s", str(sample_files["script"])])
        assert result.exit_code != 0

    def test_missing_script(self, cli_runner, sample_files):
        """Missing script option should show error."""
        result = cli_runner.invoke(main, ["-p", str(sample_files["plan"])])
        assert result.exit_code != 0

    def test_nonexistent_plan(self, cli_runner, sample_files):
        """Nonexistent plan file should show error."""
        result = cli_runner.invoke(
            main,
            ["-p", "/nonexistent/plan.plx", "-s", str(sample_files["script"])],
        )
        assert result.exit_code != 0

    def test_nonexistent_script(self, cli_runner, sample_files):
        """Nonexistent script file should show error."""
        result = cli_runner.invoke(
            main,
            ["-p", str(sample_files["plan"]), "-s", "/nonexistent/script.psx"],
        )
        assert result.exit_code != 0


@pytest.mark.unit
class TestCLIWithMocks:
    """Test CLI with mocked pipeline."""

    @patch('plexilv_run.cli.Pipeline')
    def test_successful_execution(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Successful execution should exit with 0."""
        # Set PLEXILV_HOME
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        # Mock pipeline
        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            ["-p", str(sample_files["plan"]), "-s", str(sample_files["script"])],
        )

        assert result.exit_code == 0
        mock_pipeline.run.assert_called_once()

    @patch('plexilv_run.cli.Pipeline')
    def test_with_debug_flag(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Debug flag should be passed to config."""
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            [
                "-p", str(sample_files["plan"]),
                "-s", str(sample_files["script"]),
                "--debug",
            ],
        )

        # Check that config was created with debug=True
        config = mock_pipeline_class.call_args[0][0]
        assert config.debug is True

    @patch('plexilv_run.cli.Pipeline')
    def test_with_custom_timeouts(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Custom timeouts should be passed to config."""
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            [
                "-p", str(sample_files["plan"]),
                "-s", str(sample_files["script"]),
                "--maude-timeout", "600",
                "--conversion-timeout", "60",
            ],
        )

        config = mock_pipeline_class.call_args[0][0]
        assert config.maude_timeout == 600
        assert config.conversion_timeout == 60

    @patch('plexilv_run.cli.Pipeline')
    def test_with_maude_opts(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Maude options should be passed to config."""
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            [
                "-p", str(sample_files["plan"]),
                "-s", str(sample_files["script"]),
                "--maude-opts", "-verbose",
                "--maude-opts", "-print-to-stdout",
            ],
        )

        config = mock_pipeline_class.call_args[0][0]
        assert "-verbose" in config.maude_opts
        assert "-print-to-stdout" in config.maude_opts

    @patch('plexilv_run.cli.Pipeline')
    def test_with_cleanup_flags(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Cleanup flags should be passed to config."""
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            [
                "-p", str(sample_files["plan"]),
                "-s", str(sample_files["script"]),
                "--cleanup",
            ],
        )

        config = mock_pipeline_class.call_args[0][0]
        assert config.cleanup is True
        assert config.force_cleanup is False

    @patch('plexilv_run.cli.Pipeline')
    def test_with_relative_paths(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Relative paths flag should be passed to config."""
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            [
                "-p", str(sample_files["plan"]),
                "-s", str(sample_files["script"]),
                "--maude-relative-paths",
            ],
        )

        config = mock_pipeline_class.call_args[0][0]
        assert config.relative_paths is True

    @patch('plexilv_run.cli.Pipeline')
    def test_with_custom_tool_paths(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Custom tool paths should be passed to config."""
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            [
                "-p", str(sample_files["plan"]),
                "-s", str(sample_files["script"]),
                "--plx2maude-path", "/custom/plx2maude",
                "--psx2maude-path", "/custom/psx2maude",
                "--maude-path", "/custom/maude",
            ],
        )

        config = mock_pipeline_class.call_args[0][0]
        assert config.plx2maude_path == "/custom/plx2maude"
        assert config.psx2maude_path == "/custom/psx2maude"
        assert config.maude_path == "/custom/maude"

    @patch('plexilv_run.cli.Pipeline')
    def test_with_semantics_module(self, mock_pipeline_class, cli_runner, sample_files):
        """Direct semantics module path should work."""
        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 0
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            [
                "-p", str(sample_files["plan"]),
                "-s", str(sample_files["script"]),
                "--semantics-module", str(sample_files["semantics"]),
            ],
        )

        assert result.exit_code == 0

    @patch('plexilv_run.cli.Pipeline')
    def test_pipeline_failure(self, mock_pipeline_class, cli_runner, sample_files, monkeypatch):
        """Pipeline failure should exit with non-zero."""
        monkeypatch.setenv("PLEXILV_HOME", str(sample_files["plexilv_home"]))

        mock_pipeline = Mock()
        mock_pipeline.run.return_value = 1
        mock_pipeline_class.return_value = mock_pipeline

        result = cli_runner.invoke(
            main,
            ["-p", str(sample_files["plan"]), "-s", str(sample_files["script"])],
        )

        assert result.exit_code == 1

    def test_missing_plexilv_home(self, cli_runner, sample_files, monkeypatch):
        """Missing PLEXILV_HOME should show error."""
        monkeypatch.delenv("PLEXILV_HOME", raising=False)

        result = cli_runner.invoke(
            main,
            ["-p", str(sample_files["plan"]), "-s", str(sample_files["script"])],
        )

        assert result.exit_code == 1
        assert "PLEXILV_HOME" in result.output