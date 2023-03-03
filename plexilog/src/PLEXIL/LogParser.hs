{-# LANGUAGE FlexibleContexts #-}
{-# LANGUAGE QuasiQuotes #-}


module PLEXIL.LogParser where

import Data.Maybe (catMaybes)
import qualified Data.Set as Set
import Control.Monad.Identity
import Text.Parsec hiding (State)
import Text.ParserCombinators.Parsec.Number
import Text.RawString.QQ

parseTrace :: String -> Either ParseError Trace
parseTrace = parse p_trace "stdin"

parseMicro :: String -> Either ParseError Micro
parseMicro = parse p_micro "micro"

parsePLEXILOutput :: String -> String -> Either ParseError Trace
parsePLEXILOutput name contents = parse p_trace name contents

main = do
  contents <- getContents
  case parse p_trace "test" contents of
    Left a -> putStrLn $ "Error found: " ++ show a
    Right b -> putStrLn $ show b
  putStrLn "FINISHED"


newtype SeqTrace = SeqTrace { unSeqTrace :: [Event] } deriving (Eq)
data Event = MiCRO | MACRO | TRANS Id State State deriving (Show,Eq,Ord)

instance Show SeqTrace where
  show = concatMap showEvent . unSeqTrace
    where
      showEvent (TRANS id origin destiny) = "  " ++ show id ++ " " ++ show origin ++ " -> " ++ show destiny ++ "\n"
      showEvent MiCRO = " micro" ++ "\n"
      showEvent MACRO = "MACRO" ++ "\n"


seqTransition :: Transition -> Event
seqTransition (Transition id origin destiny) = TRANS id origin destiny

seqMicro :: Micro -> [Event]
seqMicro (Micro trans) = Set.toList (Set.map seqTransition trans) ++ [MiCRO]

seqMacro :: Macro -> [Event]
seqMacro (Macro micros) = (MACRO:) $ concatMap seqMicro micros

seqTrace :: Trace -> SeqTrace
seqTrace (Trace macros) = SeqTrace $ concatMap seqMacro macros


newtype Trace = Trace { unTrace :: [Macro]      } deriving (Show, Eq)
newtype Macro = Macro { unMacro :: [Micro]      } deriving (Show, Eq)

newtype Micro = Micro { unMicro :: Set.Set Transition } deriving (Show, Eq)
data Transition = Transition Id State State deriving (Show, Eq, Ord)
data State = Inactive | Waiting | Executing | IterationEnded | Failing | Finishing | Finished deriving (Show, Eq, Ord)
newtype Id = Id String deriving (Show, Eq, Ord)

maudifyLabel :: String -> String
maudifyLabel label = ('\'':) $ map underscore2hyphen label
    where underscore2hyphen '_' = '-'
          underscore2hyphen   c = c

p_number :: Parsec String () Int
p_number = nat
-- p_number = do s <- getInput
--               case reads s of
--                 [(n, s')] -> n <$ setInput s'
--                 _         -> empty

p_colon :: Parsec String () Char
p_colon = char ':'

p_dot :: Parsec String () Char
p_dot = char '.'

p_identifier :: Parsec String () String
p_identifier = fmap maudifyLabel $ (:) <$> (letter <|> char '\'') <*> many (alphaNum <|> oneOf ['_','-'] <?> "identifier")

p_identifier' :: Parsec String () Id
-- p_identifier' = Id <$> maudifyLabel <$> p_identifier
p_identifier' = Id <$> ((:) <$> (letter <|> char '\'') <*> many (alphaNum <|> oneOf ['_','-']) <?> "quoted identifier")

p_qualified_identifier :: Parsec String () Id
p_qualified_identifier = head <$> (p_identifier' `sepBy1` (try $ nonEOLSpaces *> p_dot <* nonEOLSpaces) <?> "qualified identifier")

-- p_maude_identifier :: Parsec String () String
-- p_maude_identifier = p_word `sepBy` p_dot
--   where p_word = (:) <$> letter <*> many alphaNum
--         p_dot  = string "."

p_state :: Parsec String () State
p_state = choice [Inactive       <$ ((try $ string "INACTIVE")        <|> (try $ string "inactive")      )
                 ,Waiting        <$ ((try $ string "WAITING")         <|> (try $ string "waiting")       )
                 ,Failing        <$ ((try $ string "FAILING")         <|> (try $ string "failing")       )
                 ,Executing      <$ ((try $ string "EXECUTING")       <|> (try $ string "executing")     )
                 ,IterationEnded <$ ((try $ string "ITERATION_ENDED") <|> (try $ string "iterationEnded"))
                 ,Finished       <$ ((try $ string "FINISHED")        <|> (try $ string "finished")      )
                 ,Finishing      <$ ((try $ string "FINISHING")       <|> (try $ string "finishing")     )
                 ] <?> "state"

p_transition :: Parsec String () Transition
p_transition = do spaces *> string "[PlexilExec:step]"
                  between (char '[') (char ']') $ p_number `sepBy` p_colon
                  spaces
                  string "Transitioning node"
                  spaces
                  id <- p_identifier
                  spaces
                  input <- getInput
                  char '0' *> hexadecimal
                  spaces
                  string "from"
                  spaces
                  from <- p_state
                  spaces
                  string "to"
                  spaces
                  to <- p_state
                  -- spaces
                  -- char '\n'
                  spaceEOL
                  return $ Transition (Id id) from to

  -- Transition (Id "A") Waiting <$> spaces *> string "[PlexilExec:step]" *> pure Executing -- *> [1:0:0] Transitioning node

nonEOLSpace :: Parsec String () Char
nonEOLSpace = notFollowedBy endOfLine *> space

-- nonEOLSpaces :: Parsec String () ()
nonEOLSpaces = skipMany nonEOLSpace

spaceEOL :: Parsec String () Char
spaceEOL = optional (try nonEOLSpaces) *> endOfLine

eol :: Parsec String () Char
eol = char '\n'

p_step_id :: Parsec String () [Int]
p_step_id = between (char '[') (char ']') $ p_number `sepBy` p_colon

p_memaddr :: Parsec String () String
p_memaddr = try spaces *> char '0' *> (show <$> hexadecimal)

p_micro :: Parsec String () Micro
-- p_micro = (string "[PlexilExec:step]" *> p_step_id *> string " Check queue: " *> many (noneOf "\n") *> spaceEOL)
--           *> (string "[PlexilExec:step]" *> p_step_id *> string " State change queue: ") *> many (noneOf "\n") *> spaceEOL *> (Micro <$> (try $ many (try p_transition)))
p_micro = do
  trans <- (string "[PlexilExec:step]" *> p_step_id *> string " Check queue: " *> many (noneOf "\n") *> spaceEOL)
          *> (string "[PlexilExec:step]" *> p_step_id *> string " State change queue: ") *> many (noneOf "\n") *> spaceEOL *> ((try $ many (try p_transition)))
  return $ Micro $ Set.fromList trans
-- p_micro = do
--   string "[PlexilExec:step]" *> p_step_id *> string " Check queue: " *> many (noneOf "\n") *> spaceEOL
--   string "[PlexilExec:step]" *> p_step_id *> string " State change queue: " *> many (noneOf "\n") *> spaceEOL
--   trans <- many $ try p_transition
--   return $ Micro trans

p_output_line :: Parsec String () String
p_output_line = (notFollowedBy $ string "[PlexilExec:") *> many (noneOf "\n") <* spaceEOL

p_empty_micro :: Parsec String () ()
p_empty_micro = string "[PlexilExec:step]" *> p_step_id *> string " Check queue: " *> many (noneOf "\n") *> spaceEOL *> pure ()

p_macro :: Parsec String () Macro
p_macro = (do
  (string "[PlexilExec:cycle] ==>Start cycle ") *> p_number *> spaceEOL
  let p_micro_or_empty_micro = (Just <$> try p_micro) <|> (const Nothing <$> p_empty_micro) <?> "micro step or empty micro step"
  macro <- catMaybes <$> many1 (try p_micro_or_empty_micro)
  many p_output_line
  (string "[PlexilExec:cycle] ==>End cycle ") *> p_number *> spaceEOL  -- <* eof
  return $ Macro macro) <?> "macro step"


p_header = (many $ (notFollowedBy $ string "[PlexilExec:") *> many (notFollowedBy endOfLine *> anyToken) *> endOfLine *> return ()) <?> "trace header"

p_trace :: Parsec String () Trace
p_trace = Trace <$> (p_header *> many (try p_macro) <* eof <?> "full trace")

test_input_1 = "[PlexilExec:step][1:0:0] Transitioning node Example7 0x19c3370 from INACTIVE to WAITING\n"
test_input_2 = "[PlexilExec:step][1:0:0] Transitioning node Example7 0x19c3370 from WAITING to EXECUTING\n"
test_input_3 = "[PlexilExec:step][1:0] Check queue: Example7 0x19c3370  \n[PlexilExec:step][1:0] State change queue: Example7 0x19c3370   \n" ++ test_input_1 ++ test_input_2
test_input_4 = "[PlexilExec:cycle] ==>Start cycle 1\n" ++ test_input_3

test_macro = unlines
  ["[PlexilExec:cycle] ==>Start cycle 1"
  ,"[PlexilExec:step][1:1] Check queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:1] State change queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:1:0] Transitioning node Example7 0x19c3370 from WAITING to EXECUTING"
  ,"[PlexilExec:step][1:0] Check queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:0] State change queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:0:0] Transitioning node Example7 0x19c3370 from INACTIVE to WAITING"
  ,"[PlexilExec:step][1:2] Check queue: Example7 0x19c3370 Empty 0x19c36b0 Assignment 0x19c3840 WaitingNode 0x19c3aa0"
  ,"[PlexilExec:step][1:2] State change queue: Empty 0x19c36b0 Assignment 0x19c3840 WaitingNode 0x19c3aa0"
  ,"[PlexilExec:step][1:2:0] Transitioning node Empty 0x19c36b0 from INACTIVE to WAITING"
  ,"[PlexilExec:step][1:2:1] Transitioning node Assignment 0x19c3840 from INACTIVE to WAITING"
  ,"[PlexilExec:step][1:2:2] Transitioning node WaitingNode 0x19c3aa0 from INACTIVE to WAITING"
  ,"[PlexilExec:step][1:3] Check queue: Example7 0x19c3370 Empty 0x19c36b0 Assignment 0x19c3840 WaitingNode 0x19c3aa0"
  ,"[PlexilExec:step][1:3] State change queue: Empty 0x19c36b0 Assignment 0x19c3840"
  ,"[PlexilExec:step][1:3:0] Transitioning node Empty 0x19c36b0 from WAITING to EXECUTING"
  ,"[PlexilExec:step][1:3:1] Transitioning node Assignment 0x19c3840 from WAITING to EXECUTING"
  ,"[PlexilExec:cycle] ==>End cycle 1"
  ,"[PlexilExec:cycle] ==>Start cycle 2"
  ,"[PlexilExec:step][2:0] Check queue: Example7 0x19c3370 WaitingNode 0x19c3aa0 Empty 0x19c36b0 Assignment 0x19c3840"
  ,"[PlexilExec:step][2:0] State change queue: Empty 0x19c36b0 Assignment 0x19c3840"
  ,"[PlexilExec:step][2:0:0] Transitioning node Empty 0x19c36b0 from EXECUTING to ITERATION_ENDED"
  ,"[PlexilExec:step][2:0:1] Transitioning node Assignment 0x19c3840 from EXECUTING to ITERATION_ENDED"
  ,"[PlexilExec:step][2:1] Check queue: Example7 0x19c3370 WaitingNode 0x19c3aa0 Empty 0x19c36b0 Assignment 0x19c3840"
  ,"[PlexilExec:step][2:1] State change queue: WaitingNode 0x19c3aa0 Empty 0x19c36b0 Assignment 0x19c3840"
  ,"[PlexilExec:step][2:1:0] Transitioning node WaitingNode 0x19c3aa0 from WAITING to EXECUTING"
  ,"[PlexilExec:step][2:1:1] Transitioning node Empty 0x19c36b0 from ITERATION_ENDED to FINISHED"
  ,"[PlexilExec:step][2:1:2] Transitioning node Assignment 0x19c3840 from ITERATION_ENDED to FINISHED"
  ,""
  ,"THIS SHOULD BE 0! guard = 1000"
  ,""
  ,"[PlexilExec:cycle] ==>End cycle 2"
  ]

test_macro2 = [r|[PlexilExec:cycle] ==>Start cycle 1
[PlexilExec:step][1:0] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:0] State change queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:0:0] Transitioning node TRAFFIC_RESOLUTION 0x6e3dc0 from INACTIVE to WAITING
[PlexilExec:step][1:1] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:1] State change queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:1:0] Transitioning node TRAFFIC_RESOLUTION 0x6e3dc0 from WAITING to EXECUTING
[PlexilExec:step][1:2] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0 ASSIGNMENT__0 0x72db80 ASSIGNMENT__1 0x72dde0 ASSIGNMENT__2 0x72e040 ASSIGNMENT__3 0x72e2a0 ASSIGNMENT__4 0x72e500 ASSIGNMENT__5 0x72e760 ASSIGNMENT__6 0x72e9c0 ASSIGNMENT__7 0x72ec20 ASSIGNMENT__8 0x72ee80 ASSIGNMENT__9 0x72f0e0 BLOCK__10 0x72f340 ASSIGNMENT__81 0x73a430
[PlexilExec:step][1:2] State change queue: ASSIGNMENT__0 0x72db80 ASSIGNMENT__1 0x72dde0 ASSIGNMENT__2 0x72e040 ASSIGNMENT__3 0x72e2a0 ASSIGNMENT__4 0x72e500 ASSIGNMENT__5 0x72e760 ASSIGNMENT__6 0x72e9c0 ASSIGNMENT__7 0x72ec20 ASSIGNMENT__8 0x72ee80 ASSIGNMENT__9 0x72f0e0 BLOCK__10 0x72f340 ASSIGNMENT__81 0x73a430
[PlexilExec:step][1:2:0] Transitioning node ASSIGNMENT__0 0x72db80 from INACTIVE to WAITING
[PlexilExec:step][1:2:1] Transitioning node ASSIGNMENT__1 0x72dde0 from INACTIVE to WAITING
[PlexilExec:step][1:2:2] Transitioning node ASSIGNMENT__2 0x72e040 from INACTIVE to WAITING
[PlexilExec:step][1:2:3] Transitioning node ASSIGNMENT__3 0x72e2a0 from INACTIVE to WAITING
[PlexilExec:step][1:2:4] Transitioning node ASSIGNMENT__4 0x72e500 from INACTIVE to WAITING
[PlexilExec:step][1:2:5] Transitioning node ASSIGNMENT__5 0x72e760 from INACTIVE to WAITING
[PlexilExec:step][1:2:6] Transitioning node ASSIGNMENT__6 0x72e9c0 from INACTIVE to WAITING
[PlexilExec:step][1:2:7] Transitioning node ASSIGNMENT__7 0x72ec20 from INACTIVE to WAITING
[PlexilExec:step][1:2:8] Transitioning node ASSIGNMENT__8 0x72ee80 from INACTIVE to WAITING
[PlexilExec:step][1:2:9] Transitioning node ASSIGNMENT__9 0x72f0e0 from INACTIVE to WAITING
[PlexilExec:step][1:2:10] Transitioning node BLOCK__10 0x72f340 from INACTIVE to WAITING
[PlexilExec:step][1:2:11] Transitioning node ASSIGNMENT__81 0x73a430 from INACTIVE to WAITING
[PlexilExec:step][1:3] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0 ASSIGNMENT__0 0x72db80 ASSIGNMENT__1 0x72dde0 ASSIGNMENT__2 0x72e040 ASSIGNMENT__3 0x72e2a0 ASSIGNMENT__4 0x72e500 ASSIGNMENT__5 0x72e760 ASSIGNMENT__6 0x72e9c0 ASSIGNMENT__7 0x72ec20 ASSIGNMENT__8 0x72ee80 ASSIGNMENT__9 0x72f0e0 BLOCK__10 0x72f340 ASSIGNMENT__81 0x73a430
[PlexilExec:step][1:3] State change queue: ASSIGNMENT__0 0x72db80
[PlexilExec:step][1:3:0] Transitioning node ASSIGNMENT__0 0x72db80 from WAITING to EXECUTING
[PlexilExec:cycle] ==>End cycle 1|]

