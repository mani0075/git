package com.algonquincollege.cst8277.entity;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table(name = "security_user")
@NamedQuery(name = SecurityUser.SECURITY_USER_BY_NAME, query = "SELECT su FROM SecurityUser su LEFT JOIN FETCH su.roles WHERE su.username = :param1")
@NamedQuery(name = SecurityUser.SECURITY_USER_BY_STUDENT_ID, query = "SELECT su FROM SecurityUser su LEFT JOIN FETCH su.roles WHERE su.student.id = :param1")
public class SecurityUser extends PojoBase implements Serializable, Principal {
    private static final long serialVersionUID = 1L;
    
    public static final String SECURITY_USER_BY_NAME = "SecurityUser.userByName";
    public static final String SECURITY_USER_BY_STUDENT_ID = "SecurityUser.userByStudentId";

    @Basic(optional = false)
    @Column(nullable = false, length = 100)
    protected String username;
    
    @Basic(optional = false)
    @Column(name = "password_hash", nullable = false, length = 256)
    protected String pwHash;
    
    @OneToOne
    @JoinColumn(name = "student_id")
    protected Student student;
    
    @ManyToMany
    @JoinTable(name = "user_has_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    protected Set<SecurityRole> roles = new HashSet<SecurityRole>();

    public SecurityUser() {
        super();
    }

    // Inherited getId() and setId() from PojoBase

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwHash() {
        return pwHash;
    }
    
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    @JsonSerialize(using = com.algonquincollege.cst8277.rest.serializer.SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    // Principal
    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // Uses getId() inherited from PojoBase
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
        if (obj instanceof SecurityUser otherSecurityUser) {
            // Compares using getId() inherited from PojoBase
            return Objects.equals(this.getId(), otherSecurityUser.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityUser [id = ").append(getId()).append(", username = ").append(username).append("]");
        return builder.toString();
    }
}