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

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ui.tests.swtbot.DltkTestsHelper;
import org.eclipse.dltk.ui.tests.swtbot.ErrorMessages;
import org.eclipse.dltk.ui.tests.swtbot.operations.ProjectOperations;

public class ProjectTests extends SWTBotEclipseTestCase {

	private static boolean DEBUG;

	static {
		String value = Platform
				.getDebugOption("org.eclipse.dltk.tcl.ui.tests.swtbot/Project");
		DEBUG = Boolean.valueOf(value).booleanValue();
	}

	private static final String PROJECT_NAME = DltkTestsHelper.ETALON_PROJECT;

	private DltkTestsHelper helper;

	public ProjectTests(String name) {
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
		DltkTestsHelper.debug(getName() + " started");
	}

	public void tearDown() throws Exception {
		DltkTestsHelper.debug(getName() + " finishing...");
		helper.closeDialogs();

		helper.deleteProject(PROJECT_NAME);
		helper.clearInterpreters();
		helper.tearDownSuite();

		super.tearDown();
		DltkTestsHelper.debug(getName() + " finished");
	}

	/**
	 * Create a Tcl project in workspace using main menu and switch to Tcl
	 * perspective. (DLTK-502)<br>
	 * <br>
	 * Predefined conditions:<br>
	 * 1. Current perspective - not Tcl<br>
	 * <br>
	 * Actions:<br>
	 * 1. Open "New Project" wizard using Project File->New->Project...<br>
	 * 2. Select "Tcl/Tcl Project" and press "Next" button<br>
	 * 3. Enter project name and press "Finish" button<br>
	 * Expect "Open Associated Perspective?" dialog<br>
	 * 4. Press "OK"<br>
	 * <br>
	 * Expect:<br>
	 * 1. Created Tcl project<br>
	 * 2. Tcl perspective opened<br>
	 * 
	 */
	public void testCreateTclProject001() {
		helper.openJavaPerspective();
		new ProjectOperations(bot).createPromptPerspective(PROJECT_NAME);
		helper.assertTclProject(PROJECT_NAME);
		helper.assertPerspective(DltkTestsHelper.TCL_PERSPECTIVE_ID);
	}

	/**
	 * Create a Tcl project in workspace with default interpreter with use
	 * context menu of Script Explorer view. (DLTK-746)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. System contain Default Tcl interpreter<br>
	 * 2. Tcl perspective is opened<br>
	 * <br>
	 * Actions:<br>
	 * 1. Open "Create Tcl project" dizlog using context mebu New->Tcl Project
	 * in ScriptExplorer<br>
	 * 2. Enter project name and press "Finish" button<br>
	 * 3. Press "OK"<br>
	 * <br>
	 * Expect:<br>
	 * 1. Created Tcl project<br>
	 * 2. Project have default Interpreter Library<br>
	 * 
	 */
	public void testCreateTclProject002() {
		helper.openTclPerspective();
		helper.initOnlyDefInterpreter();

		ProjectOperations operation = new ProjectOperations(bot);
		operation.createFromScriptExplorer(PROJECT_NAME);
		helper.assertTclProject(PROJECT_NAME);
		helper.assertInterpreter(PROJECT_NAME,
				DltkTestsHelper.DEF_INTERPRETER_ID);
	}

	/**
	 * Create a Tcl project from existing source with detect option switched on.
	 * (DLTK-503)
	 * 
	 */
	public void _testCreateTclProject003_FAILED() {
		String externalProjectName = helper.initExternalProject();
		helper.openTclPerspective();

		new ProjectOperations(bot).createFromSource(PROJECT_NAME,
				externalProjectName, true);

		// TODO: add implementation for compare of model structure (with detect
		// packages)
		helper.assertTclProject(PROJECT_NAME);
	}

	/**
	 * Create a Tcl project from existing source with detect option switched
	 * off. (DLTK-504)
	 * 
	 */
	public void _testCreateTclProject004() {
		String externalProjectName = helper.initExternalProject();
		helper.openTclPerspective();

		new ProjectOperations(bot).createFromSource(PROJECT_NAME,
				externalProjectName, false);

		// TODO: add implementation for compare of model structure (without
		// detect packages)
		helper.assertTclProject(PROJECT_NAME);
	}

