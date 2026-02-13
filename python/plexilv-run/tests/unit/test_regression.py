"""Regression tests for bugs that were found and fixed."""

import subprocess
from pathlib import Path
from unittest.mock import Mock, patch

import pytest

from plexilv_run.config import Config
from plexilv_run.tools import check_tool, get_tool_path
from plexilv_run.workspace import Workspace


@pytest.mark.unit
class TestToolPathRegression:
    """Regression tests for tool path resolution issues."""

    def test_tool_name_as_string_should_use_path_lookup(self, tmp_path, monkeypatch):
        """
        Regression: When tool path is just the tool name (e.g., 'plx2maude'),
        it should look in PATH, not treat it as a custom path.

        Bug: In cli.py we set plx2maude_path="plx2maude" as default,
        which was being treated as a custom path instead of triggering PATH lookup.

        Fix: get_tool_path() now checks if custom_path == name and uses PATH lookup.
        """
        # Create a mock tool in a directory
        tool_dir = tmp_path / "bin"
        tool_dir.mkdir()
        tool = tool_dir / "mytool"
        tool.write_text("#!/bin/bash\necho test")
        tool.chmod(tool.stat().st_mode | 0o111)

        # Add to PATH
        monkeypatch.setenv("PATH", str(tool_dir))

        # This should find the tool in PATH, not treat "mytool" as a file path
        result = get_tool_path("mytool", custom_path="mytool")

        assert result == str(tool)

    def test_actual_custom_path_is_used(self, tmp_path):
        """
        Regression: When a real custom path is provided (not just tool name),
        it should be used instead of PATH lookup.
        """
        # Create a tool at a custom location
        custom_tool = tmp_path / "custom" / "mytool"
        custom_tool.parent.mkdir()
        custom_tool.write_text("#!/bin/bash\necho test")
        custom_tool.chmod(custom_tool.stat().st_mode | 0o111)

        # Use custom path (not in PATH)
        result = check_tool("mytool", custom_path=str(custom_tool))

        assert result == str(custom_tool)


@pytest.mark.unit
class TestMaudeStdinRegression:
    """Regression tests for Maude stdin handling."""

    @patch('subprocess.run')
    def test_maude_subprocess_has_stdin_devnull(self, mock_run, tmp_path):
        """
        Regression: Maude was hanging because it was waiting for stdin input.

        Bug: subprocess.run() was called without stdin parameter, so Maude
        could wait for input and hang indefinitely.

        Fix: Added stdin=subprocess.DEVNULL to prevent Maude from reading stdin.
        """
        from plexilv_run.maude import MaudeRunner

        mock_run.return_value = Mock(
            returncode=0,
            stdout="output",
            stderr="stderr",
        )

        # Create minimal config and workspace
        plan = tmp_path / "test.plx"
        plan.touch()
        script = tmp_path / "test.psx"
        script.touch()
        semantics = tmp_path / "plexil-v.maude"
        semantics.touch()

        config = Config(
            plan_path=plan,
            script_path=script,
            semantics_path=semantics,
            plx2maude_path="plx2maude",
            psx2maude_path="psx2maude",
            maude_path="maude",
            maude_opts=[],
            maude_timeout=300,
            conversion_timeout=30,
            work_dir=None,
            log_file=None,
            debug=False,
            cleanup=False,
            force_cleanup=False,
            relative_paths=False,
        )

        workspace = Workspace(tmp_path / "ws")
        workspace.create()

        runner = MaudeRunner(workspace, config)
        runner.execute()

        # Verify subprocess.run was called with stdin=subprocess.DEVNULL
        call_kwargs = mock_run.call_args[1]
        assert 'stdin' in call_kwargs
        assert call_kwargs['stdin'] == subprocess.DEVNULL

    @patch('subprocess.run')
    def test_conversion_subprocess_has_stdin_devnull(self, mock_run, tmp_path):
        """
        Regression: Conversion tools should also have stdin=DEVNULL for consistency.

        Fix: Added stdin=subprocess.DEVNULL to conversion tool subprocess calls.
        """
        from plexilv_run.conversion import ConversionRunner

        mock_run.return_value = Mock(
            returncode=0,
            stdout="output",
            stderr="",
        )

        # Create minimal config and workspace
        plan = tmp_path / "test.plx"
        plan.touch()
        script = tmp_path / "test.psx"
        script.touch()
        semantics = tmp_path / "plexil-v.maude"
        semantics.touch()

        config = Config(
            plan_path=plan,
            script_path=script,
            semantics_path=semantics,
            plx2maude_path="plx2maude",
            psx2maude_path="psx2maude",
            maude_path="maude",
            maude_opts=[],
            maude_timeout=300,
            conversion_timeout=30,
            work_dir=None,
            log_file=None,
            debug=False,
            cleanup=False,
            force_cleanup=False,
            relative_paths=False,
        )

        workspace = Workspace(tmp_path / "ws")
        workspace.create()

        runner = ConversionRunner(workspace, config)
        runner.run_plx2maude()

        # Verify subprocess.run was called with stdin=subprocess.DEVNULL
        call_kwargs = mock_run.call_args[1]
        assert 'stdin' in call_kwargs
        assert call_kwargs['stdin'] == subprocess.DEVNULL


