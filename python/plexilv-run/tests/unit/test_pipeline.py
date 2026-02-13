"""Unit tests for pipeline orchestration."""

from pathlib import Path
from unittest.mock import Mock, patch

import pytest

from plexilv_run.config import Config
from plexilv_run.exceptions import (
    ConfigurationError,
    MaudeError,
    ToolNotFoundError,
    ValidationError,
    WorkspaceError,
)
from plexilv_run.pipeline import Pipeline


@pytest.fixture
def mock_config(tmp_path):
    """Create a mock Config for testing."""
    plan = tmp_path / "test_plan.plx"
    plan.touch()
    script = tmp_path / "test_script.psx"
    script.touch()
    semantics = tmp_path / "plexil-v.maude"
    semantics.touch()

    # Use tmp_path for work_dir to avoid conflicts
    work_dir = tmp_path / "workspace"

    return Config(
        plan_path=plan,
        script_path=script,
        semantics_path=semantics,
        plx2maude_path="/usr/bin/plx2maude",
        psx2maude_path="/usr/bin/psx2maude",
        maude_path="/usr/bin/maude",
        maude_opts=[],
        maude_timeout=300,
        conversion_timeout=30,
        work_dir=work_dir,
        log_file=None,
        debug=False,
        cleanup=False,
        force_cleanup=False,
        relative_paths=False,
    )


@pytest.mark.unit
class TestPipeline:
    """Test Pipeline initialization."""

    def test_init(self, mock_config):
        """Should initialize with config."""
        pipeline = Pipeline(mock_config)
        assert pipeline.config == mock_config
        assert pipeline.workspace is None
        assert pipeline.errors == []


@pytest.mark.unit
class TestPipelineRun:
    """Test complete pipeline execution."""

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    @patch('plexilv_run.pipeline.ConversionRunner')
    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_successful_run(
        self,
        mock_maude_runner_class,
        mock_conversion_runner_class,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
    ):
        """Successful run should return 0."""
        # Mock conversion runner
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = True
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        # Mock Maude runner
        mock_maude_runner = Mock()
        mock_maude_runner.execute.return_value = 0
        mock_maude_runner_class.return_value = mock_maude_runner

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 0
        assert pipeline.workspace is not None

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    def test_validation_error(
        self,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
        capsys,
    ):
        """Validation error should return 1."""
        mock_validate_plan.side_effect = ValidationError("Invalid plan file")

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        captured = capsys.readouterr()
        assert "Invalid plan file" in captured.err

    @patch('plexilv_run.pipeline.check_all_tools')
    def test_tool_not_found_error(self, mock_check_tools, mock_config, capsys):
        """Tool not found should return 1."""
        mock_check_tools.side_effect = ToolNotFoundError("plx2maude", "Tool not found")

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        captured = capsys.readouterr()
        assert "Tool not found" in captured.err

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    @patch('plexilv_run.pipeline.ConversionRunner')
    def test_conversion_failure(
        self,
        mock_conversion_runner_class,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
        capsys,
    ):
        """Conversion failure should return 1."""
        # Mock conversion runner with failure
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = False
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        captured = capsys.readouterr()
        assert "Conversion failed" in captured.err

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    @patch('plexilv_run.pipeline.ConversionRunner')
    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_maude_error(
        self,
        mock_maude_runner_class,
        mock_conversion_runner_class,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
        capsys,
    ):
        """Maude execution error should return non-zero."""
        # Mock conversion runner
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = True
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        # Mock Maude runner with error
        mock_maude_runner = Mock()
        mock_maude_runner.execute.side_effect = MaudeError(1, "Execution failed")
        mock_maude_runner_class.return_value = mock_maude_runner

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        captured = capsys.readouterr()
        # The error message from MaudeError exception
        assert "Error:" in captured.err


@pytest.mark.unit
class TestPipelinePhases:
    """Test individual pipeline phases."""

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    def test_validate_phase(
        self,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
    ):
        """Validate phase should check tools and files."""
        pipeline = Pipeline(mock_config)
        pipeline._validate()

        # Should have called all validation functions
        mock_check_tools.assert_called_once()
        mock_validate_plan.assert_called_once_with(mock_config.plan_path)
        mock_validate_script.assert_called_once_with(mock_config.script_path)

    def test_create_workspace_phase(self, mock_config):
        """Create workspace phase should create workspace."""
        pipeline = Pipeline(mock_config)
        pipeline._create_workspace()

        assert pipeline.workspace is not None
        assert pipeline.workspace.path.exists()

    @patch('plexilv_run.pipeline.ConversionRunner')
    def test_convert_phase_success(self, mock_conversion_runner_class, mock_config):
        """Convert phase should return True if both conversions succeed."""
        # Create workspace first
        pipeline = Pipeline(mock_config)
        pipeline._create_workspace()

        # Mock conversion runner
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = True
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        result = pipeline._convert()

        assert result is True
        mock_conversion_runner.run_plx2maude.assert_called_once()
        mock_conversion_runner.run_psx2maude.assert_called_once()

    @patch('plexilv_run.pipeline.ConversionRunner')
    def test_convert_phase_failure(self, mock_conversion_runner_class, mock_config):
        """Convert phase should return False if any conversion fails."""
        pipeline = Pipeline(mock_config)
        pipeline._create_workspace()

        # Mock conversion runner with one failure
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = False
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        result = pipeline._convert()

        assert result is False
        # Both should still be called
        mock_conversion_runner.run_plx2maude.assert_called_once()
        mock_conversion_runner.run_psx2maude.assert_called_once()

    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_generate_run_file_phase(self, mock_maude_runner_class, mock_config):
        """Generate run file phase should create run.maude."""
        pipeline = Pipeline(mock_config)
        pipeline._create_workspace()

        mock_maude_runner = Mock()
        mock_maude_runner_class.return_value = mock_maude_runner

        pipeline._generate_run_file()

        mock_maude_runner.generate_run_file.assert_called_once_with(
            "test_plan", "test_script"
        )

    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_execute_maude_phase(self, mock_maude_runner_class, mock_config):
        """Execute Maude phase should run Maude."""
        pipeline = Pipeline(mock_config)
        pipeline._create_workspace()

        mock_maude_runner = Mock()
        mock_maude_runner.execute.return_value = 0
        mock_maude_runner_class.return_value = mock_maude_runner

        result = pipeline._execute_maude()

        assert result == 0
        mock_maude_runner.execute.assert_called_once()


