package net.nooj4nlp.cmd.processing;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

public abstract class NtextImporter {
	private final Language language;

	protected NtextImporter(Language language) {
		this.language = language;
	}
	
	public abstract Ntext convert(String input);

	protected Ntext convert(String bufferValue, String delimiter, String[] xmlTags) {
		Ntext nText = new Ntext(language.isoName, delimiter, xmlTags);
		
		nText.buffer = bufferValue;
		
		return nText;
	}
}