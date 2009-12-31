/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.documentation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.ui.manpages.Documentation;
import org.eclipse.dltk.tcl.ui.manpages.ManPageFinder;
import org.eclipse.dltk.tcl.ui.manpages.ManPageFolder;
import org.eclipse.dltk.tcl.ui.manpages.ManPageResource;
import org.eclipse.dltk.tcl.ui.manpages.ManpagesFactory;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.dialogs.TimeTriggeredProgressMonitorDialog;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ManPagesLocationsDialog extends StatusDialog implements
		ModifyListener {

	private final boolean isNew;
	private final ManPageResource documentations;
	private final Documentation documentation;
	private final Documentation input;

	/**
	 * @param shell
	 * @param documentation
	 */
	public ManPagesLocationsDialog(Shell shell, ManPageResource documentations,
			Documentation documentation) {
		super(shell);
		this.documentations = documentations;
		this.input = documentation;
		this.isNew = documentation == null;
		this.documentation = documentation != null ? (Documentation) EcoreUtil
				.copy(documentation) : newDocumentation();
		setTitle(isNew ? "Add Documentation Set" : "Edit Documentation Set");
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	private static Documentation newDocumentation() {
		final Documentation value = ManpagesFactory.eINSTANCE
				.createDocumentation();
		value.setId(EcoreUtil.generateUUID());
		value.setName(""); //$NON-NLS-1$
		return value;
	}

	/**
	 * @return
	 */
	public Documentation getResult() {
		return documentation;
	}

	private Text nameField;
	private TreeViewer pathViewer;
	private Button addButton;
	private Button removeButton;

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite dialogArea = (Composite) super.createDialogArea(parent);
		final Composite content = new Composite(dialogArea, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		content.setLayout(new GridLayout(3, false));
		new Label(content, SWT.NONE).setText("Name");
		nameField = new Text(content, SWT.BORDER);
		final GridData nameGD = new GridData(GridData.FILL_HORIZONTAL);
		nameGD.horizontalSpan = 2;
		nameField.setLayoutData(nameGD);
		nameField.addModifyListener(this);
		final Label pathLabel = new Label(content, SWT.NONE);
		pathLabel.setText("Paths");
		pathLabel.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING
						| GridData.VERTICAL_ALIGN_BEGINNING));
		final Composite pathComp = new Composite(content, SWT.NONE);
		pathComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout pathLayout = new GridLayout(2, false);
		pathLayout.marginWidth = pathLayout.marginHeight = 0;
		pathComp.setLayout(pathLayout);
		pathViewer = new TreeViewer(pathComp);
		pathViewer.setContentProvider(new FolderContentProvider());
		pathViewer.setLabelProvider(new FolderLabelProvider());
		pathViewer.setSorter(new ViewerSorter());
		final GridData pathGD = new GridData(GridData.FILL_BOTH);
		pathGD.heightHint = convertHeightInCharsToPixels(16);
		pathGD.widthHint = convertWidthInCharsToPixels(64);
		pathViewer.getControl().setLayoutData(pathGD);
		pathViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				pathSelectionChanged();
			}
		});
		final Composite buttonComp = new Composite(pathComp, SWT.NONE);
		final GridLayout buttonLayout = new GridLayout();
		buttonLayout.marginHeight = buttonLayout.marginWidth = 0;

		buttonComp.setLayout(buttonLayout);
		buttonComp.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		addButton = new Button(buttonComp, SWT.PUSH);
		ManPagesLocationsBlock.setButtonLayoutData(addButton);
		addButton.setText("Add");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAdd();
			}
		});
		removeButton = new Button(buttonComp, SWT.PUSH);
		ManPagesLocationsBlock.setButtonLayoutData(removeButton);
		removeButton.setText("Remove");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IStructuredSelection selection = getSelection();
				if (canRemove(selection)) {
					doRemove(selection);
				}
			}

		});
		//		
		getData(documentation);
		//	
		return dialogArea;
	}

	static class FolderContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ManPageFolder) {
				final ManPageFolder folder = (ManPageFolder) parentElement;
				final EMap<String, String> keywords = folder.getKeywords();
				return keywords.toArray(new Map.Entry[keywords.size()]);
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			if (element instanceof EObject) {
				return ((EObject) element).eContainer();
			} else {
				return null;
			}
		}

		public boolean hasChildren(Object element) {
			if (element instanceof ManPageFolder) {
				final ManPageFolder folder = (ManPageFolder) element;
				return !folder.getKeywords().isEmpty();
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Documentation) {
				final Documentation doc = (Documentation) inputElement;
				final List<ManPageFolder> folders = doc.getFolders();
				return folders.toArray(new Object[folders.size()]);
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	static class FolderLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof ManPageFolder) {
				return ((ManPageFolder) element).getPath();
			} else if (element instanceof EObject
					&& element instanceof BasicEMap.Entry<?, ?>) {
				@SuppressWarnings("unchecked")
				final BasicEMap.Entry<String, String> entry = (BasicEMap.Entry<String, String>) element;
				return entry.getKey() + " (" + entry.getValue() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			return super.getText(element);
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof ManPageFolder) {
				return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_LIBRARY);
			} else {
				return DLTKUIPlugin.getImageDescriptorRegistry().get(
						DLTKPluginImages.DESC_OBJS_INFO_OBJ);
			}
		}

	}

	protected void doAdd() {
		final DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setMessage("Select directory to search into");
		final String result = dialog.open();
		if (result != null) {
			final File file = new File(result);
			if (file.isDirectory()) {
				ProgressMonitorDialog dialog2 = new TimeTriggeredProgressMonitorDialog(
						null, 500);
				try {
					dialog2.run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) {
							monitor.beginTask("Searching for man pages", 1);
							final ManPageFinder finder = new ManPageFinder();
							finder.find(documentation, file);
							monitor.done();
						}
					});
				} catch (InvocationTargetException e) {
					TclUI.error(e);
				} catch (InterruptedException e) {
					// ignore
				}
				pathViewer.refresh();
				updateStatus();
			}
		}
	}

	protected void doRemove(IStructuredSelection selection) {
		for (Iterator<?> i = selection.iterator(); i.hasNext();) {
			final Object obj = i.next();
			if (obj instanceof ManPageFolder) {
				EcoreUtil.remove((EObject) obj);
			}
		}
		pathViewer.refresh();
		updateStatus();
	}

	protected void pathSelectionChanged() {
		final IStructuredSelection selection = getSelection();
		removeButton.setEnabled(canRemove(selection));
	}

	/**
	 * @param doc
	 */
	private void getData(Documentation doc) {
		++busyCounter;
		try {
			nameField.setText(doc.getName());
			pathViewer.setInput(documentation);
		} finally {
			--busyCounter;
		}
	}

	private void setData(Documentation doc) {
		doc.setName(nameField.getText().trim());
	}

	@Override
	protected void okPressed() {
		setData(documentation);
		if (isNew) {
			documentations.getContents().add(documentation);
		} else {
			input.setName(documentation.getName());
			// TODO smart update
			input.getFolders().clear();
			input.getFolders().addAll(documentation.getFolders());
		}
		super.okPressed();
	}

	private int busyCounter = 0;

	public void modifyText(ModifyEvent e) {
		if (busyCounter > 0) {
			return;
		}
		updateStatus();
	}

	private void updateStatus() {
		updateStatus(validate());
	}

	private IStatus validate() {
		if (nameField.getText().trim().length() == 0) {
			return new StatusInfo(IStatus.ERROR,
					"Enter a name for the documentation set");
		}
		// TODO validate name uniqueness
		if (documentation.getFolders().isEmpty()) {
			return new StatusInfo(IStatus.ERROR,
					"Specify documentation folders");
		}
		return StatusInfo.OK_STATUS;
	}

	protected IStructuredSelection getSelection() {
		return (IStructuredSelection) pathViewer.getSelection();
	}

	protected boolean canRemove(IStructuredSelection selection) {
		if (selection.isEmpty()) {
			return false;
		}
		for (Iterator<?> i = selection.iterator(); i.hasNext();) {
			final Object obj = i.next();
			if (!(obj instanceof ManPageFolder)) {
				return false;
			}
		}
		return true;
	}

}
