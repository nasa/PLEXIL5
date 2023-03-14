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

    # # Compile each plan and script
    # for plan in plans:
    #     commands.append('plexilc ' + plans_path + str(plan))
    
    # for script in scripts:
    #     commands.append('plexilc ' + scripts_path + str(script))

    # n = 8
    # for i in range(max(int(len(commands_compile)/n), 1)):
    #     procs = [subprocess.Popen(i, shell=True) for i in commands_compile[i*n: min((i+1)*n, len(commands_compile))]]
    #     for p in procs:
    #         p.wait()

    compiled_plans = os.listdir(plans_path)
    compiled_plans = [plan for plan in compiled_plans if plan.endswith('.plx')]
    compiled_scripts = os.listdir(scripts_path)
    compiled_scripts = [script for script in compiled_scripts if script.endswith('.psx')]
    
    for plan in compiled_plans:
        commands_parse.append('plx2maude ' + plans_path + str(plan) + ' >> ' + parsed_tests_path + 'plans/' + str(plan[:-4] + '.maude'))
    
    for script in compiled_scripts:
        commands_parse.append('psx2maude ' + scripts_path + str(script) + ' >> ' + parsed_tests_path + 'scripts/' +  str(plan[:-4] + '.maude'))

    os.system('rm ' + parsed_tests_path + 'plans/' + '*.maude')
    os.system('rm ' + parsed_tests_path + 'scripts/' + '*.maude')

    n = 8
    for i in range(max(int(len(commands_parse)/n), 1)):
        procs = [subprocess.Popen(i, shell=True) for i in commands_parse[i*n: min((i+1)*n, len(commands_parse))]]
        for p in procs:
            p.wait()

    maude_plans = os.listdir(parsed_tests_path + 'plans/')

    print(os.getcwd())
    
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
        os.system('maude -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr ' + 'semantics/test/regression/' + 'run.maude ' + parsed_tests_path + 'plans/' + maude_plan + ' ' + parsed_tests_path + 'plans/' + maude_plan)

def main():
    runRegression()

if __name__ == '__main__':
    main()