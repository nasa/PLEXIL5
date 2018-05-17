<<<<<<< .mine
  <PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://plexil.svn.sourceforge.net/viewvc/plexil/trunk/schema/core-plexil.xsd" FileName="/Users/hcadavid/a_plx5examples/Exchange.ple">
      <Node FileName="/Users/hcadavid/a_plx5examples/Exchange.ple" LineNo="2" ColNo="1" NodeType="NodeList">
=======
  <PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://plexil.svn.sourceforge.net/viewvc/plexil/trunk/schema/core-plexil.xsd" FileName="/Users/Camilo/Projects/plexil5/trunk/examples/plexil5/Exchange.ple">
      <Node FileName="/Users/Camilo/Projects/plexil5/trunk/examples/plexil5/Exchange.ple" LineNo="2" ColNo="1" NodeType="NodeList">
>>>>>>> .r2713
          <NodeId>Exchange</NodeId>
          <VariableDeclarations>
              <DeclareVariable>
                  <Name>x</Name>
                  <Type>Integer</Type>
                  <InitialValue>
                      <IntegerValue>1</IntegerValue>
                  </InitialValue>
              </DeclareVariable>
              <DeclareVariable>
                  <Name>y</Name>
                  <Type>Integer</Type>
                  <InitialValue>
                      <IntegerValue>2</IntegerValue>
                  </InitialValue>
              </DeclareVariable>
          </VariableDeclarations>
          <InvariantCondition>
              <EQNumeric>
                  <ADD>
                      <IntegerVariable>x</IntegerVariable>
                      <IntegerVariable>y</IntegerVariable>
                  </ADD>
                  <IntegerValue>3</IntegerValue>
              </EQNumeric>
          </InvariantCondition>
          <PostCondition>
              <NOT>
                  <EQNumeric>
                      <IntegerVariable>x</IntegerVariable>
                      <IntegerVariable>y</IntegerVariable>
                  </EQNumeric>
              </NOT>
          </PostCondition>
          <NodeBody>
              <NodeList>
<<<<<<< .mine
                  <Node FileName="/Users/hcadavid/a_plx5examples/Exchange.ple" LineNo="13" ColNo="17" NodeType="Assignment">
=======
                  <Node FileName="/Users/Camilo/Projects/plexil5/trunk/examples/plexil5/Exchange.ple" LineNo="13" ColNo="17" NodeType="Assignment">
>>>>>>> .r2713
                      <NodeId>X</NodeId>
                      <VariableDeclarations>
                          <DeclareVariable>
                              <Name>x</Name>
                              <Type>Integer</Type>
                              <InitialValue>
                                  <IntegerValue>22</IntegerValue>
                              </InitialValue>
                          </DeclareVariable>
                          <DeclareVariable>
                              <Name>w</Name>
                              <Type>Integer</Type>
                              <InitialValue>
                                  <IntegerValue>99</IntegerValue>
                              </InitialValue>
                          </DeclareVariable>
                      </VariableDeclarations>
                      <NodeBody>
                          <Assignment>
                              <IntegerVariable>x</IntegerVariable>
                              <NumericRHS>
                                  <IntegerVariable>w</IntegerVariable>
                              </NumericRHS>
                          </Assignment>
                      </NodeBody>
                  </Node>
<<<<<<< .mine
                  <Node FileName="/Users/hcadavid/a_plx5examples/Exchange.ple" LineNo="20" ColNo="17" NodeType="Assignment">
=======
                  <Node FileName="/Users/Camilo/Projects/plexil5/trunk/examples/plexil5/Exchange.ple" LineNo="18" ColNo="17" NodeType="Assignment">
>>>>>>> .r2713
                      <NodeId>Y</NodeId>
                      <NodeBody>
                          <Assignment>
                              <IntegerVariable>y</IntegerVariable>
                              <NumericRHS>
                                  <IntegerVariable>x</IntegerVariable>
                              </NumericRHS>
                          </Assignment>
                      </NodeBody>
                  </Node>
              </NodeList>
          </NodeBody>
      </Node>
  </PlexilPlan>
