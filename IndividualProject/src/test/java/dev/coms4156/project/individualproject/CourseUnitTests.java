package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the {@code Course} class.
 */
@SpringBootTest
@ContextConfiguration
public class CourseUnitTests {

  @BeforeAll
  public static void setupCourseForTesting() {
    testCourse = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
  }


  @Test
  public void toStringTest() {
    String expectedResult = "\nInstructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55";
    assertEquals(expectedResult, testCourse.toString());
  }

  @Test
  public void getCourseLocationTest() {
    assertEquals("417 IAB", testCourse.getCourseLocation());
  }

  @Test
  public void getCourseTimeSlotTest() {
    assertEquals("11:40-12:55", testCourse.getCourseTimeSlot());
  }

  @Test
  public void getInstructorNameTest() {
    assertEquals("Griffin Newbold", testCourse.getInstructorName());
  }

  @Test
  public void isCourseFullTest() {
    assertFalse(testCourse.isCourseFull());
  }

  @Test
  public void setEnrolledStudentCountTest() {
    testCourse.setEnrolledStudentCount(200);
    assertFalse(testCourse.isCourseFull());
  }

  @Test
  public void dropStudentSuccessTest() {
    testCourse.setEnrolledStudentCount(100);
    assertTrue(testCourse.dropStudent());
  }

  @Test
  public void dropStudentFailTest() {
    testCourse.setEnrolledStudentCount(0);
    assertFalse(testCourse.dropStudent());
  }

  @Test
  public void enrollStudentSuccessTest() {
    testCourse.setEnrolledStudentCount(0);
    assertTrue(testCourse.enrollStudent());
  }

  @Test
  public void enrollStudentFailTest() {
    testCourse.setEnrolledStudentCount(250);
    assertFalse(testCourse.enrollStudent());
  }

  /** The test course instance used for testing. */
  public static Course testCourse;
}

