package net.nooj4nlp.cmd.processing;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

public final class RawText2Ntext extends NtextConverter {
	private static final String[] NO_XML_NODES = null;
	
	private final String delimiter;

	public RawText2Ntext(Language language, String delimiter) {
		super(language);
		this.delimiter = delimiter;
	}

	public Ntext convert(String text) {
		return convert(text, delimiter, NO_XML_NODES);
	}
}