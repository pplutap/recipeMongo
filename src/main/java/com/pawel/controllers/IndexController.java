package com.pawel.controllers;

import com.pawel.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Pawel on 2017-10-24.
 */
@Slf4j
@Controller
public class IndexController {

	private final RecipeService recipeService;

	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping({"", "/", "index"})
	public String getIndexPage(Model model) {
		model.addAttribute("recipes", recipeService.getRecipes());
		log.debug("recipes added to model in IndexController");
		return "index";
	}
}
