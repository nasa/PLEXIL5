module RecursiveUnfolder where

import Data.List (delete)

recSingleUnfold :: (Eq a, Show a) => [(String, a)] -> [(String, a)] -> ((String, a) -> a -> a) -> ((String, a) -> [String]) -> (String, a) -> (String, a)
recSingleUnfold pairs unfolded unfoldF depsF current = foldr (\(l1, x1) (accl, accx) -> (accl, unfoldF (l1, x1) accx)) current unfoldedDeps
  where
    deps = map f $ depsF current
      where
        f l = case lookup l pairs of
          Just a -> (l, a)
          Nothing ->
            error $
              "recSingleUnfold.deps:\n\tlookup coult not find "
                ++ show l
                ++ " in "
                ++ show (map fst pairs)

    unfoldedDeps = map f deps
      where
        f = recSingleUnfold pairs unfolded' unfoldF depsF

    unfolded' = current : unfolded

test :: (String, [String]) -> (String, [String])
test = recSingleUnfold pairs unfolded unfoldF depsF
  where
    pairs = [("a", ["b", "c", "d"]), ("b", ["c"]), ("c", ["d", "e"]), ("d", []), ("e", [])]
    unfolded = []
    depsF (_, xs) = xs
    unfoldF (l, _) = delete l
