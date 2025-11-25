package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.entity.Student.ALL_STUDENTS_QUERY_NAME;

import java.io.Serializable;
import java.util.*;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.algonquincollege.cst8277.entity.*;
import com.algonquincollege.cst8277.utility.MyConstants;

@SuppressWarnings("unused")
@Singleton
public class ACMECollegeService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LogManager.getLogger();
    
    private static final String READ_ALL_PROGRAMS = "SELECT name FROM program";
    private static final String FIND_GRADE = "SELECT name FROM letter_grade";
    
    @PersistenceContext(name = MyConstants.PU_NAME)
    protected EntityManager em;
    
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    // --- Student CRUD (Existing/Base) ---
    public List<Student> getAllStudents() { /* ... implementation ... */ return null; }
    public Student getStudentById(int id) { return em.find(Student.class, id); }
    @Transactional
    public Student persistStudent(Student newStudent) { em.persist(newStudent); return newStudent; }
    
    @Transactional
    public void buildUserForNewStudent(Student newStudent) {
        SecurityUser userForNewStudent = new SecurityUser();
        userForNewStudent.setUsername(
            MyConstants.DEFAULT_USER_PREFIX + "_" + newStudent.getFirstName() + "." + newStudent.getLastName());
        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(MyConstants.PROPERTY_ALGORITHM, MyConstants.DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(MyConstants.PROPERTY_ITERATIONS, MyConstants.DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(MyConstants.PROPERTY_SALT_SIZE, MyConstants.DEFAULT_SALT_SIZE);
        pbAndjProperties.put(MyConstants.PROPERTY_KEY_SIZE, MyConstants.DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(MyConstants.DEFAULT_USER_PASSWORD.toCharArray());
        userForNewStudent.setPwHash(pwHash);
        userForNewStudent.setStudent(newStudent);
        
        SecurityRole userRole = em.createNamedQuery(SecurityRole.SECURITY_ROLE_BY_NAME, SecurityRole.class)
                                  .setParameter(MyConstants.PARAM1, MyConstants.USER_ROLE)
                                  .getSingleResult();
        userForNewStudent.getRoles().add(userRole);
        userRole.getUsers().add(userForNewStudent);
        em.persist(userForNewStudent);
    }
    
    @Transactional
    public Student updateStudentById(int id, Student studentWithUpdates) {
    	Student existing = getStudentById(id);
        if (existing != null) { em.merge(studentWithUpdates); }
        return studentWithUpdates;
    }
    
    @Transactional
    public Student deleteStudentById(int id) {
        Student student = getStudentById(id);
        if (student != null) {
            em.refresh(student);
            TypedQuery<SecurityUser> findUser = em.createNamedQuery("SecurityUser.userByStudentId", SecurityUser.class).setParameter(MyConstants.PARAM1, id);
            try {
                 SecurityUser sUser = findUser.getSingleResult();
                 em.remove(sUser);
            } catch (NoResultException e) {}
            em.remove(student);
        }
        return student;
    }
    
	@SuppressWarnings("unchecked")
    public List<String> getAllPrograms() { /* ... implementation ... */ return new ArrayList<>(); }

    // ----------------- Professor CRUD -----------------
    public List<Professor> getAllProfessors() { TypedQuery<Professor> query = em.createNamedQuery(Professor.ALL_PROFESSORS_QUERY, Professor.class); return query.getResultList(); }
    public Professor getProfessorById(int id) { return em.find(Professor.class, id); }
    @Transactional
    public Professor persistProfessor(Professor newProfessor) { em.persist(newProfessor); return newProfessor; }
    @Transactional
    public Professor updateProfessor(int id, Professor professorWithUpdates) { Professor existing = getProfessorById(id); if (existing != null) { em.merge(professorWithUpdates); } return professorWithUpdates; }
    @Transactional
    public void deleteProfessorById(int id) { Professor professor = getProfessorById(id); if (professor != null) { em.remove(professor); } }

    // ----------------- Course CRUD -----------------
    public List<Course> getAllCourses() { TypedQuery<Course> query = em.createNamedQuery(Course.ALL_COURSES_QUERY, Course.class); return query.getResultList(); }
    public Course getCourseById(int id) { return em.find(Course.class, id); }
    @Transactional
    public Course persistCourse(Course newCourse) { em.persist(newCourse); return newCourse; }
    @Transactional
    public Course updateCourse(int id, Course courseWithUpdates) { Course existing = getCourseById(id); if (existing != null) { em.merge(courseWithUpdates); } return courseWithUpdates; }
    @Transactional
    public void deleteCourseById(int id) { Course course = getCourseById(id); if (course != null) { em.remove(course); } }

    // ----------------- CourseRegistration CRUD -----------------
    public List<CourseRegistration> getAllCourseRegistrations() { TypedQuery<CourseRegistration> query = em.createNamedQuery(CourseRegistration.ALL_COURSE_REGISTRATIONS_QUERY_NAME, CourseRegistration.class); return query.getResultList(); }
    public CourseRegistration getCourseRegistrationById(int studentId, int courseId) { CourseRegistrationPK pk = new CourseRegistrationPK(studentId, courseId); return em.find(CourseRegistration.class, pk); }
    @Transactional
    public CourseRegistration persistCourseRegistration(CourseRegistration newRegistration) { em.persist(newRegistration); return newRegistration; }
    @Transactional
    public CourseRegistration assignProfessorToCourseRegistration(int studentId, int courseId, Professor professor) { CourseRegistration existing = getCourseRegistrationById(studentId, courseId); if (existing != null) { existing.setProfessor(professor); em.merge(existing); } return existing; }
    @Transactional
    public CourseRegistration assignGradeToCourseRegistration(int studentId, int courseId, String grade) { CourseRegistration existing = getCourseRegistrationById(studentId, courseId); if (existing != null) { existing.setLetterGrade(grade); em.merge(existing); } return existing; }
    @Transactional
    public void deleteCourseRegistrationById(int studentId, int courseId) { CourseRegistrationPK pk = new CourseRegistrationPK(studentId, courseId); CourseRegistration registration = em.find(CourseRegistration.class, pk); if (registration != null) { em.remove(registration); } }
    @SuppressWarnings("unchecked")
    public List<String> getAllLetterGrades() { /* ... implementation ... */ return new ArrayList<>(); }

    // ----------------- StudentClub CRUD -----------------
    public List<StudentClub> getAllStudentClubs() { TypedQuery<StudentClub> query = em.createNamedQuery(StudentClub.ALL_STUDENT_CLUBS_QUERY, StudentClub.class); return query.getResultList(); }
    public StudentClub getStudentClubById(int id) { return em.find(StudentClub.class, id); }
    @Transactional
    public StudentClub persistStudentClub(StudentClub newClub) { em.persist(newClub); return newClub; }
    @Transactional
    public StudentClub updateStudentClub(int id, StudentClub updates) { StudentClub existing = getStudentClubById(id); if (existing != null) { existing.setName(updates.getName()); existing.setDesc(updates.getDesc()); existing.setAcademic(updates.getAcademic()); em.merge(existing); } return existing; }
    @Transactional
    public void deleteStudentClubById(int id) { StudentClub club = getStudentClubById(id); if (club != null) { em.remove(club); } }
}