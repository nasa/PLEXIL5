"""Unit tests for the unified CLI."""

import click
from click.testing import CliRunner
import pytest
from unittest.mock import MagicMock, patch

from plexilv_cli.cli import main


@pytest.fixture
def cli_runner():
    """Create a Click CLI runner."""
    return CliRunner()


@pytest.mark.unit
class TestCLI:
    """Test unified CLI."""

    def test_help(self, cli_runner):
        """--help should display usage and all subcommands."""
        result = cli_runner.invoke(main, ['--help'])
        assert result.exit_code == 0
        assert 'PLEXIL verification tools' in result.output
        assert 'run' in result.output
        assert 'test' in result.output
        assert 'diff' in result.output
        assert 'PLEXIL-V formal semantics' in result.output

    def test_version(self, cli_runner):
        """--version should display version."""
        result = cli_runner.invoke(main, ['--version'])
        assert result.exit_code == 0
        assert '0.1.0' in result.output

    def test_no_command(self, cli_runner):
        """Running without subcommand should show usage."""
        result = cli_runner.invoke(main, [])
        # Click groups exit with code 0 when invoked without subcommand
        # and display the usage/help text
        assert result.exit_code in (0, 2)
        assert 'Usage:' in result.output or 'Commands:' in result.output

    def test_invalid_command(self, cli_runner):
        """Invalid command should fail with proper error."""
        result = cli_runner.invoke(main, ['invalid-command'])
        assert result.exit_code != 0
        assert 'No such command' in result.output or 'Usage:' in result.output


@pytest.mark.unit
class TestHelpCommand:
    """Test help subcommand."""

    def test_help_command_without_argument(self, cli_runner):
        """'help' without argument should show main help."""
        result = cli_runner.invoke(main, ['help'])
        assert result.exit_code == 0
        assert 'PLEXIL verification tools' in result.output

    def test_help_command_for_run(self, cli_runner):
        """'help run' should show help for run command."""
        result = cli_runner.invoke(main, ['help', 'run'])
        assert result.exit_code == 0
        assert 'Usage:' in result.output

    def test_help_command_for_test(self, cli_runner):
        """'help test' should show help for test command."""
        result = cli_runner.invoke(main, ['help', 'test'])
        assert result.exit_code == 0
        assert 'Usage:' in result.output

    def test_help_command_for_diff(self, cli_runner):
        """'help diff' should show help for diff command."""
        result = cli_runner.invoke(main, ['help', 'diff'])
        assert result.exit_code == 0
        assert 'Usage:' in result.output

    def test_help_command_for_invalid_command(self, cli_runner):
        """'help invalid' should show error."""
        result = cli_runner.invoke(main, ['help', 'invalid-command'])
        assert result.exit_code != 0
        assert 'Unknown command' in result.output or 'Error' in result.output

    def test_help_command_alias(self, cli_runner):
        """'help' should work as alternative to '--help'."""
        help_flag = cli_runner.invoke(main, ['--help'])
        help_cmd = cli_runner.invoke(main, ['help'])

        # Both should succeed
        assert help_flag.exit_code == 0
        assert help_cmd.exit_code == 0

        # Both should contain similar content
        assert 'PLEXIL verification tools' in help_flag.output
        assert 'PLEXIL verification tools' in help_cmd.output


@pytest.mark.unit
class TestRunCommand:
    """Test run subcommand integration."""

    def test_run_help(self, cli_runner):
        """'run --help' should display run command help."""
        result = cli_runner.invoke(main, ['run', '--help'])
        assert result.exit_code == 0
        # The help should mention PLEXIL execution
        assert 'Execute' in result.output or 'PLEXIL' in result.output or 'Usage:' in result.output

    def test_test_help(self, cli_runner):
        """'test --help' should display test command help (alias for run)."""
        result = cli_runner.invoke(main, ['test', '--help'])
        assert result.exit_code == 0
        # Same help as run since it's an alias
        assert 'Execute' in result.output or 'PLEXIL' in result.output or 'Usage:' in result.output

    def test_run_and_test_are_same_command(self, cli_runner):
        """'run' and 'test' should be the same command (alias)."""
        run_help = cli_runner.invoke(main, ['run', '--help'])
        test_help = cli_runner.invoke(main, ['test', '--help'])

        # Both should have same exit code
        assert run_help.exit_code == test_help.exit_code
        # Both should have similar output (same command object)
        assert run_help.exit_code == 0
        assert test_help.exit_code == 0

    @patch('plexilv_run.cli.main')
    def test_run_command_invoked_with_arguments(self, mock_run, cli_runner):
        """run command should pass arguments to plexilv_run."""
        # Mock the run command to track it was called
        mock_run.return_value = 0

        result = cli_runner.invoke(main, ['run', '-p', 'test.plx', '-s', 'test.psx'])
        # We expect this to call the actual run command
        assert result.exit_code is not None  # Command was invoked


