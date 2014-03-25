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
import org.junit.Test;

import com.google.common.base.Joiner;

public class ApplicationTest {
	private static final Path LOG_FILE = Paths.get("test.log");
	
	private Path dict;
	private Path grammar;
	private Path propertiesDefinitions;
	private Path putin_txt;
	private Path test_txt;
	private Path putin_xml_txt;
	private Path test_xml_txt;
	private Path expected_xml_putin;
	private Path expected_xml_test;
	private String[] commandLine;

	@Before
	public void setupStaticInitialization() throws IOException {
		StaticInitialization.initialize(LOG_FILE);
	}
	
	@Before
	public void setupFilePaths() throws URISyntaxException {
		dict = getPath("/ONooj/en/Lexical Analysis/_Sample.jnod");
		grammar = getPath("/ONooj/en/Syntactic Analysis/_Date.nog");
		propertiesDefinitions = getPath("/ONooj/en/Lexical Analysis/_properties.def");
		putin_txt = getPath("/ONooj/en/Projects/Putin.txt");
		test_txt = getPath("/ONooj/en/Projects/Test.txt");
		expected_xml_putin = getPath("/ONooj/en/Projects/Putin.xml.txt.expected");
		expected_xml_test = getPath("/ONooj/en/Projects/Test.xml.txt.expected");
		
		commandLine = createCommandLine();
	}

	private Path getPath(String path) throws URISyntaxException {
		return Paths.get(new URI(getClass().getResource(path).toString()).getPath());
	}

	@After
	public void removeFiles() throws IOException {
		removeFile(LOG_FILE);
		removeFile(putin_xml_txt);
		removeFile(test_xml_txt);
		putin_xml_txt = null;
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
		
		putin_xml_txt = getPath("/ONooj/en/Projects/Putin.xml.txt");
		test_xml_txt = getPath("/ONooj/en/Projects/Test.xml.txt");
		
		assertTrue(FileUtils.contentEquals(expected_xml_putin.toFile(), putin_xml_txt.toFile()));
		assertTrue(FileUtils.contentEquals(expected_xml_test.toFile(), test_xml_txt.toFile()));
	}
	
	private String[] createCommandLine() {
		String[] cmd = {
			"--input", pathsString(putin_txt, test_txt),
			"--dicts", pathsString(dict),
			"--grammars", pathsString(grammar),
			"--properties", pathsString(propertiesDefinitions),
			"--delimiter", "\n"};
		
		return cmd;
	}

	private String pathsString(Path... paths) {
		return Joiner.on(",").join(paths);
	}
}
