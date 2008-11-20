/**************************************************import junit.framework.TestSuite;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.ui.tests.swtbot.DltkTestsHelper;
import org.eclipse.dltk.ui.tests.swtbot.operations.Operations;
import org.eclipse.dltk.ui.tests.swtbot.operations.ProjectContentOperations;
nc. - initial API and Implementation
 *******************************************************************************/
package org.eclipse.dltk.ui.tests.swtbot.complex;

import junit.framework.TestSuite;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.ui.tests.swtbot.DltkTestsHelper;
import org.eclipse.dltk.ui.tests.swtbot.operations.Operations;
import org.eclipse.dltk.ui.tests.swtbot.operations.ProjectContentOperations;

public class ProjectContentTests extends SuiteOfTestCases {

	private static boolean DEBUG;

	static {
		String value = Platform
				.getDebugOption("org.eclipse.dltk.tcl.ui.tests.swtbot/ProjectContent");
		DEBUG = Boolean.valueOf(value).booleanValue();
	}

	private static final String PROJECT_NAME = "ProjectContent";
	private static final String ROOT_IN_BUILD_PATH_PROJECT_NAME = "RootInBuildPath";

	private static final String NEW_FOLDER1 = "abc";
	private static final String NEW_FOLDER2 = "def";
	private static final String NEW_TCL_SCRIPT1 = "a.tcl";
	private static final String NEW_FILE = "aaa.ext";

	private static final String FOLDER1 = "Folder1";
	private static final String FOLDER2 = "Folder2";
	private static final String SOURCE_FOLDER1 = "SourceFolder1";
	private static final String SOURCE_FOLDER2 = "SourceFolder2";

	private static final String FILE1 = "File1.ext";
	private static final String SCRIPT1 = "Script1.tcl";
	private static final String SCRIPT_IN_ROOT = "Script5.tcl";
	private static final String FILE_IN_ROOT = "File6.ext";
	private static final String SCRIPT_IN_SOURCE_FOLDER = "Script3.tcl";
	private static final String NESTED_FOLDER = "NestedFolder";

	private static final String NESTED_FOLDER2 = "NestedFolder/Nested2";

	private IScriptProject project;
	private DltkTestsHelper helper;
	private String altProjectName;

	ProjectContentOperations operations;
	private SWTEclipseBot bot = new SWTEclipseBot();

	public ProjectContentTests(String name) {
		super(name);
		setName(name);
		helper = new DltkTestsHelper(name, bot);
		DltkTestsHelper.DEBUG = DEBUG;
		operations = new ProjectContentOperations(bot);
	}

	public static TestSuite suite() {
		return new Suite(ProjectContentTests.class);
	}

	public void setUp() throws Exception {
		DltkTestsHelper.debug(getName() + " starting...");
		super.setUp();
		helper.setUpSuite();
		helper.openTclPerspective();
		project = helper.setUpScriptProject(PROJECT_NAME);
		altProjectName = null;
		DltkTestsHelper.debug(getName() + " started");
	}

	public void tearDown() throws Exception {
		DltkTestsHelper.debug(getName() + " finishing...");
		helper.closeDialogs();

		helper.deleteProject(PROJECT_NAME);
		if (altProjectName != null) {
			helper.deleteProject(altProjectName);
		}

		helper.tearDownSuite();
		super.tearDown();
		DltkTestsHelper.debug(getName() + " finished");
	}

	protected String getProjectName() {
		return project.getProject().getName();
	}

	/**
	 * Create a folder (DLTK-517).<br>
	 * <br>
	 * 1. Select parent folder in Script explorer<br>
	 * 2. Open New Folder dialog (File->New->Folder)<br>
	 * 3. Enter folder name<br>
	 * 4. Finish<br>
	 * 
	 */
	public void testCreateFolder001() {
		operations.createFolder001(getProjectName(), FOLDER1, NEW_FOLDER1);
		String folderName = FOLDER1 + "/" + NEW_FOLDER1;
		helper.assertFolderExists(getProjectName(), folderName);
		operations.assertProjectElementExist(getProjectName(), folderName);
	}

	/**
	 * Create a folder (DLTK-517)<br>
	 * <br>
	 * 1. Open New Folder dialog (File->New->Folder)<br>
	 * 2. Select parent folder in tree in dialog<br>
	 * 3. Enter folder name<br>
	 * 4. Finish<br>
	 * 
	 */
	public void testCreateFolder002() {
		operations.createFolder002(getProjectName(), FOLDER1, NEW_FOLDER1);
		String folderName = FOLDER1 + "/" + NEW_FOLDER1;
		helper.assertFolderExists(getProjectName(), folderName);
		operations.assertProjectElementExist(getProjectName(), folderName);
	}

