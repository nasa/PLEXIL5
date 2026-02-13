"""Custom exceptions for plexilv-diff application."""


class DiffPlexilvError(Exception):
    """Base exception for all plexilv-diff errors."""
    pass


class WorkspaceError(DiffPlexilvError):
    """Raised when workspace operations fail."""
    pass


class CompilationError(DiffPlexilvError):
    """Raised when PLEXIL compilation fails."""

    def __init__(self, message: str, file_path: str = None, stderr: str = None):
        """Initialize compilation error.

        Args:
            message: Human-readable error message
            file_path: Path to the file that failed to compile
            stderr: Standard error output from the compiler
        """
        super().__init__(message)
        self.file_path = file_path
        self.stderr = stderr


class ExecutionError(DiffPlexilvError):
    """Raised when interpreter execution fails."""

    def __init__(self, message: str, interpreter: str = None, stderr: str = None):
        """Initialize execution error.

        Args:
            message: Human-readable error message
            interpreter: Name of the interpreter that failed (plexiltest/plexilv)
            stderr: Standard error output from the interpreter
        """
        super().__init__(message)
        self.interpreter = interpreter
        self.stderr = stderr


class DiffError(DiffPlexilvError):
    """Raised when plexilog-diff execution fails (not when logs differ)."""

    def __init__(self, message: str, stderr: str = None):
        """Initialize diff error.

        Args:
            message: Human-readable error message
            stderr: Standard error output from plexilog-diff
        """
        super().__init__(message)
        self.stderr = stderr


class TimeoutError(DiffPlexilvError):
    """Raised when an operation times out."""

    def __init__(self, message: str, timeout_seconds: int = None):
        """Initialize timeout error.

        Args:
            message: Human-readable error message
            timeout_seconds: The timeout duration that was exceeded
        """
        super().__init__(message)
        self.timeout_seconds = timeout_seconds


class ValidationError(DiffPlexilvError):
    """Raised when input validation fails (e.g., tool not found when --failproof is used)."""
    pass


class ConfigurationError(DiffPlexilvError):
    """Raised when configuration is invalid."""
    pass