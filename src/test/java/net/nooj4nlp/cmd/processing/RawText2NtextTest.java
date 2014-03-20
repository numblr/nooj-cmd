package net.nooj4nlp.cmd.processing;

import static org.junit.Assert.assertEquals;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Test;

public class RawText2NtextTest {
	private static final String DELIMITER = "\n";
	private static final Language ENGLISH = new Language("en");
	private static final String TEXT = "A test string.";
	
	@Test
	public void convertCreatesNtextAndFillsFields() {
		RawText2Ntext converter = new RawText2Ntext(ENGLISH, DELIMITER);
		
		Ntext nText = converter.convert(TEXT);
		
		assertEquals(TEXT, nText.buffer);
		assertEquals(ENGLISH.isoName, nText.Lan.isoName);
		assertEquals(DELIMITER, nText.DelimPattern);
	}
}
