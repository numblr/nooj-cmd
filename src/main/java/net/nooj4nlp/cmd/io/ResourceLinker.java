package net.nooj4nlp.cmd.io;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.UID;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.nooj4nlp.cmd.io.LinguisticResources.ResourceLinkCreationException;
import net.nooj4nlp.engine.Constants;
import net.nooj4nlp.engine.Engine;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Maps;

/**
 * Creates temporary symbolic links to the given resources satisfying the
 * directory structure required by {@link Engine}.
 * 
 * The created links are removed again by calling {@link #close()}.
 */
final class ResourceLinker implements AutoCloseable {
	private static final Path PROPERTIES_DEFINITIONS = Paths.get("_properties.def");
	
	private final String language;
	private final Path tmpDirectory;

	ResourceLinker(String language, Path tmpDirectory) {
		this.language = checkNotNull(language);
		this.tmpDirectory = tmpDirectory.resolve(new UID().toString().replaceAll("\\W", ""));
	}
	
	@Override
	public void close() throws IOException {
		FileUtils.deleteDirectory(tmpDirectory.toFile());
	}
	
	Path linkResources(List<Path> lexicalResources,
			List<Path> syntacticResources,
			Path propertiesDefinitions) {
		linkInDirectoryStructure(lexicalResources,
				Paths.get(Constants.LEXICAL_ANALYSIS_PATH));
		linkInDirectoryStructure(syntacticResources,
				Paths.get(Constants.SYNTACTIC_ANALYSIS_PATH));
		linkPropertyDefinitions(propertiesDefinitions);
		
		return tmpDirectory;
	}
	
	private Path linkInDirectoryStructure(List<Path> resourcesPaths, Path resourceDirectory) {
		Path directoryPath = createDirectoryPath(resourceDirectory);
		Map<Path, Path> linkPaths = createLinkPaths(resourcesPaths, directoryPath);
		linkSources(linkPaths);
		
		return directoryPath;
	}
	
	private Path createDirectoryPath(Path resourceDirectory) {
		Path path = tmpDirectory
				.resolve(Paths.get(language)
				.resolve(resourceDirectory));
		try {
			return Files.createDirectories(path);
		} catch (IOException e) {
			throw new ResourceLinkCreationException(path, e.getMessage());
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
	
	private void linkSources(Map<Path, Path> linkMap) {
		for (Entry<Path, Path> link : linkMap.entrySet()) {
			try {
				Files.createSymbolicLink(link.getKey(), link.getValue());
			} catch (IOException e) {
				throw new ResourceLinkCreationException(link.getKey(), e.getMessage());
			}
		}
	}
	
	private void linkPropertyDefinitions(Path propertiesDefinitions) {
		Path link = tmpDirectory
				.resolve(language)
				.resolve(Constants.LEXICAL_ANALYSIS_PATH)
				.resolve(PROPERTIES_DEFINITIONS);
		try {
			Files.createSymbolicLink(link, propertiesDefinitions);
		} catch (IOException e) {
			throw new ResourceLinkCreationException(propertiesDefinitions, e.getMessage());
		}
	}
}