test_macro3 = [r|[PlexilExec:cycle] ==>Start cycle 1
[PlexilExec:step][1:0] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:0] State change queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:0:0] Transitioning node TRAFFIC_RESOLUTION 0x6e3dc0 from INACTIVE to WAITING
[PlexilExec:cycle] ==>End cycle 1|]

test_micro3 = [r|[PlexilExec:step][1:0] Check queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:0] State change queue: TRAFFIC_RESOLUTION 0x6e3dc0
[PlexilExec:step][1:0:0] Transitioning node TRAFFIC_RESOLUTION 0x6e3dc0 from INACTIVE to WAITING|]


testManyMicro = unlines $
  ["[PlexilExec:step][1:1] Check queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:1] State change queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:1:0] Transitioning node Example7 0x19c3370 from WAITING to EXECUTING"
  ,"[PlexilExec:step][1:0] Check queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:0] State change queue: Example7 0x19c3370"
  ,"[PlexilExec:step][1:0:0] Transitioning node Example7 0x19c3370 from INACTIVE to WAITING"
  ]

test_maude_input = unlines $
  ["    Example1 from inactive to waiting"
  ,"  micro"
  ,"    Example1 from waiting to executing"
  ,"  micro"
  ,"    TriggerNode . Example1 from inactive to waiting"
  ,"    WaitingNode . Example1 from inactive to waiting"
  ,"  micro"
  ,"    TriggerNode . Example1 from waiting to executing"
  ,"  micro"
  ,"QuIeScEnCe"
  ,"MACRO"
  ,"    TriggerNode . Example1 from executing to failing"
  ,"    WaitingNode . Example1 from waiting to executing"
  ,"  micro"
  ,"QuIeScEnCe"
  ,"MACRO"
  ,"    TriggerNode . Example1 from failing to iterationEnded"
  ,"    WaitingNode . Example1 from executing to finishing"
  ,"  micro"
  ,"    TriggerNode . Example1 from iterationEnded to finished"
  ,"    WaitingNode . Example1 from finishing to iterationEnded"
  ,"  micro"
  ,"    WaitingNode . Example1 from iterationEnded to finished"
  ,"  micro"
  ,"    Example1 from executing to finishing"
  ,"  micro"
  ,"    Example1 from finishing to iterationEnded"
  ,"  micro"
  ,"    Example1 from iterationEnded to finished"
  ,"  micro"
  ,"QuIeScEnCe"
  ,"MACRO"
  ]

