package com.algonquincollege.cst8277.entity;

import java.io.Serializable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity implementation for Academic Student Clubs.
 */
@Entity
@DiscriminatorValue("1") // Discriminator value for Academic (as opposed to 0 for NonAcademic)
public class Academic extends StudentClub implements Serializable {
	private static final long serialVersionUID = 1L;

	public Academic() {
		super(true); // Call super constructor setting isAcademic to true
	}
}