package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



/**
 * This class contains unit tests for the {@code RouterController} class.
 */
@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc
public class RouteControllerTests {
  @MockBean
  private MyFileDatabase myFileDatabase;

  private HashMap<String, Department> mockDepartmentData;

  public static MyFileDatabase originalDatabase;


  @Autowired
  private MockMvc mockMvc;

  @BeforeAll
  public static void saveOriginalDatabase() {
    originalDatabase = IndividualProjectApplication.myFileDatabase;
  }
  /**
   * Sets up a department with sample courses for testing.
   * This method initializes a Department object with test data
   * and is executed before each test case.
   */

  @BeforeEach
  public void setup() {
    mockDepartmentData = new HashMap<>();

    HashMap<String, Course> mockCourses = new HashMap<>();
    mockCourses.put("1004", new Course("Adam Cannon", "417 IAB", "11:40-12:55", 400));

    Department computerDept = new Department("COMS", mockCourses, "Luca Carloni", 2700);
    mockDepartmentData.put("COMS", computerDept);
    myFileDatabase.setMapping(mockDepartmentData);
    when(myFileDatabase.getDepartmentMapping()).thenReturn(mockDepartmentData);
    IndividualProjectApplication.myFileDatabase = myFileDatabase;
  }

  @AfterAll
  public static void restoreOriginalDatabase() {
    IndividualProjectApplication.myFileDatabase = originalDatabase;
  }

