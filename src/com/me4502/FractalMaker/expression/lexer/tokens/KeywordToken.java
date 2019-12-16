package com.me4502.FractalMaker.expression.lexer.tokens;

/**
 * A keyword
 *
 * @author TomyLobo
 */
public class KeywordToken extends Token {
	public final String value;

	public KeywordToken(int position, String value) {
		super(position);
		this.value = value;
	}

	@Override
	public char id() {
		return 'k';
	}

	@Override
	public String toString() {
		return "KeywordToken(" + value + ")";
	}
}