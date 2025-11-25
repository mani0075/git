package com.algonquincollege.cst8277.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@SuppressWarnings("unused")
@Entity
@Table(name = "course")
@NamedQuery(name = Course.ALL_COURSES_QUERY, query = "SELECT c FROM Course c")
public class Course extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ALL_COURSES_QUERY = "Course.findAll";

	@Basic(optional = false)
	@Column(name = "course_code", nullable = false, length = 7, unique = true)
	protected String courseCode;

	@Basic(optional = false)
	@Column(name = "course_title", nullable = false, length = 100)
	protected String courseTitle;

	@Basic(optional = false)
	@Column(name = "credit_units", nullable = false)
	protected Integer creditUnits;

	@Basic(optional = false)
	@Column(columnDefinition = "BIT(1)", nullable = false)
	protected Short online;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	protected Set<CourseRegistration> courseRegistrations = new HashSet<>();
	
	@Transient
	protected boolean editable = false;

	public Course() {
		super();
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public Integer getCreditUnits() {
		return creditUnits;
	}

	public void setCreditUnits(Integer creditUnits) {
		this.creditUnits = creditUnits;
	}

	public Short getOnline() {
		return online;
	}

	public void setOnline(Short online) {
		this.online = online;
	}

	public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
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
		builder.append("Course[id = ").append(getId()).append(", courseCode = ").append(courseCode).append(", courseTitle = ")
				.append(courseTitle).append(", creditUnits = ").append(creditUnits).append(", online = ").append(online)
				.append(", created = ").append(getCreated()).append(", updated = ").append(getUpdated()).append(", version = ").append(getVersion()).append("]");
		return builder.toString();
	}
}