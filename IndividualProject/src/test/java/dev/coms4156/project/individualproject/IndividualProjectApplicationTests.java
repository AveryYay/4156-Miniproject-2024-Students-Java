package dev.coms4156.project.individualproject;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
  public void setup(){
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
