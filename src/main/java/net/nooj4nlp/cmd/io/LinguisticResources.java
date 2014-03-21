package net.nooj4nlp.cmd.io;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.nooj4nlp.engine.Constants;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class LinguisticResources {
	private static final Path PROPERTIES_DEFINITIONS = Paths.get("_properties.def");
	
	private final List<Path> lexicalResources;
	private final List<Path> syntacticResources;
	private final Path propertiesDefinitions;
	private final String language;
	private final Path projectDirectory;

	public LinguisticResources(
			List<Path> lexicalResources,
			List<Path> syntacticResources,
			Path propertiesDefinitions,
			Language language,
			Path projectDirectory) {
		checkNotNull(projectDirectory, "outputPath");
		checkNotNull(propertiesDefinitions, "propertiesDefinitions");
		checkArgument(lexicalResources.size() < 101, "There can be at most 100 dictionaries");
		checkArgument(syntacticResources.size() < 101, "There can be at most 100 grammars");
		
		this.lexicalResources = lexicalResources;
		this.syntacticResources = syntacticResources;
		this.propertiesDefinitions = propertiesDefinitions;
		this.projectDirectory = projectDirectory;
		this.language = language.isoName;
	}

	public void loadInto(Engine engine) {
		if (!engine.Lan.isoName.equals(language)) {
			throw new LinguisticResourceException(language, "Incorrect language in engine: " + engine.Lan.isoName);
		}
		
		linkInDirectoryStructure(lexicalResources, Paths.get(Constants.LEXICAL_ANALYSIS_PATH));
		linkInDirectoryStructure(syntacticResources, Paths.get(Constants.SYNTACTIC_ANALYSIS_PATH));
		linkPropertyDefinitions();
		
		RefObject<String> errMessage = new RefObject<String>("");
		try {
			boolean success = engine.loadResources(
					//Suffering from bad interface design..
					Lists.newArrayList(prefixResourceNames(lexicalResources)),
					Lists.newArrayList(prefixResourceNames(syntacticResources)),
					true,
					errMessage);
			
			if (!success) {
				throw new LinguisticResourceException(language, errMessage.argvalue);
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new LinguisticResourceException(language, errMessage.argvalue);
		}
	}

	public void loadInto(Ntext nText) {
		if (!nText.Lan.isoName.equals(language)) {
			throw new LinguisticResourceException(language, "Incorrect language in nText: " + nText.Lan.isoName);
		}
		
		ArrayList<String> resources = new ArrayList<>();
		resources.addAll(postfixResourceNames(lexicalResources));
		resources.addAll(postfixResourceNames(syntacticResources));

		nText.listOfResources = resources;
	}
	
	private void linkInDirectoryStructure(List<Path> resourcesPaths, Path resourceDirectory) {
		Path directoryPath = createDirectoryPath(resourceDirectory);
		Map<Path, Path> linkPaths = createLinkPaths(resourcesPaths, directoryPath);
		linkSources(linkPaths, language);
	}
	
	private Path createDirectoryPath(Path resourceDirectory) {
		Path path = projectDirectory
				.resolve(Paths.get(language)
				.resolve(resourceDirectory));
		try {
			return Files.createDirectories(path);
		} catch (IOException e) {
			throw new LinguisticResourceException(language, "Could not create directories: " + path + " : " + e.getMessage());
		}
	}
	
	private Map<Path, Path> createLinkPaths(List<Path> sourcePaths, Path fullResourcePath) {
		Map<Path, Path> links = Maps.newHashMap();
		for (Path source : sourcePaths) {
			Path link = fullResourcePath.resolve(source.getFileName());
			links.put(link, source);
		}
		
		return links;
	}
	
	private void linkSources(Map<Path, Path> linkMap, String language) {
		for (Entry<Path, Path> link : linkMap.entrySet()) {
			try {
				Files.createSymbolicLink(link.getKey(), link.getValue());
			} catch (IOException e) {
				throw new LinguisticResourceException(language, "Could not create link " + link.getKey() + " to " + link.getValue() + " : " + e.getMessage());
			}
		}
	}
	
	private void linkPropertyDefinitions() {
		Path link = projectDirectory
				.resolve(language)
				.resolve(Constants.LEXICAL_ANALYSIS_PATH)
				.resolve(PROPERTIES_DEFINITIONS);
		try {
			Files.createSymbolicLink(link, propertiesDefinitions);
		} catch (IOException e) {
			throw new LinguisticResourceException(language, "Could not create link to properties definitions: " + propertiesDefinitions.toString());
		}
	}
	
	private List<String> postfixResourceNames(List<Path> resouces) {
		List<String> resources = new ArrayList<>();
		for (int priority = 0; priority < resouces.size(); priority++) {
			resources.add(priorityPostfixFileName(resouces.get(priority), priority));
		}
		
		return resources;
	}
	
	private List<String> prefixResourceNames(List<Path> resouces) {
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
		
		private final String languageName;
		
		LinguisticResourceException(String languageName, String message) {
			super(message);
			this.languageName = languageName;
		}

		public String getLanguageName() {
			return languageName;
		}
	}
}
