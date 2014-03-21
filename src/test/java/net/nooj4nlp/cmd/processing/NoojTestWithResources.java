package net.nooj4nlp.cmd.processing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.NoojTest;
import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.cmd.io.LinguisticResourcesTest;
import net.nooj4nlp.engine.Language;

import org.junit.Before;

import com.google.common.collect.ImmutableList;

public class NoojTestWithResources extends NoojTest {
	private static final Path DICT = Paths.get(LinguisticResourcesTest.class
			.getResource("/Linguistic/_Sample-s.dic").getPath());
	private static final Path GRAMMAR = Paths.get(LinguisticResourcesTest.class
			.getResource("/Linguistic/_Sample-s.dic").getPath());
	private static final Path PROPERTIES_DEFINITIONS = Paths.get(LinguisticResourcesTest.class
			.getResource("/Linguistic/_properties.def").getPath());
	
	private LinguisticResources linguisticResources;

	protected NoojTestWithResources(Language language) throws IOException {
		super(language);
	}

	@Before
	public void setupLinguisticResources(){
		List<Path> lexicalResources = ImmutableList.of(DICT);
		List<Path> syntacticResources = ImmutableList.of(GRAMMAR);
		linguisticResources = new LinguisticResources(lexicalResources,
				syntacticResources,
				PROPERTIES_DEFINITIONS,
				getOutputDirectory());
		
		getLinguisticResources().loadInto(getEngine());
	}

	protected final LinguisticResources getLinguisticResources() {
		return linguisticResources;
	}
}