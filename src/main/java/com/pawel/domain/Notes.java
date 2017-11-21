package com.pawel.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Created by Pawel on 2017-10-28.
 */
@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"recipe"})
public class Notes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Recipe recipe;

	@Lob
	private String recipeNotes;

	public Notes() {
	}

}
