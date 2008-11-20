/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation
 *******************************************************************************/
package org.eclipse.dltk.ui.tests.swtbot.operations;

import java.util.List;
import java.util.StringTokenizer;

import net.sf.swtbot.SWTBotTestCase;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;
import net.sf.swtbot.eclipse.finder.widgets.SWTBotView;
import net.sf.swtbot.finder.ControlFinder;
import net.sf.swtbot.finder.MenuFinder;
import net.sf.swtbot.finder.UIThreadRunnable;
import net.sf.swtbot.finder.results.WidgetResult;
import net.sf.swtbot.matcher.WidgetMatcherFactory;
import net.sf.swtbot.matcher.WidgetOfType;
import net.sf.swtbot.wait.DefaultCondition;
import net.sf.swtbot.widgets.AbstractSWTBot;
import net.sf.swtbot.widgets.SWTBotButton;
import net.sf.swtbot.widgets.SWTBotCheckBox;
import net.sf.swtbot.widgets.SWTBotCombo;
import net.sf.swtbot.widgets.SWTBotMenu;
import net.sf.swtbot.widgets.SWTBotRadio;
import net.sf.swtbot.widgets.SWTBotShell;
import net.sf.swtbot.widgets.SWTBotStyledText;
import net.sf.swtbot.widgets.SWTBotTree;
import net.sf.swtbot.widgets.SWTBotTreeItem;
import net.sf.swtbot.widgets.TimeoutException;
import net.sf.swtbot.widgets.WidgetNotFoundException;

import org.eclipse.dltk.ui.tests.swtbot.ErrorMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

public class Operations {

	public static final String MENU_FILE = OperationMessages.Operations_menu_file;
	public static final String MENU_EDIT = OperationMessages.Operations_menu_edit;
	public static final String MENU_REFACTOR = OperationMessages.Operations_menu_refactor;
	public static final String MENU_NAVIGATE = OperationMessages.Operations_menu_navigate;
	public static final String MENU_WINDOW = OperationMessages.Operations_menu_window;
	public static final String MENU_PREFERENCES = OperationMessages.Operations_menu_preferences;
	public static final String MENU_NEW = OperationMessages.Operations_menu_new;
	public static final String MENU_OPEN = OperationMessages.Operations_menu_open;
	public static final String MENU_NEW_PROJECT = OperationMessages.Operations_menu_new_project;
	public static final String MENU_TCL_PROJECT = OperationMessages.Operations_menu_tcl_project;
	public static final String MENU_NEW_FILE = OperationMessages.Operations_menu_new_file;
	public static final String MENU_TCL_FILE = OperationMessages.Operations_menu_tcl_file;
	public static final String MENU_NEW_FOLDER = OperationMessages.Operations_menu_new_folder;
	public static final String MENU_NEW_SOURCE_FOLDER = OperationMessages.Operations_menu_new_source_folder;
	public static final String MENU_COPY = OperationMessages.Operations_menu_copy;
	public static final String MENU_PASTE = OperationMessages.Operations_menu_paste;
	public static final String MENU_DELETE = OperationMessages.Operations_menu_delete;
	public static final String MENU_RENAME = OperationMessages.Operations_menu_rename;

	public static final String MENU_RUN_AS = OperationMessages.Operations_menu_run_as;
	public static final String MENU_DEBUG_AS = OperationMessages.Operations_menu_debug_as;

	public static final String MENU_OPEN_DECLARATION = OperationMessages.Operations_menu_open_declaration;

	public static final String WIZARD_NEXT = createLabel(IDialogConstants.NEXT_LABEL);
	public static final String WIZARD_FINISH = createLabel(IDialogConstants.FINISH_LABEL);

	public static final String SCRIPT_POPUP_OPEN_PRJ = OperationMessages.Operations_script_popup_open_prj;
	public static final String SCRIPT_POPUP_CLOSE_PRJ = OperationMessages.Operations_script_popup_close_prj;
	public static final String SCRIPT_POPUP_PROPERTIES = OperationMessages.Operations_script_popup_properties;

