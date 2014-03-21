package net.nooj4nlp.cmd.processing;

import net.nooj4nlp.cmd.NoojTest;
import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

public class LexicalAnalyzerTest extends NoojTest {
	private static final Language ENGLISH = new Language("en");
	
	public LexicalAnalyzerTest() {
		super(ENGLISH);
	}

	@Ignore
	@Before
	public void setupLexicalResources(){
//		ListMultimap<String, String> lexicalResources = ImmutableListMultimap.<String, String>of(ENGLISH.isoName, "");
//		LinguisticResources linguisticResources = new LinguisticResources(lexicalResources, ImmutableListMultimap.<String, String>of());
//		linguisticResources.loadInto(getEngine());
	}
	
	@Ignore
	@Test
	public void test() {
		LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(getEngine());
		lexicalAnalyzer.process(new Ntext(ENGLISH.isoName));
	}
}