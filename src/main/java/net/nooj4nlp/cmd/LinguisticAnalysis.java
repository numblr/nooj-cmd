package net.nooj4nlp.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

public class LinguisticAnalysis {
	private final Engine engine;

	public LinguisticAnalysis(Engine engine) {
		this.engine = engine;
	}
	
	public void analyze(Ntext nText) {
		RefObject<String> errorMessage = new RefObject<>("");
		delimitText(nText);
		lexicalAnalyze(nText, errorMessage);
		syntacticParse(nText, errorMessage);
	}
	
	private void delimitText(Ntext nText) throws LexicalAnalysisException {
		if (nText.mft == null) {
			if (nText.XmlNodes == null) {
				delimitRawText(nText);
			} else {
				engine.delimitXmlTextUnitsAndImportXmlTags(null, nText);
			}
		} else if (nText.XmlNodes == null) {
			reset(nText);
		}
		
		if (nText.mft == null) {
			throw new DelimitException();
		}
	}
	
	private void reset(Ntext nText) {
		nText.mft.resetTransitions();
		nText.annotations = new ArrayList<>();
		nText.hLexemes = new HashMap<>();
	}
	
	private void delimitRawText(Ntext nText) {
		nText.mft = engine.delimit(nText);
		nText.annotations = new ArrayList<>();
		nText.hLexemes = new HashMap<>();
	}

	private void lexicalAnalyze(Ntext nText, RefObject<String> errorMessage) {
		nText.hUnknowns = new HashMap<>();
		boolean success = engine.tokenize(null,
				nText,
				nText.annotations,
				new HashMap<String, ArrayList<String>>(), 
				errorMessage);
		
		if (!success) {
			throw new LexicalAnalysisException(errorMessage.argvalue);
		}
	}
	
	private void syntacticParse(Ntext nText, RefObject<String> errorMessage) {
		if (nText.XmlNodes == null) {
			nText.hPhrases = new HashMap<>();
		}

		applySyntax(0, nText, errorMessage);
		nText.cleanupBadAnnotations(nText.annotations);
		applySyntax(1, nText, errorMessage);
	}
	
	private void applySyntax(int startingPoint, Ntext nText, RefObject<String> errorMessage) {
		if (engine.synGrms == null || engine.synGrms.size() <= 0) {
			return;
		}
		
		boolean success = false;
		try {
			success = engine.applyAllGrammars(null,
					nText,
					nText.annotations,
					startingPoint,
					errorMessage);
		} catch (ClassNotFoundException | IOException e) {
			throw new GrammarException(errorMessage.argvalue);
		}
		
		if (!success && errorMessage != null) {
			throw new SyntaxParsingException(errorMessage.argvalue);
		}
		
		if (!success) {
			nText.hPhrases = null;
		}
	}
	
	public static class AnalysisException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		private final String message;

		AnalysisException(String message) {
			this.message = message;
		}

		public String getErrorMessage() {
			return message;
		}
	}
	
	public static final class LexicalAnalysisException extends AnalysisException {
		private static final long serialVersionUID = 1L;
		
		LexicalAnalysisException(String message) {
			super(message);
		}
	}
	
	public static final class GrammarException extends AnalysisException {
		private static final long serialVersionUID = 1L;
		
		GrammarException(String message) {
			super(message);
		}
	}
	
	public static final class SyntaxParsingException extends AnalysisException {
		private static final long serialVersionUID = 1L;
		
		SyntaxParsingException(String message) {
			super(message);
		}
	}
	
	public static final class DelimitException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
}

