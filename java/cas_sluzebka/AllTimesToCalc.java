package cas_sluzebka;

public class AllTimesToCalc {

	public final TimeToCalc trip;
	public final TimeToCalc tripBack;
	public final boolean otherDay;
	public final int breakTimeHours;
	public final int breakTimeMins;

	public AllTimesToCalc(TimeToCalc trip, TimeToCalc tripBack, boolean otherDay, int breakTimeHours,
			int breakTimeMins) {
		this.trip = validTimeToCalc(trip);
		this.tripBack = validTimeToCalc(tripBack);
		this.otherDay = otherDay;
		this.breakTimeHours = validBreakHours(breakTimeHours);
		this.breakTimeMins = validBreakMinutes(breakTimeMins);
	}

	private static TimeToCalc validTimeToCalc(TimeToCalc timeToCalc) {
		if (timeToCalc == null)
			throw new IllegalArgumentException("Time to calc must not be null.");
		return timeToCalc;
	}

	private static int validBreakHours(int breakHours) {
		if (breakHours < 0 || breakHours > 23) {
			throw new IllegalArgumentException("Break time hours must be between 0 and 23.");
		}
		return breakHours;
	}

	private static int validBreakMinutes(int breakMinutes) {
		if (breakMinutes < 0 || breakMinutes > 59) {
			throw new IllegalArgumentException("Break time minutes must be between 0 and 59.");
		}
		return breakMinutes;
	}
}
