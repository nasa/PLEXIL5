"""Tests for PLEXIL interpreter execution."""

import pytest
from pathlib import Path
from unittest.mock import Mock, patch, MagicMock
from plumbum.commands.processes import ProcessTimedOut

from plexilv_diff.executor import Executor, ExecutionResult
from plexilv_diff.exceptions import ExecutionError, TimeoutError as DiffPlexilvTimeoutError


@pytest.fixture
def test_files(tmp_path):
    """Create test files."""
    plan = tmp_path / "plan.plx"
    plan.write_text("<?xml version='1.0'?><PlexilPlan/>")

    script = tmp_path / "test.psx"
    script.write_text("<?xml version='1.0'?><PLEXILScript/>")

    debug_cfg = tmp_path / "debug.cfg"
    debug_cfg.write_text("PlexilExec:step\nPlexilExec:cycle\n")

    test_dir = tmp_path / "001"
    test_dir.mkdir()

    return {
        'plan': plan,
        'script': script,
        'debug_cfg': debug_cfg,
        'test_dir': test_dir
    }


@patch('plexilv_diff.executor.local')
def test_executor_init_defaults(mock_local):
    """Test executor initialization with defaults."""
    # Mock local to return mock commands
    mock_local.__getitem__.return_value = MagicMock()

    executor = Executor()

    assert executor.plexiltest_path == "plexiltest"
    assert executor.plexilv_run_path == "plexilv-run"
    assert executor.execution_timeout == 300
    assert executor.plexiltest_flags == []
    assert executor.plexilv_run_flags == []


@patch('plexilv_diff.executor.local')
def test_executor_init_custom(mock_local):
    """Test executor initialization with custom values."""
    # Mock local to return mock commands
    mock_local.__getitem__.return_value = MagicMock()

    executor = Executor(
        plexiltest_path="/usr/local/bin/plexiltest",
        plexilv_run_path="/usr/local/bin/plexilv-run",
        execution_timeout=600,
        plexiltest_flags="--quiet --debug",
        plexilv_run_flags="--verbose"
    )

    assert executor.plexiltest_path == "/usr/local/bin/plexiltest"
    assert executor.plexilv_run_path == "/usr/local/bin/plexilv-run"
    assert executor.execution_timeout == 600
    assert executor.plexiltest_flags == ["--quiet", "--debug"]
    assert executor.plexilv_run_flags == ["--verbose"]


@patch('plexilv_diff.executor.local')
def test_executor_parse_flags_empty(mock_local):
    """Test parsing empty flags."""
    mock_local.__getitem__.return_value = MagicMock()

    executor = Executor()

    assert executor._parse_flags("") == []
    assert executor._parse_flags("   ") == []


@patch('plexilv_diff.executor.local')
def test_executor_parse_flags_multiple(mock_local):
    """Test parsing multiple flags."""
    mock_local.__getitem__.return_value = MagicMock()

    executor = Executor()

    flags = executor._parse_flags("--flag1 --flag2 value")
    assert flags == ["--flag1", "--flag2", "value"]


def test_execution_result_dataclass():
    """Test ExecutionResult dataclass."""
    log_file = Path("/tmp/test.log")
    result = ExecutionResult(
        log_file=log_file,
        returncode=0,
        success=True,
        stdout="output",
        stderr="errors"
    )

    assert result.log_file == log_file
    assert result.returncode == 0
    assert result.success is True
    assert result.stdout == "output"
    assert result.stderr == "errors"


@patch('plexilv_diff.executor.local')
def test_run_plexiltest_success(mock_local, test_files):
    """Test successful plexiltest execution."""
    # Mock the command chain
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "Test execution log", "")

    mock_plexiltest = MagicMock()
    mock_plexiltest.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexiltest
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    result = executor.run_plexiltest(
        test_files['plan'],
        test_files['script'],
        test_files['debug_cfg'],
        test_files['test_dir'],
        "plan"
    )

    assert result.success is True
    assert result.returncode == 0
    assert result.log_file == test_files['test_dir'] / "plan.plexil.log"
    assert result.log_file.exists()
    assert "Test execution log" in result.log_file.read_text()


@patch('plexilv_diff.executor.local')
def test_run_plexiltest_with_flags(mock_local, test_files):
    """Test plexiltest execution with additional flags."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "output", "")

    mock_plexiltest = MagicMock()
    mock_plexiltest.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexiltest
    mock_local.cwd.return_value = MagicMock()

    executor = Executor(plexiltest_flags="--quiet --debug")
    executor.run_plexiltest(
        test_files['plan'],
        test_files['script'],
        test_files['debug_cfg'],
        test_files['test_dir'],
        "plan"
    )

    # Verify flags were passed
    call_args = mock_plexiltest.__getitem__.call_args[0][0]
    assert "--quiet" in call_args
    assert "--debug" in call_args
    assert "-p" in call_args
    assert "-s" in call_args
    assert "-d" in call_args


@patch('plexilv_diff.executor.local')
def test_run_plexiltest_nonzero_exit(mock_local, test_files):
    """Test plexiltest execution with non-zero exit code."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (1, "output", "error message")

    mock_plexiltest = MagicMock()
    mock_plexiltest.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexiltest
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    result = executor.run_plexiltest(
        test_files['plan'],
        test_files['script'],
        test_files['debug_cfg'],
        test_files['test_dir'],
        "plan"
    )

    # Should not raise, but mark as failed
    assert result.success is False
    assert result.returncode == 1
    assert result.log_file.exists()


