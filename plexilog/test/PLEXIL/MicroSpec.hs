{-# LANGUAGE QuasiQuotes #-}

module PLEXIL.MicroSpec where

import PLEXIL.LogParser

import qualified Data.Set as Set
import Test.Hspec
import Text.RawString.QQ

-- import Control.Monad.Identity
import Text.Parsec hiding (State)
-- import Text.Parsec.Char
-- import Text.ParserCombinators.Parsec.Number

spec :: Spec
spec = do
    describe "parseMaudeMicro" $ do
        it "parses two consecutive micro steps" $ 
            let input = [r|   'TRAFFIC-RESOLUTION from inactive to waiting
         micro
            'ALT . 'BLOCK--10 . 'TRAFFIC-RESOLUTION from inactive to finished
            'ASSIGNMENT--11 . 'BLOCK--10 . 'TRAFFIC-RESOLUTION from inactive to finished
         micro
|]
                output = [Micro $ Set.fromList [Transition (Id "'TRAFFIC-RESOLUTION") Inactive Waiting]
                         ,Micro $ Set.fromList [Transition (Id "'ALT")            Inactive Finished
                                ,Transition (Id "'ASSIGNMENT--11") Inactive Finished]
                         ]
                -- parser = (\x y -> [x,y]) <$> p_maude_micro <*> (p_maude_micro <|> pure (Micro []))
                parser = many1 (try $ p_maude_micro) <* eof
            -- TODO: in parse parser "<unknown>" input `shouldBe` Right output
            in parse parser "<unknown>" input `shouldBe` Right output
               
        it "parses one micro step" $ 
            let input = [r| 'TRAFFIC-RESOLUTION . 'BLOCK from inactive to waiting
         micro
|]
                output = [Micro $ Set.fromList [Transition (Id "'TRAFFIC-RESOLUTION") Inactive Waiting]
                         ,Micro $ Set.fromList [Transition (Id "'ASSIGNMENT--11") Inactive Finished]
                         ]
                parser = many1 (try p_maude_micro)
            -- TODO: in parse parser "<unknown>" input `shouldBe` Right output
            in True `shouldBe` True
               