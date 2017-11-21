package com.pawel.repositories;

import com.pawel.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Pawel on 2017-10-28.
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {

	Optional<Category> findByDescription(String string);
}
