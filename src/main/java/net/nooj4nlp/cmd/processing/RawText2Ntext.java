package net.nooj4nlp.cmd.processing;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

public class RawText2Ntext extends NtextImporter {
	private static final String[] EMPTY_XML_NODES = new String[0];
	
	private final String delimiter;

	public RawText2Ntext(Language language, String delimiter) {
		super(language);
		this.delimiter = delimiter;
	}

	public Ntext convert(String text) {
		return convert(text, delimiter, EMPTY_XML_NODES);
	}
}