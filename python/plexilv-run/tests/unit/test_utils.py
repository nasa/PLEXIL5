"""Unit tests for utility functions."""

import pytest
from plexilv_run.utils import get_last_n_lines, format_command


@pytest.mark.unit
class TestGetLastNLines:
    """Test get_last_n_lines function."""

    def test_empty_string(self):
        """Empty string should return empty string."""
        assert get_last_n_lines("") == ""

    def test_single_line(self):
        """Single line should be returned as-is."""
        assert get_last_n_lines("single line") == "single line"

    def test_fewer_lines_than_requested(self):
        """If text has fewer lines than N, return all lines."""
        text = "line1\nline2\nline3"
        assert get_last_n_lines(text, 10) == text

    def test_exact_n_lines(self):
        """If text has exactly N lines, return all lines."""
        text = "line1\nline2\nline3"
        assert get_last_n_lines(text, 3) == text

    def test_more_lines_than_requested(self):
        """If text has more lines than N, return last N lines."""
        text = "line1\nline2\nline3\nline4\nline5"
        result = get_last_n_lines(text, 2)
        assert result == "line4\nline5"

    def test_default_n_value(self):
        """Default N value should be 10."""
        lines = [f"line{i}" for i in range(1, 16)]  # 15 lines
        text = "\n".join(lines)
        result = get_last_n_lines(text)
        expected = "\n".join(lines[-10:])  # Last 10 lines
        assert result == expected

    def test_n_equals_zero(self):
        """N=0 should return empty string."""
        text = "line1\nline2\nline3"
        assert get_last_n_lines(text, 0) == ""

    def test_n_equals_one(self):
        """N=1 should return only the last line."""
        text = "line1\nline2\nline3"
        assert get_last_n_lines(text, 1) == "line3"

    def test_preserves_line_content(self):
        """Should preserve exact line content including special characters."""
        text = "line with spaces\n\tline with tab\nline-with-dashes"
        result = get_last_n_lines(text, 2)
        assert result == "\tline with tab\nline-with-dashes"

    def test_multiline_with_empty_lines(self):
        """Should handle empty lines correctly."""
        text = "line1\n\nline3\n\nline5"
        result = get_last_n_lines(text, 3)
        assert result == "line3\n\nline5"

    def test_text_ending_with_newline(self):
        """Should handle text ending with newline."""
        text = "line1\nline2\nline3\n"
        result = get_last_n_lines(text, 2)
        # splitlines() doesn't include the final empty string
        assert result == "line2\nline3"

    def test_large_text(self):
        """Should handle large text efficiently."""
        lines = [f"line{i}" for i in range(1, 1001)]  # 1000 lines
        text = "\n".join(lines)
        result = get_last_n_lines(text, 10)
        expected = "\n".join(lines[-10:])
        assert result == expected


@pytest.mark.unit
class TestFormatCommand:
    """Test format_command function."""

    def test_simple_command(self):
        """Simple command with single argument."""
        cmd = ["maude", "run.maude"]
        assert format_command(cmd) == "maude run.maude"

    def test_command_with_flags(self):
        """Command with multiple flags."""
        cmd = ["maude", "-no-banner", "-no-wrap", "run.maude"]
        assert format_command(cmd) == "maude -no-banner -no-wrap run.maude"

    def test_command_with_path(self):
        """Command with file path."""
        cmd = ["plx2maude", "/path/to/plan.plx"]
        assert format_command(cmd) == "plx2maude /path/to/plan.plx"

    def test_single_element(self):
        """Single element command."""
        cmd = ["maude"]
        assert format_command(cmd) == "maude"

    def test_empty_list(self):
        """Empty list should return empty string."""
        cmd = []
        assert format_command(cmd) == ""

    def test_command_with_spaces_in_args(self):
        """Command with arguments containing spaces (not quoted)."""
        # Note: This function doesn't handle shell quoting
        cmd = ["echo", "hello world"]
        assert format_command(cmd) == "echo hello world"

    def test_preserves_order(self):
        """Should preserve exact order of command parts."""
        cmd = ["tool", "--flag1", "arg1", "--flag2", "arg2"]
        assert format_command(cmd) == "tool --flag1 arg1 --flag2 arg2"

    def test_with_equals_sign_flags(self):
        """Command with flags using equals sign."""
        cmd = ["tool", "--option=value", "file.txt"]
        assert format_command(cmd) == "tool --option=value file.txt"