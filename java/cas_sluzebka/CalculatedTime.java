package cas_sluzebka;

public class CalculatedTime {

	public final int hoursBeforeWorkingTime;
	public final int minutesBeforeWorkingTime;
	public final int hoursAfterWorkingTime;
	public final int minutesAfterWorkingTime;

	public CalculatedTime(int hoursBeforeWorkingTime, int minutesBeforeWorkingTime, int hoursAfterWorkingTime,
			int minutesAfterWorkingTime) {

		this.hoursBeforeWorkingTime = hoursBeforeWorkingTime;
		this.minutesBeforeWorkingTime = minutesBeforeWorkingTime;
		this.hoursAfterWorkingTime = hoursAfterWorkingTime;
		this.minutesAfterWorkingTime = minutesAfterWorkingTime;
	}
}
