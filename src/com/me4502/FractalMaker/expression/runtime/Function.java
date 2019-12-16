package com.me4502.FractalMaker.expression.runtime;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper for a Java method and its arguments (other Nodes)
 *
 * @author TomyLobo
 */
public class Function extends Node {
	/**
	 * Add this annotation on functions that don't always return the same value
	 * for the same inputs and on functions with side-effects.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Dynamic { }

	final Method method;
	final RValue[] args;

	Function(int position, Method method, RValue... args) {
		super(position);
		this.method = method;
		this.args = args;
	}

	@Override
	public final double getValue() throws EvaluationException {
		return invokeMethod(method, args);
	}

	protected static final double invokeMethod(Method method, Object[] args) throws EvaluationException {
		try {
			return (Double) method.invoke(null, args);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof EvaluationException) {
				throw (EvaluationException) e.getTargetException();
			}
			throw new EvaluationException(-1, "Exception caught while evaluating expression", e.getTargetException());
		} catch (IllegalAccessException e) {
			throw new EvaluationException(-1, "Internal error while evaluating expression", e);
		}
	}

	@Override
	public String toString() {
		final StringBuilder ret = new StringBuilder(method.getName()).append('(');
		boolean first = true;
		for (Object obj : args) {
			if (!first) {
				ret.append(", ");
			}
			first = false;
			ret.append(obj);
		}
		return ret.append(')').toString();
	}

	@Override
	public char id() {
		return 'f';
	}

	@Override
	public RValue optimize() throws EvaluationException {
		final RValue[] optimizedArgs = new RValue[args.length];
		boolean optimizable = !method.isAnnotationPresent(Dynamic.class);
		int position = getPosition();
		for (int i = 0; i < args.length; ++i) {
			final RValue optimized = optimizedArgs[i] = args[i].optimize();

			if (!(optimized instanceof Constant)) {
				optimizable = false;
			}

			if (optimized.getPosition() < position) {
				position = optimized.getPosition();
			}
		}

		if (optimizable) {
			return new Constant(position, invokeMethod(method, optimizedArgs));
		} else if (this instanceof LValueFunction) {
			return new LValueFunction(position, method, ((LValueFunction) this).setter, optimizedArgs);
		} else {
			return new Function(position, method, optimizedArgs);
		}
	}
}