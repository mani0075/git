package com.algonquincollege.cst8277.entity;

import java.io.Serializable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity implementation for NonAcademic Student Clubs.
 */
@Entity
@DiscriminatorValue("0") // Discriminator value 0 for Non-Academic
public class NonAcademic extends StudentClub implements Serializable {
	private static final long serialVersionUID = 1L;

	public NonAcademic() {
		super(false); // Call super constructor setting isAcademic to false
	}
}