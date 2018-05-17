  <PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://plexil.svn.sourceforge.net/viewvc/plexil/trunk/schema/extended-plexil.xsd" FileName="/Users/hcadavid/NIA/Plexil5/trunk/examples/radu/testwhile.ple">
      <Sequence FileName="/Users/hcadavid/NIA/Plexil5/trunk/examples/radu/testwhile.ple" LineNo="2" ColNo="1">
          <NodeId>testwhile</NodeId>
          <VariableDeclarations>
              <DeclareVariable>
                  <Name>distance</Name>
                  <Type>Integer</Type>
                  <InitialValue>
                      <IntegerValue>5</IntegerValue>
                  </InitialValue>
              </DeclareVariable>
          </VariableDeclarations>
          <Node FileName="/Users/hcadavid/NIA/Plexil5/trunk/examples/radu/testwhile.ple" LineNo="8" ColNo="5" NodeType="Assignment">
              <NodeId>Enter</NodeId>
              <NodeBody>
                  <Assignment>
                      <IntegerVariable>distance</IntegerVariable>
                      <NumericRHS>
                          <IntegerValue>3</IntegerValue>
                      </NumericRHS>
                  </Assignment>
              </NodeBody>
          </Node>
          <While FileName="/Users/hcadavid/NIA/Plexil5/trunk/examples/radu/testwhile.ple" LineNo="13" ColNo="5">
              <NodeId>Drive</NodeId>
              <Condition>
                  <GT>
                      <IntegerVariable>distance</IntegerVariable>
                      <IntegerValue>0</IntegerValue>
                  </GT>
              </Condition>
              <Action>
                  <Node FileName="/Users/hcadavid/NIA/Plexil5/trunk/examples/radu/testwhile.ple" LineNo="15" ColNo="7" NodeType="Assignment">
                      <NodeId>Drive__CHILD__1</NodeId>
                      <NodeBody>
                          <Assignment>
                              <IntegerVariable>distance</IntegerVariable>
                              <NumericRHS>
                                  <SUB>
                                      <IntegerVariable>distance</IntegerVariable>
                                      <IntegerValue>1</IntegerValue>
                                  </SUB>
                              </NumericRHS>
                          </Assignment>
                      </NodeBody>
                  </Node>
              </Action>
          </While>
      </Sequence>
  </PlexilPlan>
