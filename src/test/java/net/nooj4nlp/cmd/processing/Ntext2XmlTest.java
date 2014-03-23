package net.nooj4nlp.cmd.processing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Mft;
import net.nooj4nlp.engine.Ntext;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Ntext2XmlTest {
	private static final Language ENGLISH = new Language("en");
	private static final String DELIMITER = "";
	
	private static final ImmutableList<String> XML_ANNOTATIONS = ImmutableList.of("<SYNTAX>");
	
	private static final ArrayList<Object> NTEXT_ANNOTATIONS = Lists.<Object>newArrayList(
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
	private static final HashMap<String, Integer> NTEXT_HPHRASES = new HashMap<String, Integer>() {
		{
			put("On Monday,SYNTAX,ADV+Date", 9);
			put("On Tuesday,SYNTAX,ADV+Date", 10);
		}
	};
	
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
	
	private Ntext nText;
	
	@Before
	public void setupNtext() {
		Mft mft = new Mft(1);
		mft.addTransition(1, 0.0, 9, 9.0);
		mft.addTransition(1, 26.0, 10, 36.0);
		int[] tuAddresses = {0, 0};
		int[] tuLengths = {0, 46};
		mft.tuAddresses = tuAddresses;
		mft.tuLengths = tuLengths;
		
		nText = new Ntext(ENGLISH.isoName, DELIMITER, null);
		nText.nbOfDigits = 0;
		nText.nbOfTokens = 10;
		nText.nbOfTextUnits = 1;
		nText.nbOfWords = 9;
		nText.annotations = NTEXT_ANNOTATIONS;
		nText.hUnknowns = NTEXT_HUNKNOWNS;
		nText.hPhrases = NTEXT_HPHRASES;
		nText.hLexemes = Maps.newHashMap();
		nText.mft = mft;
		nText.buffer = "On Monday the sun shines.\nOn Tuesday it rains.";
	}
	
	@Test
	public void convertsNtextToXml() {
		Ntext2Xml ntext2Xml = new Ntext2Xml(XML_ANNOTATIONS, ENGLISH, false);
		
		String expected = "<ADV TYPE=\"Date\">On Monday</ADV> the sun shines.\n" +
				"<ADV TYPE=\"Date\">On Tuesday</ADV> it rains.";

		assertEquals(expected, ntext2Xml.convert(nText));
	}
	
	@Test
	public void withFilterConvertsToAnnotationsOnly() {
		Ntext2Xml ntext2Xml = new Ntext2Xml(XML_ANNOTATIONS, ENGLISH, true);
		
		String expected = "<ADV TYPE=\"Date\"></ADV>" +
				"<ADV TYPE=\"Date\"></ADV>";
		
		assertEquals(expected, ntext2Xml.convert(nText));
	}
}