package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the {@code IndividualProjectApplication} class.
 */
@SpringBootTest
@ContextConfiguration
public class IndividualProjectApplicationTests {
  @MockBean
  private MyFileDatabase mockFileDatabase;
  private IndividualProjectApplication application;
  public static MyFileDatabase originalDatabase;

  @BeforeAll
  public static void saveOriginalDatabase() {
    originalDatabase = IndividualProjectApplication.myFileDatabase;
  }

  @AfterAll
  public static void restoreOriginalDatabase() {
    IndividualProjectApplication.myFileDatabase = originalDatabase;
  }

  @BeforeEach
  public void setup() {
    application = new IndividualProjectApplication();
    IndividualProjectApplication.myFileDatabase = mockFileDatabase;
  }

  @AfterEach
  public void reset() {
    application.setSaveData(true);
  }

  @Test
  public void mainWithSetUpTest() {
    doNothing().when(mockFileDatabase).saveContentsToFile();
    IndividualProjectApplication.main(new String[]{"setup"});
    assertNotNull(IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void runTestWithSetUpTest() {
    String[] args = {"setup"};
    application.run(args);
    assertNotNull(IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void runTestWithOutSetUpTest() {
    String[] args = {"blabla"};
    application.run(args);
    assertNotNull(IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void overrideDatabaseTest() {
    MyFileDatabase testData = new MyFileDatabase(100, "./invalidDataFile");
    IndividualProjectApplication.overrideDatabase(testData);
    assertNull(IndividualProjectApplication.myFileDatabase.getDepartmentMapping());
  }

  @Test
  public void resetDataFileTest() {
    MyFileDatabase testData = new MyFileDatabase(100, "./invalidDataFile");
    IndividualProjectApplication.overrideDatabase(testData);
    application.resetDataFile();
    assertNotNull(IndividualProjectApplication.myFileDatabase.getDepartmentMapping());
  }

  @Test
  public void onTerminationSaveFileTest() {
    application.setSaveData(true);
    doNothing().when(mockFileDatabase).saveContentsToFile();
    application.onTermination();
    verify(mockFileDatabase, times(1)).saveContentsToFile();
  }

  @Test
  public void onTerminationNotSaveFileTest() {
    application.setSaveData(false);
    application.onTermination();
    verify(mockFileDatabase, never()).saveContentsToFile();
  }

}
