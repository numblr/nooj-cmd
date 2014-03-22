package net.nooj4nlp.cmd.app;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.Encoding.InputType;
import net.nooj4nlp.engine.Language;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@SuppressWarnings("static-access")
public class NoojOptions {
	private static final char OPTION_SEPARATOR = ',';
	
	private static final String DICTS = "d";
	private static final String GRAMMARS = "g";
	private static final String PROPERTIES = "p";
	private static final String CHAR_VARIANTS = "c";
	private static final String XML_TAGS = "x";
	private static final String XML_ANNOTATIONS = "a";
	private static final String FILTER = "f";
	private static final String INPUT_FILES = "i";
	private static final String LANGUAGE = "l";
	private static final String DELIMITER = "s";
	private static final String ENCODING = "e";
	private static final String FILE_TYPE = "t";
	private static final String OUTPUT = "o";

	private static final Options OPTIONS;
	
	static {
		Option language = OptionBuilder
				.withLongOpt("language")
				.hasArgs(1)
				.withArgName("LANG")
				.withDescription("language of the texts")
				.create(LANGUAGE);
		
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
		
		Option xmlTags = OptionBuilder.withLongOpt("xml-tags")
				.hasArgs()
				.withArgName("TAGS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma sepearted list of xml tag strings without braces")
				.create(XML_TAGS);
		
		Option xmlAnnotations = OptionBuilder.withLongOpt("xml-annotations")
				.hasArgs()
				.withArgName("ANNOTATIONS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma sepearted list of xml annotation strings without braces")
				.create(XML_ANNOTATIONS);
		
		Option properties = OptionBuilder
				.withLongOpt("properties")
				.hasArg()
				.withArgName("PROPS")
				.withDescription("comma sepearted list of xml annotation strings without braces")
				.isRequired()
				.create(PROPERTIES);
		
		Option charVariants = OptionBuilder
				.withLongOpt("character-variants")
				.hasArg()
				.withArgName("CHAR_VAR")
				.withDescription("character variants file")
				.create(CHAR_VARIANTS);
		
		Option delimiter = OptionBuilder
				.withLongOpt("delimiter")
				.hasArg()
				.withArgName("DEL")
				.withDescription("delimiter")
				.create(DELIMITER);
		
		Option encoding = OptionBuilder
				.withLongOpt("encoding")
				.hasArg()
				.withArgName("ENC")
				.withDescription("encoding used in the input files")
				.create(ENCODING);
		
		Option inputFileType = OptionBuilder
				.withLongOpt("filetype")
				.hasArg()
				.withArgName("TYPE")
				.withDescription("type of the input files")
				.create(FILE_TYPE);
		
		Option filterXml = OptionBuilder
				.withLongOpt("filter-xml")
				.withDescription("filter output xml")
				.create(FILTER);
		
		Option inputFiles = OptionBuilder
				.withLongOpt("input")
				.hasArgs()
				.withArgName("FILES")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("input files")
				.create(INPUT_FILES);
		
		Option outputDir = OptionBuilder
				.withLongOpt("output")
				.hasArg()
				.withArgName("DIR")
				.withDescription("output directory")
				.create(OUTPUT);
		
		OPTIONS = new Options();
		OPTIONS.addOption(language);
		OPTIONS.addOption(dicts);
		OPTIONS.addOption(grammars);
		OPTIONS.addOption(properties);
		OPTIONS.addOption(charVariants);
		OPTIONS.addOption(delimiter);
		OPTIONS.addOption(encoding);
		OPTIONS.addOption(xmlAnnotations);
		OPTIONS.addOption(xmlTags);
		OPTIONS.addOption(filterXml);
		OPTIONS.addOption(inputFileType);
		OPTIONS.addOption(inputFiles);
		OPTIONS.addOption(outputDir);
	}
	
	private static final String DEFUALT_LANGUAGE = "en";
	private static final Encoding DEFAULT_ENCODING = new Encoding(null, InputType.DEFAULT);
	
	private final CommandLine options;
	
	public NoojOptions(CommandLine options) {
		this.options = options;
	}
	
	static NoojOptions create(String[] args) throws ParseException {
		return new NoojOptions(new GnuParser().parse(OPTIONS, args));
	}
	
	public Language getLanguage() {
		if (!options.hasOption(LANGUAGE)) {
			return new Language(DEFUALT_LANGUAGE);
		};
		
		return new Language(options.getOptionValue(LANGUAGE));
	}
	
	public Encoding getEncoding() {
		if (!options.hasOption(LANGUAGE)) {
			return DEFAULT_ENCODING;
		};
		
		String encoding = options.getOptionValue(ENCODING);
		String inputTypeString = options.getOptionValue(FILE_TYPE).trim();
		InputType inputType = InputType.valueOf(inputTypeString.toUpperCase());
		
		return new Encoding(encoding, inputType);
	}
	
	public List<File> getFiles() {
		return files;
	}

	public List<Path> getLexicalResources() {
		return lexicalResources;
	}

	public List<Path> getSyntacticResources() {
		return syntacticResources;
	}

	public List<String> getXmlAnnotations() {
		return xmlAnnotations;
	}

	public Path getPropertiesDefinitions() {
		return propertiesDefinitions;
	}

	public File getCharVariantsFile() {
		return charVariantsFile;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public boolean isFilterXml() {
		return filterXml;
	}

	public Path getOutputDirectory() {
		return outputDirectory;
	}

	public List<String> getXmlTags() {
		return xmlTags;
	}

	private List<File> files;
	private List<Path> lexicalResources;
	private List<Path> syntacticResources;
	private List<String> xmlAnnotations;
	private Path propertiesDefinitions;
	private File charVariantsFile;
	private String delimiter;
	private boolean filterXml;
	private Path outputDirectory;
	private List<String> xmlTags;
	
}
