package com.me4502.FractalMaker.expression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.me4502.FractalMaker.expression.lexer.Lexer;
import com.me4502.FractalMaker.expression.lexer.tokens.Token;
import com.me4502.FractalMaker.expression.parser.Parser;
import com.me4502.FractalMaker.expression.runtime.Constant;
import com.me4502.FractalMaker.expression.runtime.EvaluationException;
import com.me4502.FractalMaker.expression.runtime.RValue;
import com.me4502.FractalMaker.expression.runtime.ReturnException;
import com.me4502.FractalMaker.expression.runtime.Variable;

/**
 * Compiles and evaluates expressions.
 *
 * Supported operators:
 * Logical: &&, ||, ! (unary)
 * Bitwise: ~ (unary), >>, <<
 * Arithmetic: +, -, *, /, % (modulo), ^ (power), - (unary), --, ++ (prefix only)
 * Comparison: <=, >=, >, <, ==, !=, ~= (near)
 *
 * Supported functions: abs, acos, asin, atan, atan2, cbrt, ceil, cos, cosh, exp, floor, ln, log, log10, max, max, min, min, rint, round, sin, sinh, sqrt, tan, tanh
 *
 * Constants: e, pi
 *
 * To compile an equation, run <code>Expression.compile("expression here", "var1", "var2"...)</code>
 * If you wish to run the equation multiple times, you can then optimize it, by calling myExpression.optimize();
 * You can then run the equation as many times as you want by calling myExpression.evaluate(var1, var2...)
 * You do not need to pass values for all variables specified while compiling.
 * To query variables after evaluation, you can use myExpression.getVariable("variable name").
 * To get a value out of these, use myVariable.getValue()
 *
 * Variables are also supported and can be set either by passing values to <code>evaluate</code>
 *
 * @author TomyLobo
 */
public class Expression {
	private static final ThreadLocal<Stack<Expression>> instance = new ThreadLocal<Stack<Expression>>();

	private final Map<String, RValue> variables = new HashMap<String, RValue>();
	private final String[] variableNames;
	private RValue root;
	private final Map<Integer, double[]> megabuf = new HashMap<Integer, double[]>();

	public static Expression compile(String expression, String... variableNames) throws ExpressionException {
		return new Expression(expression, variableNames);
	}

	private Expression(String expression, String... variableNames) throws ExpressionException {
		this(Lexer.tokenize(expression), variableNames);
	}

	private Expression(List<Token> tokens, String... variableNames) throws ExpressionException {
		this.variableNames = variableNames;

		variables.put("e", new Constant(-1, Math.E));
		variables.put("pi", new Constant(-1, Math.PI));
		variables.put("true", new Constant(-1, 1));
		variables.put("false", new Constant(-1, 0));

		for (String variableName : variableNames) {
			if (variables.containsKey(variableName)) {
				throw new ExpressionException(-1, "Tried to overwrite identifier '" + variableName + "'");
			}
			variables.put(variableName, new Variable(0));
		}

		root = Parser.parse(tokens, this);
	}

	public double evaluate(double... values) throws EvaluationException {
		for (int i = 0; i < values.length; ++i) {
			final String variableName = variableNames[i];
			final RValue invokable = variables.get(variableName);
			if (!(invokable instanceof Variable)) {
				throw new EvaluationException(invokable.getPosition(), "Tried to assign constant " + variableName + ".");
			}

			((Variable) invokable).value = values[i];
		}

		pushInstance();
		try {
			return root.getValue();
		} catch (ReturnException e) {
			return e.getValue();
		} finally {
			popInstance();
		}
	}

	public void optimize() throws EvaluationException {
		root = root.optimize();
	}

	@Override
	public String toString() {
		return root.toString();
	}

	public RValue getVariable(String name, boolean create) {
		RValue variable = variables.get(name);
		if (variable == null && create) {
			variables.put(name, variable = new Variable(0));
		}

		return variable;
	}

	public static Expression getInstance() {
		return instance.get().peek();
	}

	private void pushInstance() {
		Stack<Expression> foo = instance.get();
		if (foo == null) {
			instance.set(foo = new Stack<Expression>());
		}

		foo.push(this);
	}

	private void popInstance() {
		Stack<Expression> foo = instance.get();

		foo.pop();

		if (foo.isEmpty()) {
			instance.set(null);
		}
	}

	public Map<Integer, double[]> getMegabuf() {
		return megabuf;
	}
}