"""Fixtures for end-to-end tests."""

import pytest
from pathlib import Path
import shutil


@pytest.fixture
def stubs_dir():
    """Path to stub executables directory."""
    return Path(__file__).parent.parent / "stubs"


@pytest.fixture
def stub_tools(stubs_dir, tmp_path):
    """Provide paths to stub executables.

    Copies stubs to a temporary directory so we can modify them if needed.
    """
    temp_stubs = tmp_path / "stubs"
    temp_stubs.mkdir()

    # Copy all stub files
    for stub in stubs_dir.glob("*"):
        if stub.is_file():
            dest = temp_stubs / stub.name
            shutil.copy2(stub, dest)
            dest.chmod(0o755)  # Make executable

    return {
        'plexilc': str(temp_stubs / 'plexilc'),
        'plexiltest': str(temp_stubs / 'plexiltest'),
        'plexilv-run': str(temp_stubs / 'plexilv-run'),
        'plexilv_run_different': str(temp_stubs / 'plexilv-run-different'),
        'plexilog_diff': str(temp_stubs / 'plexilog-diff'),
    }


@pytest.fixture
def sample_plan(tmp_path):
    """Create a sample PLEXIL plan file."""
    plan = tmp_path / "test_plan.ple"
    plan.write_text("""
// Sample PLEXIL Plan
Node: Root
{
    NodeBody: TestNode
}
""")
    return plan


@pytest.fixture
def sample_scripts(tmp_path):
    """Create sample PLEXIL script files."""
    scripts = []

    for i in range(3):
        script = tmp_path / f"test{i+1}.pst"
        script.write_text(f"""
// Sample PLEXIL Script {i+1}
Script: Test{i+1}
{{
    // Test commands
}}
""")
        scripts.append(script)

    return scripts