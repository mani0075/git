package com.algonquincollege.cst8277.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@SuppressWarnings("unused")
@Entity(name = "Student")
@Table(name = "student")
@NamedQuery(name = Student.ALL_STUDENTS_QUERY_NAME, query = "SELECT s FROM Student s LEFT JOIN FETCH s.courseRegistrations LEFT JOIN FETCH s.studentClubs")
@NamedQuery(name = Student.QUERY_STUDENT_BY_ID, query = "SELECT s FROM Student s LEFT JOIN FETCH s.courseRegistrations LEFT JOIN FETCH s.studentClubs WHERE s.id = :param1")
public class Student extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ALL_STUDENTS_QUERY_NAME = "Student.findAll";
    public static final String QUERY_STUDENT_BY_ID = "Student.findAllByID";

    public Student() {
    	super();
    }
    
    @Basic(optional = false)
	@Column(name = "first_name", nullable = false, length = 50)
	protected String firstName;
    
    @Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	protected String lastName;

    @Basic(optional = true)
	@Column(name = "email", nullable = true, length = 100)
    protected String email;

    @Basic(optional = true)
	@Column(name = "phone", nullable = true, length = 10)
    protected String phone;

    @Basic(optional = true)
	@Column(name = "program", nullable = true, length = 45)
    protected String program;
    
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "student")
	protected Set<CourseRegistration> courseRegistrations = new HashSet<>();
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name="club_membership",
    joinColumns=@JoinColumn(name="student_id", referencedColumnName="id"),
    inverseJoinColumns=@JoinColumn(name="club_id", referencedColumnName="id"))
    @JsonIgnore
    protected Set<StudentClub> studentClubs = new HashSet<StudentClub>();
    
    @Transient
	protected boolean editable = false;
    
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

    @JsonIgnore
    public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

    public Set<StudentClub> getStudentClubs() {
		return studentClubs;
	}

	public void setStudentClubs(Set<StudentClub> studentClubs) {
		this.studentClubs = studentClubs;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Student[id = ").append(getId()).append(", firstName = ").append(firstName).append(", lastName = ")
				.append(lastName).append(", email = ").append(email).append(", phone = ").append(phone).append(", program = ").append(program)
				.append(", created = ").append(getCreated()).append(", updated = ").append(getUpdated()).append(", version = ").append(getVersion()).append("]");
		return builder.toString();
	}
}