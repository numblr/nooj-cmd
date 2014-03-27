package net.nooj4nlp.cmd.app;

import java.io.IOException;
import java.util.List;

import net.nooj4nlp.cmd.NoojTest;
import net.nooj4nlp.engine.Language;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CharVariantsTest extends NoojTest {
	private static final Language FRENCH = new Language("fr");
	
	private static final List<String> FRENCH_VARIANTS = Lists.newArrayList(
			"æ", "ae",
			"œ", "oe",
			"ﬁ", "fi",
			"ﬂ", "fl");
	
	public CharVariantsTest() throws IOException {
		super(FRENCH);
	}

	@Test
	public void loadsCharvariantsIntoLanguage() {
		Assert.assertEquals(FRENCH_VARIANTS, getEngine().Lan.chartable);
	}
}
