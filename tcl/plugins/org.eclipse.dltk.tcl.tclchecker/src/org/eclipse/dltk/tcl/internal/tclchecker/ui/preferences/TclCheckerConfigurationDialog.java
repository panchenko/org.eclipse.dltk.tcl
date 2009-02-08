/**
 * 
 */
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TclCheckerConfigurationDialog extends StatusDialog {

	private final ConfigInstance instance;

	public TclCheckerConfigurationDialog(Shell parent, ConfigInstance instance) {
		super(parent);
		this.instance = instance;
	}

	private Text name;
	private Group workingMode;
	private Button summary;
	private Button useTclVer;
	private Text commandLineOptions;

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite content = (Composite) super.createDialogArea(parent);
		initNameGroup(content);
		initModeGroup(content, new GridData(GridData.FILL_HORIZONTAL));
		initCommandLineOptions(content);
		initOptions(content);

		// CTabFolder folder = new CTabFolder(content, SWT.NONE);
		// folder.setLayoutData(new GridData(GridData.FILL_BOTH));

		return content;
	}

	protected void initNameGroup(Composite parent) {
		Composite nameComposite = new Composite(parent, SWT.NONE);
		nameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameComposite.setLayout(new GridLayout(2, false));
		Label nameLabel = new Label(nameComposite, SWT.NONE);
		nameLabel.setText("Configuration Name");
		name = new Text(nameComposite, SWT.BORDER);
	}

	protected void initModeGroup(Composite parent, Object layoutData) {
		workingMode = new Group(parent, SWT.NONE);
		workingMode.setText(PreferencesMessages.TclChecker_mode);
		workingMode.setLayoutData(layoutData);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		workingMode.setLayout(layout);

		createModeOption(workingMode, PreferencesMessages.TclChecker_mode_none,
				CheckerMode.W0);
		createModeOption(workingMode,
				PreferencesMessages.TclChecker_mode_errors, CheckerMode.W1);
		createModeOption(workingMode,
				PreferencesMessages.TclChecker_mode_errorsAndUsageWarnings,
				CheckerMode.W2);
		createModeOption(
				workingMode,
				PreferencesMessages.TclChecker_mode_errorsAndWarningsExceptUpgrade,
				CheckerMode.W3);
		createModeOption(workingMode, PreferencesMessages.TclChecker_mode_all,
				CheckerMode.W4);
	}

	private static final String MODE_KEY = CheckerMode.class.getName();

	private void createModeOption(Group parent, String text, CheckerMode mode) {
		Button button = new Button(parent, SWT.RADIO);
		button.setText(text);
		button.setData(MODE_KEY, mode);
	}

	protected void initCommandLineOptions(Composite parent) {
		Composite nameComposite = new Composite(parent, SWT.NONE);
		nameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameComposite.setLayout(new GridLayout(2, false));
		Label nameLabel = new Label(nameComposite, SWT.NONE);
		nameLabel.setText("Configuration Name");
		commandLineOptions = new Text(nameComposite, SWT.BORDER);
	}

	protected void initOptions(Composite parent) {

	}

	@Override
	public void create() {
		super.create();
		if (instance != null) {
			getObjectData(instance);
		} else {
			initNew();
		}
	}

	private void setWorkingMode(CheckerMode mode) {
		for (Control control : workingMode.getChildren()) {
			if (control instanceof Button
					&& (control.getStyle() & SWT.RADIO) != 0) {
				if (control.getData(MODE_KEY) == mode) {
					((Button) control).setSelection(true);
					break;
				}
			}
		}
	}

	private CheckerMode getWorkingMode() {
		for (Control control : workingMode.getChildren()) {
			if (control instanceof Button
					&& (control.getStyle() & SWT.RADIO) != 0) {
				if (((Button) control).getSelection()) {
					CheckerMode mode = (CheckerMode) control.getData(MODE_KEY);
					if (mode != null) {
						return mode;
					}
				}
			}
		}
		return CheckerMode.W3;
	}

	protected void initNew() {
		setWorkingMode(CheckerMode.W3);
		name.setText(Util.EMPTY_STRING);
		commandLineOptions.setText(Util.EMPTY_STRING);
	}

	protected void getObjectData(ConfigInstance config) {
		name.setText(config.getName() != null ? config.getName()
				: Util.EMPTY_STRING);
		if (config.getMode() != null) {
			setWorkingMode(config.getMode());
		}
		commandLineOptions
				.setText(config.getCommandLineOptions() != null ? config
						.getCommandLineOptions() : Util.EMPTY_STRING);
	}

	public void setObjectData(ConfigInstance config) {
		config.setName(name.getText());
		config.setMode(getWorkingMode());
		config.setCommandLineOptions(commandLineOptions.getText());
		// TODO Auto-generated method stub

	}

}
