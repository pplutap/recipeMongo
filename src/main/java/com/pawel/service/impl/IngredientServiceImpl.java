package com.pawel.service.impl;

import com.pawel.commands.IngredientCommand;
import com.pawel.converters.IngredientCommandToIngredient;
import com.pawel.converters.IngredientToIngredientCommand;
import com.pawel.domain.Ingredient;
import com.pawel.domain.Recipe;
import com.pawel.repositories.RecipeRepository;
import com.pawel.repositories.UnitOfMeasureRepository;
import com.pawel.service.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final RecipeRepository recipeRepository;
	private UnitOfMeasureRepository unitOfMeasureRepository;

	public IngredientServiceImpl(IngredientCommandToIngredient ingredientCommandToIngredient,
			IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository,
			UnitOfMeasureRepository unitOfMeasureRepository) {
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.recipeRepository = recipeRepository;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if (!recipeOptional.isPresent()) {
			log.error("Recipe not found for ingredientId: " + recipeId);
		}

		Recipe recipe = recipeOptional.get();

		Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients()
				.stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientId))
				.map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
				.findFirst();

		if (!ingredientCommandOptional.isPresent()) {
			log.error("Ingredient id not found: " + ingredientId);
		}

		return ingredientCommandOptional.get();
	}

	@Override
	@Transactional
	public IngredientCommand saveIngredient(IngredientCommand command) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

		if (!recipeOptional.isPresent()) {

			log.error("Recipe not found for id: " + command.getRecipeId());
			return new IngredientCommand();
		} else {
			Recipe recipe = recipeOptional.get();

			Optional<Ingredient> ingredientOptional =
					recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();

			if (ingredientOptional.isPresent()) {
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
						.orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
			} else {
				//add new Ingredient
				Ingredient ingredient = ingredientCommandToIngredient.convert(command);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
			}

			Recipe savedRecipe = recipeRepository.save(recipe);

			Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
					.stream()
					.filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
					.findFirst();

			//check by description
			if (!savedIngredientOptional.isPresent()) {
				//not totally safe... But best guess
				savedIngredientOptional = savedRecipe.getIngredients()
						.stream()
						.filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
						.filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
						.filter(recipeIngredients -> recipeIngredients.getUom()
								.getId()
								.equals(command.getUom().getId()))
						.findFirst();
			}

			//to do check for fail
			return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
		}
	}

	@Override
	public void deleteById(Long recipeId, Long ingredientId) {
		log.debug("Deleting ingredient, recipeId: " + recipeId + ", ingredientId: " + ingredientId);

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		if (recipeOptional.isPresent()) {
			Recipe recipe = recipeOptional.get();
			log.debug("Found recipe: " + recipeId);
			Optional<Ingredient> ingredientOptional = recipe
					.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equals(ingredientId))
					.findFirst();
			if (ingredientOptional.isPresent()) {
				log.debug("Found ingredient: " + ingredientId);
				Ingredient ingredientToDelete = ingredientOptional.get();
				ingredientToDelete.setRecipe(null);
				recipe.getIngredients().remove(ingredientToDelete);
				recipeRepository.save(recipe);
			}
		} else {
			log.debug("Recipe id: " + recipeId + " not found");
		}
	}
}
