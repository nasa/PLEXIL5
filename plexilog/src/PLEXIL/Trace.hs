module PLEXIL.Trace (Trace (..), Macro (..), Micro (..), Transition (..), State (..), Id (..)) where

import qualified Data.Set as Set

newtype Trace = Trace {unTrace :: [Macro]} deriving (Show, Eq)

newtype Macro = Macro {unMacro :: [Micro]} deriving (Show, Eq)

newtype Micro = Micro {unMicro :: Set.Set Transition} deriving (Show, Eq)

data Transition = Transition Id State State deriving (Show, Eq, Ord)

data State = Inactive | Waiting | Executing | IterationEnded | Failing | Finishing | Finished deriving (Show, Eq, Ord)

newtype Id = Id String deriving (Show, Eq, Ord)
