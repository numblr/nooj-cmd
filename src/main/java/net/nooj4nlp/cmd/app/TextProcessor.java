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
	private Language language;
	private List<File> files;
	private List<Path> lexicalResources;
	private List<Path> syntacticResources;
	private List<String> xmlAnnotations;
	private Path propertiesDefinitions;
	private File charVariantsFile;
	private String delimiter;
	private Encoding encoding;
	private boolean filterXml;
	private Path docDirectory;

	public void process() {
		new CharVariantsLoader(language).loadCharVariants(charVariantsFile);
		TextLoader textIO = new TextLoader(encoding, language);
		LinguisticResources resources =
				new LinguisticResources(lexicalResources,
						syntacticResources,
						propertiesDefinitions,
						docDirectory);
		List<NtextProcessor> ntextProcessors = createNtextProcessors(language, resources);
		RawText2Ntext rawTextConverter = new RawText2Ntext(language, delimiter);
		Ntext2Xml xmlConverter = new Ntext2Xml(xmlAnnotations, language, filterXml);
		
		for (File file : files) {
			String rawText = textIO.load(file);
			Ntext nText = rawTextConverter.convert(rawText);
			resources.loadInto(nText);
			
			for (NtextProcessor processor : ntextProcessors) {
				processor.process(nText);
			}
			
			String xml = xmlConverter.convert(nText);
			textIO.write(xml, null);
		}
	}

	private List<NtextProcessor> createNtextProcessors(Language language, LinguisticResources resources) {
		Engine engine = new Engine(new RefObject<Language>(language),
				"", docDirectory.toAbsolutePath().toString(), "",
				false, null, false, null);
		
		resources.loadInto(engine);
		
		return ImmutableList.of(new TextDelimiter(engine),
				new LexicalAnalyzer(engine),
				new SyntacticParser(engine));
	}
}
