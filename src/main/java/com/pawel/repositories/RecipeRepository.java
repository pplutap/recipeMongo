package com.pawel.repositories;

import com.pawel.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Pawel on 2017-10-28.
 */
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
