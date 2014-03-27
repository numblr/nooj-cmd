package net.nooj4nlp.cmd.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import net.nooj4nlp.cmd.NoojTest;
import net.nooj4nlp.cmd.processing.TextDelimiter.DelimiterException;
import net.nooj4nlp.cmd.processing.TextDelimiter.LimitExeededException;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.UnsignedShort;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class TextDelimiterTest extends NoojTest {
	private static final String DELIMITER = "\n";
	private static final Language ENGLISH = new Language("en");
	private static final List<String> XML_NODES = ImmutableList.of("<Node1>", "<Node2>");
	
	private static final String RAW_TEXT = "This is the first sentence.\n"
			+ "This is the second sentence, placed on the second line. The third sentence is on the same line as the second.";
	
	private static final String XML = "<Node1>Trying to delimit text\nBased on tags</Node1><Node2>\n\nTryout once more</Node2>";
	
	public TextDelimiterTest() throws IOException {
		super(ENGLISH);
	}
	
	@Test
	public void delimitTextSetsTextUnitsAndCreatesMftObject() {
		TextDelimiter textDelimiter = new TextDelimiter(getEngine());
		
		Ntext nText = new Ntext(ENGLISH.isoName, DELIMITER, null);
		nText.buffer = RAW_TEXT;
		textDelimiter.process(nText);
		
		assertEquals(2, nText.nbOfTextUnits);
		assertNotNull(nText.mft);
	}
	
	@Test
	public void delimitTextWithRegexSetsTextUnitsAndCreatesMftObject() {
		TextDelimiter textDelimiter = new TextDelimiter(getEngine());
		
		Ntext nText = new Ntext(ENGLISH.isoName, "\\d", null);
		nText.buffer = "First1Second2Third3Fourth";
		textDelimiter.process(nText);
		
		assertEquals(4, nText.nbOfTextUnits);
		assertNotNull(nText.mft);
	}
	
	@Test
	public void delimitXmlSetsTextUnitsAndCreatesMftObject() {
		TextDelimiter textDelimiter = new TextDelimiter(getEngine());
		
		Ntext nText = new Ntext(ENGLISH.isoName, DELIMITER, XML_NODES.toArray(new String[0]));
		nText.buffer = XML;
		textDelimiter.process(nText);
		
		assertEquals(2, nText.nbOfTextUnits);
		assertNotNull(nText.mft);
	}
	
	@Test
	public void delimitXmlWithoutXmlNodesIgnoresTags() {
		TextDelimiter textDelimiter = new TextDelimiter(getEngine());
		
		Ntext nText = new Ntext(ENGLISH.isoName, DELIMITER, null);
		nText.buffer = XML;
		textDelimiter.process(nText);
		
		assertEquals(3, nText.nbOfTextUnits);
		assertNotNull(nText.mft);
	}
	
	@Test
	public void delimitXmlWithEmptyXmlNodesThrows() {
		thrown.expect(DelimiterException.class);
		thrown.expectMessage("no XML tag");

		TextDelimiter textDelimiter = new TextDelimiter(getEngine());
		
		Ntext nText = new Ntext(ENGLISH.isoName, DELIMITER, new String[0]);
		nText.buffer = XML;
		textDelimiter.process(nText);
	}

	@Test
	public void longUndelimitedTextThrows() {
		thrown.expect(LimitExeededException.class);
		
		TextDelimiter textDelimiter = new TextDelimiter(getEngine());
		
		Ntext nText = new Ntext(ENGLISH.isoName, "", null);
		nText.buffer = StringUtils.repeat("a", UnsignedShort.MAX_VALUE + 1);
		textDelimiter.process(nText);
	}
}
