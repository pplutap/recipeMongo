package com.pawel.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2017-10-29.
 */
public class CategoryTest {

	private Category category;
	private final Long id = 4L;
	private final String name = "American";

	@Before
	public void setUp() {
		this.category = new Category();
		this.category.setId(id);
		this.category.setDescription(name);
	}

	@Test
	public void getId() throws Exception {
		assertEquals(id, category.getId());
	}

	@Test
	public void getName() throws Exception {
		assertEquals(name, category.getDescription());
	}
}
