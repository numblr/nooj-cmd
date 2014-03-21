package net.nooj4nlp.cmd.io;

import java.io.File;

class FileException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final String path;

	FileException(File file, String errorMessage) {
		super(errorMessage);
		this.path = file.getAbsolutePath();
	}
	
	public String getPath() {
		return path;
	}
}
