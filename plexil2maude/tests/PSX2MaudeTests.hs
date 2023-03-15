{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}
{-# LANGUAGE ScopedTypeVariables #-}

module PSX2MaudeTests where

import Control.Monad.Except
import Data.Either
import Prelude hiding ((<>))

import Test.Tasty
import Test.Tasty.HUnit
import Text.RawString.QQ
import Text.PrettyPrint (text)

import Text.XML.HXT.Core

import PLEXILScript
import PSX2Maude.PrettyMaude
import Text.PrettyPrint


testPSX2Maude :: TestTree
testPSX2Maude =
  testGroup "PSX2Maude tests"
    [ testCommand
    , testParameter
    , testCommandHandles
    , testPrettyPrint
    , testOptionalElements
    ]

testOptionalElements :: TestTree
testOptionalElements = testGroup "Optional XML Elements"
  [ testGroup "InitialState"
    [ [r||] `testItParsesAs` InitialState []
    ]
  ]
  where
    testItParsesAs :: String -> InitialState -> TestTree
    testItParsesAs str st = testCase (show st) $ str `parsesOnlyAs` st

testPrettyPrint :: TestTree
testPrettyPrint = testGroup "Pretty Printer"
  [ testGroup "Parameter"
    [ Parameter "10" PXReal `testPrettiesAs` "val(float(10))"
    , Parameter "10.0" PXReal `testPrettiesAs` "val(10.0)"
    , Parameter "11" PXInt  `testPrettiesAs` "val(11)"
    , Parameter "Valencia" PXString `testPrettiesAs` "val(\"Valencia\")"
    , Parameter "1" PXBool `testPrettiesAs` "val(True)"
    -- , Parameter "0 0 1" PXBoolArray `testPrettiesAs` "array(val(False) # val(False) # val(True))"
    ]
  , testGroup "CommandAck"
    [ CommandAck "c1" [Parameter "1" PXInt] CommandAccepted PXString
        `testPrettiesAs`
           "commandAck('c1,(val(1)),CommandAccepted)"
    , CommandAck "c2" [Parameter "1" PXReal ,Parameter "10" PXInt] CommandDenied PXInt
        `testPrettiesAs`
           "commandAck('c2,(val(float(1)) val(10)),CommandDenied)"
    ]
  , testGroup "Emptyness of Script or Initial State"
    [ Script [] `testPrettiesAs` "nilEInputsList"
    , InitialState [] `testPrettiesAs` "noExternalInputs"
    ]
  ]
  where
    testPrettiesAs :: forall a. (Pretty a,Show a) => a -> String -> TestTree
    testPrettiesAs p str =
      testCase (show p) $
        assertEqual "is not pretty printed as"
          (text str)
          (pretty p)


testCommand :: TestTree
testCommand = testGroup "Command"
  [ testGroup "from XML"
    [ [r|<Command name="get_bool" type="bool"><Result>1</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "get_bool"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVBool True)
                  , cmdType = PXBool
                  }
    , [r|<Command name="get_string" type="string"><Result>fred</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "get_string"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVString "fred")
                  , cmdType = PXString
                  }
    , [r|<Command name="get_int" type="int"><Result>0</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "get_int"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVInt 0)
                  , cmdType = PXInt
                  }
    , [r|<Command name="get_real" type="real"><Result>12.345</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "get_real"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVReal 12.345)
                  , cmdType = PXReal
                  }
    , [r|<Command name="boolArrayCommand" type="bool-array"><Result>1</Result><Result>1</Result><Result>1</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "boolArrayCommand"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVBoolArray [True,True,True])
                  , cmdType = PXBoolArray
                  }
    , [r|<Command name="get_string_aray" type="string-array"><Result>john</Result><Result>paul</Result><Result>george</Result><Result>ringo</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "get_string_aray"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVStringArray ["john","paul","george","ringo"])
                  , cmdType = PXStringArray
                  }
    , [r|<Command name="get_int_aray" type="int-array"><Result>1</Result><Result>2</Result><Result>3</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "get_int_aray"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVIntArray [1,2,3])
                  , cmdType = PXIntArray
                  }
    , [r|<Command name="get_real_aray" type="real-array"><Result>1.1</Result><Result>2.2</Result><Result>3.3</Result></Command>|]
        `testItParsesAs`
          Command { cmdName = "get_real_aray"
                  , cmdParams = []
                  , cmdResult = Result $ TypedValue (TVRealArray [1.1,2.2,3.3])
                  , cmdType = PXRealArray
                  }
  ]
  where
    testItParsesAs :: String -> Command -> TestTree
    testItParsesAs str cmd = testCase (show cmd) $ str `parsesOnlyAs` cmd

    testItPicklesAs :: Command -> String -> TestTree
    testItPicklesAs cmd str = testCase (show cmd) $ cmd `isPickledAs` str


