"""Tool availability checking for plexilv."""

import os
import shutil
from pathlib import Path

from plexilv_run.exceptions import ToolNotFoundError


def get_tool_path(name: str, custom_path: str | None = None) -> str:
    """
    Get the path to a tool executable.

    Args:
        name: Tool name to look for in PATH
        custom_path: Custom path to the tool (takes precedence)

    Returns:
        Path to the tool executable

    Raises:
        ToolNotFoundError: If tool is not found
    """
    if custom_path and custom_path != name:
        # Use custom path if provided AND it's not just the tool name
        tool_path = custom_path
    else:
        # Look for tool in PATH
        tool_path = shutil.which(name)
        if not tool_path:
            raise ToolNotFoundError(
                name,
                f"Tool '{name}' not found in PATH"
            )

    return tool_path


def check_tool(name: str, custom_path: str | None = None) -> str:
    """
    Check if a tool is available and executable.

    Args:
        name: Tool name for error messages
        custom_path: Custom path to the tool (optional)

    Returns:
        Path to the tool executable

    Raises:
        ToolNotFoundError: If tool is not found or not executable
    """
    tool_path = get_tool_path(name, custom_path)

    # Convert to Path for easier checking
    path = Path(tool_path)

    # Check if path exists
    if not path.exists():
        raise ToolNotFoundError(
            name,
            f"Tool '{name}' not found: {tool_path}"
        )

    # Check if it's executable
    if not os.access(path, os.X_OK):
        raise ToolNotFoundError(
            name,
            f"Tool '{name}' is not executable: {tool_path}"
        )

    return str(path)


def check_all_tools(
    plx2maude_path: str | None = None,
    psx2maude_path: str | None = None,
    maude_path: str | None = None,
) -> tuple[str, str, str]:
    """
    Check that all required tools are available.

    Args:
        plx2maude_path: Custom path to plx2maude (optional)
        psx2maude_path: Custom path to psx2maude (optional)
        maude_path: Custom path to maude (optional)

    Returns:
        Tuple of (plx2maude_path, psx2maude_path, maude_path)

    Raises:
        ToolNotFoundError: If any tool is missing, with combined error message
    """
    errors = []
    tools = {}

    # Check plx2maude
    try:
        tools['plx2maude'] = check_tool('plx2maude', plx2maude_path)
    except ToolNotFoundError as e:
        errors.append(('plexilv', str(e)))

    # Check psx2maude
    try:
        tools['psx2maude'] = check_tool('psx2maude', psx2maude_path)
    except ToolNotFoundError as e:
        errors.append(('plexilv', str(e)))

    # Check maude
    try:
        tools['maude'] = check_tool('maude', maude_path)
    except ToolNotFoundError as e:
        errors.append(('maude_tool', str(e)))

    # If any errors, raise combined error message
    if errors:
        error_msgs = []

        # Group PLEXIL-V tools
        plexilv_errors = [msg for tool_type, msg in errors if tool_type == 'plexilv']
        if plexilv_errors:
            error_msgs.append(
                "Missing PLEXIL-V tools:\n" +
                "\n".join(f"  - {err}" for err in plexilv_errors) +
                "\n\nPlease install PLEXIL-V tools and ensure they are in your PATH."
            )

        # Group Maude tool
        maude_errors = [msg for tool_type, msg in errors if tool_type == 'maude_tool']
        if maude_errors:
            error_msgs.append(
                "Missing Maude:\n" +
                "\n".join(f"  - {err}" for err in maude_errors) +
                "\n\nPlease install Maude and ensure it is in your PATH."
            )

        raise ToolNotFoundError(
            "multiple",
            "\n\n".join(error_msgs)
        )

    return tools['plx2maude'], tools['psx2maude'], tools['maude']