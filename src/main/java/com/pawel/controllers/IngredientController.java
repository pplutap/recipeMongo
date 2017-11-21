package com.pawel.controllers;

import com.pawel.commands.IngredientCommand;
import com.pawel.commands.RecipeCommand;
import com.pawel.commands.UnitOfMeasureCommand;
import com.pawel.service.IngredientService;
import com.pawel.service.RecipeService;
import com.pawel.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class IngredientController {

	private RecipeService recipeService;
	private IngredientService ingredientService;
	private UnitOfMeasureService unitOfMeasureService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@GetMapping("recipe/{id}/ingredients")
	public String showIngredients(@PathVariable String id, Model model) {
		log.debug("Getting ingredient list for recipe id: " + id);
		model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
		return "recipe/ingredient/list";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{id}/show")
	public String showIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
		log.debug("Getting ingredient for recipe id: " + recipeId + " , ingredient id: " + id);
		IngredientCommand command = ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id));
		model.addAttribute("ingredient", command);
		return "recipe/ingredient/show";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{id}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId,
			@PathVariable String id, Model model) {
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
		model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUoms());
		return "recipe/ingredient/ingredientform";
	}

	@PostMapping("recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
		IngredientCommand savedCommand = ingredientService.saveIngredient(command);

		log.debug("Saved recipe id: " + command.getRecipeId());
		log.debug("Saved ingredient id: " + command.getId());

		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}

	@GetMapping("recipe/{recipeId}/ingredient/new")
	public String newRecipe(@PathVariable String recipeId, Model model) {

		RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(Long.valueOf(recipeId));
		model.addAttribute("ingredient", ingredientCommand);

		ingredientCommand.setUom(new UnitOfMeasureCommand());

		model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUoms());
		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
	public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
		log.debug("Deleting ingredient id: " + id);
		ingredientService.deleteById(Long.valueOf(recipeId), Long.valueOf(id));
		return "redirect:/recipe/" + recipeId + "/ingredients";
	}
}
