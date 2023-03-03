{-# LANGUAGE QuasiQuotes #-}

module PLEXIL.LogParserSpec where

import PLEXIL.LogParser
 
import qualified Data.Set as Set
import Test.Hspec
-- import Text.RawString.QQ

-- import Control.Monad.Identity
import Text.Parsec hiding (State)
-- import Text.Parsec.Char
-- import Text.ParserCombinators.Parsec.Number

spec :: Spec
spec = do
    describe "parseMicro" $ do
        it "returns a micro-step element when there is one transition" $
            let input = unlines ["[PlexilExec:step][1:0] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0"
                                ,"[PlexilExec:step][1:0] State change queue: TRAFFIC_RESOLUTION 0x6e3dc0"
                                ,"[PlexilExec:step][1:0:0] Transitioning node TRAFFIC_RESOLUTION 0x6e3dc0 from INACTIVE to WAITING"]
            in (parseMicro input `shouldBe` (Right $ Micro $ Set.fromList [Transition (Id "'TRAFFIC-RESOLUTION") Inactive Waiting]))

    describe "parseMicro first match" $ do
        it "matches our working example" $
            let input = "[PlexilExec:step][1:0] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0\n"
                match = (string "[PlexilExec:step]" *> p_step_id *> string " Check queue: " *> many (noneOf "\n") *> spaceEOL) *> (pure "OK")
            in parse match "header" input `shouldBe` (Right "OK")

    describe "parseMicro second match" $ do
        it "matches our working example" $
            let input = "[PlexilExec:step][1:0] State change queue: TRAFFIC_RESOLUTION 0x6e3dc0\n"
                match =(string "[PlexilExec:step]" *> p_step_id *> string " State change queue: ") *> many (noneOf "\n") *> spaceEOL *> (pure "OK")
            in parse match "header" input `shouldBe` (Right "OK")

    describe "parseMicro main match" $ do
        it "matches our working example" $
            let input = "[PlexilExec:step][1:0:0] Transitioning node 'TRAFFIC_RESOLUTION 0x6e3dc0 from INACTIVE to WAITING\n"
                match = (try $ many (try p_transition)) *> pure "OK"
            in parse match "header" input `shouldBe` (Right "OK")

    describe "parseTransition main match" $ do
        it "matches our working example" $
            let input = "[PlexilExec:step][1:0:0] Transitioning node TRAFFIC_RESOLUTION 0x6e3dc0 from INACTIVE to WAITING\n"
            in parse p_transition "transition" input `shouldBe` (Right $ Transition (Id "'TRAFFIC-RESOLUTION") Inactive Waiting)

    describe "parseMicroOrEmpty first match" $ do
        it "matches our working example" $
            let input = "[PlexilExec:step][23:18] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0 ASSIGNMENT__0 0x72db80 ASSIGNMENT__1 0x72dde0 ASSIGNMENT__2 0x72e040 ASSIGNMENT__3 0x72e2a0 ASSIGNMENT__4 0x72e500 ASSIGNMENT__5 0x72e760 ASSIGNMENT__6 0x72e9c0 ASSIGNMENT__7 0x72ec20 ASSIGNMENT__8 0x72ee80 ASSIGNMENT__9 0x72f0e0 BLOCK__10 0x72f340 ASSIGNMENT__81 0x73a430 \n"
            in parse p_empty_micro "header" input `shouldBe` (Right ())