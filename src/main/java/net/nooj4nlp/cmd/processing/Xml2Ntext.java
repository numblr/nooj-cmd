package net.nooj4nlp.cmd.processing;

import java.util.Collection;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

abstract class Xml2Ntext extends NtextImporter {
	private static final String EMPTY_DELIMITER = "";
	
	private final Collection<String> xmlTags;

	public Xml2Ntext(Language language, Collection<String> xmlTags) {
		super(language);
		this.xmlTags = xmlTags;
	}

	public Ntext convert(String xml) {
		return convert(xml, EMPTY_DELIMITER, xmlTags.toArray(new String[0]));
	}
}