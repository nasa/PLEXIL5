{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}

module Main where

import Parser
import Control.Applicative
import Control.Monad.Except
import Data.Char
import Data.Either
import Data.Maybe
-- import Data.List (intersperse)
-- import qualified Data.Map as M
import Data.String (fromString)
-- import Data.Text (Text) --  hiding (map)
-- import Debug.Trace
-- import qualified Data.Text as T --  hiding (map)
import Prelude hiding ((<>)) -- (concat)
-- import System.Environment
-- import System.Exit

-- import Debug.Trace
-- import Data.Text.Internal.Lazy
import Test.Tasty
import Test.Tasty.HUnit
import Text.PrettyPrint
-- import qualified Text.PrettyPrint as PP
import Text.RawString.QQ
import Text.XML hiding (Name)
-- import qualified Text.XML as XML
import Text.XML.Cursor

import Benchmarks(testBenchmarks)
import PSX2MaudeTests(testPSX2Maude)

------------------------------------------------------------
----------    Test helpers

benchmark2 :: IO ()
benchmark2 = (fmap (render . elementVisitor) $ fromDocument <$> Text.XML.readFile def "/home/mfeliu/Desktop/xmlparser/haskell/HaXml-try-1/example.plx") >>= putStrLn

benchmark3 :: IO ()
benchmark3 = (fmap (render . elementVisitor) $ fromDocument <$> Text.XML.readFile def "/home/mfeliu/Desktop/PLEXIL/maude-semantics/benchmark/icarous/TRAFFIC_RESOLUTION.plx") >>= putStrLn


main :: IO ()
main = defaultMain $
  testGroup "Tests"
    [testsLegacy
    ,testBenchmarks
    ,testPSX2Maude
    ]

