module Main where

import PLEXIL.LogParser (parseTrace,seqTrace)

main :: IO ()
main = do
  contents <- getContents
  case parseTrace contents of
    Left a -> putStrLn $ "Error found: " ++ show a
    Right b -> putStrLn $ show $ seqTrace b
