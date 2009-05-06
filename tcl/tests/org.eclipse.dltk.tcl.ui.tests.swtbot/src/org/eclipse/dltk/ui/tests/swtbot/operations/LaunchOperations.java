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

import org.eclipse.dltk.ui.tests.swtbot.complex.LaunchTests;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTEclipseBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class LaunchOperations extends Operations {

	private static final String CONSOLE_RESULT = "Hello world !!!\n";

	public LaunchOperations(SWTEclipseBot bot) {
		super(bot);
	}

	/**
	 * @see LaunchTests#testRunByContextMenu()
	 * 
	 * @param projectName
	 * @param scriptName
	 */
	public void runByContextMenu(String projectName, String scriptName) {
		try {
			SWTBotTreeItem projectBot = navigateToProjectElement(projectName,
					scriptName);
			projectBot.contextMenu(MENU_RUN_AS).menu("1 Tcl Script").click();
			// clickContextSubMenu(projectBot, MENU_RUN_AS, "1 Tcl Script");
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * @see LaunchTests#testDebugByContextMenu()
	 * 
	 * @param projectName
	 * @param scriptName
	 */
	public void debugByContextMenu(String projectName, String scriptName) {
		try {
			SWTBotTreeItem projectBot = navigateToProjectElement(projectName,
					scriptName);
			projectBot.contextMenu(MENU_DEBUG_AS).menu("1 Tcl Script").click();
			// clickContextSubMenu(projectBot, MENU_DEBUG_AS, "1 Tcl Script");
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * @see LaunchTests#testCheckDebugPreferences()
	 * 
	 * @param projectName
	 */
	public void checkDebugPreferences(String projectName) {
		try {
			SWTBotShell preffBot = openPreferences();
			expandTree(PREFERENCES_TCL, "Debug");
			// not implemented
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	// /////////////////////////////////////////////////////////////////////
	//
	// Asserts
	//
	// /////////////////////////////////////////////////////////////////////
	/**
	 * @see LaunchTests#testRunByContextMenu()
	 * @see LaunchTests#testDebugByContextMenu()
	 */
	public void assertExecuteCompleteOk() {
		try {
			SWTBotStyledText consoleBot = getConsole();
			SWTBotTestCase.assertEquals(CONSOLE_RESULT, consoleBot.getText());
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	public void waitingDebugConsole() {
		try {
			// IViewPart debugView;
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().showView(
										"org.eclipse.debug.ui.DebugView");
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			});
			SWTBotView debugViewBot = getBot().view("Debug");
			debugViewBot.setFocus();
			getBot().waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return null;
				}

				public boolean test() throws Exception {
					try {
						SWTBotTreeItem[] items = getBot().tree().getAllItems();
						return items[0].getText().contains("<terminated>");
					} catch (Exception e) {
						return false;
					}
				}
			});
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	public void assertExecuteCompleteWithError() {
		try {
			SWTBotStyledText consoleBot = getConsole();
			SWTBotTestCase.assertFalse(CONSOLE_RESULT.equals(consoleBot
					.getText()));
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}
}
