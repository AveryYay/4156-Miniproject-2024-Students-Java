package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;

/**
 * This class contains unit tests for the {@code Department} class.
 */
@SpringBootTest
@ContextConfiguration
public class DepartmentUnitTests {

  @BeforeEach
  public void setupDepartmentForTesting() {
    HashMap<String, Course> courseMap = new HashMap<>();
    courseMap.put("1004", new Course("Adam Cannon", "417 IAB", "11:40-12:55", 400));
    courseMap.put("3134", new Course("Brian Borowski", "301 URIS", "4:10-5:25", 250));

    testDepartment = new Department("COMS", courseMap, "COMSChair", 3);
  }


  @Test
  public void toStringTest() {
    String expectedResult = "COMS 1004: \n Instructor: Adam Cannon; Location: 417 IAB; " +
        "Time: 11:40-12:55\n" + "COMS 3134: \n Instructor: Brian Borowski; Location: 301 URIS; " +
        "Time: 4:10-5:25\n";
    assertEquals(expectedResult, testDepartment.toString());
  }

  @Test
  public void getNumberOfMajorsTest() {
    assertEquals(3, testDepartment.getNumberOfMajors());
  }

  @Test
  public void getDepartmentChairTest() {
    assertEquals("COMSChair", testDepartment.getDepartmentChair());
  }

  @Test
  public void getCourseSelectionTest() {
    HashMap<String, Course> courses = testDepartment.getCourseSelection();
    assertEquals(2, courses.size());
    assertTrue(courses.containsKey("1004"));
    assertTrue(courses.containsKey("3134"));
  }

  @Test
  public void addPersonToMajorTest() {
    testDepartment.addPersonToMajor();
    assertEquals(4, testDepartment.getNumberOfMajors());
  }

  @Test
  public void dropPersonFromMajorPositiveTest() {
    testDepartment.dropPersonFromMajor();
    assertEquals(2, testDepartment.getNumberOfMajors());
  }

  @Test
  public void dropPersonFromMajorNegativeTest() {
    testDepartment.dropPersonFromMajor();
    testDepartment.dropPersonFromMajor();
    testDepartment.dropPersonFromMajor();
    testDepartment.dropPersonFromMajor();
    assertEquals(0, testDepartment.getNumberOfMajors());
  }

  @Test
  public void addCourseTest() {
    testDepartment.addCourse("3157", new Course("Jae Lee", "417 IAB", "4:10-5:25", 400));
    HashMap<String, Course> courses = testDepartment.getCourseSelection();
    assertEquals(3, courses.size());
    assertTrue(courses.containsKey("3157"));
  }

  @Test
  public void addCourseDuplicateTest() {
    testDepartment.addCourse("1004", new Course("Adam Cannon", "417 IAB", "11:40-12:55", 400));
    assertEquals(2, testDepartment.getCourseSelection().size());
  }

  @Test
  public void createCourseTest() {
    testDepartment.createCourse("3157", "Jae Lee", "417 IAB", "4:10-5:25", 400);
    HashMap<String, Course> courses = testDepartment.getCourseSelection();
    assertEquals(3, courses.size());
    assertTrue(courses.containsKey("3157"));
  }

  /** The test course instance used for testing. */
  public static Department testDepartment;
}

