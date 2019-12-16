package com.me4502.FractalMaker.expression.parser;

import com.me4502.FractalMaker.expression.Identifiable;

/**
 * A pseudo-token, inserted by the parser instead of the lexer.
 *
 * @author TomyLobo
 */
public abstract class PseudoToken implements Identifiable {
	private final int position;

	public PseudoToken(int position) {
		this.position = position;
	}

	@Override
	public abstract char id();

	@Override
	public int getPosition() {
		return position;
	}
}