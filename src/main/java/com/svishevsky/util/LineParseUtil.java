package com.svishevsky.util;

public final class LineParseUtil {

  private static final String QUOTES_PATTERN = "(\")(.*?)(\")";
  private static final String COMMENT_PATTERN = "(/\\*)(.*?)(\\*/)";
  private static final String ONE_LINE_COMMENT_PATTERN = "//.*";
  private static final String MULTILINE_COMMENT_START_PATTERN = "/*";
  private static final String MULTILINE_COMMENT_START_REPLACE_PATTERN = "(/\\*.*)";
  private static final String MULTILINE_COMMENT_END_PATTERN = "*/";
  private static final String MULTILINE_COMMENT_END_REPLACE_PATTERN = "(.*?\\*/)";
  private static final String EMPTY_STRING = "";

  private LineParseUtil() {
  }

  public static String removeInlineComments(String line) {
    String trimmedLine = line.trim();
    return trimmedLine.replaceAll(COMMENT_PATTERN, EMPTY_STRING)
        .replaceAll(ONE_LINE_COMMENT_PATTERN, EMPTY_STRING);
  }

  public static boolean multilineCommentStarts(String line) {
    String withoutQuotes = line.replaceAll(QUOTES_PATTERN, EMPTY_STRING);

    return withoutQuotes.contains(MULTILINE_COMMENT_START_PATTERN);
  }

  public static boolean multilineCommentEnds(String line) {
    return line.contains(MULTILINE_COMMENT_END_PATTERN);
  }

  public static String removeMultilineStartComment(String line) {
    String trimmedLine = line.trim();
    return trimmedLine.replaceAll(MULTILINE_COMMENT_START_REPLACE_PATTERN, EMPTY_STRING);
  }

  public static String removeMultilineEndComment(String line) {
    return line.replaceFirst(MULTILINE_COMMENT_END_REPLACE_PATTERN, EMPTY_STRING);
  }


}
