package net.nooj4nlp.cmd;

abstract class EngineException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final String message;

	EngineException(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return message;
	}
}
