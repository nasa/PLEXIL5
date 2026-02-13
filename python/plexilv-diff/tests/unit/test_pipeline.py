"""Tests for pipeline orchestration."""

import pytest
from pathlib import Path
from unittest.mock import Mock, patch, MagicMock

from plexilv_diff.pipeline import Pipeline, TestResult, PipelineResult
from plexilv_diff.config import Config
from plexilv_diff.exceptions import CompilationError, ExecutionError


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
        'scripts': [script1, script2]
    }


def test_test_result_dataclass():
    """Test TestResult dataclass."""
    result = TestResult(
        test_number=1,
        test_name="test1.pst",
        success=True
    )

    assert result.test_number == 1
    assert result.test_name == "test1.pst"
    assert result.success is True
    assert result.compiled_script is None
    assert result.error_message is None


def test_pipeline_result_dataclass(tmp_path):
    """Test PipelineResult dataclass."""
    result = PipelineResult(
        workspace_path=tmp_path,
        compilation_success=True,
        exit_code=0
    )

    assert result.workspace_path == tmp_path
    assert result.compilation_success is True
    assert result.exit_code == 0
    assert result.compilation_errors == []
    assert result.test_results == []


def test_pipeline_init(sample_files):
    """Test pipeline initialization."""
    config = Config(
        plan=sample_files['plan'],
        scripts=sample_files['scripts']
    )

    pipeline = Pipeline(config)

    assert pipeline.config == config
    assert pipeline.workspace is None
    assert pipeline.compiler is None
    assert pipeline.executor is None
    assert pipeline.differ is None


@patch('plexilv_diff.pipeline.Workspace')
@patch('plexilv_diff.pipeline.Compiler')
@patch('plexilv_diff.pipeline.Executor')
@patch('plexilv_diff.pipeline.Differ')
def test_pipeline_run_compilation_error(mock_differ, mock_executor, mock_compiler, mock_workspace, sample_files):
    """Test pipeline when compilation fails."""
    config = Config(
        plan=sample_files['plan'],
        scripts=sample_files['scripts']
    )

    # Mock workspace
    mock_ws_instance = MagicMock()
    mock_ws_instance.path = Path("/tmp/workspace")
    mock_ws_instance.create_debug_config.return_value = Path("/tmp/workspace/debug.cfg")
    mock_ws_instance.copy_file.return_value = sample_files['plan']
    mock_workspace.return_value = mock_ws_instance

    # Mock compiler to fail
    mock_comp_instance = MagicMock()
    mock_comp_instance.compile_plan.side_effect = CompilationError("Syntax error")
    mock_compiler.return_value = mock_comp_instance

    pipeline = Pipeline(config)
    result = pipeline.run()

    assert result.compilation_success is False
    assert len(result.compilation_errors) > 0
    assert result.exit_code == 2
    assert "compilation failed" in result.summary.lower()


@patch('plexilv_diff.pipeline.Workspace')
@patch('plexilv_diff.pipeline.Compiler')
@patch('plexilv_diff.pipeline.Executor')
@patch('plexilv_diff.pipeline.Differ')
@patch('plexilv_diff.executor.local')
def test_pipeline_run_success_no_diffs(mock_local, mock_differ, mock_executor, mock_compiler, mock_workspace, sample_files):
    """Test successful pipeline run with no differences."""
    # Mock plumbum local
    mock_local.__getitem__.return_value = MagicMock()

    config = Config(
        plan=sample_files['plan'],
        scripts=sample_files['scripts']
    )

    # Mock workspace
    mock_ws_instance = MagicMock()
    mock_ws_instance.path = Path("/tmp/workspace")
    mock_ws_instance.create_test_directory.return_value = Path("/tmp/workspace/001")
    mock_ws_instance.get_test_directory.side_effect = lambda i: Path(f"/tmp/workspace/{i:03d}")
    mock_ws_instance.copy_file.return_value = sample_files['plan']
    mock_ws_instance.copy_file_to_test_dir.side_effect = lambda f, i: Path(f"/tmp/workspace/{i:03d}/{f.name}")
    mock_workspace.return_value = mock_ws_instance

    # Mock compiler
    mock_comp_instance = MagicMock()
    mock_comp_instance.compile_plan.return_value = Path("/tmp/workspace/plan.plx")
    mock_comp_instance.compile_script.return_value = Path("/tmp/workspace/001/test.psx")
    mock_compiler.return_value = mock_comp_instance

    # Mock executor
    from plexilv_diff.executor import ExecutionResult
    mock_exec_instance = MagicMock()
    mock_exec_instance.run_both.return_value = (
        ExecutionResult(Path("/tmp/test.plexil.log"), 0, True),
        ExecutionResult(Path("/tmp/test.plexilv.log"), 0, True)
    )
    mock_executor.return_value = mock_exec_instance

    # Mock differ
    from plexilv_diff.differ import DiffResult
    mock_diff_instance = MagicMock()
    mock_diff_instance.compare_logs.return_value = DiffResult(
        diff_file=Path("/tmp/diff.txt"),
        identical=True,
        differ=False,
        error=False,
        returncode=0,
        output="No differences"
    )
    mock_differ.return_value = mock_diff_instance

    pipeline = Pipeline(config)
    result = pipeline.run()

    assert result.compilation_success is True
    assert len(result.compilation_errors) == 0
    assert len(result.test_results) == 2
    assert all(r.success for r in result.test_results)
    assert result.exit_code == 0


