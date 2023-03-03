{-# LANGUAGE QuasiQuotes #-}

module PLEXIL.CommonSpec where

import PLEXIL.LogParser

import Test.Hspec
-- import Text.RawString.QQ

-- import Control.Monad.Identity
import Text.Parsec hiding (State)
-- import Text.Parsec.Char
-- import Text.ParserCombinators.Parsec.Number

spec :: Spec
spec = do
    describe "parseQualifiedIdentifier" $ do

        it "parses a simple identifier as itself" $ 
            let input = "'TRAFFIC-RESOLUTION"
                output = Id input
            in parse p_qualified_identifier "<unknown>" input `shouldBe` Right output

        it "parses a single qualified identifier ignoring the qualification" $ 
            let input = "'TRAFFIC-RESOLUTION . 'ROOT"
                output = Id "'TRAFFIC-RESOLUTION"
            in parse p_qualified_identifier "<unknown>" input `shouldBe` Right output

        it "parses a double qualified identifier ignoring the qualification" $ 
            let input = "'ALT . 'BLOCK--10 . 'TRAFFIC-RESOLUTION"
                output = Id "'ALT"
            in parse p_qualified_identifier "<unknown>" input `shouldBe` Right output
