package com.pawel.controllers;

import com.pawel.domain.Recipe;
import com.pawel.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by Pawel on 2017-10-29.
 */
public class IndexControllerTest {

	@Mock
	private RecipeService recipeService;

	@Mock
	private Model model;

	private IndexController indexController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.indexController = new IndexController(this.recipeService);
	}

	@Test
	public void testMockMVC() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

	@Test
	public void getIndexPage() throws Exception {

		//given
		Set<Recipe> recipes = new HashSet<>();
		Recipe firstRecipe = new Recipe();
		firstRecipe.setId(1L);
		Recipe secondRecipe = new Recipe();
		firstRecipe.setId(2L);
		recipes.add(firstRecipe);
		recipes.add(secondRecipe);

		when(recipeService.getRecipes()).thenReturn(recipes);

		ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

		//then
		assertEquals("index", indexController.getIndexPage(this.model));

		Mockito.verify(recipeService, Mockito.times(1)).getRecipes();

		Mockito.verify(model, Mockito.times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
		Set<Recipe> setInController = argumentCaptor.getValue();
		assertEquals(2, setInController.size());

	}

}
