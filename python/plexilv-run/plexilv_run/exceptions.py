"""Custom exception types for plexilv-run."""


class PlexilvRunError(Exception):
    """Base exception for all plexilv-run errors."""

    pass


class ConfigurationError(PlexilvRunError):
    """Error in configuration (env vars, paths, options)."""

    pass


class ValidationError(PlexilvRunError):
    """Error validating input files."""

    pass


class WorkspaceError(PlexilvRunError):
    """Error creating or managing workspace."""

    pass


class ToolNotFoundError(PlexilvRunError):
    """Required tool not found or not executable."""

    def __init__(self, tool_name: str, message: str):
        self.tool_name = tool_name
        super().__init__(message)


class ConversionError(PlexilvRunError):
    """Error during plx2maude or psx2maude conversion."""

    def __init__(self, tool_name: str, exit_code: int, stderr: str):
        self.tool_name = tool_name
        self.exit_code = exit_code
        self.stderr = stderr
        super().__init__(f"{tool_name} failed with exit code {exit_code}")


class ConversionTimeoutError(PlexilvRunError):
    """Conversion tool timed out."""

    def __init__(self, tool_name: str, timeout: int):
        self.tool_name = tool_name
        self.timeout = timeout
        super().__init__(f"{tool_name} timed out after {timeout} seconds")


class MaudeError(PlexilvRunError):
    """Error during Maude execution."""

    def __init__(self, exit_code: int, stderr: str):
        self.exit_code = exit_code
        self.stderr = stderr
        super().__init__(f"Maude execution failed with exit code {exit_code}")


class MaudeTimeoutError(PlexilvRunError):
    """Maude execution timed out."""

    def __init__(self, timeout: int):
        self.timeout = timeout
        super().__init__(f"Maude execution timed out after {timeout} seconds")