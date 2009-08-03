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
package org.eclipse.dltk.tcl.internal.console.ui;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;

/**
 * Abstract implementation of an action that displays a drop-down launch history
 * for a specific launch group.
 * <p>
 * Clients may subclass this class.
 * </p>
 * 
 * @since 2.0
 */
public abstract class AbstractPulldownAction implements
		IWorkbenchWindowPulldownDelegate2 {

	/**
	 * The menu created by this action
	 */
	private Menu fMenu;

	/**
	 * The action used to render this delegate.
	 */
	private IAction fAction;

	/**
	 * Indicates whether the launch history has changed and the sub menu needs
	 * to be recreated.
	 */
	protected boolean fRecreateMenu = false;

	/**
	 * Sets the action used to render this delegate.
	 * 
	 * @param action
	 *            the action used to render this delegate
	 */
	private void setAction(IAction action) {
		fAction = action;
	}

	/**
	 * Returns the action used to render this delegate.
	 * 
	 * @return the action used to render this delegate
	 */
	protected IAction getAction() {
		return fAction;
	}

	/**
	 * Adds the given action to the specified menu with an accelerator specified
	 * by the given number.
	 * 
	 * @param menu
	 *            the menu to add the action to
	 * @param action
	 *            the action to add
	 * @param accelerator
	 *            the number that should appear as an accelerator
	 */
	protected void addToMenu(Menu menu, IAction action) {
		action.setText(action.getText());
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(menu, -1);
	}

	/**
	 * Initialize this action so that it can dynamically set its tool-tip. Also
	 * set the enabled state of the underlying action based on whether there are
	 * any registered launch configuration types that understand how to launch
	 * in the mode of this action.
	 */
	private void initialize(IAction action) {
		setAction(action);
		updateTooltip();
		// FIXME action.setEnabled(existsConfigTypesForMode());
	}

	/**
	 * Updates this action's tool-tip. The tooltip is based on user preference
	 * settings for launching - either the previous launch, or based on the
	 * selection and which configuration will be launched.
	 * <p>
	 * Subclasses may override as required.
	 * </p>
	 */
	protected void updateTooltip() {
		// FIXME getAction().setToolTipText(getToolTip());
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		setMenu(null);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		setMenu(new Menu(parent));
		fillMenu(fMenu);
		initMenu();
		return fMenu;
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		setMenu(new Menu(parent));
		fillMenu(fMenu);
		initMenu();
		return fMenu;
	}

	/**
	 * Creates the menu for the action
	 */
	private void initMenu() {
		// Add listener to re-populate the menu each time
		// it is shown because of dynamic history list
		fMenu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				if (fRecreateMenu) {
					Menu m = (Menu) e.widget;
					MenuItem[] items = m.getItems();
					for (int i = 0; i < items.length; i++) {
						items[i].dispose();
					}
					fillMenu(m);
					fRecreateMenu = false;
				}
			}
		});
	}

	/**
	 * Sets this action's drop-down menu, disposing the previous menu.
	 * 
	 * @param menu
	 *            the new menu
	 */
	private void setMenu(Menu menu) {
		if (fMenu != null) {
			fMenu.dispose();
		}
		fMenu = menu;
	}

	/**
	 * Fills the drop-down menu with favorites and launch history
	 * 
	 * @param menu
	 *            the menu to fill
	 */
	protected abstract void fillMenu(Menu menu);

	/**
	 * Adds a separator to the given menu
	 * 
	 * @param menu
	 */
	protected void addSeparator(Menu menu) {
		new MenuItem(menu, SWT.SEPARATOR);
	}

	protected Menu addSubmenu(Menu menu, String text) {
		final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText(text);
		final Menu menu2 = new Menu(menuItem);
		menuItem.setMenu(menu2);
		return menu2;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		// do nothing - this is just a menu
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (fAction == null) {
			initialize(action);
		}
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		//
	}

}
