package net.nooj4nlp.cmd.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.Encoding.FileType;
import net.nooj4nlp.engine.Language;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Sets;

@SuppressWarnings("static-access")
final class NoojOptions {
	private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

	private static final char OPTION_SEPARATOR = ',';
	
	private static final String INPUT_FILES = "i";
	private static final String DICTS = "d";
	private static final String GRAMMARS = "g";
	private static final String WORKING_DIR = "w";
	private static final String PROPERTIES = "p";
	private static final String CHAR_VARIANTS = "c";
	private static final String XML_TAGS = "x";
	private static final String FILTER = "f";
	private static final String LANGUAGE = "l";
	private static final String DELIMITER = "s";
	private static final String ENCODING = "e";
	private static final String FILE_TYPE = "t";
	private static final String TMP = "m";
	private static final String LOG = "r";
	private static final String HELP = "h";

	private static final Options OPTIONS;
	
	static {
		Option inputFiles = OptionBuilder
				.withLongOpt("input")
				.hasArgs()
				.withArgName("FILES")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma separated list of input files")
				.isRequired()
				.create(INPUT_FILES);
		
		Option dicts = OptionBuilder
				.withLongOpt("dicts")
				.hasArgs()
				.withArgName("DICTS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma separated list of .jnod file names")
				.isRequired()
				.create(DICTS);
		
		Option grammars = OptionBuilder
				.withLongOpt("grammars")
				.hasArgs()
				.withArgName("GRAMMARS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma separated list of .nog file names")
				.isRequired()
				.create(GRAMMARS);
		
		Option workingDir = OptionBuilder
				.withLongOpt("workingdir")
				.hasArg()
				.withArgName("DIR")
				.withDescription("nooj working directory")
				.create(WORKING_DIR);
		
		Option properties = OptionBuilder
				.withLongOpt("properties")
				.hasArg()
				.withArgName("PROPS")
				.withDescription("properties definition file")
				.isRequired()
				.create(PROPERTIES);
		
		Option charVariants = OptionBuilder
				.withLongOpt("charactervariants")
				.hasArg()
				.withArgName("CHAR_VAR")
				.withDescription("character variants file")
				.create(CHAR_VARIANTS);
		
		Option xmlTags = OptionBuilder.withLongOpt("xmltags")
				.hasArgs()
				.withArgName("TAGS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma sepearted list of xml tags")
				.create(XML_TAGS);
		
		Option filterXml = OptionBuilder
				.withLongOpt("filterxml")
				.withDescription("output contains only xml annotations")
				.create(FILTER);
		
		Option language = OptionBuilder
				.withLongOpt("language")
				.hasArgs(1)
				.withArgName("LANG")
				.withDescription("iso code for the language of the input files")
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
				.withDescription("delimiter used for splitting text into text units")
				.create(DELIMITER);
		
		Option tmpDir = OptionBuilder
				.withLongOpt("temp")
				.hasArg()
				.withArgName("DIR")
				.withDescription("directory for temporary files")
				.create(TMP);
		
		Option log = OptionBuilder
				.withLongOpt("log")
				.hasArg()
				.withArgName("FILE")
				.withDescription("log file")
				.create(LOG);
		
		Option help = OptionBuilder
				.withLongOpt("help")
				.withArgName("FILE")
				.withDescription("print this help message")
				.create(HELP);
		
		OPTIONS = new Options();
		OPTIONS.addOption(inputFiles);
		OPTIONS.addOption(dicts);
		OPTIONS.addOption(grammars);
		OPTIONS.addOption(workingDir);
		OPTIONS.addOption(properties);
		OPTIONS.addOption(charVariants);
		OPTIONS.addOption(xmlTags);
		OPTIONS.addOption(filterXml);
		OPTIONS.addOption(language);
		OPTIONS.addOption(encoding);
		OPTIONS.addOption(inputFileType);
		OPTIONS.addOption(delimiter);
		OPTIONS.addOption(tmpDir);
		OPTIONS.addOption(log);
		OPTIONS.addOption(help);
	}
	
	private static final List<String> DEFAULT_XML_ANNOTATIONS = ImmutableList.of("<SYNTAX>");
	private static final String DEFAULT_LANGUAGE = "en";
	private static final Encoding DEFAULT_ENCODING = new Encoding(null, FileType.UNICODE);
	private static final Path DEFAULT_TMP_DIR = Paths.get(System.getProperty("java.io.tmpdir"));
	private static final String DEFAULT_DELIMITER = "";
	private static final Path DEFAULT_LOG_FILE = Paths.get("noojcmd.log");
	private static final Path DEFAULT_WORKING_DIR = Paths.get(System.getProperty("user.dir"));

