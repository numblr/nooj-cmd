package net.nooj4nlp.cmd;

import java.io.IOException;

import net.nooj4nlp.cmd.app.StaticInitialization;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.RefObject;

import org.junit.Before;
import org.junit.BeforeClass;

public abstract class NoojTest {
	
	private final Language language;
	
	private Engine engine;

	protected NoojTest(Language language) {
		this.language = language;
	}
	
	@BeforeClass
	public static void initialize() throws IOException {
		StaticInitialization.initialize();
	}
	
	@Before
	public void setupEngine() throws IOException {
		engine = new Engine(new RefObject<Language>(language),
				"", "", "", false, null, false, null);
	}

	protected Engine getEngine() {
		return engine;
	}
}
