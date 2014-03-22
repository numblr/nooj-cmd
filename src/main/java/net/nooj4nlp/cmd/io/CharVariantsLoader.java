package net.nooj4nlp.cmd.io;

import java.io.IOException;
import java.nio.file.Path;

import net.nooj4nlp.engine.Language;

public class CharVariantsLoader {
	private final Path charVariants;

	public CharVariantsLoader(Path charVariants) {
		this.charVariants = charVariants;
	}
	
	public void loadInto(Language language) {
		try
		{
			StringBuilder errorMessage = new StringBuilder("");
			if (!language.loadCharacterVariants(charVariants.toAbsolutePath().toString(), errorMessage)) {
				throw new CharVariantsException(charVariants, errorMessage.toString());
			}
		}
		catch (IOException exception)
		{
			throw new CharVariantsException(charVariants, exception.getMessage());
		}
	}
	
	public static class CharVariantsException extends FileException {
		private static final long serialVersionUID = 1L;
		
		CharVariantsException(Path file, String errorMessage) {
			super(file, errorMessage);
		}
	}
}
