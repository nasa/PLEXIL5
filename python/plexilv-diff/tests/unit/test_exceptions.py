"""Tests for custom exceptions."""

import pytest
from plexilv_diff.exceptions import (
    DiffPlexilvError,
    WorkspaceError,
    CompilationError,
    ExecutionError,
    DiffError,
    TimeoutError,
    ValidationError,
    ConfigurationError,
)


def test_base_exception():
    """Test that DiffPlexilvError can be instantiated and raised."""
    error = DiffPlexilvError("Base error message")
    assert str(error) == "Base error message"

    with pytest.raises(DiffPlexilvError, match="Base error message"):
        raise error


def test_workspace_error():
    """Test WorkspaceError inherits from DiffPlexilvError."""
    error = WorkspaceError("Workspace creation failed")
    assert isinstance(error, DiffPlexilvError)
    assert str(error) == "Workspace creation failed"


def test_compilation_error_basic():
    """Test CompilationError with just a message."""
    error = CompilationError("Compilation failed")
    assert str(error) == "Compilation failed"
    assert error.file_path is None
    assert error.stderr is None


def test_compilation_error_with_details():
    """Test CompilationError with file path and stderr."""
    error = CompilationError(
        "Compilation failed",
        file_path="/path/to/plan.ple",
        stderr="syntax error on line 42"
    )
    assert str(error) == "Compilation failed"
    assert error.file_path == "/path/to/plan.ple"
    assert error.stderr == "syntax error on line 42"


def test_execution_error_basic():
    """Test ExecutionError with just a message."""
    error = ExecutionError("Execution failed")
    assert str(error) == "Execution failed"
    assert error.interpreter is None
    assert error.stderr is None


def test_execution_error_with_details():
    """Test ExecutionError with interpreter name and stderr."""
    error = ExecutionError(
        "Execution timed out",
        interpreter="plexiltest",
        stderr="Node failed: timeout"
    )
    assert str(error) == "Execution timed out"
    assert error.interpreter == "plexiltest"
    assert error.stderr == "Node failed: timeout"


def test_diff_error_basic():
    """Test DiffError with just a message."""
    error = DiffError("Diff tool failed")
    assert str(error) == "Diff tool failed"
    assert error.stderr is None


def test_diff_error_with_stderr():
    """Test DiffError with stderr."""
    error = DiffError(
        "Diff tool crashed",
        stderr="Segmentation fault"
    )
    assert str(error) == "Diff tool crashed"
    assert error.stderr == "Segmentation fault"


def test_timeout_error_basic():
    """Test TimeoutError with just a message."""
    error = TimeoutError("Operation timed out")
    assert str(error) == "Operation timed out"
    assert error.timeout_seconds is None


def test_timeout_error_with_duration():
    """Test TimeoutError with timeout duration."""
    error = TimeoutError(
        "Compilation timed out",
        timeout_seconds=30
    )
    assert str(error) == "Compilation timed out"
    assert error.timeout_seconds == 30


def test_validation_error():
    """Test ValidationError inherits from DiffPlexilvError."""
    error = ValidationError("Tool not found: plexilc")
    assert isinstance(error, DiffPlexilvError)
    assert str(error) == "Tool not found: plexilc"


def test_configuration_error():
    """Test ConfigurationError inherits from DiffPlexilvError."""
    error = ConfigurationError("Invalid timeout value: -1")
    assert isinstance(error, DiffPlexilvError)
    assert str(error) == "Invalid timeout value: -1"


def test_all_exceptions_inherit_from_base():
    """Test that all custom exceptions inherit from DiffPlexilvError."""
    exceptions = [
        WorkspaceError("test"),
        CompilationError("test"),
        ExecutionError("test"),
        DiffError("test"),
        TimeoutError("test"),
        ValidationError("test"),
        ConfigurationError("test"),
    ]

    for exc in exceptions:
        assert isinstance(exc, DiffPlexilvError)


def test_can_catch_all_with_base_exception():
    """Test that all exceptions can be caught with base exception."""
    with pytest.raises(DiffPlexilvError):
        raise CompilationError("Test error")

    with pytest.raises(DiffPlexilvError):
        raise ExecutionError("Test error")

    with pytest.raises(DiffPlexilvError):
        raise WorkspaceError("Test error")