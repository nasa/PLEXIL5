{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}
{-# LANGUAGE ScopedTypeVariables #-}

module TestLibraryCall where

import Data.Char (isSpace)
import Parser (parseCommand')
import Test.Tasty (TestTree, testGroup)
import Test.Tasty.HUnit (assertBool, testCase)
import TestCommon (testErrorParser)
import qualified Text.PrettyPrint as PP
import Text.RawString.QQ (r)

import PLX.LibraryCall (parseLibraryCallNode)

testLibraryCall :: TestTree
testLibraryCall =
  testGroup
    "LibraryCall tests"
    $ testErrorParser
      parseLibraryCallNode
      [ ( "w/o arguments",
          [r|<Node ColNo="0" LineNo="3" NodeType="LibraryNodeCall">
        <NodeId>libraryCall0</NodeId>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="5">
                <NodeId>theLibrary</NodeId>
            <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>theLibrary</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node></LibraryNodeCall>
        </NodeBody>
    </Node>|],
          [r|
 library('libraryCall0,
    nilocdecl,
    (none),
    callInfo('theLibrary, ids(nilq), nilpar),
    list('theLibrary,
        nilocdecl,
        ((inv: (noChildFailed))),
        (empty('empty,nilocdecl,(none)))
    )
)|]
        )
      , ( "w/o arguments II",
          [r|<Node ColNo="0" LineNo="3" NodeType="LibraryNodeCall">
        <NodeId>libraryCall1</NodeId>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="5">
                <NodeId>anotherLibrary</NodeId>
            <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>anotherLibrary</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node></LibraryNodeCall>
        </NodeBody>
    </Node>|],
          [r|
 library('libraryCall1,
    nilocdecl,
    (none),
    callInfo('anotherLibrary, ids(nilq), nilpar),
    list('anotherLibrary,
        nilocdecl,
        ((inv: (noChildFailed))),
        (empty('empty,nilocdecl,(none)))
    )
)|]
        )
      , ( "with one input argument",
          [r|
    <Node ColNo="0" LineNo="3" NodeType="LibraryNodeCall">
        <NodeId>libraryCall1</NodeId>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="5">
                <NodeId>theLibrary</NodeId>
                <Alias>
                    <NodeParameter>number</NodeParameter>
                    <IntegerValue>1</IntegerValue>
                </Alias>
                    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>theLibrary</NodeId>
        <Interface>
            <In>
                <DeclareVariable ColNo="6" LineNo="3">
                    <Name>number</Name>
                    <Type>Integer</Type>
                </DeclareVariable>
            </In>
        </Interface>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="4" NodeType="Empty">
                    <NodeId>empty</NodeId>
                    <StartCondition ColNo="6" LineNo="5">
                        <EQNumeric ColNo="19" LineNo="5">
                            <IntegerVariable>number</IntegerVariable>
                            <IntegerValue>1</IntegerValue>
                        </EQNumeric>
                    </StartCondition>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
            </LibraryNodeCall>
        </NodeBody>
    </Node>|],
          [r|
  library('libraryCall1,
    (
        ('number : unknown)
    ),
    (none),
    callInfo('theLibrary, ids('number), const(val(1))),
    list('theLibrary,
      nilocdecl,
      ((inv: (noChildFailed))),
      (
        empty('empty,
          nilocdecl,
          ((startc: (_equ_(var('number),const(val(1))))))
        )
      )
    )
  )|]
        )
      , ( "with three input arguments",
          [r|
    <Node ColNo="0" LineNo="7" NodeType="LibraryNodeCall">
        <NodeId>libraryCall2</NodeId>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="9">
                <NodeId>theLibrary</NodeId>
                <Alias>
                    <NodeParameter>number</NodeParameter>
                    <IntegerValue>1</IntegerValue>
                </Alias>
                <Alias>
                    <NodeParameter>flag</NodeParameter>
                    <BooleanValue>true</BooleanValue>
                </Alias>
                <Alias>
                    <NodeParameter>message</NodeParameter>
                    <StringValue>OK</StringValue>
                </Alias>
                    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="3">
        <NodeId>theLibrary</NodeId>
        <Interface>
            <In>
                <DeclareVariable ColNo="6" LineNo="5">
                    <Name>number</Name>
                    <Type>Integer</Type>
                </DeclareVariable>
                <DeclareVariable ColNo="6" LineNo="6">
                    <Name>flag</Name>
                    <Type>Boolean</Type>
                </DeclareVariable>
                <DeclareVariable ColNo="6" LineNo="7">
                    <Name>message</Name>
                    <Type>String</Type>
                </DeclareVariable>
            </In>
        </Interface>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="8" NodeType="Command">
                    <NodeId>empty</NodeId>
                    <StartCondition ColNo="6" LineNo="9">
                        <AND ColNo="24" LineNo="9">
                            <EQNumeric ColNo="19" LineNo="9">
                                <IntegerVariable>number</IntegerVariable>
                                <IntegerValue>1</IntegerValue>
                            </EQNumeric>
                            <BooleanVariable>flag</BooleanVariable>
                        </AND>
                    </StartCondition>
                    <NodeBody>
                        <Command ColNo="6" LineNo="10">
                            <Name>
                                <StringValue>pprint</StringValue>
                            </Name>
                            <Arguments ColNo="13" LineNo="10">
                                <StringValue>the message is: </StringValue>
                                <StringVariable>message</StringVariable>
                            </Arguments>
                        </Command>
                    </NodeBody>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
            </LibraryNodeCall>
        </NodeBody>
    </Node>
|],
          [r|
  library('libraryCall2,
    (
      ('number : unknown)
      ('flag : unknown)
      ('message : unknown)
    ),
    (none),
    callInfo('theLibrary, ids('number . 'flag . 'message), (const(val(1)) const(val(true)) const(val("OK")))),
    list('theLibrary,
      nilocdecl,
      ((inv: (noChildFailed))),
      (command('empty,
        nilocdecl,
        ((startc: (_and_(_equ_(var('number),const(val(1))),var('flag))))),
        (('pprint) / (const(val("the message is: ")) var('message)) / nothing))
      )
    )
  )
  |]
        )
      , ( "with one output argument",
          [r|
    <Node ColNo="0" LineNo="5" NodeType="LibraryNodeCall">
        <NodeId>libraryCall3</NodeId>
        <VariableDeclarations>
            <DeclareVariable ColNo="3" LineNo="7">
                <Name>myNumber</Name>
                <Type>Integer</Type>
                <InitialValue>
                    <IntegerValue>1</IntegerValue>
                </InitialValue>
            </DeclareVariable>
        </VariableDeclarations>
        <EndCondition ColNo="3" LineNo="8">
            <EQNumeric ColNo="25" LineNo="8">
                <IntegerVariable>myNumber</IntegerVariable>
                <IntegerValue>2</IntegerValue>
            </EQNumeric>
        </EndCondition>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="9">
                <NodeId>theLibrary</NodeId>
                <Alias>
                    <NodeParameter>number</NodeParameter>
                    <IntegerVariable>myNumber</IntegerVariable>
                </Alias>
                    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>theLibrary</NodeId>
        <Interface>
            <InOut>
                <DeclareVariable ColNo="9" LineNo="3">
                    <Name>number</Name>
                    <Type>Integer</Type>
                </DeclareVariable>
            </InOut>
        </Interface>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="4" NodeType="Assignment">
                    <NodeId>assignment</NodeId>
                    <StartCondition ColNo="6" LineNo="5">
                        <EQNumeric ColNo="19" LineNo="5">
                            <IntegerVariable>number</IntegerVariable>
                            <IntegerValue>1</IntegerValue>
                        </EQNumeric>
                    </StartCondition>
                    <NodeBody>
                        <Assignment ColNo="6" LineNo="6">
                            <IntegerVariable>number</IntegerVariable>
                            <NumericRHS>
                                <IntegerValue>2</IntegerValue>
                            </NumericRHS>
                        </Assignment>
                    </NodeBody>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
            </LibraryNodeCall>
        </NodeBody>
    </Node>
|],
          [r|
  library('libraryCall3,
    (
        ('myNumber : val(1))
        )
    (
      ('number : unknown)
    ),
    ((endc: (_equ_(var('myNumber),const(val(2)))))),
    callInfo('theLibrary, ids('number), const(varForwarding('myNumber))),
    list('theLibrary,
      nilocdecl,
      ((inv: (noChildFailed))),
      (assignment('assignment,
        nilocdecl,
        ((startc: (_equ_(var('number),const(val(1)))))),
        (var( 'number ) := const(val(2)))
      ))
    )
  )
|]
        )
      , ( "with one input and two output arguments",
          [r|
    <Node ColNo="0" LineNo="5" NodeType="LibraryNodeCall">
        <NodeId>libraryCall4</NodeId>
        <VariableDeclarations>
            <DeclareVariable ColNo="3" LineNo="7">
                <Name>myNumber</Name>
                <Type>Integer</Type>
                <InitialValue>
                    <IntegerValue>1</IntegerValue>
                </InitialValue>
            </DeclareVariable>
            <DeclareVariable ColNo="3" LineNo="8">
                <Name>myMessage</Name>
                <Type>String</Type>
                <InitialValue>
                    <StringValue>INITIAL</StringValue>
                </InitialValue>
            </DeclareVariable>
        </VariableDeclarations>
        <EndCondition ColNo="3" LineNo="9">
            <AND ColNo="30" LineNo="9">
                <EQNumeric ColNo="25" LineNo="9">
                    <IntegerVariable>myNumber</IntegerVariable>
                    <IntegerValue>2</IntegerValue>
                </EQNumeric>
                <EQString ColNo="43" LineNo="9">
                    <StringVariable>myMessage</StringVariable>
                    <StringValue>FINAL</StringValue>
                </EQString>
            </AND>
        </EndCondition>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="10">
                <NodeId>theLibrary</NodeId>
                <Alias>
                    <NodeParameter>number</NodeParameter>
                    <IntegerVariable>myNumber</IntegerVariable>
                </Alias>
                <Alias>
                    <NodeParameter>message</NodeParameter>
                    <StringVariable>myMessage</StringVariable>
                </Alias>
                <Alias>
                    <NodeParameter>flag</NodeParameter>
                    <BooleanValue>true</BooleanValue>
                </Alias>
                    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>theLibrary</NodeId>
        <Interface>
            <In>
                <DeclareVariable ColNo="6" LineNo="4">
                    <Name>flag</Name>
                    <Type>Boolean</Type>
                </DeclareVariable>
            </In>
            <InOut>
                <DeclareVariable ColNo="9" LineNo="3">
                    <Name>number</Name>
                    <Type>Integer</Type>
                </DeclareVariable>
                <DeclareVariable ColNo="9" LineNo="5">
                    <Name>message</Name>
                    <Type>String</Type>
                </DeclareVariable>
            </InOut>
        </Interface>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node NodeType="NodeList" epx="Sequence" ColNo="3" LineNo="6">
                    <NodeId>assignment</NodeId>
                    <StartCondition ColNo="6" LineNo="7">
                        <AND ColNo="24" LineNo="7">
                            <EQNumeric ColNo="19" LineNo="7">
                                <IntegerVariable>number</IntegerVariable>
                                <IntegerValue>1</IntegerValue>
                            </EQNumeric>
                            <BooleanVariable>flag</BooleanVariable>
                        </AND>
                    </StartCondition>
                    <InvariantCondition>
                        <NoChildFailed>
                            <NodeRef dir="self" />
                        </NoChildFailed>
                    </InvariantCondition>
                    <NodeBody>
                        <NodeList>
                            <Node ColNo="6" LineNo="8" NodeType="Assignment">
                                <NodeId>setNumber</NodeId>
                                <NodeBody>
                                    <Assignment ColNo="9" LineNo="9">
                                        <IntegerVariable>number</IntegerVariable>
                                        <NumericRHS>
                                            <IntegerValue>2</IntegerValue>
                                        </NumericRHS>
                                    </Assignment>
                                </NodeBody>
                            </Node>
                            <Node ColNo="6" LineNo="11" NodeType="Assignment">
                                <NodeId>setMessage</NodeId>
                                <StartCondition>
                                    <Finished>
                                        <NodeRef dir="sibling">setNumber</NodeRef>
                                    </Finished>
                                </StartCondition>
                                <NodeBody>
                                    <Assignment ColNo="9" LineNo="12">
                                        <StringVariable>message</StringVariable>
                                        <StringRHS>
                                            <StringValue>FINAL</StringValue>
                                        </StringRHS>
                                    </Assignment>
                                </NodeBody>
                            </Node>
                        </NodeList>
                    </NodeBody>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
            </LibraryNodeCall>
        </NodeBody>
    </Node>
|],
          [r|
  library('libraryCall4,
    (
      ('myNumber : val(1))
      ('myMessage : val("INITIAL"))
    )
    (
      ('number : unknown)
      ('message : unknown)
      ('flag : unknown)
    ),
    ((endc: (_and_(_equ_(var('myNumber),const(val(2))),_equ_(var('myMessage),const(val("FINAL"))))))),
    callInfo('theLibrary, ids('number . 'message . 'flag), (const(varForwarding('myNumber)) const(varForwarding('myMessage)) const(val(true)))),
    list('theLibrary,
      nilocdecl,
      ((inv: (noChildFailed))),
      (list('assignment,
        nilocdecl,
        (
          (startc: (_and_(_equ_(var('number),const(val(1))),var('flag)))),
          (inv: (noChildFailed))
        ),
        (assignment('setNumber,nilocdecl,(none),(var( 'number ) := const(val(2)))) assignment('setMessage,nilocdecl,((startc: ((isFinished?(sibling('setNumber)))))),(var( 'message ) := const(val("FINAL"))))))
      )
    )
  )
|]
        )
      ]
  where
    renderAndClean = clean . render
      where
        render = PP.renderStyle (PP.style {PP.mode = PP.OneLineMode})
        clean = filter (not . isSpace)