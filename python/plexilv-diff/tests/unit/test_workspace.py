"""Tests for workspace management."""

import pytest
from pathlib import Path
from datetime import datetime
from plexilv_diff.workspace import Workspace
from plexilv_diff.exceptions import WorkspaceError


def test_workspace_auto_created(tmp_path, monkeypatch):
    """Test workspace with auto-generated name."""
    # Change to tmp directory so workspace is created there
    monkeypatch.chdir(tmp_path)

    workspace = Workspace()

    assert workspace.path.exists()
    assert workspace.path.is_dir()
    assert workspace._auto_created is True
    assert workspace.path.name.startswith("diff-")

    # Cleanup
    workspace.cleanup()


def test_workspace_auto_created_name_format(tmp_path, monkeypatch):
    """Test that auto-generated name follows timestamp format."""
    monkeypatch.chdir(tmp_path)

    workspace = Workspace()

    # Name should be: diff-YYYY-MM-DD-HH-MM-SS
    # Split only on the last 6 dashes (the timestamp part)
    name = workspace.path.name

    # Check it starts with the prefix
    assert name.startswith("diff-")

    # Extract timestamp part (everything after "diff-")
    timestamp_part = name[len("diff-"):]
    timestamp_parts = timestamp_part.split("-")

    # Should have 6 parts: YYYY, MM, DD, HH, MM, SS
    assert len(timestamp_parts) == 6, f"Expected 6 timestamp parts, got {len(timestamp_parts)}: {timestamp_parts}"

    year, month, day, hour, minute, second = timestamp_parts

    # Check each part is numeric and correct length
    assert year.isdigit() and len(year) == 4, f"Invalid year: {year}"
    assert month.isdigit() and len(month) == 2, f"Invalid month: {month}"
    assert day.isdigit() and len(day) == 2, f"Invalid day: {day}"
    assert hour.isdigit() and len(hour) == 2, f"Invalid hour: {hour}"
    assert minute.isdigit() and len(minute) == 2, f"Invalid minute: {minute}"
    assert second.isdigit() and len(second) == 2, f"Invalid second: {second}"

    # Verify values are in valid ranges
    assert 1 <= int(month) <= 12, f"Month out of range: {month}"
    assert 1 <= int(day) <= 31, f"Day out of range: {day}"
    assert 0 <= int(hour) <= 23, f"Hour out of range: {hour}"
    assert 0 <= int(minute) <= 59, f"Minute out of range: {minute}"
    assert 0 <= int(second) <= 59, f"Second out of range: {second}"

    workspace.cleanup()


def test_workspace_custom_path(tmp_path):
    """Test workspace with custom path."""
    custom_path = tmp_path / "my-workspace"

    workspace = Workspace(work_dir=custom_path)

    assert workspace.path == custom_path
    assert workspace.path.exists()
    assert workspace._auto_created is False


def test_workspace_already_exists_error(tmp_path):
    """Test error when workspace directory already exists."""
    existing_dir = tmp_path / "existing"
    existing_dir.mkdir()

    with pytest.raises(WorkspaceError, match="already exists"):
        Workspace(work_dir=existing_dir)


def test_workspace_copy_file(tmp_path):
    """Test copying a file to workspace root."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    src_file = tmp_path / "source.txt"
    src_file.write_text("test content")

    dest = workspace.copy_file(src_file)

    assert dest.exists()
    assert dest.parent == workspace.path
    assert dest.name == "source.txt"
    assert dest.read_text() == "test content"


def test_workspace_copy_file_custom_name(tmp_path):
    """Test copying a file with custom destination name."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    src_file = tmp_path / "source.txt"
    src_file.write_text("test content")

    dest = workspace.copy_file(src_file, dest_name="renamed.txt")

    assert dest.name == "renamed.txt"
    assert dest.read_text() == "test content"


def test_workspace_copy_file_not_exists(tmp_path):
    """Test error when source file doesn't exist."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    with pytest.raises(WorkspaceError, match="does not exist"):
        workspace.copy_file(Path("/nonexistent/file.txt"))


def test_workspace_copy_file_already_exists(tmp_path):
    """Test error when destination file already exists."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    src_file = tmp_path / "source.txt"
    src_file.write_text("test content")

    # Copy once
    workspace.copy_file(src_file)

    # Try to copy again
    with pytest.raises(WorkspaceError, match="already exists"):
        workspace.copy_file(src_file)


def test_workspace_create_test_directory(tmp_path):
    """Test creating numbered test subdirectories."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    test_dir_1 = workspace.create_test_directory(1)
    test_dir_2 = workspace.create_test_directory(2)
    test_dir_10 = workspace.create_test_directory(10)

    assert test_dir_1.exists()
    assert test_dir_1.name == "001"
    assert test_dir_2.name == "002"
    assert test_dir_10.name == "010"


def test_workspace_create_test_directory_already_exists(tmp_path):
    """Test error when test directory already exists."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    workspace.create_test_directory(1)

    with pytest.raises(WorkspaceError, match="already exists"):
        workspace.create_test_directory(1)


def test_workspace_copy_file_to_test_dir(tmp_path):
    """Test copying a file to test subdirectory."""
    workspace = Workspace(work_dir=tmp_path / "workspace")
    workspace.create_test_directory(1)

    src_file = tmp_path / "test.txt"
    src_file.write_text("test content")

    dest = workspace.copy_file_to_test_dir(src_file, 1)

    assert dest.exists()
    assert dest.parent.name == "001"
    assert dest.name == "test.txt"
    assert dest.read_text() == "test content"


