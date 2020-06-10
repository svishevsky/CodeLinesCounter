package com.svishevsky.model;

import java.util.ArrayList;
import java.util.List;

public class DirDescription {

  private String name;
  private int linesCount;
  private List<DirDescription> dirs;
  private List<FileDescription> files;

  public DirDescription() {
  }

  public DirDescription(String name, int linesCount) {
    this.name = name;
    this.linesCount = linesCount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLinesCount() {
    return linesCount;
  }

  public void setLinesCount(int linesCount) {
    this.linesCount = linesCount;
  }

  public List<DirDescription> getDirs() {
    return dirs;
  }

  public void addDir(DirDescription dir) {
    if (dirs == null) {
      dirs = new ArrayList<>();
    }
    dirs.add(dir);
  }

  public List<FileDescription> getFiles() {
    return files;
  }

  public void addFile(FileDescription file) {
    if (files == null) {
      files = new ArrayList<>();
    }
    files.add(file);
  }

}