@patch('plexilv_diff.pipeline.Workspace')
@patch('plexilv_diff.pipeline.Compiler')
@patch('plexilv_diff.pipeline.Executor')
@patch('plexilv_diff.pipeline.Differ')
@patch('plexilv_diff.executor.local')
def test_pipeline_run_success_with_diffs(mock_local, mock_differ, mock_executor, mock_compiler, mock_workspace, sample_files):
    """Test successful pipeline run with differences found."""
    # Mock plumbum local
    mock_local.__getitem__.return_value = MagicMock()

    config = Config(
        plan=sample_files['plan'],
        scripts=sample_files['scripts']
    )

    # Mock workspace
    mock_ws_instance = MagicMock()
    mock_ws_instance.path = Path("/tmp/workspace")
    mock_ws_instance.create_test_directory.return_value = Path("/tmp/workspace/001")
    mock_ws_instance.get_test_directory.side_effect = lambda i: Path(f"/tmp/workspace/{i:03d}")
    mock_ws_instance.copy_file.return_value = sample_files['plan']
    mock_ws_instance.copy_file_to_test_dir.side_effect = lambda f, i: Path(f"/tmp/workspace/{i:03d}/{f.name}")
    mock_workspace.return_value = mock_ws_instance

    # Mock compiler
    mock_comp_instance = MagicMock()
    mock_comp_instance.compile_plan.return_value = Path("/tmp/workspace/plan.plx")
    mock_comp_instance.compile_script.return_value = Path("/tmp/workspace/001/test.psx")
    mock_compiler.return_value = mock_comp_instance

    # Mock executor
    from plexilv_diff.executor import ExecutionResult
    mock_exec_instance = MagicMock()
    mock_exec_instance.run_both.return_value = (
        ExecutionResult(Path("/tmp/test.plexil.log"), 0, True),
        ExecutionResult(Path("/tmp/test.plexilv.log"), 0, True)
    )
    mock_executor.return_value = mock_exec_instance

    # Mock differ - return differences
    from plexilv_diff.differ import DiffResult
    mock_diff_instance = MagicMock()
    mock_diff_instance.compare_logs.return_value = DiffResult(
        diff_file=Path("/tmp/diff.txt"),
        identical=False,
        differ=True,
        error=False,
        returncode=1,
        output="< Line from plexiltest\n> Line from plexilv"
    )
    mock_differ.return_value = mock_diff_instance

    pipeline = Pipeline(config)
    result = pipeline.run()

    assert result.compilation_success is True
    assert len(result.test_results) == 2
    assert all(r.success for r in result.test_results)
    assert result.exit_code == 1  # Differences found


