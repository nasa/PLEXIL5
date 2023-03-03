{-# LANGUAGE OverloadedStrings #-}
module MainTestSuite where

import Control.Applicative
import Data.Either
import Data.Maybe
import Data.String (fromString)
import Data.Text (Text) --  hiding (map)
import Debug.Trace
import qualified Data.Text as T --  hiding (map)
import Prelude -- hiding (concat)

-- import Debug.Trace
-- import Data.Text.Internal.Lazy
import Test.Tasty
import Test.Tasty.HUnit
import Text.PrettyPrint
import qualified Text.PrettyPrint as PP
import Text.XML
import Text.XML.Cursor

import NodeStateTests

main :: IO ()
main = defaultMain tests

tests :: TestTree
tests =
    testGroup "Tests"
        [testsParseBooleanExpression
        ,testsParseNodeStateValue
        ,testsParseNodeStateVariable
        ,testsParseNodeState
        ]


-- TODO: Use prettydoc for text generation
parseBooleanExpression :: Cursor -> Maybe Text
parseBooleanExpression cursor =
  case node cursor of
    NodeElement el ->
      case T.unpack $ nameLocalName $ elementName el of
        "BooleanValue" ->
            nestUnder "const" $ nestUnder "val" $ concatContent $ descendant cursor
        "BooleanVariable" ->
            nestUnder "var" $ T.append "'" <$> concatContent (descendant cursor)
        "IsKnown" ->
            nestChildrenL "isKnown" cursor
        "OR" ->
            recNestChildrenL "_or_" cursor
        "AND" ->
            recNestChildrenL "_and_" cursor
        "XOR" ->
            recNestChildrenL "_xor_" cursor
        "NOT" ->
            nestUnder "not_" $ recNestChildrenL "not_" cursor
        "EQBoolean" ->
            recNestChildrenL "_equ_" cursor
        "EQNumeric" ->
            recNestChildrenNumericL "_equ_" cursor

        _ -> fail "non-exhaustive pattern"
    other -> trace ("Hi there!" ++ show other) undefined
--  where
--      nestChildrenL funName cursor' =
--          do arg <- T.intercalate "," <$> mapM parseBooleanExpression (child cursor')
--             return $ T.concat [funName,"(",arg,")"]
--      recNestChildrenL funName cursor' = (fmap $ foldl1 (\acc x -> T.concat [funName, "(", acc, ",", x, ")"]))
--        (mapM parseBooleanExpression $ child cursor')
--      recNestChildrenNumericL funName cursor' = (fmap $ foldl1 (\acc x -> T.concat [funName, "(", acc, ",", x, ")"]))
--        (mapM parseNumericExpression $ child cursor')

nestChildrenL funName cursor' =
    do arg <- T.intercalate "," <$> mapM parseBooleanExpression (child cursor')
       return $ T.concat [funName,"(",arg,")"]
recNestChildrenL funName cursor' = (fmap $ foldl1 (\acc x -> T.concat [funName, "(", acc, ",", x, ")"]))
  (mapM parseBooleanExpression $ child cursor')
recNestChildrenNumericL funName cursor' = (fmap $ foldl1 (\acc x -> T.concat [funName, "(", acc, ",", x, ")"]))
  (mapM parseNumericExpression $ child cursor')

concatContent cursors = return $ T.concat $ concatMap content cursors
nestUnder funName arg = arg >>= \x -> return (T.concat [funName,"(",x,")"])

parseNumericExpression :: Cursor -> Maybe Text
parseNumericExpression cursor =
  case node cursor of
    NodeElement el ->
      case T.unpack $ nameLocalName $ elementName el of
        "FINISHED" -> return "Finished"
        x -> fail x
    other -> trace ("Hi there!" ++ show other) undefined

parseGeneralizedNumericExpression :: Cursor -> Maybe Text
parseGeneralizedNumericExpression = parseNumericExpression

testsParseBooleanExpression :: TestTree
testsParseBooleanExpression =
  testGroup
    "parseBooleanExpression" $
        map (testify parseBooleanExpression)
            [("BooleanValue true",
              "<BooleanValue>true</BooleanValue>"
              ,"const(val(true))")
            ,("BooleanValue false",
              "<BooleanValue>false</BooleanValue>",
              "const(val(false))")
            ,("BooleanVariable",
              "<BooleanVariable>myvar</BooleanVariable>","var('myvar)")
            ,("KnownTest IsKnown",
              "<IsKnown><BooleanValue>true</BooleanValue></IsKnown>",
              "isKnown(const(val(true)))")
            ,("LogicalOperator OR",
              "<OR><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue><BooleanValue>true</BooleanValue></OR>",
              "_or_(_or_(const(val(true)),const(val(false))),const(val(true)))")
            ,("LogicalOperator AND",
              "<AND><BooleanValue>true</BooleanValue><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></AND>",
              "_and_(_and_(const(val(true)),const(val(true))),const(val(false)))")
            ,("LogicalOperator AND",
              "<AND><BooleanValue>true</BooleanValue><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></AND>",
              "_and_(_and_(const(val(true)),const(val(true))),const(val(false)))")
            ,("LogicalOperator XOR",
              "<XOR><BooleanValue>true</BooleanValue><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></XOR>",
              "_xor_(_xor_(const(val(true)),const(val(true))),const(val(false)))")
            ,("LogicalOperator NOT",
              "<NOT><BooleanValue>false</BooleanValue></NOT>",
              "not_(const(val(false)))")
            ,("Equality EQBoolean",
              "<EQBoolean><BooleanValue>true</BooleanValue><BooleanValue>false</BooleanValue></EQBoolean>",
              "_equ_(const(val(true)),const(val(false)))")
            ,("Equality EQNumeric",
              "<EQNumeric><IntegerValue>3</IntegerValue><IntegerValue>4</IntegerValue></EQNumeric>",
              "_equ_(const(val(3)),const(val(4)))")
            ,("Equality EQInternal",
              "<EQNumeric><IntegerValue>3</IntegerValue><IntegerValue>4</IntegerValue></EQNumeric>",
              "_equ_(const(val(3)),const(val(4)))")
            ]

testify f (msg,i,o) = testCase msg $ test f i @?= Just o

-- test f str = f cursor
--     where doc = parseText_ def $ fromString str
--           cursor = fromDocument doc
--
visit :: Cursor -> Doc
visit cursor = showNode cursor <> (hcat $ map visit $ concatMap anyElement $ child cursor)

showNode :: Cursor -> Doc
showNode cursor =
    case node cursor of
        NodeElement el -> text $ T.unpack $ nameLocalName $ elementName el
        _ -> ""

elementVisitor :: Cursor -> Doc
elementVisitor cursor = visit -- $+$ (nest 4 $ vcat childDocs)
    where
        childElements = concatMap anyElement $ child cursor
        childDocs = map elementVisitor childElements
        visit =
            case node cursor of
                NodeElement el -> prettyElement el childDocs

                _ -> text "Non-element"


prettyElement :: Element -> [Doc] -> Doc
prettyElement el children =
    let name = T.unpack $ nameLocalName $ elementName el
        cursor = fromNode $ NodeElement el
    in
        case name of
            "BooleanVariable" ->
                text "var" <> parens (
                    char '\'' <>
                        text ( T.unpack $ T.concat $ concatMap (map T.toLower . content) $ child cursor )
                )
            "IntegerValue" ->
                text "const" <> parens (
                    text "val" <> parens (
                        text $ T.unpack $ T.concat $ concatMap content $ child cursor
                    )
                )
            "BooleanValue" ->
                text "const" <> parens (
                    text "val" <> parens (
                        text $ T.unpack $ T.concat $ concatMap (map T.toLower . content) $ child cursor
                    )
                )
            "StringValue" ->
                text "const" <> parens (
                    text "val" <> parens (
                        doubleQuotes $ text ( T.unpack $ T.concat $ concatMap (map T.toLower . content) $ child cursor )
                    )
                )
            "EQBoolean"  -> text "_equ_" <> parens (hcat $ punctuate comma children)
            "EQNumeric"  -> text "_equ_" <> parens (hcat $ punctuate comma children)
            "EQInternal" -> text "_equ_" <> parens (hcat $ punctuate comma children)
            "NOT" -> text "not_"  <> parens (vcat $ map (nest 2) ( punctuate comma children))
            "AND" -> text "_and_" <> parens (vcat $ map (nest 2) ( punctuate comma children))
            "OR" -> text "_or_"   <> parens (vcat $ map (nest 2) ( punctuate comma children))
            "XOR" -> text "_xor_" <> parens (vcat $ map (nest 2) ( punctuate comma children))
            "IsKnown" -> text "isKnown" <> parens (hcat ( punctuate comma children))

            "NodeStateValue" -> prettyNodeStateValue cursor
            "NodeOutcomeVariable" -> prettyNodeOutcomeVariable cursor

            "GlobalDeclarations" -> PP.empty -- TODO: For the moment we ignore declarations

            "PlexilPlan" -> -- Header of the plan
                text ( unlines ["mod " ++ case getNodeId rootNode of
                                            Left msg -> trace msg "UNKNOWN"
                                            Right id -> id
                                       ++ "-PLAN is",
                                "",
                                "protecting PLEXILITE-PREDS .",
                                ""
                               ] ) $+$ vcat children
                    where
                        rootNode = head $ (child >=> checkName (=="Node")) cursor

            _ -> text name $+$ if null children then Text.PrettyPrint.empty else ( (vcat $ map (nest 2) (punctuate comma children)))

getNodeId :: Cursor -> Either String String
getNodeId cursor =
    let nodeIdAxis = (element "Node" >=> child >=> element "NodeId") cursor
    in
        case nodeIdAxis of
            [idNode] -> Right $ T.unpack $ head $ (child >=> content) idNode
            _        -> Left  $ "Wrong NodeId structure:" ++ show cursor
