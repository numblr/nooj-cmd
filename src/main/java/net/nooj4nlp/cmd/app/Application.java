package net.nooj4nlp.cmd.app;

import java.io.IOException;
import java.util.Arrays;

import net.nooj4nlp.cmd.StaticInitialization;
import net.nooj4nlp.cmd.io.FileIO.FileIOLoadException;
import net.nooj4nlp.cmd.io.FileIO.FileIOUnsupportedTypeException;
import net.nooj4nlp.cmd.io.FileIO.FileIOWriteException;
import net.nooj4nlp.cmd.io.LinguisticResources.LinguisticResourceException;
import net.nooj4nlp.cmd.processing.LexicalAnalyzer.LexicalAnalysisException;
import net.nooj4nlp.cmd.processing.SyntacticParser.GrammarException;
import net.nooj4nlp.cmd.processing.SyntacticParser.SyntaxParsingException;
import net.nooj4nlp.cmd.processing.TextDelimiter.DelimiterException;
import net.nooj4nlp.cmd.processing.TextDelimiter.LimitExeededException;
import net.nooj4nlp.engine.Dic;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.exception.ExceptionUtils;

final class Application {
	void run(NoojOptions options) throws ParseException {
		TextProcessor textProcessor = new TextProcessorFactory(options).create();
		textProcessor.processFiles(options.getFiles());
	}

	public static void main(String[] args) throws IOException, ParseException {
		NoojOptions options = parseOptions(args);
		
		StaticInitialization.initialize(options.getLogFile());
		
		boolean success = false;
		try {
			new Application().run(options);
			success = true;
		} catch (LinguisticResourceException e) {
			logException(e, "Cannot load linugistic resources: "
					+ e.getMessage());
		} catch (FileIOUnsupportedTypeException e) {
			logException(e, "Cannot load file: " + e.getPath()
					+ "unsupported file type: " + e.getMessage());
		} catch (FileIOLoadException e) {
			logException(e, "Cannot load file: "
					+ e.getPath() + ", " + e.getMessage());
		} catch (FileIOWriteException e) {
			logException(e, "Cannot write file: "
					+ e.getPath() + ", " + e.getMessage());
		} catch (DelimiterException e) {
			logException(e, "Cannot split text into text units: "
					+ e.getMessage());
		} catch (LimitExeededException e) {
			logException(e, "Cannot split text into text units: "
					+ "one text unit is larger than 65K characters");
		} catch (LexicalAnalysisException e) {
			logException(e, "Lexical analysis failed, cannot tokenize text: "
					+ e.getMessage());
		} catch (SyntaxParsingException e) {
			logException(e, "Syntactic parsing failed: "
					+ e.getMessage());
		} catch (GrammarException e) {
			logException(e, "Cannot apply grammars: "
					+ e.getMessage());
		}

		System.exit(success ? 0 : 1);
	}

	private static NoojOptions parseOptions(String[] args) throws IOException {
		if (Arrays.asList(args).contains("--help")) {
			NoojOptions.printHelp();
			System.exit(0);
		}
		
		if (Arrays.asList(args).contains("--version")) {
			NoojOptions.printVersion();
			System.exit(0);
		}
		
		NoojOptions options = null;
		try {
			options = NoojOptions.create(args);
		} catch (ParseException e) {
			System.err.println("Cannot parse command line options: "
					+ e.getMessage());
			System.err.println("Use --help for usage information");
			System.exit(1);
		}
		
		return options;
	}
	
	private static void logException(Exception exception, String message) {
		System.err.println(message);
		System.err.println("Use --help for usage information");
		
		Dic.writeLog(message);
		Dic.writeLog(ExceptionUtils.getStackTrace(exception));
	}
}
