{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}

module TestNodeRef where

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
-- import Data.Text.Internal.Lazy
import Test.Tasty
import Test.Tasty.HUnit
import Text.PrettyPrint
-- import qualified Text.PrettyPrint as PP
import Text.RawString.QQ
import TestCommon

testNodeRef :: TestTree
testNodeRef =
  testGroup "Node References"
    [let c = getTestCursor "<testContext><NodeRef dir=\"self\" /></testContext>"
      in testCase "self is parsed" $ parseRelativeNodeReference c @?= Right ("","self")
    ]