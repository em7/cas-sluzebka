package cas_sluzebka;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Main {

	protected Shell shell;
	private DateTime workTimeStartDateTime;
	private DateTime workTimeEndDateTime;
	private DateTime travelThereDepartureTimeDateTime;
	private DateTime travelThereArrivalTimeDateTime;
	private DateTime travelBackDepartureTimeDateTime;
	private DateTime travelBackArrivalTimeDateTime;
	private Button travelBackOtherDayCheckButton;

	private final Logic logic = new Logic();
	private Display display;
	private Spinner workTimeBreakMinSpinner;
	private Table resultTable;

	private boolean trace;
	private Label calculateResultInitializingLabel;
	private Button calculateButton;

	public Main() {
		if (System.getProperty("trace") != null) {
			trace = true;
		}
	}

	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window. This method is blocking and implements the event loop.
	 */
	public void open() {
		display = Display.getDefault();
		initializeLogic();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMinimumSize(new Point(350, 600));
		shell.setSize(350, 600);
		shell.setText("Čas na cestě v/mimo prac. dobu");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

		TabItem calculationTab = new TabItem(tabFolder, SWT.NONE);
		calculationTab.setText("Počítání času");

		Composite calculationComposite = new Composite(tabFolder, SWT.NONE);
		calculationTab.setControl(calculationComposite);
		calculationComposite.setLayout(new GridLayout(1, false));

		Group travelThereGroup = new Group(calculationComposite, SWT.NONE);
		travelThereGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		travelThereGroup.setText("Cesta tam");
		travelThereGroup.setLayout(new GridLayout(2, false));

		Label travelThereDepartureTimeLabel = new Label(travelThereGroup, SWT.NONE);
		travelThereDepartureTimeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		travelThereDepartureTimeLabel.setText("Odjezd");

		travelThereDepartureTimeDateTime = new DateTime(travelThereGroup, SWT.BORDER | SWT.TIME | SWT.SHORT);

		Label travelThereArrivalTimeLabel = new Label(travelThereGroup, SWT.NONE);
		travelThereArrivalTimeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		travelThereArrivalTimeLabel.setText("Příjezd");

		travelThereArrivalTimeDateTime = new DateTime(travelThereGroup, SWT.BORDER | SWT.TIME | SWT.SHORT);

		Group travelBackGroup = new Group(calculationComposite, SWT.NONE);
		travelBackGroup.setLayout(new GridLayout(2, false));
		travelBackGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		travelBackGroup.setText("Cesta zpět");

		travelBackOtherDayCheckButton = new Button(travelBackGroup, SWT.CHECK);
		travelBackOtherDayCheckButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		travelBackOtherDayCheckButton.setText("Jiný den");

		Label travelBackDepartureTimeLabel = new Label(travelBackGroup, SWT.NONE);
		travelBackDepartureTimeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		travelBackDepartureTimeLabel.setText("Odjezd");

		travelBackDepartureTimeDateTime = new DateTime(travelBackGroup, SWT.BORDER | SWT.TIME | SWT.SHORT);

		Label travelBackArrivalTimeLabel = new Label(travelBackGroup, SWT.NONE);
		travelBackArrivalTimeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		travelBackArrivalTimeLabel.setText("Příjezd");

		travelBackArrivalTimeDateTime = new DateTime(travelBackGroup, SWT.BORDER | SWT.TIME | SWT.SHORT);

		Group resultGroup = new Group(calculationComposite, SWT.NONE);
		resultGroup.setLayout(new GridLayout(1, false));
		resultGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		resultGroup.setText("Výsledek");

		Composite calculateResultComposite = new Composite(resultGroup, SWT.NONE);
		GridLayout gl_calculateResultComposite = new GridLayout(2, false);
		gl_calculateResultComposite.marginHeight = 0;
		gl_calculateResultComposite.marginWidth = 0;
		calculateResultComposite.setLayout(gl_calculateResultComposite);
		calculateResultComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		calculateButton = new Button(calculateResultComposite, SWT.NONE);
		calculateButton.setEnabled(false);
		calculateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onCalculateClick();
			}
		});
		calculateButton.setText("Spočítat");

		calculateResultInitializingLabel = new Label(calculateResultComposite, SWT.NONE);
		calculateResultInitializingLabel.setText("Probíhá inicializace...");

		resultTable = new Table(resultGroup, SWT.BORDER | SWT.FULL_SELECTION);
		resultTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		resultTable.setHeaderVisible(true);
		resultTable.setLinesVisible(true);

		TableColumn resultTableTimeLabelColumn = new TableColumn(resultTable, SWT.RIGHT);
		resultTableTimeLabelColumn.setText("Časový údaj");
		resultTableTimeLabelColumn.setWidth(100);

		TableColumn resultTableTimeColumn = new TableColumn(resultTable, SWT.LEFT);
		resultTableTimeColumn.setWidth(100);
		resultTableTimeColumn.setText("Čas");

		Label versionLabel = new Label(resultGroup, SWT.NONE);
		versionLabel.setText("Verze 1.0.0 (180210-01)");

		TabItem settingsTab = new TabItem(tabFolder, SWT.NONE);
		settingsTab.setText("Nastavení");

		Composite settingsComposite = new Composite(tabFolder, SWT.NONE);
		settingsTab.setControl(settingsComposite);
		settingsComposite.setLayout(new GridLayout(2, false));

		Label workTimeStartLabel = new Label(settingsComposite, SWT.NONE);
		workTimeStartLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		workTimeStartLabel.setAlignment(SWT.RIGHT);
		workTimeStartLabel.setText("Počátek pracovní doby");

		workTimeStartDateTime = new DateTime(settingsComposite, SWT.BORDER | SWT.TIME | SWT.SHORT);

		Label workTimeEndLabel = new Label(settingsComposite, SWT.NONE);
		workTimeEndLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		workTimeEndLabel.setAlignment(SWT.RIGHT);
		workTimeEndLabel.setText("Konec pracovní doby");

		workTimeEndDateTime = new DateTime(settingsComposite, SWT.BORDER | SWT.TIME | SWT.SHORT);

		Label workTimeBreakLabel = new Label(settingsComposite, SWT.NONE);
		workTimeBreakLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		workTimeBreakLabel.setText("Přestávka v práci");

		Composite workTimeBreakPanel = new Composite(settingsComposite, SWT.NONE);
		GridLayout gl_workTimeBreakPanel = new GridLayout(2, false);
		gl_workTimeBreakPanel.marginWidth = 0;
		workTimeBreakPanel.setLayout(gl_workTimeBreakPanel);

		workTimeBreakMinSpinner = new Spinner(workTimeBreakPanel, SWT.BORDER);
		workTimeBreakMinSpinner.setMaximum(1440);
		workTimeBreakMinSpinner.setSelection(30);

		Label workTimeBreakUnitLabel = new Label(workTimeBreakPanel, SWT.NONE);
		workTimeBreakUnitLabel.setText("minut");
		new Label(settingsComposite, SWT.NONE);

		Label workTimeBreakExplain = new Label(settingsComposite, SWT.NONE);
		workTimeBreakExplain.setText("Délka např. přestávky na oběd.");

		loadDefaults();
	}

	private void loadDefaults() {
		workTimeStartDateTime.setHours(8);
		workTimeStartDateTime.setMinutes(0);

		workTimeEndDateTime.setHours(16);
		workTimeEndDateTime.setMinutes(30);

		travelThereDepartureTimeDateTime.setHours(7);
		travelThereDepartureTimeDateTime.setMinutes(0);

		travelThereArrivalTimeDateTime.setHours(9);
		travelThereArrivalTimeDateTime.setMinutes(0);

		travelBackDepartureTimeDateTime.setHours(16);
		travelBackDepartureTimeDateTime.setMinutes(0);

		travelBackArrivalTimeDateTime.setHours(18);
		travelBackArrivalTimeDateTime.setMinutes(0);
	}

	/**
	 * Asynchronously initializes logic. This can take some time.
	 */
	private void initializeLogic() {

		if (trace) {
			System.out.println(String.format("[TRACE] %d: initializing logic", System.nanoTime()));
		}

		Runnable initLogicRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					logic.initialize();
					
					if (trace) {
						System.out.println(String.format("[TRACE] %d: logic initialized", System.nanoTime()));	
					}
					
					if (display != null) {
						display.asyncExec(() -> {
							calculateButton.setEnabled(true);
							calculateResultInitializingLabel.setVisible(false);
						});
					}
				} catch (Exception e) {
					displayExceptionInfo("Error during initializing logic.", e);
				}
			}
		};

		new Thread(initLogicRunnable).start();
	}

	private void onCalculateClick() {
		TimeToCalc trip = new TimeToCalc(travelThereDepartureTimeDateTime.getHours(), //
				travelThereDepartureTimeDateTime.getMinutes(), //
				travelThereArrivalTimeDateTime.getHours(), //
				travelThereArrivalTimeDateTime.getMinutes(), //
				workTimeStartDateTime.getHours(), //
				workTimeStartDateTime.getMinutes());

		TimeToCalc tripBack = new TimeToCalc(travelBackDepartureTimeDateTime.getHours(), //
				travelBackDepartureTimeDateTime.getMinutes(), //
				travelBackArrivalTimeDateTime.getHours(), //
				travelBackArrivalTimeDateTime.getMinutes(), //
				workTimeEndDateTime.getHours(), //
				workTimeEndDateTime.getMinutes());

		boolean otherDay = travelBackOtherDayCheckButton.getSelection();

		int workTimeBreakHours = workTimeBreakMinSpinner.getSelection() / 60;
		int workTimebreakMins = workTimeBreakMinSpinner.getSelection() % 60;

		AllTimesToCalc allTimes = new AllTimesToCalc(trip, tripBack, otherDay, workTimeBreakHours, workTimebreakMins);
		logic.calculateTimesAsync(allTimes, (calcOrEx) -> {
			display.asyncExec(() -> displayTimesOrException(calcOrEx));
		});

	}

	/**
	 * Displays calculated times.
	 */
	private void displayTimesOrException(CalculatedTimesOrException calcOrEx) {
		if (calcOrEx.hasValue()) {
			displayCalculatedTimes(calcOrEx.getCalculatedTimes());
		} else {
			Exception e = calcOrEx.getException();
			MessageBox msgBox = new MessageBox(shell, SWT.ICON_WARNING);
			msgBox.setText("Exception occurred.");
			msgBox.setMessage("Could not calculate times." + "\n\nException caugh: " + e.getMessage());
			msgBox.open();
		}
	}

	private void displayCalculatedTimes(CalculatedTimes calculatedTimes) {
		resultTable.removeAll();

		if (calculatedTimes.day2 == null) {
			TableItem outOfWorkHours = new TableItem(resultTable, SWT.DEFAULT);
			outOfWorkHours.setText(0, "Mimo pracovní dobu");
			outOfWorkHours.setText(1, String.format("%d:%02d",
					calculatedTimes.day1.hoursBeforeWorkingTime,
					calculatedTimes.day1.minutesBeforeWorkingTime));

			TableItem inWorkHours = new TableItem(resultTable, SWT.DEFAULT);
			inWorkHours.setText(0, "V pracovní době");
			inWorkHours.setText(1, String.format("%d:%02d",
					calculatedTimes.day1.hoursAfterWorkingTime,
					calculatedTimes.day1.minutesAfterWorkingTime));

			TableItem projectHours = new TableItem(resultTable, SWT.DEFAULT);
			projectHours.setText(0, "Čas na projektu");
			projectHours.setText(1, String.format("%d:%02d",
					calculatedTimes.day1.projectWorkingTimeHours,
					calculatedTimes.day1.projectWorkingTimeMinutes));
		} else {
			TableItem outOfWorkHours1 = new TableItem(resultTable, SWT.DEFAULT);
			outOfWorkHours1.setText(0, "1. den mimo pracovní dobu");
			outOfWorkHours1.setText(1, String.format("%d:%02d",
					calculatedTimes.day1.hoursBeforeWorkingTime,
					calculatedTimes.day1.minutesBeforeWorkingTime));

			TableItem inWorkHours1 = new TableItem(resultTable, SWT.DEFAULT);
			inWorkHours1.setText(0, "1. den v pracovní době");
			inWorkHours1.setText(1, String.format("%d:%02d",
					calculatedTimes.day1.hoursAfterWorkingTime,
					calculatedTimes.day1.minutesAfterWorkingTime));

			TableItem projectHours1 = new TableItem(resultTable, SWT.DEFAULT);
			projectHours1.setText(0, "1. den čas na projektu");
			projectHours1.setText(1, String.format("%d:%02d",
					calculatedTimes.day1.projectWorkingTimeHours,
					calculatedTimes.day1.projectWorkingTimeMinutes));

			TableItem outOfWorkHours2 = new TableItem(resultTable, SWT.DEFAULT);
			outOfWorkHours2.setText(0, "2. den mimo pracovní dobu");
			outOfWorkHours2.setText(1, String.format("%d:%02d",
					calculatedTimes.day2.hoursAfterWorkingTime,
					calculatedTimes.day2.minutesAfterWorkingTime));

			TableItem inWorkHours2 = new TableItem(resultTable, SWT.DEFAULT);
			inWorkHours2.setText(0, "2. den v pracovní době");
			inWorkHours2.setText(1, String.format("%d:%02d",
					calculatedTimes.day2.hoursBeforeWorkingTime,
					calculatedTimes.day2.minutesBeforeWorkingTime));

			TableItem projectHours2 = new TableItem(resultTable, SWT.DEFAULT);
			projectHours2.setText(0, "2 den čas na projektu");
			projectHours2.setText(1, String.format("%d:%02d",
					calculatedTimes.day2.projectWorkingTimeHours,
					calculatedTimes.day2.projectWorkingTimeMinutes));
		}

		for (TableColumn column : resultTable.getColumns()) {
			column.pack();
		}
	}

	/**
	 * Opens a message box with exception message and header text. Can be called
	 * from background threads.
	 */
	private void displayExceptionInfo(String text, Exception e) {
		if (display != null) {
			display.asyncExec(() -> {
				MessageBox msgBox = new MessageBox(shell, SWT.ICON_WARNING);
				msgBox.setText("Exception occurred.");
				msgBox.setMessage(text + "\n\nException caugh: " + e.getMessage());
				msgBox.open();
			});
		}

		e.printStackTrace();
	}
}
