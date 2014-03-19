package net.nooj4nlp.cmd;

import java.util.ArrayList;
import java.util.HashMap;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;

public class TextDelimiter implements NtextProcessor {
	private final Engine engine;

	public TextDelimiter(Engine engine) {
		this.engine = engine;
	}
	
	@Override
	public void process(Ntext nText) {
		if (nText.XmlNodes != null) {
			delimitXml(nText);
		} else {
			delimitRawText(nText);
		}
	}

	private void delimitRawText(Ntext nText) {
		if (nText.mft == null) {
			nText.mft = engine.delimit(nText);
		} else {
			nText.mft.resetTransitions();
		}
		
		if (nText.mft == null) {
			throw new DelimiterException();
		}

		nText.annotations = new ArrayList<>();
		nText.hLexemes = new HashMap<>();
	}

	private void delimitXml(Ntext nText) {
		if (nText.mft == null) {
			engine.delimitXmlTextUnitsAndImportXmlTags(null, nText);
		}
	}
	
	public static final class DelimiterException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
}
