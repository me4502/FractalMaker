package com.me4502.FractalMaker.expression.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.me4502.FractalMaker.expression.lexer.tokens.CharacterToken;
import com.me4502.FractalMaker.expression.lexer.tokens.IdentifierToken;
import com.me4502.FractalMaker.expression.lexer.tokens.KeywordToken;
import com.me4502.FractalMaker.expression.lexer.tokens.NumberToken;
import com.me4502.FractalMaker.expression.lexer.tokens.OperatorToken;
import com.me4502.FractalMaker.expression.lexer.tokens.Token;

/**
 * Processes a string into a list of tokens.
 *
 * Tokens can be numbers, identifiers, operators and assorted other characters.
 *
 * @author TomyLobo
 */
public class Lexer {
	private final String expression;
	private int position = 0;

	private Lexer(String expression) {
		this.expression = expression;
	}

	public static final List<Token> tokenize(String expression) throws LexerException {
		return new Lexer(expression).tokenize();
	}

	private final DecisionTree operatorTree = new DecisionTree(null,
			'+', new DecisionTree("+",
					'=', new DecisionTree("+="),
					'+', new DecisionTree("++")
					),
					'-', new DecisionTree("-",
							'=', new DecisionTree("-="),
							'-', new DecisionTree("--")
							),
							'*', new DecisionTree("*",
									'=', new DecisionTree("*="),
									'*', new DecisionTree("**")
									),
									'/', new DecisionTree("/",
											'=', new DecisionTree("/=")
											),
											'%', new DecisionTree("%",
													'=', new DecisionTree("%=")
													),
													'^', new DecisionTree("^",
															'=', new DecisionTree("^=")
															),
															'=', new DecisionTree("=",
																	'=', new DecisionTree("==")
																	),
																	'!', new DecisionTree("!",
																			'=', new DecisionTree("!=")
																			),
																			'<', new DecisionTree("<",
																					'<', new DecisionTree("<<"),
																					'=', new DecisionTree("<=")
																					),
																					'>', new DecisionTree(">",
																							'>', new DecisionTree(">>"),
																							'=', new DecisionTree(">=")
																							),
																							'&', new DecisionTree(null, // not implemented
																									'&', new DecisionTree("&&")
																									),
																									'|', new DecisionTree(null, // not implemented
																											'|', new DecisionTree("||")
																											),
																											'~', new DecisionTree("~",
																													'=', new DecisionTree("~=")
																													)
			);

	private static final Set<Character> characterTokens = new HashSet<Character>();
	static {
		characterTokens.add(',');
		characterTokens.add('(');
		characterTokens.add(')');
		characterTokens.add('{');
		characterTokens.add('}');
		characterTokens.add(';');
		characterTokens.add('?');
		characterTokens.add(':');
	}

	private static final Set<String> keywords = new HashSet<String>(Arrays.asList("if", "else", "while", "do", "for", "break", "continue", "return", "switch", "case", "default"));

	private static final Pattern numberPattern = Pattern.compile("^([0-9]*(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?)");
	private static final Pattern identifierPattern = Pattern.compile("^([A-Za-z][0-9A-Za-z_]*)");

	private final List<Token> tokenize() throws LexerException {
		List<Token> tokens = new ArrayList<Token>();

		do {
			skipWhitespace();
			if (position >= expression.length()) {
				break;
			}

			Token token = operatorTree.evaluate(position);
			if (token != null) {
				tokens.add(token);
				continue;
			}

			final char ch = peek();

			if (characterTokens.contains(ch)) {
				tokens.add(new CharacterToken(position++, ch));
				continue;
			}

			final Matcher numberMatcher = numberPattern.matcher(expression.substring(position));
			if (numberMatcher.lookingAt()) {
				String numberPart = numberMatcher.group(1);
				if (numberPart.length() > 0) {
					try {
						tokens.add(new NumberToken(position, Double.parseDouble(numberPart)));
					} catch (NumberFormatException e) {
						throw new LexerException(position, "Number parsing failed", e);
					}

					position += numberPart.length();
					continue;
				}
			}

			final Matcher identifierMatcher = identifierPattern.matcher(expression.substring(position));
			if (identifierMatcher.lookingAt()) {
				String identifierPart = identifierMatcher.group(1);
				if (identifierPart.length() > 0) {
					if (keywords.contains(identifierPart)) {
						tokens.add(new KeywordToken(position, identifierPart));
					} else {
						tokens.add(new IdentifierToken(position, identifierPart));
					}

					position += identifierPart.length();
					continue;
				}
			}

			throw new LexerException(position, "Unknown character '" + ch + "'");
		} while (position < expression.length());

		return tokens;
	}

	private char peek() {
		return expression.charAt(position);
	}

	private final void skipWhitespace() {
		while (position < expression.length() && Character.isWhitespace(peek())) {
			++position;
		}
	}

	public class DecisionTree {
		private final String tokenName;
		private final Map<Character, DecisionTree> subTrees = new HashMap<Character, Lexer.DecisionTree>();

		private DecisionTree(String tokenName, Object... args) {
			this.tokenName = tokenName;

			if (args.length % 2 != 0) {
				throw new UnsupportedOperationException("You need to pass an even number of arguments.");
			}

			for (int i = 0; i < args.length; i += 2) {
				if (!(args[i] instanceof Character)) {
					throw new UnsupportedOperationException("Argument #" + i + " expected to be 'Character', not '" + args[i].getClass().getName() + "'.");
				}
				if (!(args[i + 1] instanceof DecisionTree)) {
					throw new UnsupportedOperationException("Argument #" + (i + 1) + " expected to be 'DecisionTree', not '" + args[i + 1].getClass().getName() + "'.");
				}

				Character next = (Character) args[i];
				DecisionTree subTree = (DecisionTree) args[i + 1];

				subTrees.put(next, subTree);
			}
		}

		private Token evaluate(int startPosition) throws LexerException {
			if (position < expression.length()) {
				final char next = peek();

				final DecisionTree subTree = subTrees.get(next);
				if (subTree != null) {
					++position;
					final Token subTreeResult = subTree.evaluate(startPosition);
					if (subTreeResult != null) {
						return subTreeResult;
					}
					--position;
				}
			}

			if (tokenName == null) {
				return null;
			}

			return new OperatorToken(startPosition, tokenName);
		}
	}
}