package com.pawel.service;

import com.pawel.commands.IngredientCommand;

public interface IngredientService {
	IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long id);

	IngredientCommand saveIngredient(IngredientCommand command);

	void deleteById(Long recipeId, Long ingredientId);
}
