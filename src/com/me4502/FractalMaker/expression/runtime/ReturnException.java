package com.me4502.FractalMaker.expression.runtime;

/**
 * Thrown when a return statement is encountered.
 * {@link com.sk89q.worldedit.expression.Expression#evaluate} catches this exception and returns the enclosed value.
 *
 * @author TomyLobo
 */
public class ReturnException extends EvaluationException {
	private static final long serialVersionUID = 1L;

	final double value;

	public ReturnException(double value) {
		super(-1);

		this.value = value;
	}

	public double getValue() {
		return value;
	}
}