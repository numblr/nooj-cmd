package net.nooj4nlp.cmd.processing;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Ignore;
import org.junit.Test;

public class Ntext2XmlTest {
	private static final Language ENGLISH = new Language("en");
	
	@Ignore
	@Test
	public void convertsNtextToXml() {
		Ntext ntext = new Ntext(ENGLISH.isoName);
		ntext.buffer = "Test text.";
		int[] textUnits = ntext.mft.tuAddresses;
		int[] textUnitLengths = ntext.mft.tuLengths;
	}
}
