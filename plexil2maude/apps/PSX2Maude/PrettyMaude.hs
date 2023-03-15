{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE ScopedTypeVariables #-}

module PSX2Maude.PrettyMaude where

import PLEXILScript

import Data.List (intersperse)
import Data.OneOfN (OneOf3(..), OneOf4(..))
import Prelude hiding ((<>))
import Text.PrettyPrint

class Pretty a where
  pretty :: a -> Doc

newtype PLEXILScript2Maude = ToMaude { unToMaude :: PLEXILScript } deriving (Show,Eq)

instance Pretty PLEXILScript2Maude where
  pretty (ToMaude (PLEXILScript { initialState, script })) =
    vcat $
      [text "mod INPUT is"
      ,nest 2 $ vcat $
        [text "protecting PLEXILITE ."
        ,empty
        ,text "op input : -> ExternalInputGenerator ."
        ,text "eq input = sequenceGenerator("
        ,nest 2 $ vcat
          [pretty initialState
          ,text "#"
          ,pretty script]
        ,text ") ."
        ]
      ,text "endm"
      ]

instance Pretty InitialState where
  pretty (InitialState []) = text "noExternalInputs"
  pretty (InitialState xs) = vcat (map pretty xs)

instance Pretty Script where
  pretty (Script []) = text "nilEInputsList"
  pretty (Script xs) = vcat $ intersperse (char '#') $ map pretty xs

instance Pretty ScriptEntry where
  pretty (ScriptEntry e)
    | (OneOf4 st)    <- e = pretty st
    | (TwoOf4 ack)   <- e = pretty ack
    | (ThreeOf4 sim) <- e = pretty sim

instance Pretty State where
  pretty (State
    { stName
    , stParams
    , stValue
    , stType
    }) =
      text "stateLookup"
        <> parens (
          hcat $ punctuate comma
            [text "'" <> text stName
            ,text "nilarg"
            ,text "val" <> (parens $ text $ unValue $ head stValue)]
        )

instance Pretty Command where
  pretty (Command
    { cmdName
    , cmdParams
    , cmdResult
    , cmdType
    }) =
      text "Command"
        <> parens (
          hcat $ punctuate comma
            [text "'" <> text cmdName
            ,if null cmdParams then text "nilarg" else parens (hsep (map pretty cmdParams))
            ,pretty cmdResult]
        )

instance Pretty CommandAck where
  pretty (CommandAck
    { caName
    , caParams
    , caHandle
    , caType
    }) =
      text "commandAck"
        <> parens (
          hcat $ punctuate comma
            [text "'" <> text caName
            ,if null caParams then text "nilarg" else parens (hsep (map pretty caParams))
            ,pretty caHandle]
        )

instance Pretty Parameter where
  pretty Parameter{ parValue, parType } =
    case parType of
      PXReal -> prettyReal parValue
      PXInt  -> prettyValue parValue
      _ -> error $ "unimplemented pretty for parameters of type " ++ show parType
    where
      prettyReal valueStr =
        if isNumberWithDot valueStr
          then prettyValue valueStr
          else prettyValueWithCast "float" valueStr
          where
            isNumberWithDot :: String -> Bool
            isNumberWithDot valueStr =
              case reads valueStr of
                [(v :: Double,"")] -> '.' `elem` valueStr
                _                  -> error $ "Cannot parse as number: " ++ show valueStr

      prettyValue valueStr =
        text "val" <> parens (text parValue)

      prettyValueWithCast typeStr valueStr =
        text "val" <> parens (text typeStr <> parens (text parValue))


instance Pretty Simultaneous where
  pretty (Simultaneous es) = vcat $ map pretty es

instance Pretty SimultaneousEntry where
  pretty (SimultaneousEntry e)
    | OneState       st <- e = pretty st
    | OneCommandAck ack <- e = pretty ack

instance Pretty CommandHandle where
  pretty CommandDenied = text "CommandDenied"
  pretty CommandAccepted = text "CommandAccepted"
  pretty CommandSentToSystem = text "CommandSentToSystem"
  pretty CommandReceivedBySystem = text "CommandReceivedBySystem"
  pretty CommandSuccess = text "CommandSuccess"
  pretty CommandFailed = text "CommandFailed"
  pretty x = text $ show x

instance Pretty Value where
  pretty (Value str) = text str
  pretty (TypedValue (TVBoolArray bs)) = text "array" <+> parens values
    where
      values = hcat $ punctuate (text " # ") $
        map
          (\b -> if b
            then text "val(true)"
            else text "val(false)")
          bs
  
instance Pretty Result where
  pretty = pretty . unResult