package net.nooj4nlp.cmd.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import net.nooj4nlp.engine.Constants;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Paths;

public class CharVariantsLoader {
	private final Language language;

	public CharVariantsLoader(Language language) {
		this.language = language;
	}
	
	public void load() {
		File charVariants = resolveCharVariants();
		loadCharVariants(charVariants);
	}
	
	public void loadCharVariants(File charVariants) {
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
	
	private File resolveCharVariants() {
		File charVariants = resolveCharVariantsPath(Constants.CHAR_VARIANTS_PATH);
		
		return charVariants.exists() ?
				charVariants :
				resolveCharVariantsPath(Constants.CHAR_VARIANTS_SUFFIX_PATH);
	}
	
	private File resolveCharVariantsPath(String charVariantsPath) {
		Path path = java.nio.file.Paths.get(Paths.docDir,
				language.isoName,
				Constants.LEXICAL_ANALYSIS_PATH,
				charVariantsPath);
		
		return path.toFile();
	}
	
	public static class CharVariantsException extends FileException {
		private static final long serialVersionUID = 1L;
		
		CharVariantsException(File file, String errorMessage) {
			super(file, errorMessage);
		}
	}
}
