package net.nooj4nlp.cmd.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nooj4nlp.cmd.io.Encoding.InputType;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class NoojOptionsTest {
	private static final String[] STRING_ARRAY = new String[0];

	private static final Map<String, String> ARGS = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("-d", "test/dir/dict.dic,other/test/dir/tcid.dic");
			put("-g", "test/dir/grammar.nop,other/test/dir/rammarg.nop");
			put("-p", "ONooj/en/Lexical Analysis/properties.def");
			put("-c", "test/dir/charactervariants.txt");
			put("-x", "s,div,h,header");
			put("-a", "date,something");
			put("-f", null);
			put("-i", "input.txt,with space.txt,/absolute/in.txt,relative/input-file.txt");
			put("-l", "en-us");
			put("-s", "\t");
			put("-e", "iso-8859-1");
			put("-t", "pdf");
			put("-m", "/tmp");
		}
	};
	
	private List<String> args;
	
	@Before
	public void setupRequiredArguments() {
		args = Lists.newArrayList(
				"-d", ARGS.get("-d"),
				"-g", ARGS.get("-g"),
				"-p", ARGS.get("-p")
			);
	}
	
	@Test(expected=ParseException.class)
	public void missingMandatoryOptionsThrows() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(STRING_ARRAY);
		
		assertEquals(ARGS.get("-p"), noojOptions.getPropertiesDefinitions().toString());
	}
	
	@Test
	public void optionDictionariesIsParsedToPathList() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		List<Path> expected = Lists.newArrayList(Paths.get("test/dir/dict.dic"),
				Paths.get("other/test/dir/tcid.dic"));
		
		assertEquals(expected, noojOptions.getLexicalResources());
	}
	
	@Test
	public void optionGrammarsIsParsedToPathList() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		List<Path> expected = Lists.newArrayList(Paths.get("test/dir/grammar.nop"),
				Paths.get("other/test/dir/rammarg.nop"));
		
		assertEquals(expected, noojOptions.getSyntacticResources());
	}
	
	@Test
	public void optionInputIsParsedToPathList() throws ParseException {
		args.add("-i");
		args.add(ARGS.get("-i"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		List<Path> expected = Lists.newArrayList(Paths.get("input.txt"),
				Paths.get("with space.txt"),
				Paths.get("/absolute/in.txt"),
				Paths.get("relative/input-file.txt"));
		
		
		assertEquals(expected, noojOptions.getFiles());
	}
	
	@Test
	public void noOptionInputIsParsedToNull() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertNull(noojOptions.getFiles());
	}
	
	@Test
	public void optionPropertiesIsParsedToPath() throws ParseException {
		args.add("-p");
		args.add(ARGS.get("-p"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-p"), noojOptions.getPropertiesDefinitions().toString());
	}

	@Test
	public void optionCharacterVariantsIsParsedToPath() throws ParseException {
		args.add("-c");
		args.add(ARGS.get("-c"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-c"), noojOptions.getCharVariantsFile().toString());
	}
	
	@Test
	public void noOptionCharacterVariantsIsParsedToNull() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertNull(noojOptions.getCharVariantsFile());
	}
	
	@Test
	public void optionXmlTagsIsParsedToStringList() throws ParseException {
		args.add("-x");
		args.add(ARGS.get("-x"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
	
		List<String> expected = Lists.newArrayList("s","div","h","header");
		
		assertEquals(expected, noojOptions.getXmlTags());
	}
	
	@Test
	public void noOptionXmlTagsIsParsedToNull() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertNull(noojOptions.getXmlTags());
	}

	@Test
	public void optionXmlAnnotationsIsParsedToStringList() throws ParseException {
		args.add("-a");
		args.add(ARGS.get("-a"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		List<String> expected = Lists.newArrayList("date","something");
		
		assertEquals(expected, noojOptions.getXmlAnnotations());
	}
	
	@Test
	public void noOptionXmlAnnotationsIsParsedToEmptyList() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(Collections.emptyList(), noojOptions.getXmlAnnotations());
	}

	@Test
	public void optionFilterIsParsedToTrue() throws ParseException {
		args.add("-f");
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertTrue(noojOptions.isFilterXml());
	}

	@Test
	public void noOptionFilterIsParsedToFalse() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertFalse(noojOptions.isFilterXml());
	}

	@Test
	public void optionLanguageIsParsedToLanguage() throws ParseException {
		args.add("-l");
		args.add(ARGS.get("-l"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-l"), noojOptions.getLanguage().isoName);
	}

	@Test
	public void noOptionLanguageIsParsedToEnglish() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals("en", noojOptions.getLanguage().isoName);
	}

	@Test
	public void optionDelimiterIsParsedToString() throws ParseException {
		args.add("-s");
		args.add(ARGS.get("-s"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-s"), noojOptions.getDelimiter());
	}

	@Test
	public void noOptionDelimiterIsParsedToNull() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertNull(noojOptions.getDelimiter());
	}

	@Test
	public void optionFileTypeIsParsedToEncoding() throws ParseException {
		args.add("-t");
		args.add(ARGS.get("-t"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(null, noojOptions.getEncoding().getEncoding());
		assertEquals(InputType.PDF, noojOptions.getEncoding().getInputType());
	}

	@Test
	public void optionEncodingIsParsedToEncoding() throws ParseException {
		args.add("-e");
		args.add(ARGS.get("-e"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-e"), noojOptions.getEncoding().getEncoding());
		assertEquals(InputType.DEFAULT, noojOptions.getEncoding().getInputType());
	}
	
	@Test
	public void bothEncodingOptionsAreParsed() throws ParseException {
		args.add("-t");
		args.add(ARGS.get("-t"));
		args.add("-e");
		args.add(ARGS.get("-e"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-e"), noojOptions.getEncoding().getEncoding());
		assertEquals(InputType.PDF, noojOptions.getEncoding().getInputType());
	}

	@Test
	public void noEncodingOptionsIsParsedToDefaultEncoding() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(null, noojOptions.getEncoding().getEncoding());
		assertEquals(InputType.DEFAULT, noojOptions.getEncoding().getInputType());
	}
	
	@Test
	public void optionTempIsParsedToPath() throws ParseException {
		args.add("-m");
		args.add(ARGS.get("-m"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-m"), noojOptions.getTmpDirectory().toString());
	}

	@Test
	public void noOptionTempIsParsedToDefaultTempPath() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(System.getProperty("java.io.tmpdir"), noojOptions.getTmpDirectory().toString());
	}
	
}
