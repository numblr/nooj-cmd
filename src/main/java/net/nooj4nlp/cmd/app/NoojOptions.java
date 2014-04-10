package net.nooj4nlp.cmd.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

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
import org.apache.commons.cli.Parser;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Sets;

@SuppressWarnings("static-access")
final class NoojOptions {
	private static final Parser PARSER = new GnuParser();
	private static final String PROPERTIES_FILE = "/.properties";
	private static final String VERSION_PROPERTY = "version";
	
	private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

	private static final List<Path> DEFAULT_DICTS = Collections.emptyList();
	private static final List<Path> DEFAULT_GRAMMARS = Collections.emptyList();
	private static final Path DEFAULT_WORKING_DIR = Paths.get(System.getProperty("user.dir"));
	private static final String DEFAULT_LANGUAGE = "en";
	private static final String DEFAULT_DELIMITER = "\n";
	private static final Encoding DEFAULT_ENCODING = new Encoding(null, FileType.UNICODE_TEXT);
	private static final String DEFAULT_CHAR_ENCODING = "UTF-8";
	private static final Path DEFAULT_LOG_FILE = Paths.get("noojcmd.log");
	private static final List<String> DEFAULT_XML_ANNOTATIONS = ImmutableList.of("<SYNTAX>");

	private static final char OPTION_SEPARATOR = ',';
	
	private static final String INPUT_FILES = "i";
	private static final String DICTS = "d";
	private static final String GRAMMARS = "g";
	private static final String WORKING_DIR = "w";
	private static final String XML_TAGS = "x";
	private static final String FILTER = "f";
	private static final String LANGUAGE = "l";
	private static final String DELIMITER = "s";
	private static final String ENCODING = "e";
	private static final String FILE_TYPE = "t";
	private static final String LOG = "r";
	private static final String HELP = "help";
	private static final String VERSION = "version";

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
				.withDescription(description("comma separated list of .jnod file names (without path)", "no dictionaries"))
				.create(DICTS);
		
		Option grammars = OptionBuilder
				.withLongOpt("grammars")
				.hasArgs()
				.withArgName("GRAMMARS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription(description("comma separated list of .nog file names (without path)", "no grammars"))
				.create(GRAMMARS);
		
		Option workingDir = OptionBuilder
				.withLongOpt("workingdir")
				.hasArg()
				.withArgName("DIR")
				.withDescription(description("nooj working directory containing "
						+ "the language folders with dictionaries and grammars following "
						+ "the ONooj conventions", "current working directory"))
				.create(WORKING_DIR);
		
		Option xmlTags = OptionBuilder.withLongOpt("xmltags")
				.hasArgs()
				.withArgName("TAGS")
				.withValueSeparator(OPTION_SEPARATOR)
				.withDescription("comma sepearted list of xml tags used for splitting input into text units. "
						+ "This option is mutually exclusive with the delimiter option")
				.create(XML_TAGS);
		
		Option filterXml = OptionBuilder
				.withLongOpt("filterxml")
				.withDescription("if sepcified, only annotated text is included in the output")
				.create(FILTER);
		
		Option language = OptionBuilder
				.withLongOpt("language")
				.hasArg()
				.withArgName("LANG")
				.withDescription(description("iso code of the language of the input files", DEFAULT_LANGUAGE))
				.create(LANGUAGE);
		
		Option encoding = OptionBuilder
				.withLongOpt("encoding")
				.hasArg()
				.withArgName("ENC")
				.withDescription(description("encoding of the input files. "
						+ "This option may be ignored if not applicable to a file type", DEFAULT_CHAR_ENCODING))
				.create(ENCODING);
		
		Option inputFileType = OptionBuilder
				.withLongOpt("filetype")
				.hasArg()
				.withArgName("TYPE")
				.withDescription(description("type of the input files", DEFAULT_ENCODING.getFileTypeName()))
				.create(FILE_TYPE);
		
		Option delimiter = OptionBuilder
				.withLongOpt("delimiter")
				.hasArg()
				.withArgName("DEL")
				.withDescription(description("PERL regular expression used for splitting input into text units, "
						+ "use empty character (\"\") to process the whole input as one unit", "new line (\"\\n\")"))
				.create(DELIMITER);
		
		Option log = OptionBuilder
				.withLongOpt("log")
				.hasArg()
				.withArgName("FILE")
				.withDescription(description("log file", DEFAULT_LOG_FILE.toString()))
				.create(LOG);
		
		Option version = OptionBuilder
				.withLongOpt(VERSION)
				.withDescription("print version")
				.create();
		
		Option help = OptionBuilder
				.withLongOpt(HELP)
				.withDescription("print this help message")
				.create();
		
		OPTIONS = new Options();
		OPTIONS.addOption(inputFiles);
		OPTIONS.addOption(dicts);
		OPTIONS.addOption(grammars);
		OPTIONS.addOption(workingDir);
		OPTIONS.addOption(xmlTags);
		OPTIONS.addOption(filterXml);
		OPTIONS.addOption(language);
		OPTIONS.addOption(encoding);
		OPTIONS.addOption(inputFileType);
		OPTIONS.addOption(delimiter);
		OPTIONS.addOption(log);
		OPTIONS.addOption(version);
		OPTIONS.addOption(help);
	}

