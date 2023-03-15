import subprocess
import os

# Run the regression tests
def runRegression():
    os.chdir('../../../')
    plans_path = 'plexil/test/TestExec-regression-test/plans/'
    scripts_path = 'plexil/test/TestExec-regression-test/scripts/'
    semantics_path = 'semantics/src/plexilite.maude'
    parsed_tests_path = 'semantics/test/regression/parsedTests/'
    commands_compile = []
    commands_parse = []
    commands_maude = []


    # Get the list of plans and scripts to compile
    plans = os.listdir(plans_path)
    plans = [plan for plan in plans if plan.endswith('.ple')]
    scripts = os.listdir(scripts_path)
    scripts = [script for script in scripts if script.endswith('.pst')]

    # Generate the commands to compile plans and scripts
    for plan in plans:
        commands_compile.append('plexilc ' + plans_path + str(plan))
    
    for script in scripts:
        commands_compile.append('plexilc ' + scripts_path + str(script))

    # Compile plans and scripts in parallel
    n = 8
    for i in range(max(int(len(commands_compile)/n), 1)):
        procs = [subprocess.Popen(i, shell=True) for i in commands_compile[i*n: min((i+1)*n, len(commands_compile))]]
        for p in procs:
            p.wait()

    # Get the list of compiled plans and scripts
    compiled_plans = os.listdir(plans_path)
    compiled_plans = [plan for plan in compiled_plans if plan.endswith('.plx')]
    compiled_scripts = os.listdir(scripts_path)
    compiled_scripts = [script for script in compiled_scripts if script.endswith('.psx')]
    
    # Generate the commands to parse plans and scripts
    for plan in compiled_plans:
        commands_parse.append('plx2maude ' + plans_path + str(plan) + ' >> ' + parsed_tests_path + 'plans/' + str(plan[:-4] + '.maude'))
    
    for script in compiled_scripts:
        commands_parse.append('psx2maude ' + scripts_path + str(script) + ' >> ' + parsed_tests_path + 'scripts/' +  str(script[:-4] + '.maude'))

    # Remove the old plans and scripts
    os.system('rm ' + parsed_tests_path + 'plans/' + '*.maude')
    os.system('rm ' + parsed_tests_path + 'scripts/' + '*.maude')

    #Parse plans and scripts in parallel
    n = 8
    for i in range(max(int(len(commands_parse)/n), 1)):
        procs = [subprocess.Popen(i, shell=True) for i in commands_parse[i*n: min((i+1)*n, len(commands_parse))]]
        for p in procs:
            p.wait()

    # Get the list of parsed plans and scripts
    maude_plans = os.listdir(parsed_tests_path + 'plans/')

    # Run plexiltest differentiating plans with and without script
    for compiled_plan in compiled_plans:
        plan_script = has_script(compiled_plan[:-4], compiled_scripts)
        os.system('plexiltest -p '+ plans_path + compiled_plan + ' -s ' + scripts_path + plan_script)

    # Run the maude plans and generate the corresponding run.maude
    for maude_plan in maude_plans:
        print(maude_plan)
        with open('semantics/test/regression/' + 'run.maude', 'w') as file:
            file.write(f'''load ../../src/plexilite.maude
load parsedTests/plans/{maude_plan}
            
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
        #Run the maude plan with the corresponding script (they have the same name)
        os.system('maude -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr ' + 'semantics/test/regression/' + 'run.maude ' + parsed_tests_path + 'plans/' + maude_plan + ' ' + parsed_tests_path + 'plans/' + maude_plan)

# Check if the plan has a script or return the empty input script
def has_script(plan_name, compiled_scripts):
    for script in compiled_scripts:
        if script[:-4] == plan_name:
            return script
    return 'empty.psx'

def main():
    runRegression()

if __name__ == '__main__':
    main()
