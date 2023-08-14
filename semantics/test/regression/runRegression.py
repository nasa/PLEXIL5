import subprocess
import os
import xml.etree.ElementTree as ET
import re
import argparse

# Run the regression tests
def runRegression():
    parser = argparse.ArgumentParser()
    parser.add_argument('-c', '--compile', action='store_true', help="compile plans and scripts")
    parser.add_argument('-t', '--test', nargs='?', const='.', help="test to be executed (and compiled if chosen)")
    parser.add_argument('-r', '--redirect', nargs='?', const = '/dev/null', help="path to redirect maude standard output")
    parser.add_argument('-np', '--noparse', action='store_false', help="use this flag if you dont want to parse the tests again")
    parser.add_argument('-p', '--pipeline', action='store_true', help="this flag is intended for pipeline use, you can use it locally but do not use any other flag with it")
    args = parser.parse_args()
    if args.redirect is None:
        args.redirect = '/dev/null'
    if args.noparse is None:
        args.noparse = True


    os.chdir(os.path.dirname(__file__))
    os.chdir('../../../')
    plans_path = 'plexil/test/TestExec-regression-test/plans/'
    scripts_path = 'plexil/test/TestExec-regression-test/scripts/'
    parsed_tests_path = 'semantics/test/regression/parsedTests/'

    # Get the list of plans and scripts to compile
    plans = os.listdir(plans_path)
    plans = [plan for plan in plans if plan.endswith('.ple')]
    scripts = os.listdir(scripts_path)
    scripts = [script for script in scripts if script.endswith('.pst')]

    if args.compile == True:
        compile_plans_and_scripts(plans_root=plans_path, scripts_root=scripts_path, plans=plans, scripts=scripts, args=args)

    # Get the list of compiled plans and scripts
    compiled_plans = os.listdir(plans_path)
    compiled_plans = [plan for plan in compiled_plans if plan.endswith('.plx')]
    compiled_scripts = os.listdir(scripts_path)
    compiled_scripts = [script for script in compiled_scripts if script.endswith('.psx')]

    check_dir_exists(parsed_tests_path)

    if args.noparse is True:
        parse_plans_and_scripts(plans_root=plans_path, scripts_root=scripts_path, compiled_plans=compiled_plans, compiled_scripts=compiled_scripts, parsed_tests_root=parsed_tests_path, args=args)

    # Get the list of parsed plans and scripts
    maude_plans = os.listdir(parsed_tests_path + 'plans/')

    passed_tests = run_tests_and_compare(maude_plans=maude_plans, parsed_tests_root=parsed_tests_path, plans_root=plans_path, scripts_root=scripts_path, compiled_scripts=compiled_scripts, args=args)
    if args.pipeline is True:
        failed_tests = []
        with open('semantics/test/regression/passed-tests.txt', 'r') as file:
            for line in file:
                line = line.strip()
                if line not in passed_tests:
                    failed_tests.append(line)
                else:
                    passed_tests.remove(line)
            if failed_tests != [] or len(passed_tests) > 0:
                raise Exception(f'Failed Tests: {str(failed_tests)}\nNew Tests supported: {str(passed_tests)}')

# Check if the plan has a script or return the empty input script
def get_script(plan_name, compiled_scripts):
    for script in compiled_scripts:
        if script[:-4] == plan_name:
            return script
    return 'empty.psx'

def get_script_maude(maude_plan_name):
    maude_scripts = os.listdir('semantics/test/regression/parsedTests/scripts/')
    for script in maude_scripts:
        if script[:-6] == maude_plan_name:
            return script
    return 'empty.maude'

def remove_extra_whitespace(*, scripts_root, script):
    tree = ET.parse(scripts_root + script)
    root = tree.getroot()
    xml_str = ET.tostring(root, encoding='unicode')
    clean_xml = re.sub(">\s+?<","><", xml_str)
    with open(scripts_root + script, 'w') as file:
        file.write(clean_xml)

def compile_plans_and_scripts(*, plans_root, scripts_root, plans, scripts, args):
    commands_compile = []
    for plan in plans:
        if str(args.test) in str(plan) or args.test is None:
            commands_compile.append('plexilc ' + plans_root + str(plan))

    for script in scripts:
        if str(args.test) in str(script) or args.test is None:
            commands_compile.append('plexilc ' + scripts_root + str(script))

    # Compile plans and scripts in parallel
    n = 8
    for i in range(max(int(len(commands_compile)/n), 1)):
        procs = [subprocess.Popen(i, shell=True) for i in commands_compile[i*n: min((i+1)*n, len(commands_compile))]]
        for p in procs:
            p.wait()

