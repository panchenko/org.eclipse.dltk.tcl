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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.custom.BusyIndicator;

public class TreeSelectionControl implements ITreeViewerListener,
		ICheckStateListener {

	/**
	 * @param viewer
	 */
	public TreeSelectionControl(CheckboxTreeViewer viewer) {
		this.fViewer = viewer;
		this.treeContentProvider = (ITreeContentProvider) viewer
				.getContentProvider();
	}

	private final CheckboxTreeViewer fViewer;
	private final ITreeContentProvider treeContentProvider;

	private static class SelectionState {

		public static SelectionState WHITE = new SelectionState();

		public static SelectionState CHECKED = new SelectionState();

		@Override
		public String toString() {
			if (this == WHITE)
				return "WHITE"; //$NON-NLS-1$
			else if (this == CHECKED)
				return "CHECKED"; //$NON-NLS-1$
			else
				return super.toString();
		}

	}

	private Map<Object, SelectionState> checkedStateStore = new HashMap<Object, SelectionState>();
	private Set<Object> whiteChecked = new HashSet<Object>();
	private Set<Object> expandedTreeNodes = new HashSet<Object>();

	protected void treeItemChecked(Object treeElement, boolean state) {
		// recursively adjust all child tree elements appropriately
		setTreeChecked(treeElement, state);
		Object parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			// now update upwards in the tree hierarchy
			if (state) {
				grayCheckHierarchy(parent);
			} else {
				ungrayCheckHierarchy(parent);
			}
			// Update the hierarchy but do not white select the parent
			grayUpdateHierarchy(parent);
		}
		if (DLTKCore.DEBUG) {
			dump();
		}
	}

	/**
	 * 
	 */
	void dump() {
		final ArrayList<Object> includes = new ArrayList<Object>();
		final ArrayList<Object> excludes = new ArrayList<Object>();
		ICollector collector = new ICollector() {
			public void include(Object object) {
				includes.add(object);
			}

			public void exclude(Object arg0) {
				excludes.add(arg0);
			}
		};
		collectCheckedItems(collector);
		System.out.println("=== [INCLUDES]"); //$NON-NLS-1$
		for (Iterator<?> i = includes.iterator(); i.hasNext();) {
			Object item = i.next();
			if (item instanceof IModelElement) {
				System.out.println(((IModelElement) item).getPath());
			}
		}
		System.out.println("=== [EXCLUDES]"); //$NON-NLS-1$
		for (Iterator<?> i = excludes.iterator(); i.hasNext();) {
			Object item = i.next();
			if (item instanceof IModelElement) {
				System.out.println(((IModelElement) item).getPath());
			}
		}
		System.out.println("==="); //$NON-NLS-1$
	}

	/**
	 * Returns a flat list of all of the leaf elements which are checked. Filter
	 * then based on the supplied ElementFilter. If monitor is cancelled then
	 * return null
	 * 
	 * @param collector
	 *            - the filter for the data
	 */
	public void collectCheckedItems(ICollector collector) {
		// Iterate through the children of the root as the root is not in the
		// store
		Object[] children = treeContentProvider.getElements(root);
		for (int i = 0; i < children.length; ++i) {
			final Object child = children[i];
			collectAllSelectedItems(child, whiteChecked.contains(child),
					collector, false);
		}
	}

	public interface ICollector {
		public void include(Object element);

		public void exclude(Object element);
	}

	/**
	 * Add all of the selected children of nextEntry to result recursively. This
	 * does not set any values in the checked state.
	 * 
	 * @param The
	 *            treeElement being queried
	 * @param addAll
	 *            a boolean to indicate if the checked state store needs to be
	 *            queried
	 * @param collector
	 *            IElementFilter - the filter being used on the data
	 */
	private void collectAllSelectedItems(Object treeElement, boolean addAll,
			ICollector collector, boolean wasInclude) {
		boolean nextWasInclude = wasInclude;
		if (addAll) {
			if (!wasInclude) {
				collector.include(treeElement);
				nextWasInclude = true;
			}
		} else { // Add what we have stored
			SelectionState state = checkedStateStore.get(treeElement);
			if (state != null) {
				if (state == SelectionState.CHECKED) {
					if (!wasInclude) {
						collector.include(treeElement);
						nextWasInclude = true;
					}
				}
			} else {
				collector.exclude(treeElement);
				return;
			}
		}
		if (!expandedTreeNodes.contains(treeElement)) {
			return;
		}
		Object[] treeChildren = treeContentProvider.getChildren(treeElement);
		for (int i = 0; i < treeChildren.length; i++) {
			Object child = treeChildren[i];
			if (addAll) {
				collectAllSelectedItems(child, true, collector, nextWasInclude);
			} else {
				if (checkedStateStore.containsKey(child)) {
					// Only continue for those with checked state
					collectAllSelectedItems(child,
							whiteChecked.contains(child), collector,
							nextWasInclude);
				} else {
					collector.exclude(child);
				}
			}
		}
	}

	/**
	 * 
	 */
	public void install() {
		fViewer.addCheckStateListener(this);
		fViewer.addTreeListener(this);
	}

	private Object root;

	public void setInput(Object input) {
		this.root = input;
		fViewer.setInput(input);
		expandedTreeNodes.clear();
		expandedTreeNodes.add(input);
	}

	/**
	 * This method must be called just before this window becomes visible.
	 */
	public void aboutToOpen() {
		// determineWhiteCheckedDescendents(root);
		checkNewTreeElements(treeContentProvider.getElements(root));
		// select the first Object in the list
		Object[] elements = treeContentProvider.getElements(root);
		Object primary = elements.length > 0 ? elements[0] : null;
		if (primary != null) {
			fViewer.setSelection(new StructuredSelection(primary));
		}
	}

	/**
	 * Recursively add appropriate tree elements to the collection of known
	 * white-checked tree elements.
	 * 
	 * @param treeElement
	 */
	protected void determineWhiteCheckedDescendents(Object treeElement) {
		// always go through all children first since their white-checked
		// statuses will be needed to determine the white-checked status for
		// this tree Object
		if (treeElement instanceof IModelElement) {
			System.out.println(((IModelElement) treeElement).getPath());
		}
		Object[] children = root == treeElement ? treeContentProvider
				.getElements(treeElement) : treeContentProvider
				.getChildren(treeElement);
		for (int i = 0; i < children.length; ++i) {
			determineWhiteCheckedDescendents(children[i]);
		}
		// now determine the white-checked status for this tree Object
		if (determineShouldBeWhiteChecked(treeElement)) {
			setWhiteChecked(treeElement, true);
		}
	}

	/**
	 * Returns a boolean indicating whether the passed tree item should be
	 * white-checked.
	 * 
	 * @return boolean
	 * @param treeElement
	 */
	protected boolean determineShouldBeWhiteChecked(Object treeElement) {
		return areAllChildrenWhiteChecked(treeElement)
				&& isChecked(treeElement);
	}

	/**
	 * Return a boolean indicating whether all children of the passed tree
	 * Object are currently white-checked
	 * 
	 * @return boolean
	 * @param treeElement
	 */
	protected boolean areAllChildrenWhiteChecked(Object treeElement) {
		Object[] children = treeContentProvider.getChildren(treeElement);
		for (int i = 0; i < children.length; ++i) {
			if (!whiteChecked.contains(children[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return a boolean indicating whether all list elements associated with the
	 * passed tree Object are currently checked
	 * 
	 * @return boolean
	 * @param treeElement
	 */
	protected boolean isChecked(Object treeElement) {
		return checkedStateStore.get(treeElement) == SelectionState.CHECKED;
	}

	/**
	 * Set the checked state of the passed tree Object appropriately, and do so
	 * recursively to all of its child tree elements as well
	 */
	protected void setTreeChecked(Object treeElement, boolean state) {
		if (state) {
			setListForWhiteSelection(treeElement);
		} else {
			checkedStateStore.remove(treeElement);
		}
		setWhiteChecked(treeElement, state);
		fViewer.setChecked(treeElement, state);
		fViewer.setGrayed(treeElement, false);
		if (expandedTreeNodes.contains(treeElement)) {
			Object[] children = treeContentProvider.getChildren(treeElement);
			for (int i = 0; i < children.length; ++i) {
				Object child = children[i];
				setTreeChecked(child, state);
			}
		}
	}

	/**
	 * Adjust the collection of references to white-checked tree elements
	 * appropriately.
	 * 
	 * @param treeElement
	 * @param isWhiteChecked
	 */
	protected void setWhiteChecked(Object treeElement, boolean isWhiteChecked) {
		if (isWhiteChecked) {
			if (!whiteChecked.contains(treeElement)) {
				whiteChecked.add(treeElement);
			}
		} else {
			whiteChecked.remove(treeElement);
		}
	}

	/**
	 * The treeElement has been white selected. Get the list for the Object and
	 * set it in the checked state store.
	 * 
	 * @param treeElement
	 *            the Object being updated
	 */
	private void setListForWhiteSelection(Object treeElement) {
		checkedStateStore.put(treeElement, SelectionState.CHECKED);
	}

	/**
	 * Logically gray-check all ancestors of treeItem by ensuring that they
	 * appear in the checked table
	 */
	protected void grayCheckHierarchy(Object treeElement) {
		// expand the Object first to make sure we have populated for it
		expandTreeElement(treeElement);
		// if this tree Object is already gray then its ancestors all are as
		// well
		if (checkedStateStore.containsKey(treeElement)) {
			return; // no need to proceed upwards from here
		}
		checkedStateStore.put(treeElement, SelectionState.WHITE);
		Object parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			grayCheckHierarchy(parent);
		}
	}

	/**
	 * Expand an Object in a tree viewer
	 */
	private void expandTreeElement(final Object item) {
		BusyIndicator.showWhile(fViewer.getControl().getDisplay(),
				new Runnable() {
					public void run() {
						// First see if the children need to be given their
						// checked state at all. If they've
						// already been realized then this won't be necessary
						if (expandedTreeNodes.contains(item)) {
							checkNewTreeElements(treeContentProvider
									.getChildren(item));
						} else {
							expandedTreeNodes.add(item);
							if (whiteChecked.contains(item)) {
								// If this is the first expansion and this is a
								// white checked node then check the children
								Object[] children = treeContentProvider
										.getChildren(item);
								for (int i = 0; i < children.length; ++i) {
									if (!whiteChecked.contains(children[i])) {
										Object child = children[i];
										setWhiteChecked(child, true);
										fViewer.setChecked(child, true);
										checkedStateStore.put(child,
												SelectionState.WHITE);
									}
								}
								// Now be sure to select the list of items too
								setListForWhiteSelection(item);
							}
						}

					}
				});
	}

	/**
	 * Iterate through the passed elements which are being realized for the
	 * first time and check each one in the tree viewer as appropriate
	 */
	protected void checkNewTreeElements(Object[] elements) {
		for (int i = 0; i < elements.length; ++i) {
			Object currentElement = elements[i];
			boolean checked = checkedStateStore.containsKey(currentElement);
			fViewer.setChecked(currentElement, checked);
			fViewer.setGrayed(currentElement, checked
					&& !whiteChecked.contains(currentElement));
		}
	}

	/**
	 * Logically un-gray-check all ancestors of treeItem if appropriate.
	 */
	protected void ungrayCheckHierarchy(Object treeElement) {
		if (!determineShouldBeAtLeastGrayChecked(treeElement)) {
			checkedStateStore.remove(treeElement);
		}
		Object parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			ungrayCheckHierarchy(parent);
		}
	}

	/**
	 * Returns a boolean indicating whether the passed tree Object should be at
	 * LEAST gray-checked. Note that this method does not consider whether it
	 * should be white-checked, so a specified tree item which should be
	 * white-checked will result in a <code>true</code> answer from this method.
	 * To determine whether a tree item should be white-checked use method
	 * #determineShouldBeWhiteChecked(Object).
	 * 
	 * @param treeElement
	 * @return boolean
	 * @see #determineShouldBeWhiteChecked(Object)
	 */
	protected boolean determineShouldBeAtLeastGrayChecked(Object treeElement) {
		// if any list items associated with treeElement are checked then it
		// retains its gray-checked status regardless of its children
		SelectionState checked = checkedStateStore.get(treeElement);
		if (checked == SelectionState.CHECKED) {
			return true;
		}
		// if any children of treeElement are still gray-checked then
		// treeElement
		// must remain gray-checked as well. Only ask expanded nodes
		if (expandedTreeNodes.contains(treeElement)) {
			Object[] children = treeContentProvider.getChildren(treeElement);
			for (int i = 0; i < children.length; ++i) {
				if (checkedStateStore.containsKey(children[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Set the checked state of self and all ancestors appropriately. Do not
	 * white check anyone - this is only done down a hierarchy.
	 */
	private void grayUpdateHierarchy(Object treeElement) {
		boolean shouldBeAtLeastGray = determineShouldBeAtLeastGrayChecked(treeElement);
		fViewer.setGrayChecked(treeElement, shouldBeAtLeastGray);
		if (whiteChecked.contains(treeElement)) {
			whiteChecked.remove(treeElement);
		}
		// proceed up the tree Object hierarchy
		Object parent = treeContentProvider.getParent(treeElement);
		if (parent != null) {
			grayUpdateHierarchy(parent);
		}
	}

	/**
	 * Handle the collapsing of an Object in a tree viewer
	 */
	public void treeCollapsed(TreeExpansionEvent event) {
		// We don't need to do anything with this
	}

	/**
	 * Handle the expansion of an Object in a tree viewer
	 */
	public void treeExpanded(TreeExpansionEvent event) {
		expandTreeElement(event.getElement());
	}

	/*
	 * @see ICheckStateListener#checkStateChanged(CheckStateChangedEvent)
	 */
	public void checkStateChanged(CheckStateChangedEvent event) {
		treeItemChecked(event.getElement(), event.getChecked());
	}

	/**
	 * Update the selections of the tree elements in items to reflect the new
	 * selections provided.
	 * 
	 * @param includes
	 * @param excludes
	 */
	public void setInitialState(Collection<?> includes, Collection<?> excludes) {
		// We are replacing all selected items with the given selected items,
		// so reinitialize everything.
		// fViewer.setCheckedElements(new Object[0]);
		whiteChecked.clear();
		checkedStateStore.clear();
		final Set<Object> includedNodes = new HashSet<Object>();
		// Update the store before the hierarchy to prevent updating parents
		// before all of the children are done
		for (Iterator<?> i = includes.iterator(); i.hasNext();) {
			Object key = i.next();
			// Replace the items in the checked state store with those from the
			// supplied items
			checkedStateStore.put(key, SelectionState.CHECKED);
			whiteChecked.add(key);
			includedNodes.add(key);
			// proceed up the tree element hierarchy
			Object parent = treeContentProvider.getParent(key);
			if (parent != null) {
				// proceed up the tree element hierarchy and make sure
				// everything is in the table
				primeHierarchyForInclusion(parent, includedNodes);
			}
		}

		final Set<Object> excludedNodes = new HashSet<Object>();
		for (Iterator<?> i = excludes.iterator(); i.hasNext();) {
			Object key = i.next();
			excludedNodes.add(key);
			// proceed up the tree element hierarchy
			Object parent = treeContentProvider.getParent(key);
			if (parent != null) {
				// proceed up the tree element hierarchy and make sure
				// everything is in the table
				primeHierarchyForExclusion(parent, excludedNodes);
			}
		}
		if (containsAny(includedNodes, excludedNodes)) {

		}
		dump();
		// System.out.println(whiteChecked);
		// Update the checked tree items. Since each tree item has a selected
		// item, all the tree items will be gray checked.
		// fViewer.setCheckedElements(checkedStateStore.keySet().toArray());
		// for (Iterator<?> i = excludes.iterator(); i.hasNext();) {
		// Object key = i.next();
		// fViewer.setChecked(key, false);
		// fViewer.setGrayed(key, false);
		// }
		// fViewer.setGrayedElements(checkedStateStore.keySet().toArray());
	}

	/**
	 * @param collection
	 * @param subset
	 * @return
	 */
	private boolean containsAny(Collection<Object> collection,
			Collection<Object> subset) {
		for (Object obj : subset) {
			if (collection.contains(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Logically gray-check all ancestors of treeItem by ensuring that they
	 * appear in the checked table. Add any elements to the selectedNodes so we
	 * can track that has been done.
	 */
	private void primeHierarchyForInclusion(Object item,
			Set<Object> selectedNodes) {
		// Only prime it if we haven't visited yet
		if (selectedNodes.contains(item)) {
			return;
		}
		checkedStateStore.put(item, SelectionState.WHITE);
		// mark as expanded as we are going to populate it after this
		expandedTreeNodes.add(item);
		selectedNodes.add(item);
		Object parent = treeContentProvider.getParent(item);
		if (parent != null) {
			primeHierarchyForInclusion(parent, selectedNodes);
		}
	}

	/**
	 * Logically gray-check all ancestors of treeItem by ensuring that they
	 * appear in the checked table. Add any elements to the selectedNodes so we
	 * can track that has been done.
	 */
	private void primeHierarchyForExclusion(Object item,
			Set<Object> selectedNodes) {
		// Only prime it if we haven't visited yet
		if (selectedNodes.contains(item)) {
			return;
		}		
		if (expandedTreeNodes.contains(item)) {
			
		}
		else {
			
		}
		selectedNodes.add(item);
		Object parent = treeContentProvider.getParent(item);
		if (parent != null) {
			primeHierarchyForInclusion(parent, selectedNodes);
		}
	}
}
