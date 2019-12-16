package com.me4502.FractalMaker.expression.runtime;

/**
 * A value that can be used on the left side of an assignment.
 *
 * @author TomyLobo
 */
public interface LValue extends RValue {
	public double assign(double value) throws EvaluationException;
}