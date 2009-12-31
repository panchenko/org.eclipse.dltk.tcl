/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *     xored software, Inc. - TCL ManPageFolder management refactoring (Alex Panchenko <alex@xored.com>)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.documentation;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.tcl.ui.manpages.Documentation;
import org.eclipse.dltk.tcl.ui.manpages.ManPageLoader;
import org.eclipse.dltk.tcl.ui.manpages.ManPageFolder;
import org.eclipse.dltk.tcl.ui.manpages.ManPageResource;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Control used to edit the libraries associated with a Interpreter install
 */
public class ManPagesLocationsBlock implements ISelectionChangedListener {

	/**
	 * Attribute name for the last path used to open a file/directory chooser
	 * dialog.
	 */
	protected static final String LAST_PATH_SETTING = "LAST_PATH_SETTING"; //$NON-NLS-1$

	/**
	 * the prefix for dialog setting pertaining to this block
	 */
	protected static final String DIALOG_SETTINGS_PREFIX = "ManPagesLocationsBlock"; //$NON-NLS-1$

	private TreeViewer fLocationsViewer;
	private Button fAddButton;
	private Button fEditButton;
	private Button fRemoveButton;
	private Button fDefaultButton;

	private final IStatusChangeListener fPage;
	private final boolean fEditable;

	public ManPagesLocationsBlock(IStatusChangeListener page, boolean editable) {
		fPage = page;
		fEditable = editable;
	}