def test_workspace_copy_file_to_test_dir_custom_name(tmp_path):
    """Test copying file to test dir with custom name."""
    workspace = Workspace(work_dir=tmp_path / "workspace")
    workspace.create_test_directory(1)

    src_file = tmp_path / "test.txt"
    src_file.write_text("test content")

    dest = workspace.copy_file_to_test_dir(src_file, 1, dest_name="renamed.txt")

    assert dest.name == "renamed.txt"


def test_workspace_copy_file_to_test_dir_not_exists(tmp_path):
    """Test error when test directory doesn't exist."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    src_file = tmp_path / "test.txt"
    src_file.write_text("test content")

    with pytest.raises(WorkspaceError, match="does not exist"):
        workspace.copy_file_to_test_dir(src_file, 1)


def test_workspace_copy_file_to_test_dir_source_not_exists(tmp_path):
    """Test error when source file doesn't exist."""
    workspace = Workspace(work_dir=tmp_path / "workspace")
    workspace.create_test_directory(1)

    with pytest.raises(WorkspaceError, match="does not exist"):
        workspace.copy_file_to_test_dir(Path("/nonexistent.txt"), 1)


def test_workspace_copy_file_to_test_dir_already_exists(tmp_path):
    """Test error when destination in test dir already exists."""
    workspace = Workspace(work_dir=tmp_path / "workspace")
    workspace.create_test_directory(1)

    src_file = tmp_path / "test.txt"
    src_file.write_text("test content")

    workspace.copy_file_to_test_dir(src_file, 1)

    with pytest.raises(WorkspaceError, match="already exists"):
        workspace.copy_file_to_test_dir(src_file, 1)


def test_workspace_create_debug_config(tmp_path):
    """Test creating debug.cfg file."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    debug_cfg = workspace.create_debug_config()

    assert debug_cfg.exists()
    assert debug_cfg.name == "debug.cfg"
    assert debug_cfg.parent == workspace.path

    content = debug_cfg.read_text()
    assert "PlexilExec:step" in content
    assert "PlexilExec:cycle" in content


def test_workspace_create_debug_config_already_exists(tmp_path):
    """Test error when debug.cfg already exists."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    workspace.create_debug_config()

    with pytest.raises(WorkspaceError, match="already exists"):
        workspace.create_debug_config()


def test_workspace_get_test_directory(tmp_path):
    """Test getting test directory path."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    test_dir = workspace.get_test_directory(5)

    assert test_dir == workspace.path / "005"
    # Note: directory doesn't exist yet, just returns the path


def test_workspace_write_summary(tmp_path):
    """Test writing summary report."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    summary_content = "Test Summary\n============\n8/10 tests passed"
    summary_file = workspace.write_summary(summary_content)

    assert summary_file.exists()
    assert summary_file.name == "summary.txt"
    assert summary_file.parent == workspace.path
    assert summary_file.read_text() == summary_content


def test_workspace_write_summary_overwrite(tmp_path):
    """Test that write_summary can overwrite existing file."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    workspace.write_summary("First version")
    summary_file = workspace.write_summary("Second version")

    assert summary_file.read_text() == "Second version"


def test_workspace_cleanup(tmp_path):
    """Test cleanup removes workspace directory."""
    workspace = Workspace(work_dir=tmp_path / "workspace")
    workspace.create_debug_config()
    workspace.create_test_directory(1)

    assert workspace.path.exists()

    workspace.cleanup()

    assert not workspace.path.exists()


def test_workspace_cleanup_already_cleaned(tmp_path):
    """Test cleanup on already-cleaned workspace doesn't error."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    workspace.cleanup()
    # Call again - should not raise
    workspace.cleanup()


def test_workspace_str(tmp_path):
    """Test string representation."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    assert str(workspace) == str(tmp_path / "workspace")


def test_workspace_repr(tmp_path):
    """Test developer representation."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    repr_str = repr(workspace)
    assert "Workspace" in repr_str
    assert str(workspace.path) in repr_str
    assert "auto_created=False" in repr_str


def test_workspace_integration_full_setup(tmp_path):
    """Integration test: full workspace setup."""
    workspace = Workspace(work_dir=tmp_path / "workspace")

    # Create debug config
    debug_cfg = workspace.create_debug_config()
    assert debug_cfg.exists()

    # Copy plan file
    plan = tmp_path / "plan.ple"
    plan.write_text("// PLEXIL plan")
    copied_plan = workspace.copy_file(plan)
    assert copied_plan.exists()

    # Create test directories and copy scripts
    for i in range(1, 4):
        test_dir = workspace.create_test_directory(i)

        script = tmp_path / f"test{i}.pst"
        script.write_text(f"// Test script {i}")
        copied_script = workspace.copy_file_to_test_dir(script, i)

        assert test_dir.exists()
        assert copied_script.exists()

    # Write summary
    summary = workspace.write_summary("All tests completed")
    assert summary.exists()

    # Verify structure
    assert (workspace.path / "debug.cfg").exists()
    assert (workspace.path / "plan.ple").exists()
    assert (workspace.path / "001").exists()
    assert (workspace.path / "002").exists()
    assert (workspace.path / "003").exists()
    assert (workspace.path / "001" / "test1.pst").exists()
    assert (workspace.path / "summary.txt").exists()

    # Cleanup
    workspace.cleanup()
    assert not workspace.path.exists()