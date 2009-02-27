/**
 * 
 */
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.internal.databinging.RadioButtonListValue;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerProblemDescription;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState;
import org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.dltk.validators.configs.ValidatorsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class TclCheckerConfigurationDialog extends StatusDialog {

	private class MessageLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof MessageGroup) {
				if (columnIndex == 0) {
					return ((MessageGroup) element).getName();
				}
			} else if (element instanceof CheckerMessage) {
				final CheckerMessage message = (CheckerMessage) element;
				switch (columnIndex) {
				case 0: {
					final String id = message.getMessageId();
					final int index = id
							.indexOf(TclCheckerProblemDescription.MESSAGE_ID_SEPARATOR);
					if (index >= 0) {
						return id
								.substring(index
										+ TclCheckerProblemDescription.MESSAGE_ID_SEPARATOR
												.length());
					} else {
						return id;
					}
				}
				case 1: {
					final MessageCategory category = message.getCategory();
					if (category.isError()) {
						return Messages.TclChecker_error;
					} else if (category.isWarning()) {
						return Messages.TclChecker_warning;
					}
				}
				case 2:
					return stateToString.get(instance.getMessageStates().get(
							message.getMessageId()));
				}
			} else {
				return element.toString();
			}
			return Util.EMPTY_STRING;
		}
	}

	private static final Map<MessageState, String> stateToString = new HashMap<MessageState, String>();

	static {
		stateToString.put(MessageState.DEFAULT,
				Messages.TclChecker_processType_default);
		stateToString.put(null, Messages.TclChecker_processType_default);
		stateToString.put(MessageState.CHECK,
				Messages.TclChecker_processType_check);
		stateToString.put(MessageState.SUPPRESS,
				Messages.TclChecker_processType_suppress);
	}

	private static class MessageContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof MessageGroup) {
				return ((MessageGroup) parentElement).getMessages().toArray();
			} else {
				return new Object[0];
			}
		}

		public Object getParent(Object element) {
			if (element instanceof CheckerMessage) {
				return ((CheckerMessage) element).getGroup();
			} else {
				return null;
			}
		}

		public boolean hasChildren(Object element) {
			return element instanceof MessageGroup;
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			} else {
				return new Object[0];
			}
		}

		public void dispose() {
			// empty
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// empty
		}

	}

	private static class MessageViewerComparator extends ViewerComparator {

		@Override
		public int category(Object element) {
			if (element instanceof MessageGroup) {
				return -(((MessageGroup) element).getPriority() + 2);
			} else if (element instanceof CheckerMessage) {
				return 1;
			} else {
				return super.category(element);
			}
		}

	}

	private class TclCheckerMessageActionEditingSupport extends EditingSupport {

		/**
		 * @param viewer
		 */
		public TclCheckerMessageActionEditingSupport(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected boolean canEdit(Object element) {
			return element instanceof CheckerMessage;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			final String[] items = new String[MessageState.VALUES.size()];
			for (int i = 0; i < items.length; ++i) {
				items[i] = stateToString.get(MessageState.VALUES.get(i));
			}
			return new ComboBoxCellEditor((Composite) getViewer().getControl(),
					items, SWT.READ_ONLY);
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof CheckerMessage) {
				final MessageState state = instance.getMessageStates().get(
						((CheckerMessage) element).getMessageId());
				return MessageState.VALUES.indexOf(state != null ? state
						: MessageState.DEFAULT);
			}
			return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof CheckerMessage && value instanceof Integer) {
				final MessageState state = MessageState.VALUES
						.get((Integer) value);
				final String messageId = ((CheckerMessage) element)
						.getMessageId();
				if (state == MessageState.DEFAULT) {
					instance.getMessageStates().removeKey(messageId);
				} else {
					instance.getMessageStates().put(messageId, state);
				}
				getViewer().refresh(element);
			}
		}
	}

	private final CheckerConfig instance;
	private final DataBindingContext bindingContext;

	public TclCheckerConfigurationDialog(Shell parent, CheckerConfig instance) {
		super(parent);
		Assert.isNotNull(instance);
		this.instance = instance;
		setShellStyle(getShellStyle() | SWT.RESIZE);
		bindingContext = new DataBindingContext();
		instance.eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				updateStatus();
			}
		});
		updateStatus();
	}

	private Text name;
	private Group workingMode;
	private Text commandLineOptions;
	private Button summary;
	private Button useTclVer;
	private TreeViewer messageViewer;

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		Composite content = new Composite(dialogArea, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout contentLayout = new GridLayout(2, false);
		contentLayout.marginHeight = 0;
		content.setLayout(contentLayout);
		initNameGroup(content);
		GridData modeLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		modeLayoutData.horizontalSpan = 2;
		initModeGroup(content, modeLayoutData);
		initOptions(content);
		initMessageOptions(content);
		return dialogArea;
	}

	protected void initNameGroup(Composite parent) {
		Label nameLabel = new Label(parent, SWT.NONE);
		nameLabel
				.setText(Messages.TclCheckerConfigurationDialog_ConfigurationName);
		name = new Text(parent, SWT.BORDER);
		name.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bindingContext.bindValue(SWTObservables.observeText(name, SWT.Modify),
				EMFObservables.observeValue(instance,
						ValidatorsPackage.Literals.VALIDATOR_CONFIG__NAME), null,
				null);
	}

	protected void initModeGroup(Composite parent, Object layoutData) {
		workingMode = new Group(parent, SWT.NONE);
		workingMode.setText(Messages.TclChecker_mode);
		workingMode.setLayoutData(layoutData);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 2;
		workingMode.setLayout(layout);

		Map<Button, CheckerMode> modeOptions = new HashMap<Button, CheckerMode>();
		createModeOption(workingMode, Messages.TclChecker_mode_default,
				modeOptions, CheckerMode.DEFAULT);
		createModeOption(workingMode, Messages.TclChecker_mode_none,
				modeOptions, CheckerMode.W0);
		createModeOption(workingMode, Messages.TclChecker_mode_errors,
				modeOptions, CheckerMode.W1);
		createModeOption(workingMode,
				Messages.TclChecker_mode_errorsAndUsageWarnings, modeOptions,
				CheckerMode.W2);
		createModeOption(workingMode,
				Messages.TclChecker_mode_errorsAndWarningsExceptUpgrade,
				modeOptions, CheckerMode.W3);
		createModeOption(workingMode, Messages.TclChecker_mode_all,
				modeOptions, CheckerMode.W4);
		bindingContext.bindValue(new RadioButtonListValue<CheckerMode>(
				CheckerMode.class, modeOptions), EMFObservables.observeValue(
				instance, ConfigsPackage.Literals.CHECKER_CONFIG__MODE), null,
				null);
	}

	private void createModeOption(Group parent, String text,
			Map<Button, CheckerMode> modeOptions, CheckerMode checkerMode) {
		final Button button = new Button(parent, SWT.RADIO);
		button.setText(text);
		modeOptions.put(button, checkerMode);
	}

	protected void initOptions(Composite parent) {
		Group group = SWTFactory.createGroup(parent,
				Messages.TclCheckerConfigurationDialog_Options, 2, 2,
				GridData.FILL_HORIZONTAL);
		Label nameLabel = new Label(group, SWT.NONE);
		nameLabel
				.setText(Messages.TclCheckerConfigurationDialog_CommandLineOptions);
		commandLineOptions = new Text(group, SWT.BORDER);
		commandLineOptions
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bindingContext
				.bindValue(
						SWTObservables.observeText(commandLineOptions,
								SWT.Modify),
						EMFObservables
								.observeValue(
										instance,
										ValidatorsPackage.Literals.VALIDATOR_CONFIG__COMMAND_LINE_OPTIONS),
						null, null);
		summary = SWTFactory.createCheckButton(group,
				Messages.TclCheckerConfigurationDialog_Summary, null, false, 2);
		bindingContext.bindValue(SWTObservables.observeSelection(summary),
				EMFObservables.observeValue(instance,
						ConfigsPackage.Literals.CHECKER_CONFIG__SUMMARY), null,
				null);
		useTclVer = SWTFactory.createCheckButton(group,
				Messages.TclCheckerConfigurationDialog_UseTclVer, null, false,
				2);
		bindingContext.bindValue(SWTObservables.observeSelection(useTclVer),
				EMFObservables.observeValue(instance,
						ConfigsPackage.Literals.CHECKER_CONFIG__USE_TCL_VER),
				null, null);
	}

	private void initMessageOptions(Composite parent) {
		final Group group = SWTFactory.createGroup(parent,
				Messages.TclChecker_suppressProblems, 2, 2, GridData.FILL_BOTH);
		final Button individualMessageConfiguration = SWTFactory
				.createCheckButton(
						group,
						Messages.TclCheckerConfigurationDialog_MessageConfiguration,
						null, false, 2);
		bindingContext
				.bindValue(
						SWTObservables
								.observeSelection(individualMessageConfiguration),
						EMFObservables
								.observeValue(
										instance,
										ConfigsPackage.Literals.CHECKER_CONFIG__INDIVIDUAL_MESSAGE_STATES),
						null, null);
		final Composite messageContainer = new Composite(group, SWT.NONE);
		final GridLayout messageContainerLayout = new GridLayout(2, false);
		messageContainerLayout.marginWidth = 0;
		messageContainerLayout.marginHeight = 0;
		messageContainer.setLayout(messageContainerLayout);
		messageContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		final Composite messageComposite = new Composite(messageContainer,
				SWT.NONE);
		final TreeColumnLayout messageTreeLayout = new TreeColumnLayout();
		messageComposite.setLayout(messageTreeLayout);
		final GridData messageGridData = new GridData(GridData.FILL_BOTH);
		messageGridData.heightHint = new PixelConverter(parent)
				.convertHeightInCharsToPixels(16);
		messageComposite.setLayoutData(messageGridData);
		final Tree messageTree = new Tree(messageComposite, SWT.BORDER
				| SWT.FULL_SELECTION);
		messageTree.setHeaderVisible(true);
		messageTree.setLinesVisible(true);
		final TreeColumn nameColumn = new TreeColumn(messageTree, SWT.LEFT);
		nameColumn.setText(Messages.TclChecker_problems_name);
		messageTreeLayout.setColumnData(nameColumn, new ColumnWeightData(60));
		final TreeColumn typeColumn = new TreeColumn(messageTree, SWT.LEFT);
		typeColumn.setText(Messages.TclChecker_problems_type);
		messageTreeLayout.setColumnData(typeColumn, new ColumnWeightData(20));
		final TreeColumn actionColumn = new TreeColumn(messageTree, SWT.LEFT);
		actionColumn.setText(Messages.TclChecker_problems_action);
		messageTreeLayout.setColumnData(actionColumn, new ColumnWeightData(20));
		messageViewer = new TreeViewer(messageTree);
		new TreeViewerColumn(messageViewer, actionColumn)
				.setEditingSupport(new TclCheckerMessageActionEditingSupport(
						messageViewer));
		messageViewer.setLabelProvider(new MessageLabelProvider());
		messageViewer.setContentProvider(new MessageContentProvider());
		messageViewer.setComparator(new MessageViewerComparator());
		final List<MessageGroup> problemGroups = TclCheckerProblemDescription
				.getProblemGroups();
		messageViewer.setInput(problemGroups);
		messageViewer.expandToLevel(problemGroups.get(0), 1);
		final Composite buttonContainer = new Composite(messageContainer,
				SWT.NONE);
		final GridLayout buttonContainerLayout = new GridLayout(1, false);
		buttonContainerLayout.marginHeight = 0;
		buttonContainerLayout.marginWidth = 0;
		buttonContainerLayout.marginLeft = 5;
		buttonContainer.setLayout(buttonContainerLayout);
		final GridData buttonContainerLayoutData = new GridData(SWT.CENTER,
				SWT.BEGINNING, false, true);
		buttonContainer.setLayoutData(buttonContainerLayoutData);
		SWTFactory.createPushButton(buttonContainer,
				Messages.TclChecker_processType_defaultAll, null)
				.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						instance.getMessageStates().clear();
						messageViewer.refresh();
					}
				});
		SWTFactory.createPushButton(buttonContainer,
				Messages.TclChecker_processType_checkAll).addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						fillMessageState(MessageState.CHECK);
					}
				});
		SWTFactory.createPushButton(buttonContainer,
				Messages.TclChecker_processType_suppressAll)
				.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						fillMessageState(MessageState.SUPPRESS);
					}
				});
		individualMessageConfiguration
				.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						updateMessageViewerStatus(buttonContainer,
								individualMessageConfiguration.getSelection());
					}
				});
		updateMessageViewerStatus(buttonContainer, instance
				.isIndividualMessageStates());
	}

	private void updateMessageViewerStatus(final Composite buttonContainer,
			final boolean enable) {
		messageViewer.getControl().setEnabled(enable);
		for (Control child : buttonContainer.getChildren()) {
			if (child instanceof Button) {
				child.setEnabled(enable);
			}
		}
	}

	/**
	 * @param newState
	 */
	protected void fillMessageState(MessageState newState) {
		for (String messageId : TclCheckerProblemDescription
				.getProblemIdentifiers()) {
			MessageState oldState = instance.getMessageStates().get(messageId);
			if (oldState != newState) {
				instance.getMessageStates().put(messageId, newState);
			}
		}
		messageViewer.refresh();
	}

	private void updateStatus() {
		updateStatus(validate());
	}

	/**
	 * @return
	 */
	protected IStatus validate() {
		final String configurationName = instance.getName();
		if (configurationName == null || configurationName.trim().length() == 0) {
			return new StatusInfo(
					IStatus.ERROR,
					Messages.TclCheckerConfigurationDialog_errorEmptyConfigurationName);
		}
		return StatusInfo.OK_STATUS;
	}

	@Override
	public boolean close() {
		bindingContext.dispose();
		return super.close();
	}

	@Override
	protected Point getInitialSize() {
		final Point p = super.getInitialSize();
		final int expectedWidth = new PixelConverter(getShell())
				.convertWidthInCharsToPixels(110);
		if (p.x < expectedWidth) {
			p.x = expectedWidth;
		}
		return p;
	}

}
