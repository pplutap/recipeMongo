package com.pawel.service;

import com.pawel.commands.RecipeCommand;
import com.pawel.converters.RecipeCommandToRecipe;
import com.pawel.converters.RecipeToRecipeCommand;
import com.pawel.domain.Recipe;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * Created by Pawel on 2017-11-11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {

	private final static String  NEW_DECRIPTION = "New Description";

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private RecipeToRecipeCommand recipeToRecipeCommand;

	@Autowired
	private RecipeCommandToRecipe recipeCommandToRecipe;

	@Test
	@Transactional
	public void testSaveOfDescription() {
		//given
		Set<Recipe> recipes = recipeService.getRecipes();
		Recipe recipe = recipes.iterator().next();
		RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

		//when
		recipeCommand.setDescription(NEW_DECRIPTION);
		RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

		//then
		Assert.assertEquals(NEW_DECRIPTION, savedRecipeCommand.getDescription());
		Assert.assertEquals(recipe.getId(), savedRecipeCommand.getId());
		Assert.assertEquals(recipe.getCategories().size(), savedRecipeCommand.getCategories().size());
	}
}