	/**
	 * Create a folder (DLTK-517)<br>
	 *<br>
	 * 1. Open New Folder dialog (File->New->Folder)<br>
	 * 2. Enter folder path with use path separator<br>
	 * 3. Finish<br>
	 * 
	 */
	public void testCreateFolder003() {
		String folderName = FOLDER1 + "/" + NEW_FOLDER1;
		operations.createFolder002(getProjectName(), null, folderName);
		helper.assertFolderExists(getProjectName(), folderName);
		operations.assertProjectElementExist(getProjectName(), folderName);
	}

	/**
	 * Create folder by context menu (DLTK-604) <br>
	 * <br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select parent folder in Script explorer<br>
	 * 2. Open New Folder dialog (PopupMenu->New->Folder)<br>
	 * 3. Enter folder name<br>
	 * 4. Finish<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in parent folder<br>
	 * 
	 */
	public void testCreateFolderByContextMenu() {
		operations.createFolderByContextMenu(getProjectName(), FOLDER1,
				NEW_FOLDER1);
		String folderName = FOLDER1 + "/" + NEW_FOLDER1;
		helper.assertFolderExists(getProjectName(), folderName);
		operations.assertProjectElementExist(getProjectName(), folderName);
	}

	/**
	 * Create a source folder without update exclusion filter (DLTK-516)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with one source folder and one not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select project in Script explorer<br>
	 * 2. Open New Folder dialog (Menu->New->Folder)<br>
	 * 3. Check: "Project name" equals to name of project, "Finish" button is
	 * disable<br>
	 * 3. Enter "Folder name"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 */
	public void testCreateSourceFolder001() {
		operations.createSourceFolder(getProjectName(), "", NEW_FOLDER1, false);
		helper.assertSourceFolderExists(project, NEW_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER1);
	}

	/**
	 * Create a source folder without update exclusion filter (DLTK-516) <br>
	 * <br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with one source folder and one not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select source folder in Script explorer<br>
	 * 2. Open New Folder dialog (Menu->New->Folder)<br>
	 * 3. Check: "Project name" equals to name of project, "Finish" button is
	 * disable<br>
	 * 3. Enter "Folder name"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 * 
	 */
	public void testCreateSourceFolder002() {
		operations.createSourceFolder(getProjectName(), SOURCE_FOLDER1,
				NEW_FOLDER1, false);
		helper.assertSourceFolderExists(project, NEW_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER1);

		String folderName = SOURCE_FOLDER1 + "/" + NEW_FOLDER1;
		helper.assertSourceFolderDoesNotExist(project, folderName);
		operations.assertProjectElementNotExist(getProjectName(), folderName);
	}

	/**
	 * Create a source folder without update exclusion filter(DLTK-516) <br>
	 * <br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with one source folder and one not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select not source folder in Script explorer:<br>
	 * 2. Open New Folder dialog (Menu->New->Folder)<br>
	 * 3. Check: "Project name" equals to name of project, "Finish" button is
	 * disable<br>
	 * 3. Enter "Folder name"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 * 
	 */
	public void testCreateSourceFolder003() {
		operations.createSourceFolder(getProjectName(), FOLDER1, NEW_FOLDER1,
				false);
		helper.assertSourceFolderExists(project, NEW_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER1);

		String folderName = FOLDER1 + "/" + NEW_FOLDER1;
		helper.assertSourceFolderDoesNotExist(project, folderName);
		operations.assertProjectElementNotExist(getProjectName(), folderName);
	}

	/**
	 * Create a nested source folder without update exclusion filter (DLTK-708)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select project in Script explorer:<br>
	 * 2. Open New Folder dialog (Menu->New->Folder)<br>
	 * 3. Check: "Project name" equals to name of project, "Finish" button is
	 * disable<br>
	 * 4. Enter "<parent not source folder>/<new source folder name>"<br>
	 * 5. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in parent folder<br>
	 * 2. New folder is source folder<br>
	 * 3. Parent folder is not source folder<br>
	 * 
	 */
	public void testCreateNestedSourceFolder() {
		String folderName = FOLDER1 + "/" + NEW_FOLDER1;
		operations.createSourceFolder(getProjectName(), "", folderName, false);
		helper.assertSourceFolderDoesNotExist(project, FOLDER1);
		helper.assertSourceFolderExists(project, folderName);
		operations.assertProjectElementExist(getProjectName(), FOLDER1);
		operations.assertProjectElementNotExist(getProjectName(), folderName);
	}

	/**
	 * Create a source folder without update exclusion filter using pop-up menu
	 * (DLTK-603)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with one source folder and one not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select project in Script explorer<br>
	 * 2. Open New Folder dialog (Menu->New->Folder)<br>
	 * 3. Check: "Project name" equals to name of project, "Finish" button is
	 * disable<br>
	 * 3. Enter "Folder name"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 * 
	 */
	public void testCreateSourceFolderByContextMenu001() {
		operations.createSourceFolderByContextMenu(getProjectName(), "",
				NEW_FOLDER1, false);
		helper.assertSourceFolderExists(project, NEW_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER1);
	}

