package net.nooj4nlp.cmd.io;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.nooj4nlp.engine.Constants;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class LinguisticResources {
	private final List<String> lexicalResources;
	private final List<String> syntacticResources;
	private final Path outputPath;
	private String language;

	public LinguisticResources(
			List<String> lexicalResources,
			List<String> syntacticResources,
			Language language,
			Path outputPath) {
		checkNotNull(outputPath, "outputPath");
		checkArgument(lexicalResources.size() < 101, "There can be at most 100 dictionaries");
		checkArgument(syntacticResources.size() < 101, "There can be at most 100 grammars");
		
		this.lexicalResources = lexicalResources;
		this.syntacticResources = syntacticResources;
		this.outputPath = outputPath;
		this.language = language.isoName;
	}

	public void loadInto(Engine engine) {
		if (!engine.Lan.isoName.equals(language)) {
			throw new LinguisticResourceException(language, "Incorrect language in engine: " + engine.Lan.isoName);
		}
		
		linkInDirectoryStructure(lexicalResources, Constants.LEXICAL_ANALYSIS_PATH);
		linkInDirectoryStructure(syntacticResources, Constants.SYNTACTIC_ANALYSIS_PATH);
		
		RefObject<String> errMessage = new RefObject<String>("");
		try {
			boolean success = engine.loadResources(
					//Suffering from bad interface design..
					Lists.newArrayList(lexicalResources),
					Lists.newArrayList(syntacticResources),
					true,
					errMessage);
			
			if (!success) {
				throw new LinguisticResourceException(languageName, errMessage.argvalue);
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new LinguisticResourceException(languageName, errMessage.argvalue);
		}
	}

	public void loadInto(Ntext nText) {
		String languageName = nText.Lan.isoName;
		
		ArrayList<String> resources = new ArrayList<>();
		resources.addAll(formatResourceNames(lexicalResources.get(languageName)));
		resources.addAll(formatResourceNames(syntacticResources.get(languageName)));

		nText.listOfResources = resources;
	}
	
	private List<String> formatResourceNames(List<String> resouces) {
		List<String> resources = new ArrayList<>();
		for (int priority = 0; priority < resouces.size(); priority++) {
			resources.add(prioriytPostfixedFileName(resouces.get(priority), priority));
		}

		return resources;
	}
	
	private void linkInDirectoryStructure(List<String> resources, String directoryPath) {
		createDirectoryStructure(resources.keySet(), directoryPath);
			
		for (String language : resources.keys()) {
			Map<Path, Path> linkPaths = createLinkPaths(language,
					resources.get(language), directoryPath);
			linkSources(linkPaths, language);
		}
	}

	private void linkSources(Map<Path, Path> linkMap, String language) {
		for (Entry<Path, Path> link : linkMap.entrySet()) {
			try {
				Files.createSymbolicLink(link.getKey(), link.getValue());
			} catch (IOException e) {
				throw new LinguisticResourceException(language, "Could not create link " + link.getKey() + " to " + link.getValue());
			}
		}
	}
	
	private void createDirectoryStructure(Set<String> languages, String directoryPath) {
		for (String language : languages) {
			Path path = outputPath.resolve(Paths.get(
					new UID().toString().replace(":", ""),
					language,
					directoryPath));
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new LinguisticResourceException(language, "Could not create link directories: " + path);
			}
		}
	}
	
	private Map<Path, Path> createLinkPaths(String language,
			List<String> sourcePaths,
			String directoryPath) {
		Map<Path, Path> links = Maps.newHashMap();
		for (int priority = 0; priority < sourcePaths.size(); priority++) {
			Path source = Paths.get(sourcePaths.get(priority));
			Path link = outputPath
					.resolve(Paths.get(language, directoryPath))
					.resolve(prioriytPrependedFileName(source, priority));
			links.put(link, source);
		}
		
		return links;
	}
	
	@SuppressWarnings("resource")
	private String prioriytPrependedFileName(Path source, int priority) {
		String fileName = source.getFileName().toString();
		
		return new Formatter().format("%d02", priority).toString() + fileName;
	}
	
	@SuppressWarnings("resource")
	private String prioriytPostfixedFileName(String source, int priority) {
		return FilenameUtils.getName(source) +
				"(" + new Formatter().format("%d02", priority).toString() + ")";
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
