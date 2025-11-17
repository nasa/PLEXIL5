{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}

module GlobalDeclarations where

-- import Control.Applicative
-- import Control.Monad.Except
-- import Data.Char
-- import Data.Either
-- import Data.Maybe
-- import Data.List (intersperse)
-- import qualified Data.Map as M
-- import Data.String (fromString)
-- import Data.Text (Text) --  hiding (map)
-- import Debug.Trace
-- import qualified Data.Text as T --  hiding (map)
-- import Prelude -- hiding (concat)
-- import System.Environment
-- import System.Exit

-- import Debug.Trace
import Test.Tasty
import Test.Tasty.HUnit
-- import Text.PrettyPrint
-- import qualified Text.PrettyPrint as PP
-- import Text.RawString.QQ (r)
-- import Text.XML hiding (Name)
-- import qualified Text.XML as XML
-- import Text.XML.Cursor

testGlobalDeclarations :: TestTree
testGlobalDeclarations = testGroup "Global Declarations"
    [testCase "Global Declarations dummy"
        (assertBool "<msg>" False)
    ,testParameter
    ]


testParameter :: TestTree
testParameter = testGroup "Parameter" $ testifyAll []
--    [("Simple type",
--      [r|
--          <Parameter>
--              <Name>fromHeading</Name>
--              <Type>Real</Type>
--          </Parameter>
--      |],
--      (Right ():: Either () ()) @?= Right ()
--    ]

type TestEntry = (TestName,String,String)

testifyAll :: [TestEntry] -> [TestTree]
testifyAll _ = []
