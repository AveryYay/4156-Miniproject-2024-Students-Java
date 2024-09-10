package dev.coms4156.project.individualproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration
public class IndividualProjectApplicationTests {

  private IndividualProjectApplication application;

  @BeforeEach
  public void setup(){
    application = new IndividualProjectApplication();
  }

  @AfterEach
  public void reset() {
    application.resetDataFile();
    application.setSaveData(true);
  }

  @Test
  public void mainWithSetUpTest() {
    IndividualProjectApplication.main(new String[]{"setup"});
    assertNotNull(IndividualProjectApplication.myFileDatabase);
    File file = new File("./data.txt");
    assertTrue(file.exists());
  }

  @Test
  public void runTestWithSetUpTest() {
    String[] args = {"setup"};
    application.run(args);
    assertNotNull(IndividualProjectApplication.myFileDatabase);
    File file = new File("./data.txt");
    assertTrue(file.exists());
  }

  @Test
  public void runTestWithOutSetUpTest() {
    String[] args = {};
    application.run(args);
    assertNotNull(IndividualProjectApplication.myFileDatabase);
    File file = new File("./data.txt");
    assertTrue(file.exists());
  }

  @Test
  public void overrideDatabaseTest() {
    MyFileDatabase testData = new MyFileDatabase(100, "./invalidDataFile");
    IndividualProjectApplication.overrideDatabase(testData);
    assertNull(IndividualProjectApplication.myFileDatabase.getDepartmentMapping());
  }

  @Test
  public void resetDataFileTest() {
    application.resetDataFile();
    assertNotNull(IndividualProjectApplication.myFileDatabase.getDepartmentMapping());
  }

  @Test
  public void onTerminationSaveFileTest() {
    MyFileDatabaseTestImpl testData = new MyFileDatabaseTestImpl(100, "./invalidDataFile");
    IndividualProjectApplication.overrideDatabase(testData);
    application.setSaveData(true);
    application.onTermination();
    MyFileDatabaseTestImpl test = (MyFileDatabaseTestImpl) IndividualProjectApplication.myFileDatabase;
    assertTrue(test.isSaveCalled());
  }

  @Test
  public void onTerminationNotSaveFileTest() {
    MyFileDatabaseTestImpl testData = new MyFileDatabaseTestImpl(100, "./invalidDataFile");
    IndividualProjectApplication.overrideDatabase(testData);
    application.onTermination();
    MyFileDatabaseTestImpl test = (MyFileDatabaseTestImpl) IndividualProjectApplication.myFileDatabase;
    assertFalse(test.isSaveCalled());
  }

  public static class MyFileDatabaseTestImpl extends MyFileDatabase {
    private boolean saveCalled = false;
    public MyFileDatabaseTestImpl(int flag, String filePath) {
      super(flag, filePath);
    }
    @Override
    public void saveContentsToFile() {
      saveCalled = true;
    }
    public boolean isSaveCalled() {
      return saveCalled;
    }
  }

}