  @Test
  public void indexSlashTest() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().string("Welcome, in order to make an API call "
            + "direct your browser or Postman to an endpoint \n\n "
            + "This can be done using the following format: "
            + "\n\n http:127.0.0.1:8080/endpoint?arg=value"));
  }

  @Test
  public void indexIndexTest() throws Exception {
    mockMvc.perform(get("/index"))
        .andExpect(status().isOk())
        .andExpect(content().string("Welcome, in order to make an API call "
            + "direct your browser or Postman to an endpoint \n\n "
            + "This can be done using the following format: "
            + "\n\n http:127.0.0.1:8080/endpoint?arg=value"));
  }

  @Test
  public void indexHomeTest() throws Exception {
    mockMvc.perform(get("/home"))
        .andExpect(status().isOk())
        .andExpect(content().string("Welcome, in order to make an API call "
            + "direct your browser or Postman to an endpoint \n\n "
            + "This can be done using the following format: "
            + "\n\n http:127.0.0.1:8080/endpoint?arg=value"));
  }

  @Test
  public void retrieveDepartmentSuccessTest() throws Exception {
    mockMvc.perform(get("/retrieveDept")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("COMS 1004: \nInstructor: Adam Cannon; "
                + "Location: 417 IAB; Time: 11:40-12:55\n"));
  }

  @Test
  public void retrieveDepartmentNotFoundTest() throws Exception {
    mockMvc.perform(get("/retrieveDept")
            .param(RouteController.DEPT_CODE_PARAM, "Random")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.DEPT_NOT_FOUND));
  }

  @Test
  public void retrieveCourseSuccessTest() throws Exception {
    mockMvc.perform(get("/retrieveCourse")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("\nInstructor: Adam Cannon; Location: 417 IAB; "
                + "Time: 11:40-12:55"));
  }

  @Test
  public void retrieveCourseNotFoundTest() throws Exception {
    mockMvc.perform(get("/retrieveCourse")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void retrieveCourseDepartmentNotFoundTest() throws Exception {
    mockMvc.perform(get("/retrieveCourse")
            .param(RouteController.DEPT_CODE_PARAM, "Random")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(RouteController.DEPT_NOT_FOUND));
  }

  @Test
  public void isCourseFullFalseTest() throws Exception {
    Course course = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    course.setEnrolledStudentCount(50);
    mockMvc.perform(get("/isCourseFull")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("false"));
  }

  @Test
  public void isCourseFullTrueTest() throws Exception {
    Course course = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    course.setEnrolledStudentCount(1000);
    mockMvc.perform(get("/isCourseFull")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void isCourseFullNotFoundTest() throws Exception {
    mockMvc.perform(get("/isCourseFull")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void isCourseFullDepartmentNotFoundTest() throws Exception {
    mockMvc.perform(get("/isCourseFull")
            .param(RouteController.DEPT_CODE_PARAM, "Random")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void getMajorCountFromDepartmentSuccessTest() throws Exception {
    mockMvc.perform(get("/getMajorCountFromDept")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("There are: 2700 majors in the department"));
  }

  @Test
  public void getMajorCountFromDepartmentNotFoundTest() throws Exception {
    mockMvc.perform(get("/getMajorCountFromDept")
            .param(RouteController.DEPT_CODE_PARAM, "Random")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(RouteController.DEPT_NOT_FOUND));
  }

  @Test
  public void identifyDeptChairSuccessTest() throws Exception {
    mockMvc.perform(get("/idDeptChair")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Luca Carloni is the department chair."));
  }

  @Test
  public void identifyDeptChairFailTest() throws Exception {
    mockMvc.perform(get("/idDeptChair")
            .param(RouteController.DEPT_CODE_PARAM, "Random")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.DEPT_NOT_FOUND));
  }

  @Test
  public void findCourseLocationSuccessTest() throws Exception {
    mockMvc.perform(get("/findCourseLocation")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("417 IAB is where the course is located."));
  }

  @Test
  public void findCourseLocationFailTest() throws Exception {
    mockMvc.perform(get("/findCourseLocation")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void findCourseInstructorSuccessTest() throws Exception {
    mockMvc.perform(get("/findCourseInstructor")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("Adam Cannon is the instructor for the course."));
  }

  @Test
  public void findCourseInstructorFailTest() throws Exception {
    mockMvc.perform(get("/findCourseInstructor")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void findCourseTimeSuccessTest() throws Exception {
    mockMvc.perform(get("/findCourseTime")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("The course meets at: 11:40-12:55"));
  }

  @Test
  public void findCourseTimeFailTest() throws Exception {
    mockMvc.perform(get("/findCourseTime")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void addMajorToDeptSuccessTest() throws Exception {
    Department computerDept =
        IndividualProjectApplication.myFileDatabase.getDepartmentMapping().get("COMS");
    mockMvc.perform(patch("/addMajorToDept")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("Attribute was updated successfully"));
    assertEquals(computerDept.getNumberOfMajors(), 2701);
  }

  @Test
  public void addMajorToDeptFailedTest() throws Exception {
    mockMvc.perform(patch("/addMajorToDept")
            .param(RouteController.DEPT_CODE_PARAM, "Random")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.DEPT_NOT_FOUND));
  }

  @Test
  public void removeMajorFromDeptSuccessTest() throws Exception {
    Department computerDept =
        IndividualProjectApplication.myFileDatabase.getDepartmentMapping().get("COMS");
    mockMvc.perform(patch("/removeMajorFromDept")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("Attribute was updated or is at minimum"));
    assertEquals(computerDept.getNumberOfMajors(), 2699);
  }

  @Test
  public void removeMajorFromDeptFailedTest() throws Exception {
    mockMvc.perform(patch("/removeMajorFromDept")
            .param(RouteController.DEPT_CODE_PARAM, "Random")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.DEPT_NOT_FOUND));
  }

  @Test
  public void dropStudentFromCourseSuccessTest() throws Exception {
    Course mockCourse = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    mockCourse.setEnrolledStudentCount(100);
    mockMvc.perform(patch("/dropStudentFromCourse")
        .param(RouteController.DEPT_CODE_PARAM, "COMS")
        .param(RouteController.COURSE_CODE_PARAM, "1004")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Student has been dropped."));
  }

  @Test
  public void dropStudentFromCourseFailedTest() throws Exception {
    Course mockCourse = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    mockCourse.setEnrolledStudentCount(0);
    mockMvc.perform(patch("/dropStudentFromCourse")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Student has not been dropped."));
  }

  @Test
  public void dropStudentFromCourseNotFoundTest() throws Exception {
    mockMvc.perform(patch("/dropStudentFromCourse")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void setEnrollStudentCountSuccessTest() throws Exception {
    Course mockCourse = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    mockMvc.perform(patch("/setEnrollmentCount")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .param("count", "123")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(RouteController.ATTRIBUTE_UPDATE_SUCCESS));
    assertEquals(123, mockCourse.getEnrolledStudentCount());
  }

  @Test
  public void setEnrollStudentCountFailedTest() throws Exception {
    mockMvc.perform(patch("/setEnrollmentCount")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .param("count", "123")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void changeCourseTimeSuccessTest() throws Exception {
    Course mockCourse = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    mockMvc.perform(patch("/changeCourseTime")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .param("time", "11:40-12:00")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(RouteController.ATTRIBUTE_UPDATE_SUCCESS));
    assertEquals("11:40-12:00", mockCourse.getCourseTimeSlot());
  }

  @Test
  public void changeCourseTimeNotFoundTest() throws Exception {
    mockMvc.perform(patch("/changeCourseTime")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .param("time", "11:40-12:00")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
           .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void changeCourseTeacherSuccessTest() throws Exception {
    Course mockCourse = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    mockMvc.perform(patch("/changeCourseTeacher")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .param("teacher", "New Instructor")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(RouteController.ATTRIBUTE_UPDATE_SUCCESS));
    assertEquals("New Instructor", mockCourse.getInstructorName());
  }

  @Test
  public void changeCourseTeacherNotFoundTest() throws Exception {
    mockMvc.perform(patch("/changeCourseTeacher")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .param("teacher", "New Instructor")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void changeCourseLocationSuccessTest() throws Exception {
    Course mockCourse = mockDepartmentData.get("COMS").getCourseSelection().get("1004");
    mockMvc.perform(patch("/changeCourseLocation")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1004")
            .param("location", "New Location")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(RouteController.ATTRIBUTE_UPDATE_SUCCESS));
    assertEquals("New Location", mockCourse.getCourseLocation());
  }

  @Test
  public void changeCourseLocationNotFoundTest() throws Exception {
    mockMvc.perform(patch("/changeCourseLocation")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .param(RouteController.COURSE_CODE_PARAM, "1005")
            .param("location", "New Location")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(RouteController.COURSE_NOT_FOUND));
  }

  @Test
  public void handleExceptionTest() throws Exception {
    when(myFileDatabase.getDepartmentMapping()).thenThrow(new RuntimeException("Database Error"));
    mockMvc.perform(get("/retrieveDept")
            .param(RouteController.DEPT_CODE_PARAM, "COMS")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An Error has occurred"));
  }

}
