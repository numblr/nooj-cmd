package net.nooj4nlp.cmd.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.text.BadLocationException;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.TextIO;

public class FileIO {
	public static final Charset OUTPUT_ENCODING = Charset.forName("UTF-8");
	
	private Encoding encoding;
	private Language language;

	public FileIO(Encoding encoding, Language language) {
		this.language = language;
		this.encoding = encoding;
	}
	
	public String load(Path file) {
		String text;
		try {
			text = TextIO.loadText(file.toAbsolutePath().toString(),
					encoding.getFileTypeOrdinal(),
					encoding.getEncoding(),
					encoding.getFileTypeName(),
					language.chartable);
		} catch (IOException | BadLocationException e) {
			throw new FileIOLoadException(file, e.getMessage());
		}
		
		if (text == null) {
			throw new FileIOUnsupportedTypeException(file, encoding.getFileTypeName());
		}
		
		return text;
	}
	
	public void write(String text, Path file) {
		try (BufferedWriter fileWriter = Files.newBufferedWriter(file, OUTPUT_ENCODING)) {
			fileWriter.write(text);
		} catch (IOException e) {
			throw new FileIOWriteException(file, e.getMessage());
		}
	}
	
	public static class FileIOLoadException extends FileException {
		private static final long serialVersionUID = 1L;

		private FileIOLoadException(Path file, String errorMessage) {
			super(file, errorMessage);
		}
	}
	
	public static class FileIOUnsupportedTypeException extends FileIOLoadException {
		private static final long serialVersionUID = 1L;
		
		private FileIOUnsupportedTypeException(Path file, String errorMessage) {
			super(file, errorMessage);
		}
	}
	
	public static class FileIOWriteException extends FileException {
		private static final long serialVersionUID = 1L;
		
		private FileIOWriteException(Path file, String errorMessage) {
			super(file, errorMessage);
		}
	}
	
}
