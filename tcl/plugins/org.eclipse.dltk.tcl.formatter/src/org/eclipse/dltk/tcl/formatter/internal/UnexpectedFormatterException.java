package org.eclipse.dltk.tcl.formatter.internal;

public class UnexpectedFormatterException extends RuntimeException {

	private static final long serialVersionUID = -8485937726428204124L;

	/**
	 * @param message
	 * @param cause
	 */
	public UnexpectedFormatterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UnexpectedFormatterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnexpectedFormatterException(Throwable cause) {
		super(cause);
	}

}