	/**
	 * Create a source folder without update exclusion filter using pop-up menu
	 * (DLTK-603)<br>
	 * <br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with one source folder and one not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select source folder in Script explorer<br>
	 * 2. Open New Folder dialog (Menu->New->Folder)<br>
	 * 3. Check: "Project name" equals to name of project, "Finish" button is
	 * disable<br>
	 * 3. Enter "Folder name"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 * 
	 */
	public void testCreateSourceFolderByContextMenu002() {
		operations.createSourceFolderByContextMenu(getProjectName(),
				SOURCE_FOLDER1, NEW_FOLDER1, false);
		helper.assertSourceFolderExists(project, NEW_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER1);

		String folderName = SOURCE_FOLDER1 + "/" + NEW_FOLDER1;
		helper.assertSourceFolderDoesNotExist(project, folderName);
		operations.assertProjectElementNotExist(getProjectName(), folderName);
	}

	/**
	 * Create a source folder without update exclusion filter using pop-up menu
	 * (DLTK-603)<br>
	 * <br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project with one source folder and one not source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select not source folder in Script explorer:<br>
	 * 2. Open New Folder dialog (Menu->New->Folder)<br>
	 * 3. Check: "Project name" equals to name of project, "Finish" button is
	 * disable<br>
	 * 3. Enter "Folder name"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 * 
	 */
	public void testCreateSourceFolderByContextMenu003() {
		operations.createSourceFolderByContextMenu(getProjectName(), FOLDER1,
				NEW_FOLDER1, false);
		helper.assertSourceFolderExists(project, NEW_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER1);

		String folderName = FOLDER1 + "/" + NEW_FOLDER1;
		helper.assertSourceFolderDoesNotExist(project, folderName);
		operations.assertProjectElementNotExist(getProjectName(), folderName);
	}

	/**
	 * Create a Tcl file in project root (DLTK-518)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project<br>
	 * 2. Root not in source path<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select project in Script explorer:<br>
	 * 2. Open New File dialog (Menu->New->File)<br>
	 * 3. Enter "File"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 * 
	 */
	public void testCreateFile001() {
		operations.createFile001(getProjectName(), NEW_FILE);
		helper.assertFileExists(getProjectName(), NEW_FILE);
		operations.assertProjectElementExist(getProjectName(), NEW_FILE);
	}

	/**
	 * Create a Tcl file in project root (DLTK-518)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. Exist Tcl project<br>
	 * 2. Root not in source path<br>
	 * <br>
	 * Actions:<br>
	 * 1. Open New File dialog (Menu->New->File)<br>
	 * 2. Select project in tree in dialog<br>
	 * 3. Enter "File"<br>
	 * 4. Press "Finish" button<br>
	 * <br>
	 * Expected:<br>
	 * 1. New folder exist in project root (independently of selected folder)<br>
	 * 2. This folder is source folder<br>
	 * 
	 */
	public void testCreateFile002() {
		operations.createFile002(getProjectName(), null, NEW_FILE);
		helper.assertFileExists(getProjectName(), NEW_FILE);
		operations.assertProjectElementExist(getProjectName(), NEW_FILE);
	}

	/**
	 * Create a Tcl file in project root using pop-up menu (DLTK-607). Equals to
	 * testCreateFile001 but for Open New File dialog use (PopupMenu->New->File)
	 */
	public void testCreateFileByContextMenu() {
		operations.createFileByContextMenu(getProjectName(), null, NEW_FILE);
		helper.assertFileExists(getProjectName(), NEW_FILE);
		operations.assertProjectElementExist(getProjectName(), NEW_FILE);
	}

