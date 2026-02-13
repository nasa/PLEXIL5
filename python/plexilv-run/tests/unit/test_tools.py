"""Unit tests for tool availability checking."""

import os
import stat
from pathlib import Path

import pytest

from plexilv_run.exceptions import ToolNotFoundError
from plexilv_run.tools import check_all_tools, check_tool, get_tool_path


@pytest.mark.unit
class TestGetToolPath:
    """Test get_tool_path function."""

    def test_custom_path_provided(self):
        """Custom path should be returned as-is."""
        custom = "/custom/path/to/tool"
        result = get_tool_path("sometool", custom)
        assert result == custom

    def test_tool_in_path(self, tmp_path, monkeypatch):
        """Tool in PATH should be found."""
        # Create a mock tool
        tool_dir = tmp_path / "bin"
        tool_dir.mkdir()
        tool = tool_dir / "mytool"
        tool.touch()
        tool.chmod(tool.stat().st_mode | stat.S_IEXEC)

        # Add to PATH
        monkeypatch.setenv("PATH", str(tool_dir))

        result = get_tool_path("mytool")
        assert result == str(tool)

    def test_tool_not_in_path(self, monkeypatch):
        """Tool not in PATH should raise error."""
        monkeypatch.setenv("PATH", "")

        with pytest.raises(ToolNotFoundError, match="not found in PATH"):
            get_tool_path("nonexistent")

    def test_custom_path_takes_precedence(self, tmp_path, monkeypatch):
        """Custom path should take precedence over PATH."""
        # Create tool in PATH
        tool_dir = tmp_path / "bin"
        tool_dir.mkdir()
        tool_path = tool_dir / "mytool"
        tool_path.touch()

        monkeypatch.setenv("PATH", str(tool_dir))

        # Use different custom path
        custom = "/custom/path/to/mytool"
        result = get_tool_path("mytool", custom)
        assert result == custom


@pytest.mark.unit
class TestCheckTool:
    """Test check_tool function."""

    def test_valid_tool_in_path(self, tmp_path, monkeypatch):
        """Valid executable tool should pass."""
        # Create executable tool
        tool_dir = tmp_path / "bin"
        tool_dir.mkdir()
        tool = tool_dir / "mytool"
        tool.touch()
        tool.chmod(tool.stat().st_mode | stat.S_IEXEC)

        monkeypatch.setenv("PATH", str(tool_dir))

        result = check_tool("mytool")
        assert Path(result) == tool

    def test_valid_custom_path(self, tmp_path):
        """Valid custom path should pass."""
        tool = tmp_path / "mytool"
        tool.touch()
        tool.chmod(tool.stat().st_mode | stat.S_IEXEC)

        result = check_tool("mytool", str(tool))
        assert Path(result) == tool

    def test_tool_not_found_in_path(self, monkeypatch):
        """Tool not in PATH should raise error."""
        monkeypatch.setenv("PATH", "")

        with pytest.raises(ToolNotFoundError, match="not found in PATH"):
            check_tool("nonexistent")

    def test_custom_path_does_not_exist(self):
        """Non-existent custom path should raise error."""
        with pytest.raises(ToolNotFoundError, match="not found"):
            check_tool("mytool", "/nonexistent/path/to/tool")

    def test_custom_path_not_executable(self, tmp_path):
        """Non-executable custom path should raise error."""
        tool = tmp_path / "mytool"
        tool.touch()
        tool.chmod(0o644)  # Read/write but not executable

        with pytest.raises(ToolNotFoundError, match="not executable"):
            check_tool("mytool", str(tool))

    def test_error_includes_tool_name(self, monkeypatch):
        """Error should include tool name."""
        monkeypatch.setenv("PATH", "")

        with pytest.raises(ToolNotFoundError) as exc_info:
            check_tool("mytool")

        assert exc_info.value.tool_name == "mytool"


