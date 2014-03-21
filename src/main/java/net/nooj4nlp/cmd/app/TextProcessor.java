package net.nooj4nlp.cmd.app;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import net.nooj4nlp.cmd.io.CharVariantsLoader;
import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.LinguisticResources;
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
	private List<Path> lexicalResources;
	private List<Path> syntacticResources;
	private Path propertiesDefinitions;
	private Path docDirectory;

	public void process(Language language, Encoding encoding, File file) {
		new CharVariantsLoader(language).loadCharVariants(null);
		
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
		
		LinguisticResources resources =
				new LinguisticResources(lexicalResources,
						syntacticResources,
						propertiesDefinitions,
						docDirectory);
		resources.loadInto(engine);
		resources.loadInto(nText);
		
		return ImmutableList.of(new TextDelimiter(engine),
				new LexicalAnalyzer(engine),
				new SyntacticParser(engine));
	}
}
