package cas_sluzebka;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

/**
 * Wrapper around clojure logic.
 */
public class Logic {

	private static final String CLOJURE_NAMESPACE = "cas-sluzebka.core";
	private static final String CLOJURE_FUNCTION = "calc-times";

	AtomicBoolean initialized = new AtomicBoolean(false);
	IFn calcTimesFn;

	/**
	 * Initializes the logic in a background thread.
	 */
	public synchronized void initialize() {
		if (! initialized.get() || calcTimesFn == null) {
			final IFn require = Clojure.var("clojure.core", "require");
			require.invoke(Clojure.read(CLOJURE_NAMESPACE));
			calcTimesFn = Clojure.var(CLOJURE_NAMESPACE, CLOJURE_FUNCTION);
		}
		initialized.set(true);
	}

	/**
	 * Calculates the times, synchronously. Logic is initialized if not
	 * already.
	 */
	public CalculatedTimes calculateTimes(AllTimesToCalc timesToCalc) {
		if (! initialized.get() || calcTimesFn == null) {
			initialize();
		}

		CalculatedTimes calcTimes = (CalculatedTimes)calcTimesFn.invoke(timesToCalc);
		return calcTimes;
	}

	/**
	 * Asynchronously calculates the value.
	 */
	public void calculateTimesAsync(final AllTimesToCalc timesToCalc,
			final Consumer<CalculatedTimesOrException> callback) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					CalculatedTimes calculatedTimes = calculateTimes(timesToCalc);
					callback.accept(new CalculatedTimesOrException(calculatedTimes));
				} catch (Exception e) {
					callback.accept(new CalculatedTimesOrException(e));
				}
			}
		};

		new Thread(runnable).start();
	}

}
