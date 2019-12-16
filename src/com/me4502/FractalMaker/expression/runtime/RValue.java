package com.me4502.FractalMaker.expression.runtime;

import com.me4502.FractalMaker.expression.Identifiable;

/**
 * A value that can be used on the right side of an assignment.
 *
 * @author TomyLobo
 */
public interface RValue extends Identifiable {
	public double getValue() throws EvaluationException;

	public RValue optimize() throws EvaluationException;
}