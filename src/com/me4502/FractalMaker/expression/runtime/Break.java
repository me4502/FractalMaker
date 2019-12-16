package com.me4502.FractalMaker.expression.runtime;

/**
 * A break or continue statement.
 *
 * @author TomyLobo
 */
public class Break extends Node {
	boolean doContinue;

	public Break(int position, boolean doContinue) {
		super(position);

		this.doContinue = doContinue;
	}

	@Override
	public double getValue() throws EvaluationException {
		throw new BreakException(doContinue);
	}

	@Override
	public char id() {
		return 'b';
	}

	@Override
	public String toString() {
		return doContinue ? "continue" : "break";
	}
}