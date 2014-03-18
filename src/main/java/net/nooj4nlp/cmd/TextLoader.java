package net.nooj4nlp.cmd;

import java.io.File;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.TextIO;

public class TextLoader {
	private Encoding encoding;
	private Language language;

	public TextLoader(Encoding encoding, Language language) {
		this.language = language;
		this.encoding = encoding;
	}
	
	public String load(File file) {
		String text;
		try {
			text = TextIO.loadText(file.getAbsolutePath(),
					encoding.getTypeCode(),
					encoding.getCode(),
					encoding.getTypeName(),
					language.chartable);
		} catch (IOException | BadLocationException e) {
			throw new TextLoaderException(file, e.getMessage());
		}
		
		if (text == null) {
			throw new TextLoaderException(file, "");
		}
		
		return text;
	}
	
	public static class TextLoaderException extends FileException {
		private static final long serialVersionUID = 1L;

		private TextLoaderException(File file, String errorMessage) {
			super(file, errorMessage);
		}
	}
	
}
