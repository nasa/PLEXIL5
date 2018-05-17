  <PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://plexil.svn.sourceforge.net/viewvc/plexil/trunk/schema/extended-plexil.xsd" FileName="/Users/hcadavid/a_plx5examples/Test.ple">
      <While FileName="/Users/hcadavid/a_plx5examples/Test.ple" LineNo="4" ColNo="1">
          <NodeId>Test</NodeId>
          <VariableDeclarations>
              <DeclareVariable>
                  <Name>x</Name>
                  <Type>Integer</Type>
                  <InitialValue>
                      <IntegerValue>0</IntegerValue>
                  </InitialValue>
              </DeclareVariable>
          </VariableDeclarations>
          <Condition>
              <LT>
                  <IntegerVariable>x</IntegerVariable>
                  <IntegerValue>10</IntegerValue>
              </LT>
          </Condition>
          <Action>
              <Node FileName="/Users/hcadavid/a_plx5examples/Test.ple" LineNo="7" ColNo="21" NodeType="Empty">
                  <NodeId>Test__CHILD__1</NodeId>
              </Node>
          </Action>
      </While>
  </PlexilPlan>
