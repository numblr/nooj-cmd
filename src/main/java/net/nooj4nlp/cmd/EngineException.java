package net.nooj4nlp.cmd;

public abstract class EngineException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final String message;

	protected EngineException(String message) {
		super(message);
		this.message = message;
	}

	public String getErrorMessage() {
		return message;
	}
}
