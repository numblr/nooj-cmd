package net.nooj4nlp.cmd.io;

import org.junit.Test;

import net.nooj4nlp.cmd.NoojTest;
import net.nooj4nlp.engine.Language;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

public class LinguisticResourcesTest extends NoojTest {
	private static final Language ENGLISH = new Language("en");

	public LinguisticResourcesTest() {
		super(ENGLISH);
	}

	@Test
	public void setupLexicalResources() {
		ListMultimap<String, String> lexicalResources =
				ImmutableListMultimap.<String, String> of(ENGLISH.isoName,
						"resources/Linguistic/01_Sample-s.dic");
		LinguisticResources linguisticResources = new LinguisticResources(
				lexicalResources, ImmutableListMultimap.<String, String> of());
		linguisticResources.loadInto(getEngine());
	}

}
