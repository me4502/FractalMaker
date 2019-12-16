package com.me4502.FractalMaker.expression.parser;

import com.me4502.FractalMaker.expression.lexer.tokens.OperatorToken;

/**
 * The parser uses this pseudo-token to mark operators as unary operators.
 *
 * @author TomyLobo
 */
public class UnaryOperator extends PseudoToken {
	final String operator;

	public UnaryOperator(OperatorToken operatorToken) {
		this(operatorToken.getPosition(), operatorToken.operator);
	}

	public UnaryOperator(int position, String operator) {
		super(position);
		this.operator = operator;
	}

	@Override
	public char id() {
		return 'p';
	}

	@Override
	public String toString() {
		return "UnaryOperator(" + operator + ")";
	}
}