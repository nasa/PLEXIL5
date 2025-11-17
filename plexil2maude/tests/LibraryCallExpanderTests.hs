{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}
{-# LANGUAGE ScopedTypeVariables #-}

module LibraryCallExpanderTests where

import LibraryCallExpander (oneLevelLibraryCallExpander)

import Control.Monad.Except
import qualified Data.Text as T
import qualified Data.Text.IO as TIO
import Data.Either
import Data.Map (empty)
import Prelude hiding ((<>),writeFile)

import Test.Tasty
import Test.Tasty.HUnit
import Text.RawString.QQ
import Text.PrettyPrint (text)


import PLEXILScript
import PSX2Maude.PrettyMaude

import Text.XML hiding (Name)

import System.Directory (getCurrentDirectory,setCurrentDirectory)
import System.FilePath ((</>),(<.>))
import System.IO.Temp (withSystemTempDirectory)
import System.Process (system)


testLibraryCallExpander :: TestTree
testLibraryCallExpander =
  testGroup "LibraryCallExpander tests"
    [ testCase "replace 1 library call w/o arguments" $ do
        let
            plan = ("libraryCall0", [r|<?xml version="1.0" encoding="UTF-8"?>
<PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" FileName="libraryCall0.ple">
    <GlobalDeclarations ColNo="0" LineNo="1">
        <LibraryNodeDeclaration ColNo="0" LineNo="1">
            <Name>theLibrary</Name>
        </LibraryNodeDeclaration>
    </GlobalDeclarations>
    <Node ColNo="0" LineNo="3" NodeType="LibraryNodeCall">
        <NodeId>libraryCall0</NodeId>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="5">
                <NodeId>theLibrary</NodeId>
            </LibraryNodeCall>
        </NodeBody>
    </Node>
</PlexilPlan>|])
            libs = [("theLibrary", [r|<?xml version="1.0" encoding="UTF-8"?>
<PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" FileName="theLibrary.ple">
    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>theLibrary</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
</PlexilPlan>|])]
            reference = parseText_ def
                [r|<?xml version="1.0" encoding="UTF-8"?>
<PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" FileName="libraryCall0.ple">
    <GlobalDeclarations ColNo="0" LineNo="1">
        <LibraryNodeDeclaration ColNo="0" LineNo="1">
            <Name>theLibrary</Name>
        </LibraryNodeDeclaration>
    </GlobalDeclarations>
    <Node ColNo="0" LineNo="3" NodeType="LibraryNodeCall">
        <NodeId>libraryCall0</NodeId>
        <NodeBody>
            <LibraryNodeCall ColNo="3" LineNo="5">
                <NodeId>theLibrary</NodeId>
            <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>theLibrary</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node></LibraryNodeCall>
        </NodeBody>
    </Node>
</PlexilPlan>|]

        actual <- expandPlanOnTempDirectory plan libs
        assertEqual "is different"
            (renderText def reference)
            (renderText def actual)
    , testCase "replace 2 library calls w/o arguments" $ do
        let
            plan = ("libraryCall6", [r|<?xml version="1.0" encoding="UTF-8"?>
<PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" FileName="libraryCall6.ple">
    <GlobalDeclarations ColNo="0" LineNo="1">
        <LibraryNodeDeclaration ColNo="0" LineNo="1">
            <Name>library1</Name>
        </LibraryNodeDeclaration>
        <LibraryNodeDeclaration ColNo="0" LineNo="2">
            <Name>library2</Name>
        </LibraryNodeDeclaration>
    </GlobalDeclarations>
    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="7">
        <NodeId>libraryCall6</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="9" NodeType="LibraryNodeCall">
                    <NodeId generated="1">LibraryCall__0</NodeId>
                    <NodeBody>
                        <LibraryNodeCall ColNo="3" LineNo="9">
                            <NodeId>library1</NodeId>
                        </LibraryNodeCall>
                    </NodeBody>
                </Node>
                <Node ColNo="3" LineNo="10" NodeType="LibraryNodeCall">
                    <NodeId generated="1">LibraryCall__1</NodeId>
                    <StartCondition>
                        <Finished>
                            <NodeRef dir="sibling">LibraryCall__0</NodeRef>
                        </Finished>
                    </StartCondition>
                    <NodeBody>
                        <LibraryNodeCall ColNo="3" LineNo="10">
                            <NodeId>library2</NodeId>
                        </LibraryNodeCall>
                    </NodeBody>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
</PlexilPlan>|])
            libs =
                [("library1", [r|<?xml version="1.0" encoding="UTF-8"?>
<PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" FileName="library1.ple">
    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>library1</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
</PlexilPlan>|])
                ,("library2", [r|<?xml version="1.0" encoding="UTF-8"?>
<PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" FileName="library2.ple">
    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>library2</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
</PlexilPlan>|])]
            reference = parseText_ def
                [r|<?xml version="1.0" encoding="UTF-8"?>
<PlexilPlan xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" FileName="libraryCall6.ple">
    <GlobalDeclarations ColNo="0" LineNo="1">
        <LibraryNodeDeclaration ColNo="0" LineNo="1">
            <Name>library1</Name>
        </LibraryNodeDeclaration>
        <LibraryNodeDeclaration ColNo="0" LineNo="2">
            <Name>library2</Name>
        </LibraryNodeDeclaration>
    </GlobalDeclarations>
    <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="7">
        <NodeId>libraryCall6</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="9" NodeType="LibraryNodeCall">
                    <NodeId generated="1">LibraryCall__0</NodeId>
                    <NodeBody>
                        <LibraryNodeCall ColNo="3" LineNo="9">
                            <NodeId>library1</NodeId>
                        <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>library1</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node></LibraryNodeCall>
                    </NodeBody>
                </Node>
                <Node ColNo="3" LineNo="10" NodeType="LibraryNodeCall">
                    <NodeId generated="1">LibraryCall__1</NodeId>
                    <StartCondition>
                        <Finished>
                            <NodeRef dir="sibling">LibraryCall__0</NodeRef>
                        </Finished>
                    </StartCondition>
                    <NodeBody>
                        <LibraryNodeCall ColNo="3" LineNo="10">
                            <NodeId>library2</NodeId>
                        <Node NodeType="NodeList" epx="Sequence" ColNo="0" LineNo="1">
        <NodeId>library2</NodeId>
        <InvariantCondition>
            <NoChildFailed>
                <NodeRef dir="self" />
            </NoChildFailed>
        </InvariantCondition>
        <NodeBody>
            <NodeList>
                <Node ColNo="3" LineNo="3" NodeType="Empty">
                    <NodeId>empty</NodeId>
                </Node>
            </NodeList>
        </NodeBody>
    </Node></LibraryNodeCall>
                    </NodeBody>
                </Node>
            </NodeList>
        </NodeBody>
    </Node>
</PlexilPlan>|]

        actual <- expandPlanOnTempDirectory plan libs
        assertEqual "is different"
            (renderText def reference)
            (renderText def actual)

    ]

expandPlanOnTempDirectory :: (String,T.Text) -> [(String,T.Text)] -> IO Document
expandPlanOnTempDirectory plan@(planName,planText) libs =
    withSystemTempDirectory "xml-library-call-expander" $ \tempDir -> do
        setCurrentDirectory tempDir
        planPath <- createPlanPLXFile tempDir plan
        createLibraryPLXFiles tempDir libs
        oneLevelLibraryCallExpander planPath
    where
        createPlanPLXFile dir (name,content) = TIO.writeFile path content >> return path
            where
                path = getPLXPath dir name

        createLibraryPLXFiles dir = mapM_ createPLXFile
            where
                createPLXFile (name,content) = TIO.writeFile path content
                    where
                        path = getPLXPath dir name

        getPLXPath dir name = dir </> addPLXExt name
            where
                addPLXExt = (<.> "plx")