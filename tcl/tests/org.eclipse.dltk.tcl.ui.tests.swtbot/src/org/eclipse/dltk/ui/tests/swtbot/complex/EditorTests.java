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
package org.eclipse.dltk.ui.tests.swtbot.complex;

import net.sf.swtbot.widgets.TimeoutException;
import net.sf.swtbot.widgets.WidgetNotFoundException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.ui.tests.swtbot.DltkTestsHelper;
import org.eclipse.dltk.ui.tests.swtbot.operations.EditorOperations;

public class EditorTests extends SWTBotEclipseTestCase {

	private static boolean DEBUG;

	static {
		String value = Platform
				.getDebugOption("org.eclipse.dltk.tcl.ui.tests.swtbot/Editor");
		DEBUG = Boolean.valueOf(value).booleanValue();
	}

	private static final String PROJECT_NAME = "EditorProject";
	private static final String SCRIPT_PATH = "SourceFolder";
	private static final String SCRIPT_NAME = "Script1.tcl";

	private static final String NAMESPACE_1 = "ns1";
	private static final String NAMESPACE_2 = "ns2";

	private static final String VAR_GLOBAL = "globalVar";
	private static final String VAR_NAMESPACE = "nsVar";
	private static final String VAR_LOCAL = "localVar";
	private static final String VAR_PARAM = "param1";
	private static final String VAR_UP = "upVar";
	private static final String VAR_UP_PARAM = "param2";

	private static final String PROC_1 = "proc1";
	private static final String PROC_2 = "proc2";
	private static final String PROC_3 = "proc3";
	private static final String PROC_4 = "proc4";
	private static final String PROC_GLOBAL = "globalProc";

	private static final String FULL_PROC_2 = NAMESPACE_1 + "::" + PROC_2;
	private static final String FULL_PROC_3 = PROC_3;
	private static final String FULL_PROC_4 = PROC_4;
	private static final String FULL_PROC_GLOBAL = PROC_GLOBAL;

	private static final String CALL_PROC_2 = NAMESPACE_1 + "::" + PROC_2;
	private static final String CALL_PROC_3 = NAMESPACE_1 + "::" + PROC_3;
	private static final String CALL_PROC_4 = PROC_4;
	private static final String CALL_PROC_GLOBAL = PROC_GLOBAL;

	private static final String PROC_1_DECLARE = "proc " + PROC_1
			+ " { param1 param2 } {";

	private DltkTestsHelper helper;

	public EditorTests(String name) {
		super();
		setName(name);
		helper = new DltkTestsHelper(name, bot);
		DltkTestsHelper.DEBUG = DEBUG;
	}

	public void setUp() throws Exception {
		DltkTestsHelper.debug(getName() + " starting...");
		super.setUp();
		helper.setUpSuite();
		// Close welcome page.
		closeWelcome();
		helper.openTclPerspective();
		helper.setUpScriptProject(PROJECT_NAME);
		DltkTestsHelper.debug(getName() + " started");
	}

	public void tearDown() throws Exception {
		DltkTestsHelper.debug(getName() + " finishing...");
		helper.closeDialogs();
		helper.closeEditors();
		helper.deleteProject(PROJECT_NAME);
		helper.tearDownSuite();

		super.tearDown();
		DltkTestsHelper.debug(getName() + " finished");
	}

	private String getDeclareProc(String fullName) {
		return "proc " + fullName + " { } {";
	}

	private String getDeclareVar(String name) {
		return "set " + name + " 0";
	}

	private String getCallVar(String name) {
		return "$" + name;
	}

