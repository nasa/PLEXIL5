  <PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://plexil.svn.sourceforge.net/viewvc/plexil/trunk/schema/supported-plexil.xsd" FileName="/home/hcadavid/workspace/PLEXILite/examples/RepeatedSingleAssignment.ple">
      <Node NodeType="Assignment" FileName="/home/hcadavid/workspace/PLEXILite/examples/RepeatedSingleAssignment.ple" LineNo="2" ColNo="1">
          <VariableDeclarations>
              <DeclareVariable>
                  <Name>x</Name>
                  <Type>Integer</Type>
                  <InitialValue>
                      <IntegerValue>0</IntegerValue>
                  </InitialValue>
              </DeclareVariable>
          </VariableDeclarations>
          <NodeId>RepeatedSimpleAssignment</NodeId>
          <RepeatCondition>
              <BooleanValue>true</BooleanValue>
          </RepeatCondition>
          <NodeBody>
              <Assignment>
                  <IntegerVariable>x</IntegerVariable>
                  <NumericRHS>
                      <ADD>
                          <IntegerVariable>x</IntegerVariable>
                          <IntegerValue>1</IntegerValue>
                      </ADD>
                  </NumericRHS>
              </Assignment>
          </NodeBody>
      </Node>
  </PlexilPlan>
