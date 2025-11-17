module Main where

import PLEXIL.LogParser (seqTrace)
import PLEXIL.DebugLogParser (filterAndParseTrace)

main :: IO ()
main = do
  contents <- getContents
  case filterAndParseTrace contents of
    Left a -> putStrLn $ "Error found: " ++ show a
    Right b -> putStrLn $ show $ seqTrace b
