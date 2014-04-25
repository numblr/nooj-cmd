package net.nooj4nlp.cmd.app;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.nooj4nlp.cmd.StaticInitialization;

import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import com.google.common.base.Joiner;

@FixMethodOrder
public class ApplicationTest {
	private static final Path LOG_FILE = Paths.get("test.log");
	
	private String dicts;
	private String grammars;
	
	private Path workingDir;
	private Path test_txt;
	private Path test_xml_txt;
	private Path expected_xml_test;
	private String[] commandLine;

	@Before
	public void setupStaticInitialization() throws IOException {
		StaticInitialization.initialize(LOG_FILE);
	}
	
	@Before
	public void setupFilePaths() throws URISyntaxException {
		dicts = pathsString(Paths.get("test_dict.jnod"));
		grammars = pathsString(Paths.get("aggr_test.nog"), Paths.get("nation_test.nog"));
		workingDir = getPath("/ONooj");
		test_txt = getPath("/ONooj/hu/Projects/test.txt");
		expected_xml_test = getPath("/ONooj/hu/Projects/test.xml.txt.expected");
		
		commandLine = createCommandLine();
	}

	private Path getPath(String path) throws URISyntaxException {
		return Paths.get(new URI(getClass().getResource(path).toString()).getPath());
	}

	@After
	public void removeFiles() throws IOException {
		removeFile(LOG_FILE);
		removeFile(test_xml_txt);
		test_xml_txt = null;
	}
	
	private void removeFile(Path file) throws IOException {
		if (file != null) {
			Files.deleteIfExists(file);
		}
	}
	
	@Test
	public void testApp() throws ParseException, IOException, URISyntaxException {
		new Application().run(NoojOptions.create(commandLine));
		
		test_xml_txt = getPath("/ONooj/hu/Projects/test.xml.txt");
		
		assertTrue(FileUtils.contentEquals(expected_xml_test.toFile(), test_xml_txt.toFile()));
	}
	
	private String[] createCommandLine() {
		String[] commandLine = {
			"--dicts", dicts,
			"--grammars", grammars,
			"--input", pathsString(test_txt),
			"--workingdir", pathsString(workingDir),
			"--language", "hu"};
		
		return commandLine;
	}
	
	private String pathsString(Path... paths) {
		return Joiner.on(",").join(paths);
	}
}
