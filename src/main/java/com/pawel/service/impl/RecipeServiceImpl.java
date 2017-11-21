package com.pawel.service.impl;

import com.pawel.commands.RecipeCommand;
import com.pawel.converters.RecipeCommandToRecipe;
import com.pawel.converters.RecipeToRecipeCommand;
import com.pawel.domain.Recipe;
import com.pawel.exceptions.NotFoundException;
import com.pawel.repositories.RecipeRepository;
import com.pawel.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Pawel on 2017-10-28.
 */
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;
	private final RecipeToRecipeCommand recipeToRecipeCommand;
	private final RecipeCommandToRecipe recipeCommandToRecipe;

	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand,
			RecipeCommandToRecipe recipeCommandToRecipe) {
		this.recipeRepository = recipeRepository;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
	}

	@Override
	public Set<Recipe> getRecipes() {
		log.debug("Getting recipes");
		Set<Recipe> recipes = new HashSet<>();
		recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
		return recipes;
	}

	@Override
	public Recipe findById(Long id) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(id);

		if (!recipeOptional.isPresent()) {
			throw new NotFoundException("Recipe not found. For id value: " + id.toString());
		}

		return recipeOptional.get();
	}

	@Override
	@Transactional
	public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
		Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
		Recipe saved = recipeRepository.save(detachedRecipe);
		log.debug("Saved recipe: " + saved.getId());
		return recipeToRecipeCommand.convert(saved);
	}

	@Override
	@Transactional
	public RecipeCommand findCommandById(Long id) {
		return recipeToRecipeCommand.convert(findById(id));
	}

	@Override
	public void deleteById(Long id) {
		recipeRepository.deleteById(id);
	}
}
