package com.me4502.FractalMaker.expression.lexer.tokens;

/**
 * A single character that doesn't fit any of the other token categories.
 *
 * @author TomyLobo
 */
public class CharacterToken extends Token {
	public final char character;

	public CharacterToken(int position, char character) {
		super(position);
		this.character = character;
	}

	@Override
	public char id() {
		return character;
	}

	@Override
	public String toString() {
		return "CharacterToken(" + character + ")";
	}
}