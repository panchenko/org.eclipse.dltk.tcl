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
package org.eclipse.dltk.ui.tests.swtbot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.debug.ui.interpreters.InterpretersUpdater;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.launching.ScriptRuntime.DefaultInterpreterEntry;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.swtbot.eclipse.finder.SWTEclipseBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class DltkTestsHelper extends AbstractModelTests {

	public static int RESOURCE_REFRESH_TIMEOUT = 1000;
	public static int INTERPRETER_REFRESH_TIMEOUT = 1000;

	public static boolean DEBUG = false;

	/**
	 * Name of etalon project (See workspace folder).
	 * 
	 * @see #setUpProject(String)
	 */
	public static final String ETALON_PROJECT = "EtalonTclProject";
	/**
	 * Name of empty project with default interpreter (See workspace folder).
	 * 
	 * @see #setUpProject(String)
	 */
	public static final String EMPTY_PROJECT = "EmptyTclProject";

	/**
	 * Name of empty project without Interpreter Library (See workspace folder).
	 * 
	 * @see #setUpProject(String)
	 */
	public static final String EMPTY_PROJECT_WITHOUT_LIB = "EmptyTclProjectWithoutLib";

	/**
	 * Name of empty project with alternative interpreter (See workspace
	 * folder).
	 * 
	 * @see #setUpProject(String)
	 */
	public static final String EMPTY_PROJECT_WITH_ALT_INTERPR = "EmptyProjectWithAltInterpreter";

	// TODO: ???
	private static final String TEMP_DIR;

	public static final String DEF_INTERPRETER_ID = "DefaultTclInterpreter";
	public static final String DEF_INTERPRETER_PATH = TestMessages.DltkTestsHelper_Def_interpreter_path;

	public static final String ALT_INTERPRETER_ID = "AltTclInterpreter";
	public static final String ALT_INTERPRETER_PATH = TestMessages.DltkTestsHelper_Alt_interpreter_path;

	public static final String ALT_2_INTERPRETER_PATH = TestMessages.DltkTestsHelper_Atl_2_interpreter_path;

	public static final String TCL_PERSPECTIVE_ID = "org.eclipse.dltk.tcl.ui.TclPerspective";

	private SWTEclipseBot bot;

	static {
		String tmpDir = null;
		try {
			tmpDir = File.createTempFile("test", null).getParentFile()
					.getAbsolutePath();
		} catch (IOException ioEx) {
			tmpDir = TestMessages.DltkTestsHelper_temp_dir;
		}
		TEMP_DIR = tmpDir;
	}

	public DltkTestsHelper(String name, SWTEclipseBot bot) {
		super(Activator.PLUGIN_ID, name);
		this.bot = bot;
	}

	/**
	 * 
	 * @throws CoreException
	 */
	// public void waitForProjectToBuild() throws CoreException {
	// long startTime = 0;
	// if (DEBUG) {
	// startTime = System.currentTimeMillis();
	// debug("Start build project");
	// }
	// waitUntilIndexesReady();
	// ResourcesPlugin.getWorkspace()
	// .build(IncrementalProjectBuilder.FULL_BUILD,
	// new NullProgressMonitor());
	// waitForAutoBuild();
	// if (DEBUG) {
	// long time = System.currentTimeMillis() - startTime;
	// debug("Build project complete (" + time / 1000 + " sec)");
	// }
	// }
	/**
	 * 
	 */
	public void closeDialogs() {
		try {
			SWTBotShell[] shells = bot.shells();
			for (int index = shells.length - 1; index > 0; index--) {
				debug("Test finished but exist opened window. Try to close this window...");
				System.out.println("Try to close shell:"
						+ shells[index].getText());
				Thread.sleep(3000);
				if (shells[index].isOpen()) {
					shells[index].close();
				}
			}

			bot.waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return null;
				}

				public boolean test() throws Exception {
					return bot.shells().length == 1;
				}
			});

		} catch (Throwable thIgnore) {
			debug("Fatal error. Can't close windows");
			throw new RuntimeException(ErrorMessages.Common_internalFatalError,
					thIgnore);
		}
	}

	public void closeEditors() {
		try {
			for (SWTBotEclipseEditor editor : bot.editors()) {
				editor.close();
			}

			/*
			 * bot.waitUntil(new DefaultCondition() { public String
			 * getFailureMessage() { return null; }
			 * 
			 * public boolean test() throws Exception { return
			 * (DltkTestsHelper.this.bot.editors().size() == 0) &&
			 * (SWTBotEclipseEditor.findEditors().size() == 0); } });
			 */
		} catch (Throwable thIgnore) {
			debug("Fatal error. Can't close editor");
			throw new RuntimeException(ErrorMessages.Common_internalFatalError,
					thIgnore);
		}
	}

	/**
	 * 
	 * @param project
	 * @param sourceFolder
	 * @param fileName
	 * @param perspectiveId
	 * @return
	 * @throws Exception
	 */
	// public IEditorPart openSourceFileInEditor(String project,
	// String sourceFolder, String fileName, String perspectiveId)
	// throws Exception {
	// ISourceModule cu = getSourceModule(project, sourceFolder, fileName);
	//
	// IWorkbenchWindow[] ww = PlatformUI.getWorkbench().getWorkbenchWindows();
	// PlatformUI.getWorkbench().showPerspective(perspectiveId, ww[0]);
	//
	// SWTBotShell[] sh = bot.shells();
	// sh[0].activate();
	//
	// return EditorUtility.openInEditor(cu);
	// }
	public String createExternalProject() throws IOException {
		File targetProject = new File(TEMP_DIR + "/ExternalTclProject");
		File sourceProject = new File(getSourceWorkspacePath(), ETALON_PROJECT);

		copyDirectory(sourceProject, targetProject);

		return targetProject.getAbsolutePath();
	}

	public IScriptProject setUpScriptProject(final String projectName)
			throws CoreException, IOException {
		return super.setUpScriptProject(projectName);
	}

	public void deleteProject(String projectName) throws CoreException {
		super.deleteProject(projectName);
	}

	public IProject getProject(String name) {
		return super.getProject(name);
	}

	public String getNature() {
		return TclNature.NATURE_ID;
	}

	public boolean isTclProject(IProject project) {
		IScriptProject tclProject = DLTKCore.create(project);
		IDLTKLanguageToolkit toolkit = DLTKLanguageManager
				.getLanguageToolkit(tclProject);
		return getNature().equals(toolkit.getNatureId());
	}

	public void openTclPerspective() {
		BoolResult result = new BoolResult() {
			public Boolean run() {
				IWorkbenchWindow[] ww = PlatformUI.getWorkbench()
						.getWorkbenchWindows();
				try {
					PlatformUI.getWorkbench().showPerspective(
							TCL_PERSPECTIVE_ID, ww[0]);
					return true;
				} catch (Throwable th) {
					return false;
				}
			}
		};

		boolean error = UIThreadRunnable.syncExec(bot.getDisplay(), result);
		assertTrue(ErrorMessages.Common_errOpenPerspective, error);
	}

	public void openJavaPerspective() {
		BoolResult result = new BoolResult() {
			public Boolean run() {
				IWorkbenchWindow[] ww = PlatformUI.getWorkbench()
						.getWorkbenchWindows();
				try {
					PlatformUI.getWorkbench().showPerspective(
							"org.eclipse.jdt.ui.JavaPerspective", ww[0]);
					return true;
				} catch (Throwable th) {
					return false;
				}
			}
		};

		boolean error = UIThreadRunnable.syncExec(bot.getDisplay(), result);
		assertTrue(ErrorMessages.Common_errOpenPerspective, error);
	}

	public List<IInterpreterInstall> getInterpreters() {
		List<IInterpreterInstall> result = new ArrayList<IInterpreterInstall>();
		IInterpreterInstallType[] types = getInterpreterTypes();
		for (int i = 0; i < types.length; i++) {
			IInterpreterInstallType type = types[i];
			IInterpreterInstall[] installs = type.getInterpreterInstalls();
			if (installs != null) {
				for (int j = 0; j < installs.length; j++) {
					IInterpreterInstall install = installs[j];
					result.add(install);
				}
			}
		}
		return result;
	}

	protected IInterpreterInstall createDefaultInterpreter()
			throws CoreException {
		IInterpreterInstallType[] types = getInterpreterTypes();
		IInterpreterInstall interpreter = createInterpreterInstall(
				DEF_INTERPRETER_PATH, DEF_INTERPRETER_ID, types[0]);
		ScriptRuntime.setDefaultInterpreterInstall(interpreter, null, false);
		return interpreter;
	}

	protected IInterpreterInstall createAltInterpreter() {
		IInterpreterInstallType[] types = getInterpreterTypes();
		IInterpreterInstall interpreter = createInterpreterInstall(
				ALT_INTERPRETER_PATH, ALT_INTERPRETER_ID, types[0]);
		return interpreter;
	}

	protected IInterpreterInstall createInterpreter(IPath path) {
		IInterpreterInstallType[] types = getInterpreterTypes();
		String name = path.lastSegment();
		IInterpreterInstall interpreter = createInterpreterInstall(path
				.toOSString(), name, types[0]);
		return interpreter;
	}

	public void clearInterpreters() {
		IInterpreterInstallType[] types = getInterpreterTypes();
		for (int indexType = 0; indexType < types.length; indexType++) {
			IInterpreterInstallType type = types[indexType];
			IInterpreterInstall[] installs = type.getInterpreterInstalls();
			for (int index = 0; index < installs.length; index++) {
				type.disposeInterpreterInstall(installs[index].getId());
			}
		}
	}

	public IInterpreterInstall getInterpreter(String id) {
		IInterpreterInstallType[] types = getInterpreterTypes();
		return types[0].findInterpreterInstall(id);
	}

	public IInterpreterInstall getInterpreterByName(String name) {
		IInterpreterInstallType[] types = getInterpreterTypes();
		return types[0].findInterpreterInstallByName(name);
	}

	protected IInterpreterInstallType[] getInterpreterTypes() {
		return ScriptRuntime.getInterpreterInstallTypes(getNature());
	}

	protected IInterpreterInstall createInterpreterInstall(String path,
			String id, IInterpreterInstallType type) {
		IFileHandle file = EnvironmentManager.getLocalEnvironment().getFile(
				new Path(path));
		if (!file.exists()) {
			return null;
		}
		IInterpreterInstall install = type.findInterpreterInstall(id);

		if (install == null)
			install = type.createInterpreterInstall(id);

		install.setName(id);
		install.setInstallLocation(file);
		install.setLibraryLocations(null);
		install.setEnvironmentVariables(null);
		return install;
	}

	public boolean isDefaultInterpreter(IInterpreterInstall interpreter) {
		final IEnvironment env = interpreter.getEnvironment();
		if (env == null) {
			return false;
		}
		final String environmentId = env.getId();
		String nature = interpreter.getInterpreterInstallType().getNatureId();
		DefaultInterpreterEntry defaultInterpreterID = new DefaultInterpreterEntry(
				nature, environmentId);

		IInterpreterInstall defInterpr = ScriptRuntime
				.getDefaultInterpreterInstall(defaultInterpreterID);
		return (interpreter == defInterpr);
	}

	protected IInterpreterInstall getInterpreterByIdOrName(String interpr) {
		IInterpreterInstall interpreter = getInterpreter(interpr);
		if (interpreter == null) {
			interpreter = getInterpreterByName(interpr);
		}
		return interpreter;
	}

	protected void interpreterUpdate(final IInterpreterInstall[] interpreters,
			final IInterpreterInstall[] defaultInterpreters) {
		VoidResult result = new VoidResult() {
			public void run() {
				InterpretersUpdater updater = new InterpretersUpdater();
				updater.updateInterpreterSettings(getNature(), interpreters,
						defaultInterpreters);
			}
		};

		UIThreadRunnable.syncExec(bot.getDisplay(), result);
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Initialize methods
	//
	// /////////////////////////////////////////////////////////////////////////
	public String initExternalProject() {
		try {
			return createExternalProject();
		} catch (Throwable th) {
			throw new RuntimeException(ErrorMessages.Common_initError, th);
		}
	}

	public IInterpreterInstall initOnlyDefInterpreter() {
		IInterpreterInstall defInterpreter = initDefInterpreter();
		assertDefInterpreterExist();

		IInterpreterInstall[] interpreters = new IInterpreterInstall[] { defInterpreter };
		interpreterUpdate(interpreters, interpreters);
		return defInterpreter;
	}

	public IInterpreterInstall[] initDefAndAltInterpreters() {
		IInterpreterInstall defInterpreter = initDefInterpreter();
		assertDefInterpreterExist();

		IInterpreterInstall altInterpreter = initAltInterpreter();
		assertAltInterpreterExist();

		IInterpreterInstall[] allInterpreters = new IInterpreterInstall[] {
				defInterpreter, altInterpreter };
		IInterpreterInstall[] defInterpreters = new IInterpreterInstall[] { defInterpreter };

		interpreterUpdate(allInterpreters, defInterpreters);
		return allInterpreters;
	}

	public IInterpreterInstall[] init3Interpreters() {
		IInterpreterInstall defInterpreter = initDefInterpreter();
		assertDefInterpreterExist();

		IInterpreterInstall altInterpreter = initAltInterpreter();
		assertAltInterpreterExist();

		IPath path = new Path(DltkTestsHelper.ALT_2_INTERPRETER_PATH);
		IInterpreterInstall interpr = createInterpreter(path);

		IInterpreterInstall[] allInterpreters = new IInterpreterInstall[] {
				defInterpreter, altInterpreter, interpr };
		IInterpreterInstall[] defInterpreters = new IInterpreterInstall[] { defInterpreter };

		interpreterUpdate(allInterpreters, defInterpreters);
		return allInterpreters;
	}

	protected IInterpreterInstall initDefInterpreter() {
		try {
			return createDefaultInterpreter();
		} catch (Throwable th) {
			throw new RuntimeException(ErrorMessages.Common_initError, th);
		}
	}

	protected IInterpreterInstall initAltInterpreter() {
		try {
			return createAltInterpreter();
		} catch (Throwable th) {
			throw new RuntimeException(ErrorMessages.Common_initError, th);
		}
	}

	protected boolean isScriptFolder(IScriptProject project, IFolder folder)
			throws ModelException {
		if (folder == null || !folder.exists()) {
			return false;
		}

		IScriptFolder[] folders = project.getScriptFolders();
		for (int index = 0; index < folders.length; index++) {
			IResource resource = folders[index].getResource();
			if (resource == null) {
				continue;
			}
			if (folder.equals(resource)) {
				return true;
			}
		}
		return false;
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Asserts
	//
	// /////////////////////////////////////////////////////////////////////////

	public void assertDefInterpreterExist() {
		assertInterpreterByIdOrName(DltkTestsHelper.DEF_INTERPRETER_ID,
				ErrorMessages.Common_defInterprNotFound);
	}

	public void assertAltInterpreterExist() {
		assertInterpreterByIdOrName(DltkTestsHelper.ALT_INTERPRETER_ID,
				ErrorMessages.Common_altInterprNotFound);
	}

	public void assertInterpretersEmpty() {
		assertInterpretersCount(0);
	}

	public void assertInterpretersCount(final int count) {
		try {
			bot.waitUntil(new DefaultCondition() {
				private String message;

				public String getFailureMessage() {
					return message;
				}

				public boolean test() throws Exception {
					message = ErrorMessages.Common_invalidInterprCount;
					int realInterpreterCount = getInterpreters().size();
					message = ErrorMessages.format(message, new Integer[] {
							new Integer(count),
							new Integer(realInterpreterCount) });
					return (realInterpreterCount == count);
				}
			}, INTERPRETER_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertInterpreter(String projectName, String interprId) {
		IScriptProject tclProject = DLTKCore.create(getProject(projectName));
		assertInterpreter(tclProject, interprId);
	}

	public void assertInterpreter(IScriptProject project, String interprId) {
		String errorMessage = ErrorMessages.Common_prjInterprNotFound;
		errorMessage = ErrorMessages.format(errorMessage, interprId);
		try {
			IInterpreterInstall interpreter = ScriptRuntime
					.getInterpreterInstall(project);
			assertNotNull(errorMessage, interpreter);
			assertEquals(interprId, interpreter.getName());
		} catch (CoreException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	protected void assertInterpreterByIdOrName(final String interpr,
			final String message) {
		try {
			bot.waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return message;
				}

				public boolean test() throws Exception {
					return getInterpreterByIdOrName(interpr) != null;
				}
			}, INTERPRETER_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertTclProject(final String name) {
		try {
			bot.waitUntil(new DefaultCondition() {
				private String message;

				public String getFailureMessage() {
					return message;
				}

				public boolean test() throws Exception {
					IProject project = getProject(name);
					if (!project.exists()) {
						message = ErrorMessages.Project_errNotFound;
						return false;
					}

					message = ErrorMessages.Project_errNotTclProject;
					return isTclProject(project);
				}
			}, INTERPRETER_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertProjectDoesNotExist(final String projectName) {
		try {
			bot.waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return ErrorMessages.Project_errDelete;
				}

				public boolean test() throws Exception {
					IProject project = getProject(projectName);
					return !project.exists();
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertScriptExists(final String projectName, String srcPath,
			String fullyQualifiedName) {

		IProject project = getWorkspaceRoot().getProject(projectName);
		IPath projectPath = project.getFullPath();
		final String rootPath = projectPath.toString() + "/" + srcPath;
		final IPath sourcePath = new Path(fullyQualifiedName);

		String msg = ErrorMessages.ProjectContent_errScriptNotExist;
		final String errorMessage = ErrorMessages.format(msg,
				fullyQualifiedName);

		try {
			bot.waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					ISourceModule file = getSourceModule(projectName, rootPath,
							sourcePath);

					return (file != null && file.exists());
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertFileExists(String projectName, String fullyQualifiedName) {
		String msg = ErrorMessages.ProjectContent_errFileNotExist;
		final String errorMessage = ErrorMessages.format(msg,
				fullyQualifiedName);

		final IProject project = getWorkspaceRoot().getProject(projectName);
		final IPath sourcePath = new Path(fullyQualifiedName);

		try {
			bot.waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					IFile file = project.getFile(sourcePath);
					return (file != null && file.exists());
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertFolderExists(String projectName,
			final String fullyQualifiedName) {
		IProject project = getWorkspaceRoot().getProject(projectName);
		final IPath projectPath = project.getFullPath();
		final IPath sourcePath = new Path(fullyQualifiedName);

		String msg = ErrorMessages.ProjectContent_errFolderNotExist;
		final String errorMessage = ErrorMessages.format(msg,
				fullyQualifiedName);

		try {
			bot.waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					IFolder folder = getFolder(projectPath.append(sourcePath));
					return (folder != null && folder.exists());
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertSourceFolderExists(final IScriptProject project,
			final String fullyQualifiedName) {
		final IFolder folder = project.getProject().getFolder(
				fullyQualifiedName);

		String msg = ErrorMessages.ProjectContent_errSourceFolderNotExist;
		final String errorMessage = ErrorMessages.format(msg,
				fullyQualifiedName);

		try {
			bot.waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					return isScriptFolder(project, folder);
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertTreeStructure(SWTBotTree tree, String treePath) {
		StringTokenizer tok = new StringTokenizer(treePath, "/\\");

		if (!tok.hasMoreTokens())
			return;
		String s = tok.nextToken();

		SWTBotTreeItem item;
		try {
			item = tree.getTreeItem(s);
		} catch (WidgetNotFoundException e) {
			item = null;
		}

		assertNotNull("Element \"" + s + "\" not found in tree", item);
		item.expand();

		while (tok.hasMoreTokens()) {
			s = tok.nextToken();
			item = item.getNode(s);
			assertNotNull("Element " + s + " not found in tree", item);
			item.expand();

		}

	}

	public void assertExternalProject(String name) {
		IProject project = getProject(name);
		assertTrue(ErrorMessages.Project_errNotFound, project.exists());
		assertTrue(ErrorMessages.Project_errNotTclProject,
				isTclProject(project));
		// TODO: add implementation for compare of file system structure
	}

	public void assertFileDoesNotExist(String projectName,
			String fullyQualifiedName) {

		final IProject project = getWorkspaceRoot().getProject(projectName);
		final IPath sourcePath = new Path(fullyQualifiedName);

		String msg = ErrorMessages.ProjectContent_errFileExist;
		final String errorMessage = ErrorMessages.format(msg,
				fullyQualifiedName);

		try {
			bot.waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					IFile file = project.getFile(sourcePath);
					return (file == null || !file.exists());
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertSourceFolderDoesNotExist(final IScriptProject project,
			String fullyQualifiedName) {
		final IFolder folder = project.getProject().getFolder(
				fullyQualifiedName);

		String msg = ErrorMessages.ProjectContent_errSourceFolderExist;
		final String errorMessage = ErrorMessages.format(msg,
				fullyQualifiedName);

		try {
			bot.waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					return !isScriptFolder(project, folder);
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertFolderDoesNotExist(final IScriptProject project,
			final String fullyQualifiedName) {
		String msg = ErrorMessages.ProjectContent_errFolderExist;
		final String errorMessage = ErrorMessages.format(msg,
				fullyQualifiedName);

		try {
			bot.waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return errorMessage;
				}

				public boolean test() throws Exception {
					IFolder folder = project.getProject().getFolder(
							fullyQualifiedName);
					return !isScriptFolder(project, folder);
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertPerspective(final String perspectiveId) {
		BoolResult result = new BoolResult() {
			public Boolean run() {
				try {
					String activePerspId = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getPerspective().getId();
					return perspectiveId.equals(activePerspId);
				} catch (Throwable th) {
					return false;
				}
			}
		};

		boolean error = UIThreadRunnable.syncExec(bot.getDisplay(), result);
		assertTrue(ErrorMessages.Common_invalidPerspective, error);
	}

	public void assertProjectContentExist(final IPath path) {
		try {
			bot.waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return ErrorMessages.Project_errContentNotExist;
				}

				public boolean test() throws Exception {
					// TODO: add compare of content
					return path.toFile().exists();
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void assertProjectContentDoesNotExist(final IPath path) {
		try {
			bot.waitUntil(new DefaultCondition() {

				public String getFailureMessage() {
					return ErrorMessages.Project_errContentExist;
				}

				public boolean test() throws Exception {
					return !path.toFile().exists();
				}
			}, RESOURCE_REFRESH_TIMEOUT);
		} catch (TimeoutException ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public static void debug(String message) {
		if (DEBUG) {
			System.out.println(message);
		}
	}
}
