package cas_sluzebka;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class Main {

	protected Shell shell;
	private DateTime workTimeStartDateTime;
	private DateTime workTimeEndDateTime;
	private DateTime travelThereDepartureTimeDateTime;
	private DateTime travelThereArrivalTimeDateTime;
	private DateTime travelBackDepartureTimeDateTime;
	private DateTime travelBackArrivalTimeDateTime;

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
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
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
		shell.setSize(450, 300);
		shell.setText("SWT Application");
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

		Button travelBackOtherDayCheckButton = new Button(travelBackGroup, SWT.CHECK);
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
}
