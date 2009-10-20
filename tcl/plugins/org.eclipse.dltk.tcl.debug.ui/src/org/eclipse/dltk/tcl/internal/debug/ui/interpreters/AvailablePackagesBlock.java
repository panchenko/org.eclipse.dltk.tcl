package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * @since 2.0
 */
public class AvailablePackagesBlock {
	private List<String> elements = new ArrayList<String>();
	private TreeViewer viewer;

	public void createIn(Composite content) {
		viewer = new TreeViewer(content);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.heightHint = 50;
		viewer.getControl().setLayoutData(gd);
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public Image getImage(Object element) {
				return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_PACKAGE);
			}
		});
		viewer.setContentProvider(new ITreeContentProvider() {
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				return elements.toArray();
			}

			public boolean hasChildren(Object element) {
				return false;
			}

			public Object getParent(Object element) {
				return null;
			}

			public Object[] getChildren(Object parentElement) {
				return new Object[0];
			}
		});
		viewer.setInput(elements);
	}

	public void updatePackages(List<String> packages) {
		elements.clear();
		elements.addAll(packages);
		Collections.sort(elements);
		viewer.getControl().getDisplay().asyncExec(new Runnable() {
			public void run() {
				viewer.refresh();
			}
		});
	}
}
