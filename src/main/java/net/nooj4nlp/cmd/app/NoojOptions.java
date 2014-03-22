package net.nooj4nlp.cmd.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.Encoding.InputType;
import net.nooj4nlp.engine.Language;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

@SuppressWarnings("static-access")
public class NoojOptions {
	private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

	private static final char OPTION_SEPARATOR = ',';
	
	private static final String INPUT_FILES = "i";
	private static final String DICTS = "d";
	private static final String GRAMMARS = "g";
	private static final String PROPERTIES = "p";
	private static final String CHAR_VARIANTS = "c";
	private static final String XML_TAGS = "x";
//	private static final String XML_ANNOTATIONS = "a";
	private static final String FILTER = "f";
	private static final String LANGUAGE = "l";
	private static final String DELIMITER = "s";
	private static final String ENCODING = "e";
	private static final String FILE_TYPE = "t";
	private static final String TMP = "m";

	private static final Options OPTIONS;
	
	static {
		Option inputFiles = OptionBuilder
				.withLongOpt("input")
				.hasArgs()
				.withArgName("FILES")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma seperated list of input files")
				.create(INPUT_FILES);
		
		Option dicts = OptionBuilder
				.withLongOpt("dicts")
				.hasArgs()
				.withArgName("DICTS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma sepearted list of .dic files")
				.isRequired()
				.create(DICTS);
		
		Option grammars = OptionBuilder
				.withLongOpt("grammars")
				.hasArgs()
				.withArgName("GRAMMARS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma sepearted list of .nog files")
				.isRequired()
				.create(GRAMMARS);
		
		Option properties = OptionBuilder
				.withLongOpt("properties")
				.hasArg()
				.withArgName("PROPS")
				.withDescription("properties definition file")
				.isRequired()
				.create(PROPERTIES);
		
		Option charVariants = OptionBuilder
				.withLongOpt("character-variants")
				.hasArg()
				.withArgName("CHAR_VAR")
				.withDescription("character variants file")
				.create(CHAR_VARIANTS);
		
		Option xmlTags = OptionBuilder.withLongOpt("xml-tags")
				.hasArgs()
				.withArgName("TAGS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma sepearted list of xml tag strings without braces")
				.create(XML_TAGS);
		
//		Option xmlAnnotations = OptionBuilder.withLongOpt("xml-annotations")
//				.hasArgs()
//				.withArgName("ANNOTATIONS")
//				.withValueSeparator(OPTION_SEPARATOR)
//				.withDescription("comma sepearted list of xml annotation strings without braces")
//				.create(XML_ANNOTATIONS);
		
		Option filterXml = OptionBuilder
				.withLongOpt("filter-xml")
				.withDescription("filter output xml")
				.create(FILTER);
		
		Option language = OptionBuilder
				.withLongOpt("language")
				.hasArgs(1)
				.withArgName("LANG")
				.withDescription("language of the input files")
				.create(LANGUAGE);
		
		Option encoding = OptionBuilder
				.withLongOpt("encoding")
				.hasArg()
				.withArgName("ENC")
				.withDescription("encoding of the input files")
				.create(ENCODING);
		
		Option inputFileType = OptionBuilder
				.withLongOpt("filetype")
				.hasArg()
				.withArgName("TYPE")
				.withDescription("type of the input files")
				.create(FILE_TYPE);
		
		Option delimiter = OptionBuilder
				.withLongOpt("delimiter")
				.hasArg()
				.withArgName("DEL")
				.withDescription("delimiter for text units")
				.create(DELIMITER);
		
		Option tmpDir = OptionBuilder
				.withLongOpt("temp")
				.hasArg()
				.withArgName("DIR")
				.withDescription("directory for temporary files")
				.create(TMP);
		
		OPTIONS = new Options();
		OPTIONS.addOption(inputFiles);
		OPTIONS.addOption(dicts);
		OPTIONS.addOption(grammars);
		OPTIONS.addOption(properties);
		OPTIONS.addOption(charVariants);
		//OPTIONS.addOption(xmlAnnotations);
		OPTIONS.addOption(xmlTags);
		OPTIONS.addOption(filterXml);
		OPTIONS.addOption(language);
		OPTIONS.addOption(encoding);
		OPTIONS.addOption(inputFileType);
		OPTIONS.addOption(delimiter);
		OPTIONS.addOption(tmpDir);
	}
	