	private final CommandLine options;
	
	private NoojOptions(CommandLine options) {
		this.options = checkNotNull(options);
	}
	
	static NoojOptions create(String[] args) throws ParseException {
		return new NoojOptions(new GnuParser().parse(OPTIONS, args));
	}
	
	List<Path> getFiles() {
		return getPaths(INPUT_FILES);
	}

	List<Path> getLexicalResources() {
		return getPaths(DICTS);
	}

	List<Path> getSyntacticResources() {
		return getPaths(GRAMMARS);
	}

	private List<Path> getPaths(String optionKey) {
		String[] filePaths = options.getOptionValues(optionKey);
		Builder<Path> files = ImmutableList.builder();
		for (String file : filePaths) {
			files.add(Paths.get(file));
		}
		
		return files.build();
	}
	
	Path getWorkingDirectory() {
		if (!options.hasOption(WORKING_DIR)) {
			return DEFAULT_WORKING_DIR;
		}
		
		return Paths.get(options.getOptionValue(WORKING_DIR));
	}

	Path getPropertiesDefinitions() {
		return Paths.get(options.getOptionValue(PROPERTIES));
	}

	Path getCharVariantsFile() {
		if (!options.hasOption(CHAR_VARIANTS)) {
			return null;
		}
		
		return Paths.get(options.getOptionValue(CHAR_VARIANTS));
	}

	List<String> getXmlTags() {
		if (!options.hasOption(XML_TAGS)) {
			return null;
		}
		
		return ImmutableList.copyOf(options.getOptionValues(XML_TAGS));
	}

	List<String> getXmlAnnotations() {
		return DEFAULT_XML_ANNOTATIONS;
	}
	
	boolean isFilterXml() {
		return options.hasOption(FILTER);
	}

	Language getLanguage() {
		if (!options.hasOption(LANGUAGE)) {
			return new Language(DEFAULT_LANGUAGE);
		};
		
		return new Language(options.getOptionValue(LANGUAGE));
	}

	Encoding getEncoding() {
		if (!options.hasOption(FILE_TYPE) && !options.hasOption(ENCODING)) {
			return DEFAULT_ENCODING;
		};
		
		String encoding = options.getOptionValue(ENCODING);
	
		FileType fileType;
		if (options.hasOption(FILE_TYPE)) {
			String fileTypeString = options.getOptionValue(FILE_TYPE).trim();
			fileType = FileType.valueOf(fileTypeString.toUpperCase());
		} else {
			fileType = DEFAULT_ENCODING.getFileType();
		}
		
		return new Encoding(encoding, fileType);
	}

	String getDelimiter() {
		if (!options.hasOption(DELIMITER)) {
			return DEFAULT_DELIMITER;
		}
		
		return options.getOptionValue(DELIMITER);
	}
	
	Path getLogFile() {
		if (!options.hasOption(LOG)) {
			return DEFAULT_LOG_FILE;
		}
		
		return Paths.get(options.getOptionValue(LOG));
	}

	Path getTmpDirectory() {
		if (!options.hasOption(TMP)) {
			return DEFAULT_TMP_DIR;
		}
		
		return Paths.get(options.getOptionValue(TMP));
	}
	
	static void printHelp() {
		HELP_FORMATTER.printHelp(60,
				USAGE,
				HELP_MESSAGE,
				OPTIONS,
				AVAILABLE_OPTIONS);
	}
	
	private static final String USAGE =
			"java -jar [options] -i file[,file..] -d dict[,dict..] -g grammar[,grammar..] -p propdefs";
	
	private static final String HELP_MESSAGE = "Command Line interface for ONooj:"
			+ "\n"
			+ "The specified input files are converted to xml annotated text files."
			+ "The output files are created next to the input files with a .xml.txt "
			+ "extension."
			+ "\n"
			+ "Dictionary, grammar and property definition files used for linguistic "
			+ "analysis must be specified."
			+ "\n"
			+ "If any of the arguments contains whitespace characters, then on Windows "
			+ "the argument must be surrounded with double quotes. On Unix use simple "
			+ "quotes, double quotes, or escape the space with a backslash."
			+ "\n\n\n";
			
	private static final String AVAILABLE_OPTIONS = "Supported file tpyes:"
			+ "\n"
			+ Joiner.on(", ").join(FileType.values())
			+ "\n"
			+ "Supported language codes:"
			+ "\n"
			+ Joiner.on(", ").join(Language.getAllLanguages())
			+ "\n"
			+ "Supported encodings:"
			+ "\n"
			+ Joiner.on(", ")
					.join(Sets.newTreeSet(Charset.availableCharsets().keySet()));
}
