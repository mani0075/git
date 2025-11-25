package com.algonquincollege.cst8277.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("unused")
@Entity
@Table(name = "professor")
@NamedQuery(name = Professor.ALL_PROFESSORS_QUERY, query = "SELECT p FROM Professor p")
public class Professor extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ALL_PROFESSORS_QUERY = "Professor.findAll";

	@Basic(optional = false)
	@Column(name = "first_name", nullable = false, length = 50)
	protected String firstName;

	@Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	protected String lastName;

	@Basic
	@Column(length = 45)
	protected String degree;

	@OneToMany(mappedBy = "professor", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	protected Set<CourseRegistration> courseRegistrations = new HashSet<>();
	
	@Transient
	protected boolean editable = false;

	public Professor() {
		super();
	}

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

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

    @JsonIgnore
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof Professor otherProfessor) {
			return Objects.equals(this.getId(), otherProfessor.getId());
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Professor[id = ").append(getId()).append(", firstName = ").append(firstName).append(", lastName = ")
				.append(lastName).append(", degree = ").append(degree)
				.append(", created = ").append(getCreated()).append(", updated = ").append(getUpdated()).append(", version = ").append(getVersion()).append("]");
		return builder.toString();
	}
}