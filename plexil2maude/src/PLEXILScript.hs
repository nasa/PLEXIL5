{-# LANGUAGE FlexibleInstances #-}

module PLEXILScript where

import Text.XML.HXT.Core
import Data.Maybe (fromJust)
import Data.Either (partitionEithers)
import Data.Either.Combinators (maybeToRight)
import Data.OneOfN (OneOf3(..), OneOf5(..))

data PLEXILScript = PLEXILScript
  { initialState :: InitialState
  , script       :: Script
  } deriving (Show,Eq)

instance XmlPickler PLEXILScript where
  xpickle = xpPLEXILScript

xpPLEXILScript :: PU PLEXILScript
xpPLEXILScript =
  xpElem "PLEXILScript" $
  xpWrap (uncurry PLEXILScript,\x -> (initialState x,script x)) $
  xpPair xpickle xpickle


newtype InitialState = InitialState [State] deriving (Show,Eq)

instance XmlPickler InitialState where
  xpickle = xpInitialState

xpInitialState :: PU InitialState
xpInitialState =
  xpDefault (InitialState []) $
  xpElem "InitialState" $
  xpWrap (InitialState,\(InitialState x) -> x) $
  xpList xpickle

newtype Script = Script [ScriptEntry] deriving (Show,Eq)

newtype ScriptEntry = ScriptEntry
  { unScriptEntry :: (OneOf5 State Command CommandAck Simultaneous UpdateAck)
  } deriving (Show,Eq)

instance XmlPickler ScriptEntry where
  xpickle = xpWrap (ScriptEntry,unScriptEntry) $ xpAlt tag ps
    where
      tag (OneOf5   _) = 0
      tag (TwoOf5   _) = 1
      tag (ThreeOf5 _) = 2
      tag (FourOf5  _) = 3
      tag (FiveOf5  _) = 4

      ps
        = [ xpWrap (OneOf5,  \(OneOf5 x)   -> x) $ xpickle
          , xpWrap (TwoOf5,  \(TwoOf5 x)   -> x) $ xpickle
          , xpWrap (ThreeOf5,\(ThreeOf5 x) -> x) $ xpickle
          , xpWrap (FourOf5, \(FourOf5 x)  -> x) $ xpickle
          , xpWrap (FiveOf5, \(FiveOf5 x)  -> x) $ xpickle ]

instance XmlPickler Script where
  xpickle = xpScript

xpScript :: PU Script
xpScript =
  xpElem "Script" $
  xpWrap (Script,\(Script xs) -> xs) $
  xpList xpickle

newtype Simultaneous = Simultaneous [SimultaneousEntry] deriving (Show,Eq)

newtype SimultaneousEntry = SimultaneousEntry { unSimultaneousEntry :: OneOf } deriving (Show,Eq)

instance XmlPickler Simultaneous where
  xpickle = xpSimultaneous

xpSimultaneous :: PU Simultaneous
xpSimultaneous =
  xpElem "Simultaneous" $
  xpWrap (Simultaneous,\(Simultaneous x) -> x) $
  xpList xpickle

instance XmlPickler SimultaneousEntry where
  xpickle = xpWrap (SimultaneousEntry,unSimultaneousEntry) xpickle

data OneOf
  = OneState State
  | OneCommandAck CommandAck
  deriving (Show,Eq)

instance XmlPickler OneOf where
  xpickle = xpOneOf

xpOneOf :: PU OneOf
xpOneOf =
  xpAlt tag ps
  where
    tag (OneState _) = 0
    tag (OneCommandAck _) = 1

    ps =
      [ xpWrap (OneState,      \(OneState s)      -> s) $ xpickle
      , xpWrap (OneCommandAck, \(OneCommandAck a) -> a) $ xpickle ]

data Command = Command
  { cmdName   :: String
  , cmdParams :: [Parameter]
  , cmdResult :: Result
  , cmdType   :: Type
  } deriving (Show,Eq)

instance XmlPickler Command where
  xpickle = xpCommand

xpCommand :: PU Command
xpCommand =
  xpElem "Command" $
  xpWrap
    ( \(n,phs,t) -> let (ps,r:_) = partitionEithers phs in toCommand n phs t
    , \a -> (cmdName a, Right (cmdResult a):map Left (cmdParams a), cmdType a)) $
  xpTriple
    (xpAttr "name" xpText)
    (xpWrap (projectOneTwo, projectLeftRight) $ xpList $ xpAlt tag ps)
    (xpAttr "type" xpickle)
  where
    projectOneTwo [] = []
    projectOneTwo ((OneOf3 x):xs) = Left x : projectOneTwo xs
    projectOneTwo ((TwoOf3 x):xs) = Right x : projectOneTwo xs
    projectOneTwo (_:xs) = projectOneTwo xs

    projectLeftRight [] = []
    projectLeftRight (Left x:xs) = OneOf3 x : projectLeftRight xs
    projectLeftRight (Right x:xs) = TwoOf3 x : projectLeftRight xs
    projectLeftRight (_:xs) = projectLeftRight xs

    tag (OneOf3 _) = 0
    tag (TwoOf3 _) = 1
    tag (ThreeOf3 _) = 2

    ps =
      [ xpWrap (OneOf3, \(OneOf3 params) -> params) $ xpickle
      , xpWrap (TwoOf3, \(TwoOf3 result) -> result) $ xpickle
      , xpWrap (ThreeOf3, \(ThreeOf3 text) -> text) $ xpText]

    toCommand name paramsOrResult typ = Command name params result typ
      where
        (params,untypedResult:_) = partitionEithers paramsOrResult
        result = Result $ case unResult untypedResult of
          Values strs ->
            case typ of
              PXBool -> case strs of
                [str] -> TypedValue $ TVBool (strToBool str)
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommand`"
              PXInt -> case strs of
                [str] -> TypedValue $ TVInt (read str)
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommand`"
              PXReal -> case strs of
                [str] -> TypedValue $ TVReal (read str)
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommand`"
              PXString -> case strs of
                [str] -> TypedValue $ TVString $ read $ ("\"") ++ str ++ "\""
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommand`"
              PXBoolArray   -> TypedValue $ TVBoolArray (map strToBool strs)
              PXStringArray -> TypedValue $ TVStringArray strs
              PXIntArray    -> TypedValue $ TVIntArray (map read strs)
              PXRealArray   -> TypedValue $ TVRealArray (map read strs)
          anotherValue -> error $ "Unsupported value: " ++ show anotherValue ++ " in `toCommand`"

        strToBool str =
          case str of
            "1" -> True
            "0" -> False
            str -> error $ "Impossible to parse: '" ++ str
              ++ "' as a PXBool from `toCommand "
              ++ show name ++ " "
              ++ show paramsOrResult ++ " "
              ++ show typ ++ "`"



data Result = Result { unResult :: Value } deriving (Show,Eq)

instance XmlPickler Result where
  xpickle = xpWrap ( Result . Values , unValues . unResult ) $
    xpList1 $ xpElem "Result" $ xpDefault "" $ xpWrap ( id , id ) xpText


data CommandAck = CommandAck
  { caName   :: String
  , caParams :: [Parameter]
  , caHandle :: CommandHandle
  , caType   :: Type
  } deriving (Show,Eq)

instance XmlPickler CommandAck where
  xpickle = xpCommandAck

xpCommandAck :: PU CommandAck
xpCommandAck =
  xpElem "CommandAck" $
  xpWrap
    ( \(n,phs,t) -> let (ps,h:_) = partitionEithers phs in CommandAck n ps h t
    , \a -> (caName a, Right (caHandle a):map Left (caParams a), caType a)) $
  xpTriple
    (xpAttr "name" xpText)
    (xpList xpickle)
    (xpAttr "type" xpickle)

data CommandHandle
  = CommandDenied
  | CommandAccepted
  | CommandSentToSystem
  | CommandReceivedBySystem
  | CommandSuccess
  | CommandFailed deriving (Show,Eq)

instance XmlPickler CommandHandle where
  xpickle = xpCommandHandle

xpCommandHandle :: PU CommandHandle
xpCommandHandle = xpElem "Result" $
  xpWrapEither
  ( xmlToCommandHandle
  , commandHandleToXML
  ) xpText
  where
    xmlToCommandHandle :: String -> Either String CommandHandle
    xmlToCommandHandle xml = maybeToRight msg $ lookup xml xmlMap
      where msg = "unknown CommandHandle: " ++ show xml

    commandHandleToXML = fromJust . flip lookup (map swap xmlMap)

    xmlMap = [("COMMAND_DENIED",CommandDenied)
             ,("COMMAND_ACCEPTED",CommandAccepted)
             ,("COMMAND_SENT_TO_SYSTEM",CommandSentToSystem)
             ,("COMMAND_RCVD_BY_SYSTEM",CommandReceivedBySystem)
             ,("COMMAND_SUCCESS",CommandSuccess)
             ,("COMMAND_FAILED",CommandFailed)]

data CommandAbort = CommandAbort
  { cabName   :: String
  , cabParams :: [Parameter]
  , cabResult :: Result
  , cabType   :: Type
  } deriving (Show,Eq)

instance XmlPickler CommandAbort where
  xpickle = xpCommandAbort

xpCommandAbort :: PU CommandAbort
xpCommandAbort =
  xpElem "CommandAbort" $
  xpWrap
    ( \(n,phs,t) -> let (ps,r:_) = partitionEithers phs in toCommandAbort n phs t
    , \a -> (cabName a, Right (cabResult a):map Left (cabParams a), cabType a)) $
  xpTriple
    (xpAttr "name" xpText)
    (xpList xpickle)
    (xpAttr "type" xpickle)
  where
    toCommandAbort name paramsOrResult typ = CommandAbort name params result typ
      where
        (params,untypedResult:_) = partitionEithers paramsOrResult
        result = Result $ case unResult untypedResult of
          Values strs ->
            case typ of
              PXBool -> case strs of
                [str] -> TypedValue $ TVBool (strToBool str)
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommandAbort`"
              PXInt -> case strs of
                [str] -> TypedValue $ TVInt (read str)
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommandAbort`"
              PXReal -> case strs of
                [str] -> TypedValue $ TVReal (read str)
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommandAbort`"
              PXString -> case strs of
                [str] -> TypedValue $ TVString str
                _ -> error $ "Wrong number of values: " ++ show strs ++ " in `toCommandAbort`"
              PXBoolArray   -> TypedValue $ TVBoolArray (map strToBool strs)
              PXStringArray -> TypedValue $ TVStringArray strs
              PXIntArray    -> TypedValue $ TVIntArray (map read strs)
              PXRealArray   -> TypedValue $ TVRealArray (map read strs)
          anotherValue -> error $ "Unsupported value: " ++ show anotherValue ++ " in `toCommandAbort`"

        strToBool str =
          case str of
            "1" -> True
            "0" -> False
            str -> error $ "Impossible to parse: '" ++ str
              ++ "' as a PXBool from `toCommandAbort "
              ++ show name ++ " "
              ++ show paramsOrResult ++ " "
              ++ show typ ++ "`"

data State = State
  { stName   :: String
  , stParams :: [Parameter]
  , stValue  :: [Value]
  , stType   :: Type
  } deriving (Show,Eq)

instance XmlPickler State where
  xpickle = xpState


xpState :: PU State
xpState
  = xpElem "State" $
    xpWrap
      ( \(n,pvs,t) -> let (ps,vs) = partitionEithers pvs in State n ps vs t
      , \st -> (stName st, map Left (stParams st) ++ map Right (stValue st), stType st)) $
    xpTriple
      (xpAttr "name" xpText)
      (xpList xpickle)
      (xpAttr "type" xpickle)

data UpdateAck = UpdateAck
  { uaName   :: String
  } deriving (Show,Eq)

instance XmlPickler UpdateAck where
  xpickle = xpUpdateAck

xpUpdateAck :: PU UpdateAck
xpUpdateAck
  = xpElem "UpdateAck" $
    xpWrap
      ( UpdateAck
      , \ua -> uaName ua) $
    xpAttr "name" xpText

data Parameter = Parameter
  { parValue :: String
  , parType  :: Type
  } deriving (Show,Eq)

instance XmlPickler Parameter where
  xpickle = xpParameter

xpParameter :: PU Parameter
xpParameter
  = xpElem "Param" $
    xpWrap
      ( uncurry Parameter
      , \p -> (parValue p, parType p)) $
    xpPair
      xpText0
      (xpAttr "type" xpickle)

data Value =
  Value { unValue :: String }
  | Values { unValues :: [String] }
  | TypedValue { unTypedValue :: TypedValue } deriving (Show,Eq)

data TypedValue = TVString String
                | TVInt Int
                | TVReal Double
                | TVBool Bool
                | TVStringArray [String]
                | TVIntArray [Int]
                | TVRealArray [Double]
                | TVBoolArray [Bool]
                deriving (Show,Eq)

instance XmlPickler Value where
  xpickle = xpElem "Value" $ xpWrap ( Value, unValue ) xpText


data Type = PXBool
          | PXReal
          | PXInt
          | PXString
          | PXBoolArray
          | PXRealArray
          | PXIntArray
          | PXStringArray
          deriving (Eq,Show,Read)

instance XmlPickler Type where
  xpickle = xpType

xpType :: PU Type
xpType =
  xpWrap
  ( fromJust . flip lookup xmlMap
  , fromJust . flip lookup (map swap xmlMap)
  ) xpText
  where
    xmlMap = [("bool",PXBool)
             ,("real",PXReal)
             ,("int",PXInt)
             ,("string",PXString)
             ,("bool-array",PXBoolArray)
             ,("real-array",PXRealArray)
             ,("int-array",PXIntArray)
             ,("string-array",PXStringArray)]