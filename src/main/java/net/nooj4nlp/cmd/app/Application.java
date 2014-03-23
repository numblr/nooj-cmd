package net.nooj4nlp.cmd.app;

import java.io.IOException;

import net.nooj4nlp.cmd.StaticInitialization;
import net.nooj4nlp.cmd.io.CharVariantsLoader.CharVariantsException;
import net.nooj4nlp.cmd.io.LinguisticResources.LinguisticResourceException;
import net.nooj4nlp.cmd.io.TextLoader.TextLoaderException;
import net.nooj4nlp.cmd.processing.LexicalAnalyzer.LexicalAnalysisException;
import net.nooj4nlp.cmd.processing.SyntacticParser.GrammarException;
import net.nooj4nlp.cmd.processing.SyntacticParser.SyntaxParsingException;
import net.nooj4nlp.cmd.processing.TextDelimiter.DelimiterException;

import org.apache.commons.cli.ParseException;

class Application {
	public static void main(String[] args) throws IOException, ParseException {
		StaticInitialization.initialize();
		
		try {
			NoojOptions noojOptions = NoojOptions.create(args);
			TextProcessor textProcessor = new TextProcessorFactory(noojOptions)
					.create();
			textProcessor.processFiles(noojOptions.getFiles());
		} catch (ParseException e) {
		} catch (CharVariantsException e) {
		} catch (LinguisticResourceException e) {
		} catch (TextLoaderException e) {
		} catch (DelimiterException e) {
		} catch (LexicalAnalysisException e) {
		} catch (SyntaxParsingException e) {
		} catch (GrammarException e) {
		}
	}
}