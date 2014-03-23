package net.nooj4nlp.cmd;

import java.io.IOException;
import java.nio.file.Path;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.RefObject;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public abstract class NoojTest {
	@Rule public ExpectedException thrown = ExpectedException.none();
	@Rule public TemporaryFolder tmp = new TemporaryFolder();
	
	private final Language language;
	private Path outputDirectory;
	
	private Engine engine;
	
	protected NoojTest(Language language) throws IOException {
		this.language = language;
	}
	
	@BeforeClass
	public static void initialize() throws IOException {
		StaticInitialization.initialize();
	}
	
	@Before
	public void setupEngine() throws IOException {
		outputDirectory = tmp.newFolder().toPath();
		engine = new Engine(new RefObject<Language>(language),
				"", "", "",
				false, null, false, null);
	}

	protected final Engine getEngine() {
		return engine;
	}

	protected final Path getOutputDirectory() {
		return outputDirectory;
	}
}
