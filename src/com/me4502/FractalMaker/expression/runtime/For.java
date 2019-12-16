package com.me4502.FractalMaker.expression.runtime;


/**
 * A Java/C-style for loop.
 *
 * @author TomyLobo
 */
public class For extends Node {
	RValue init;
	RValue condition;
	RValue increment;
	RValue body;

	public For(int position, RValue init, RValue condition, RValue increment, RValue body) {
		super(position);

		this.init = init;
		this.condition = condition;
		this.increment = increment;
		this.body = body;
	}

	@Override
	public double getValue() throws EvaluationException {
		int iterations = 0;
		double ret = 0.0;

		for (init.getValue(); condition.getValue() > 0; increment.getValue()) {
			if (iterations > 256) {
				throw new EvaluationException(getPosition(), "Loop exceeded 256 iterations.");
			}
			++iterations;

			try {
				ret = body.getValue();
			} catch (BreakException e) {
				if (e.doContinue) {
					continue;
				} else {
					break;
				}
			}
		}

		return ret;
	}

	@Override
	public char id() {
		return 'F';
	}

	@Override
	public String toString() {
		return "for (" + init + "; " + condition + "; " + increment + ") { " + body + " }";
	}

	@Override
	public RValue optimize() throws EvaluationException {
		final RValue newCondition = condition.optimize();

		if (newCondition instanceof Constant && newCondition.getValue() <= 0) {
			// If the condition is always false, the loop can be flattened.
			// So we run the init part and then return 0.0.
			return new Sequence(getPosition(), init, new Constant(getPosition(), 0.0)).optimize();
		}

		//return new Sequence(getPosition(), init.optimize(), new While(getPosition(), condition, new Sequence(getPosition(), body, increment), false)).optimize();
		return new For(getPosition(), init.optimize(), newCondition, increment.optimize(), body.optimize());
	}
}