{-# LANGUAGE QuasiQuotes #-}

module PLEXIL.TransitionSpec where

import PLEXIL.LogParser

import Test.Hspec
-- import Text.RawString.QQ

-- import Control.Monad.Identity
import Text.Parsec hiding (State)
-- import Text.Parsec.Char
-- import Text.ParserCombinators.Parsec.Number

spec :: Spec
spec = do
    describe "parseMaudeTransition" $ do

        it "parses a transition as itself" $ 
            let input = "   'TRAFFIC-RESOLUTION from inactive to waiting\n"
                output = Transition (Id "'TRAFFIC-RESOLUTION") Inactive Waiting
            in parse p_maude_transition "<unknown>" input `shouldBe` Right output

        it "parses another transition as itself" $ 
            let input = "            'ALT . 'BLOCK--10 . 'TRAFFIC-RESOLUTION from inactive to finished\n"
                output = Transition (Id "'ALT") Inactive Finished
            in parse p_maude_transition "<unknown>" input `shouldBe` Right output