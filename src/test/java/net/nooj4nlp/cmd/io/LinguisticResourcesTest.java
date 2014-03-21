package net.nooj4nlp.cmd.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.NoojTest;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class LinguisticResourcesTest extends NoojTest {
	private static final Language ENGLISH = new Language("en");
	
	private static final Path DICT = Paths.get(LinguisticResourcesTest.class
			.getResource("/Linguistic/_Sample-s.dic").getPath());
	private static final Path GRAMMAR = Paths.get(LinguisticResourcesTest.class
			.getResource("/Linguistic/_Sample-s.dic").getPath());
	private static final Path PROPERTIES_DEFINITIONS = Paths.get(LinguisticResourcesTest.class
			.getResource("/Linguistic/_properties.def").getPath());

	public LinguisticResourcesTest() {
		super(ENGLISH);
	}

	@Test
	public void loadIntoEngineDoesNotThrow() {
		List<Path> lexicalResources = ImmutableList.of(DICT);
		List<Path> syntacticResources = ImmutableList.of(GRAMMAR);
		LinguisticResources linguisticResources = new LinguisticResources(lexicalResources,
				syntacticResources,
				PROPERTIES_DEFINITIONS,
				getProjectDirectory());

		linguisticResources.loadInto(getEngine());
	}

	@Test
	public void loadIntoNtextDoesNotThrow() {
		List<Path> lexicalResources = ImmutableList.of(DICT);
		List<Path> syntacticResources = ImmutableList.of(GRAMMAR);
		LinguisticResources linguisticResources = new LinguisticResources(lexicalResources,
				syntacticResources,
				PROPERTIES_DEFINITIONS,
				getProjectDirectory());
		
		linguisticResources.loadInto(new Ntext(ENGLISH.isoName));
	}
}