@pytest.mark.unit
class TestMaudePathRegression:
    """Regression tests for Maude file path handling."""

    @patch('subprocess.run')
    def test_maude_uses_absolute_path_to_run_file(self, mock_run, tmp_path):
        """
        Regression: Maude couldn't find run.maude even though it existed.

        Bug: We were passing "run.maude" as a relative path, but Maude couldn't
        find it even with cwd set to workspace.

        Fix: Use absolute path to run.maude file in the command.
        """
        from plexilv_run.maude import MaudeRunner

        mock_run.return_value = Mock(
            returncode=0,
            stdout="output",
            stderr="stderr",
        )

        # Create minimal setup
        plan = tmp_path / "test.plx"
        plan.touch()
        script = tmp_path / "test.psx"
        script.touch()
        semantics = tmp_path / "plexil-v.maude"
        semantics.touch()

        config = Config(
            plan_path=plan,
            script_path=script,
            semantics_path=semantics,
            plx2maude_path="plx2maude",
            psx2maude_path="psx2maude",
            maude_path="maude",
            maude_opts=[],
            maude_timeout=300,
            conversion_timeout=30,
            work_dir=None,
            log_file=None,
            debug=False,
            cleanup=False,
            force_cleanup=False,
            relative_paths=False,
        )

        workspace = Workspace(tmp_path / "ws")
        workspace.create()

        runner = MaudeRunner(workspace, config)
        runner.execute()

        # Verify the command uses absolute path to run.maude
        cmd = mock_run.call_args[0][0]
        last_arg = cmd[-1]

        # Should be absolute path
        assert Path(last_arg).is_absolute()
        assert last_arg.endswith("run.maude")


@pytest.mark.unit
class TestMaudeOutputRegression:
    """Regression tests for Maude output handling."""

    @patch('subprocess.run')
    def test_maude_only_prints_stderr_by_default(self, mock_run, tmp_path, capsys):
        """
        Regression: Maude was printing both stdout and stderr.

        Bug: The code was doing:
            print(result.stdout, end="")
            print(result.stderr, end="")

        Fix: Only print stderr by default (stdout is internal Maude messages).
        """
        from plexilv_run.maude import MaudeRunner

        mock_run.return_value = Mock(
            returncode=0,
            stdout="Maude internal messages",
            stderr="MACRO\nmicro\n",
        )

        # Create minimal setup
        plan = tmp_path / "test.plx"
        plan.touch()
        script = tmp_path / "test.psx"
        script.touch()
        semantics = tmp_path / "plexil-v.maude"
        semantics.touch()

        config = Config(
            plan_path=plan,
            script_path=script,
            semantics_path=semantics,
            plx2maude_path="plx2maude",
            psx2maude_path="psx2maude",
            maude_path="maude",
            maude_opts=[],
            maude_timeout=300,
            conversion_timeout=30,
            work_dir=None,
            log_file=None,
            debug=False,
            cleanup=False,
            force_cleanup=False,
            relative_paths=False,
        )

        workspace = Workspace(tmp_path / "ws")
        workspace.create()

        runner = MaudeRunner(workspace, config)
        runner.execute()

        captured = capsys.readouterr()

        # Should print stderr
        assert "MACRO" in captured.out
        assert "micro" in captured.out

        # Should NOT print stdout
        assert "Maude internal messages" not in captured.out

    @patch('subprocess.run')
    def test_maude_writes_only_stderr_to_log_file(self, mock_run, tmp_path):
        """
        Regression: Log file should contain only stderr, not stdout.

        Bug: Requirement was to capture stderr only by default.
        """
        from plexilv_run.maude import MaudeRunner

        mock_run.return_value = Mock(
            returncode=0,
            stdout="Maude internal messages",
            stderr="MACRO\nmicro\n",
        )

        # Create minimal setup
        plan = tmp_path / "test.plx"
        plan.touch()
        script = tmp_path / "test.psx"
        script.touch()
        semantics = tmp_path / "plexil-v.maude"
        semantics.touch()
        log_file = tmp_path / "output.log"

        config = Config(
            plan_path=plan,
            script_path=script,
            semantics_path=semantics,
            plx2maude_path="plx2maude",
            psx2maude_path="psx2maude",
            maude_path="maude",
            maude_opts=[],
            maude_timeout=300,
            conversion_timeout=30,
            work_dir=None,
            log_file=log_file,
            debug=False,
            cleanup=False,
            force_cleanup=False,
            relative_paths=False,
        )

        workspace = Workspace(tmp_path / "ws")
        workspace.create()

        runner = MaudeRunner(workspace, config)
        runner.execute()

        # Log file should contain only stderr
        log_content = log_file.read_text()
        assert "MACRO" in log_content
        assert "micro" in log_content
        assert "Maude internal messages" not in log_content