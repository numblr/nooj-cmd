package net.nooj4nlp.cmd.processing;

import java.util.ArrayList;
import java.util.HashMap;

import net.nooj4nlp.cmd.EngineException;
import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.UnsignedShort;

public class TextDelimiter implements NtextProcessor {
	private static final String LIMIT_EXCEEDED = "Exceeded limit for text unit of " + UnsignedShort.MAX_VALUE + " characters";
	
	private final Engine engine;

	public TextDelimiter(Engine engine) {
		this.engine = engine;
	}
	
	@Override
	public void process(Ntext nText) {
		String errorMessage = "";
		if (nText.XmlNodes != null) {
			errorMessage = delimitXml(nText);
		} else {
			delimitRawText(nText);
			errorMessage = LIMIT_EXCEEDED;
		}
		
		if (nText.mft == null) {
			throw new DelimiterException(errorMessage);
		}
	}

	private void delimitRawText(Ntext nText) {
		if (nText.mft == null) {
			nText.mft = engine.delimit(nText);
		} else {
			nText.mft.resetTransitions();
		}

		nText.annotations = new ArrayList<>();
		nText.hLexemes = new HashMap<>();
	}

	private String delimitXml(Ntext nText) {
		if (nText.mft == null) {
			return engine.delimitXmlTextUnitsAndImportXmlTags(null, nText);
		}
		
		return "";
	}
	
	public static final class DelimiterException extends EngineException {
		private static final long serialVersionUID = 1L;
		
		private DelimiterException(String message) {
			super(message);
		}
	}
}
