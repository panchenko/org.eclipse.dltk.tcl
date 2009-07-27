package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.ui.environment.IEnvironmentUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SourcesSelectionDialog extends Dialog {

	private final class SourcesLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof String) {
				return (String) element;
			}
			return "";
		}
	}

	private final class SourcesContentProvider implements
			IStructuredContentProvider {
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			return sources.toArray();
		}
	}

	private ListViewer sourcesViewer;
	private Set<String> sources = new HashSet<String>();
	private IEnvironmentUI environmentUI;
	private IEnvironment environment;
	private Button remove;
	private Button add;

	protected SourcesSelectionDialog(IShellProvider parentShell,
			IEnvironment environment) {
		super(parentShell);
		this.environment = environment;
		this.environmentUI = (IEnvironmentUI) environment
				.getAdapter(IEnvironmentUI.class);
	}

	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Files selection dialog.");
		Composite contents = (Composite) super.createDialogArea(parent);
		contents.setLayout(new GridLayout(2, false));
		sourcesViewer = new ListViewer(contents, SWT.SINGLE | SWT.BORDER);
		sourcesViewer.setLabelProvider(new SourcesLabelProvider());
		sourcesViewer.setContentProvider(new SourcesContentProvider());
		sourcesViewer.setInput(sources);
		sourcesViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite buttons = new Composite(contents, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.DEFAULT, SWT.FILL, false, true));
		buttons.setLayout(new GridLayout(1, false));
		add = new Button(buttons, SWT.PUSH);
		add.setText("Add");
		add.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				String file = environmentUI.selectFile(sourcesViewer.getList()
						.getShell(), IEnvironmentUI.DEFAULT);
				if (file != null) {
					sources.add(file);
					sourcesViewer.refresh();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		add.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		remove = new Button(buttons, SWT.PUSH);
		remove.setText("Remove");
		remove.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		remove.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String path = getSelection();
				if (path != null) {
					sources.remove(path);
					sourcesViewer.refresh();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		updateEnablement();
		sourcesViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						updateEnablement();
					}
				});
		getShell().layout();
		return contents;
	}

	@Override
	protected Point getInitialSize() {
		Point size = super.getInitialSize();
		if (size.x < 400) {
			size.x = 400;
		}
		if (size.y < 300) {
			size.y = 300;
		}
		return size;
	}

	private String getSelection() {
		IStructuredSelection selection = (IStructuredSelection) sourcesViewer
				.getSelection();
		if (selection.isEmpty()) {
			return null;
		}
		return (String) selection.getFirstElement();
	}

	private void updateEnablement() {
		String path = getSelection();
		if (path == null) {
			// edit.setEnabled(false);
			remove.setEnabled(false);
		} else {
			// edit.setEnabled(true);
			remove.setEnabled(true);
		}
	}

	public String[] getSources() {
		return (String[]) sources.toArray(new String[sources.size()]);
	}
}
