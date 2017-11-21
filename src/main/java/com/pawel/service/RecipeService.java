package com.pawel.service;

import com.pawel.commands.RecipeCommand;
import com.pawel.domain.Recipe;

import java.util.Set;

/**
 * Created by Pawel on 2017-10-28.
 */
public interface RecipeService {

	Set<Recipe> getRecipes();

	Recipe findById(Long id);

	RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);

	RecipeCommand findCommandById(Long id);

	void deleteById(Long id);
}
