package com.me4502.FractalMaker.expression.runtime;

import java.lang.reflect.Method;

/**
 * Wrapper for a pair of Java methods and their arguments (other Nodes), forming an LValue
 *
 * @author TomyLobo
 */
public class LValueFunction extends Function implements LValue {
	private final Object[] setterArgs;
	final Method setter;

	LValueFunction(int position, Method getter, Method setter, RValue... args) {
		super(position, getter, args);

		setterArgs = new Object[args.length + 1];
		System.arraycopy(args, 0, setterArgs, 0, args.length);
		this.setter = setter;
	}

	@Override
	public char id() {
		return 'l';
	}

	@Override
	public double assign(double value) throws EvaluationException {
		setterArgs[setterArgs.length - 1] = value;
		return invokeMethod(setter, setterArgs);
	}
}