	public static final String DLG_PREFERENCES = OperationMessages.Operations_dlg_preferences;
	public static final String DLG_PRJ_PROPERTIES = OperationMessages.Operations_dlg_prj_properties;
	public static final String DLG_NEW_PROJECT = OperationMessages.Operations_dlg_new_project;
	public static final String DLG_NEW_FILE = OperationMessages.Operations_dlg_new_file;
	public static final String DLG_NEW_FOLDER = OperationMessages.Operations_dlg_new_folder;
	public static final String DLG_NEW_SOURCE_FOLDER = OperationMessages.Operations_dlg_new_source_folder;
	public static final String DLG_CREATE_TCL_FILE = OperationMessages.Operations_dlg_create_tcl_file;
	public static final String DLG_RENAME = OperationMessages.Operations_dlg_rename;
	public static final String DLG_RENAME_SCRIPT_PROJECT = OperationMessages.Operations_dlg_rename_script_project;
	public static final String DLG_RENAME_SOURCE_FOLDER = OperationMessages.Operations_dlg_rename_source_folder;
	public static final String DLG_RENAME_SOURCE_MODULE = OperationMessages.Operations_dlg_rename_source_module;
	public static final String DLG_CONFIRM_DELETE = OperationMessages.Operations_dlg_confirm_delete;

	public static final String VIEW_SCRIPT_EXPLORER = OperationMessages.Operations_view_script_explorer;
	public static final String VIEW_CONSOLE = OperationMessages.Operations_view_console;

	public static final String PREFERENCES_TCL = OperationMessages.Operations_preferences_tcl;

