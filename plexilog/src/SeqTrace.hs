module PLEXIL.SeqTrace where

import PLEXIL.Trace

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
