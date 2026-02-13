"""Tests for PLEXIL log differ."""

import pytest
from pathlib import Path
from unittest.mock import Mock, patch, MagicMock
from plumbum.commands.processes import ProcessTimedOut

from plexilv_diff.differ import Differ, DiffResult
from plexilv_diff.exceptions import DiffError, TimeoutError as DiffPlexilvTimeoutError


@pytest.fixture
def log_files(tmp_path):
    """Create sample log files."""
    log1 = tmp_path / "test.plexil.log"
    log1.write_text("Log from plexiltest\nLine 2\nLine 3")

    log2 = tmp_path / "test.plexilv.log"
    log2.write_text("Log from plexilv\nLine 2\nLine 3")

    output = tmp_path / "test.log.diff"

    return {
        'log1': log1,
        'log2': log2,
        'output': output
    }


@patch('plexilv_diff.differ.local')
def test_differ_init_defaults(mock_local):
    """Test differ initialization with defaults."""
    mock_local.__getitem__.return_value = MagicMock()

    differ = Differ()

    assert differ.plexilog_diff_path == "plexilog-diff"
    assert differ.additional_flags == []
    assert differ.timeout == 60


@patch('plexilv_diff.differ.local')
def test_differ_init_custom(mock_local):
    """Test differ initialization with custom values."""
    mock_local.__getitem__.return_value = MagicMock()

    differ = Differ(
        plexilog_diff_path="/usr/local/bin/plexilog-diff",
        additional_flags="--verbose --color",
        timeout=120
    )

    assert differ.plexilog_diff_path == "/usr/local/bin/plexilog-diff"
    assert differ.additional_flags == ["--verbose", "--color"]
    assert differ.timeout == 120


@patch('plexilv_diff.differ.local')
def test_differ_parse_flags_empty(mock_local):
    """Test parsing empty flags."""
    mock_local.__getitem__.return_value = MagicMock()

    differ = Differ()

    assert differ._parse_flags("") == []
    assert differ._parse_flags("   ") == []


@patch('plexilv_diff.differ.local')
def test_differ_parse_flags_multiple(mock_local):
    """Test parsing multiple flags."""
    mock_local.__getitem__.return_value = MagicMock()

    differ = Differ()

    flags = differ._parse_flags("--flag1 --flag2 value")
    assert flags == ["--flag1", "--flag2", "value"]


def test_diff_result_dataclass():
    """Test DiffResult dataclass."""
    diff_file = Path("/tmp/test.diff")
    result = DiffResult(
        diff_file=diff_file,
        identical=True,
        differ=False,
        error=False,
        returncode=0,
        output="No differences"
    )

    assert result.diff_file == diff_file
    assert result.identical is True
    assert result.differ is False
    assert result.error is False
    assert result.returncode == 0
    assert result.output == "No differences"


@patch('plexilv_diff.differ.local')
def test_compare_logs_identical(mock_local, log_files):
    """Test comparing identical logs (exit code 0)."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "No differences found", "")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ()
    result = differ.compare_logs(
        log_files['log1'],
        log_files['log2'],
        log_files['output']
    )

    assert result.identical is True
    assert result.differ is False
    assert result.error is False
    assert result.returncode == 0
    assert result.diff_file.exists()
    assert "No differences found" in result.diff_file.read_text()


@patch('plexilv_diff.differ.local')
def test_compare_logs_differ(mock_local, log_files):
    """Test comparing different logs (exit code 1)."""
    diff_output = "< Line from plexiltest\n> Line from plexilv"

    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (1, diff_output, "")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ()
    result = differ.compare_logs(
        log_files['log1'],
        log_files['log2'],
        log_files['output']
    )

    assert result.identical is False
    assert result.differ is True
    assert result.error is False
    assert result.returncode == 1
    assert result.diff_file.exists()
    assert diff_output in result.diff_file.read_text()


@patch('plexilv_diff.differ.local')
def test_compare_logs_error(mock_local, log_files):
    """Test diff tool error (exit code 2)."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (2, "", "Internal error")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ()

    with pytest.raises(DiffError, match="encountered an error"):
        differ.compare_logs(
            log_files['log1'],
            log_files['log2'],
            log_files['output']
        )


