package com.github.intrigus.ftd.exception;

/**
 * Thrown when a required computation failed.
 *
 */
@SuppressWarnings("serial")
public class ComputationFailedException extends Exception {
	public ComputationFailedException(String message) {
		super(message);
	}

	public ComputationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComputationFailedException(Throwable cause) {
		super(cause);
	}

}
