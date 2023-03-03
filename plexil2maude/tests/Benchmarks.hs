{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}

module Benchmarks where

import Parser
import Control.Applicative
import Control.Monad.Except
import Data.Char
import Data.Either
import Data.Maybe
import Data.List (intersperse)
import qualified Data.Map as M
import Data.String (fromString)
import Data.Text (Text) --  hiding (map)
import Debug.Trace
import qualified Data.Text as T --  hiding (map)
import Prelude hiding ((<>)) -- (concat)
import System.Environment
import System.Exit

-- import Debug.Trace
-- import Data.Text.Internal.Lazy
import Test.Tasty
import Test.Tasty.HUnit
import Text.PrettyPrint
import qualified Text.PrettyPrint as PP
import Text.RawString.QQ
import Text.XML hiding (Name)
import qualified Text.XML as XML
import Text.XML.Cursor

import GlobalDeclarations (testGlobalDeclarations,TestEntry)


------------------------------------------------------------
----------    Test helpers

main :: IO ()
main = defaultMain testBenchmarks

testBenchmarks :: TestTree
testBenchmarks =
  testGroup "Benchmarks Tests"
    [testStatePredicates
    ,benchmark "Simple command"
      [r|
<Node NodeType="NodeList" epx="Sequence" LineNo="3" ColNo="0">
  <NodeId>Plan</NodeId>
  <InvariantCondition>
    <NoChildFailed>
      <NodeRef dir="self"/>
    </NoChildFailed>
  </InvariantCondition>
  <NodeBody>
    <NodeList>
      <Node NodeType="Empty" LineNo="1" ColNo="0">
        <NodeId>Empty</NodeId>
      </Node>
    </NodeList>
  </NodeBody>
</Node>|]
      "list('Plan,nilocdecl,((inv: (noChildFailed))),(empty('Empty,nilocdecl,(none))))"
    ]

testStatePredicates :: TestTree
testStatePredicates =
  testGroup "State Predicates"
    [benchmark "Finished"
      [r|
<Finished>
  <NodeRef dir="sibling">GetResolutionType</NodeRef>
</Finished>|]
      "(isFinished?(sibling('GetResolutionType)))"
    ]

benchmark :: TestName -> String -> String -> TestTree
benchmark name input output = testify'' elementVisitor (name,input,output)
  where
    testify'' :: (Cursor -> Doc) -> TestEntry -> TestTree
    testify'' f (msg,i,o) = testCase msg $
        clean (renderStyle (style {mode = OneLineMode}) (test f i)) @?= clean (renderStyle (style {mode = OneLineMode}) (text (fromString o)))
        where o' = fromString o
              clean s = filter (not . isSpace) s

    test f str = f cursor
        where
            doc = parseText_ def $ fromString str
            cursor = fromDocument doc
