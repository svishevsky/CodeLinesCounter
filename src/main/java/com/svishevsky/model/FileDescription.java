package com.svishevsky.model;

public class FileDescription {

  private final String name;
  private final int linesCount;

  public FileDescription(String name, int linesCount) {
    this.name = name;
    this.linesCount = linesCount;
  }

  public String getName() {
    return name;
  }

  public int getLinesCount() {
    return linesCount;
  }
}
