package com.pawel.service;

import com.pawel.converters.RecipeCommandToRecipe;
import com.pawel.converters.RecipeToRecipeCommand;
import com.pawel.domain.Recipe;
import com.pawel.exceptions.NotFoundException;
import com.pawel.repositories.RecipeRepository;
import com.pawel.service.impl.RecipeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel on 2017-10-29.
 */
public class RecipeServiceTest {

	private RecipeService recipeService;
	
	@Mock
	private RecipeRepository recipeRepository;

	@Mock
	private RecipeCommandToRecipe recipeCommandToRecipe;

	@Mock
	private RecipeToRecipeCommand recipeToRecipeCommand;
	
	@Test
	public void getRecipes() throws Exception {
		Recipe recipe = new Recipe();
		Set<Recipe> recipesData = new HashSet<>();
		recipesData.add(recipe);

		when(recipeService.getRecipes()).thenReturn(recipesData);

		Set<Recipe> recipes = recipeService.getRecipes();

		assertEquals(1, recipes.size());

		verify(recipeRepository, times(1)).findAll();
	}

	@Test
	public void getRecipeById() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		Optional<Recipe> recipeOptional = Optional.of(recipe);

		when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

		Recipe returnedRecipe = recipeService.findById(1L);

		assertNotNull("Null recipe returned", returnedRecipe);
		verify(recipeRepository, times(1)).findById(anyLong());
		verify(recipeRepository, never()).findAll();
	}

	@Test(expected = NotFoundException.class)
	public void getRecipeByIdTestNotFound() throws Exception {
		Optional<Recipe> recipeOptional = Optional.empty();
		when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
		Recipe result = recipeService.findById(1L);
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		recipeService = new RecipeServiceImpl(recipeRepository, recipeToRecipeCommand, recipeCommandToRecipe);
	}

	@Test
	public void testDeleteById() throws Exception {
		recipeService.deleteById(2L);

		verify(recipeRepository, times(1)).deleteById(anyLong());
	}
}
