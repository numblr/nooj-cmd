package net.nooj4nlp.cmd.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.nooj4nlp.cmd.EngineException;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

public final class LinguisticResources {
	private static final Supplier<List<String>> ARRAY_LIST_SUPPLIER = new Supplier<List<String>>() {
		@Override
		public List<String> get() {
			return new ArrayList<>();
		}
	};

	private final ListMultimap<String, String> lexicalResources;
	private final ListMultimap<String, String> syntacticResources;

	public LinguisticResources(
			Map<String, Collection<String>> lexicalResources,
			Map<String, Collection<String>> syntacticResources) {
		this.lexicalResources = ImmutableListMultimap.copyOf(Multimaps
				.newListMultimap(lexicalResources, ARRAY_LIST_SUPPLIER));
		this.syntacticResources = ImmutableListMultimap.copyOf(Multimaps
				.newListMultimap(syntacticResources, ARRAY_LIST_SUPPLIER));
	}

	public void loadInto(Engine engine) {
		String languageName = engine.Lan.isoName;
		if (!lexicalResources.containsKey(languageName)) {
			throw new LinguisticResourceException(languageName, "");
		}

		RefObject<String> errMessage = new RefObject<String>("");
		try {
			boolean success = engine.loadResources(
					//WTF..
					(ArrayList<String>)lexicalResources.get(languageName),
					(ArrayList<String>)syntacticResources.get(languageName),
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
		resources.addAll(formatResourceName(lexicalResources.get(languageName)));
		resources.addAll(formatResourceName(syntacticResources.get(languageName)));

		nText.listOfResources = resources;
	}
	
	private List<String> formatResourceName(List<String> resouces) {
		ArrayList<String> resources = new ArrayList<>();
		for (String path : resouces) {
			String fileName = FilenameUtils.getName(path);
			String priority = fileName.substring(0, 2);
			fileName = fileName.substring(2);
			
			resources.add(fileName + "(" + priority + ")");
		}

		return resources;
	}

	public static final class LinguisticResourceException extends EngineException {
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
