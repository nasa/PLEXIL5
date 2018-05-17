package org.nianet.plexil;

public class Util {

  public static boolean isCondition(String s) {
    return s.startsWith("repeatc") || s.startsWith("inv")
        || s.startsWith("endc") || s.startsWith("post")
        || s.startsWith("skip") || s.startsWith("active")
        || s.startsWith("startc") || s.startsWith("pre");
  }

}
