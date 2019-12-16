package com.me4502.FractalMaker.expression.runtime;

import com.me4502.FractalMaker.expression.ExpressionException;

/**
 * Thrown when there's a problem during expression evaluation.
 *
 * @author TomyLobo
 */
public class EvaluationException extends ExpressionException {
	private static final long serialVersionUID = 1L;

	public EvaluationException(int position) {
		super(position, getPrefix(position));
	}

	public EvaluationException(int position, String message, Throwable cause) {
		super(position, getPrefix(position) + ": " + message, cause);
	}

	public EvaluationException(int position, String message) {
		super(position, getPrefix(position) + ": " + message);
	}

	public EvaluationException(int position, Throwable cause) {
		super(position, getPrefix(position), cause);
	}

	private static String getPrefix(int position) {
		return position < 0 ? "Evaluation error" : "Evaluation error at " + (position + 1);
	}
}