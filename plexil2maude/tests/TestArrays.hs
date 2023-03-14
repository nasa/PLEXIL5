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
      ,testArrayAccess
      ,testArrayEquality
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
        "const(array(val(\"zero\") # val(\"one\") # val(\"two\")))"),
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
        "const(array(val(0) # val(1) # val(2) # val(3) # val(4) # val(5) # val(6) # val(7) # val(8) # val(9)))"),
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
        "const(array(val(0.0) # val(1.0) # val(2.0) # val(3.0) # val(4.0) # val(5.0) # val(6.0) # val(7.0) # val(8.0) # val(9.0)))"),
        ("ArrayValueBoolean",
        [r|
          <ArrayValue Type="Boolean">
            <BooleanValue>TRUE</BooleanValue>
            <BooleanValue>FALSE</BooleanValue>
          </ArrayValue>
        |],
        "const(array(val(true) # val(false)))")
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
        "('a2:array(val(\"zero\") # val(\"one\") # val(\"two\")))"),
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
        "('a1:array(val(0) # val(1) # val(2) # val(3) # val(4) # val(5) # val(6) # val(7) # val(8) # val(9)))"),
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
        "('a4:array(val(12.3) # val(3456.67856)))"),
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
        "('a3:array(val(true) # val(false)))")
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

testArrayAccess :: TestTree
testArrayAccess =
  testGroup "Access to an array" $
    testErrorParser parseArrayElement [("ArrayAccess",
        [r|
          <ArrayElement>
            <ArrayVariable>a4</ArrayVariable>
            <Index>
              <IntegerValue>0</IntegerValue>
            </Index>
          </ArrayElement>
        |],
        "arrayVar('a4, const(val(0)))")
        ]

testArrayEquality :: TestTree
testArrayEquality =
  testGroup
    "parseBooleanExpression" $
        testParser elementVisitor
            [("Equality EQArray",
              "<EQArray><ArrayVariable>a</ArrayVariable><ArrayVariable>b</ArrayVariable></EQArray>",
              "_equ_(arrayVar('a),arrayVar('b))")
            ,("Non-Equality NEArray",
              "<NEArray><ArrayVariable>a</ArrayVariable><ArrayVariable>b</ArrayVariable></NEArray>",
              "_nequ_(arrayVar('a),arrayVar('b))")]


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