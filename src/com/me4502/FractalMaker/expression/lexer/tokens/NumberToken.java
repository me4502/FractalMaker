package com.me4502.FractalMaker.expression.lexer.tokens;

/**
 * A number
 *
 * @author TomyLobo
 */
public class NumberToken extends Token {
	public final double value;

	public NumberToken(int position, double value) {
		super(position);
		this.value = value;
	}

	@Override
	public char id() {
		return '0';
	}

	@Override
	public String toString() {
		return "NumberToken(" + value + ")";
	}
}