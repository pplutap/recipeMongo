package com.pawel.converters;

import com.pawel.commands.NotesCommand;
import com.pawel.domain.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Created by Pawel on 2017-11-11.
 */
@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {

	@Synchronized
	@Nullable
	@Override
	public NotesCommand convert(Notes source) {
		if (source == null) {
			return null;
		}

		final NotesCommand notesCommand = new NotesCommand();
		notesCommand.setId(source.getId());
		notesCommand.setRecipeNotes(source.getRecipeNotes());
		return notesCommand;
	}
}
