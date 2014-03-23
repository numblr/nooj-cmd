package net.nooj4nlp.cmd.processing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.nooj4nlp.cmd.NoojTestWithResources;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Mft;
import net.nooj4nlp.engine.Ntext;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class SyntacticParserTest extends NoojTestWithResources {
	private static final Language ENGLISH = new Language("en");
	private static final String DELIMITER = "\n";
	
	private static final String RAW_TEXT = "On Monday the sun shines.\nOn Tuesday it rains.";
	
	private static final ArrayList<Object> NTEXT_ANNOTATIONS = Lists.<Object>newArrayList(
			"On,UNKNOWN",
			"Monday,UNKNOWN",
			"the,UNKNOWN",
			"sun,UNKNOWN",
			"shines,UNKNOWN",
			"On,UNKNOWN",
			"Tuesday,UNKNOWN",
			"it,UNKNOWN",
			"rains,UNKNOWN");
	
	private static final ArrayList<Object> EXPECTED_NTEXT_ANNOTATIONS = Lists.<Object>newArrayList(
			"On,UNKNOWN",
			"Monday,UNKNOWN",
			"the,UNKNOWN",
			"sun,UNKNOWN",
			"shines,UNKNOWN",
			"On,UNKNOWN",
			"Tuesday,UNKNOWN",
			"it,UNKNOWN",
			"rains,UNKNOWN",
			"On Monday,SYNTAX,ADV+Date",
			"On Tuesday,SYNTAX,ADV+Date");
	
	@SuppressWarnings("serial")
	private static final HashMap<String, Integer> NTEXT_HUNKNOWNS = new HashMap<String, Integer>() {
		{
			put("On,UNKNOWN",0);
			put("Monday,UNKNOWN", 1);
			put("the,UNKNOWN", 2);
			put("sun,UNKNOWN", 3);
			put("shines,UNKNOWN", 4);
			put("Tuesday,UNKNOWN", 6);
			put("it,UNKNOWN", 7);
			put("rains,UNKNOWN", 8);
		}
	};
	
	@SuppressWarnings("serial")
	private static final HashMap<String, Integer> EXPECTED_NTEXT_HPHRASES = new HashMap<String, Integer>() {
		{
			put("On Monday,SYNTAX,ADV+Date", 9);
			put("On Tuesday,SYNTAX,ADV+Date", 10);
		}
	};
	
	private Ntext nText;

	public SyntacticParserTest() throws IOException {
		super(ENGLISH);
	}

	@Before
	public void setupNtext() {
		Mft mft = new Mft(2);
		int[] tuAddresses = {0, 0, 26};
		int[] tuLengths = {0, 25, 20};
		mft.tuAddresses = tuAddresses;
		mft.tuLengths = tuLengths;
		
		nText = new Ntext(ENGLISH.isoName, DELIMITER, null);
		nText.nbOfDelimiters = 2;
		nText.nbOfDigits = 0;
		nText.nbOfTokens = 11;
		nText.nbOfTextUnits = 2;
		nText.nbOfWords = 9;
		nText.annotations = NTEXT_ANNOTATIONS;
		nText.hUnknowns = NTEXT_HUNKNOWNS;
		nText.mft = mft;
		nText.buffer = RAW_TEXT;
	}
	
	@Test
	public void processFillsNtextFields() {
		//This seems to be not necessary, but it is done in GUI code
		getLinguisticResources().loadInto(nText);
		SyntacticParser syntacticParser = new SyntacticParser(getEngine());

		syntacticParser.process(nText);
		
		assertEquals(EXPECTED_NTEXT_ANNOTATIONS, nText.annotations);
		assertEquals(EXPECTED_NTEXT_HPHRASES, nText.hPhrases);
	}
}