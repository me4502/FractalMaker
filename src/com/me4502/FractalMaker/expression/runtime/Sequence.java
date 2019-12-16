package com.me4502.FractalMaker.expression.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * A sequence of operations, usually separated by semicolons in the input stream.
 *
 * @author TomyLobo
 */
public class Sequence extends Node {
	final RValue[] sequence;

	public Sequence(int position, RValue... sequence) {
		super(position);

		this.sequence = sequence;
	}

	@Override
	public char id() {
		return 's';
	}

	@Override
	public double getValue() throws EvaluationException {
		double ret = 0;
		for (RValue invokable : sequence) {
			ret = invokable.getValue();
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("seq(");
		boolean first = true;
		for (RValue invokable : sequence) {
			if (!first) {
				sb.append(", ");
			}
			sb.append(invokable);
			first = false;
		}

		return sb.append(')').toString();
	}

	@Override
	public RValue optimize() throws EvaluationException {
		final List<RValue> newSequence = new ArrayList<RValue>();

		RValue droppedLast = null;
		for (RValue invokable : sequence) {
			droppedLast = null;
			invokable = invokable.optimize();
			if (invokable instanceof Sequence) {
				for (RValue subInvokable : ((Sequence) invokable).sequence) {
					newSequence.add(subInvokable);
				}
			} else if (invokable instanceof Constant) {
				droppedLast = invokable;
			} else {
				newSequence.add(invokable);
			}
		}

		if (droppedLast != null) {
			newSequence.add(droppedLast);
		}

		if (newSequence.size() == 1) {
			return newSequence.get(0);
		}

		return new Sequence(getPosition(), newSequence.toArray(new RValue[newSequence.size()]));
	}
}