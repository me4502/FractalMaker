package com.me4502.FractalMaker.expression.runtime;

/**
 * A return statement.
 *
 * @author TomyLobo
 */
public class Return extends Node {
	RValue value;

	public Return(int position, RValue value) {
		super(position);

		this.value = value;
	}

	@Override
	public double getValue() throws EvaluationException {
		throw new ReturnException(value.getValue());
	}

	@Override
	public char id() {
		return 'r';
	}

	@Override
	public String toString() {
		return "return " + value;
	}
}