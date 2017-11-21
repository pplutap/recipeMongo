package com.pawel.repositories;

import com.pawel.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2017-10-29.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoryIT {

	@Autowired
	private UnitOfMeasureRepository repository;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void findByDescription() throws Exception {
		Optional<UnitOfMeasure>  uomOptional = repository.findByDescription("Teaspoon");

		assertEquals("Teaspoon", uomOptional.get().getDescription());
	}

	@Test
	public void findByDescriptionCup() throws Exception {
		Optional<UnitOfMeasure>  uomOptional = repository.findByDescription("Cup");

		assertEquals("Cup", uomOptional.get().getDescription());
	}

}
