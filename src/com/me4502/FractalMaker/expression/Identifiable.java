package com.me4502.FractalMaker.expression;

/**
 * A common superinterface for everything passed to parser processors.
 *
 * @author TomyLobo
 */
public interface Identifiable {
	/**
	 * Returns a character that helps identify the token, pseudo-token or invokable in question.
	 *
	 * <pre>
	 * Tokens:
	 * i - IdentifierToken
	 * 0 - NumberToken
	 * o - OperatorToken
	 * \0 - NullToken
	 * CharacterTokens are returned literally
	 *
	 * PseudoTokens:
	 * p - UnaryOperator
	 *
	 * Nodes:
	 * c - Constant
	 * v - Variable
	 * f - Function
	 * l - LValueFunction
	 * s - Sequence
	 * I - Conditional
	 * w - While
	 * F - For
	 * r - Return
	 * b - Break (includes continue)
	 * S - SimpleFor
	 * C - Switch
	 * </pre>
	 */
	public abstract char id();

	public int getPosition();
}