testCommandHandles :: TestTree
testCommandHandles = testGroup "Command Handles"
  [ testGroup "from XML"
    [ "<Result>COMMAND_DENIED</Result>"         `testItParsesAs` CommandDenied
    , "<Result>COMMAND_ACCEPTED</Result>"       `testItParsesAs` CommandAccepted
    , "<Result>COMMAND_SENT_TO_SYSTEM</Result>" `testItParsesAs` CommandSentToSystem
    , "<Result>COMMAND_RCVD_BY_SYSTEM</Result>" `testItParsesAs` CommandReceivedBySystem
    , "<Result>COMMAND_SUCCESS</Result>"        `testItParsesAs` CommandSuccess
    , "<Result>COMMAND_FAILED</Result>"         `testItParsesAs` CommandFailed
    , testItDoesNotParse "<Result>COMMAND_denied</Result>"
    , testItDoesNotParse "<result>COMMAND_DENIED</Result>"
    , testItDoesNotParse "<Result>COMMAND_ACCEPTED</result>"
    , testItDoesNotParse "<Result>command_sent_to_system</Result>"
    , testItDoesNotParse "<Result>COMMAND</Result>"
    , testItDoesNotParse "<Result>SUCCESS</Result>"
    , testItDoesNotParse "<Result>FAILED</Result>"
    ]
  , testGroup "to XML"
    [ CommandDenied           `testItPicklesAs` "<Result>COMMAND_DENIED</Result>"
    , CommandAccepted         `testItPicklesAs` "<Result>COMMAND_ACCEPTED</Result>"
    , CommandSentToSystem     `testItPicklesAs` "<Result>COMMAND_SENT_TO_SYSTEM</Result>"
    , CommandReceivedBySystem `testItPicklesAs` "<Result>COMMAND_RCVD_BY_SYSTEM</Result>"
    , CommandSuccess          `testItPicklesAs` "<Result>COMMAND_SUCCESS</Result>"
    , CommandFailed           `testItPicklesAs` "<Result>COMMAND_FAILED</Result>"
    ]
  ]
  where
    anyCommandHandle :: CommandHandle
    anyCommandHandle = undefined

    testItParsesAs :: String -> CommandHandle -> TestTree
    testItParsesAs str cmdHandle = testCase (show cmdHandle) $ str `parsesOnlyAs` cmdHandle

    testItDoesNotParse :: String -> TestTree
    testItDoesNotParse str = testCase "It does not parse" $ str `doesNotParseAs` anyCommandHandle

    testItPicklesAs :: CommandHandle -> String -> TestTree
    testItPicklesAs cmdHandle str = testCase (show cmdHandle) $ cmdHandle `isPickledAs` str


parsesOnlyAs :: (XmlPickler a,Eq a,Show a) => String -> a -> Assertion
parsesOnlyAs str expectedData =
  do
    results <- runX ( readString [withErrors False] str >>> arr (unpickleDoc' xpickle) )
    case results of
      [Right  parsedData] -> assertEqual "Incorrectly parsed: " expectedData parsedData
      [Left errorMessage] -> assertFailure $ "Incorrectly parsed " ++ errorMessage
      []                  -> assertFailure $ "No parsing for " ++ show str
      _                   -> assertFailure $ "Many parses for " ++ show results


doesNotParseAs :: forall a. (XmlPickler a,Eq a,Show a) => String -> a -> Assertion
doesNotParseAs str _ =
  do
    results <- runX ( readString [withErrors no] str >>> arr (unpickleDoc' (xpickle :: PU a)) )
    case results of
      [Right  parsedData] -> assertFailure $
                                  "Incorrectly parsed " ++ show str
                                  ++ " as '" ++ show parsedData ++ "'"
      _                   -> return ()

isPickledAs :: (XmlPickler a,Eq a,Show a) => a -> String ->Assertion
isPickledAs a expectedStr = assertEqual "Incorrect serialization: " expectedStr pickledA
  where
    pickledA :: String
    pickledA = showPickled [] a