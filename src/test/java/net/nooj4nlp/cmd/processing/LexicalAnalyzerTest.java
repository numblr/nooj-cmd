package net.nooj4nlp.cmd.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import net.nooj4nlp.cmd.NoojTestWithResources;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Mft;
import net.nooj4nlp.engine.Ntext;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class LexicalAnalyzerTest extends NoojTestWithResources {
	private static final Language ENGLISH = new Language("en");
	private static final String DELIMITER = "\n";
	
	private static final String RAW_TEXT = "This is the first sentence.\n"
			+ "This is the second sentence, placed on line 2. The third sentence is on the same line as the second.";
	
	private static final List<Object> EXPECTED_ANNOTATIONS = Lists.<Object>newArrayList(
			"This,UNKNOWN",
			"is,be,V+FLX=BE+Tense=PR+Nb=s+Pers=3+Aux",
			"the,UNKNOWN",
			"first,UNKNOWN",
			"sentence,UNKNOWN",
			"This,UNKNOWN",
			"the,UNKNOWN",
			"second,UNKNOWN",
			"sentence,UNKNOWN",
			"placed,UNKNOWN",
			"on,UNKNOWN",
			"line,UNKNOWN",
			"The,UNKNOWN",
			"third,UNKNOWN",
			"sentence,UNKNOWN",
			"on,UNKNOWN",
			"the,UNKNOWN",
			"same,UNKNOWN",
			"line,UNKNOWN",
			"as,UNKNOWN",
			"the,UNKNOWN",
			"second,UNKNOWN");
	
	private Ntext nText;

	public LexicalAnalyzerTest() throws IOException {
		super(ENGLISH);
	}

	@Before
	public void setupNtext() {
		Mft mft = new Mft(2);
		int[] tuAddresses = {0, 0, 28};
		int[] tuLengths = {0, 27, 100};
		mft.tuAddresses = tuAddresses;
		mft.tuLengths = tuLengths;
		
		nText = new Ntext(ENGLISH.isoName, DELIMITER, null);
		nText.nbOfTextUnits = 2;
		nText.annotations = Lists.newArrayList();
		nText.mft = mft;
		nText.buffer = RAW_TEXT;
	}
	
	@Test
	public void processSetsNtextFields() {
		//This seems to be not necessary, but it is done in GUI code
		getLinguisticResources().loadInto(nText);
		
		LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(getEngine());
		lexicalAnalyzer.process(nText);
		
		assertEquals(29, nText.nbOfTokens);
		assertEquals(1, nText.nbOfDigits);
		assertEquals(4, nText.nbOfDelimiters);
		assertTrue(0 < nText.annotations.size());
		assertEquals(EXPECTED_ANNOTATIONS, nText.annotations);
	}
}