package net.nooj4nlp.cmd.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.engine.Language;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CharVariantsLoaderTest {
	private static final Path CHAR_VARIANTS = Paths.get(LinguisticResourcesTest.class
			.getResource("/charactervariants/charvariants.txt").getPath());
	private static final List<String> FRENCH_VARIANTS = Lists.newArrayList(
			"æ", "ae",
			"œ", "oe",
			"ﬁ", "fi",
			"ﬂ", "fl");
	
	@Test
	public void loadsCharvariantsIntoLanguage() {
		CharVariantsLoader charVariantsLoader = new CharVariantsLoader(CHAR_VARIANTS);
		Language french = new Language("fr");

		charVariantsLoader.loadInto(french);
		
		Assert.assertEquals(FRENCH_VARIANTS, french.chartable);
	}
}