test_maude_output = Trace
  [Macro
    [Micro $ Set.fromList [Transition (Id "Example1")    Inactive Waiting  ]
    ,Micro $ Set.fromList [Transition (Id "Example1")    Waiting  Executing]
    ,Micro $ Set.fromList [Transition (Id "TriggerNode") Inactive Waiting
           ,Transition (Id "WaitingNode") Inactive Waiting  ]
    ,Micro $ Set.fromList [Transition (Id "TriggerNode") Waiting  Executing]
    ]
  ,Macro
    [Micro $ Set.fromList [Transition (Id "TriggerNode") Executing Failing
           ,Transition (Id "WaitingNode") Waiting   Executing]
    ]
  ,Macro
    [Micro $ Set.fromList [Transition (Id "TriggerNode") Failing        IterationEnded
           ,Transition (Id "WaitingNode") Executing      Finishing     ]
    ,Micro $ Set.fromList [Transition (Id "TriggerNode") IterationEnded Finished
           ,Transition (Id "WaitingNode") Finishing      IterationEnded]
    ,Micro $ Set.fromList [Transition (Id "WaitingNode") IterationEnded Finished      ]
    ,Micro $ Set.fromList [Transition (Id "Example1")    Executing      Finishing     ]
    ,Micro $ Set.fromList [Transition (Id "Example1")    Finishing      IterationEnded]
    ,Micro $ Set.fromList [Transition (Id "Example1")    IterationEnded Finished      ]
    ]
  ]


