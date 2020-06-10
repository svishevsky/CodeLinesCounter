package com.svishevsky;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.svishevsky.model.DirDescription;
import com.svishevsky.model.FileDescription;
import com.svishevsky.service.PathParserService;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CliProcessor.class)
public class CliProcessorTest {

  private final static PrintStream outBackup = System.out;

  @Mock
  private PathParserService pathParserService;

  @Mock
  private Scanner scanner;

  @Mock
  private PrintStream out;

  private CliProcessor cliProcessor;

  @AfterClass
  public static void afterAll() {
    System.setOut(outBackup);
  }

  @Before
  public void setup() throws Exception {
    PowerMockito.whenNew(PathParserService.class).withNoArguments().thenReturn(pathParserService);
    PowerMockito.whenNew(Scanner.class).withAnyArguments().thenReturn(scanner);
    System.setOut(out);
    cliProcessor = new CliProcessor();
  }

  @Test
  public void shouldExit() {
    when(scanner.nextLine()).thenReturn("exit");
    cliProcessor.run();

    verify(out, times(1)).println(anyString());
  }

  @Test
  public void shouldPrintHelp() {
    when(scanner.nextLine()).thenReturn("help").thenReturn("exit");
    cliProcessor.run();

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(out, times(2)).println(captor.capture());

    assertEquals(CliProcessor.HELP_MESSAGE, captor.getValue());
  }

  @Test
  public void shouldShowShowHelpNotificationOnBadCommand() {
    when(scanner.nextLine()).thenReturn("wrong command").thenReturn("exit");

    cliProcessor.run();

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    verify(out, times(3)).println(captor.capture());
    assertEquals(CliProcessor.HELP_NOTIFICATION, captor.getValue());
  }

  @Test
  public void shouldShowWrongPathMessage() {
    when(scanner.nextLine()).thenReturn("lines sample").thenReturn("exit");
    PowerMockito.mockStatic(Paths.class);
    when(Paths.get(any(String.class))).thenReturn(mock(Path.class));
    PowerMockito.mockStatic(Files.class);
    when(Files.exists(any(Path.class))).thenReturn(false);
    cliProcessor.run();

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    verify(out, times(2)).println(captor.capture());
    assertEquals(String.format(CliProcessor.WRONG_PATH_FORMAT, "sample"), captor.getValue());
  }

  @Test
  public void shouldPrintDirDescription() {
    when(scanner.nextLine()).thenReturn("lines sample").thenReturn("exit");
    PowerMockito.mockStatic(Paths.class);
    when(Paths.get(any(String.class))).thenReturn(mock(Path.class));
    PowerMockito.mockStatic(Files.class);
    when(Files.exists(any(Path.class))).thenReturn(true);
    when(Files.isDirectory(any(Path.class))).thenReturn(true);

    DirDescription root = new DirDescription("root", 21);
    DirDescription innerDir = new DirDescription("inner", 15);
    innerDir.addFile(new FileDescription("1.java", 10));
    innerDir.addFile(new FileDescription("2.java", 5));
    root.addDir(innerDir);
    root.addFile(new FileDescription("3.java", 6));
    when(pathParserService.parseDir(any(Path.class))).thenReturn(root);

    cliProcessor.run();

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    List<String> expected = Arrays.asList(
        "root : 21",
        " inner : 15",
        "  1.java : 10",
        "  2.java : 5",
        " 3.java : 6");

    verify(out, times(6)).println(captor.capture());
    assertEquals(expected, captor.getAllValues().subList(1, 6));
  }

  @Test
  public void shouldPrintFileDescription() {
    when(scanner.nextLine()).thenReturn("lines sample").thenReturn("exit");
    PowerMockito.mockStatic(Paths.class);
    Path mockPath = mock(Path.class);
    when(Paths.get(any(String.class))).thenReturn(mockPath);
    PowerMockito.mockStatic(Files.class);
    when(Files.exists(any(Path.class))).thenReturn(true);
    when(Files.isDirectory(any(Path.class))).thenReturn(false);
    Path fileNamePath = mock(Path.class);
    when(mockPath.getFileName()).thenReturn(fileNamePath);
    when(fileNamePath.toString()).thenReturn("Hello.java");

    FileDescription file = new FileDescription("Hello.java", 32);
    when(pathParserService.parseFile(any(Path.class))).thenReturn(file);

    cliProcessor.run();

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    verify(out, times(2)).println(captor.capture());
    assertEquals("Hello.java : 32", captor.getValue());
  }

}
