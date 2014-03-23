package net.nooj4nlp.cmd.io;

import java.io.IOException;
import java.nio.file.Path;

import net.nooj4nlp.engine.Language;

public class CharVariantsLoader {
	private final Path characterVariants;

	public CharVariantsLoader(Path charVariants) {
		this.characterVariants = charVariants;
	}
	
	public void loadInto(Language language) {
		try
		{
			StringBuilder errorMessage = new StringBuilder("");
			String filePath = characterVariants.toAbsolutePath().toString();
			if (!language.loadCharacterVariants(filePath, errorMessage)) {
				throw new CharVariantsException(characterVariants, errorMessage.toString());
			}
		}
		catch (IOException exception)
		{
			throw new CharVariantsException(characterVariants, exception.getMessage());
		}
	}
	
	public static class CharVariantsException extends FileException {
		private static final long serialVersionUID = 1L;
		
		private CharVariantsException(Path file, String errorMessage) {
			super(file, errorMessage);
		}
	}
}
