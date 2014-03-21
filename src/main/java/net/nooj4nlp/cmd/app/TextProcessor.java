package net.nooj4nlp.cmd.app;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.TextLoader;
import net.nooj4nlp.cmd.processing.LexicalAnalyzer;
import net.nooj4nlp.cmd.processing.Ntext2Xml;
import net.nooj4nlp.cmd.processing.NtextProcessor;
import net.nooj4nlp.cmd.processing.RawText2Ntext;
import net.nooj4nlp.cmd.processing.SyntacticParser;
import net.nooj4nlp.cmd.processing.TextDelimiter;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

import com.google.common.collect.ImmutableList;

public class TextProcessor {
	private String delimiter;
	private Map<String, Collection<String>> lexicalResources;
	private Map<String, Collection<String>> syntacticResources;

	public void process(Language language, Encoding encoding, File file) {
		TextLoader textIO = new TextLoader(encoding, language);
		String rawText = textIO.load(file);
		Ntext nText = new RawText2Ntext(language, delimiter).convert(rawText);

		for (NtextProcessor processor : createNtextProcessors(language, nText)) {
			processor.process(nText);
		}
		
		String xml = new Ntext2Xml(null, null, language, false).convert(nText);
		textIO.write(xml, null);
	}

	private ImmutableList<NtextProcessor> createNtextProcessors(Language language, Ntext nText) {
		Engine engine = new Engine(new RefObject<Language>(language),
				"", "", "", false, null, false, null);
		
//		LinguisticResources resourceLoader =
//				new LinguisticResources(lexicalResources, syntacticResources);
//		resourceLoader.loadInto(engine);
//		resourceLoader.loadInto(nText);
		
		return ImmutableList.of(new TextDelimiter(engine),
				new LexicalAnalyzer(engine),
				new SyntacticParser(engine));
	}
}
