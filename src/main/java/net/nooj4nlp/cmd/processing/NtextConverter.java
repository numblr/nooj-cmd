package net.nooj4nlp.cmd.processing;

import static com.google.common.base.Preconditions.checkNotNull;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

public abstract class NtextConverter {
	private final Language language;

	protected NtextConverter(Language language) {
		this.language = checkNotNull(language);
	}
	
	public abstract Ntext convert(String input);

	protected Ntext convert(String bufferValue, String delimiter, String[] xmlTags) {
		Ntext nText = new Ntext(language.isoName, delimiter, xmlTags);
		
		nText.buffer = bufferValue;
		
		return nText;
	}
}