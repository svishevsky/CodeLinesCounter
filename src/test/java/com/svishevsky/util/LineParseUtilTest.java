package com.svishevsky.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineParseUtilTest {

  @Test
  public void shouldRemoveComments() {
    String line = "      System./*wait*/out./*for*/println/*it*/(\"Hello/*\");";
    String result = LineParseUtil.removeInlineComments(line);

    assertEquals("System.out.println(\"Hello/*\");", result);
  }

  @Test
  public void shouldRemoveInlineComments() {
    String line = "//Sample inline comment";
    String result = LineParseUtil.removeInlineComments(line);

    assertEquals("", result);
  }

  @Test
  public void shouldRemoveSpaces() {
    String line = "\t    test  \n";
    String result = LineParseUtil.removeInlineComments(line);

    assertEquals("test", result);
  }

  @Test
  public void shouldReturnTrueForMultilineCommentStart() {
    String line = "/* Comment";
    boolean result = LineParseUtil.multilineCommentStarts(line);

    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseForMultilineCommentStartInsideString() {
    String line = "String stringComment = \"/* Comment\"";
    boolean result = LineParseUtil.multilineCommentStarts(line);

    assertFalse(result);
  }

  @Test
  public void shouldReturnTrueForMultilineCommentEnd() {
    String line = "*/int a=1;/* Comment";
    boolean result = LineParseUtil.multilineCommentEnds(line);

    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseForMultilineCommentEnd() {
    String line = "* ////* Comment\"";
    boolean result = LineParseUtil.multilineCommentEnds(line);

    assertFalse(result);
  }

  @Test
  public void shouldRemoveMultilineCommentStart() {
    String line = "*/int a = 1;/* Comment\"";
    String result = LineParseUtil.removeMultilineStartComment(line);

    assertEquals("*/int a = 1;", result);
  }

  @Test
  public void shouldRemoveMultilineCommentEnd() {
    String line = "*/int a = 1;/* Comment\"*//*";
    String result = LineParseUtil.removeMultilineEndComment(line);

    assertEquals("int a = 1;/* Comment\"*//*", result);
  }
}