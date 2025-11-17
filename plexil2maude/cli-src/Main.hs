{-# LANGUAGE NamedFieldPuns #-}

module Main where

import Control.Monad (when)
import LibraryCallExpander (oneLevelLibraryCallExpander)
import Options (Options (Options, optPLXFile, optWriteExpandedXML), parseOptions)
import Parser
import Text.PrettyPrint
import Text.XML hiding (Name)
import Text.XML.Cursor
import Prelude hiding (writeFile, (<>))

main :: IO ()
main = do
  Options {optPLXFile, optWriteExpandedXML} <- parseOptions
  standalonePlan <- oneLevelLibraryCallExpander optPLXFile
  when optWriteExpandedXML $ do
    let expandedXMLFilename = optPLXFile ++ ".expanded.xml"
    writeFile def expandedXMLFilename standalonePlan
  let cursor = fromDocument standalonePlan
  let output = render $ elementVisitor cursor
  putStrLn output
