package com.svishevsky.service;

import com.svishevsky.model.DirDescription;
import com.svishevsky.model.FileDescription;
import com.svishevsky.util.LineParseUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathParserService {

  public static final String JAVA_EXTENSION = ".java";

  public DirDescription parseDir(Path dir) {
    DirDescription result = new DirDescription();
    result.setName(dir.getFileName().toString());
    try {
      Files.list(dir).forEach(path -> {
        if (Files.isDirectory(path)) {
          DirDescription innerDir = parseDir(path);
          result.setLinesCount(result.getLinesCount() + innerDir.getLinesCount());
          result.addDir(innerDir);
        } else if (path.getFileName().toString().endsWith(JAVA_EXTENSION)) {
          FileDescription innerFile = parseFile(path);
          result.setLinesCount(result.getLinesCount() + innerFile.getLinesCount());
          result.addFile(innerFile);
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return result;
  }

  public FileDescription parseFile(Path path) {
    String name = path.getFileName().toString();

    int linesCount = 0;
    boolean multilineComment = false;
    try {
      for (String line : Files.readAllLines(path)) {
        String parsedLine = line;
        if (multilineComment) {
          if (LineParseUtil.multilineCommentEnds(line)) {
            parsedLine = LineParseUtil.removeMultilineEndComment(line);
            multilineComment = false;
          } else {
            continue;
          }
        }

        parsedLine = LineParseUtil.removeInlineComments(parsedLine);
        if (LineParseUtil.multilineCommentStarts(parsedLine)) {
          parsedLine = LineParseUtil.removeMultilineStartComment(parsedLine);
          multilineComment = true;
        }
        linesCount += parsedLine.isEmpty() ? 0 : 1;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return new FileDescription(name, linesCount);
  }
}
