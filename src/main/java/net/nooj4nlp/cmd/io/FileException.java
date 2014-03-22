package net.nooj4nlp.cmd.io;

import java.nio.file.Path;

class FileException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final String path;

	FileException(Path file, String errorMessage) {
		super(errorMessage);
		this.path = file.toAbsolutePath().toString();
	}
	
	public String getPath() {
		return path;
	}
}
