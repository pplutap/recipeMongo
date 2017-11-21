package com.pawel.controllers;

import com.pawel.commands.RecipeCommand;
import com.pawel.domain.Recipe;
import com.pawel.exceptions.NotFoundException;
import com.pawel.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by Pawel on 2017-11-01.
 */
public class RecipeControllerTest {

	@Mock
	private RecipeService service;

	private RecipeController controller;
	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.controller = new RecipeController(service);
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(controller)
				.setControllerAdvice(new ControllerExceptionHandler())
				.build();
	}

	@Test
	public void testGetRecipe() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		when(service.findById(anyLong())).thenReturn(recipe);

		mockMvc.perform(get("/recipe/1/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/show"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testGetRecipeNotFound() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		when(service.findById(anyLong())).thenThrow(NotFoundException.class);

		mockMvc.perform(get("/recipe/1/show"))
				.andExpect(status().isNotFound())
				.andExpect(view().name("404error"));
	}

	@Test
	public void testGetRecipeNumberFormatException() throws Exception {
		mockMvc.perform(get("/recipe/asdf/show"))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("400error"));
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {
		RecipeCommand command = new RecipeCommand();

		mockMvc.perform(get("/recipe/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId(2L);

		when(service.saveRecipeCommand(any())).thenReturn(command);

		mockMvc.perform(post("/recipe")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "")
				.param("description", "some description")
				.param("directions", "some directions")
		)
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/show"));
	}

	@Test
	public void testGetUpdateView() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId(2L);

		when(service.findCommandById(anyLong())).thenReturn(command);

		mockMvc.perform(get("/recipe/1/update"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testDeleteAction() throws Exception {
		mockMvc.perform(get("/recipe/1/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}

	@Test
	public void testPostNewRecipeFormValidationFail() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId(2L);

		when(service.saveRecipeCommand(any())).thenReturn(command);

		mockMvc.perform(post("/recipe")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("id", "")
			.param("directions", "some directions")
			.param("cookTime", "3000")
		)
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("recipe"))
			.andExpect(view().name("recipe/recipeform"));
	}
}
