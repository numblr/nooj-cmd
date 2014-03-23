package net.nooj4nlp.cmd.processing;

import java.util.ArrayList;
import java.util.HashMap;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;

public final class TextDelimiter implements NtextProcessor {
	private final Engine engine;

	public TextDelimiter(Engine engine) {
		this.engine = engine;
	}
	
	@Override
	public void process(Ntext nText) {
		if (nText.XmlNodes != null) {
			String errorMessage = delimitXml(nText);
			if (nText.mft == null) {
				throw new DelimiterException(errorMessage);
			}
		} else {
			delimitRawText(nText);
			if (nText.mft == null) {
				throw new LimitExeededException();
			}
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
	
	public static final class DelimiterException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		private DelimiterException(String message) {
			super(message);
		}
	}
	
	public static final class LimitExeededException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
}
