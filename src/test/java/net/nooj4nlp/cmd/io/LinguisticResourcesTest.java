package net.nooj4nlp.cmd.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.NoojTest;
import net.nooj4nlp.engine.Language;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class LinguisticResourcesTest extends NoojTest {
	private static final Language ENGLISH = new Language("en");

	public LinguisticResourcesTest() {
		super(ENGLISH);
	}

	@Test
	public void setupLexicalResources() throws IOException {
		Path dict = Paths.get(this.getClass().getResource("/Linguistic/_Sample-s.dic").getPath());
		Path propertiesDefinitions = Paths.get(this.getClass().getResource("/Linguistic/_properties.def").getPath());
		List<Path> lexicalResources = ImmutableList.of(dict);
		List<Path> syntacticResources = ImmutableList.of();
		LinguisticResources linguisticResources = new LinguisticResources(lexicalResources,
				syntacticResources,
				propertiesDefinitions,
				ENGLISH,
				getProjectDirectory());

		linguisticResources.loadInto(getEngine());
	}

}