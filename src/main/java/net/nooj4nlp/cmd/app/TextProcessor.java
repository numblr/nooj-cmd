package net.nooj4nlp.cmd.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.io.CharVariantsLoader;
import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.cmd.io.TextLoader;
import net.nooj4nlp.cmd.processing.LexicalAnalyzer;
import net.nooj4nlp.cmd.processing.Ntext2Xml;
import net.nooj4nlp.cmd.processing.NtextImporter;
import net.nooj4nlp.cmd.processing.NtextProcessor;
import net.nooj4nlp.cmd.processing.RawText2Ntext;
import net.nooj4nlp.cmd.processing.SyntacticParser;
import net.nooj4nlp.cmd.processing.TextDelimiter;
import net.nooj4nlp.cmd.processing.Xml2Ntext;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

import com.google.common.collect.ImmutableList;

public class TextProcessor {
	private static final String XML_EXTENSION = ".xml";
	
	private final TextLoader textIO;
	private final NtextImporter converter;
	private final LinguisticResources resources;
	private final List<NtextProcessor> ntextProcessors;
	private final Ntext2Xml xmlConverter;

	public TextProcessor(TextLoader textIO,
			NtextImporter inputConverter,
			LinguisticResources resources,
			List<NtextProcessor> ntextProcessors,
			Ntext2Xml xmlConverter) {
		this.textIO = textIO;
		this.converter = inputConverter;
		this.resources = resources;
		this.ntextProcessors = ntextProcessors;
		this.xmlConverter = xmlConverter;
	}
	
	public static TextProcessor create(NoojOptions options) {
		Language language = options.getLanguage();
		Encoding encoding = options.getEncoding();
		
		Path characterVariants = options.getCharVariantsFile();
		if (characterVariants != null) {
			new CharVariantsLoader(characterVariants).loadInto(language);
		}
		
		TextLoader textIO = new TextLoader(encoding, language);
		
		List<String> xmlTags = options.getXmlTags();
		NtextImporter inputConverter;
		if (xmlTags == null) {
			inputConverter = new RawText2Ntext(language, options.getDelimiter());
		} else {
			inputConverter = new Xml2Ntext(language, xmlTags);
		}
		
		List<Path> lexicalResources = options.getLexicalResources();
		List<Path> syntacticResources = options.getSyntacticResources();
		Path propertiesDefinitions = options.getPropertiesDefinitions();
		Path tmpDirectory = options.getTmpDirectory();
		
		LinguisticResources resources =
				new LinguisticResources(lexicalResources,
						syntacticResources,
						propertiesDefinitions,
						tmpDirectory);
		
		List<NtextProcessor> ntextProcessors = createNtextProcessors(language, resources);

		List<String> xmlAnnotations = options.getXmlAnnotations();
		boolean filterXml = options.isFilterXml();
		Ntext2Xml xmlConverter = new Ntext2Xml(xmlAnnotations, language, filterXml);
		
		return new TextProcessor(textIO, inputConverter, resources, ntextProcessors, xmlConverter);
	}
	
	private static List<NtextProcessor> createNtextProcessors(Language language, LinguisticResources resources) {
		Engine engine = new Engine(new RefObject<Language>(language),
				"", "", "",
				false, null, false, null);
		
		resources.loadInto(engine);
		
		return ImmutableList.of(new TextDelimiter(engine),
				new LexicalAnalyzer(engine),
				new SyntacticParser(engine));
	}

	void processFiles(List<Path> files) {
		for (Path file : files) {
			String rawText = textIO.load(file);
			Ntext nText = converter.convert(rawText);
			resources.loadInto(nText);
			
			for (NtextProcessor processor : ntextProcessors) {
				processor.process(nText);
			}
			
			String xml = xmlConverter.convert(nText);
			String outputFileName = file.toAbsolutePath().toString() + XML_EXTENSION;
			textIO.write(xml, Paths.get(outputFileName));
		}
	}
}
