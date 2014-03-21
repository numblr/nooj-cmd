package net.nooj4nlp.cmd.processing;

import java.io.IOException;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Test;

public class SyntacticParserTest extends NoojTestWithResources {
	private static final Language ENGLISH = new Language("en");
	private static final String DELIMITER = "\n";
	
	private static final String RAW_TEXT = "This is the first sentence.\n"
			+ "This is the second sentence, placed on line 2. The third sentence is on the same line as the second.";

	public SyntacticParserTest() throws IOException {
		super(ENGLISH);
	}

	@Test
	public void processDoesNotThrow() {
		Ntext nText = new Ntext(ENGLISH.isoName, DELIMITER, null);
		nText.buffer = RAW_TEXT;
		
		new TextDelimiter(getEngine()).process(nText);
		getLinguisticResources().loadInto(nText);
		LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(getEngine());
		lexicalAnalyzer.process(nText);
		
		SyntacticParser syntacticParser = new SyntacticParser(getEngine());
		syntacticParser.process(nText);
	}
}