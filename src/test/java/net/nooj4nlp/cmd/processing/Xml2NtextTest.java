package net.nooj4nlp.cmd.processing;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class Xml2NtextTest {
	private static final Language ENGLISH = new Language("en");
	private static final String XML = "<Node1>Trying to delimit text\nBased on tags</Node1><Node2>\n\nTryout once more</Node2>";
	private static final List<String> XML_NODES = ImmutableList.of("<Node1>", "<Node2>");
	
	@Test
	public void convertCreatesNtextAndFillsFields() {
		Xml2Ntext converter = new Xml2Ntext(ENGLISH, XML_NODES);
		
		Ntext nText = converter.convert(XML);
		
		assertEquals(XML, nText.buffer);
		assertEquals(ENGLISH.isoName, nText.Lan.isoName);
		assertEquals("", nText.DelimPattern);
		assertArrayEquals(XML_NODES.toArray(new String[0]), nText.XmlNodes);
	}
}