def check_dir_exists(parsed_tests_path):
    if not os.path.exists(parsed_tests_path):
        os.makedirs(parsed_tests_path)
    if not os.path.exists(parsed_tests_path + 'plans/'):
        os.makedirs(parsed_tests_path + 'plans/')
    if not os.path.exists(parsed_tests_path + 'scripts/'):
        os.makedirs(parsed_tests_path + 'scripts/')

def parse_plans_and_scripts(*, plans_root, scripts_root, compiled_plans, compiled_scripts, parsed_tests_root, args):
    commands_parse = []
    for plan in compiled_plans:
        if str(args.test) in str(plan) or args.test is None:
            commands_parse.append('plx2maude ' + plans_root + str(plan) + ' >> ' + parsed_tests_root + 'plans/' + str(plan[:-4] + '.maude'))

    for script in compiled_scripts:
        commands_parse.append('psx2maude ' + scripts_root + str(script) + ' >> ' + parsed_tests_root + 'scripts/' +  str(script[:-4] + '.maude'))
        remove_extra_whitespace(scripts_root=scripts_root, script=script)

    commands_parse.append('psx2maude ' + scripts_root + 'empty.psx' + ' >> ' + parsed_tests_root + 'scripts/' +  'empty.maude')
    # Remove the old plans and scripts
    os.system('rm ' + parsed_tests_root + 'plans/' + '*.maude')
    os.system('rm ' + parsed_tests_root + 'scripts/' + '*.maude')

    for command in commands_parse:
        os.system(command)

def run_tests_and_compare(*, maude_plans, parsed_tests_root, plans_root, scripts_root, compiled_scripts, args):
    passed_tests = []
    for maude_plan in maude_plans:
        if str(args.test) in str(maude_plan) or args.test is None:
            os.system('rm semantics/test/regression/output.maude')
            os.system('rm semantics/test/regression/output.plexil')
            with open('semantics/test/regression/' + 'run.maude', 'w') as file:
                file.write(f'''load ../../src/plexilite.maude
load parsedTests/plans/{maude_plan}
load parsedTests/scripts/{get_script_maude(maude_plan[:-6])}

set print attribute on .
set print color on .

mod PLAN-SIMULATION is
    protecting {maude_plan[:-6]}-PLAN .
    protecting INPUT .
endm

rew run(compile(rootNode,input)) .
q
'''
            )
            #Run the maude plan with the corresponding script (they have the same name) and plexiltest
            run_command_with_timeout('maude -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr ' + 'semantics/test/regression/' + 'run.maude ' + parsed_tests_root + 'plans/' + maude_plan + ' ' + parsed_tests_root + 'plans/' + maude_plan + ' > ' + args.redirect + ' 2> semantics/test/regression/output.maude', 5)
            plan_script = get_script(maude_plan[:-6], compiled_scripts)
            run_command_with_timeout('plexiltest -p '+ plans_root + maude_plan[:-6] + '.plx' + ' -s ' + scripts_root + plan_script + ' -d semantics/benchmark/Debug.AcceptanceTest.cfg' + ' > semantics/test/regression/output.plexil 2>&1',5)
            print('\n\n' + '--------------------------------' + maude_plan + '--------------------------------' + '\n')
            if capture_console_output(['plexilog-diff', 'semantics/test/regression/output.plexil', 'semantics/test/regression/output.maude']) == 'EQ':
                passed_tests.append(maude_plan)
    return passed_tests

def capture_console_output(command):
    try:
        result = subprocess.run(command, capture_output=True, text=True, timeout=5)
        output = result.stdout.strip()
        print(output)
        return output
    except subprocess.TimeoutExpired:
        return 'Infinite loop'

def run_command_with_timeout(command, timeout):
    try:
        process = subprocess.Popen(command, shell=True)
        process.wait(timeout=timeout)
    except subprocess.TimeoutExpired:
        process.terminate()
        print('A test has likely encountered an infinite loop')

def main():
    runRegression()

if __name__ == '__main__':
    main()
