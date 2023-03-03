{-# LANGUAGE NamedFieldPuns #-}

module Main where

import PSX2Maude.Options
import PSX2Maude.PrettyMaude
import PLEXILScript
import Paths_plexil2maude

import Text.PrettyPrint (renderStyle,style)
import Text.XML.HXT.Core
import Text.XML.HXT.RelaxNG

main :: IO ()
main = do
  Options { psxFilePath } <- parseOptions
  rngSchema <- getDataFileName "data/plexil-script.rng"
  result:_ <-
    runX $ readDocument
      [withRelaxNG rngSchema
      ,withValidate yes
      ,withRemoveWS yes]
      psxFilePath
    >>> arr (unpickleDoc' xpickle)
  case result of
    Left err -> putStrLn err
    Right plan -> putStrLn $ renderStyle style $ pretty $ ToMaude plan
