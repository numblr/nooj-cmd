package net.nooj4nlp.cmd.processing;

import static com.google.common.base.Preconditions.checkNotNull;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

public abstract class NtextConverter {
	private final Language language;

	protected NtextConverter(Language language) {
		this.language = checkNotNull(language);
	}
	
	/**
	 * Converts the given input to a {@link Ntext} object.
	 * 
	 * @param input input string to be converted
	 * 
	 * @return a new {@code Ntext} instance based on the input string
	 */
	public abstract Ntext convert(String input);

	protected final Ntext convert(String bufferValue, String delimiter, String[] xmlTags) {
		Ntext nText = new Ntext(language.isoName, delimiter, xmlTags);
		
		nText.buffer = bufferValue;
		
		return nText;
	}
}