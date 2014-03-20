package net.nooj4nlp.cmd.app;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import net.nooj4nlp.cmd.XmlExport;
import net.nooj4nlp.cmd.io.Encoding;
import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.cmd.processing.NtextImporter;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

public class TextProcessor {
	private String delimiter;
	private Map<String, Collection<String>> lexicalResources;
	private Map<String, Collection<String>> syntacticResources;

	public void process(Language language, Encoding encoding, File file) {
		Engine engine = new Engine(new RefObject<Language>(language),
				"", "", "", false, null, false, null);
		
		NtextImporter textImport = new NtextImporter(language, encoding, delimiter, engine);
		Ntext nText = textImport.fromFile(file);
		
		LinguisticResources resourceLoader =
				new LinguisticResources(lexicalResources, syntacticResources);
		resourceLoader.loadInto(engine);
		resourceLoader.loadInto(nText);
		
		LinguisticAnalysis analysis = new LinguisticAnalysis(engine);
		analysis.analyze(nText);
		
		new XmlExport().write();
	}
}
