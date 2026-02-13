"""Input validation functions for plexilv."""

import os
import re
from pathlib import Path

from plexilv_run.exceptions import ValidationError


def validate_plexil_identifier(name: str) -> None:
    """
    Validate that a name is a valid PLEXIL identifier.

    A valid PLEXIL identifier must:
    - Start with a letter (a-z, A-Z)
    - Contain only letters, numbers, and underscores

    Args:
        name: The identifier to validate

    Raises:
        ValidationError: If the name is not a valid PLEXIL identifier

    Examples:
        >>> validate_plexil_identifier("MyPlan")  # Valid
        >>> validate_plexil_identifier("test_plan_01")  # Valid
        >>> validate_plexil_identifier("123Plan")  # Raises ValidationError
    """
    if not name:
        raise ValidationError(
            "Filename cannot be empty.\n"
            "Plan filename must be a valid PLEXIL identifier:\n"
            "  - Start with a letter (a-z, A-Z)\n"
            "  - Contain only letters, numbers, and underscores\n"
            "  - Example valid names: MyPlan.plx, test_plan_01.plx, Plan2.plx"
        )

    if not re.match(r"^[a-zA-Z][a-zA-Z0-9_]*$", name):
        raise ValidationError(
            f"Invalid plan filename '{name}'\n"
            "Plan filename must be a valid PLEXIL identifier:\n"
            "  - Start with a letter (a-z, A-Z)\n"
            "  - Contain only letters, numbers, and underscores\n"
            "  - Example valid names: MyPlan.plx, test_plan_01.plx, Plan2.plx"
        )


def validate_file_extension(path: Path, expected_ext: str, file_type: str) -> None:
    """
    Validate that a file has the expected extension.

    Args:
        path: Path to the file
        expected_ext: Expected extension (e.g., '.plx')
        file_type: Type description for error message (e.g., 'Plan')

    Raises:
        ValidationError: If the file doesn't have the expected extension
    """
    if path.suffix != expected_ext:
        raise ValidationError(
            f"Error: {file_type} file must have {expected_ext} extension (got: {path.name})"
        )


def validate_file_readable(path: Path, file_type: str) -> None:
    """
    Validate that a file exists and is readable.

    Args:
        path: Path to the file
        file_type: Type description for error message (e.g., 'Plan')

    Raises:
        ValidationError: If the file is not readable
    """
    if not os.access(path, os.R_OK):
        raise ValidationError(
            f"Error: Cannot read {file_type.lower()} file (permission denied): {path}"
        )


def validate_plan_file(path: Path) -> None:
    """
    Validate a PLEXIL plan file.

    Checks:
    - File has .plx extension
    - File is readable
    - Filename (without extension) is a valid PLEXIL identifier

    Args:
        path: Path to the plan file

    Raises:
        ValidationError: If validation fails
    """
    # Check extension
    validate_file_extension(path, ".plx", "Plan")

    # Check readability
    validate_file_readable(path, "Plan")

    # Check filename is valid PLEXIL identifier
    stem = path.stem
    validate_plexil_identifier(stem)


def validate_script_file(path: Path) -> None:
    """
    Validate a PLEXIL script file.

    Checks:
    - File has .psx extension
    - File is readable

    Args:
        path: Path to the script file

    Raises:
        ValidationError: If validation fails
    """
    # Check extension
    validate_file_extension(path, ".psx", "Script")

    # Check readability
    validate_file_readable(path, "Script")