test_parser :: (Stream s Identity t,Eq a,Show a) => Parsec s () a -> String -> s -> a -> IO ()
test_parser parser name input output =
  case parse parser name input of
    Left msg -> print msg
    Right tr -> if tr == output
                then putStrLn "OK"
                else putStrLn $ "NOPE:\n" ++ show tr

test_maude_failing = test_parser p_trace "test-maude" input output
  where
    input = test_maude_input
    output = test_maude_output

p_maude_transition' = Transition
                     <$> (nonEOLSpaces *> p_qualified_identifier <* nonEOLSpaces)
                     <*> (string "from" *> nonEOLSpaces *> p_state <* nonEOLSpaces)
                     <*> (string "to" *> nonEOLSpaces *> p_state <* spaceEOL)
                     <?> "transition"
p_maude_transition =
  do nonEOLSpaces
     id <- p_qualified_identifier
     nonEOLSpaces
     string "from"
     nonEOLSpaces
     origin <- p_state
     nonEOLSpaces
     string "to"
     nonEOLSpaces
     destiny <- p_state
     spaceEOL
     return $ Transition id origin destiny

test_maude_transition = do
  test_parser p_maude_transition "test-maude-transition-1" input1 output1
  test_parser p_maude_transition "test-maude-transition-2" input2 output2
    where
      input1  = "    Example1 from inactive to waiting \n"
      output1 = Transition (Id "Example1") Inactive Waiting
      input2  = "    TriggerNode . Example1 from failing to waiting\n"
      output2 = Transition (Id "TriggerNode") Failing Waiting

