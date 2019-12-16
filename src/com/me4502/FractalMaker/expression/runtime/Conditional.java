package com.me4502.FractalMaker.expression.runtime;

import javax.sound.midi.Sequence;

/**
 * An if/else statement or a ternary operator.
 *
 * @author TomyLobo
 */
public class Conditional extends Node {
	RValue condition;
	RValue truePart;
	RValue falsePart;

	public Conditional(int position, RValue condition, RValue truePart, RValue falsePart) {
		super(position);

		this.condition = condition;
		this.truePart = truePart;
		this.falsePart = falsePart;
	}

	@Override
	public double getValue() throws EvaluationException {
		if (condition.getValue() > 0.0) {
			return truePart.getValue();
		} else {
			return falsePart == null ? 0.0 : falsePart.getValue();
		}
	}

	@Override
	public char id() {
		return 'I';
	}

	@Override
	public String toString() {
		if (falsePart == null) {
			return "if (" + condition + ") { " + truePart + " }";
		} else if (truePart instanceof Sequence || falsePart instanceof Sequence) {
			return "if (" + condition + ") { " + truePart + " } else { " + falsePart + " }";
		} else {
			return "(" + condition + ") ? (" + truePart + ") : (" + falsePart + ")";
		}
	}

	@Override
	public RValue optimize() throws EvaluationException {
		final RValue newCondition = condition.optimize();

		if (newCondition instanceof Constant) {
			if (newCondition.getValue() > 0) {
				return truePart.optimize();
			} else {
				return falsePart == null ? new Constant(getPosition(), 0.0) : falsePart.optimize();
			}
		}

		return new Conditional(getPosition(), newCondition, truePart.optimize(), falsePart == null ? null : falsePart.optimize());
	}
}