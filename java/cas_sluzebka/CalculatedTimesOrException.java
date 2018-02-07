package cas_sluzebka;

/**
 * Holds either calculated times or exception which occurred during
 * the calculation.
 *
 * Either value can be null but not both. Use {@link #hasValue()} to check
 * whether calculated times value is present. Otherwise holds exception.
 */
public class CalculatedTimesOrException {

	private final Exception exception;
	private final CalculatedTimes calculatedTimes;

	public CalculatedTimesOrException(CalculatedTimes calculatedTimes) {
		if (calculatedTimes == null) {
			throw new IllegalArgumentException("calculated times can not be null");
		}

		this.calculatedTimes = calculatedTimes;
		this.exception = null;
	}

	public CalculatedTimesOrException(Exception exception) {
		if (exception == null) {
			throw new IllegalArgumentException("exception can not be null.");
		}

		this.calculatedTimes = null;
		this.exception = exception;
	}

	public boolean hasValue() {
		return calculatedTimes != null;
	}

	public CalculatedTimes getCalculatedTimes() {
		return calculatedTimes;
	}

	public Exception getException() {
		return exception;
	}

}
