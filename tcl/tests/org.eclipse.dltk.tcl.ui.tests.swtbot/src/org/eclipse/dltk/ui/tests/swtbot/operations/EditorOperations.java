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

import org.eclipse.dltk.ui.tests.swtbot.complex.EditorTests;
import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.SWTEclipseBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.Position;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

public class EditorOperations extends Operations {

	private SWTBotEclipseEditor editorBot;

	public EditorOperations(SWTEclipseBot bot, String projectName, String path,
			final String scriptName) throws WidgetNotFoundException,
			TimeoutException {
		super(bot);
		SWTBotTreeItem scriptBot = navigateToProjectElement(projectName, path
				+ "/" + scriptName);
		scriptBot.contextMenu(MENU_OPEN).click();
		editorBot = getBot().editor(scriptName);
	}

	public EditorOperations(SWTEclipseBot bot, SWTBotEclipseEditor editorBot) {
		super(bot);
		this.editorBot = editorBot;
	}

	/**
	 * @see EditorTests#testGotoDeclaration001()
	 * @see EditorTests#testGotoDeclaration002()
	 * @see EditorTests#testGotoDeclaration003()
	 * @see EditorTests#testGotoDeclaration004()
	 * @see EditorTests#testGotoDeclaration005()
	 * @see EditorTests#testGotoDeclaration006()
	 * @see EditorTests#testGotoDeclaration007()
	 * @see EditorTests#testGotoDeclaration008()
	 * @see EditorTests#testGotoDeclaration009()
	 * @see EditorTests#testGotoDeclaration010()
	 * 
	 * @param pattern
	 * @param selection
	 * @param selLine
	 */
	public void gotoDeclarationByF3(String pattern, String selection,
			String selLine) {
		Position pos = locatePattern(pattern);
		setCursorPosition(pos.line, pos.column);
		sendKeyboardEvent(SWT.F3);

		assertSelectedEquals(selection);
		assertCurrentLineEquals(selLine);
	}

	/**
	 * @see EditorTests#testGotoDeclarationByContextMenu001()
	 * @see EditorTests#testGotoDeclarationByContextMenu002()
	 * @see EditorTests#testGotoDeclarationByContextMenu003()
	 * @see EditorTests#testGotoDeclarationByContextMenu004()
	 * @see EditorTests#testGotoDeclarationByContextMenu005()
	 * @see EditorTests#testGotoDeclarationByContextMenu006()
	 * @see EditorTests#testGotoDeclarationByContextMenu007()
	 * @see EditorTests#testGotoDeclarationByContextMenu008()
	 * @see EditorTests#testGotoDeclarationByContextMenu009()
	 * @see EditorTests#testGotoDeclarationByContextMenu010()
	 * 
	 * @param pattern
	 * @param selection
	 * @param selLine
	 */
	public void gotoDeclarationByContextMenu(String pattern, String selection,
			String selLine) {
		try {
			Position pos = locatePattern(pattern);
			setCursorPosition(pos.line, pos.column);
			editorBot.contextMenu(Operations.MENU_OPEN_DECLARATION).click();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}

		assertSelectedEquals(selection);
		assertCurrentLineEquals(selLine);
	}

	/**
	 * @see EditorTests#testGotoDeclarationByMainMenu001()
	 * @see EditorTests#testGotoDeclarationByMainMenu002()
	 * @see EditorTests#testGotoDeclarationByMainMenu003()
	 * @see EditorTests#testGotoDeclarationByMainMenu004()
	 * @see EditorTests#testGotoDeclarationByMainMenu005()
	 * @see EditorTests#testGotoDeclarationByMainMenu006()
	 * @see EditorTests#testGotoDeclarationByMainMenu007()
	 * @see EditorTests#testGotoDeclarationByMainMenu008()
	 * @see EditorTests#testGotoDeclarationByMainMenu009()
	 * @see EditorTests#testGotoDeclarationByMainMenu010()
	 * 
	 * @param pattern
	 * @param selection
	 * @param selLine
	 */
	public void gotoDeclarationByMainMenu(String pattern, String selection,
			String selLine) {
		try {
			Position pos = locatePattern(pattern);
			setCursorPosition(pos.line, pos.column);
			getBot().menu(MENU_NAVIGATE).menu(MENU_OPEN_DECLARATION).click();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}

		assertSelectedEquals(selection);
		assertCurrentLineEquals(selLine);
	}

	// ///////////////////////////////////////////////////////////////
	//
	// Internal
	//
	// ///////////////////////////////////////////////////////////////
	protected void enterText(String text) {
		editorBot.setText(text);
	}

	protected void setCursorPosition(int line, int column) {
		editorBot.navigateTo(line, column);
	}

	protected Position locatePattern(String pattern) {
		for (int lineIndex = 0;; lineIndex++) {
			String line = editorBot.getTextOnLine(lineIndex);
			int col = line.indexOf(pattern);
			if (col != -1) {
				return new Position(lineIndex, col);
			}
		}
	}

	protected void sendKeyboardEvent(int modificationKey, int keyCode) {
		editorBot.notifyKeyboardEvent(modificationKey, (char) 0, keyCode);
	}

	protected void sendKeyboardEvent(int keyCode) {
		sendKeyboardEvent(0, keyCode);
	}

	protected void sendKeyboardEvent(int modificationKey, char character) {
		editorBot.notifyKeyboardEvent(modificationKey, character);
	}

	protected String getSelection() {
		return editorBot.getSelection();
	}

	protected String getTextOnCurrentLine() {
		return editorBot.getTextOnCurrentLine();
	}

	// ///////////////////////////////////////////////////////////////
	//
	// Asserts
	//
	// ///////////////////////////////////////////////////////////////
	public void assertSelectedEquals(final String expected) {
		final String errorMessage = "Expected " + expected;
		try {
			getBot().waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					String selection = getSelection();
					return expected.equals(selection);
				}
			});
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	public void assertCurrentLineEquals(final String expected) {
		final String errorMessage = "Expected " + expected;
		try {
			getBot().waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					String line = getTextOnCurrentLine();
					return line != null && expected.equals(line.trim());
				}
			});
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}
}
