package net.nooj4nlp.cmd.io;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

import com.google.common.collect.Lists;

public final class LinguisticResources {
	private final List<Path> lexicalResources;
	private final List<Path> syntacticResources;
	private final Path propertiesDefinitions;
	private final Path outputDirectory;

	public LinguisticResources(List<Path> lexicalResources,
			List<Path> syntacticResources,
			Path propertiesDefinitions,
			Path outputDirectory) {
		checkNotNull(propertiesDefinitions, "propertiesDefinitions");
		checkNotNull(outputDirectory, "outputDirectory");
		checkArgument(lexicalResources.size() < 101, "There can be at most 100 dictionaries");
		checkArgument(syntacticResources.size() < 101, "There can be at most 100 grammars");
		
		this.lexicalResources = lexicalResources;
		this.syntacticResources = syntacticResources;
		this.propertiesDefinitions = propertiesDefinitions;
		this.outputDirectory = outputDirectory;
	}

	public void loadInto(Engine engine) {
		String language = engine.Lan.isoName;
		
		try (ResourceLinker linker = new ResourceLinker(language, outputDirectory)) {
			Path docDirectory = linker.linkResources(lexicalResources,
					syntacticResources,
					propertiesDefinitions);
			
			engine.docDir = docDirectory.toAbsolutePath().toString();
			loadViaLinks(engine, language);
			engine.docDir = "";
		} catch (IOException e) {
			throw new LinguisticResourceException("Could not remove generated links.");
		}
	}

	private void loadViaLinks(Engine engine, String language) {
		RefObject<String> errMessage = new RefObject<String>("");
		try {
			boolean success = engine.loadResources(
					//Suffering from bad interface design..
					Lists.newArrayList(priorityPrefixedFileNames(lexicalResources)),
					Lists.newArrayList(priorityPrefixedFileNames(syntacticResources)),
					true,
					errMessage);
			
			if (!success) {
				throw new LinguisticResourceException(errMessage.argvalue);
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new LinguisticResourceException(errMessage.argvalue);
		}
	}

	public void loadInto(Ntext nText) {
		ArrayList<String> resources = new ArrayList<>();
		resources.addAll(priorityPostfixedFileNames(lexicalResources));
		resources.addAll(priorityPostfixedFileNames(syntacticResources));

		nText.listOfResources = resources;
	}
	
	private List<String> priorityPostfixedFileNames(List<Path> resouces) {
		List<String> resources = new ArrayList<>();
		for (int priority = 0; priority < resouces.size(); priority++) {
			resources.add(priorityPostfixFileName(resouces.get(priority), priority));
		}
		
		return resources;
	}
	
	private List<String> priorityPrefixedFileNames(List<Path> resouces) {
		List<String> resources = new ArrayList<>();
		for (int priority = 0; priority < resouces.size(); priority++) {
			resources.add(priorityPrefixFileName(resouces.get(priority), priority));
		}
		
		return resources;
	}
	
	private String priorityPrefixFileName(Path source, int priority) {
		String fileName = source.getFileName().toString();
		
		return String.format("%02d", priority) + fileName;
	}
	
	private String priorityPostfixFileName(Path source, int priority) {
		String fileName = source.getFileName().toString();
		
		return fileName + "(" + String.format("%02d", priority) + ")";
	}
	
	public static final class LinguisticResourceException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		private LinguisticResourceException(String message) {
			super(message);
		}
	}
}