@pytest.mark.unit
class TestCheckAllTools:
    """Test check_all_tools function."""

    def create_mock_tools(self, tmp_path):
        """Helper to create mock executable tools."""
        tool_dir = tmp_path / "bin"
        tool_dir.mkdir()

        tools = {}
        for name in ["plx2maude", "psx2maude", "maude"]:
            tool = tool_dir / name
            tool.touch()
            tool.chmod(tool.stat().st_mode | stat.S_IEXEC)
            tools[name] = tool

        return tool_dir, tools

    def test_all_tools_available(self, tmp_path, monkeypatch):
        """All tools available should return their paths."""
        tool_dir, tools = self.create_mock_tools(tmp_path)
        monkeypatch.setenv("PATH", str(tool_dir))

        plx2maude, psx2maude, maude = check_all_tools()

        assert Path(plx2maude) == tools['plx2maude']
        assert Path(psx2maude) == tools['psx2maude']
        assert Path(maude) == tools['maude']

    def test_with_custom_paths(self, tmp_path):
        """Custom paths should be used."""
        _, tools = self.create_mock_tools(tmp_path)

        plx2maude, psx2maude, maude = check_all_tools(
            plx2maude_path=str(tools['plx2maude']),
            psx2maude_path=str(tools['psx2maude']),
            maude_path=str(tools['maude']),
        )

        assert Path(plx2maude) == tools['plx2maude']
        assert Path(psx2maude) == tools['psx2maude']
        assert Path(maude) == tools['maude']

    def test_missing_plx2maude(self, monkeypatch):
        """Missing plx2maude should raise with installation hint."""
        monkeypatch.setenv("PATH", "")

        with pytest.raises(ToolNotFoundError, match="Missing PLEXIL-V tools"):
            check_all_tools()

    def test_missing_psx2maude(self, tmp_path, monkeypatch):
        """Missing psx2maude should raise with installation hint."""
        tool_dir, tools = self.create_mock_tools(tmp_path)
        tools['psx2maude'].unlink()  # Remove psx2maude

        monkeypatch.setenv("PATH", str(tool_dir))

        with pytest.raises(ToolNotFoundError, match="Missing PLEXIL-V tools"):
            check_all_tools()

    def test_missing_maude(self, tmp_path, monkeypatch):
        """Missing maude should raise with installation hint."""
        tool_dir, tools = self.create_mock_tools(tmp_path)
        tools['maude'].unlink()  # Remove maude

        monkeypatch.setenv("PATH", str(tool_dir))

        with pytest.raises(ToolNotFoundError, match="Missing Maude"):
            check_all_tools()

    def test_multiple_missing_tools(self, monkeypatch):
        """Multiple missing tools should report all errors."""
        monkeypatch.setenv("PATH", "")

        with pytest.raises(ToolNotFoundError) as exc_info:
            check_all_tools()

        error_msg = str(exc_info.value)
        # Should mention both PLEXIL-V tools and Maude
        assert "PLEXIL-V tools" in error_msg
        assert "Maude" in error_msg

    def test_missing_plexilv_and_maude_separate(self, tmp_path, monkeypatch):
        """Missing PLEXIL-V and Maude tools should be grouped separately."""
        # Only create plx2maude
        tool_dir = tmp_path / "bin"
        tool_dir.mkdir()
        plx2maude = tool_dir / "plx2maude"
        plx2maude.touch()
        plx2maude.chmod(plx2maude.stat().st_mode | stat.S_IEXEC)

        monkeypatch.setenv("PATH", str(tool_dir))

        with pytest.raises(ToolNotFoundError) as exc_info:
            check_all_tools()

        error_msg = str(exc_info.value)
        # Should have separate sections
        assert "PLEXIL-V tools" in error_msg
        assert "Maude" in error_msg
        # psx2maude should be in PLEXIL-V section
        assert "psx2maude" in error_msg

    def test_custom_path_not_executable(self, tmp_path):
        """Non-executable custom path should be reported."""
        tool_dir, tools = self.create_mock_tools(tmp_path)

        # Create non-executable plx2maude
        non_exec = tmp_path / "non-exec-plx2maude"
        non_exec.touch()
        non_exec.chmod(0o644)  # Make not executable

        with pytest.raises(ToolNotFoundError, match="not executable"):
            check_all_tools(
                plx2maude_path=str(non_exec),
                psx2maude_path=str(tools['psx2maude']),
                maude_path=str(tools['maude']),
            )

    def test_mixed_custom_and_path(self, tmp_path, monkeypatch):
        """Should work with mix of custom paths and PATH lookup."""
        tool_dir, tools = self.create_mock_tools(tmp_path)
        monkeypatch.setenv("PATH", str(tool_dir))

        # Use custom path for plx2maude, PATH for others
        plx2maude, psx2maude, maude = check_all_tools(
            plx2maude_path=str(tools['plx2maude'])
        )

        assert Path(plx2maude) == tools['plx2maude']
        assert Path(psx2maude) == tools['psx2maude']
        assert Path(maude) == tools['maude']