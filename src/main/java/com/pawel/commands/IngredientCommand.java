package com.pawel.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Pawel on 2017-11-11.
 */
@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
	private Long id;
	private Long recipeId;
	private String description;
	private BigDecimal amount;
	private UnitOfMeasureCommand uom;
}
