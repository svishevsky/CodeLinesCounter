package com.svishevsky.service;

import static org.junit.Assert.assertEquals;

import com.svishevsky.model.DirDescription;
import com.svishevsky.model.FileDescription;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;

public class PathParserServiceTest {

  private PathParserService pathParserService;

  @Before
  public void setup() {
    pathParserService = new PathParserService();
  }

  @Test
  public void testParseFile() throws Exception {
    Path path = getPath("Hello.java");
    FileDescription result = pathParserService.parseFile(path);

    assertEquals("Hello.java", result.getName());
    assertEquals(9, result.getLinesCount());
  }

  @Test
  public void testParseDir() throws Exception {
    Path path = getPath("test");
    DirDescription result = pathParserService.parseDir(path);
    assertEquals("test", result.getName());
    assertEquals(15, result.getLinesCount());
    assertEquals(1, result.getFiles().size());
    assertEquals(1, result.getDirs().size());

    FileDescription file = result.getFiles().get(0);
    assertEquals("1.java", file.getName());
    assertEquals(9, file.getLinesCount());

    DirDescription innerDir = result.getDirs().get(0);
    assertEquals("inner", innerDir.getName());
    assertEquals(6, innerDir.getLinesCount());
    assertEquals(1, innerDir.getFiles().size());

    FileDescription innerFile = innerDir.getFiles().get(0);
    assertEquals("2.java", innerFile.getName());
    assertEquals(6, innerFile.getLinesCount());
  }

  private Path getPath(String name) throws Exception {
    return Paths
        .get(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(name)).toURI());
  }

}