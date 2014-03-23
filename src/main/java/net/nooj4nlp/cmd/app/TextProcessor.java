package net.nooj4nlp.cmd.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.cmd.io.TextLoader;
import net.nooj4nlp.cmd.processing.Ntext2Xml;
import net.nooj4nlp.cmd.processing.NtextConverter;
import net.nooj4nlp.cmd.processing.NtextProcessor;
import net.nooj4nlp.engine.Ntext;

import org.apache.commons.io.FilenameUtils;

class TextProcessor {
	private static final String XML_EXTENSION = ".xml.txt";
	
	private final TextLoader textIO;
	private final NtextConverter inputConverter;
	private final LinguisticResources resources;
	private final List<NtextProcessor> ntextProcessors;
	private final Ntext2Xml xmlConverter;

	TextProcessor(TextLoader textIO,
			NtextConverter inputConverter,
			LinguisticResources resources,
			List<NtextProcessor> ntextProcessors,
			Ntext2Xml xmlConverter) {
		this.textIO = textIO;
		this.inputConverter = inputConverter;
		this.resources = resources;
		this.ntextProcessors = ntextProcessors;
		this.xmlConverter = xmlConverter;
	}

	void processFiles(List<Path> files) {
		for (Path file : files) {
			String rawText = textIO.load(file);
			Ntext nText = inputConverter.convert(rawText);
			resources.loadInto(nText);
			
			for (NtextProcessor processor : ntextProcessors) {
				processor.process(nText);
			}
			
			String xml = xmlConverter.convert(nText);
			Path outputFile = createOutputFileName(file);
			textIO.write(xml, outputFile);
		}
	}

	private Path createOutputFileName(Path file) {
		String filePath = file.toAbsolutePath().toString();
		String xmlFilePath = FilenameUtils.removeExtension(filePath) + XML_EXTENSION;
		
		return Paths.get(xmlFilePath);
	}
}