	private static class ManPagesLabelProvider extends LabelProvider implements
			IFontProvider {

		public ManPagesLabelProvider() {
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof Documentation) {
				return DLTKUIPlugin.getImageDescriptorRegistry().get(
						DLTKPluginImages.DESC_OBJS_JAVADOCTAG);
			} else if (element instanceof ManPageFolder) {
				return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_LIBRARY);
			} else {
				return super.getImage(element);
			}
		}

		@Override
		public String getText(Object element) {
			if (element instanceof Documentation) {
				final Documentation doc = (Documentation) element;
				String text = doc.getName();
				if (doc.isDefault()) {
					text += " [default]";
				}
				return text;
			} else if (element instanceof ManPageFolder) {
				return ((ManPageFolder) element).getPath();
			} else {
				return super.getText(element);
			}
		}

		public Font getFont(Object element) {
			if (element instanceof Documentation) {
				final Documentation doc = (Documentation) element;
				if (doc.isDefault()) {
					return JFaceResources.getFontRegistry().getBold(
							JFaceResources.DIALOG_FONT);
				}
			}
			return null;
		}
	}

	private ManPageResource documentations = null;

	private static class ManLocationsContentProvider implements
			ITreeContentProvider {

		public ManLocationsContentProvider() {
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Documentation) {
				final EList<ManPageFolder> folders = ((Documentation) parentElement)
						.getFolders();
				return folders.toArray(new ManPageFolder[folders.size()]);
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof Documentation)
				return true;
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ManPageResource) {
				final List<Documentation> docs = ((ManPageResource) inputElement)
						.getDocumentations();
				return docs.toArray(new Object[docs.size()]);
			} else {
				return new Object[0];
			}
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	};

	/**
	 * Creates and returns the source lookup control.
	 * 
	 * @param parent
	 *            the parent widget of this control
	 */
	public Control createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = fEditable ? 2 : 1;
		topLayout.marginHeight = 0;
		topLayout.marginWidth = 0;
		comp.setLayout(topLayout);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));

		fLocationsViewer = new TreeViewer(comp);
		final GridData gd = new GridData(GridData.FILL_BOTH);
		final PixelConverter pixConv = new PixelConverter(parent);
		gd.widthHint = pixConv.convertWidthInCharsToPixels(48);
		gd.heightHint = pixConv.convertHeightInCharsToPixels(8);
		fLocationsViewer.getControl().setLayoutData(gd);
		fLocationsViewer.setContentProvider(new ManLocationsContentProvider());
		fLocationsViewer.setLabelProvider(new ManPagesLabelProvider());
		fLocationsViewer.setSorter(new ViewerSorter());
		fLocationsViewer.addSelectionChangedListener(this);
		if (fEditable) {
			createButtons(comp);
		}
		Dialog.applyDialogFont(comp);
		return comp;
	}

	private void createButtons(Composite comp) {
		Composite pathButtonComp = new Composite(comp, SWT.NONE);
		GridLayout pathButtonLayout = new GridLayout();
		pathButtonLayout.marginHeight = 0;
		pathButtonLayout.marginWidth = 0;
		pathButtonComp.setLayout(pathButtonLayout);
		pathButtonComp.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING
						| GridData.HORIZONTAL_ALIGN_FILL));

		fAddButton = createPushButton(pathButtonComp, "Add");
		fAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				add();
			}
		});
		fEditButton = createPushButton(pathButtonComp, "Edit");
		fEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IStructuredSelection selection = getSelection();
				if (selection.size() == 1
						&& selection.getFirstElement() instanceof Documentation) {
					edit((Documentation) selection.getFirstElement());
				}
			}
		});
		fRemoveButton = createPushButton(pathButtonComp, "Remove");
		fRemoveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IStructuredSelection selection = getSelection();
				if (canRemove(selection)) {
					remove(selection);
				}
			}
		});
		fDefaultButton = createPushButton(pathButtonComp, "Set Default");
		((GridData) fDefaultButton.getLayoutData()).verticalIndent = 16;
		fDefaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IStructuredSelection selection = getSelection();
				if (selection.size() == 1
						&& selection.getFirstElement() instanceof Documentation) {
					setDefault((Documentation) selection.getFirstElement());
				}
			}
		});
	}

	protected void setDefault(Documentation documentation) {
		documentation.setDefault(true);
		for (Documentation doc : documentations.getDocumentations()) {
			if (doc != documentation && doc.isDefault()) {
				doc.setDefault(false);
			}
		}
		fLocationsViewer.refresh();
	}

	/**
	 * Creates and returns a button
	 * 
	 * @param parent
	 *            parent widget
	 * @param label
	 *            label
	 * @return Button
	 */
	protected Button createPushButton(Composite parent, String label) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		button.setText(label);
		setButtonLayoutData(button);
		return button;
	}

	static void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int widthHint = 80;
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}

	/**
	 * Create some empty space
	 */
	protected void createVerticalSpacer(Composite comp, int colSpan) {
		Label label = new Label(comp, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = colSpan;
		label.setLayoutData(gd);
	}

	/**
	 * Updates buttons and status based on current mans
	 */
	public void update() {
		updateButtons();
		updatePageStatus(StatusInfo.OK_STATUS);
	}

	// public void setDefaults() {
	// String res = fStore
	// .getDefaultString(TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS);
	// fStore.setValue(TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS, res);
	// initialize();
	// }

	protected void updatePageStatus(IStatus status) {
		if (fPage == null)
			return;
		fPage.statusChanged(status);
	}

	public void initialize() {
		this.documentations = ManPageLoader.load();
		fLocationsViewer.setInput(documentations);
		fLocationsViewer.expandToLevel(2);
		update();
	}

	/**
	 * Saves settings
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		documentations.save(null);
	}

	private Shell getShell() {
		if (fPage instanceof IShellProvider) {
			return ((IShellProvider) fPage).getShell();
		} else {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
		}
	}

	/**
	 * Open the file selection dialog, and add the return locations.
	 */
	protected void add() {
		final ManPagesLocationsDialog dialog = new ManPagesLocationsDialog(
				getShell(), documentations, null);
		if (dialog.open() == Window.OK) {
			final Documentation documentation = dialog.getResult();
			documentations.checkDefault();
			fLocationsViewer.refresh();
			fLocationsViewer
					.setSelection(new StructuredSelection(documentation));
		}
	}

	protected void edit(Documentation documentation) {
		final ManPagesLocationsDialog dialog = new ManPagesLocationsDialog(
				getShell(), documentations, documentation);
		if (dialog.open() == Window.OK) {
			fLocationsViewer.refresh(documentation);
		}
	}

	protected void remove(IStructuredSelection selection) {
		boolean changes = false;
		for (Iterator<?> i = selection.iterator(); i.hasNext();) {
			final Object obj = i.next();
			if (obj instanceof EObject) {
				EcoreUtil.remove(((EObject) obj));
				changes = true;
			}
		}
		if (changes) {
			documentations.checkDefault();
			fLocationsViewer.refresh();
		}
	}

	/*
	 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		updateButtons();
	}

	/**
	 * Refresh the enable/disable state for the buttons.
	 */
	private void updateButtons() {
		if (!fEditable)
			return;
		final IStructuredSelection selection = getSelection();
		final boolean singleDoc = selection.size() == 1
				&& selection.getFirstElement() instanceof Documentation;
		fEditButton.setEnabled(singleDoc);
		fDefaultButton.setEnabled(singleDoc
				&& !((Documentation) selection.getFirstElement()).isDefault());
		fRemoveButton.setEnabled(canRemove(selection));
	}

	protected boolean canRemove(IStructuredSelection selection) {
		if (selection.isEmpty()) {
			return false;
		}
		for (Iterator<?> i = selection.iterator(); i.hasNext();) {
			final Object obj = i.next();
			if (!(obj instanceof Documentation)) {
				return false;
			}
		}
		return true;
	}

	protected IStructuredSelection getSelection() {
		return (IStructuredSelection) fLocationsViewer.getSelection();
	}

}
