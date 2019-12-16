package com.me4502.FractalMaker.expression;

/**
 * Thrown when there's a problem during any stage of the expression compilation or evaluation.
 *
 * @author TomyLobo
 */
public class ExpressionException extends Exception {
	private static final long serialVersionUID = 1L;

	private final int position;

	public ExpressionException(int position) {
		this.position = position;
	}

	public ExpressionException(int position, String message, Throwable cause) {
		super(message, cause);
		this.position = position;
	}

	public ExpressionException(int position, String message) {
		super(message);
		this.position = position;
	}

	public ExpressionException(int position, Throwable cause) {
		super(cause);
		this.position = position;
	}

	public int getPosition() {
		return position;
	}
}