	/**
	 * Create a Tcl project using a project specific interpreter. (DLTK-505)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. System contain two interpreters<br>
	 * 2. Tcl perspective is opened<br>
	 * <br>
	 * Actions:<br>
	 * 1. Open "Create Tcl project" dialog<br>
	 * 2. Select "Use a project specific interpreter" radio and select
	 * alternative interpreter in combo<br>
	 * 3. Enter project name and press "Finish" button<br>
	 * 4. Press "OK"<br>
	 * <br>
	 * Expect:<br>
	 * 1. Created Tcl project<br>
	 * 2. Project have alternative Interpreter Library<br>
	 * 
	 */
	public void testCreateTclProject005() {
		helper.openTclPerspective();

		helper.initDefAndAltInterpreters();

		new ProjectOperations(bot).createWithAltInterpr(PROJECT_NAME);

		helper.assertTclProject(PROJECT_NAME);
		helper.assertInterpreter(PROJECT_NAME,
				DltkTestsHelper.ALT_INTERPRETER_ID);
	}

	/**
	 * Create a Tcl project with default undefined interpreter. (DLTK-506)
	 * 
	 */
	public void testCreateTclProject006() {
		helper.assertInterpretersEmpty();
		helper.openTclPerspective();

		new ProjectOperations(bot).create(PROJECT_NAME);

		helper.assertTclProject(PROJECT_NAME);
		IScriptProject tclProject = DLTKCore.create(helper
				.getProject(PROJECT_NAME));

		try {
			IInterpreterInstall interpreter = ScriptRuntime
					.getInterpreterInstall(tclProject);
			DltkTestsHelper.assertNull(interpreter);
		} catch (CoreException cex) {
			throw new RuntimeException(cex);
		}
	}

	/**
	 * Close a Tcl project. (DLTK-507)
	 * 
	 */
	public void testCloseTclProject() {
		initProject(true);

		new ProjectOperations(bot).closeProject(PROJECT_NAME);

		IProject project = helper.getProject(PROJECT_NAME);
		assertTrue(ErrorMessages.Project_errClose, !project.isOpen());
	}

	/**
	 * Open a Tcl project. (DLTK-508)
	 * 
	 */
	public void testOpenTclProject() {
		initProject(false);

		new ProjectOperations(bot).openProject(PROJECT_NAME);

		IProject project = helper.getProject(PROJECT_NAME);
		assertTrue(ErrorMessages.Project_errOpen, project.isOpen());
	}

	/**
	 * Delete Tcl project. (DLTK-509)
	 * 
	 */
	public void testDeleteProject() {
		initProject(true);
		helper.openTclPerspective();
		new ProjectOperations(bot).deleteProject(PROJECT_NAME);
		helper.assertProjectDoesNotExist(PROJECT_NAME);
	}

	/**
	 * Delete a Tcl project without project content deletion. (DLTK-626)
	 */
	public void testDeleteProjectByContextMenu001() {
		IScriptProject project = initProject(true);
		IPath path = project.getProject().getLocation();
		helper.openTclPerspective();
		new ProjectOperations(bot).deleteProjectByContextMenu(PROJECT_NAME,
				false);
		helper.assertProjectDoesNotExist(PROJECT_NAME);
		helper.assertProjectContentExist(path);
	}

	/**
	 * Delete a Tcl project with project content. (DLTK-627)
	 */
	public void testDeleteProjectByContextMenu002() {
		IScriptProject project = initProject(true);
		IPath path = project.getProject().getLocation();
		helper.openTclPerspective();
		new ProjectOperations(bot).deleteProjectByContextMenu(PROJECT_NAME,
				true);
		helper.assertProjectDoesNotExist(PROJECT_NAME);
		helper.assertProjectContentDoesNotExist(path);
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Initialize methods
	//
	// /////////////////////////////////////////////////////////////////////////
	protected IScriptProject initProject(boolean open) {
		IScriptProject result = null;
		try {
			result = helper.setUpScriptProject(PROJECT_NAME);
			assertTrue(ErrorMessages.Project_errNotFound, result != null);

			IProject project = result.getProject();
			assertTrue(ErrorMessages.Project_errNotFound, project != null
					&& project.exists());

			if (!open) {
				project.close(null);
				assertTrue(ErrorMessages.Project_errClose,
						project.isOpen() == open);
			} else {
				assertTrue(ErrorMessages.Project_errOpen,
						project.isOpen() == open);
			}

			return result;
		} catch (IOException ex) {
			throw new RuntimeException(ErrorMessages.Common_initError, ex);
		} catch (CoreException ex) {
			throw new RuntimeException(ErrorMessages.Common_initError, ex);
		}
	}

}
