package cas_sluzebka;

/**
 * Holds times needed for performing the calculation
 */
public class TimeToCalc {

	public final int departureHour;
	public final int departureMinute;
	public final int arrivalHour;
	public final int arrivalMinute;
	public final int thresholdHour;
	public final int thresholdMinute;

	public TimeToCalc(int departureHour, int departureMinute, int arrivalHour, int arrivalMinute, int thresholdHour,
			int thresholdMinute) {

		this.departureHour = validHour(departureHour);
		this.departureMinute = validMinute(departureMinute);
		this.arrivalHour = validHour(arrivalHour);
		this.arrivalMinute = validMinute(arrivalMinute);
		this.thresholdHour = validHour(thresholdHour);
		this.thresholdMinute = validMinute(thresholdMinute);
	}

	public static int validHour(int hour) {
		if (hour < 0 || hour > 23)
			throw new IllegalArgumentException("Hour must be between 0 and 23.");
		return hour;
	}

	public static int validMinute(int minute) {
		if (minute < 0 || minute > 59)
			throw new IllegalArgumentException("Minute must be between 0 and 59.");
		return minute;
	}
}