	private static final List<String> DEFAULT_XML_ANNOTATIONS = ImmutableList.of("<SYNTAX>");
	private static final String DEFUALT_LANGUAGE = "en";
	private static final Encoding DEFAULT_ENCODING = new Encoding(null, InputType.DEFAULT);
	private static final String DEFAULT_TMP_DIR = System.getProperty("java.io.tmpdir");
	
	private final CommandLine options;
	
	private NoojOptions(CommandLine options) {
		this.options = options;
	}
	
	static NoojOptions create(String[] args) throws ParseException {
		return new NoojOptions(new GnuParser().parse(OPTIONS, args));
	}
	
	public List<Path> getFiles() {
		if (!options.hasOption(INPUT_FILES)) {
			return null;
		}
		
		return getPathOptions(INPUT_FILES);
	}

	public List<Path> getLexicalResources() {
		return getPathOptions(DICTS);
	}

	public List<Path> getSyntacticResources() {
		return getPathOptions(GRAMMARS);
	}

	public List<Path> getPathOptions(String optionKey) {
		String[] filePaths = options.getOptionValues(optionKey);
		Builder<Path> files = ImmutableList.builder();
		for (String file : filePaths) {
			files.add(Paths.get(file));
		}
		
		return files.build();
	}

	public Path getPropertiesDefinitions() {
		return Paths.get(options.getOptionValue(PROPERTIES));
	}

	public Path getCharVariantsFile() {
		if (!options.hasOption(CHAR_VARIANTS)) {
			return null;
		}
		
		return Paths.get(options.getOptionValue(CHAR_VARIANTS));
	}

	public List<String> getXmlTags() {
		if (!options.hasOption(XML_TAGS)) {
			return null;
		}
		
		return ImmutableList.copyOf(options.getOptionValues(XML_TAGS));
	}

	public List<String> getXmlAnnotations() {
		return DEFAULT_XML_ANNOTATIONS;
		//not implemented yet
//		if (!options.hasOption(XML_ANNOTATIONS)) {
//			return ImmutableList.of("<SYNTAX>");
//		}
//		
//		return ImmutableList.copyOf(options.getOptionValues(XML_ANNOTATIONS));
	}

	public boolean isFilterXml() {
		return options.hasOption(FILTER);
	}

	public Language getLanguage() {
		if (!options.hasOption(LANGUAGE)) {
			return new Language(DEFUALT_LANGUAGE);
		};
		
		return new Language(options.getOptionValue(LANGUAGE));
	}

	public Encoding getEncoding() {
		if (!options.hasOption(FILE_TYPE) && !options.hasOption(ENCODING)) {
			return DEFAULT_ENCODING;
		};
		
		String encoding = options.getOptionValue(ENCODING);
	
		InputType inputType;
		if (options.hasOption(FILE_TYPE)) {
			String inputTypeString = options.getOptionValue(FILE_TYPE).trim();
			inputType = InputType.valueOf(inputTypeString.toUpperCase());
		} else {
			inputType = InputType.DEFAULT;
		}
		
		return new Encoding(encoding, inputType);
	}

	public String getDelimiter() {
		return options.getOptionValue(DELIMITER);
	}

	public Path getTmpDirectory() {
		if (!options.hasOption(TMP)) {
			return Paths.get(DEFAULT_TMP_DIR);
		}
		
		return Paths.get(options.getOptionValue(TMP));
	}
	
	public static void printHelp() {
		HELP_FORMATTER.printHelp(HELP_MESSAGE, OPTIONS);
	}
	
	private static final String HELP_MESSAGE = "Command Line interfacec for ONooj.\n" +
			"\n" +
			"The specified input files are converted to xml annotated text files." +
			"The output files are created next to the input files with an xml postfix." +
			"\n" +
			"Dictionary, grammar and property definition files must be specified.";
}
