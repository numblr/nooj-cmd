package net.nooj4nlp.cmd.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import net.nooj4nlp.cmd.NoojTestWithResources;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Test;

public class LinguisticResourcesTest extends NoojTestWithResources {
	private static final Language ENGLISH = new Language("en");
	
	public LinguisticResourcesTest() throws IOException {
		super(ENGLISH);
	}
	
	@Test
	public void loadIntoEngineDoesNotThrow() {
		getLinguisticResources().loadInto(getEngine());
	}

	@Test
	public void loadIntoNtextDoesNotThrow() {
		getLinguisticResources().loadInto(new Ntext(ENGLISH.isoName));
	}
	
	@Test
	public void loadIntoEngineCleansUpTemporaryFiles() {
		getLinguisticResources().loadInto(getEngine());
		
		assertEquals(0, getOutputDirectory().toFile().list().length);
	}
}