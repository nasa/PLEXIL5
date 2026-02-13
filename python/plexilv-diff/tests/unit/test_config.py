"""Tests for configuration management."""

import pytest
from pathlib import Path
from plexilv_diff.config import Config
from plexilv_diff.exceptions import ConfigurationError, ValidationError


@pytest.fixture
def sample_plan(tmp_path):
    """Create a sample plan file."""
    plan = tmp_path / "plan.ple"
    plan.write_text("// Sample plan")
    return plan


@pytest.fixture
def sample_scripts(tmp_path):
    """Create sample script files."""
    scripts = []
    for i in range(3):
        script = tmp_path / f"test{i+1}.pst"
        script.write_text(f"// Sample script {i+1}")
        scripts.append(script)
    return scripts


def test_config_basic(sample_plan, sample_scripts):
    """Test creating a basic configuration."""
    config = Config(plan=sample_plan, scripts=sample_scripts)

    assert config.plan == sample_plan
    assert config.scripts == sample_scripts
    assert config.plexilc == "plexilc"
    assert config.plexiltest == "plexiltest"
    assert config.plexilv_run == "plexilv-run"
    assert config.plexilog_diff == "plexilog-diff"


def test_config_default_values(sample_plan, sample_scripts):
    """Test that default values are set correctly."""
    config = Config(plan=sample_plan, scripts=sample_scripts)

    # Tool flags
    assert config.plexilc_flags == ""
    assert config.plexiltest_flags == ""
    assert config.plexilv_run_flags == ""
    assert config.plexilog_diff_flags == ""

    # Timeouts
    assert config.compilation_timeout == 30
    assert config.execution_timeout == 300

    # Cleanup options
    assert config.cleanup is False
    assert config.force_cleanup is False

    # Behavior options
    assert config.fail_fast is False
    assert config.failproof is False
    assert config.quiet is False

    # Optional paths
    assert config.debug_config is None
    assert config.work_dir is None


def test_config_custom_values(sample_plan, sample_scripts):
    """Test creating configuration with custom values."""
    config = Config(
        plan=sample_plan,
        scripts=sample_scripts,
        plexilc="/custom/path/plexilc",
        compilation_timeout=60,
        execution_timeout=600,
        cleanup=True,
        fail_fast=True,
        failproof=True,
        quiet=True,
    )

    assert config.plexilc == "/custom/path/plexilc"
    assert config.compilation_timeout == 60
    assert config.execution_timeout == 600
    assert config.cleanup is True
    assert config.fail_fast is True
    assert config.failproof is True
    assert config.quiet is True


def test_config_converts_strings_to_paths(tmp_path):
    """Test that string paths are converted to Path objects."""
    plan = tmp_path / "plan.ple"
    plan.write_text("// Plan")
    script = tmp_path / "test.pst"
    script.write_text("// Script")

    config = Config(
        plan=str(plan),
        scripts=[str(script)]
    )

    assert isinstance(config.plan, Path)
    assert isinstance(config.scripts[0], Path)


def test_config_plan_not_found():
    """Test error when plan file doesn't exist."""
    with pytest.raises(ConfigurationError, match="Plan file does not exist"):
        Config(
            plan=Path("/nonexistent/plan.ple"),
            scripts=[Path("/tmp/test.pst")]
        )


def test_config_script_not_found(sample_plan):
    """Test error when script file doesn't exist."""
    with pytest.raises(ConfigurationError, match="Script file does not exist"):
        Config(
            plan=sample_plan,
            scripts=[Path("/nonexistent/test.pst")]
        )


def test_config_invalid_plan_extension(tmp_path):
    """Test error when plan has invalid extension."""
    plan = tmp_path / "plan.txt"
    plan.write_text("// Invalid")

    with pytest.raises(ConfigurationError, match="must have .ple or .plx extension"):
        Config(plan=plan, scripts=[])


def test_config_invalid_script_extension(sample_plan, tmp_path):
    """Test error when script has invalid extension."""
    script = tmp_path / "test.txt"
    script.write_text("// Invalid")

    with pytest.raises(ConfigurationError, match="must have .pst or .psx extension"):
        Config(plan=sample_plan, scripts=[script])


def test_config_no_scripts(sample_plan):
    """Test error when no scripts provided."""
    with pytest.raises(ConfigurationError, match="At least one script file is required"):
        Config(plan=sample_plan, scripts=[])


def test_config_negative_compilation_timeout(sample_plan, sample_scripts):
    """Test error with negative compilation timeout."""
    with pytest.raises(ConfigurationError, match="Compilation timeout must be positive"):
        Config(
            plan=sample_plan,
            scripts=sample_scripts,
            compilation_timeout=-1
        )


def test_config_zero_execution_timeout(sample_plan, sample_scripts):
    """Test error with zero execution timeout."""
    with pytest.raises(ConfigurationError, match="Execution timeout must be positive"):
        Config(
            plan=sample_plan,
            scripts=sample_scripts,
            execution_timeout=0
        )


