module PSX2Maude.Options where

import Options.Applicative

newtype Options = Options { psxFilePath :: FilePath } deriving (Show,Eq)

optionsParser :: Parser Options
optionsParser =
  Options
    <$> strArgument
      ( metavar "PSX-FILE"
      <> help "PSX file path")

parseOptions :: IO Options
parseOptions = execParser parserOpts
    where parserOpts = info (optionsParser <**> helper)
                             ( fullDesc
                            <> progDesc "PSX 2 PLEXIL Maude Semantics translator"
                            <> header "PSX2Maude" )