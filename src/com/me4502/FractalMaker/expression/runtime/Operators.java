package com.me4502.FractalMaker.expression.runtime;

/**
 * Contains all unary and binary operators.
 *
 * @author TomyLobo
 */
public final class Operators {
	public static final Function getOperator(int position, String name, RValue lhs, RValue rhs) throws NoSuchMethodException {
		if (lhs instanceof LValue) {
			try {
				return new Function(position, Operators.class.getMethod(name, LValue.class, RValue.class), lhs, rhs);
			} catch (NoSuchMethodException e) {
			}
		}
		return new Function(position, Operators.class.getMethod(name, RValue.class, RValue.class), lhs, rhs);
	}

	public static final Function getOperator(int position, String name, RValue argument) throws NoSuchMethodException {
		if (argument instanceof LValue) {
			try {
				return new Function(position, Operators.class.getMethod(name, LValue.class), argument);
			} catch (NoSuchMethodException e) {
			}
		}
		return new Function(position, Operators.class.getMethod(name, RValue.class), argument);
	}


	public static final double add(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() + rhs.getValue();
	}

	public static final double sub(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() - rhs.getValue();
	}

	public static final double mul(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() * rhs.getValue();
	}

	public static final double div(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() / rhs.getValue();
	}

	public static final double mod(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() % rhs.getValue();
	}

	public static final double pow(RValue lhs, RValue rhs) throws EvaluationException {
		return Math.pow(lhs.getValue(), rhs.getValue());
	}


	public static final double neg(RValue x) throws EvaluationException {
		return -x.getValue();
	}

	public static final double not(RValue x) throws EvaluationException {
		return x.getValue() > 0.0 ? 0.0 : 1.0;
	}

	public static final double inv(RValue x) throws EvaluationException {
		return ~(long) x.getValue();
	}


	public static final double lth(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() < rhs.getValue() ? 1.0 : 0.0;
	}

	public static final double gth(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() > rhs.getValue() ? 1.0 : 0.0;
	}

	public static final double leq(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() <= rhs.getValue() ? 1.0 : 0.0;
	}

	public static final double geq(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() >= rhs.getValue() ? 1.0 : 0.0;
	}


	public static final double equ(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() == rhs.getValue() ? 1.0 : 0.0;
	}

	public static final double neq(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() != rhs.getValue() ? 1.0 : 0.0;
	}

	public static final double near(RValue lhs, RValue rhs) throws EvaluationException {
		return almostEqual2sComplement(lhs.getValue(), rhs.getValue(), 450359963L) ? 1.0 : 0.0;
		//return Math.abs(lhs.invoke() - rhs.invoke()) < 1e-7 ? 1.0 : 0.0;
	}


	public static final double or(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() > 0.0 || rhs.getValue() > 0.0 ? 1.0 : 0.0;
	}

	public static final double and(RValue lhs, RValue rhs) throws EvaluationException {
		return lhs.getValue() > 0.0 && rhs.getValue() > 0.0 ? 1.0 : 0.0;
	}


	public static final double shl(RValue lhs, RValue rhs) throws EvaluationException {
		return (long) lhs.getValue() << (long) rhs.getValue();
	}

	public static final double shr(RValue lhs, RValue rhs) throws EvaluationException {
		return (long) lhs.getValue() >> (long) rhs.getValue();
	}


	public static final double ass(LValue lhs, RValue rhs) throws EvaluationException {
		return lhs.assign(rhs.getValue());
	}

	public static final double aadd(LValue lhs, RValue rhs) throws EvaluationException {
		return lhs.assign(lhs.getValue() + rhs.getValue());
	}

	public static final double asub(LValue lhs, RValue rhs) throws EvaluationException {
		return lhs.assign(lhs.getValue() - rhs.getValue());
	}

	public static final double amul(LValue lhs, RValue rhs) throws EvaluationException {
		return lhs.assign(lhs.getValue() * rhs.getValue());
	}

	public static final double adiv(LValue lhs, RValue rhs) throws EvaluationException {
		return lhs.assign(lhs.getValue() / rhs.getValue());
	}

	public static final double amod(LValue lhs, RValue rhs) throws EvaluationException {
		return lhs.assign(lhs.getValue() % rhs.getValue());
	}

	public static final double aexp(LValue lhs, RValue rhs) throws EvaluationException {
		return lhs.assign(Math.pow(lhs.getValue(), rhs.getValue()));
	}


	public static final double inc(LValue x) throws EvaluationException {
		return x.assign(x.getValue() + 1);
	}

	public static final double dec(LValue x) throws EvaluationException {
		return x.assign(x.getValue() - 1);
	}

	public static final double postinc(LValue x) throws EvaluationException {
		final double oldValue = x.getValue();
		x.assign(oldValue + 1);
		return oldValue;
	}

	public static final double postdec(LValue x) throws EvaluationException {
		final double oldValue = x.getValue();
		x.assign(oldValue - 1);
		return oldValue;
	}


	private static final double[] factorials = new double[171];
	static {
		double accum = 1;
		factorials[0] = 1;
		for (int i = 1; i < factorials.length; ++i) {
			factorials[i] = accum *= i;
		}
	}

	public static final double fac(RValue x) throws EvaluationException {
		int n = (int) x.getValue();

		if (n < 0) {
			return 0;
		}

		if (n >= factorials.length) {
			return Double.POSITIVE_INFINITY;
		}

		return factorials[n];
	}

	// Usable AlmostEqual function, based on http://www.cygnus-software.com/papers/comparingfloats/comparingfloats.htm
	private static boolean almostEqual2sComplement(double A, double B, long maxUlps) {
		// Make sure maxUlps is non-negative and small enough that the
		// default NAN won't compare as equal to anything.
		//assert(maxUlps > 0 && maxUlps < 4 * 1024 * 1024); // this is for floats, not doubles

		long aLong = Double.doubleToRawLongBits(A);
		// Make aLong lexicographically ordered as a twos-complement long
		if (aLong < 0) aLong = 0x8000000000000000L - aLong;

		long bLong = Double.doubleToRawLongBits(B);
		// Make bLong lexicographically ordered as a twos-complement long
		if (bLong < 0) bLong = 0x8000000000000000L - bLong;

		long longDiff = Math.abs(aLong - bLong);
		return longDiff <= maxUlps;
	}
}