def test_config_debug_config_not_found(sample_plan, sample_scripts):
    """Test error when debug config file doesn't exist."""
    with pytest.raises(ConfigurationError, match="Debug config file does not exist"):
        Config(
            plan=sample_plan,
            scripts=sample_scripts,
            debug_config=Path("/nonexistent/debug.cfg")
        )


def test_config_work_dir_exists(sample_plan, sample_scripts, tmp_path):
    """Test error when work_dir already exists."""
    existing_dir = tmp_path / "existing"
    existing_dir.mkdir()

    with pytest.raises(ConfigurationError, match="Working directory already exists"):
        Config(
            plan=sample_plan,
            scripts=sample_scripts,
            work_dir=existing_dir
        )


def test_config_needs_plan_compilation(tmp_path):
    """Test needs_plan_compilation method."""
    ple_plan = tmp_path / "plan.ple"
    ple_plan.write_text("// Plan")
    plx_plan = tmp_path / "plan.plx"
    plx_plan.write_text("<?xml version='1.0'?>")
    script = tmp_path / "test.pst"
    script.write_text("// Script")

    config_ple = Config(plan=ple_plan, scripts=[script])
    config_plx = Config(plan=plx_plan, scripts=[script])

    assert config_ple.needs_plan_compilation() is True
    assert config_plx.needs_plan_compilation() is False


def test_config_needs_script_compilation(tmp_path):
    """Test needs_script_compilation method."""
    plan = tmp_path / "plan.ple"
    plan.write_text("// Plan")
    pst_script = tmp_path / "test.pst"
    pst_script.write_text("// Script")
    psx_script = tmp_path / "test.psx"
    psx_script.write_text("<?xml version='1.0'?>")

    config = Config(plan=plan, scripts=[pst_script, psx_script])

    assert config.needs_script_compilation(pst_script) is True
    assert config.needs_script_compilation(psx_script) is False


def test_config_get_plan_name(sample_plan, sample_scripts):
    """Test get_plan_name method."""
    config = Config(plan=sample_plan, scripts=sample_scripts)
    assert config.get_plan_name() == "plan"


def test_config_parse_tool_flags_empty(sample_plan, sample_scripts):
    """Test parsing empty tool flags."""
    config = Config(plan=sample_plan, scripts=sample_scripts)

    assert config.parse_tool_flags("") == []
    assert config.parse_tool_flags("   ") == []


def test_config_parse_tool_flags_single(sample_plan, sample_scripts):
    """Test parsing single flag."""
    config = Config(plan=sample_plan, scripts=sample_scripts)

    assert config.parse_tool_flags("--quiet") == ["--quiet"]


def test_config_parse_tool_flags_multiple(sample_plan, sample_scripts):
    """Test parsing multiple flags."""
    config = Config(plan=sample_plan, scripts=sample_scripts)

    flags = config.parse_tool_flags("--flag1 --flag2 value --flag3")
    assert flags == ["--flag1", "--flag2", "value", "--flag3"]


def test_config_validate_tools_not_found(sample_plan, sample_scripts):
    """Test validate_tools when tool is not found."""
    config = Config(
        plan=sample_plan,
        scripts=sample_scripts,
        plexilc="/nonexistent/plexilc"
    )

    with pytest.raises(ValidationError, match="Tool 'plexilc' not found"):
        config.validate_tools()


def test_config_validate_tools_success(sample_plan, sample_scripts):
    """Test validate_tools when all tools are found."""
    # Use common shell commands that should exist
    config = Config(
        plan=sample_plan,
        scripts=sample_scripts,
        plexilc="echo",
        plexiltest="echo",
        plexilv_run="echo",
        plexilog_diff="echo"
    )

    # Should not raise
    config.validate_tools()


def test_config_single_script_as_path(sample_plan, tmp_path):
    """Test providing a single script as Path (not list)."""
    script = tmp_path / "test.pst"
    script.write_text("// Script")

    config = Config(plan=sample_plan, scripts=script)

    assert isinstance(config.scripts, list)
    assert len(config.scripts) == 1
    assert config.scripts[0] == script


def test_config_plx_extension_valid(tmp_path):
    """Test that .plx extension is accepted for plan."""
    plan = tmp_path / "plan.plx"
    plan.write_text("<?xml version='1.0'?>")
    script = tmp_path / "test.pst"
    script.write_text("// Script")

    config = Config(plan=plan, scripts=[script])
    assert config.plan == plan


def test_config_psx_extension_valid(sample_plan, tmp_path):
    """Test that .psx extension is accepted for script."""
    script = tmp_path / "test.psx"
    script.write_text("<?xml version='1.0'?>")

    config = Config(plan=sample_plan, scripts=[script])
    assert config.scripts[0] == script