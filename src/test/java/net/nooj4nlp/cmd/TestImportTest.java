package net.nooj4nlp.cmd;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestImportTest extends NoojTest {
	private static final File DUMMY_FILE = new File("");
	private static final String DELIMITER = "\n";
	private static final Language ENGLISH = new Language("en");
	
	private TextLoader textLoaderMock;
	
	@Before
	public void setup() {
		textLoaderMock = mock(TextLoader.class);
		when(textLoaderMock.load(Mockito.<File>anyObject()))
				.thenReturn(TEXT);
	}
	
	@Test
	public void fromFileCreatesNtext() {
		TextImport textImport = new TextImport(ENGLISH, DELIMITER, textLoaderMock);
		Ntext ntext = textImport.fromFile(DUMMY_FILE);
		
		assertEquals(ENGLISH.isoName, ntext.getLanguage().isoName);
		assertEquals(2, ntext.nbOfTextUnits);
	}

	private static final String TEXT = "This is the first sentence.\n"
			+ "This is the second sentence, placed on the second line. The third sentence is on the same line as the second.";
}
