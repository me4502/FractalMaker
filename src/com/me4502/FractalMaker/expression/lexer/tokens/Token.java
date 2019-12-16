package com.me4502.FractalMaker.expression.lexer.tokens;

import com.me4502.FractalMaker.expression.Identifiable;

/**
 * A token. The lexer generates these to make the parser's job easier.
 *
 * @author TomyLobo
 */
public abstract class Token implements Identifiable {
	private final int position;

	public Token(int position) {
		this.position = position;
	}

	@Override
	public int getPosition() {
		return position;
	}
}