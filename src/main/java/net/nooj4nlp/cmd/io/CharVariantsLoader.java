package net.nooj4nlp.cmd.io;

import java.io.File;
import java.io.IOException;

import net.nooj4nlp.engine.Language;

public class CharVariantsLoader {
	private final File charVariants;

	public CharVariantsLoader(File charVariants) {
		this.charVariants = charVariants;
	}
	
	public void loadInto(Language language) {
		try
		{
			StringBuilder errorMessage = new StringBuilder("");
			if (!language.loadCharacterVariants(charVariants.getAbsolutePath(), errorMessage)) {
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
		
		CharVariantsException(File file, String errorMessage) {
			super(file, errorMessage);
		}
	}
}
