package net.nooj4nlp.cmd.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.text.BadLocationException;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.TextIO;

public class TextLoader {
	private static final Charset UTF_8 = Charset.forName("UTF-8");
	
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
					encoding.getTypeOrdinal(),
					encoding.getCode(),
					encoding.getTypeName(),
					language.chartable);
		} catch (IOException | BadLocationException e) {
			throw new TextLoaderException(file, e.getMessage());
		}
		
		if (text == null) {
			throw new TextLoaderException(file, "Unsupported encoding: " + encoding.getTypeName());
		}
		
		return text;
	}
	
	public void write(String text, File file) {
		Path path = file.toPath();
		try (BufferedWriter fileWriter = Files.newBufferedWriter(path, UTF_8)) {
			fileWriter.write(text);
		} catch (IOException e) {
			throw new TextLoaderException(file, e.getMessage());
		}
	}
	
	public static class TextLoaderException extends FileException {
		private static final long serialVersionUID = 1L;

		private TextLoaderException(File file, String errorMessage) {
			super(file, errorMessage);
		}
	}
	
}
