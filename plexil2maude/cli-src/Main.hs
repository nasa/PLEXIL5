module Main where

import Parser
import Control.Monad.Except
import Prelude hiding ((<>))
import System.Environment
import System.Exit
import Text.PrettyPrint
import Text.XML hiding (Name)
import Text.XML.Cursor

main :: IO ()
main = do
    args <- getArgs
    when (null args) $
        do putStrLn "No arguments provided. Exiting..."
           exitFailure

    let filename:_ = args
    file <- Text.XML.readFile def filename
    let cursor = fromDocument file
    let output = render $ elementVisitor cursor
    putStrLn output
