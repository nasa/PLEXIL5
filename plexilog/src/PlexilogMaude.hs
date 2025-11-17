module Main where

import PLEXIL.LogParser (parseMaudeTrace,seqTrace)

main :: IO ()
main = do
  contents <- getContents
  case parseMaudeTrace contents of
    Left a -> putStrLn $ "Error found: " ++ show a
    Right b -> putStrLn $ show $ seqTrace b
  putStrLn "FINISHED"
