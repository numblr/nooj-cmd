package net.nooj4nlp.cmd.app;

import java.nio.file.Path;
import java.util.List;

import net.nooj4nlp.cmd.io.CharVariantsLoader;
import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.cmd.io.FileIO;
import net.nooj4nlp.cmd.processing.LexicalAnalyzer;
import net.nooj4nlp.cmd.processing.Ntext2Xml;
import net.nooj4nlp.cmd.processing.NtextConverter;
import net.nooj4nlp.cmd.processing.NtextProcessor;
import net.nooj4nlp.cmd.processing.RawText2Ntext;
import net.nooj4nlp.cmd.processing.SyntacticParser;
import net.nooj4nlp.cmd.processing.TextDelimiter;
import net.nooj4nlp.cmd.processing.Xml2Ntext;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.RefObject;

import com.google.common.collect.ImmutableList;

final class TextProcessorFactory {
	private final NoojOptions options;

	TextProcessorFactory(NoojOptions options) {
		this.options = options;
	}
	
	TextProcessor create() {
		Language language = options.getLanguage();
		
		loadCharacterVariants(language);
		
		FileIO fileIO = createFileIO(language);
		NtextConverter inputConverter = createInputConverter(language);
		LinguisticResources resources = createLinugisticResources();
		List<NtextProcessor> processors = createProcessors(language, resources);
		Ntext2Xml xmlConverter = createXmlConverter(language);
		
		return new TextProcessor(fileIO,
				inputConverter,
				resources,
				processors,
				xmlConverter);
	}

	private FileIO createFileIO(Language language) {
		Encoding encoding = options.getEncoding();
		
		return new FileIO(encoding, language);
	}

	private Ntext2Xml createXmlConverter(Language language) {
		List<String> xmlAnnotations = options.getXmlAnnotations();
		boolean filterXml = options.isFilterXml();
		
		return new Ntext2Xml(xmlAnnotations, language, filterXml);
	}

	private LinguisticResources createLinugisticResources() {
		List<Path> lexicalResources = options.getLexicalResources();
		List<Path> syntacticResources = options.getSyntacticResources();
		Path propertiesDefinitions = options.getPropertiesDefinitions();
		Path tmpDirectory = options.getTmpDirectory();
		
		return new LinguisticResources(lexicalResources,
				syntacticResources,
				propertiesDefinitions,
				tmpDirectory);
	}

	private NtextConverter createInputConverter(Language language) {
		List<String> xmlTags = options.getXmlTags();
		NtextConverter inputConverter;
		if (xmlTags == null) {
			inputConverter = new RawText2Ntext(language, options.getDelimiter());
		} else {
			inputConverter = new Xml2Ntext(language, xmlTags);
		}
		
		return inputConverter;
	}

	private void loadCharacterVariants(Language language) {
		Path characterVariants = options.getCharVariantsFile();
		if (characterVariants != null) {
			new CharVariantsLoader(characterVariants).loadInto(language);
		}
	}
	
	private List<NtextProcessor> createProcessors(Language language, LinguisticResources resources) {
		Engine engine = new Engine(new RefObject<Language>(language),
				"", "", "",
				false, null, false, null);
		
		resources.loadInto(engine);
		
		return ImmutableList.of(new TextDelimiter(engine),
				new LexicalAnalyzer(engine),
				new SyntacticParser(engine));
	}
}
