"""Unit tests for custom exceptions."""

import pytest
from plexilv_run.exceptions import (
    PlexilvRunError,
    ConfigurationError,
    ValidationError,
    WorkspaceError,
    ToolNotFoundError,
    ConversionError,
    ConversionTimeoutError,
    MaudeError,
    MaudeTimeoutError,
)


@pytest.mark.unit
class TestExceptionHierarchy:
    """Test exception inheritance."""

    def test_all_inherit_from_plexilv_run_error(self):
        """All custom exceptions should inherit from PlexilvRunError."""
        assert issubclass(ConfigurationError, PlexilvRunError)
        assert issubclass(ValidationError, PlexilvRunError)
        assert issubclass(WorkspaceError, PlexilvRunError)
        assert issubclass(ToolNotFoundError, PlexilvRunError)
        assert issubclass(ConversionError, PlexilvRunError)
        assert issubclass(ConversionTimeoutError, PlexilvRunError)
        assert issubclass(MaudeError, PlexilvRunError)
        assert issubclass(MaudeTimeoutError, PlexilvRunError)

    def test_plexilv_run_error_inherits_from_exception(self):
        """PlexilvRunError should inherit from Exception."""
        assert issubclass(PlexilvRunError, Exception)


@pytest.mark.unit
class TestBasicExceptions:
    """Test basic exceptions without custom attributes."""

    def test_configuration_error(self):
        """ConfigurationError can be raised with a message."""
        with pytest.raises(ConfigurationError, match="config problem"):
            raise ConfigurationError("config problem")

    def test_validation_error(self):
        """ValidationError can be raised with a message."""
        with pytest.raises(ValidationError, match="invalid input"):
            raise ValidationError("invalid input")

    def test_workspace_error(self):
        """WorkspaceError can be raised with a message."""
        with pytest.raises(WorkspaceError, match="workspace issue"):
            raise WorkspaceError("workspace issue")


@pytest.mark.unit
class TestToolNotFoundError:
    """Test ToolNotFoundError with custom attributes."""

    def test_stores_tool_name(self):
        """ToolNotFoundError should store tool name."""
        error = ToolNotFoundError("plx2maude", "Tool not found")
        assert error.tool_name == "plx2maude"

    def test_message(self):
        """ToolNotFoundError should have the provided message."""
        error = ToolNotFoundError("plx2maude", "Tool not found in PATH")
        assert str(error) == "Tool not found in PATH"

    def test_can_be_caught(self):
        """ToolNotFoundError can be caught and attributes accessed."""
        with pytest.raises(ToolNotFoundError) as exc_info:
            raise ToolNotFoundError("psx2maude", "Missing tool")

        assert exc_info.value.tool_name == "psx2maude"
        assert "Missing tool" in str(exc_info.value)


@pytest.mark.unit
class TestConversionError:
    """Test ConversionError with custom attributes."""

    def test_stores_attributes(self):
        """ConversionError should store tool name, exit code, and stderr."""
        error = ConversionError("plx2maude", 1, "Parse error")
        assert error.tool_name == "plx2maude"
        assert error.exit_code == 1
        assert error.stderr == "Parse error"

    def test_message_format(self):
        """ConversionError should format message with tool name and exit code."""
        error = ConversionError("plx2maude", 2, "Some error")
        assert str(error) == "plx2maude failed with exit code 2"

    def test_can_be_caught(self):
        """ConversionError can be caught and attributes accessed."""
        with pytest.raises(ConversionError) as exc_info:
            raise ConversionError("psx2maude", 3, "stderr output")

        assert exc_info.value.tool_name == "psx2maude"
        assert exc_info.value.exit_code == 3
        assert exc_info.value.stderr == "stderr output"


@pytest.mark.unit
class TestConversionTimeoutError:
    """Test ConversionTimeoutError with custom attributes."""

    def test_stores_attributes(self):
        """ConversionTimeoutError should store tool name and timeout."""
        error = ConversionTimeoutError("plx2maude", 30)
        assert error.tool_name == "plx2maude"
        assert error.timeout == 30

    def test_message_format(self):
        """ConversionTimeoutError should format message with tool name and timeout."""
        error = ConversionTimeoutError("psx2maude", 60)
        assert str(error) == "psx2maude timed out after 60 seconds"

    def test_can_be_caught(self):
        """ConversionTimeoutError can be caught and attributes accessed."""
        with pytest.raises(ConversionTimeoutError) as exc_info:
            raise ConversionTimeoutError("plx2maude", 45)

        assert exc_info.value.tool_name == "plx2maude"
        assert exc_info.value.timeout == 45


@pytest.mark.unit
class TestMaudeError:
    """Test MaudeError with custom attributes."""

    def test_stores_attributes(self):
        """MaudeError should store exit code and stderr."""
        error = MaudeError(1, "execution error")
        assert error.exit_code == 1
        assert error.stderr == "execution error"

    def test_message_format(self):
        """MaudeError should format message with exit code."""
        error = MaudeError(5, "some stderr")
        assert str(error) == "Maude execution failed with exit code 5"

    def test_can_be_caught(self):
        """MaudeError can be caught and attributes accessed."""
        with pytest.raises(MaudeError) as exc_info:
            raise MaudeError(2, "stderr content")

        assert exc_info.value.exit_code == 2
        assert exc_info.value.stderr == "stderr content"


@pytest.mark.unit
class TestMaudeTimeoutError:
    """Test MaudeTimeoutError with custom attributes."""

    def test_stores_timeout(self):
        """MaudeTimeoutError should store timeout."""
        error = MaudeTimeoutError(300)
        assert error.timeout == 300

    def test_message_format(self):
        """MaudeTimeoutError should format message with timeout."""
        error = MaudeTimeoutError(600)
        assert str(error) == "Maude execution timed out after 600 seconds"

    def test_can_be_caught(self):
        """MaudeTimeoutError can be caught and attributes accessed."""
        with pytest.raises(MaudeTimeoutError) as exc_info:
            raise MaudeTimeoutError(120)

        assert exc_info.value.timeout == 120