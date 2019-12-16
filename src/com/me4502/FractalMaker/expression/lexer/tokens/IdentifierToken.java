package com.me4502.FractalMaker.expression.lexer.tokens;

/**
 * An identifier
 *
 * @author TomyLobo
 */
public class IdentifierToken extends Token {
	public final String value;

	public IdentifierToken(int position, String value) {
		super(position);
		this.value = value;
	}

	@Override
	public char id() {
		return 'i';
	}

	@Override
	public String toString() {
		return "IdentifierToken(" + value + ")";
	}
}