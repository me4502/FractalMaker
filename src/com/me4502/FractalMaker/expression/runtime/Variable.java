package com.me4502.FractalMaker.expression.runtime;

/**
 * A variable.
 *
 * @author TomyLobo
 */
public final class Variable extends Node implements LValue {
	public double value;

	public Variable(double value) {
		super(-1);
		this.value = value;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "var";
	}

	@Override
	public char id() {
		return 'v';
	}

	@Override
	public double assign(double value) {
		return this.value = value;
	}
}