{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}

module TestArrays where

import Parser
import Control.Applicative
import Control.Monad.Except
import Data.Either
import Data.Maybe
import Prelude hiding ((<>)) -- (concat)
import Test.Tasty
import Test.Tasty.HUnit
import Text.PrettyPrint
import Text.RawString.QQ
import Text.XML hiding (Name)
import Text.XML.Cursor

import TestCommon

testArrays :: TestTree
testArrays =
    testGroup "Arrays" $ [
      testParseArrayValue
      ,testParseArray
      -- ,testIsArrayValue
      ,testHasArrayValue1Level
    ]

testParseArrayValue :: TestTree
testParseArrayValue =
  testGroup "Parse an array value" $
    testParser elementVisitor
    [("ArrayValueString",
        [r|
          <ArrayValue Type="String">
            <StringValue>zero</StringValue>
            <StringValue>one</StringValue>
            <StringValue>two</StringValue>
          </ArrayValue>
        |],
        "const(array(\"zero\" # \"one\" # \"two\"))"),
      ("ArrayValueInteger",
        [r|
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
        |],
        "const(array(0 # 1 # 2 # 3 # 4 # 5 # 6 # 7 # 8 # 9))"),
        ("ArrayValueReal",
        [r|
          <ArrayValue Type="Real">
            <RealValue>0.0</RealValue>
            <RealValue>1.0</RealValue>
            <RealValue>2.0</RealValue>
            <RealValue>3.0</RealValue>
            <RealValue>4.0</RealValue>
            <RealValue>5.0</RealValue>
            <RealValue>6.0</RealValue>
            <RealValue>7.0</RealValue>
            <RealValue>8.0</RealValue>
            <RealValue>9.0</RealValue>
          </ArrayValue>
        |],
        "const(array(0.0 # 1.0 # 2.0 # 3.0 # 4.0 # 5.0 # 6.0 # 7.0 # 8.0 # 9.0))"),
        ("ArrayValueBoolean",
        [r|
          <ArrayValue Type="Boolean">
            <BooleanValue>TRUE</BooleanValue>
            <BooleanValue>FALSE</BooleanValue>
          </ArrayValue>
        |],
        "const(array(true # false))")
        ]

testParseArray :: TestTree
testParseArray =
  testGroup "Parse an array" $
    testParser elementVisitor [("ArrayString",
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
        "('a2:array(\"zero\" # \"one\" # \"two\"))"),
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
        "('a1:array(0 # 1 # 2 # 3 # 4 # 5 # 6 # 7 # 8 # 9))"),
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
        "('a4:array(12.3 # 3456.67856))"),
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
        "('a3:array(true # false))")
      ]

testHasArrayValue1Level :: TestTree
testHasArrayValue1Level =
  testGroup "Has an array value at level 2 deep" $
    testParserGeneric hasArrayValue1Level [("ArrayValueString",
        [r|
          <InitialValue>
              <ArrayValue Type="Boolean">
                <BooleanValue>true</BooleanValue>
                <BooleanValue>false</BooleanValue>
              </ArrayValue>
          </InitialValue>
        |],
        True)
        ]

-- testIsArrayValue :: TestTree
-- testIsArrayValue =
--   testGroup "Is an array value" $
--     testParserGeneric isArrayValue [("ArrayValueString",
--         [r|
--           <ArrayValue Type="String">
--             <StringValue>zero</StringValue>
--             <StringValue>one</StringValue>
--             <StringValue>two</StringValue>
--           </ArrayValue>
--         |],
--         True)
--         ]