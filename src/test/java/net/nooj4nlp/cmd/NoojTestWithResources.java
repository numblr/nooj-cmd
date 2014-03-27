package net.nooj4nlp.cmd;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.engine.Language;

import org.junit.Before;

import com.google.common.collect.ImmutableList;

public class NoojTestWithResources extends NoojTest {
	private static final Path DICT = Paths.get("_Sample.jnod");
	private static final Path GRAMMAR = Paths.get("_Date.nog");
	
	private LinguisticResources linguisticResources;

	protected NoojTestWithResources(Language language) throws IOException {
		super(language);
	}

	@Before
	public void setupLinguisticResources(){
		List<Path> lexicalResources = ImmutableList.of(DICT);
		List<Path> syntacticResources = ImmutableList.of(GRAMMAR);
		linguisticResources = new LinguisticResources(lexicalResources,
				syntacticResources);
		
		getLinguisticResources().loadInto(getEngine());
	}

	protected final LinguisticResources getLinguisticResources() {
		return linguisticResources;
	}
}