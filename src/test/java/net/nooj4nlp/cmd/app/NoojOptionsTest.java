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

import net.nooj4nlp.cmd.io.Encoding.FileType;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class NoojOptionsTest {
	private static final String[] STRING_ARRAY = new String[0];

	@SuppressWarnings("serial")
	private static final Map<String, String> ARGS = new HashMap<String, String>() {
		{
			put("-i", "input.txt,with space.txt,/absolute/in.txt,relative/input-file.txt");
			put("-d", "test/dir/dict.dic,other/test/dir/tcid.dic");
			put("-g", "test/dir/grammar.nop,other/test/dir/rammarg.nop");
			put("-w", "ONooj");
			put("-p", "ONooj/en/Lexical Analysis/properties.def");
			put("-x", "s,div,h,header");
			put("-f", null);
			put("-l", "en-us");
			put("-s", "\t");
			put("-e", "iso-8859-1");
			put("-t", "raw_text");
			put("-r", "log/noojcmd.log");
		}
	};
	
	private List<String> args;
	
	@Before
	public void setupRequiredArguments() {
		args = Lists.newArrayList("-i", ARGS.get("-i"), "-g", ARGS.get("-g"));
	}
	
	@Test(expected=ParseException.class)
	public void missingMandatoryOptionsThrows() throws ParseException {
		NoojOptions.create(STRING_ARRAY);
	}
	
	@Test
	public void optionDictionariesIsParsedToPathList() throws ParseException {
		args.add("-d");
		args.add(ARGS.get("-d"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-d"), Joiner.on(",").join(noojOptions.getLexicalResources()));
	}
	
	@Test
	public void noOoptionDictionariesIsParsedToEmptyList() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(Collections.emptyList(), noojOptions.getLexicalResources());
	}
	
	@Test
	public void optionGrammarsIsParsedToPathList() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-g"), Joiner.on(",").join(noojOptions.getSyntacticResources()));
	}
	
	@Test
	public void noOptionGrammarsIsParsedToEmptyList() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(
				Lists.newArrayList("-i", ARGS.get("-i"), "-d", ARGS.get("-d"))
				.toArray(STRING_ARRAY));
		
		assertEquals(Collections.emptyList(), noojOptions.getSyntacticResources());
	}
	
	@Test
	public void optionInputIsParsedToPathList() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		List<Path> expected = Lists.newArrayList(Paths.get("input.txt"),
				Paths.get("with space.txt"),
				Paths.get("/absolute/in.txt"),
				Paths.get("relative/input-file.txt"));
		
		
		assertEquals(expected, noojOptions.getFiles());
	}
	
	@Test
	public void optionWorkingDirIsParsedToPath() throws ParseException {
		args.add("-w");
		args.add(ARGS.get("-w"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-w"), noojOptions.getWorkingDirectory().toString());
	}
	
	@Test
	public void noOptionWorkingDirIsParsedToCurrentWorkingDir() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(System.getProperty("user.dir"), noojOptions.getWorkingDirectory().toString());
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
	
	@Test(expected=ParseException.class)
	public void optionXmlTagsExludesDelimiterOption() throws ParseException {
		args.add("-x");
		args.add(ARGS.get("-x"));
		args.add("-s");
		args.add(ARGS.get("-s"));
		
		NoojOptions.create(args.toArray(STRING_ARRAY));
	}

	@Ignore
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
		
		assertEquals(ImmutableList.of("<SYNTAX>"), noojOptions.getXmlAnnotations());
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
	public void noOptionDelimiterIsParsedToEnptyString() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals("\n", noojOptions.getDelimiter());
	}

	@Test
	public void optionFileTypeIsParsedToEncoding() throws ParseException {
		args.add("-t");
		args.add(ARGS.get("-t"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(null, noojOptions.getEncoding().getEncoding());
		assertEquals(FileType.RAW_TEXT, noojOptions.getEncoding().getFileType());
	}
	
	@Test
	public void bothEncodingOptionsAreParsed() throws ParseException {
		args.add("-t");
		args.add(ARGS.get("-t"));
		args.add("-e");
		args.add(ARGS.get("-e"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-e"), noojOptions.getEncoding().getEncoding());
		assertEquals(FileType.RAW_TEXT, noojOptions.getEncoding().getFileType());
	}
	
	@Test(expected=ParseException.class)
	public void encodingRequiresFileType() throws ParseException {
		args.add("-e");
		args.add(ARGS.get("-e"));
		NoojOptions.create(args.toArray(STRING_ARRAY));
	}

	@Test
	public void noEncodingOptionsIsParsedToDefaultEncoding() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(null, noojOptions.getEncoding().getEncoding());
		assertEquals(FileType.UNICODE_TEXT, noojOptions.getEncoding().getFileType());
	}
	
	@Test
	public void optionLogIsParsedToPath() throws ParseException {
		args.add("-r");
		args.add(ARGS.get("-r"));
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals(ARGS.get("-r"), noojOptions.getLogFile().toString());
	}
	
	@Test
	public void noOptionLogIsParsedToDefaultLogFile() throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args.toArray(STRING_ARRAY));
		
		assertEquals("noojcmd.log", noojOptions.getLogFile().toString());
	}
	
}