@patch('plexilv_diff.differ.local')
def test_compare_logs_with_flags(mock_local, log_files):
    """Test comparing logs with additional flags."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (0, "No differences", "")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ(additional_flags="--verbose --color")
    differ.compare_logs(
        log_files['log1'],
        log_files['log2'],
        log_files['output']
    )

    # Verify flags were passed
    call_args = mock_cmd.__getitem__.call_args[0][0]
    assert "--verbose" in call_args
    assert "--color" in call_args
    assert str(log_files['log1']) in call_args
    assert str(log_files['log2']) in call_args


@patch('plexilv_diff.differ.local')
def test_compare_logs_timeout(mock_local, log_files):
    """Test diff timeout."""
    mock_run_result = MagicMock()
    mock_run_result.run.side_effect = ProcessTimedOut(["plexilog-diff"], 124)

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ(timeout=10)

    with pytest.raises(DiffPlexilvTimeoutError, match="timed out after 10s"):
        differ.compare_logs(
            log_files['log1'],
            log_files['log2'],
            log_files['output']
        )


@patch('plexilv_diff.differ.local')
def test_compare_logs_first_log_not_exists(mock_local, tmp_path):
    """Test error when first log file doesn't exist."""
    mock_local.__getitem__.return_value = MagicMock()

    differ = Differ()

    log2 = tmp_path / "log2.log"
    log2.write_text("content")
    output = tmp_path / "output.diff"

    with pytest.raises(DiffError, match="First log file does not exist"):
        differ.compare_logs(
            tmp_path / "nonexistent.log",
            log2,
            output
        )


@patch('plexilv_diff.differ.local')
def test_compare_logs_second_log_not_exists(mock_local, tmp_path):
    """Test error when second log file doesn't exist."""
    mock_local.__getitem__.return_value = MagicMock()

    differ = Differ()

    log1 = tmp_path / "log1.log"
    log1.write_text("content")
    output = tmp_path / "output.diff"

    with pytest.raises(DiffError, match="Second log file does not exist"):
        differ.compare_logs(
            log1,
            tmp_path / "nonexistent.log",
            output
        )


@patch('plexilv_diff.differ.local')
def test_compare_logs_output_written(mock_local, log_files):
    """Test that diff output is written to file."""
    diff_output = "Detailed diff output\nLine 1\nLine 2"

    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (1, diff_output, "")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ()
    result = differ.compare_logs(
        log_files['log1'],
        log_files['log2'],
        log_files['output']
    )

    assert result.diff_file.exists()
    content = result.diff_file.read_text()
    assert content == diff_output
    assert "Detailed diff output" in content


@patch('plexilv_diff.differ.local')
def test_compare_logs_exit_code_3(mock_local, log_files):
    """Test that exit code 3+ is treated as error."""
    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (3, "", "Fatal error")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ()

    with pytest.raises(DiffError, match="exit code 3"):
        differ.compare_logs(
            log_files['log1'],
            log_files['log2'],
            log_files['output']
        )


@patch('plexilv_diff.differ.local')
def test_compare_logs_result_contains_output(mock_local, log_files):
    """Test that DiffResult contains the output."""
    diff_output = "Some diff output"

    mock_run_result = MagicMock()
    mock_run_result.run.return_value = (1, diff_output, "")

    mock_cmd = MagicMock()
    mock_cmd.__getitem__.return_value = mock_run_result

    mock_local.__getitem__.return_value = mock_cmd

    differ = Differ()
    result = differ.compare_logs(
        log_files['log1'],
        log_files['log2'],
        log_files['output']
    )

    assert result.output == diff_output