test_maude_micro = do
  test_parser p_maude_micro "test-maude-micro-1" input1 output1
  test_parser p_maude_micro "test-maude-micro-2" input2 output2
    where
      input1  = unlines $ ["    TriggerNode . Example1 from failing to iterationEnded"
                          ,"    WaitingNode . Example1 from executing to finishing"
                          ,"  micro"]
      output1 = Micro $ Set.fromList [Transition (Id "TriggerNode") Failing   IterationEnded
                      ,Transition (Id "WaitingNode") Executing Finishing     ]
      input2  = unlines $ ["    Example1 from finishing to iterationEnded"
                          ,"  micro"]
      output2 = Micro $ Set.fromList [Transition (Id "Example1") Finishing IterationEnded]

p_maude_micro = Micro
                <$> ((Set.fromList <$> many (try $ p_maude_transition <|> fail "transition missed"))
                <* nonEOLSpaces <* string "micro" <* spaceEOL <?> "micro step")

test_maude_macro = do
  test_parser p_maude_macro "test-maude-macro-1" input1 output1
  test_parser p_maude_macro "test-maude-macro-2" input2 output2
    where
      input1  = unlines $ ["MACRO"
                          ,"    TriggerNode . Example1 from executing to failing"
                          ,"    WaitingNode . Example1 from waiting to executing"
                          ,"  micro"
                          ,"QuIeScEnCe"]
      output1 = Macro [Micro $ Set.fromList [Transition (Id "TriggerNode") Executing Failing
                             ,Transition (Id "WaitingNode") Waiting   Executing]
                      ]

      input2  = unlines $ ["MACRO"
                          ,"    Example1 from inactive to waiting"
                          ,"  micro"
                          ,"    Example1 from waiting to executing"
                          ,"  micro"
                          ,"    TriggerNode . Example1 from inactive to waiting"
                          ,"    WaitingNode . Example1 from inactive to waiting"
                          ,"  micro"
                          ,"QuIeScEnCe"]
      output2 = Macro [Micro $ Set.fromList [Transition (Id "Example1")    Inactive Waiting  ]
                      ,Micro $ Set.fromList [Transition (Id "Example1")    Waiting  Executing]
                      ,Micro $ Set.fromList [Transition (Id "TriggerNode") Inactive Waiting
                             ,Transition (Id "WaitingNode") Inactive Waiting  ]
                      ]

p_maude_macro = Macro
                <$> (nonEOLSpaces *> string "MACRO" *> spaceEOL
                *> many (try $ p_maude_micro)
                <* optional (nonEOLSpaces <* string "QuIeScEnCe" <* spaceEOL) <?> "macro step")

p_maude_trace = Trace <$> (spaces *> many1 (try $ p_maude_macro) <* eof)

parseMaudeTrace :: String -> Either ParseError Trace
parseMaudeTrace = parse p_maude_trace "stdin"

test_maude_trace = test_parser p_maude_trace "test-maude-trace-1" test_maude_input test_maude_output

test_maude = do test_maude_transition
                test_maude_micro
                test_maude_micro
                test_maude_trace
