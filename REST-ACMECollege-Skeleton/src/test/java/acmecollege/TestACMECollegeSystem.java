package com.algonquincollege.cst8277.rest.testing;
import static com.algonquincollege.cst8277.utility.MyConstants.*; // Ensure this is present and correct
import java.util.Objects;
import static com.algonquincollege.cst8277.utility.MyConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.net.URI;
import java.util.List;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.algonquincollege.cst8277.entity.Course;
import com.algonquincollege.cst8277.entity.CourseRegistration;
import com.algonquincollege.cst8277.entity.Professor;
import com.algonquincollege.cst8277.entity.Student;
import com.algonquincollege.cst8277.entity.StudentClub;
import com.algonquincollege.cst8277.rest.resource.MyObjectMapperProvider;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMECollegeSystem {

    // Define required connection constants
    public static final String HTTP_SCHEMA = "http";
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final String APPLICATION_CONTEXT_ROOT = "/REST-ACMECollege-Skeleton";
    public static final String APPLICATION_API_VERSION = "/api/v1";

    // Authentication Filters (Assuming default data set: Admin, and user_John.Smith exists)
    public static HttpAuthenticationFeature adminAuth = HttpAuthenticationFeature.basic("admin", "admin");
    public static HttpAuthenticationFeature userAuth = HttpAuthenticationFeature.basic("user_John.Smith", "8277"); 
    
    protected Client client;
    protected WebTarget webTarget;
    protected static URI baseUrl;

    @BeforeAll
    static void initAll() throws Exception {
        baseUrl = UriBuilder
                .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
                .scheme(HTTP_SCHEMA)
                .host(HOST)
                .port(PORT)
                .build();
    }

    @BeforeEach
    void init() {
        client = ClientBuilder.newClient(
            new ClientConfig()
                .register(MyObjectMapperProvider.class)
                .register(new LoggingFeature()) // Enable logging for debugging
        );
        webTarget = client.target(baseUrl);
    }
    
    // =================================================================================
    // 1. STUDENT RESOURCE TESTS (Base/Security)
    // =================================================================================

    // Test 1: Retrieve all students with ADMIN_ROLE (Should succeed)
    @Test
    public void test01_get_all_students_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
        // Assuming default data has at least one student
        assertThat(students, is(not(empty())));
        response.close();
    }
    
    // Test 2: Retrieve all students with USER_ROLE (Should fail - Forbidden)
    @Test
    public void test02_get_all_students_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.FORBIDDEN.getStatusCode())));
        response.close();
    }
    
    // Test 3: Retrieve specific student (ID 1) with USER_ROLE (Owner - Should succeed)
    @Test
    public void test03_get_student_id_1_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME + "/1")
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        Student student = response.readEntity(Student.class);
        assertThat(student.getId(), is(equalTo(1)));
        response.close();
    }
    
    // Test 4: Retrieve specific student (ID 2) with USER_ROLE (Not Owner - Should fail - Forbidden)
    @Test
    public void test04_get_student_id_2_with_userrole_forbidden() {
        Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME + "/2")
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.FORBIDDEN.getStatusCode())));
        response.close();
    }
    
    // =================================================================================
    // 2. COURSE RESOURCE TESTS (CRUD & Security)
    // =================================================================================

    // Test 5: POST a new Course with ADMIN_ROLE
    @Test
    public void test05_post_new_course_with_adminrole() {
        Course newCourse = new Course();
        newCourse.setCourseCode("CST9999");
        newCourse.setCourseTitle("Advanced Testing");
        newCourse.setCreditUnits(3);
        newCourse.setOnline((short) 0);
        
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(newCourse));
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        Course createdCourse = response.readEntity(Course.class);
        assertThat(createdCourse.getCourseCode(), is(equalTo("CST9999")));
        
        // Clean up: delete the created course
        webTarget.register(adminAuth).path(COURSE_RESOURCE_NAME + "/" + createdCourse.getId()).request().delete().close();
        response.close();
    }

    // Test 6: POST a new Course with USER_ROLE (Should fail - Forbidden)
    @Test
    public void test06_post_new_course_with_userrole_forbidden() {
        Course newCourse = new Course();
        newCourse.setCourseCode("CST9998");
        newCourse.setCourseTitle("Forbidden Test");
        newCourse.setCreditUnits(3);
        newCourse.setOnline((short) 0);
        
        Response response = webTarget
                .register(userAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(newCourse));
        
        assertThat(response.getStatus(), is(equalTo(Status.FORBIDDEN.getStatusCode())));
        response.close();
    }
    
    // Test 7: GET all Courses with ADMIN_ROLE
    @Test
    public void test07_get_all_courses_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        List<Course> courses = response.readEntity(new GenericType<List<Course>>(){});
        // Assuming default data has at least one course
        assertThat(courses, is(not(empty())));
        response.close();
    }

    // Test 8: PUT (Update) a Course with ADMIN_ROLE
    @Test
    public void test08_put_update_course_with_adminrole() {
        // Assume Course ID 2 exists
        final int courseId = 2;
        Course update = new Course();
        update.setId(courseId);
        update.setCourseCode("CST8277");
        update.setCourseTitle("UPDATED App Programming");
        update.setCreditUnits(4); // Change credit units

        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME + "/" + courseId)
                .request()
                .put(Entity.json(update));
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        Course updatedCourse = response.readEntity(Course.class);
        assertThat(updatedCourse.getCreditUnits(), is(equalTo(4)));
        response.close();
    }

    // =================================================================================
    // 3. PROFESSOR RESOURCE TESTS (CRUD & Security)
    // =================================================================================

    // Test 9: GET all Professors with ADMIN_ROLE
    @Test
    public void test09_get_all_professors_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(PROFESSOR_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        response.close();
    }
    
    // Test 10: GET all Professors with USER_ROLE (Should fail - Forbidden)
    @Test
    public void test10_get_all_professors_with_userrole_forbidden() {
        Response response = webTarget
                .register(userAuth)
                .path(PROFESSOR_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.FORBIDDEN.getStatusCode())));
        response.close();
    }

    // Test 11: POST a new Professor with ADMIN_ROLE
    @Test
    public void test11_post_new_professor_with_adminrole() {
        Professor newProf = new Professor();
        newProf.setFirstName("Dr.");
        newProf.setLastName("Strange");
        newProf.setDegree("Magic");
        
        Response response = webTarget
                .register(adminAuth)
                .path(PROFESSOR_RESOURCE_NAME)
                .request()
                .post(Entity.json(newProf));
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        Professor createdProf = response.readEntity(Professor.class);
        assertThat(createdProf.getLastName(), is(equalTo("Strange")));
        
        // Clean up
        webTarget.register(adminAuth).path(PROFESSOR_RESOURCE_NAME + "/" + createdProf.getId()).request().delete().close();
        response.close();
    }

    // Test 12: DELETE a Professor with ADMIN_ROLE (Need to create first, then delete)
    @Test
    public void test12_delete_professor_with_adminrole() {
        Professor newProf = new Professor();
        newProf.setFirstName("Delete");
        newProf.setLastName("Me");
        newProf.setDegree("Testing");
        
        Response postResponse = webTarget
                .register(adminAuth)
                .path(PROFESSOR_RESOURCE_NAME)
                .request()
                .post(Entity.json(newProf));
        Professor createdProf = postResponse.readEntity(Professor.class);
        postResponse.close();

        Response deleteResponse = webTarget
                .register(adminAuth)
                .path(PROFESSOR_RESOURCE_NAME + "/" + createdProf.getId())
                .request()
                .delete();
        
        assertThat(deleteResponse.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        deleteResponse.close();
    }

    // =================================================================================
    // 4. STUDENT CLUB TESTS (Read & Security)
    // =================================================================================

    // Test 13: GET all Student Clubs with ADMIN_ROLE (Should succeed)
    @Test
    public void test13_get_all_studentclubs_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        response.close();
    }
    
    // Test 14: GET all Student Clubs with USER_ROLE (Should succeed - read allowed for all)
    @Test
    public void test14_get_all_studentclubs_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        response.close();
    }
    
    // Test 15: POST new Student Club with ADMIN_ROLE
    @Test
    public void test15_post_new_studentclub_with_adminrole() {
        StudentClub newClub = new StudentClub();
        newClub.setName("New Club Test");
        newClub.setDesc("A temporary club");
        newClub.setAcademic(true);
        
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(newClub));
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        StudentClub createdClub = response.readEntity(StudentClub.class);
        assertThat(createdClub.getName(), containsString("Test"));
        
        // Clean up
        webTarget.register(adminAuth).path(STUDENT_CLUB_RESOURCE_NAME + "/" + createdClub.getId()).request().delete().close();
        response.close();
    }

    // =================================================================================
    // 5. COURSE REGISTRATION TESTS (Complex Logic & Write Operations)
    // =================================================================================
    
    // Test 16: GET all Course Registrations with ADMIN_ROLE
    @Test
    public void test16_get_all_courseregistrations_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        response.close();
    }

    // Test 17: GET available Grades with ADMIN_ROLE
    @Test
    public void test17_get_grades_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/grades")
                .request()
                .get();
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        List<String> grades = response.readEntity(new GenericType<List<String>>(){});
        // Assuming database is seeded with grades (e.g., A+, A, B+, ...)
        assertThat(grades, is(not(empty())));
        response.close();
    }
    
    // Test 18: POST new Course Registration with Admin Role (Needs Student ID, Course ID, Year, Semester)
    @Test
    public void test18_post_new_courseregistration_with_adminrole() {
        // Assumes Student ID 1 and Course ID 2 exist in the database
        Student student = new Student();
        student.setId(1);
        Course course = new Course();
        course.setId(2);
        
        CourseRegistration newReg = new CourseRegistration();
        newReg.setStudent(student);
        newReg.setCourse(course);
        newReg.setYear(2025);
        newReg.setSemester("FALL");
        
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME)
                .request()
                .post(Entity.json(newReg));
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        CourseRegistration createdReg = response.readEntity(CourseRegistration.class);
        assertThat(createdReg.getStudent().getId(), is(equalTo(1)));
        
        // Clean up
        webTarget.register(adminAuth)
            .path(COURSE_REGISTRATION_RESOURCE_NAME + "/student/" + 1 + "/course/" + 2)
            .request()
            .delete().close();
        response.close();
    }

    // Test 19: PUT Assign Grade with Admin Role
    @Test
    public void test19_put_assign_grade_with_adminrole() {
        // NOTE: This test requires a CourseRegistration (ID 1, Course ID 1) to exist.
        
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/student/1/course/1/grade/A+")
                .request()
                .put(Entity.text("A+")); // Content of body is just the grade string if needed, but endpoint uses path
        
        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        CourseRegistration updatedReg = response.readEntity(CourseRegistration.class);
        assertThat(updatedReg.getLetterGrade(), is(equalTo("A+")));
        response.close();
    }
    
    // Test 20: PUT Assign Professor with Admin Role
    @Test
    public void test20_put_assign_professor_with_adminrole() {
        // NOTE: This test requires a CourseRegistration (ID 1, Course ID 1) and Professor ID 1 to exist.
        
        Response response = webTarget
                .register(adminAuth)
                .path(COURSE_REGISTRATION_RESOURCE_NAME + "/student/1/course/1/professor/1")
                .request()
                .put(Entity.json("")); // Payload often ignored for assignment logic via path

        assertThat(response.getStatus(), is(equalTo(Status.OK.getStatusCode())));
        CourseRegistration updatedReg = response.readEntity(CourseRegistration.class);
        // Assuming successful assignment
        assertThat(updatedReg.getProfessor(), is(not(equalTo(null))));
        response.close();
    }
}