module Options where

import Options.Applicative

data Options = Options
   { optPLXFile          :: FilePath
   , optWriteExpandedXML :: Bool
   , optVerbose          :: Bool
   } deriving Show

optionsParser :: Parser Options
optionsParser =
    Options
        <$> strArgument
           ( metavar "PLX-PLAN"
          <> help "PLX PLEXIL plan to translate.")
        <*> switch
          (  long "write-expanded-xml"
          <> help "Write expanded XML plan with embedded library calls."
          )
        <*> switch
          (  short 'v'
          <> long "verbose"
          <> help "Generate verbose output."
          )

parseOptions :: IO Options
parseOptions = execParser parserOpts
    where parserOpts = info (optionsParser <**> helper)
                             ( fullDesc
                            <> progDesc "PLX 2 PLEXIL Maude Semantics translator"
                            <> header "plx2maude" )