package net.nooj4nlp.cmd.app;

import java.io.IOException;
import java.util.Arrays;

import net.nooj4nlp.cmd.StaticInitialization;
import net.nooj4nlp.cmd.io.CharVariantsLoader.CharVariantsException;
import net.nooj4nlp.cmd.io.FileIO.FileIOWriteException;
import net.nooj4nlp.cmd.io.LinguisticResources.LinguisticResourceException;
import net.nooj4nlp.cmd.io.LinguisticResources.ResourceLinkCreationException;
import net.nooj4nlp.cmd.io.LinguisticResources.ResourceLinkRemovalException;
import net.nooj4nlp.cmd.io.FileIO.FileIOLoadException;
import net.nooj4nlp.cmd.io.FileIO.FileIOUnsupportedTypeException;
import net.nooj4nlp.cmd.processing.LexicalAnalyzer.LexicalAnalysisException;
import net.nooj4nlp.cmd.processing.SyntacticParser.GrammarException;
import net.nooj4nlp.cmd.processing.SyntacticParser.SyntaxParsingException;
import net.nooj4nlp.cmd.processing.TextDelimiter.DelimiterException;
import net.nooj4nlp.cmd.processing.TextDelimiter.LimitExeededException;

import org.apache.commons.cli.ParseException;

final class Application {
	void run(String[] args) throws ParseException {
		NoojOptions noojOptions = NoojOptions.create(args);
		TextProcessor textProcessor = new TextProcessorFactory(noojOptions).create();
		
		textProcessor.processFiles(noojOptions.getFiles());
	}

	public static void main(String[] args) throws IOException, ParseException {
		if (Arrays.asList(args).contains("--help")) {
			NoojOptions.printHelp();
			return;
		}
		
		StaticInitialization.initialize();
		String error = null;
		
		try {
			new Application().run(args);
		} catch (ParseException e) {
			error = "Cannot parse command line options: "
					+ e.getMessage();
		} catch (CharVariantsException e) {
			error = "Cannot load character variants file: "
					+ e.getPath() + ", " + e.getMessage();
		} catch (LinguisticResourceException e) {
			error = "Cannot load linugistic resources: "
					+ e.getMessage();
		} catch (ResourceLinkCreationException e) {
			error = "Cannot create temporary link to linguistic resource files: "
					+ e.getPath() + ", " + e.getMessage();
		} catch (ResourceLinkRemovalException e) {
			error = "Cannot not remove temporary directory for linguistic resource files: "
					+ e.getPath() + ", " + e.getMessage();
		} catch (FileIOUnsupportedTypeException e) {
			error = "Cannot load file: " + e.getPath()
					+ "unsupported file type: " + e.getMessage();
		} catch (FileIOLoadException e) {
			error = "Cannot load file: "
					+ e.getPath() + ", " + e.getMessage();
		} catch (FileIOWriteException e) {
			error = "Cannot write file: "
					+ e.getPath() + ", " + e.getMessage();
		} catch (DelimiterException e) {
			error = "Cannot split text into text units"
					+ e.getMessage();
		} catch (LimitExeededException e) {
			error = "Cannot split text into text units: "
					+ "one text unit is larger than 65K characters";
		} catch (LexicalAnalysisException e) {
			error = "Lexical analysis failed, cannot tokenize text: " + e.getMessage();
		} catch (SyntaxParsingException e) {
			error = "Syntactic parsing failed: " + e.getMessage();
		} catch (GrammarException e) {
			error = "Cannot apply grammars: " + e.getMessage();
		}
		
		if (error != null) {
			System.err.println(error);
		}
	}
}