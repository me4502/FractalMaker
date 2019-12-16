package com.me4502.FractalMaker.expression.runtime;

/**
 * A constant.
 *
 * @author TomyLobo
 */
public final class Constant extends Node {
	private final double value;

	public Constant(int position, double value) {
		super(position);
		this.value = value;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public char id() {
		return 'c';
	}
}