	protected EditorOperations createOperations() {
		try {
			return new EditorOperations(bot, PROJECT_NAME, SCRIPT_PATH,
					SCRIPT_NAME);
		} catch (TimeoutException ex) {
			throw new RuntimeException(ex);
		} catch (WidgetNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	// ///////////////////////////////////////////////////////////////
	//
	// Navigate to a Tcl element's declaration using F3 key button
	//
	// ///////////////////////////////////////////////////////////////
	/**
	 * Navigate to a Tcl element (procedure in global namespace) declaration
	 * using F3 key button (DLTK-580).
	 * 
	 */
	public void testGotoDeclaration001() {
		String declare = getDeclareProc(FULL_PROC_GLOBAL);
		createOperations().gotoDeclarationByF3(CALL_PROC_GLOBAL,
				FULL_PROC_GLOBAL, declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in other namespace) declaration
	 * using F3 key button (DLTK-719).
	 * 
	 */
	public void testGotoDeclaration002() {
		String declare = getDeclareProc(FULL_PROC_2);
		createOperations().gotoDeclarationByF3(CALL_PROC_2, FULL_PROC_2,
				declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in other namespace) declaration
	 * using F3 key button (DLTK-719).
	 * 
	 */
	public void testGotoDeclaration003() {
		String declare = getDeclareProc(FULL_PROC_3);
		createOperations().gotoDeclarationByF3(CALL_PROC_3, FULL_PROC_3,
				declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in current namespace) declaration
	 * using F3 key button (DLTK-720).
	 * 
	 */
	public void testGotoDeclaration004() {
		String declare = getDeclareProc(FULL_PROC_4);
		createOperations().gotoDeclarationByF3(CALL_PROC_4, FULL_PROC_4,
				declare);
	}

	/**
	 * Navigate to a Tcl element (variable in global namespace) declaration
	 * using F3 key button (DLTK-721).
	 * 
	 */
	public void testGotoDeclaration005() {
		String declare = getDeclareVar(VAR_GLOBAL);
		String call = getCallVar(VAR_GLOBAL);
		createOperations().gotoDeclarationByF3(call, VAR_GLOBAL, declare);
	}

	/**
	 * Navigate to a Tcl element (variable in current namespace) declaration
	 * using F3 key button (DLTK-722).
	 * 
	 */
	public void testGotoDeclaration006() {
		String declare = getDeclareVar(VAR_NAMESPACE);
		String call = getCallVar(VAR_NAMESPACE);
		createOperations().gotoDeclarationByF3(call, VAR_NAMESPACE, declare);
	}

	/**
	 * Navigate to a Tcl element (local variable in procedure) declaration using
	 * F3 key button (DLTK-723).
	 * 
	 */
	public void testGotoDeclaration007() {
		String declare = getDeclareVar(VAR_LOCAL);
		String call = getCallVar(VAR_LOCAL);
		createOperations().gotoDeclarationByF3(call, VAR_LOCAL, declare);
	}

	/**
	 * [Test] Navigate to a Tcl element (variable - parameter of procedure)
	 * declaration using F3 key button (DLTK-724). <br>
	 * <br>
	 * proc proc1 { param1 param2 } {<br>
	 * puts $param1<br>
	 * }<br>
	 * 
	 */
	/*
	 * public void testGotoDeclaration008() { String call =
	 * getCallVar(VAR_PARAM); createOperations().gotoDeclarationByF3(call,
	 * VAR_PARAM, PROC_1_DECLARE); }
	 */

	/**
	 * [Test] Navigate to a Tcl element (upvar variable) declaration using F3
	 * key button (DLTK-725).
	 * 
	 */
	public void testGotoDeclaration009() {
		String call = getCallVar(VAR_UP);
		createOperations().gotoDeclarationByF3(call, VAR_UP,
				"upvar $" + VAR_UP_PARAM + " " + VAR_UP);
	}

	/**
	 * [Test] Navigate to a Tcl element (variable - parameter of procedure)
	 * declaration using F3 key button (DLTK-724). <br>
	 * <br>
	 * proc proc1 { param1 param2 } {<br>
	 * upvar $param2 upVar<br>
	 * }<br>
	 * 
	 */
	/*
	 * public void testGotoDeclaration010() { String call =
	 * getCallVar(VAR_UP_PARAM); createOperations().gotoDeclarationByF3(call,
	 * VAR_UP_PARAM, PROC_1_DECLARE); }
	 */

	// ///////////////////////////////////////////////////////////////
	//
	// Navigate to a Tcl element's declaration using context menu
	//
	// ///////////////////////////////////////////////////////////////
	/**
	 * Navigate to a Tcl element (procedure in global namespace) declaration
	 * using context menu. (DLTK-579).
	 * 
	 */
	public void testGotoDeclarationByContextMenu001() {
		String declare = getDeclareProc(FULL_PROC_GLOBAL);
		createOperations().gotoDeclarationByContextMenu(CALL_PROC_GLOBAL,
				FULL_PROC_GLOBAL, declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in other namespace) declaration
	 * using context menu. (DLTK-727).
	 * 
	 */
	public void testGotoDeclarationByContextMenuByContextMenu002() {
		String declare = getDeclareProc(FULL_PROC_2);
		createOperations().gotoDeclarationByContextMenu(CALL_PROC_2,
				FULL_PROC_2, declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in other namespace) declaration
	 * (DLTK-727).
	 * 
	 */
	public void testGotoDeclarationByContextMenu003() {
		String declare = getDeclareProc(FULL_PROC_3);
		createOperations().gotoDeclarationByContextMenu(CALL_PROC_3,
				FULL_PROC_3, declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in current namespace) declaration
	 * (DLTK-729).
	 * 
	 */
	public void testGotoDeclarationByContextMenu004() {
		String declare = getDeclareProc(FULL_PROC_4);
		createOperations().gotoDeclarationByContextMenu(CALL_PROC_4,
				FULL_PROC_4, declare);
	}

	/**
	 * Navigate to a Tcl element (variable in global namespace) declaration
	 * (DLTK-731).
	 * 
	 */
	public void testGotoDeclarationByContextMenu005() {
		String declare = getDeclareVar(VAR_GLOBAL);
		String call = getCallVar(VAR_GLOBAL);
		createOperations().gotoDeclarationByContextMenu(call, VAR_GLOBAL,
				declare);
	}

	/**
	 * Navigate to a Tcl element (variable in current namespace) declaration
	 * (DLTK-733).
	 * 
	 */
	public void testGotoDeclarationByContextMenu006() {
		String declare = getDeclareVar(VAR_NAMESPACE);
		String call = getCallVar(VAR_NAMESPACE);
		createOperations().gotoDeclarationByContextMenu(call, VAR_NAMESPACE,
				declare);
	}

	/**
	 * Navigate to a Tcl element (local variable in procedure) declaration using
	 * F3 key button (DLTK-735).
	 * 
	 */
	public void testGotoDeclarationByContextMenu007() {
		String declare = getDeclareVar(VAR_LOCAL);
		String call = getCallVar(VAR_LOCAL);
		createOperations().gotoDeclarationByContextMenu(call, VAR_LOCAL,
				declare);
	}

	/**
	 * [Test] Navigate to a Tcl element (variable - parameter of procedure)
	 * declaration (DLTK-737). <br>
	 * <br>
	 * proc proc1 { param1 param2 } {<br>
	 * puts $param1<br>
	 * }<br>
	 * 
	 */
	/*
	 * public void testGotoDeclarationByContextMenu008() { String call =
	 * getCallVar(VAR_PARAM);
	 * createOperations().gotoDeclarationByContextMenu(call, VAR_PARAM,
	 * PROC_1_DECLARE); }
	 */

	/**
	 * [Test] Navigate to a Tcl element (upvar variable) declaration using F3
	 * key button (DLTK-739).
	 * 
	 */
	public void testGotoDeclarationByContextMenu009() {
		String call = getCallVar(VAR_UP);
		createOperations().gotoDeclarationByContextMenu(call, VAR_UP,
				"upvar $" + VAR_UP_PARAM + " " + VAR_UP);
	}

	/**
	 * [Test] Navigate to a Tcl element (variable - parameter of procedure)
	 * declaration (DLTK-737). <br>
	 * <br>
	 * proc proc1 { param1 param2 } {<br>
	 * upvar $param2 upVar<br>
	 * }<br>
	 * 
	 */
	/*
	 * public void testGotoDeclarationByContextMenu010() { String call =
	 * getCallVar(VAR_UP_PARAM);
	 * createOperations().gotoDeclarationByContextMenu(call, VAR_UP_PARAM,
	 * PROC_1_DECLARE); }
	 */

	// ///////////////////////////////////////////////////////////////
	//
	// Navigate to a Tcl element's declaration using main menu
	//
	// ///////////////////////////////////////////////////////////////
	/**
	 * Navigate to a Tcl element (procedure in global namespace) declaration
	 * using "Navigate" menu (DLTK-578).
	 * 
	 */
	public void testGotoDeclarationByMainMenu001() {
		String declare = getDeclareProc(FULL_PROC_GLOBAL);
		createOperations().gotoDeclarationByMainMenu(CALL_PROC_GLOBAL,
				FULL_PROC_GLOBAL, declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in other namespace) declaration
	 * using main menu. (DLTK-728).
	 * 
	 */
	public void testGotoDeclarationByMainMenu002() {
		String declare = getDeclareProc(FULL_PROC_2);
		createOperations().gotoDeclarationByMainMenu(CALL_PROC_2, FULL_PROC_2,
				declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in other namespace) declaration
	 * (DLTK-728).
	 * 
	 */
	public void testGotoDeclarationByMainMenu003() {
		String declare = getDeclareProc(FULL_PROC_3);
		createOperations().gotoDeclarationByMainMenu(CALL_PROC_3, FULL_PROC_3,
				declare);
	}

	/**
	 * Navigate to a Tcl element (procedure in current namespace) declaration
	 * (DLTK-730).
	 * 
	 */
	public void testGotoDeclarationByMainMenu004() {
		String declare = getDeclareProc(FULL_PROC_4);
		createOperations().gotoDeclarationByMainMenu(CALL_PROC_4, FULL_PROC_4,
				declare);
	}

	/**
	 * Navigate to a Tcl element (variable in global namespace) declaration
	 * (DLTK-732).
	 * 
	 */
	public void testGotoDeclarationByMainMenu005() {
		String declare = getDeclareVar(VAR_GLOBAL);
		String call = getCallVar(VAR_GLOBAL);
		createOperations().gotoDeclarationByMainMenu(call, VAR_GLOBAL, declare);
	}

	/**
	 * Navigate to a Tcl element (variable in current namespace) declaration
	 * (DLTK-734).
	 * 
	 */
	public void testGotoDeclarationByMainMenu006() {
		String declare = getDeclareVar(VAR_NAMESPACE);
		String call = getCallVar(VAR_NAMESPACE);
		createOperations().gotoDeclarationByMainMenu(call, VAR_NAMESPACE,
				declare);
	}

	/**
	 * Navigate to a Tcl element (local variable in procedure) declaration using
	 * F3 key button (DLTK-736).
	 * 
	 */
	public void testGotoDeclarationByMainMenu007() {
		String declare = getDeclareVar(VAR_LOCAL);
		String call = getCallVar(VAR_LOCAL);
		createOperations().gotoDeclarationByMainMenu(call, VAR_LOCAL, declare);
	}

	/**
	 * [Test] Navigate to a Tcl element (variable - parameter of procedure)
	 * declaration (DLTK-738). <br>
	 * <br>
	 * proc proc1 { param1 param2 } {<br>
	 * puts $param1<br>
	 * }<br>
	 * 
	 */
	/*
	 * public void testGotoDeclarationByMainMenu008() { String call =
	 * getCallVar(VAR_PARAM); createOperations().gotoDeclarationByMainMenu(call,
	 * VAR_PARAM, PROC_1_DECLARE); }
	 */

	/**
	 * [Test] Navigate to a Tcl element (upvar variable) declaration using F3
	 * key button (DLTK-740).
	 * 
	 */
	public void testGotoDeclarationByMainMenu009() {
		String call = getCallVar(VAR_UP);
		createOperations().gotoDeclarationByMainMenu(call, VAR_UP,
				"upvar $" + VAR_UP_PARAM + " " + VAR_UP);
	}

	/**
	 * [Test] Navigate to a Tcl element (variable - parameter of procedure)
	 * declaration (DLTK-738). <br>
	 * <br>
	 * proc proc1 { param1 param2 } {<br>
	 * upvar $param2 upVar<br>
	 * }<br>
	 * 
	 */
	/*
	 * public void testGotoDeclarationByMainMenu010() { String call =
	 * getCallVar(VAR_UP_PARAM);
	 * createOperations().gotoDeclarationByMainMenu(call, VAR_UP_PARAM,
	 * PROC_1_DECLARE); }
	 */

	//
	// /**
	// * Open call hierarchy by main menu (DLTK-581)
	// *
	// * @throws Exception
	// */
	// public void testOpenCallHierarchyMainMenu() throws Exception {
	//
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa", 1));
	//
	// bot.menu("Navigate").menu("Open Call Hierarchy").click();
	// SWTBotView view = bot.view("Call Hierarchy");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Call hierarchy tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "aaa()/bbb()");
	// view.close();
	//
	// }
	//
	// /**
	// * Open call hierarchy by popup menu (DLTK-582)
	// *
	// * @throws Exception
	// */
	// public void testOpenCallHierarchyPopupMenu() throws Exception {
	//
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa", 1));
	//
	// editor.contextMenu("Open Call Hierarchy").click();
	// SWTBotView view = bot.view("Call Hierarchy");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Call hierarchy tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "aaa()/bbb()");
	// view.close();
	//
	// }
	//
	// /**
	// * Open callee hierarchy by popup menu (DLTK-584)
	// *
	// * @throws Exception
	// */
	// public void testOpenCalleeHierarchyPopupMenu() throws Exception {
	//
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE2);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE2);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "bbb"));
	//
	// editor.contextMenu("Open Call Hierarchy").click();
	// SWTBotView view = bot.view("Call Hierarchy");
	// bot.toolbarButtonWithTooltip("Show Callee Hierarchy").click();
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Call hierarchy tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "bbb()/aaa()");
	// view.close();
	//
	// }
	//
	// /**
	// * Open callee hierarchy by main menu (DLTK-583)
	// *
	// * @throws Exception
	// */
	// public void testOpenCalleeHierarchyMainMenu() throws Exception {
	//
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE2);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE2);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "bbb"));
	//
	// bot.menu("Navigate").menu("Open Call Hierarchy").click();
	// SWTBotView view = bot.view("Call Hierarchy");
	//
	// bot.toolbarButtonWithTooltip("Show Callee Hierarchy").click();
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Call hierarchy tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "bbb()/aaa()");
	// view.close();
	//
	// }
	//
	// /**
	// * Find declarations in workspace (DLTK-586)
	// *
	// * @throws Exception
	// */
	// public void testFindDeclarationsWorkspace() throws Exception {
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa", 1));
	//
	// editor.contextMenu("Search").menu("Declarations").menu("Workspace")
	// .click();
	// // Thread.sleep(10000);
	// SWTBotView view = bot.view("Search");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Declarations tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE1 + "/aaa()");
	//
	// }
	//
	// /**
	// * Find declarations in project (DLTK-587)
	// *
	// * @throws Exception
	// */
	// public void testFindDeclarationsProject() throws Exception {
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa", 1));
	//
	// editor.contextMenu("Search").menu("Declarations").menu("Project")
	// .click();
	// // Thread.sleep(10000);
	// SWTBotView view = bot.view("Search");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Declarations tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE1 + "/aaa()");
	//
	// }
	//
	// /**
	// * Find declarations in hierarchy (DLTK-585)
	// *
	// * @throws Exception
	// */
	// public void testFindDeclarationsHierarchy() throws Exception {
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa", 1));
	//
	// editor.contextMenu("Search").menu("Declarations").menu("Hierarchy")
	// .click();
	// // Thread.sleep(10000);
	// SWTBotView view = bot.view("Search");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Declarations tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE1 + "/aaa()");
	//
	// }
	//
	// /**
	// * Find references in hierarchy (DLTK-590)
	// *
	// * @throws Exception
	// */
	// public void testFindReferencesHierarchy() throws Exception {
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa"));
	//
	// editor.contextMenu("Search").menu("References").menu("Hierarchy")
	// .click();
	// // Thread.sleep(10000);
	// SWTBotView view = bot.view("Search");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Declarations tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE1);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE2 + "/" + "bbb()");
	//
	// }
	//
	// /**
	// * Find references in project (DLTK-588)
	// *
	// * @throws Exception
	// */
	// public void testFindReferencesProject() throws Exception {
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa"));
	//
	// editor.contextMenu("Search").menu("References").menu("Project").click();
	// // Thread.sleep(10000);
	// SWTBotView view = bot.view("Search");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Declarations tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE1);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE2 + "/" + "bbb()");
	//
	// }
	//
	// /**
	// * Find references in workspace (DLTK-589)
	// *
	// * @throws Exception
	// */
	// public void testFindReferencesWorkspace() throws Exception {
	// openFileInEditor(BUILDPATH_PROJECT, "src/" + BUILDPATH_FILE1);
	//
	// SWTBotEclipseEditor editor = operations.getEditor(BUILDPATH_FILE1);
	// operations.setCursorPosition(editor, operations.locatePattern(editor,
	// "aaa"));
	//
	// editor.contextMenu("Search").menu("References").menu("Workspace")
	// .click();
	// // Thread.sleep(10000);
	// SWTBotView view = bot.view("Search");
	//
	// SWTBotTree tree = bot.tree();
	// assertNotNull("Declarations tree not found", tree);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE1);
	//
	// helper.assertTreeStructure(tree, "(default package) - src - "
	// + BUILDPATH_PROJECT + "/" + BUILDPATH_FILE2 + "/" + "bbb()");
	//
	// }
}