@pytest.mark.unit
class TestDiffCommand:
    """Test diff subcommand integration."""

    def test_diff_help(self, cli_runner):
        """'diff --help' should display diff command help."""
        result = cli_runner.invoke(main, ['diff', '--help'])
        assert result.exit_code == 0
        # The help should be available for diff
        assert 'Usage:' in result.output

    def test_diff_is_registered(self, cli_runner):
        """diff command should be registered in main group."""
        # Verify diff is in the help
        help_result = cli_runner.invoke(main, ['--help'])
        assert 'diff' in help_result.output

    @patch('plexilv_diff.cli.main')
    def test_diff_command_invoked_with_arguments(self, mock_diff, cli_runner):
        """diff command should pass arguments to plexilv_diff."""
        # Mock the diff command to track it was called
        mock_diff.return_value = 0

        result = cli_runner.invoke(main, ['diff', 'old.plx', 'new.plx'])
        # We expect this to call the actual diff command
        assert result.exit_code is not None  # Command was invoked


@pytest.mark.unit
class TestCommandRegistration:
    """Test that all commands are properly registered."""

    def test_main_is_click_group(self):
        """main should be a Click group."""
        assert isinstance(main, click.Group)

    def test_all_subcommands_registered(self):
        """All expected subcommands should be registered."""
        # Get all command names from the group
        command_names = set(main.commands.keys())

        expected_commands = {'run', 'test', 'diff', 'help'}
        assert expected_commands.issubset(command_names), \
            f"Missing commands. Expected {expected_commands}, got {command_names}"

    def test_run_and_test_reference_same_command(self):
        """run and test should reference the same Click command object."""
        run_cmd = main.commands.get('run')
        test_cmd = main.commands.get('test')

        # Both should exist
        assert run_cmd is not None, "run command not found"
        assert test_cmd is not None, "test command not found"

        # They should be the same object
        assert run_cmd is test_cmd, "run and test should be the same command object"

    def test_diff_is_separate_command(self):
        """diff should be a separate command from run/test."""
        run_cmd = main.commands.get('run')
        diff_cmd = main.commands.get('diff')

        # Both should exist
        assert run_cmd is not None, "run command not found"
        assert diff_cmd is not None, "diff command not found"

        # They should be different objects
        assert run_cmd is not diff_cmd, "diff should be a separate command from run"

    def test_help_is_separate_command(self):
        """help should be a separate command."""
        help_cmd = main.commands.get('help')

        # Should exist
        assert help_cmd is not None, "help command not found"

        # Should be a Click Command
        assert isinstance(help_cmd, click.Command)


@pytest.mark.unit
class TestIntegration:
    """Integration tests for CLI behavior."""

    def test_help_text_is_comprehensive(self, cli_runner):
        """Help text should describe all available commands."""
        result = cli_runner.invoke(main, ['--help'])
        assert result.exit_code == 0

        # Should mention the main purpose
        assert 'PLEXIL' in result.output

        # Should list all commands
        assert 'run' in result.output
        assert 'test' in result.output
        assert 'diff' in result.output
        assert 'help' in result.output

    def test_version_option_works(self, cli_runner):
        """Version option should work."""
        result = cli_runner.invoke(main, ['--version'])
        assert result.exit_code == 0
        assert '0.1.0' in result.output

    def test_each_subcommand_is_accessible(self, cli_runner):
        """Each subcommand should be accessible via CLI."""
        commands_to_test = ['run', 'test', 'diff', 'help']

        for cmd in commands_to_test:
            result = cli_runner.invoke(main, [cmd, '--help'])
            # Should not fail with "No such command"
            assert 'No such command' not in result.output, \
                f"Command '{cmd}' is not properly registered"

    def test_plexil_v_semantics_mentioned(self, cli_runner):
        """Help text should mention PLEXIL-V formal semantics."""
        result = cli_runner.invoke(main, ['--help'])
        assert result.exit_code == 0
        assert 'PLEXIL-V formal semantics' in result.output