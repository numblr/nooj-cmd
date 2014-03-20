package net.nooj4nlp.cmd.io;

import java.io.File;

class FileException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final String path;
	private final String errorMessage;

	FileException(File file, String errorMessage) {
		this.path = file.getAbsolutePath();
		this.errorMessage = errorMessage;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
