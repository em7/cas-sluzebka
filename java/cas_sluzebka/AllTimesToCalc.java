package cas_sluzebka;

public class AllTimesToCalc {

	public final TimeToCalc trip;
	public final TimeToCalc tripBack;
	public final boolean otherDay;

	public AllTimesToCalc(TimeToCalc trip, TimeToCalc tripBack, boolean otherDay) {
		this.trip = validTimeToCalc(trip);
		this.tripBack = validTimeToCalc(tripBack);
		this.otherDay = otherDay;
	}

	public TimeToCalc validTimeToCalc(TimeToCalc timeToCalc) {
		if (timeToCalc == null)
			throw new IllegalArgumentException("Time to calc must not be null.");
		return timeToCalc;
	}
}