	/**
	 * Remove <code>&</code> symbol.
	 * 
	 * @param src
	 * @return
	 */
	public static String createLabel(String src) {
		return src.replace("&", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private SWTEclipseBot bot;

	public Operations(SWTEclipseBot bot) {
		this.bot = bot;
	}

	protected SWTEclipseBot getBot() {
		return bot;
	}

	protected void waitEnableAndClick(final String name)
			throws WidgetNotFoundException, TimeoutException {
		final SWTBotButton btnBot = getBot().button(name);
		getBot().waitUntil(new DefaultCondition() {
			private String errorMessage = "Can't click to button " + name; //$NON-NLS-1$

			public String getFailureMessage() {
				return errorMessage;
			}

			public boolean test() throws Exception {
				try {
					btnBot.click();
					return true;
				} catch (Exception ex) {
					errorMessage = ex.getLocalizedMessage();
					return false;
				}
			}
		});
	}

	protected SWTBotShell openPreferences() throws WidgetNotFoundException,
			TimeoutException {
		getBot().menu(MENU_WINDOW).menu(MENU_PREFERENCES).click();
		SWTBotShell result = getBot().shell(DLG_PREFERENCES);
		result.activate();
		return result;
	}

	protected void openInterpreters() throws WidgetNotFoundException,
			TimeoutException {
		openPreferences();
		expandTree(PREFERENCES_TCL, "Interpreters"); //$NON-NLS-1$
	}

	protected void openProperties(SWTBotTreeItem projectTree)
			throws WidgetNotFoundException, TimeoutException {
		projectTree.contextMenu(SCRIPT_POPUP_PROPERTIES).click();
		String dlgProp = DLG_PRJ_PROPERTIES + projectTree.getText();
		waitActivateShell(dlgProp);
	}

	protected void waitActivateShell(final String shellName)
			throws WidgetNotFoundException, TimeoutException {
		bot.waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return null;
			}

			public boolean test() throws Exception {
				SWTBotShell activeShell = getBot().activeShell();
				return activeShell.getText().equals(shellName);
			}
		});
	}

	protected void waitCloseAllDialog() throws WidgetNotFoundException,
			TimeoutException {
		bot.waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "Opened dialog exist."; //$NON-NLS-1$
			}

			public boolean test() throws Exception {
				return (getBot().shells().length == 1);
			}
		});
	}

	protected SWTBotTreeItem expandTree(String item, String subitem)
			throws WidgetNotFoundException, TimeoutException {
		SWTBotTree treeBot = getBot().tree();
		SWTBotTreeItem result = treeBot.getTreeItem(item).expand().expandNode(
				subitem);
		result.select();

		return result;
	}

	protected SWTBotStyledText getConsole() throws WidgetNotFoundException,
			TimeoutException {
		SWTBotView viewBot = getBot().view(VIEW_CONSOLE);
		getBot().waitUntil(new ViewFocusCondition(viewBot));
		return styledTextBoxWithoutLabel(0);
	}

	protected void closeConsole() throws WidgetNotFoundException,
			TimeoutException {
		SWTBotView viewBot = getBot().view(VIEW_CONSOLE);
		viewBot.close();
	}

	protected SWTBotTreeItem getProjectItem(String name)
			throws WidgetNotFoundException, TimeoutException {
		SWTBotView viewBot = getBot().view(VIEW_SCRIPT_EXPLORER);
		getBot().waitUntil(new ViewFocusCondition(viewBot));
		final SWTBotTree treeBot = getBot().tree();
		getBot().waitUntil(new WidgetFocusCondition(treeBot));
		SWTBotTreeItem result = treeBot.getTreeItem(name);
		result.select();
		return result;
	}

	protected SWTBotTreeItem navigateToProjectElement(String projectName,
			String pathToElement) throws WidgetNotFoundException,
			TimeoutException {
		return navigateToProjectElement(getProjectItem(projectName),
				pathToElement);
	}

	protected SWTBotTreeItem navigateToProjectElement(SWTBotTreeItem item,
			String pathToElement) throws WidgetNotFoundException,
			TimeoutException {
		StringTokenizer tok = new StringTokenizer(pathToElement, "/\\"); //$NON-NLS-1$
		while (tok.hasMoreTokens()) {
			item.expand();
			String name = tok.nextToken();
			item = item.getNode(name);
			if (item == null) {
				throw new WidgetNotFoundException("Element " + name //$NON-NLS-1$
						+ " not found in project tree"); //$NON-NLS-1$
			}
		}
		item.select();
		return item;
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// ICondition classes
	//
	// //////////////////////////////////////////////////////////////////////////
	public class WidgetFocusCondition extends DefaultCondition {

		private AbstractSWTBot abstractBot;

		public WidgetFocusCondition(AbstractSWTBot abstractBot) {
			this.abstractBot = abstractBot;
		}

		public String getFailureMessage() {
			return ErrorMessages.Common_errSetFocus;
		}

		public boolean test() throws Exception {
			try {
				abstractBot.setFocus();
				return true;
			} catch (Throwable th) {
				return false;
			}
		}
	}

	public class ViewFocusCondition extends DefaultCondition {

		private SWTBotView botView;

		public ViewFocusCondition(SWTBotView botView) {
			this.botView = botView;
		}

		public String getFailureMessage() {
			return ErrorMessages.Common_errSetFocus;
		}

		public boolean test() throws Exception {
			try {
				botView.setFocus();
				return true;
			} catch (Throwable th) {
				return false;
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// SWTBot
	//
	// //////////////////////////////////////////////////////////////////////////
	protected SWTBotCombo comboBoxWithoutLabel(int index)
			throws WidgetNotFoundException {
		List findControls = new ControlFinder()
				.findControls(new WidgetOfType<Combo>(Combo.class));
		if (findControls.size() < index) {
			throw new WidgetNotFoundException(
					"Could not find combo with index " + index); //$NON-NLS-1$
		}

		return new SWTBotCombo((Combo) findControls.get(index));
	}

	protected SWTBotStyledText styledTextBoxWithoutLabel(int index)
			throws WidgetNotFoundException {
		List findControls = new ControlFinder()
				.findControls(new WidgetOfType<StyledText>(StyledText.class));
		if (findControls.size() < index) {
			throw new WidgetNotFoundException(
					"Could not find styled text with index " + index); //$NON-NLS-1$
		}

		return new SWTBotStyledText((StyledText) findControls.get(index));
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Asserts
	//
	// //////////////////////////////////////////////////////////////////////////

	protected void assertNotEnabled(AbstractSWTBot widget) {
		String errMessage = ErrorMessages.Common_errEnabled;
		errMessage = ErrorMessages.format(errMessage, widget.getText());
		SWTBotTestCase.assertFalse(errMessage, widget.isEnabled());
	}

	protected void assertSelected(SWTBotRadio radio) {
		String errMessage = ErrorMessages.Common_errNotChecked;
		errMessage = ErrorMessages.format(errMessage, radio.getText());
		SWTBotTestCase.assertTrue(errMessage, radio.isSelected());
	}

	protected void assertNotSelected(SWTBotRadio radio) {
		String errMessage = ErrorMessages.Common_errNotChecked;
		errMessage = ErrorMessages.format(errMessage, radio.getText());
		SWTBotTestCase.assertFalse(errMessage, radio.isSelected());
	}

	protected void assertChecked(SWTBotCheckBox detectPackage) {
		String errMessage = ErrorMessages.Common_errNotChecked;
		errMessage = ErrorMessages.format(errMessage, detectPackage.getText());
		SWTBotTestCase.assertTrue(errMessage, detectPackage.isChecked());
	}

	protected void assertNotChecked(SWTBotCheckBox detectPackage) {
		String errMessage = ErrorMessages.Common_errChecked;
		errMessage = ErrorMessages.format(errMessage, detectPackage.getText());
		SWTBotTestCase.assertFalse(errMessage, detectPackage.isChecked());
	}

	public void assertProjectElementExist(String projectName,
			String pathToElement) {
		try {
			navigateToProjectElement(projectName, pathToElement);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	public void assertProjectElementNotExist(String projectName, String path) {
		try {
			navigateToProjectElement(projectName, path);
			SWTBotTestCase.fail("Unexpected element in a tree: " + path); //$NON-NLS-1$
		} catch (WidgetNotFoundException ex) {
		} catch (TimeoutException ex) {
		}
	}

	public void assertProjectElementNotExist(String projectName, String path,
			String name) {
		SWTBotTreeItem bot = null;
		try {
			bot = navigateToProjectElement(projectName, path);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}

		bot.expand();

		String errorMessage = "Unexpected element in a tree. Path: " + path //$NON-NLS-1$
				+ ", element: " + name; //$NON-NLS-1$
		SWTBotTestCase.assertNotNull(errorMessage, bot.getNode(name));
	}

	protected void clickContextSubMenu(SWTBotTreeItem treeItem,
			final String menuName, final String subMenuName) {
		treeItem.contextMenu(menuName).setFocus();
		clickContextSubMenu(menuName, subMenuName);
	}

	protected void clickContextSubMenu(SWTBotTree tree, final String menuName,
			final String subMenuName) {
		tree.contextMenu(menuName).click();
		clickContextSubMenu(menuName, subMenuName);
	}

	private void clickContextSubMenu(final String menuName,
			final String subMenuName) {
		MenuItem menuItem = UIThreadRunnable.syncExec(Display.getDefault(),
				new WidgetResult<MenuItem>() {
					public MenuItem run() {
						List<MenuItem> menus = new MenuFinder()
								.findMenus(WidgetMatcherFactory
										.withMnemonic(subMenuName));
						for (MenuItem item : menus) {
							try {
								String name = item.getParent().getParentItem()
										.getText().replace("&", "");
								if (name.startsWith(menuName)) {
									return item;
								}
							} catch (Exception e) {
								// do nothing
							}
						}
						return null;
					}
				});
		if (null == menuItem) {
			throw new WidgetNotFoundException(
					"Can not found widget with name: " + subMenuName);
		}
		new SWTBotMenu(menuItem).click();
	}

	// protected void printShell() {
	// try {
	// SWTBotShell[] shells = getBot().shells();
	// int shelCount = shells.length;
	// System.out.println("shelCount: " + shelCount);
	// for (int index = 0; index < shelCount; index++) {
	// System.out.println("shell: " + shells[index].getText()
	// + ", op: " + shells[index].isOpen() + ", vis: "
	// + shells[index].isVisible() + ", en: "
	// + shells[index].isEnabled());
	// }
	// System.out.println("Active: " + getBot().activeShell().getText());
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
}
