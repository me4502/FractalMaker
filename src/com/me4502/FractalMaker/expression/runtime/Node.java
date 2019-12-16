package com.me4502.FractalMaker.expression.runtime;

/**
 * A node in the execution tree of an expression.
 *
 * @author TomyLobo
 */
public abstract class Node implements RValue {
	private final int position;

	public Node(int position) {
		this.position = position;
	}

	@Override
	public abstract String toString();

	@Override
	public RValue optimize() throws EvaluationException {
		return this;
	}

	@Override
	public final int getPosition() {
		return position;
	}
}