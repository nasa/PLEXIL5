#!/bin/sh
ghcid -c 'cabal repl plexilog-tests' -W --test='Test.Hspec.hspec Main.spec'
