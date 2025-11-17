module PLX.LibraryCall where

import Prelude hiding ((<>))
import qualified Text.PrettyPrint as PP
import Text.XML.Cursor

import Parser (ParseError, parseNode)


parseLibraryCallNode :: Cursor -> ParseError PP.Doc
parseLibraryCallNode cursor = parseNode cursor
    -- Right $ PP.text "library"