testsLegacy :: TestTree
testsLegacy =
    testGroup "Legacy Tests"
        [testParseArray
        ,testsParseBooleanExpression
        ,testsParseNodeStateValue
        ,testsParseNodeStateVariable
        ,testsParseNodeOutcomeVariable
        ,testsParseNodeState
        ,testsPrettyOutcomeEq
        ,testsPrettyStateEq
        ,testsBinarizeList
        ,testsParseNodeCondition
        ,testLookups
        ,testGroup "Parse internal equality expression" $
            map (testify'' elementVisitor)
                [("An icarous node failure equality expression ",
                  [r|
                     <EQInternal>
                       <NodeFailureVariable>
                         <NodeRef dir="sibling">ep2cp_IfTest</NodeRef>
                       </NodeFailureVariable>
                       <NodeFailureValue>POST_CONDITION_FAILED</NodeFailureValue>
                     </EQInternal>
                      |],
                  [r|isFailure?('ep2cp-IfTest,postconditionFailed)|])
                ]
        ,testGroup "Parse variable declarations" $
            map (testify'' elementVisitor)
                [("A simple variable declaration",
                  [r|
                     <VariableDeclarations>
                       <DeclareVariable>
                         <Name>r1</Name>
                         <Type>Real</Type>
                       </DeclareVariable>
                     </VariableDeclarations>
                    |],
                  [r| ( ('r1 : val(0.0)) ) |])
                ,
                 ("A simple variable declaration with initialiation",
                  [r|
                     <VariableDeclarations>
                       <DeclareVariable LineNo="6" ColNo="2">
                         <Name>i</Name>
                         <Type>Integer</Type>
                         <InitialValue>
                           <IntegerValue>0</IntegerValue>
                         </InitialValue>
                       </DeclareVariable>
                     </VariableDeclarations>
                    |],
                  [r| ( ('i : val(0)) ) |])
                ,("An icarous array declaration",
                  [r|
                     <DeclareArray LineNo="23" ColNo="4">
                       <Name>acPosition</Name>
                       <Type>Real</Type>
                       <MaxSize>3</MaxSize>
                     </DeclareArray>|],
                  [r| ('acPosition : unknownRealArray(3)) |])
                ,("An icarous array declaration with explicit initialization",
                  [r|
                     <DeclareArray LineNo="4" ColNo="2">
                       <Name>bar</Name>
                       <Type>Real</Type>
                       <MaxSize>3</MaxSize>
                       <InitialValue>
                         <RealValue>0.0</RealValue>
                         <RealValue>7.0</RealValue>
                         <RealValue>13.0</RealValue>
                       </InitialValue>
                     </DeclareArray>|],
                  [r| ('bar : array(val(0.0) # val(7.0) # val(13.0))) |])
                ]
        ,testGroup "Parse an array element" $
            map (testify'' elementVisitor)
                [("A simple array element",
                  [r|
                     <ArrayElement>
                       <ArrayVariable>foo</ArrayVariable>
                       <Index>
                         <IntegerVariable>i</IntegerVariable>
                       </Index>
                     </ArrayElement>
                    |],
                  [r| ( 'foo ) [ var( 'i ) ] |])
                ]
        ,testGroup "Parse an empty node" $
            map (testify'' elementVisitor)
                [("An icarous Empty node",
                  [r|
                     <Node NodeType="Empty" epx="Condition">
                       <NodeId>ep2cp_IfTest</NodeId>
                       <PostCondition>
                         <GE>
                           <RealVariable>speedPref</RealVariable>
                           <IntegerValue>0</IntegerValue>
                         </GE>
                       </PostCondition>
                     </Node>|],
                  [r|
                    empty(
                      'ep2cp-IfTest,
                      nilocdecl,
                      ((post: (_>=_(var('speedPref),const(val(0))))))
                    )|])
                ]
        ,testGroup "Parse an assignment node" $
            map (testify'' elementVisitor)
                [("A simple assignment node",
                  [r|
                     <Node NodeType="Assignment" LineNo="13" ColNo="4">
                       <NodeId>ASSIGNMENT__0</NodeId>
                       <NodeBody>
                         <Assignment>
                           <RealVariable>r1</RealVariable>
                           <NumericRHS>
                             <LookupNow>
                               <Name>
                                 <StringValue>r1</StringValue>
                               </Name>
                             </LookupNow>
                           </NumericRHS>
                         </Assignment>
                       </NodeBody>
                     </Node>|],
                  [r|
                    assignment(
                      'ASSIGNMENT--0,
                      nilocdecl,
                      ( none ),
                      ('r1 := lookup ('r1 , nilarg))
                    )|])
                ,("A simple array assignment node",
                  [r|
                     <Node NodeType="Assignment" LineNo="11" ColNo="27">
                       <NodeId>SimpleArrayAssignment1</NodeId>
                       <NodeBody>
                         <Assignment>
                           <ArrayElement>
                             <ArrayVariable>foo</ArrayVariable>
                             <Index>
                               <IntegerVariable>i</IntegerVariable>
                             </Index>
                           </ArrayElement>
                           <NumericRHS>
                             <RealValue>1.0</RealValue>
                           </NumericRHS>
                         </Assignment>
                       </NodeBody>
                     </Node>
                    |],
                  [r|
                    assignment(
                      'SimpleArrayAssignment1,
                       nilocdecl,
                       ( none ),
                       ( ('foo ) [ var('i) ] := const(val(1.0)))
                    )
                    |])
                ,("An icarous complex array assignment node",
                  [r|
                     <Assignment>
                       <ArrayElement>
                         <ArrayVariable>velCmd</ArrayVariable>
                         <Index>
                           <IntegerValue>0</IntegerValue>
                         </Index>
                       </ArrayElement>
                       <NumericRHS>
                         <ArrayElement>
                           <ArrayVariable>acVelocity</ArrayVariable>
                           <Index>
                             <IntegerValue>0</IntegerValue>
                           </Index>
                         </ArrayElement>
                       </NumericRHS>
                     </Assignment>
                    |],
                  [r|
                       ( ('velCmd ) [ const(val(0)) ] := ( arrayVar('acVelocity,const(val(0))) ) )
                    |])

                ]
        ,testGroup "Parse a command node" $
            map (testify'' elementVisitor)
                [("A simple command node",
                  [r|
                     <Node NodeType="Command">
                       <NodeId>Example1</NodeId>
                       <StartCondition>
                         <BooleanValue>true</BooleanValue>
                       </StartCondition>
                       <NodeBody>
                         <Command>
                           <Name>
                             <StringValue>pprint</StringValue>
                           </Name>
                           <Arguments LineNo="40" ColNo="11">
                             <StringValue>r:</StringValue>
                             <RealVariable>r</RealVariable>
                           </Arguments>
                         </Command>
                       </NodeBody>
                     </Node>|],
                  [r|
                    command(
                      'Example1,
                      nilocdecl,
                      ( (startc: (const(val(true)))) ),
                      ( ('pprint) / ( const(val("r:")) var('r) ) )
                    )|])
                ,("A complex command node",
                  [r|
                     <Node NodeType="Command" LineNo="34" ColNo="4">
                       <NodeId>Printer0</NodeId>
                       <StartCondition>
                         <BooleanValue>false</BooleanValue>
                       </StartCondition>
                       <EndCondition>
                         <AND>
                           <EQInternal>
                             <NodeStateVariable>
                               <NodeRef dir="sibling">TestReal__2</NodeRef>
                             </NodeStateVariable>
                             <NodeStateValue>FINISHED</NodeStateValue>
                           </EQInternal>
                           <EQInternal>
                             <NodeStateVariable>
                               <NodeId>TestVars1</NodeId>
                             </NodeStateVariable>
                             <NodeStateValue>FINISHED</NodeStateValue>
                           </EQInternal>
                         </AND>
                       </EndCondition>
                       <NodeBody>
                         <Command>
                           <Name>
                             <StringValue>pprint</StringValue>
                           </Name>
                           <Arguments LineNo="35" ColNo="11">
                             <StringValue>r1:</StringValue>
                             <RealVariable>r1</RealVariable>
                             <StringValue>r2:</StringValue>
                             <RealVariable>r2</RealVariable>
                           </Arguments>
                         </Command>
                       </NodeBody>
                     </Node>|],
                  [r|
                    command(
                      'Printer0,
                      nilocdecl,
                      ( (startc: (const(val(false)))),
                        (endc: (_and_(isStatus?('TestReal--2,finished),isStatus?('TestVars1,finished))))
                      ),
                      ( ('pprint) / ( const(val("r1:")) var('r1) const(val("r2:")) var('r2) ) )
                    )|])
              ]
        ,testGroup "Parse a node body" $
            map (testify'' elementVisitor)
                [("A command node body",
                  [r|
                     <NodeBody>
                       <Command>
                         <Name>
                           <StringValue>pprint</StringValue>
                         </Name>
                         <Arguments LineNo="40" ColNo="11">
                           <StringValue>r:</StringValue>
                           <RealVariable>r</RealVariable>
                         </Arguments>
                       </Command>
                     </NodeBody>|],
                  "( ('pprint) / ( const(val(\"r:\")) var('r) ) )")
                ,("An assignment node body",
                  [r|
                     <NodeBody>
                       <Assignment>
                         <RealVariable>r1</RealVariable>
                         <NumericRHS>
                           <LookupNow>
                             <Name>
                               <StringValue>r1</StringValue>
                             </Name>
                           </LookupNow>
                         </NumericRHS>
                       </Assignment>
                     </NodeBody>|],
                  "('r1 := lookup ('r1 , nilarg))")
                ]
        ,testGroup "Parse a command" $
            map (testify'' elementVisitor)
                [("Simple command",
                  [r|
                     <Command>
                       <Name>
                         <StringValue>pprint</StringValue>
                       </Name>
                       <Arguments LineNo="40" ColNo="11">
                         <StringValue>r:</StringValue>
                         <RealVariable>r</RealVariable>
                       </Arguments>
                     </Command>|],
                  "( ('pprint) / ( const(val(\"r:\")) var('r) ) )")
                ,("Simple command with assignment",
                  [r|
                     <Command>
                       <RealVariable>r</RealVariable>
                       <Name>
                         <StringValue>whatever</StringValue>
                       </Name>
                       <Arguments LineNo="40" ColNo="11">
                         <StringValue>r:</StringValue>
                         <RealVariable>r</RealVariable>
                       </Arguments>
                     </Command>|],
                  "( ('whatever) / ( const(val(\"r:\")) var('r) ) / ('r) )")
                ,("Another simple command with assignment",
                  [r|
                     <Command>
                       <RealVariable>r</RealVariable>
                       <Name>
                         <StringValue>get_real</StringValue>
                       </Name>
                     </Command>|],
                  "( ('get_real) / ( nilpar ) / ('r) )")
                ,("An icarous command with assignment",
                  [r|
                     <Command>
                       <Name>
                         <StringValue>SetVel</StringValue>
                       </Name>
                       <Arguments LineNo="86" ColNo="22">
                         <ArrayVariable>velCmd</ArrayVariable>
                       </Arguments>
                     </Command>
                     |],
                  "( ('SetVel) / (arrayVar('velCmd)) )")
                ]
        ,testGroup "Parse a name" $
            map (testify'' elementVisitor)
                [("Name with a string value element",
                  [r|
                    <Name>
                      <StringValue>pprint</StringValue>
                    </Name>|],
                  "'pprint")
                ]
        ]

type TestEntry = (TestName,String,String)

testify' :: (Cursor -> TextOrError) -> TestEntry -> TestTree
testify' f (msg,i,o) = testCase msg $
    if fromString o /= ("" :: String)
      then test f i @?= Right (fromString o)
      else assertBool "returned value should be error (Left)" $ isLeft $ test f i

testify'' :: (Cursor -> Doc) -> TestEntry -> TestTree
testify'' f (msg,i,o) = testCase msg $
    clean (renderStyle (style {mode = OneLineMode}) (test f i)) @?= clean (renderStyle (style {mode = OneLineMode}) (text (fromString o)))
    where clean s = filter (not . isSpace) s

testify''' :: (Cursor -> ParseError Doc) -> TestEntry -> TestTree
testify''' f (msg,i,o) = testCase msg $
    test f i @?= Right (text (fromString o))

test :: (Cursor -> t) -> String -> t
test f str = f cursor
    where
        doc = parseText_ def $ fromString str
        cursor = fromDocument doc

testify :: (Eq a, Show a) => (Cursor -> Maybe a) -> (TestName, String, a) -> TestTree
testify f (msg,i,o) = testCase msg $ test f i @?= Just o


testsParseNodeStateValue :: TestTree
testsParseNodeStateValue =
    testGroup "parseNodeStateValue" $
        map (testify'' elementVisitor) testsParseNodeStateValueTable

testsParseNodeStateValueTable :: [TestEntry]
testsParseNodeStateValueTable =
    [("Waiting",        "<NodeStateValue>WAITING</NodeStateValue>",         "waiting"       )
    ,("Executing",      "<NodeStateValue>EXECUTING</NodeStateValue>",       "executing"     )
    ,("Finishing",      "<NodeStateValue>FINISHING</NodeStateValue>",       "finishing"     )
    ,("Failing",        "<NodeStateValue>FAILING</NodeStateValue>",         "failing"       )
    ,("IterationEnded", "<NodeStateValue>ITERATION_ENDED</NodeStateValue>", "iterationEnded")
    ,("Finished",       "<NodeStateValue>FINISHED</NodeStateValue>",        "finished"      )
    ,("Inactive",       "<NodeStateValue>INACTIVE</NodeStateValue>",        "inactive"      )
    ]

testsParseNodeStateVariable :: TestTree
testsParseNodeStateVariable =
    testGroup "parseNodeStateVariable" $
        map (testify' parseNodeStateVariable)
            [("Simple child identifier", "<NodeStateVariable><NodeRef dir=\"child\">Node__Identifier</NodeRef></NodeStateVariable>", "Node--Identifier")
            ,("Other stuff", "<NodeStateValue>FAILING</NodeStateValue>", "")
            ]

testsParseNodeState :: TestTree
testsParseNodeState =
    testGroup "parseNodeState" $
        map (testify' parseNodeState)
            [("Identifier", "<NodeStateVariable><NodeRef dir=\"child\">NodeIdentifier</NodeRef></NodeStateVariable>", "NodeIdentifier")
            ,("StateValue", "<NodeStateValue>FAILING</NodeStateValue>", "failing")
            ]

testsParseNodeOutcomeVariable :: TestTree
testsParseNodeOutcomeVariable =
    testGroup "parseNodeOutcomeVariable" $
        map (testify'' elementVisitor)
            [("Simple child identifier", "<NodeOutcomeVariable><NodeRef dir=\"child\">Printer0</NodeRef></NodeOutcomeVariable>", "Printer0")
            ]

testsParseBooleanExpression :: TestTree
testsParseBooleanExpression =
  testGroup
    "parseBooleanExpression" $
        map (testify'' elementVisitor)
            [("BooleanValue true",
              "<BooleanValue>true</BooleanValue>"
              ,"const(val(true))")
            ,("BooleanValue false",
              "<BooleanValue>false</BooleanValue>",
              "const(val(false))")
            ,("BooleanVariable",
              "<BooleanVariable>Myvar</BooleanVariable>","var('Myvar)")
            ,("KnownTest IsKnown",
              "<IsKnown><BooleanValue>true</BooleanValue></IsKnown>",
              "isKnown(const(val(true)))")
            ,("LogicalOperator OR",
              "<OR><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue><BooleanValue>true</BooleanValue></OR>",
              "_or_(_or_(const(val(true)),const(val(false))),const(val(true)))")
            ,("LogicalOperator AND",
              "<AND><BooleanValue>true</BooleanValue><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></AND>",
              "_and_(_and_(const(val(true)),const(val(true))),const(val(false)))")
            ,("LogicalOperator AND",
              "<AND><BooleanValue>true</BooleanValue><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></AND>",
              "_and_(_and_(const(val(true)),const(val(true))),const(val(false)))")
            ,("LogicalOperator XOR",
              "<XOR><BooleanValue>true</BooleanValue><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></XOR>",
              "_xor_(_xor_(const(val(true)),const(val(true))),const(val(false)))")
            ,("LogicalOperator NOT",
              "<NOT><BooleanValue>false</BooleanValue></NOT>",
              "not_(const(val(false)))")
            ,("Equality EQBoolean",
              "<EQBoolean><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></EQBoolean>",
              "_equ_(const(val(true)),const(val(false)))")
            ,("Equality EQNumeric",
              "<EQNumeric><IntegerValue>3</IntegerValue><IntegerValue>4</IntegerValue></EQNumeric>",
              "_equ_(const(val(3)),const(val(4)))")
            ,("Equality EQInternal",
              "<EQNumeric><IntegerValue>3</IntegerValue><IntegerValue>4</IntegerValue></EQNumeric>",
              "_equ_(const(val(3)),const(val(4)))")
            ,("Non-Equality NENumeric",
              "<NENumeric><IntegerValue>3</IntegerValue><IntegerValue>4</IntegerValue></NENumeric>",
              "_nequ_(const(val(3)),const(val(4)))")
            ,("Equality EQString",
              "<EQString><StringValue>a</StringValue><StringValue>b</StringValue></EQString>",
              "_equ_(const(val(\"a\")),const(val(\"b\")))")
            ,("Non-Equality NEString",
              "<NEString><StringValue>a</StringValue><StringValue>b</StringValue></NEString>",
              "_nequ_(const(val(\"a\")),const(val(\"b\")))")
            ,("NoChildFailed",
              [r|
                <NoChildFailed>
                  <NodeRef dir="self"/>
                </NoChildFailed>|],
              "noChildFailed")
            ,("Finished",
              [r|
                <Finished>
                  <NodeRef dir="sibling">ASSIGNMENT__0</NodeRef>
                </Finished>|],
              "(isFinished?(sibling('ASSIGNMENT--0)))")
            ,("Succeeded",
              [r|
                <Succeeded>
                  <NodeRef dir="sibling">ep2cp_IfTest</NodeRef>
                </Succeeded>|],
              "hasSucceeded?(sibling('ep2cp-IfTest))")
            ,("PostconditionFailed",
              [r|
                <PostconditionFailed>
                  <NodeRef dir="sibling">ep2cp_IfTest</NodeRef>
                </PostconditionFailed>
               |],
              "hasPostconditionFailed?(sibling('ep2cp-IfTest))")
            ,("Skipped",
              [r|
                <Skipped>
                  <NodeRef dir="sibling">ep2cp_ElseIf-1</NodeRef>
                </Skipped>
               |],
              "hasSkipped?(sibling('ep2cp-ElseIf-1))")


            ]

testsPrettyOutcomeEq :: TestTree
testsPrettyOutcomeEq =
  testGroup
    "Node Outcome Equality" $
        map (testify'' elementVisitor)
            [("FAILURE",
              [r|   <EQInternal>
                        <NodeOutcomeVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeOutcomeVariable>
                        <NodeOutcomeValue>FAILURE</NodeOutcomeValue>
                    </EQInternal>|],
              "isOutcomeFailure?('TestVars1)")
            ,("SUCCESS",
              [r|   <EQInternal>
                        <NodeOutcomeValue>success</NodeOutcomeValue>
                        <NodeOutcomeVariable>
                            <NodeRef dir="child">TestVars2</NodeRef>
                        </NodeOutcomeVariable>
                    </EQInternal>|],
              "isOutcome?('TestVars2,success)")
            ,("SKIPPED",
              [r|   <EQInternal>
                        <NodeOutcomeVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeOutcomeVariable>
                        <NodeOutcomeValue>SKiPPED</NodeOutcomeValue>
                    </EQInternal>|],
              "isOutcome?('TestVars1,skipped)")
            ,("INTERRUPTED",
              [r|   <EQInternal>
                        <NodeOutcomeVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeOutcomeVariable>
                        <NodeOutcomeValue>INTERRUPTED</NodeOutcomeValue>
                    </EQInternal>|],
              "isOutcome?('TestVars1,interrupted)")
            ]

testsPrettyStateEq :: TestTree
testsPrettyStateEq =
  testGroup
    "Node State Equality" $
        map (testify'' elementVisitor)
            [("FINISHED",
              [r|   <EQInternal>
                        <NodeStateVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeStateVariable>
                        <NodeStateValue>FINISHED</NodeStateValue>
                    </EQInternal>|],
              "isStatus?('TestVars1,finished)")
            ,("WAITING",
              [r|   <EQInternal>
                        <NodeStateVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeStateVariable>
                        <NodeStateValue>waiting</NodeStateValue>
                    </EQInternal>|],
              "isStatus?('TestVars1,waiting)")
            ,("EXECUTING",
              [r|   <EQInternal>
                        <NodeStateVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeStateVariable>
                        <NodeStateValue>ExEcUTING</NodeStateValue>
                    </EQInternal>|],
              "isStatus?('TestVars1,executing)")
            ,("FINISHING",
              [r|   <EQInternal>
                        <NodeStateValue>FINISHING</NodeStateValue>
                        <NodeStateVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeStateVariable>
                    </EQInternal>|],
              "isStatus?('TestVars1,finishing)")
            ,("FAILING",
              [r|   <EQInternal>
                        <NodeStateVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeStateVariable>
                        <NodeStateValue>Failing</NodeStateValue>
                    </EQInternal>|],
              "isStatus?('TestVars1,failing)")
            ,("ITERATION_ENDED",
              [r|   <EQInternal>
                        <NodeStateVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeStateVariable>
                        <NodeStateValue>ITERATION_ENDED</NodeStateValue>
                    </EQInternal>|],
              "isStatus?('TestVars1,iterationEnded)")
            ,("INACTIVE",
              [r|   <EQInternal>
                        <NodeStateVariable>
                            <NodeRef dir="child">TestVars1</NodeRef>
                        </NodeStateVariable>
                        <NodeStateValue>INACTIVE</NodeStateValue>
                    </EQInternal>|],
              "isStatus?('TestVars1,inactive)")
            ]


testsBinarizeList :: TestTree
testsBinarizeList =
  testGroup
    "Binarize a list" $
        map (\(msg,i,o) -> testCase msg $ binarizeList i @?= o)
            [("[]",[] :: [Int],Nothing)
            ,("[1]",[1],Just $ Root 1)
            ,("[1,2]",[1,2],Just $ Branches (Root 1) (Root 2))
            ,("[1,2,3]",[1,2,3],
                Just $
                    Branches
                        (Branches (Root 1) (Root 2))
                        (Root 3)
             )
            ,("[1,2,3,4]",[1,2,3,4],
                Just $
                    Branches
                        (Branches (Root 1) (Root 2))
                        (Branches (Root 3) (Root 4))
             )
            ,("[1,2,3,4,5]",[1,2,3,4,5],
                Just $
                    Branches
                        (Branches (Root 1) (Root 2))
                        (Branches
                            (Branches (Root 3) (Root 4))
                            (Root 5))
             )
            ,("[1,2,3,4,5,6]",[1,2,3,4,5,6],
                Just $
                    Branches
                        (Branches (Root 1) (Root 2))
                        (Branches
                            (Branches (Root 3) (Root 4))
                            (Branches (Root 5) (Root 6)))

             )
            ,("[1,2,3,4,5,6,7]",[1,2,3,4,5,6,7],
                Just $
                    Branches
                        (Branches (Root 1) (Root 2))
                        (Branches
                            (Branches (Root 3) (Root 4))
                            (Branches
                                (Branches (Root 5) (Root 6))
                                (Root 7)))

             )
            ]


testsParseNodeCondition :: TestTree
testsParseNodeCondition =
    testGroup "Parse a node condition" $
        map (testify'' elementVisitor)
            [("StartCondition",
              [r|
              <StartCondition>
                <EQInternal>
                    <NodeStateVariable>
                        <NodeRef dir="sibling">TestVars1</NodeRef>
                    </NodeStateVariable>
                    <NodeStateValue>FINISHED</NodeStateValue>
                </EQInternal>
              </StartCondition>|],
              "(startc: (isStatus?('TestVars1,finished)))")
            ,("RepeatCondition",
              [r|
              <RepeatCondition>
                <BooleanVariable>Variable1</BooleanVariable>
              </RepeatCondition>|],
              "(repeatc: (var('Variable1)))")
            ,("PreCondition",
              [r|
              <PreCondition>
                <BooleanValue>TRUE</BooleanValue>
              </PreCondition>|],
              "(pre: (const(val(true))))")
            ,("PostCondition",
              [r|
              <PostCondition>
                <BooleanValue>FaLSE</BooleanValue>
              </PostCondition>|],
              "(post: (const(val(false))))")
            ,("InvariantCondition",
              [r|
              <InvariantCondition>
                <BooleanValue>TRUE</BooleanValue>
              </InvariantCondition>|],
              "(inv: (const(val(true))))")
            ,("InvariantCondition2",
              [r|
              <InvariantCondition>
                <NoChildFailed>
                  <NodeRef dir="self"/>
                </NoChildFailed>
              </InvariantCondition>|],
              "(inv: (noChildFailed))")
            ,("EndCondition",
              [r|
              <EndCondition>
                <BooleanValue>TRUE</BooleanValue>
              </EndCondition>|],
              "(endc: (const(val(true))))")
            ,("ExitCondition",
              [r|
              <ExitCondition>
                <BooleanValue>TRUE</BooleanValue>
              </ExitCondition>|],
              "(exitc: (const(val(true))))")
            ,("SkipCondition",
              [r|
              <SkipCondition>
                <BooleanValue>TRUE</BooleanValue>
              </SkipCondition>|],
              "(skip: (const(val(true))))")
            ]


testLookups :: TestTree
testLookups =
  testGroup "Lookups" $
    map (testify'' elementVisitor)
      [("LookupOnChange",
        [r|
        <LookupOnChange epx="Lookup"><Name><StringValue>inConflict</StringValue></Name></LookupOnChange>
        |],
        "lookupOnChange('inConflict, nilarg, val(0.0))")
      ]

testParseArray :: TestTree
testParseArray =
  testGroup "Parse an array" $
    map (testify'' elementVisitor)
      [("ArrayString",
        [r|
          <DeclareArray ColNo="2" LineNo="4">
            <Name>a2</Name>
            <Type>String</Type>
            <MaxSize>10</MaxSize>
            <InitialValue>
               <ArrayValue Type="String">
                  <StringValue>zero</StringValue>
                  <StringValue>one</StringValue>
                  <StringValue>two</StringValue>
               </ArrayValue>
            </InitialValue>
          </DeclareArray>
        |],
        "('a2:unknownStringArray(10))"),
        ("ArrayInteger",
        [r|
          <DeclareArray ColNo="2" LineNo="3">
            <Name>a1</Name>
            <Type>Integer</Type>
            <MaxSize>10</MaxSize>
            <InitialValue>
               <ArrayValue Type="Integer">
                  <IntegerValue>0</IntegerValue>
                  <IntegerValue>1</IntegerValue>
                  <IntegerValue>2</IntegerValue>
                  <IntegerValue>3</IntegerValue>
                  <IntegerValue>4</IntegerValue>
                  <IntegerValue>5</IntegerValue>
                  <IntegerValue>6</IntegerValue>
                  <IntegerValue>7</IntegerValue>
                  <IntegerValue>8</IntegerValue>
                  <IntegerValue>9</IntegerValue>
               </ArrayValue>
            </InitialValue>
          </DeclareArray>
        |],
        "('a1:unknownIntArray(10))"),
        ("ArrayFloat",
        [r|
          <DeclareArray ColNo="2" LineNo="6">
            <Name>a4</Name>
            <Type>Real</Type>
            <MaxSize>10</MaxSize>
            <InitialValue>
               <ArrayValue Type="Real">
                  <RealValue>12.3</RealValue>
                  <RealValue>3456.67856</RealValue>
               </ArrayValue>
            </InitialValue>
          </DeclareArray>
        |],
        "('a4:unknownRealArray(10))"),
        ("ArrayBoolean",
        [r|
          <DeclareArray ColNo="2" LineNo="5">
            <Name>a3</Name>
            <Type>Boolean</Type>
            <MaxSize>500</MaxSize>
            <InitialValue>
               <ArrayValue Type="Boolean">
                  <BooleanValue>true</BooleanValue>
                  <BooleanValue>false</BooleanValue>
               </ArrayValue>
            </InitialValue>
          </DeclareArray>
        |],
        "('a3:unknownBoolArray(500))")
      ]