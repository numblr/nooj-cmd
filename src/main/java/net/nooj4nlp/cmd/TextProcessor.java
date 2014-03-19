package net.nooj4nlp.cmd;

import java.io.File;
import java.util.Collection;
import java.util.Map;

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
		
		TextImport textImport = new TextImport(language, encoding, delimiter, engine);
		Ntext nText = textImport.fromFile(file);
		
		LinguisticResourceLoader resourceLoader =
				new LinguisticResourceLoader(lexicalResources, syntacticResources);
		resourceLoader.loadInto(engine);
		resourceLoader.loadInto(nText);
		
		LinguisticAnalysis analysis = new LinguisticAnalysis(engine);
		analysis.analyze(nText);
		
		new XmlExport().write();
	}
}
