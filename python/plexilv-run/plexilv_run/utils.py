"""Utility functions for plexilv."""


def get_last_n_lines(text: str, n: int = 10) -> str:
    """
    Get the last N lines of text.

    Args:
        text: The text to process
        n: Number of lines to return (default: 10)

    Returns:
        The last N lines of text, joined with newlines.
        Returns empty string if text is empty or n is 0.
        Returns all lines if text has fewer than N lines.

    Examples:
        >>> get_last_n_lines("line1\\nline2\\nline3", 2)
        'line2\\nline3'
        >>> get_last_n_lines("", 10)
        ''
        >>> get_last_n_lines("single line", 5)
        'single line'
    """
    if not text or n == 0:
        return ""

    lines = text.splitlines()
    if len(lines) <= n:
        return text

    return "\n".join(lines[-n:])


def format_command(cmd: list[str]) -> str:
    """
    Format a command list for display.

    Args:
        cmd: List of command parts (e.g., ['maude', '-no-banner', 'run.maude'])

    Returns:
        Space-separated command string

    Examples:
        >>> format_command(['maude', '-no-banner', 'run.maude'])
        'maude -no-banner run.maude'
        >>> format_command(['plx2maude', '/path/to/plan.plx'])
        'plx2maude /path/to/plan.plx'
    """
    return " ".join(cmd)