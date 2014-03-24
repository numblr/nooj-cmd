package net.nooj4nlp.cmd.io;

import java.nio.file.Path;

@SuppressWarnings("serial")
class FileException extends RuntimeException {
	private final String path;

	FileException(Path file, String errorMessage) {
		super(errorMessage);
		this.path = file.toAbsolutePath().toString();
	}
	
	public String getPath() {
		return path;
	}
}