@patch('plexilv_diff.pipeline.Workspace')
@patch('plexilv_diff.pipeline.Compiler')
@patch('plexilv_diff.pipeline.Executor')
@patch('plexilv_diff.pipeline.Differ')
@patch('plexilv_diff.executor.local')
def test_pipeline_run_execution_error(mock_local, mock_differ, mock_executor, mock_compiler, mock_workspace, sample_files):
    """Test pipeline when execution fails."""
    # Mock plumbum local
    mock_local.__getitem__.return_value = MagicMock()

    config = Config(
        plan=sample_files['plan'],
        scripts=sample_files['scripts']
    )

    # Mock workspace
    mock_ws_instance = MagicMock()
    mock_ws_instance.path = Path("/tmp/workspace")
    mock_ws_instance.create_test_directory.return_value = Path("/tmp/workspace/001")
    mock_ws_instance.get_test_directory.side_effect = lambda i: Path(f"/tmp/workspace/{i:03d}")
    mock_ws_instance.copy_file.return_value = sample_files['plan']
    mock_ws_instance.copy_file_to_test_dir.side_effect = lambda f, i: Path(f"/tmp/workspace/{i:03d}/{f.name}")
    mock_workspace.return_value = mock_ws_instance

    # Mock compiler
    mock_comp_instance = MagicMock()
    mock_comp_instance.compile_plan.return_value = Path("/tmp/workspace/plan.plx")
    mock_comp_instance.compile_script.return_value = Path("/tmp/workspace/001/test.psx")
    mock_compiler.return_value = mock_comp_instance

    # Mock executor to fail
    from plexilv_diff.executor import ExecutionResult
    mock_exec_instance = MagicMock()
    mock_exec_instance.run_both.return_value = (
        ExecutionResult(Path("/tmp/test.plexil.log"), 1, False),  # Failed
        ExecutionResult(Path("/tmp/test.plexilv.log"), 0, True)
    )
    mock_executor.return_value = mock_exec_instance

    pipeline = Pipeline(config)
    result = pipeline.run()

    assert result.compilation_success is True
    assert len(result.test_results) == 2
    assert not result.test_results[0].success
    assert result.exit_code == 2  # Execution errors


@patch('plexilv_diff.pipeline.Workspace')
def test_pipeline_cleanup_force(mock_workspace, sample_files):
    """Test forced cleanup."""
    config = Config(
        plan=sample_files['plan'],
        scripts=sample_files['scripts'],
        force_cleanup=False
    )

    mock_ws_instance = MagicMock()
    mock_workspace.return_value = mock_ws_instance

    pipeline = Pipeline(config)
    pipeline.workspace = mock_ws_instance

    pipeline.cleanup(force=True)

    mock_ws_instance.cleanup.assert_called_once()


def test_generate_compilation_error_summary(tmp_path):
    """Test compilation error summary generation."""
    # Create actual files
    plan = tmp_path / "plan.ple"
    plan.write_text("// plan")
    script = tmp_path / "test.pst"
    script.write_text("// script")

    config = Config(
        plan=plan,
        scripts=[script]
    )

    pipeline = Pipeline(config)

    errors = [
        "plan.ple: Syntax error on line 5",
        "test.pst: Unknown command"
    ]

    summary = pipeline._generate_compilation_error_summary(errors)

    assert "COMPILATION FAILED" in summary
    assert "plan.ple" in summary
    assert "test.pst" in summary
    assert "Total errors: 2" in summary


def test_determine_exit_code_all_success(tmp_path):
    """Test exit code determination when all tests pass with no diffs."""
    from plexilv_diff.differ import DiffResult

    # Create actual files
    plan = tmp_path / "plan.ple"
    plan.write_text("// plan")
    script = tmp_path / "test.pst"
    script.write_text("// script")

    test_results = [
        TestResult(
            test_number=1,
            test_name="test1.pst",
            success=True,
            diff_result=DiffResult(
                diff_file=Path("/tmp/diff.txt"),
                identical=True,
                differ=False,
                error=False,
                returncode=0,
                output=""
            )
        )
    ]

    config = Config(plan=plan, scripts=[script])
    pipeline = Pipeline(config)

    exit_code = pipeline._determine_exit_code(test_results)
    assert exit_code == 0


def test_determine_exit_code_with_diffs(tmp_path):
    """Test exit code determination when diffs are found."""
    from plexilv_diff.differ import DiffResult

    # Create actual files
    plan = tmp_path / "plan.ple"
    plan.write_text("// plan")
    script = tmp_path / "test.pst"
    script.write_text("// script")

    test_results = [
        TestResult(
            test_number=1,
            test_name="test1.pst",
            success=True,
            diff_result=DiffResult(
                diff_file=Path("/tmp/diff.txt"),
                identical=False,
                differ=True,
                error=False,
                returncode=1,
                output="differences"
            )
        )
    ]

    config = Config(plan=plan, scripts=[script])
    pipeline = Pipeline(config)

    exit_code = pipeline._determine_exit_code(test_results)
    assert exit_code == 1


def test_determine_exit_code_with_failures(tmp_path):
    """Test exit code determination when tests fail."""
    # Create actual files
    plan = tmp_path / "plan.ple"
    plan.write_text("// plan")
    script = tmp_path / "test.pst"
    script.write_text("// script")

    test_results = [
        TestResult(
            test_number=1,
            test_name="test1.pst",
            success=False,
            error_message="Execution failed"
        )
    ]

    config = Config(plan=plan, scripts=[script])
    pipeline = Pipeline(config)

    exit_code = pipeline._determine_exit_code(test_results)
    assert exit_code == 2