	/**
	 * Create a script in project root (DLTK-521).<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. Project root in build path<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select project in Script explorer<br>
	 * 2. Create script (Menu->New->Tcl File)<br>
	 * <br>
	 * Expected:<br>
	 * 1. script must be created in project root<br>
	 * 
	 */
	public void testCreateScript001() {
		try {
			altProjectName = ROOT_IN_BUILD_PATH_PROJECT_NAME;
			project = helper.setUpScriptProject(altProjectName);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		operations.createScript(getProjectName(), null, NEW_TCL_SCRIPT1);

		helper.assertScriptExists(getProjectName(), "", NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a script in project root (DLTK-521).<br>
	 *<br>
	 *Predefined conditions:<br>
	 * 1. Project root not in build path<br>
	 * 2. Exist source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select project in Script explorer<br>
	 * 2. Create script (Menu->New->Tcl File)<br>
	 * <br>
	 * Expected:<br>
	 * 1. script must be created in source folder<br>
	 * 
	 */
	public void testCreateScript002() {
		operations.createScript(getProjectName(), null, NEW_TCL_SCRIPT1);
		helper.assertFileDoesNotExist(getProjectName(), NEW_TCL_SCRIPT1);
		operations.assertProjectElementNotExist(getProjectName(),
				NEW_TCL_SCRIPT1);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a script by context menu (DLTK-608)<br>
	 *<br>
	 * Predefined conditions:<br>
	 * 1. Project root not in build path<br>
	 * 2. Exist source folder<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select project in Script explorer<br>
	 * 2. Create script (PopupMenu->New->Tcl File)<br>
	 * <br>
	 * Expected:<br>
	 * 1. script must be created in source folder<br>
	 * 
	 */
	public void testCreateScriptByContextMenu() {
		operations.createScriptByContextMenu(getProjectName(), null,
				NEW_TCL_SCRIPT1);
		helper.assertFileDoesNotExist(getProjectName(), NEW_TCL_SCRIPT1);
		operations.assertProjectElementNotExist(getProjectName(),
				NEW_TCL_SCRIPT1);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a script in folder. Folder not in build path. (DLTK-606).<br>
	 * <br>
	 * Test 1:<br>
	 * Predefined conditions:<br>
	 * 1. Project root in build path<br>
	 * 2. Source folder exist<br>
	 * 3. Not source folder exist<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select Not source folder in Script explorer<br>
	 * 2. Create script (Menu->New->Tcl File)<br>
	 * <br>
	 * Expected:<br>
	 * 1. script must be created in default source folder<br>
	 * 
	 */
	public void testCreateScriptInFolder() {
		operations.createScript(getProjectName(), FOLDER1, NEW_TCL_SCRIPT1);

		String path = FOLDER1 + "/" + NEW_TCL_SCRIPT1;
		helper.assertFileDoesNotExist(getProjectName(), path);
		operations.assertProjectElementNotExist(getProjectName(), path);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a script in folder by context menu (DLTK-612) <br>
	 * Predefined conditions:<br>
	 * 1. Project root in build path<br>
	 * 2. Source folder exist<br>
	 * 3. Not source folder exist<br>
	 * <br>
	 * Actions:<br>
	 * 1. Select Not source folder in Script explorer<br>
	 * 2. Create script (PopupMenu->New->Tcl File)<br>
	 * <br>
	 * Expected:<br>
	 * 1. script must be created in default source folder<br>
	 */
	public void testCreateScriptInFolderByContextMenu() {
		operations.createScriptByContextMenu(getProjectName(), FOLDER1,
				NEW_TCL_SCRIPT1);

		String path = FOLDER1 + "/" + NEW_TCL_SCRIPT1;
		helper.assertFileDoesNotExist(getProjectName(), path);
		operations.assertProjectElementNotExist(getProjectName(), path);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a script in source folder (DLTK-605)
	 * 
	 */
	public void testCreateScriptInSourceFolder() {
		operations.createScript(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a script in source folder by context menu (DLTK-610)
	 * 
	 */
	public void testCreateScriptInSourceFolderByContextMenu() {
		operations.createScriptByContextMenu(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a Tcl file in source folder (DLTK-519)
	 * 
	 */
	public void testCreateFileInSourceFolder() {
		operations.createFile002(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a Tcl file in source folder by context menu (DLTK-609)
	 * 
	 */
	public void testCreateFileInSourceFolderByContextMenu() {
		operations.createFileByContextMenu(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Create a TCL file in folder. Folder not in build path. (DLTK-520)
	 * 
	 */
	public void testCreateFileInFolder() {
		operations.createFile002(getProjectName(), FOLDER1, NEW_TCL_SCRIPT1);
		String path = FOLDER1 + "/" + NEW_TCL_SCRIPT1;
		helper.assertFileExists(getProjectName(), path);
		operations.assertProjectElementExist(getProjectName(), path);
	}

	/**
	 * Create a TCL file in folder by context menu (DLTK-611)
	 * 
	 */
	public void testCreateFileInFolderByContextMenu() {
		operations.createFileByContextMenu(getProjectName(), FOLDER1,
				NEW_TCL_SCRIPT1);
		String path = FOLDER1 + "/" + NEW_TCL_SCRIPT1;
		helper.assertFileExists(getProjectName(), path);
		operations.assertProjectElementExist(getProjectName(), path);
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Copy
	//
	// //////////////////////////////////////////////////////////////////////////

	/**
	 * Copy file from a folder to another one (DLTK-523)
	 * 
	 */
	public void testCopyFileBetweenFolders() {
		operations.copyElement(getProjectName(), FOLDER1 + "/" + FILE1);
		operations.pasteElement(getProjectName(), FOLDER2);

		helper.assertFileExists(getProjectName(), FOLDER2 + "/" + FILE1);
		operations.assertProjectElementExist(getProjectName(), FOLDER2 + "/"
				+ FILE1);
	}

	/**
	 * Copy file from a folder to another one (DLTK-523)
	 * 
	 */
	public void testCopyFileBetweenFoldersByContextMenu() {
		operations.copyElementByContextMenu(getProjectName(), FOLDER1 + "/"
				+ FILE1);
		operations.pasteElementByContextMenu(getProjectName(), FOLDER2);

		helper.assertFileExists(getProjectName(), FOLDER2 + "/" + FILE1);
		operations.assertProjectElementExist(getProjectName(), FOLDER2 + "/"
				+ FILE1);
	}

	/**
	 * Copy files from project root to a folder (DLTK-712)
	 * 
	 */
	public void testCopyFileToFolderFromProjectRoot() {
		operations.copyElement(getProjectName(), FILE_IN_ROOT);
		operations.pasteElement(getProjectName(), FOLDER1);

		helper.assertFileExists(getProjectName(), FOLDER1 + "/" + FILE_IN_ROOT);
		operations.assertProjectElementExist(getProjectName(), FOLDER1 + "/"
				+ FILE_IN_ROOT);
	}

	/**
	 * Copy files from project root to a folder (DLTK-712)
	 * 
	 */
	public void testCopyFileToFolderFromProjectRootByContextMenu() {
		operations.copyElementByContextMenu(getProjectName(), FILE_IN_ROOT);
		operations.pasteElementByContextMenu(getProjectName(), FOLDER1);

		helper.assertFileExists(getProjectName(), FOLDER1 + "/" + FILE_IN_ROOT);
		operations.assertProjectElementExist(getProjectName(), FOLDER1 + "/"
				+ FILE_IN_ROOT);
	}

	/**
	 * Copy file from a folder to project root (DLTK-524)
	 * 
	 * @
	 */
	public void testCopyFileFromFolderToProjectRoot() {
		operations.copyElement(getProjectName(), FOLDER1 + "/" + FILE1);
		operations.pasteElement(getProjectName(), "");

		helper.assertFileExists(getProjectName(), FILE1);
		operations.assertProjectElementExist(getProjectName(), FILE1);
	}

	/**
	 * Copy file from a folder to project root (DLTK-524)
	 * 
	 */
	public void testCopyFileFromFolderToProjectRootByContextMenu() {
		operations.copyElementByContextMenu(getProjectName(), FOLDER1 + "/"
				+ FILE1);
		operations.pasteElementByContextMenu(getProjectName(), "");

		helper.assertFileExists(getProjectName(), FILE1);
		operations.assertProjectElementExist(getProjectName(), FILE1);
	}

	/**
	 * Copy script from source folder to project root (DLTK-527)
	 * 
	 * @
	 */
	public void testCopyScriptFromSourceFolderToProjectRoot() {
		operations.copyElement(getProjectName(), SOURCE_FOLDER1 + "/"
				+ SCRIPT_IN_SOURCE_FOLDER);
		operations.pasteElement(getProjectName(), "");

		helper.assertFileExists(getProjectName(), SCRIPT_IN_SOURCE_FOLDER);
		operations.assertProjectElementExist(getProjectName(),
				SCRIPT_IN_SOURCE_FOLDER);

	}

	/**
	 * Copy script from source folder to project root (DLTK-527)
	 * 
	 */
	public void testCopyScriptFromSourceFolderToProjectRootByContextMenu() {
		operations.copyElementByContextMenu(getProjectName(), SOURCE_FOLDER1
				+ "/" + SCRIPT_IN_SOURCE_FOLDER);
		operations.pasteElementByContextMenu(getProjectName(), "");

		helper.assertFileExists(getProjectName(), SCRIPT_IN_SOURCE_FOLDER);
		operations.assertProjectElementExist(getProjectName(),
				SCRIPT_IN_SOURCE_FOLDER);

	}

	/**
	 * Copy files from a source folder to a source folder (DLTK-526)
	 * 
	 */
	public void testCopyScriptBetweenSourceFolders() {
		operations.copyElement(getProjectName(), SOURCE_FOLDER1 + "/"
				+ SCRIPT_IN_SOURCE_FOLDER);
		operations.pasteElement(getProjectName(), SOURCE_FOLDER2);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER2,
				SCRIPT_IN_SOURCE_FOLDER);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER2
				+ "/" + SCRIPT_IN_SOURCE_FOLDER);

	}

	/**
	 * Copy files from a source folder to a source folder (DLTK-526)
	 * 
	 */
	public void testCopyScriptBetweenSourceFoldersByContextMenu() {
		operations.copyElementByContextMenu(getProjectName(), SOURCE_FOLDER1
				+ "/" + SCRIPT_IN_SOURCE_FOLDER);
		operations.pasteElementByContextMenu(getProjectName(), SOURCE_FOLDER2);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER2,
				SCRIPT_IN_SOURCE_FOLDER);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER2
				+ "/" + SCRIPT_IN_SOURCE_FOLDER);

	}

	/**
	 * Copy files from a source folder to a folder (DLTK-525)
	 * 
	 */
	public void testCopyFileFromSourceFolderToFolder() {
		operations.copyElement(getProjectName(), SOURCE_FOLDER1 + "/"
				+ SCRIPT_IN_SOURCE_FOLDER);
		operations.pasteElement(getProjectName(), FOLDER1);

		helper.assertFileExists(getProjectName(), FOLDER1 + "/"
				+ SCRIPT_IN_SOURCE_FOLDER);
		operations.assertProjectElementExist(getProjectName(), FOLDER1 + "/"
				+ SCRIPT_IN_SOURCE_FOLDER);
	}

	/**
	 * Copy files from a source folder to a folder (DLTK-525)
	 * 
	 */
	public void testCopyFileFromSourceFolderToFolderByContextMenu() {
		operations.copyElementByContextMenu(getProjectName(), SOURCE_FOLDER1
				+ "/" + SCRIPT_IN_SOURCE_FOLDER);
		operations.pasteElementByContextMenu(getProjectName(), FOLDER1);

		helper.assertFileExists(getProjectName(), FOLDER1 + "/"
				+ SCRIPT_IN_SOURCE_FOLDER);
		operations.assertProjectElementExist(getProjectName(), FOLDER1 + "/"
				+ SCRIPT_IN_SOURCE_FOLDER);
	}

	/**
	 * Copy files from a folder to a source folder (DLTK-522)
	 * 
	 */
	public void testCopyScriptFromFolderToSourceFolder() {
		operations.copyElement(getProjectName(), FOLDER1 + "/" + SCRIPT1);
		operations.pasteElement(getProjectName(), SOURCE_FOLDER1);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1, SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + SCRIPT1);

	}

	/**
	 * Copy files from a folder to a source folder (DLTK-522)
	 * 
	 */
	public void testCopyScriptFromFolderToSourceFolderByContextMenu() {
		operations.copyElementByContextMenu(getProjectName(), FOLDER1 + "/"
				+ SCRIPT1);
		operations.pasteElementByContextMenu(getProjectName(), SOURCE_FOLDER1);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1, SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + SCRIPT1);

	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Rename
	//
	// //////////////////////////////////////////////////////////////////////////

	/**
	 * Rename a folder (DLTK-538)
	 * 
	 */
	public void testRenameFolder() {
		operations.renameElement(Operations.DLG_RENAME, getProjectName(),
				FOLDER1, NEW_FOLDER2);
		helper.assertFolderExists(getProjectName(), NEW_FOLDER2);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER2);
	}

	/**
	 * Rename a folder using context menu (DLTK-613)
	 * 
	 */
	public void testRenameFolderByContextMenu() {
		operations.renameElementByContextMenu(Operations.DLG_RENAME,
				getProjectName(), FOLDER1, NEW_FOLDER2);
		helper.assertFolderExists(getProjectName(), NEW_FOLDER2);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER2);
	}

	/**
	 * Rename a source folder (DLTK-539)
	 * 
	 */
	public void testRenameSourceFolder() {
		operations.renameElement(Operations.DLG_RENAME_SOURCE_FOLDER,
				getProjectName(), SOURCE_FOLDER1, NEW_FOLDER2);
		helper.assertSourceFolderExists(project, NEW_FOLDER2);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER2);

	}

	/**
	 * Rename a source folder using context menu (DLTK-617)
	 * 
	 */
	public void testRenameSourceFolderByContextMenu() {
		operations.renameElementByContextMenu(
				Operations.DLG_RENAME_SOURCE_FOLDER, getProjectName(),
				SOURCE_FOLDER1, NEW_FOLDER2);
		helper.assertSourceFolderExists(project, NEW_FOLDER2);
		operations.assertProjectElementExist(getProjectName(), NEW_FOLDER2);

	}

	/**
	 * Rename Tcl file in project root (DLTK-543)
	 * 
	 */
	public void testRenameFile() {
		operations.renameElement(Operations.DLG_RENAME, getProjectName(),
				FILE_IN_ROOT, NEW_TCL_SCRIPT1);
		helper.assertFileExists(getProjectName(), NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), NEW_TCL_SCRIPT1);
	}

	/**
	 * Rename a Tcl file in project root by context menu (DLTK-622)
	 * 
	 */
	public void testRenameFileByContextMenu() {
		operations.renameElementByContextMenu(Operations.DLG_RENAME,
				getProjectName(), SCRIPT_IN_ROOT, NEW_TCL_SCRIPT1);
		helper.assertFileExists(getProjectName(), NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), NEW_TCL_SCRIPT1);
	}

	/**
	 * Rename a Tcl file in folder (DLTK-542)
	 * 
	 */
	public void testRenameFileInFolder() {
		operations.renameElement(Operations.DLG_RENAME, getProjectName(),
				FOLDER1 + "/" + FILE1, NEW_FILE);

		helper.assertFileExists(getProjectName(), FOLDER1 + "/" + NEW_FILE);
		operations.assertProjectElementExist(getProjectName(), FOLDER1 + "/"
				+ NEW_FILE);
	}

	/**
	 * Rename a Tcl file in folder by context menu (DLTK-621)
	 * 
	 */
	public void testRenameFileInFolderByContextMenu() {
		operations.renameElementByContextMenu(Operations.DLG_RENAME,
				getProjectName(), FOLDER1 + "/" + FILE1, NEW_FILE);

		helper.assertFileExists(getProjectName(), FOLDER1 + "/" + NEW_FILE);
		operations.assertProjectElementExist(getProjectName(), FOLDER1 + "/"
				+ NEW_FILE);
	}

	/**
	 * Rename a Tcl file in source folder (DLTK-541)
	 * 
	 */
	public void testRenameFileInSourceFolder() {
		operations.renameElement(Operations.DLG_RENAME_SOURCE_MODULE,
				getProjectName(), SOURCE_FOLDER1 + "/"
						+ SCRIPT_IN_SOURCE_FOLDER, NEW_TCL_SCRIPT1);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Rename a Tcl File in source folder by context menu (DLTK-620)
	 * 
	 */
	public void testRenameFileInSourceFolderByContextMenu() {
		operations
				.renameElementByContextMenu(
						Operations.DLG_RENAME_SOURCE_MODULE, getProjectName(),
						SOURCE_FOLDER1 + "/" + SCRIPT_IN_SOURCE_FOLDER,
						NEW_TCL_SCRIPT1);

		helper.assertScriptExists(getProjectName(), SOURCE_FOLDER1,
				NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1
				+ "/" + NEW_TCL_SCRIPT1);
	}

	/**
	 * Rename script in project root (DLTK-537)
	 * 
	 */
	public void testRenameScript() {
		try {
			altProjectName = ROOT_IN_BUILD_PATH_PROJECT_NAME;
			project = helper.setUpScriptProject(altProjectName);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		operations.renameElement(Operations.DLG_RENAME_SOURCE_MODULE,
				getProjectName(), SCRIPT_IN_ROOT, NEW_TCL_SCRIPT1);

		helper.assertScriptExists(getProjectName(), "", NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), NEW_TCL_SCRIPT1);
	}

	/**
	 * Rename script in project root using popup-menu (DLTK-714)
	 * 
	 */
	public void testRenameScriptByContextMenu() {
		try {
			altProjectName = ROOT_IN_BUILD_PATH_PROJECT_NAME;
			project = helper.setUpScriptProject(altProjectName);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		operations.renameElementByContextMenu(
				Operations.DLG_RENAME_SOURCE_MODULE, getProjectName(),
				SCRIPT_IN_ROOT, NEW_TCL_SCRIPT1);

		helper.assertScriptExists(getProjectName(), "", NEW_TCL_SCRIPT1);
		operations.assertProjectElementExist(getProjectName(), NEW_TCL_SCRIPT1);
	}

	/**
	 * Rename a project (DLTK-540)
	 * 
	 */
	public void testRenameProject() {
		altProjectName = "RenamedProject";
		operations.renameElement(Operations.DLG_RENAME_SCRIPT_PROJECT,
				getProjectName(), "", altProjectName);
		helper.assertTclProject(altProjectName);
		operations.assertProjectElementExist(altProjectName, "");
	}

	/**
	 * Rename a project by context menu (DLTK-618)
	 * 
	 */
	public void testRenameProjectByContextMenu() {
		altProjectName = "RenamedProject";
		operations.renameElementByContextMenu(
				Operations.DLG_RENAME_SCRIPT_PROJECT, getProjectName(), "",
				altProjectName);
		helper.assertTclProject(altProjectName);
		operations.assertProjectElementExist(altProjectName, "");
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Delete
	//
	// //////////////////////////////////////////////////////////////////////////

	/**
	 * Delete a TCL file from project root (DLTK-548).<br>
	 * Delete a script from project root (DLTK-549)
	 * 
	 */
	public void testDeleteScriptFromRoot() {
		operations.deleteElement(getProjectName(), SCRIPT_IN_ROOT);
		helper.assertFileDoesNotExist(getProjectName(), SCRIPT_IN_ROOT);
		operations.assertProjectElementNotExist(getProjectName(),
				SCRIPT_IN_ROOT);
	}

	/**
	 * Delete a file from folder (DLTK-547)
	 * 
	 */
	public void testDeleteScriptFromFolder() {
		String path = FOLDER1 + "/" + SCRIPT1;
		operations.deleteElement(getProjectName(), path);

		helper.assertFolderExists(getProjectName(), FOLDER1);
		operations.assertProjectElementExist(getProjectName(), FOLDER1);

		helper.assertFileDoesNotExist(getProjectName(), path);
		operations.assertProjectElementNotExist(getProjectName(), path);
	}

	/**
	 * Delete file from source folder (DLTK-546)
	 * 
	 */
	public void testDeleteScriptFromSourceFolder() {
		String path = SOURCE_FOLDER1 + "/" + SCRIPT_IN_SOURCE_FOLDER;
		operations.deleteElement(getProjectName(), path);

		helper.assertSourceFolderExists(project, SOURCE_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1);

		helper.assertFileDoesNotExist(getProjectName(), path);
		operations.assertProjectElementNotExist(getProjectName(), path);
	}

	/**
	 * Delete folder (DLTK-545)
	 * 
	 */
	public void testDeleteFolder() {
		operations.deleteElement(getProjectName(), FOLDER1);
		helper.assertFolderDoesNotExist(project, FOLDER1);
		operations.assertProjectElementNotExist(getProjectName(), FOLDER1);
	}

	/**
	 * Delete nested folder (DLTK-545)
	 * 
	 */
	public void testDeleteNestedFolder() {
		operations.deleteElement(getProjectName(), NESTED_FOLDER2);

		helper.assertFolderExists(getProjectName(), NESTED_FOLDER);
		helper.assertFolderDoesNotExist(project, NESTED_FOLDER2);

		operations.assertProjectElementExist(getProjectName(), NESTED_FOLDER);
		operations.assertProjectElementNotExist(getProjectName(),
				NESTED_FOLDER2);
	}

	/**
	 * Delete source folder (DLTK-544)
	 * 
	 */
	public void testDeleteSourceFolder() {
		operations.deleteElement(getProjectName(), SOURCE_FOLDER1);
		helper.assertSourceFolderDoesNotExist(project, SOURCE_FOLDER1);
		operations.assertProjectElementNotExist(getProjectName(),
				SOURCE_FOLDER1);
	}

	/**
	 * Delete a TCL file from project root (DLTK-548).<br>
	 * Delete a script from project root (DLTK-549)
	 * 
	 */
	public void testDeleteScriptFromRootByContextMenu() {
		operations.deleteElementByContextMenu(getProjectName(), SCRIPT_IN_ROOT);
		helper.assertFileDoesNotExist(getProjectName(), SCRIPT_IN_ROOT);
		operations.assertProjectElementNotExist(getProjectName(),
				SCRIPT_IN_ROOT);
	}

	/**
	 * Delete a file from folder (DLTK-547)
	 * 
	 */
	public void testDeleteScriptFromFolderByContextMenu() {
		String path = FOLDER1 + "/" + SCRIPT1;
		operations.deleteElementByContextMenu(getProjectName(), path);

		helper.assertFolderExists(getProjectName(), FOLDER1);
		operations.assertProjectElementExist(getProjectName(), FOLDER1);

		helper.assertFileDoesNotExist(getProjectName(), path);
		operations.assertProjectElementNotExist(getProjectName(), path);
	}

	/**
	 * Delete file from source folder (DLTK-546)
	 * 
	 */
	public void testDeleteScriptFromSourceFolderByContextMenu() {
		String path = SOURCE_FOLDER1 + "/" + SCRIPT_IN_SOURCE_FOLDER;
		operations.deleteElementByContextMenu(getProjectName(), path);

		helper.assertSourceFolderExists(project, SOURCE_FOLDER1);
		operations.assertProjectElementExist(getProjectName(), SOURCE_FOLDER1);

		helper.assertFileDoesNotExist(getProjectName(), path);
		operations.assertProjectElementNotExist(getProjectName(), path);
	}

	/**
	 * Delete folder (DLTK-545)
	 * 
	 */
	public void testDeleteFolderByContextMenu() {
		operations.deleteElementByContextMenu(getProjectName(), FOLDER1);
		helper.assertFolderDoesNotExist(project, FOLDER1);
		operations.assertProjectElementNotExist(getProjectName(), FOLDER1);
	}

	/**
	 * Delete nested folder (DLTK-545)
	 * 
	 */
	public void testDeleteNestedFolderByContextMenu() {
		operations.deleteElementByContextMenu(getProjectName(), NESTED_FOLDER2);

		helper.assertFolderExists(getProjectName(), NESTED_FOLDER);
		helper.assertFolderDoesNotExist(project, NESTED_FOLDER2);

		operations.assertProjectElementExist(getProjectName(), NESTED_FOLDER);
		operations.assertProjectElementNotExist(getProjectName(),
				NESTED_FOLDER2);
	}

	/**
	 * Delete source folder (DLTK-544)
	 * 
	 */
	public void testDeleteSourceFolderByContextMenu() {
		operations.deleteElementByContextMenu(getProjectName(), SOURCE_FOLDER1);
		helper.assertSourceFolderDoesNotExist(project, SOURCE_FOLDER1);
		operations.assertProjectElementNotExist(getProjectName(),
				SOURCE_FOLDER1);
	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Move
	//
	// //////////////////////////////////////////////////////////////////////////

}
