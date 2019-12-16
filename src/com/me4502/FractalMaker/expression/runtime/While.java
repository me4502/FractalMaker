package com.me4502.FractalMaker.expression.runtime;

/**
 * A while loop.
 *
 * @author TomyLobo
 */
public class While extends Node {
	RValue condition;
	RValue body;
	boolean footChecked;

	public While(int position, RValue condition, RValue body, boolean footChecked) {
		super(position);

		this.condition = condition;
		this.body = body;
		this.footChecked = footChecked;
	}

	@Override
	public double getValue() throws EvaluationException {
		int iterations = 0;
		double ret = 0.0;

		if (footChecked) {
			do {
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
			} while (condition.getValue() > 0.0);
		} else {
			while (condition.getValue() > 0.0) {
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
		}

		return ret;
	}

	@Override
	public char id() {
		return 'w';
	}

	@Override
	public String toString() {
		if (footChecked) {
			return "do { " + body + " } while (" + condition + ")";
		} else {
			return "while (" + condition + ") { " + body + " }";
		}
	}

	@Override
	public RValue optimize() throws EvaluationException {
		final RValue newCondition = condition.optimize();

		if (newCondition instanceof Constant && newCondition.getValue() <= 0) {
			// If the condition is always false, the loop can be flattened.
			if (footChecked) {
				// Foot-checked loops run at least once.
				return body.optimize();
			} else {
				// Loops that never run always return 0.0.
				return new Constant(getPosition(), 0.0);
			}
		}

		return new While(getPosition(), newCondition, body.optimize(), footChecked);
	}
}