@pytest.mark.unit
class TestPipelineCleanup:
    """Test cleanup behavior."""

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    @patch('plexilv_run.pipeline.ConversionRunner')
    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_cleanup_on_success(
        self,
        mock_maude_runner_class,
        mock_conversion_runner_class,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
    ):
        """Cleanup flag should remove workspace on success."""
        mock_config.cleanup = True

        # Mock successful execution
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = True
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        mock_maude_runner = Mock()
        mock_maude_runner.execute.return_value = 0
        mock_maude_runner_class.return_value = mock_maude_runner

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 0
        # Workspace should be removed
        assert not pipeline.workspace.path.exists()

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    @patch('plexilv_run.pipeline.ConversionRunner')
    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_no_cleanup_on_failure(
        self,
        mock_maude_runner_class,
        mock_conversion_runner_class,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
    ):
        """Default should keep workspace on failure."""
        # Mock successful conversion but Maude failure
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = True
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        mock_maude_runner = Mock()
        mock_maude_runner.execute.side_effect = MaudeError(1, "Failed")
        mock_maude_runner_class.return_value = mock_maude_runner

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        # Workspace should still exist
        assert pipeline.workspace.path.exists()

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    @patch('plexilv_run.pipeline.ConversionRunner')
    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_force_cleanup_always(
        self,
        mock_maude_runner_class,
        mock_conversion_runner_class,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
    ):
        """Force cleanup should remove workspace even on failure."""
        mock_config.force_cleanup = True

        # Mock failure
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = True
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        mock_maude_runner = Mock()
        mock_maude_runner.execute.side_effect = MaudeError(1, "Failed")
        mock_maude_runner_class.return_value = mock_maude_runner

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        # Workspace should be removed even on failure
        assert not pipeline.workspace.path.exists()

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    @patch('plexilv_run.pipeline.ConversionRunner')
    @patch('plexilv_run.pipeline.MaudeRunner')
    def test_no_cleanup_by_default(
        self,
        mock_maude_runner_class,
        mock_conversion_runner_class,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
    ):
        """Default (no cleanup flags) should keep workspace."""
        # Both cleanup flags are False by default

        # Mock successful execution
        mock_conversion_runner = Mock()
        mock_conversion_runner.run_plx2maude.return_value = True
        mock_conversion_runner.run_psx2maude.return_value = True
        mock_conversion_runner_class.return_value = mock_conversion_runner

        mock_maude_runner = Mock()
        mock_maude_runner.execute.return_value = 0
        mock_maude_runner_class.return_value = mock_maude_runner

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 0
        # Workspace should still exist (no cleanup)
        assert pipeline.workspace.path.exists()


@pytest.mark.unit
class TestPipelineErrorHandling:
    """Test error handling in pipeline."""

    def test_config_validation_error(self, mock_config, capsys):
        """Config validation error should be caught."""
        # Make config invalid
        mock_config.maude_timeout = -1

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        captured = capsys.readouterr()
        assert "Error:" in captured.err

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    def test_unexpected_exception(
        self,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
        capsys,
    ):
        """Unexpected exceptions should be caught and reported."""
        mock_check_tools.side_effect = RuntimeError("Unexpected error")

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 1
        captured = capsys.readouterr()
        assert "Unexpected error" in captured.err

    @patch('plexilv_run.pipeline.check_all_tools')
    @patch('plexilv_run.pipeline.validate_plan_file')
    @patch('plexilv_run.pipeline.validate_script_file')
    def test_keyboard_interrupt(
        self,
        mock_validate_script,
        mock_validate_plan,
        mock_check_tools,
        mock_config,
        capsys,
    ):
        """Keyboard interrupt should exit gracefully."""
        mock_check_tools.side_effect = KeyboardInterrupt()

        pipeline = Pipeline(mock_config)
        result = pipeline.run()

        assert result == 130
        captured = capsys.readouterr()
        assert "Interrupted" in captured.err