{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}

module TestCommon where

import Data.Char
import Data.String (fromString)
import Text.XML.Cursor
import Test.Tasty
import Test.Tasty.HUnit
import Text.PrettyPrint
import Text.RawString.QQ
import Text.XML hiding (Name)

import Parser (ParseError)

newtype TestInput  = In String
newtype TestOutput = Out String

testParser :: (Cursor -> Doc) -> [(String,String,String)] -> [TestTree]
testParser f = map testCaseParser'
  where
    testCaseParser' (msg,i,o) = testCaseParser f (msg,In i,Out o)

testCaseParser :: (Cursor -> Doc) -> (String, TestInput, TestOutput) -> TestTree
testCaseParser f (msg,In i,Out o) =
  testCase msg $
    renderAndClean (f cursor) @?=  renderAndClean (text o)
    where
      renderAndClean = clean . render
        where
          render = renderStyle (style {mode = OneLineMode})
          clean = filter (not . isSpace)
      cursor = fromDocument doc
        where
          doc = parseText_ def $ fromString i

testParserGeneric :: (Eq t, Show t) => (Cursor -> t) -> [(String,String,t)] -> [TestTree]
testParserGeneric f = map testCaseParserGeneric'
  where
    testCaseParserGeneric' (msg,i,o) = testCaseParserGeneric f (msg,In i,o)

testCaseParserGeneric :: (Eq t, Show t) => (Cursor -> t) -> (String, TestInput, t) -> TestTree
testCaseParserGeneric f (msg,In i,o) =
  testCase msg $
    f cursor @?= o
    where
      cursor = fromDocument doc
        where
          doc = parseText_ def $ fromString i

testErrorParser :: (Cursor -> ParseError Doc) -> [(String,String,String)] -> [TestTree]
testErrorParser f = map testCaseErrorParser'
  where
    testCaseErrorParser' (msg,i,o) = testCaseErrorParser f (msg,In i,Out o)

testStringErrorParser :: (Cursor -> ParseError String) -> [(String,String,String)] -> [TestTree]
testStringErrorParser f = map testCaseErrorParser'
  where
    testCaseErrorParser' (msg,i,o) = testCase msg $ (f cursor) @?= return o
      where
        cursor = fromDocument doc
          where
            doc = parseText_ def $ fromString i

testCaseErrorParser :: (Cursor -> ParseError Doc) -> (String, TestInput, TestOutput) -> TestTree
testCaseErrorParser f (msg,In i,Out o) =
  testCase msg $
    renderAndClean <$> (f cursor) @?= return (renderAndClean (text o))
    where
      renderAndClean = clean . render
        where
          render = renderStyle (style {mode = OneLineMode})
          clean = filter (not . isSpace)
      cursor = fromDocument doc
        where
          doc = parseText_ def $ fromString i

getTestCursor :: String -> Cursor
getTestCursor str = fromDocument doc
  where
    doc = parseText_ def $ fromString str