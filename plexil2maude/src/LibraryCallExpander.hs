{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE OverloadedStrings #-}

module LibraryCallExpander
  ( oneLevelLibraryCallExpander,
    Document (..),
  )
where

import Data.Bifunctor (second)
import qualified Data.Text as T
import RecursiveUnfolder (recSingleUnfold)
import System.Directory (listDirectory)
import System.FilePath (takeBaseName, takeDirectory, takeExtension, (</>))
import Text.XML
import Text.XML.Cursor

oneLevelLibraryCallExpander :: String -> IO Document
oneLevelLibraryCallExpander filename = do
  file <- Text.XML.readFile def filename
  let directory = takeDirectory filename
  libFiles <- listPLXFilesNames directory
  libs <- mapM (\f -> Text.XML.readFile def (directory </> addPLX f) >>= \d -> return (f, d)) libFiles
  let libRoots = map (second extractRoot) libs
  let embeddedPlan = transformDocumentElement "main" file (recSingleUnfold libRoots [] replaceLibraryCallForOneLibrary getLibrariesCalled)
  return embeddedPlan

listPLXFilesNames :: FilePath -> IO [String]
listPLXFilesNames dir =
  do
    filepaths <- listFilesWithExtension dir ".plx"
    return $ map takeBaseName filepaths

listFilesWithExtension :: FilePath -> String -> IO [FilePath]
listFilesWithExtension dir ext = do
  contents <- listDirectory dir
  let potentialFiles = filter (\f -> takeExtension f == ext) contents
  return $ map (dir </>) potentialFiles

getLibrariesCalled :: (String, Element) -> [String]
getLibrariesCalled (_, el) = map T.unpack $ path cursor
  where
    cursor = fromNode $ NodeElement el
    path = descendant >=> element "LibraryNodeCall" >=> child >=> element "NodeId" >=> child >=> content

addPLX :: String -> String
addPLX = (++ ".plx")

extractRoot :: Document -> Element
extractRoot doc@Document {documentRoot} =
  case cursor $/ element "Node" &| node of
    (NodeElement el) : _ -> el
    _ -> error $ "[extractRoot] Root node not found in: " ++ show doc
  where
    cursor = fromNode $ NodeElement documentRoot

transformDocumentElement :: String -> Document -> ((String, Element) -> (String, Element)) -> Document
transformDocumentElement l Document {documentPrologue, documentRoot, documentEpilogue} f = Document {documentPrologue, documentRoot = documentRoot', documentEpilogue}
  where
    (_, documentRoot') = f (l, documentRoot)

replaceLibraryCallForOneLibrary :: (String, Element) -> Element -> Element
replaceLibraryCallForOneLibrary lib@(libName, libBody) el@Element {elementName, elementNodes} = el {elementNodes = elementNodes'}
  where
    replaceLibraryCallForOneLibrary' (NodeElement e) = NodeElement $ replaceLibraryCallForOneLibrary lib e
    replaceLibraryCallForOneLibrary' n = n

    elementNodes' =
      case elementName of
        "LibraryNodeCall"
          | nodeID == libName -> elementNodes ++ [NodeElement libBody]
          | otherwise -> elementNodes
          where
            cursor = fromNode $ NodeElement el
            nodeID = T.unpack $ T.concat $ cursor $/ element "NodeId" &// content
        _ -> map replaceLibraryCallForOneLibrary' elementNodes
