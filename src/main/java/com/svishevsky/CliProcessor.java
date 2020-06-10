package com.svishevsky;

import com.svishevsky.model.DirDescription;
import com.svishevsky.model.FileDescription;
import com.svishevsky.service.PathParserService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Scanner;

public class CliProcessor {

  public static final String HELP_CMD = "help";
  public static final String EXIT_CMD = "exit";
  public static final String READ_LINES_CMD = "lines";

  public static final String HELP_MESSAGE = "usage:\n" +
      "\thelp - type to see all commands\n" +
      "\tlines <path> - count code lines for directory or file\n" +
      "\texit - exit application";
  public static final String WRONG_INPUT_NOTIFICATION = "Wrong input!";
  public static final String WRONG_EXTENSION = "Not a '.java' file!";
  public static final String WRONG_PATH_FORMAT = "Wrong path: %s";
  public static final String HELP_NOTIFICATION = "Type 'help' to see all commands";

  private static final String WHITESPACES_PATTERN = "\\s+";
  private final Scanner scanner;
  private final PathParserService pathParserService;

  public CliProcessor() {
    this.scanner = new Scanner(System.in);
    this.pathParserService = new PathParserService();
  }

  public static void main(String[] args) {
    CliProcessor cliProcessor = new CliProcessor();
    cliProcessor.run();
  }

  public void run() {
    System.out.println(HELP_NOTIFICATION);
    boolean doExit = false;
    while (!doExit) {
      String inputString = scanner.nextLine();

      try {
        String[] split = inputString.trim().split(WHITESPACES_PATTERN);

        String cmd = split[0];

        switch (cmd) {
          case READ_LINES_CMD: {
            String path = split[1];
            readLines(path);
            break;
          }
          case EXIT_CMD: {
            doExit = true;
            break;
          }
          case HELP_CMD: {
            help();
            break;
          }
          default: {
            System.out.println(WRONG_INPUT_NOTIFICATION);
            System.out.println(HELP_NOTIFICATION);
            break;
          }
        }
      } catch (Exception e) {
        System.out.println(WRONG_INPUT_NOTIFICATION);
        System.out.println(HELP_NOTIFICATION);
      }
    }
  }

  private void readLines(String pathString) {
    Path path = Paths.get(pathString);
    if (Files.exists(path)) {
      if (Files.isDirectory(path)) {
        printDirResult(0, pathParserService.parseDir(path));
      } else if (path.getFileName().toString().endsWith(PathParserService.JAVA_EXTENSION)) {
        printFileResult(pathParserService.parseFile(path));
      } else {
        System.out.println(WRONG_EXTENSION);
      }
    } else {
      System.out.println(String.format(WRONG_PATH_FORMAT, pathString));
    }
  }

  private void printDirResult(int hierarchyLevel, DirDescription dir) {
    String prefix = String.join("", Collections.nCopies(hierarchyLevel, " "));
    System.out.println(String.format("%s%s : %s", prefix, dir.getName(), dir.getLinesCount()));

    if (dir.getDirs() != null) {
      dir.getDirs().forEach(d -> printDirResult(hierarchyLevel + 1, d));
    }
    if (dir.getFiles() != null) {
      dir.getFiles().forEach(f ->
          System.out.println(String.format(" %s%s : %s", prefix, f.getName(), f.getLinesCount())));
    }
  }

  private void printFileResult(FileDescription file) {
    System.out.println(String.format("%s : %s", file.getName(), file.getLinesCount()));
  }

  private void help() {
    System.out.println(HELP_MESSAGE);
  }

}
