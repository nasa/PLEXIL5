module Main where

import PLEXIL.LogParser (parseTrace,parseMaudeTrace,Trace(..),seqTrace,Event,SeqTrace,Macro(..))

import Control.Exception (throw,Exception)
import Data.Algorithm.Diff
import Data.Algorithm.DiffOutput
import Data.Semigroup ((<>))
import Data.Typeable (Typeable)
import Options.Applicative


data CmdArgs = CmdArgs
    { plexilLogFilename :: FilePath
    , maudeLogFilename  :: FilePath }

options :: Parser CmdArgs
options = CmdArgs
    <$> argument str (metavar "PLEXIL-log-file")
    <*> argument str (metavar "Maude-log-file")

data DiffException = ParsingMaudeException String | ParsingPLEXILException String
   deriving (Show, Typeable)

instance Exception DiffException

toStringList :: Diff Macro -> Diff [String]
toStringList (First (Macro ms)) = First $ map show ms
toStringList (Second (Macro ms)) = Second $ map show ms
toStringList (Both (Macro ms) (Macro ms')) = Both (map show ms) (map show ms')

main :: IO ()
main = do
    options <- execParser opts

    plexilLog <- readFile $ plexilLogFilename options
    maudeLog <- readFile $ maudeLogFilename options

    plexilTrace <- case parseTrace plexilLog of
        Left msg -> throw $ ParsingPLEXILException (show msg)
        Right t  -> return t

    maudeTrace <- case parseMaudeTrace maudeLog of
        Left msg -> throw $ ParsingMaudeException (show msg)
        Right t  -> return t

    if plexilTrace == maudeTrace
        then putStrLn "EQ"
        else do putStrLn $ unlines [
                    "PLEXIL log: ",
                    show plexilTrace,
                    "Maude log: ",
                    show maudeTrace
                    ]
                let plexilTrace' = unTrace plexilTrace
                let maudeTrace' = unTrace maudeTrace
                -- let diff = filter (not . isBoth) $ getDiff plexilTrace' maudeTrace'
                let diff = getDiff plexilTrace' maudeTrace'
                if null diff
                    then putStrLn "Logs are equal"
                    else do putStrLn "Diff:"
                            putStrLn $ show diff
                            putStrLn "\nDiff classic:"
                            putStrLn $ ppDiff $ map toStringList diff
                            putStrLn $ "Count: " ++ (show . length) diff



    -- contents <- getContents
    -- case parseTrace contents of
    --   Left a -> putStrLn $ "Error found: " ++ show a
    --   Right b -> putStrLn $ show b
    where
        opts = info (options <**> helper)
            ( fullDesc
            <> progDesc "Compares PLEXIL logs between the executive and its Maude model"
            <> header "plexil-diff - a PLEXIL log comparator")

        isBoth (Both _ _) = True
        isBoth _ = False


test :: IO ()
test = do Right plexilTrace <- parseTrace      <$> readFile "output.0.log"
          Right maudeTrace  <- parseMaudeTrace <$> readFile "output.0.maude"
          let seqPlexilTrace = seqTrace plexilTrace
          let seqMaudeTrace = seqTrace maudeTrace
          putStrLn "Plexil trace: "
          putStrLn $ show seqPlexilTrace
          putStrLn "Maude trace: "
          putStrLn $ show seqMaudeTrace


testPlexil =
    do Right plexilTrace <- parseTrace      <$> readFile "output.0.log"
       let seqPlexilTrace = seqTrace plexilTrace
       putStrLn "Plexil trace: "
       putStrLn $ show seqPlexilTrace

testMaude =
    do Right maudeTrace  <- parseMaudeTrace <$> readFile "output.0.maude"
       let seqMaudeTrace = seqTrace maudeTrace
       putStrLn "Maude trace: "
       putStrLn $ show seqMaudeTrace