@patch('plexilv_diff.executor.local')
def test_run_plexiltest_timeout(mock_local, test_files):
    """Test plexiltest timeout."""
    mock_run_result = MagicMock()
    mock_run_result.run.side_effect = ProcessTimedOut(["plexiltest"], 124)

    mock_plexiltest = MagicMock()
    mock_plexiltest.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexiltest
    mock_local.cwd.return_value = MagicMock()

    executor = Executor(execution_timeout=10)

    with pytest.raises(DiffPlexilvTimeoutError, match="timed out after 10s"):
        executor.run_plexiltest(
            test_files['plan'],
            test_files['script'],
            test_files['debug_cfg'],
            test_files['test_dir'],
            "plan"
        )


@patch('plexilv_diff.executor.local')
def test_run_plexilv_success(mock_local, test_files):
    """Test successful plexilv execution."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "Test execution log", "")

    mock_plexilv = MagicMock()
    mock_plexilv.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexilv
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    result = executor.run_plexilv(
        test_files['plan'],
        test_files['script'],
        test_files['test_dir'],
        "plan"
    )

    assert result.success is True
    assert result.returncode == 0
    assert result.log_file == test_files['test_dir'] / "plan.plexilv.log"
    assert result.log_file.exists()
    assert "Test execution log" in result.log_file.read_text()


@patch('plexilv_diff.executor.local')
def test_run_plexilv_with_flags(mock_local, test_files):
    """Test plexilv execution with additional flags."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "output", "")

    mock_plexilv = MagicMock()
    mock_plexilv.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexilv
    mock_local.cwd.return_value = MagicMock()

    executor = Executor(plexilv_run_flags="--verbose")
    executor.run_plexilv(
        test_files['plan'],
        test_files['script'],
        test_files['test_dir'],
        "plan"
    )

    # Verify flags were passed
    call_args = mock_plexilv.__getitem__.call_args[0][0]
    assert "--verbose" in call_args
    assert "-p" in call_args
    assert "-s" in call_args


@patch('plexilv_diff.executor.local')
def test_run_plexilv_nonzero_exit(mock_local, test_files):
    """Test plexilv execution with non-zero exit code."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (1, "output", "error message")

    mock_plexilv = MagicMock()
    mock_plexilv.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexilv
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    result = executor.run_plexilv(
        test_files['plan'],
        test_files['script'],
        test_files['test_dir'],
        "plan"
    )

    # Should not raise, but mark as failed
    assert result.success is False
    assert result.returncode == 1
    assert result.log_file.exists()


@patch('plexilv_diff.executor.local')
def test_run_plexilv_timeout(mock_local, test_files):
    """Test plexilv timeout."""
    mock_run_result = MagicMock()
    mock_run_result.run.side_effect = ProcessTimedOut(["plexilv-run"], 124)

    mock_plexilv = MagicMock()
    mock_plexilv.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexilv
    mock_local.cwd.return_value = MagicMock()

    executor = Executor(execution_timeout=10)

    with pytest.raises(DiffPlexilvTimeoutError, match="timed out after 10s"):
        executor.run_plexilv(
            test_files['plan'],
            test_files['script'],
            test_files['test_dir'],
            "plan"
        )


@patch('plexilv_diff.executor.local')
def test_run_both_sequential(mock_local, test_files):
    """Test running both interpreters sequentially."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "output", "")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    plexiltest_result, plexilv_result = executor.run_both(
        test_files['plan'],
        test_files['script'],
        test_files['debug_cfg'],
        test_files['test_dir'],
        "plan",
        parallel=False
    )

    assert plexiltest_result.success is True
    assert plexilv_result.success is True
    assert plexiltest_result.log_file.exists()
    assert plexilv_result.log_file.exists()


@patch('plexilv_diff.executor.local')
def test_run_both_parallel(mock_local, test_files):
    """Test running both interpreters in parallel."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "output", "")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    plexiltest_result, plexilv_result = executor.run_both(
        test_files['plan'],
        test_files['script'],
        test_files['debug_cfg'],
        test_files['test_dir'],
        "plan",
        parallel=True
    )

    assert plexiltest_result.success is True
    assert plexilv_result.success is True
    assert plexiltest_result.log_file.exists()
    assert plexilv_result.log_file.exists()


@patch('plexilv_diff.executor.local')
def test_run_both_one_fails(mock_local, test_files):
    """Test running both interpreters when one fails."""
    call_count = [0]

    def mock_run(*args, **kwargs):
        call_count[0] += 1
        if call_count[0] == 1:
            return (0, "plexiltest output", "")
        else:
            return (1, "plexilv output", "plexilv error")

    mock_run_result = MagicMock()
    mock_run_result.run.side_effect = mock_run

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    plexiltest_result, plexilv_result = executor.run_both(
        test_files['plan'],
        test_files['script'],
        test_files['debug_cfg'],
        test_files['test_dir'],
        "plan",
        parallel=False
    )

    assert plexiltest_result.success is True
    assert plexilv_result.success is False
    assert plexiltest_result.log_file.exists()
    assert plexilv_result.log_file.exists()


@patch('plexilv_diff.executor.local')
def test_log_file_contains_stderr(mock_local, test_files):
    """Test that log file contains both stdout and stderr."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "stdout content", "stderr content")

    mock_plexiltest = MagicMock()
    mock_plexiltest.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_plexiltest
    mock_local.cwd.return_value = MagicMock()

    executor = Executor()
    result = executor.run_plexiltest(
        test_files['plan'],
        test_files['script'],
        test_files['debug_cfg'],
        test_files['test_dir'],
        "plan"
    )

    log_content = result.log_file.read_text()
    assert "stdout content" in log_content
    assert "stderr content" in log_content
    assert "STDERR" in log_content