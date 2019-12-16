package com.me4502.FractalMaker.expression.lexer.tokens;

/**
 * A unary or binary operator.
 *
 * @author TomyLobo
 */
public class OperatorToken extends Token {
	public final String operator;

	public OperatorToken(int position, String operator) {
		super(position);
		this.operator = operator;
	}

	@Override
	public char id() {
		return 'o';
	}

	@Override
	public String toString() {
		return "OperatorToken(" + operator + ")";
	}
}