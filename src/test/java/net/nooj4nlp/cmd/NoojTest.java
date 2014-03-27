package net.nooj4nlp.cmd;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.RefObject;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public abstract class NoojTest {
	private static final Path LOG_FILE = Paths.get("test.log");
	
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
		StaticInitialization.initialize(LOG_FILE);
	}
	
	@AfterClass
	public static void deleteLogFile() throws IOException {
		Files.deleteIfExists(LOG_FILE);
	}
	
	@Before
	public void setupEngine() throws IOException, URISyntaxException {
		outputDirectory = tmp.newFolder().toPath();
		engine = new Engine(new RefObject<Language>(language),
				"", getPath("/ONooj"), "",
				false, null, false, null);
	}

	protected final Engine getEngine() {
		return engine;
	}

	protected final Path getOutputDirectory() {
		return outputDirectory;
	}
	
	private String getPath(String pathName) throws URISyntaxException {
		URI pathURI = new URI(getClass().getResource(pathName).toString());
		Path path = Paths.get(pathURI.getPath());
		
		return path.toAbsolutePath().toString();
	}
}