	private final CommandLine options;
	
	private NoojOptions(CommandLine options) throws ParseException {
		if (options.hasOption(XML_TAGS) && options.hasOption(DELIMITER)) {
			throw new ParseException("Delimiter expressions cannot be specified if XML Delimiters are used.");
		}
		
		if (options.hasOption(ENCODING) && !options.hasOption(FILE_TYPE)) {
			throw new ParseException("Encoding can be specified only in connection with a file type.");
		}
		
		if (!options.hasOption(DICTS) && !options.hasOption(GRAMMARS)) {
			throw new ParseException("There must be at least one dictionary or grammar file specified.");
		}
		
		this.options = checkNotNull(options);
	}
	
	static NoojOptions create(String[] args) throws ParseException {
		return new NoojOptions(PARSER.parse(OPTIONS, args));
	}
	
	List<Path> getFiles() {
		return getPaths(INPUT_FILES);
	}

	List<Path> getLexicalResources() {
		if (!options.hasOption(DICTS)) {
			return DEFAULT_DICTS;
		}

		return getPaths(DICTS);
	}

	List<Path> getSyntacticResources() {
		if (!options.hasOption(GRAMMARS)) {
			return DEFAULT_GRAMMARS;
		}
		
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
			//Language objects are mutable!
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
	
	private static String description(String message, String defaultValue) {
		return message + " [default: " + defaultValue + "]";
	}
	
	static void printVersion() throws IOException {
		Properties properties = new Properties();
		properties.load(NoojOptions.class.getResourceAsStream(PROPERTIES_FILE));
		
		System.out.println("Nooj-Cmd Version " + properties.getProperty(VERSION_PROPERTY));
	}
	
	static void printHelp() {
		HELP_FORMATTER.printHelp(60,
				USAGE,
				HELP_MESSAGE,
				OPTIONS,
				AVAILABLE_OPTIONS);
	}
	
	private static final String USAGE =
			"java -jar -i file[,file..] [-d dict[,dict..] -g grammar[,grammar..]] [options]";
	
	private static final String HELP_MESSAGE = "Command Line interface for ONooj:"
			+ "\n"
			+ "The specified input files are converted to xml annotated text files."
			+ "The output files are created next to the input files with a .xml.txt "
			+ "extension."
			+ "\n\n"
			+ "Dictionary, grammar and property definition files used for linguistic "
			+ "analysis must be placed in the Nooj working directory, following the the convention:\n"
			+ "languagecode/Lexical Analysis - containing dictionary, character variants and property definition files\n"
			+ "languagecode/Syntactic Analysis - containing grammar files."
			+ "\n\n"
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
			+ Joiner.on(", ").join(Sets.newTreeSet(Charset.availableCharsets().keySet()));
}
