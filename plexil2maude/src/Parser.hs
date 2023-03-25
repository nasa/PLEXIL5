{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE TupleSections #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes #-}

module Parser where

import Control.Applicative
import Control.Monad.Except
import Data.Char
import Data.Either
import Data.Maybe
import Data.List (intersperse, isInfixOf)
import qualified Data.Map as M
import Data.Text (Text) --  hiding (map)
import Debug.Trace
import qualified Data.Text as T --  hiding (map)
import Prelude hiding ((<>)) -- (concat)
import Text.PrettyPrint
import qualified Text.PrettyPrint as PP
import Text.XML hiding (Name)
import qualified Text.XML as XML
import Text.XML.Cursor

type TextOrError = Either String Text
------------------------------------------------------------
---------- NodeState

parseNodeStateValue :: Cursor -> Either String Text
parseNodeStateValue cursor =
    let axis  = checkName (=="NodeStateValue") >=> child >=> content
        state = T.concat $ axis cursor
    in case state of
        "WAITING"         -> return "waiting"
        "EXECUTING"       -> return "executing"
        "FINISHING"       -> return "finishing"
        "FAILING"         -> return "failing"
        "ITERATION_ENDED" -> return "iterationEnded"
        "FINISHED"        -> return "finished"
        "INACTIVE"        -> return "inactive"
        x -> Left $ "unknown NodeStateValue " ++ show x

parseNodeStateVariable :: Cursor -> TextOrError
parseNodeStateVariable cursor =
    let axis  = checkName (=="NodeStateVariable")
                    >=> child
                    >=> checkName (=="NodeRef")
                    >=> child
                    >=> content
        nodes = axis cursor
        identifier = T.concat nodes
    in if null nodes
         then Left "No NodeStateVariable found"
         else return $ T.pack $ maudifyLabel $ T.unpack identifier

parseNodeState :: Cursor -> TextOrError
parseNodeState cursor = parseNodeStateVariable cursor <|> parseNodeStateValue cursor

prettyNodeStateValue :: Cursor -> Doc
prettyNodeStateValue cursor =
    let axis  = checkName (=="NodeStateValue") >=> child >=> content
        state = T.concat $ axis cursor
    in text $ case state of
        "WAITING"         -> "waiting"
        "EXECUTING"       -> "executing"
        "FINISHING"       -> "finishing"
        "FAILING"         -> "failing"
        "ITERATION_ENDED" -> "iterationEnded"
        "FINISHED"        -> "finished"
        "INACTIVE"        -> "inactive"
        _ -> "unknown NodeStateValue"

t = T.concat

parseFinishedPredicate :: Cursor -> ParseError String
parseFinishedPredicate cursor = return $ "(isFinished?(" ++ dirAttrText ++ "(" ++ (maudifyLabel $ toQID refText) ++ ")))"
    where
        rootElem = checkName (flip elem ["Finished"])
        refElem = rootElem >=> child >=> checkName (== "NodeRef")
        dirAttrText = T.unpack $ t $ (refElem >=> attribute "dir") cursor
        refText = T.unpack $ t $ (refElem >=> child >=> content) cursor

    -- if null
    -- let axis  = checkName (flip elem ["Finished"])
    --                 >=> child
    --                 >=> checkName (=="NodeRef")
    --                 >=> child
    --                 >=> content
    --     nodes = axis cursor
    --     identifier = T.concat nodes
    -- in if null nodes
    --      then Left "No NodeOutcomeVariable found"
    --      else return $ render $ parens $ text "isFinished?" <> parens (text $ maudifyLabel $ toQID $ T.unpack identifier)

------------------------------------------------------------
---------- NodeOutcome

-- thisAxis :: Cursor -> [Text]
-- thisAxis =
--     checkName (=="NodeOutcomeVariable")
--                     >=> child
--                     >=> checkName (=="NodeRef")
--                     >=> child
--                     >=> content
parseNodeOutcomeVariable :: Cursor -> ParseError String
parseNodeOutcomeVariable cursor =
    let
        nodes = thisAxis cursor
        identifier = T.concat nodes
    in if null nodes
         then Left "No NodeOutcomeVariable found"
         else return $ maudifyLabel $ T.unpack identifier
    where
        thisAxis =
            checkName (=="NodeOutcomeVariable")
                            >=> child
                            >=> checkName (\n -> n =="NodeRef" || n =="NodeId")
                            >=> child
                            >=> content

prettyNodeOutcomeValue :: Cursor -> Doc
prettyNodeOutcomeValue cursor =
    let axis  = checkName (=="NodeStateValue") >=> child >=> content
        state = T.concat $ axis cursor
    in text $ case state of
        "WAITING"         -> "waiting"
        "EXECUTING"       -> "executing"
        "FINISHING"       -> "finishing"
        "FAILING"         -> "failing"
        "ITERATION_ENDED" -> "iterationEnded"
        "FINISHED"        -> "finished"
        "INACTIVE"        -> "inactive"
        _ -> "unknown NodeStateValue"


------------------------------------------------------------
---------- BooleanExpression

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
        "NENumeric" ->
            recNestChildrenNumericL "_nequ_" cursor

        _ -> fail "non-exhaustive pattern"
    other -> trace ("Hi there!" ++ show other) undefined

nestChildrenL :: Text -> Cursor -> Maybe Text
nestChildrenL funName cursor' =
    do arg <- T.intercalate "," <$> mapM parseBooleanExpression (child cursor')
       return $ T.concat [funName,"(",arg,")"]
recNestChildrenL :: Text -> Cursor -> Maybe Text
recNestChildrenL funName cursor' = (fmap $ foldl1 (\acc x -> T.concat [funName, "(", acc, ",", x, ")"]))
  (mapM parseBooleanExpression $ child cursor')
recNestChildrenNumericL :: Text -> Cursor -> Maybe Text
recNestChildrenNumericL funName cursor' = (fmap $ foldl1 (\acc x -> T.concat [funName, "(", acc, ",", x, ")"]))
  (mapM parseNumericExpression $ child cursor')

concatContent :: (Monad m, Foldable t) => t Cursor -> m Text
concatContent cursors = return $ T.concat $ concatMap content cursors

nestUnder :: Monad m => Text -> m Text -> m Text
nestUnder funName arg = arg >>= \x -> return (T.concat [funName,"(",x,")"])

parseNumericExpression :: Cursor -> Maybe Text
parseNumericExpression cursor =
  case node cursor of
    NodeElement el ->
      case T.unpack $ nameLocalName $ elementName el of
        "FINISHED" -> return "finished"
        x -> fail x
    other -> trace ("Hi there!" ++ show other) undefined

parseGeneralizedNumericExpression :: Cursor -> Maybe Text
parseGeneralizedNumericExpression = parseNumericExpression


------------------------------------------------------------
---------- Equality

prettyEQInternal :: Cursor -> [Doc] -> ParseError Doc
prettyEQInternal thisParent children =
    let isOutcomeEq = not $ null $ (child >=> element "NodeOutcomeValue") thisParent
        isStateEq   = not $ null $ (child >=> element "NodeStateValue"  ) thisParent
        isFailureEq = not $ null $ (child >=> element "NodeFailureValue"  ) thisParent
        isCommandHandleEq = not $ null $ (child >=> element "NodeCommandHandleVariable"  ) thisParent
    in if isStateEq
         then prettyStateEq thisParent children
       else if isOutcomeEq
         then prettyOutcomeEq thisParent children
         else if isFailureEq
            then prettyFailureEq thisParent
            else if isCommandHandleEq
                then prettyCommandHandleEq thisParent children
                else return $ text "_equ_" <> parens (hcat $ punctuate comma children)

prettyFailureEq :: Cursor -> ParseError Doc
prettyFailureEq cursor =
    do var   <- fstMatch parseNodeFailureVariable children
       value <- fstMatch parseNodeFailureValue children
       return $ text "isFailure?" <> parens (text var <> comma <> text value)

  where children = child cursor

        parseNodeFailureValue :: Cursor -> ParseError String
        parseNodeFailureValue c =
            do _ <- checkThisElement "NodeFailureValue" c
               value <- getUniqueTextContent' c
               case value of
                   "POST_CONDITION_FAILED" -> return "postconditionFailed"
                   "PRE_CONDITION_FAILED" -> return "preconditionFailed"
                   "INVARIANT_CONDITION_FAILED" -> return "invariantFailed"
                   "PARENT_FAILED" -> return "parentFailed"
                   "PARENT_EXITED" -> return "parentExited"
                   "EXITED" -> return "exited"
                   _ -> Left "unknownNodeFailureValue"

        parseNodeFailureVariable :: Cursor -> ParseError String
        parseNodeFailureVariable cursor =
            do checkThisElement "NodeFailureVariable" cursor
               parseNodeReference cursor

prettyOutcomeEq :: Cursor -> [Doc] -> ParseError Doc
prettyOutcomeEq cursor children =
    let outcomeVariable = getNodeOutcomeVariable cursor
        outcomeValue = getNodeOutcomeValue cursor
        maudeOutcomeValue =
            case outcomeValue of
                "FAILURE"     -> "failure"
                "SUCCESS"     -> "success"
                "SKIPPED"     -> "skipped"
                "INTERRUPTED" -> "interrupted"
                _ -> "(Error: don't know how to parse: " ++ outcomeValue ++ ")"
    in return $
        if outcomeValue == "FAILURE"
          then text "isOutcomeFailure?" <> parens outcomeVariable
          else text "isOutcome?" <> parens (outcomeVariable <> comma <> text maudeOutcomeValue)


prettyStateEq :: Cursor -> [Doc] -> ParseError Doc
prettyStateEq parent children =
    do
        var <- text <$> getNodeStateVariable parent
        let state = getNodeStateValue parent
        return $ text "isStatus?" <> (parens $ hcat $ punctuate comma [var,text state])

prettyCommandHandleEq :: Cursor -> [Doc] -> ParseError Doc
prettyCommandHandleEq parent children =
    do
        var <- text <$> getNodeCommandHandleVariable parent
        let state = getNodeCommandHandleValue parent
        return $ text "cmdHandleIs?" <> (parens $ hcat $ punctuate comma [var,text state])


--
-- TODO: Get full path ID for identifiers
--

------------------------------------------------------------
---------- Visitor

visit :: Cursor -> Doc
visit cursor = showNode cursor <> (hcat $ map visit $ concatMap anyElement $ child cursor)

showNode :: Cursor -> Doc
showNode cursor =
    case node cursor of
        NodeElement el -> text $ T.unpack $ nameLocalName $ elementName el
        _ -> ""

errorize :: ParseError Doc -> Doc
errorize = either ((text "ERROR: "<>) . text) id

errorize' :: String -> ParseError Doc -> Doc
errorize' str = either ((text ("ERROR: " ++ str)<>) . text) id

isCommand cursor =
    case node cursor of
        NodeElement el -> (nameLocalName $ elementName el) == "Command"
        _ -> False

isRHS :: Cursor -> Bool
isRHS cursor =
    case node cursor of
        NodeElement el -> "RHS" `T.isSuffixOf` (nameLocalName $ elementName el)
        _ -> False

parseDeclaredVariable = parseVariableId


-- x :: (Cursor -> ParseError a) -> [Cursor] -> ParseError a
-- x f cursors = foldM (<|>) (Left "Not found") cursors

parseCommand cursor = parseCommandWithAssignment cursor <|> parseCommand' cursor

parseCommand' cursor =
    do cmdName <- msum $ map parseNameFromStringValue elementChildren
       arguments <- parseOptionalArguments cursor
       return $ trace ("parseCommand: arguments = " ++ show arguments) ()
       return $ parens $ (parens (text cmdName) <+> text "/" <+> arguments)
    where
        elementChildren = (child >=> anyElement) cursor

parseCommandWithAssignment cursor =
    do var <- msum $ map parseDeclaredVariable children
       cmdName <- msum $ map parseNameFromStringValue elementChildren
       arguments <- parseOptionalArguments cursor
       return $ parens $ (parens (text cmdName) <+> text "/" <+> arguments <+> text "/" <+> parens (text var))

    where
        children = child cursor
        elementChildren = (child >=> anyElement) cursor
        (name:children') = map elementVisitor $ (child >=> anyElement) cursor

parseNode :: Cursor -> [Doc] -> ParseError Doc
parseNode cursor _ =
    case nodeType of
        Right label -> Right $ prettyNode (map toLower label)
        _ -> Left $ "Unsupported nodeType: " ++ show nodeType
    where
        nodeType = parseNodeType cursor
        children = (child >=> anyElement)
        children' = map elementVisitor $ children cursor

        declarations = (children >=> element "VariableDeclarations") cursor

        conditions = (children >=> checkName isCondition) cursor
        body:_ = (children >=> element "NodeBody") cursor

        isCondition :: XML.Name -> Bool
        isCondition name = "Condition" `T.isInfixOf` (nameLocalName name)

        nodeIdQID = toQID <$> maudifyLabel <$> parseNodeId cursor

        prettyNode maudeLabel = do
            case (children >=> element "NodeBody") cursor of
                (body:_) ->
                    (text maudeLabel <> (parens $
                        hcat $ punctuate comma (
                            [(errorize $ fmap text $ nodeIdQID)]
                            ++ [if null declarations
                                  then text "nilocdecl"
                                  else (hsep $ map elementVisitor declarations)]
                            ++ [parens (if null conditions
                                          then text "none"
                                          else hcat $ punctuate comma $ map elementVisitor conditions)]
                            ++ [elementVisitor body])
                                       )
                    )
                _ ->
                    (text maudeLabel <> (parens $
                        hcat $ punctuate comma (
                            [(errorize $ fmap text $ nodeIdQID)]
                            ++ [if null declarations
                                  then text "nilocdecl"
                                  else (hsep $ map elementVisitor declarations)]
                            ++ [parens (if null conditions
                                          then text "none"
                                          else hcat $ punctuate comma $ map elementVisitor conditions)]
                            )
                                       )
                    )

parseNodeType :: Cursor -> ParseError String
parseNodeType cursor =
    case attribute "NodeType" cursor of
        nodeType:_ -> Right $ maudifyNodeType $ T.unpack nodeType
        _ -> Left $ "Could not find NodeType attribute in: " ++ show cursor


parseSimpleAssignment :: Cursor -> ParseError Doc
parseSimpleAssignment cursor =
    do (var,expr) <- twoChildElements cursor
       varId <- ((fmap text $ parseVariable var) <|> parseArrayElement var)
       let expr' = fix prettyElementRHS expr
       return $ parens $ varId <+> text ":=" <+> expr'


data ArrayElement = ArrayElement Doc Doc

parseArrayElement :: Cursor -> ParseError Doc
parseArrayElement cursor =
  if length ((child >=> isArrayVariable) cursor) == 1
    then
      do (var,expr) <- twoChildElements cursor
         varId <- parseVariableId var
         let expr' = elementVisitor expr
         return $ "arrayVar" <> parens (text varId <+> "," <+> expr')
    else
      do (var,expr) <- twoChildElements cursor
         varId <- toQID <$> parseName var
         let expr' = elementVisitor expr
         return $ "arrayVar" <> parens (text varId <+> "," <+> expr')

parseArrayElement' :: Cursor -> ParseError Doc
parseArrayElement' cursor =
    do (var,expr) <- twoChildElements cursor
       varId <- parseVariableId var
       let expr' = elementVisitor expr
       return $ parens $ text "arrayVar" <> parens (text varId <+> comma <+> expr')


data Value = Value
newtype Name = Name String
newtype Type = Type String
newtype InitialValue = InitialValue Value
data DeclareVariable = DeclareVariable Name Type


parseDeclareVariable :: Cursor -> ParseError Doc
parseDeclareVariable cursor =
    parseVariableDeclaration cursor
                where
                    parseVariableDeclaration cursor =
                        do
                            (var,ty,init) <- (threeChildElements cursor >>= (\(var,ty,init) -> do { init' <- parseInitialSimpleValue init ; return (var,ty,init') } ))
                                <|> (twoChildElements cursor >>= (\(var,ty) -> do { init <- addInitialization (var,ty); return (var,ty,init) }))
                            v <- toQID <$> parseName var
                            return $ parens $ text v <+> colon <+> init
                            where
                                  buildValue value = pretty
                                    where pretty = text "val" <> parens value
                                  addInitialization (var,ty) =
                                      do tyStr <- getUniqueTextContentOrEmpty (:[]) ty
                                         case tyStr of
                                            "String" -> Right $ buildValue $ doubleQuotes PP.empty
                                            "Integer" -> Right $ buildValue $ text "0"
                                            "Real" -> Right $ buildValue $ text "0.0"
                                            "Boolean" -> Right $ buildValue $ text "false"
                                            _ -> Left $ "Don't know how to initialize type " ++ show tyStr ++ " at: " ++ show ty

-- parseCommand' cursor =
--     do cmdName <- msum $ map parseNameFromStringValue elementChildren
--        arguments <- parseOptionalArguments cursor
--        return $ parens $ (parens (text cmdName) <+> text "/" <+> arguments)
--     where
--         elementChildren = (child >=> anyElement) cursor


fstMatch :: (Cursor -> ParseError a) -> [Cursor] -> ParseError a
fstMatch f = msum . map f

hasArrayValue1Level :: Cursor -> Bool
hasArrayValue1Level cursor = res
    where
        res = length ((child >=> hasArrayValueAxis1Level) cursor) == 1
        -- NodeElement el ->
        --     let label = nameLocalName $ elementName el
        --     in label == "ArrayValue"
        -- _ -> False

parseDeclareArray :: Cursor -> ParseError Doc
parseDeclareArray cursor =
    do arrName <- toQID <$> fstMatch parseName children
       arrType <- fstMatch parseType children
       arrSize <- fstMatch parseMaxSize children
       arrInit <- fstMatch (parseArrayInit arrType) children
                            <|> (getDefaultArrayInit arrType arrSize)
       return $ parens $ (text arrName) <+> colon <+> text "createArray" <> parens ((text (show arrSize)) <+> comma <+> arrInit)
    where
        children = child cursor

        parseType :: Cursor -> ParseError String
        parseType cursor =
            do checkThisElement "Type" cursor
               typeText <- getUniqueTextContent' cursor
               return $ T.unpack typeText

        parseMaxSize :: Cursor -> ParseError Int
        parseMaxSize cursor =
            do checkThisElement "MaxSize" cursor
               read <$> T.unpack <$> getUniqueTextContent' cursor

        parseArrayInit :: String -> Cursor -> ParseError Doc
        parseArrayInit typ cursor =
          if hasArrayValue1Level cursor
            then
                return (createArrayWithValues $ head $ (child >=> hasArrayValueAxis1Level) cursor)
            else
                do checkThisElement "InitialValue" cursor
                   doc <- mapM (parseSimpleValue' $ Just $ obtainPLEXILType $ typ) children
                   return $ hsep $ intersperse (text "#") doc
                where
                    children = (child >=> isValue) cursor

        getDefaultArrayInit :: String -> Int -> ParseError Doc
        getDefaultArrayInit arrType len =
            do arrInitBuilder <- case arrType of
                "String"  -> Right "unknownArray" -- $ buildValue $ doubleQuotes PP.empty
                "Integer" -> Right "unknownArray"    -- $ buildValue $ text "0"
                "Real"    -> Right "unknownArray"   -- $ buildValue $ text "0.0"
                "Boolean" -> Right "unknownArray"   -- $ buildValue $ text "false"
                _ -> Left $ "Don't know how to initialize type " ++ show arrType
               return $ text arrInitBuilder <> parens (text $ show len)

elementVisitor :: Cursor -> Doc
elementVisitor cursor = visit
    where
        childElements = concatMap anyElement $ child cursor
        visit =
            case node cursor of
                NodeElement el -> (fix prettyElement)  cursor
                _ -> text ""


prettyElementRHS :: (Cursor -> Doc) -> Cursor -> Doc
prettyElementRHS rec cursor =
    if name == "ArrayElement"
        then errorize $ parseArrayElement' cursor
        else prettyElement rec cursor
    where name = T.unpack $ nameLocalName $ elementName el
          NodeElement el = node cursor


prettyElement :: (Cursor -> Doc) -> Cursor -> Doc
prettyElement rec cursor
    | isNodeCondition cursor =  errorize $ parseNodeCondition cursor
    | isCommand cursor = errorize' "Fail command" $ parseCommand cursor
    | isRHS cursor = hsep children
    | isStatePredicate cursor = errorize $ text <$> parseFinishedPredicate cursor
    | otherwise = helper el children
    where
        isStatePredicate c = not $ null $ checkName (=="Finished") c
        NodeElement el = node cursor
        children = map rec childElements
        childElements = concatMap anyElement $ child cursor
        -- cursor = fromNode $ NodeElement el

createArrayWithValues :: Cursor -> Doc
createArrayWithValues cursor =
                    text "" <> (
                      if typ == "Real"
                        then hcat $ punctuate (text " # ") $ map (errorize . (parseSimpleValue' $ Just $ obtainPLEXILType $ typ) ) $ (child >=> anyElement) cursor
                        else hcat $ punctuate (text " # ") $ map (errorize . parseSimpleValue) $ (child >=> anyElement) cursor
                    )
                    where
                      typ = concatMap T.unpack $ attribute "Type" cursor
                      children = (child >=> anyElement) cursor

-- parsePair :: Cursor -> ParseError Doc
-- parsePair cursor =
--   text "pair" <> parens (
--       case children of
--           [name, value] -> hcat $ punctuate comma $ [parseName (child >=> element "Name" ) cursor, elementVisitor value]
--           _ -> error "Pair must have exactly two children"
--     )

wrapVal :: Doc -> Doc -- wraps document in "val(...)"
wrapVal doc = text "val" <> parens doc


helper el children =
        case name of
            "ArrayElement" -> errorize $ parseArrayElement cursor
            "Index" -> hcat children
            "Node" -> errorize $ parseNode cursor children
            "NodeList" -> parens $ hsep children
            "NodeBody" -> hcat children
            "Update" -> parens (hcat $ punctuate space children)
            "Pair" -> text "pair" <> parens (hcat $ punctuate comma children)
            "Arguments" ->
                parens $ hsep children
            "Assignment" ->
                 errorize $ parseSimpleAssignment cursor
            "VariableDeclarations" -> parens $ hsep children
            "DeclareVariable" ->
                errorize $ parseDeclareVariable cursor
            "DeclareArray" ->
                errorize $ parseDeclareArray cursor
            "BooleanVariable" ->
                text "var" <> parens (
                    char '\'' <>
                        text ( T.unpack $ T.concat $ concatMap content $ child cursor )
                )
            "ArrayVariable" ->
                errorize $
                    do var <- parseArrayVariable cursor
                       return $ text "arrayVar" <> parens (text var)

            "IntegerVariable" ->
                text "var" <> parens (
                    char '\'' <>
                        text ( T.unpack $ T.concat $ concatMap content $ child cursor )
                )
            "RealVariable" ->
                text "var" <> parens (
                    char '\'' <>
                        text ( T.unpack $ T.concat $ concatMap content $ child cursor )
                )
            "StringVariable" ->
                text "var" <> parens (
                    char '\'' <>
                        text ( T.unpack $ T.concat $ concatMap content $ child cursor )
                )
            "RealValue" ->
                text "const" <> parens (
                    errorize $ parseSimpleValue cursor)
            "ArrayValue" ->
                text "const" <> parens (
                    text "array" <> parens (
                        createArrayWithValues cursor
                    )
                )
            "IntegerValue" ->
                text "const" <> parens (
                    text "val" <> parens (
                        text $ T.unpack $ T.concat $ concatMap content $ child cursor
                    )
                )
            "StringValue" ->
                text "const" <> parens (
                    text "val" <> parens (
                        text $ show ( T.unpack $ T.concat $ concatMap content $ child cursor )
                    )
                )
            "BooleanValue" ->
                text "const" <> parens (
                    errorize $ parseSimpleValue cursor)
            "EQBoolean"  -> text "_equ_" <> parens (hcat $ punctuate comma children)
            "NEBoolean"  -> text "_nequ_" <> parens (hcat $ punctuate comma children)
            "EQNumeric"  -> text "_equ_" <> parens (hcat $ punctuate comma children)
            "NENumeric"  -> text "_nequ_" <> parens (hcat $ punctuate comma children)
            "EQString"   -> text "_equ_" <> parens (hcat $ punctuate comma children)
            "NEString"   -> text "_nequ_" <> parens (hcat $ punctuate comma children)
            "EQArray"    -> text "_equ_" <> parens (hcat $ punctuate comma children)
            "NEArray"    -> text "_nequ_" <> parens (hcat $ punctuate comma children)
            "EQInternal" -> case prettyEQInternal cursor children of
                Left msg -> text $ "ERROR: " ++ msg
                Right s -> s
            "NOT" -> text "not_"  <> parens (vcat $ map (nest 2) ( punctuate comma children))
            "AND" -> binarizeFunctionApplication "and"
            "OR" -> binarizeFunctionApplication "or"
            "XOR" -> binarizeFunctionApplication "xor"
            "GE" -> binarizeFunctionApplication ">="
            "GT" -> binarizeFunctionApplication ">"
            "LE" -> binarizeFunctionApplication "<="
            "LT" -> binarizeFunctionApplication "<"
            "SUB" -> binarizeFunctionApplication "-"
            "MUL" -> binarizeFunctionApplication "*"
            "ADD" -> binarizeFunctionApplication "+"
            "MOD" -> binarizeFunctionApplication "rem"
            "IsKnown" -> text "isKnown" <> parens (hcat ( punctuate comma children))

            "NoChildFailed" -> text "noChildFailed"
            "Succeeded" ->
                errorize $
                    do  (ref,dir) <- parseRelativeNodeReference cursor
                        return $ text "hasSucceeded?" <> parens (text dir <> parens (text ref))
            "PostconditionFailed" ->
                errorize $
                    do  (ref,dir) <- parseRelativeNodeReference cursor
                        return $ text "hasPostconditionFailed?" <> parens (text dir <> parens (text ref))
            "Skipped" ->
                errorize $
                    do  (ref,dir) <- parseRelativeNodeReference cursor
                        return $ text "hasSkipped?" <> parens (text dir <> parens (text ref))



            "NodeStateValue" -> prettyNodeStateValue cursor
            "NodeOutcomeVariable" -> either ((text "ERROR: " <>) . text) text $ parseNodeOutcomeVariable cursor
            "NodeOutcomeValue" -> prettyNodeOutcomeVariable cursor
                where prettyNodeOutcomeVariable c = text "TODO: NodeoutcomeVariable"

            "GlobalDeclarations" -> PP.empty -- TODO:

            "PlexilPlan" -> -- Header of the plan
                text ( unlines ["mod " ++ case parseNodeId rootNode of
                                            Left msg -> trace msg "UNKNOWN"
                                            Right id -> id
                                       ++ "-PLAN is",
                                "",
                                "protecting PLEXILITE-PREDS .",
                                "",
                                "op rootNode : -> Plexil .",
                                "eq rootNode = "
                               ] ) $+$ (nest 2 $ vcat children)
                               $+$ text "   ."
                               $+$ text ""
                               $+$ text "endm"
                    where
                        rootNode = head $ (child >=> checkName (=="Node")) cursor
                        nodeId = errorize $ fmap text $ parseNodeId rootNode

            "Name" -> if null ((child >=> element "StringValue") cursor)
              then case parseName cursor of
                Left msg -> text $ "ERROR: " ++ msg
                Right s -> text $ toQID s
              else text $ toQID $ T.unpack $ T.concat $ (child >=> element "StringValue" >=> child >=> content) cursor
            "LookupNow" -> text "lookup" <> parens (hcat $ punctuate comma (children ++ [text "nilarg"] ))
            "LookupOnChange" -> text "lookupOnChange" <> parens (hcat $ punctuate comma (children ++ [text "nilarg"] ++ [text "val(0.0)"]))
            _ -> text name $+$ if null children then Text.PrettyPrint.empty else ( (vcat $ map (nest 2) (punctuate comma children)))
            where
                name = T.unpack $ nameLocalName $ elementName el
                cursor = fromNode $ NodeElement el
                binarizeFunctionApplication tag = case binChildren of
                      Just binChildren' -> pretty binChildren'
                      Nothing -> text $ "error in " ++ tag
                      where maudify tag = "_"++ map toLower tag ++"_"
                            binChildren = binarizeList children
                            pretty (Root x) = x
                            pretty (Branches x y) = text (maudify tag) <> parens (vcat $ map (nest 2) (punctuate comma $ [pretty x,pretty y]))



------------------------------------------------------------
---------- Auxiliary functions

getNodeOutcomeVariable :: Cursor -> Doc
getNodeOutcomeVariable cursor =
    -- let nodeReference:_ = (child >=> element "NodeOutcomeVariable" >=> child >=> element "NodeRef") cursor
    -- in text $ '\'':(maudifyLabel $ T.unpack $ head $ (child >=> content) nodeReference)
    if length ((child >=> element "NodeOutcomeVariable" >=> child >=> element "NodeRef") cursor) == 1
    then
      let nodeReference:_ = (child >=> element "NodeOutcomeVariable" >=> child >=> element "NodeRef") cursor
      in text $ '\'':(maudifyLabel $ T.unpack $ head $ (child >=> content) nodeReference)
    else
      let nodeReference:_ = (child >=> element "NodeOutcomeVariable" >=> child >=> element "NodeId") cursor
      in text $ '\'':(maudifyLabel $ T.unpack $ head $ (child >=> content) nodeReference)

getNodeOutcomeValue :: Cursor -> String
getNodeOutcomeValue cursor =
    let nodeReference:_ = (child >=> element "NodeOutcomeValue" >=> child >=> content) cursor
     in T.unpack $ T.toUpper nodeReference

getNodeStateVariable :: Cursor -> ParseError String
getNodeStateVariable parent =
    let nodeReference = child >=> element "NodeStateVariable"
     in if null $ nodeReference parent
          then throwError $ "\n\nCould not parse a NodeStateVariable element from:\n\n" ++ show parent ++ "\n\n"
          else parseNodeReference $ head $ nodeReference parent

getNodeStateValue :: Cursor -> String
getNodeStateValue cursor =
    let nodeReference:_ = (child >=> element "NodeStateValue" >=> child >=> content) cursor
        rawState =  T.unpack $ T.toUpper nodeReference
     in case rawState of
         "WAITING" -> "waiting"
         "EXECUTING" -> "executing"
         "FINISHING" -> "finishing"
         "FAILING" -> "failing"
         "ITERATION_ENDED" -> "iterationEnded"
         "FINISHED" -> "finished"
         "INACTIVE" -> "inactive"
         _ -> "(Error: don't know how to parse: " ++ rawState ++ ")"

getNodeCommandHandleVariable :: Cursor -> ParseError String
getNodeCommandHandleVariable parent =
    let nodeReference = child >=> element "NodeCommandHandleVariable"
     in if null $ nodeReference parent
          then throwError $ "\n\nCould not parse a NodeCommandHandleVariable element from:\n\n" ++ show parent ++ "\n\n"
          else parseNodeReference $ head $ nodeReference parent

getNodeCommandHandleValue :: Cursor -> String
getNodeCommandHandleValue cursor =
    let nodeReference:_ = (child >=> element "NodeCommandHandleValue" >=> child >=> content) cursor
        rawState =  T.unpack $ T.toUpper nodeReference
     in case rawState of
         "COMMAND_SUCCESS" -> "CommandSuccess"
         "COMMAND_FAILED" -> "CommandFailed"
         "COMMAND_DENIED" -> "CommandDenied"
         "COMMAND_SENT_TO_SYSTEM" -> "CommandSentToSystem"
         "COMMAND_ACCEPTED" -> "CommandAccepted"
         "COMMAND_RCVD_BY_SYSTEM" -> "CommandRcvdBySystem"
         "COMMAND_INTERFACE_ERROR" -> "CommandInterfaceError"
         _ -> "(Error: don't know how to parse: " ++ rawState ++ ")"

toQID :: String -> String
toQID = ("'"++)

parseNodeReference :: Cursor -> ParseError String
parseNodeReference cursor =
    toQID <$> (parseNodeRef cursor <|> parseNodeId cursor)
         `catchError`
            \_ -> throwError $ "Could not parse NodeId nor NodeRef from: " ++ show cursor
    where
        parseNodeRef :: Cursor -> ParseError String
        parseNodeRef cursor =
            let ref = (child >=> element "NodeRef" >=> child >=> content) cursor
            in case ref of
                [r] -> Right $ maudifyLabel $ T.unpack r
                _ -> Left $ "Could not parse NodeRef element from: " ++ show cursor

parseRelativeNodeReference :: Cursor -> ParseError (String,String)
parseRelativeNodeReference cursor =
    do (ref,dir) <- (parseNodeRef cursor <|> ((,"") <$> parseNodeId cursor))
       return (toQID ref,dir)
         `catchError`
            \_ -> throwError $ "Could not parse NodeId nor NodeRef from: " ++ show cursor
    where
        parseNodeRef cursor =
            let nodeRef = child >=> element "NodeRef"
                ref = (nodeRef >=> child >=> content) cursor
                dir = T.concat $ (nodeRef >=> attribute "dir") cursor
            in case ref of
                [r] -> Right $ ((maudifyLabel $ T.unpack r),T.unpack  dir)
                _ -> Left $ "Could not parse NodeRef element from: " ++ show cursor


type ParseError = Either String

parseNodeId :: Cursor -> ParseError String
parseNodeId cursor =
    let nodeIdAxis = (child >=> element "NodeId") cursor
    in case nodeIdAxis of
        [idNode] -> Right $ maudifyLabel $ T.unpack $ head $ (child >=> content) idNode
        _ -> Left  $ "Could not parse NodeId element from: " ++ show cursor


nChildElements :: Int -> Cursor -> ParseError [Cursor]
nChildElements n cursor =
    let childElements = (child >=> anyElement) cursor
     in if length childElements == n
          then Right $ take n childElements
          else Left $ "Only " ++ show n ++ " child elements was expected at: " ++ show cursor

uniqueChildElement :: Cursor -> ParseError Cursor
uniqueChildElement = fmap head . nChildElements 1

twoChildElements :: Cursor -> ParseError (Cursor,Cursor)
twoChildElements = fmap (\(x:y:_) -> (x,y)) . nChildElements 2

threeChildElements :: Cursor -> ParseError (Cursor,Cursor,Cursor)
threeChildElements = fmap (\(x:y:z:_) -> (x,y,z)) . nChildElements 3

parseName :: Cursor -> ParseError String
parseName cursor = (T.unpack <$> getUniqueTextContent (element "Name") cursor)

parseNameFromStringValue :: Cursor -> ParseError String
parseNameFromStringValue cursor =
    do checkThisElement "Name" cursor
       child <- uniqueChildElement cursor
       toQID <$> T.unpack <$> getUniqueTextContent (element "StringValue") child
       -- return (toQID $ T.unpack $ T.concat $ (child >=> element "StringValue" >=> child >=> content) cursor)

parseInitialSimpleValue :: Cursor -> ParseError Doc
parseInitialSimpleValue cursor =
    do checkThisElement "InitialValue" cursor
       child <- uniqueChildElement cursor
       doc <- parseSimpleValue child
       return doc

checkThisElement :: XML.Name -> Cursor -> ParseError Cursor
checkThisElement name cursor =
    let cursor' = element name cursor
     in if null cursor'
          then Left $ "An element `" ++ show name ++ "` was expected at: " ++ show cursor
          else Right $ head cursor'

data PLEXILType = PLEXILInteger | PLEXILReal | PLEXILString | PLEXILBoolean
    deriving (Show,Eq)

obtainPLEXILType :: String -> PLEXILType
obtainPLEXILType "Integer" = PLEXILInteger
obtainPLEXILType "Real"    = PLEXILReal
obtainPLEXILType "String"  = PLEXILString
obtainPLEXILType "Boolean" = PLEXILBoolean
obtainPLEXILType t         = error $ "Unknown PLEXIL type: " ++ t

parseSimpleValue' :: Maybe PLEXILType -> Cursor -> ParseError Doc
parseSimpleValue' typ cursor
  | Just PLEXILBoolean <- typ = (text "val" <+>) <$> parens <$> parseBooleanValue cursor
  | Just PLEXILInteger <- typ = (text "val" <+>) <$> parens <$> parseIntegerValue cursor
  | Just PLEXILReal    <- typ = (text "val" <+>) <$> parens <$> (parseIntegerValueForRealArray cursor
                                                                 <|> parseRealValue cursor)
  | Just PLEXILString  <- typ = (text "val" <+>) <$> parens <$> parseStringValue cursor
  | Nothing            <- typ = (text "val" <+>) <$> parens <$> (parseBooleanValue cursor
                                                                 <|> parseIntegerValue cursor
                                                                 <|> parseRealValue cursor
                                                                 <|> parseStringValue cursor)
  where
      parseIntegerValue c = buildIntegerValue <$> parseTag  "IntegerValue" c
      parseBooleanValue c = buildBoolValue    <$> parseTag  "BooleanValue" c
      parseRealValue    c = buildRealValue    <$> parseTag  "RealValue" c
      parseStringValue  c = buildStringValue  <$> parseTag' "StringValue" c
      parseIntegerValueForRealArray c = buildIntegerValueForRealArray <$> parseTag  "IntegerValue" c

      parseTag  tag cursor = getUniqueTextContent (element tag) cursor
      parseTag' tag cursor = getUniqueTextContentOrEmpty (element tag) cursor

      buildIntegerValue value = text $ T.unpack value
      buildRealValue    value = text $ show $ (read :: String -> Double) $ T.unpack value
      buildIntegerValueForRealArray value = text $ show $ (read :: String -> Double) $ (T.unpack value ++ ".0")
      buildStringValue  value = doubleQuotes $ text $ T.unpack value
      buildBoolValue    value = text $ T.unpack $ obtain value
              where
                obtain v =
                  if v == "1"
                    then "true"
                    else if v == "0"
                      then "false"
                      else (T.toLower v)

parseSimpleValue :: Cursor -> ParseError Doc
parseSimpleValue cursor = parseSimpleValue' Nothing cursor

parseVariable :: Cursor -> ParseError String
parseVariable cursor =
    do
        v <- parseVariableId cursor
        return $ "var( " ++ v ++ " )"

parseVariableId :: Cursor -> ParseError String
parseVariableId cursor = case toQID <$> T.unpack <$> getUniqueTextContent isDeclaredVariable cursor of
    Left x -> Left $ "Error while parsing a Variable: " ++ x
    x -> x

parseArrayVariable :: Cursor -> ParseError String
parseArrayVariable cursor = case toQID <$> T.unpack <$> getUniqueTextContent isDeclaredArrayVariable cursor of
    Left x -> Left $ "Error while parsing an Array Variable: " ++ x
    x -> x

isValue :: Axis
isValue = checkName isValueElement
  where isValueElement :: XML.Name -> Bool
        isValueElement name = "Value" `T.isSuffixOf` (nameLocalName name)

isArrayVariable :: Axis
isArrayVariable = checkName isArrayValueElement
  where isArrayValueElement :: XML.Name -> Bool
        isArrayValueElement name = "ArrayVariable" `T.isSuffixOf` (nameLocalName name)

hasArrayValueAxis1Level :: Axis
hasArrayValueAxis1Level = checkName isArrayValueElement
  where isArrayValueElement :: XML.Name -> Bool
        isArrayValueElement name = "ArrayValue" `T.isSuffixOf` (nameLocalName name)

isDeclaredVariable :: Axis
isDeclaredVariable = checkName isVariableElement
  where isVariableElement :: XML.Name -> Bool
        isVariableElement name = "Variable" `T.isInfixOf` (nameLocalName name)

isDeclaredArrayVariable :: Axis
isDeclaredArrayVariable = checkName isVariableArrayElement
  where isVariableArrayElement :: XML.Name -> Bool
        isVariableArrayElement name = "ArrayVariable" == localName
            where localName = nameLocalName name

getUniqueTextContent' :: Cursor -> ParseError Text
getUniqueTextContent' cursor =
    let textNodes = (child >=> content) cursor
     in if length textNodes == 1
          then Right $ head textNodes
          else Left $ "Could not uniquely determined text content for element: " ++ show cursor ++ "\n\ttextNodes: " ++ show textNodes

getUniqueTextContent :: Axis -> Cursor -> ParseError Text
getUniqueTextContent axis cursor =
    let textNodes = (axis >=> child >=> content) cursor
     in if length textNodes == 1
          then Right $ head textNodes
          else Left $ "Could not uniquely determined text content for element: " ++ show cursor ++ "\n\ttextNodes: " ++ show textNodes

getUniqueTextContentOrEmpty :: Axis -> Cursor -> ParseError Text
getUniqueTextContentOrEmpty axis cursor =
    let textNodes = (axis >=> child >=> content) cursor
     in if length textNodes <= 1
          then Right $ T.concat textNodes
          else Left $ "Could not uniquely determined text content for element: " ++ show cursor ++ "\n\ttextNodes: " ++ show textNodes

data BinarizedList a = Root a | Branches (BinarizedList a) (BinarizedList a) deriving (Show,Eq)

binarizeList :: [a] -> Maybe (BinarizedList a)
binarizeList []       = Nothing
binarizeList [x]      = Just $ Root x
binarizeList [x,y]    = Just $ Branches (Root x) (Root y)
binarizeList (x:y:rs) = Just $ Branches (Branches (Root x) (Root y)) $ fromJust $ binarizeList rs

check' :: Bool -> String -> ParseError ()
check' p msg = if p then Right () else Left msg

isNodeCondition :: Cursor -> Bool
isNodeCondition cursor =
    case node cursor of
        NodeElement el ->
            M.member (nameLocalName $ elementName el) nodeConditionTable
        _ -> False

getNodeConditionMaudeLabel :: Cursor -> String
getNodeConditionMaudeLabel cursor =
    case node cursor of
        NodeElement el ->
            let label = nameLocalName $ elementName el
            in M.findWithDefault "unknownLabel" label nodeConditionTable
        _ -> "unknownLabel"

nodeConditionTable = M.fromList
    [("StartCondition","startc")
    ,("RepeatCondition","repeatc")
    ,("PreCondition","pre")
    ,("PostCondition","post")
    ,("InvariantCondition","inv")
    ,("EndCondition","endc")
    ,("ExitCondition","exitc")
    ,("SkipCondition","skip")
    ]

maudifyNodeType :: String -> String
maudifyNodeType str =
    fromMaybe lowStr $ lookup str [
        ("NodeList","list"),
        ("Command","command"),
        ("Assignment","assignment"),
        ("Empty","empty")
        ]
      where lowStr = map toLower str

maudifyLabel :: String -> String
maudifyLabel label = map underscore2hyphen label
    where underscore2hyphen '_' = '-'
          underscore2hyphen   c = c

parseNodeCondition :: Cursor -> ParseError Doc
parseNodeCondition cursor =
    do
        check' (isNodeCondition cursor) "Cursor is not node condition"
        let childElements = concatMap anyElement $ child cursor
        check' (length childElements == 1) "Node condition should have only one child element"
        let label = getNodeConditionMaudeLabel cursor
        return $
            parens $ text (label ++ ": ") <> (parens (elementVisitor $ head childElements))

parseArguments :: Cursor -> ParseError Doc
parseArguments cursor =
    do checkThisElement "Arguments" cursor
       let prettyChildren = map (fix prettyElementRHS) children
       return $ parens $ hsep prettyChildren
    where children = (child >=> anyElement) cursor

parseOptionalArguments :: Cursor -> ParseError Doc
parseOptionalArguments parentCursor =
    case (child >=> checkName (=="Arguments")) parentCursor of
        [] -> return $ parens $ text "nilpar"
        [argsCursor] -> parseArguments argsCursor
        _ -> Left $ "Multiple `Arguments` elements found under: " ++ show parentCursor