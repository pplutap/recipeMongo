package com.pawel.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Pawel on 2017-11-11.
 */
@Getter
@Setter
@NoArgsConstructor
public class NotesCommand {
	private Long id;
	private String recipeNotes;
}
