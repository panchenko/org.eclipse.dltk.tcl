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

import net.sf.swtbot.SWTBotTestCase;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;
import net.sf.swtbot.wait.Conditions;
import net.sf.swtbot.widgets.SWTBotCheckBox;
import net.sf.swtbot.widgets.SWTBotShell;
import net.sf.swtbot.widgets.SWTBotText;
import net.sf.swtbot.widgets.SWTBotTreeItem;
import net.sf.swtbot.widgets.TimeoutException;
import net.sf.swtbot.widgets.WidgetNotFoundException;

import org.eclipse.dltk.ui.tests.swtbot.complex.ProjectContentTests;
import org.eclipse.dltk.ui.wizards.Messages;

public class ProjectContentOperations extends Operations {

	private static final String FLD_FILE = createLabel(Messages.NewSourceModulePage_file);

	public ProjectContentOperations(SWTEclipseBot bot) {
		super(bot);
	}

	/**
	 * Create a folder (DLTK-517).
	 * 
	 * @see ProjectContentTests#testCreateFolder001()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param folderName
	 */
	public void createFolder001(String projectName, String folderPath,
			String folderName) {
		try {
			navigateToProjectElement(projectName, folderPath);
			getBot().menu(MENU_FILE).menu(MENU_NEW).menu(MENU_NEW_FOLDER)
					.click();
			internalCreateFolder(null, null, folderName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a folder (DLTK-517).
	 * 
	 * @see ProjectContentTests#testCreateFolder002()
	 * @see ProjectContentTests#testCreateFolder003()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param folderName
	 */
	public void createFolder002(String projectName, String folderPath,
			String folderName) {
		try {
			getBot().menu(MENU_FILE).menu(MENU_NEW).menu(MENU_NEW_FOLDER)
					.click();
			internalCreateFolder(projectName, folderPath, folderName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create folder by context menu (DLTK-604).
	 * 
	 * @see ProjectContentTests#testCreateFolderByContextMenu()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param folderName
	 */
	public void createFolderByContextMenu(String projectName,
			String folderPath, String folderName) {
		try {
			SWTBotTreeItem projectBot = navigateToProjectElement(projectName,
					folderPath);
			clickContextSubMenu(projectBot, MENU_NEW, MENU_NEW_FOLDER);
			internalCreateFolder(null, null, folderName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a source folder (DLTK-516).
	 * 
	 * @see ProjectContentTests#testCreateSourceFolder001()
	 * @see ProjectContentTests#testCreateSourceFolder002()
	 * @see ProjectContentTests#testCreateSourceFolder003()
	 * @see ProjectContentTests#testCreateNestedSourceFolder()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param folderName
	 * @param exclusion
	 */
	public void createSourceFolder(String projectName, String folderPath,
			String folderName, boolean exclusion) {
		try {
			navigateToProjectElement(projectName, folderPath);
			getBot().menu(MENU_FILE).menu(MENU_NEW)
					.menu(MENU_NEW_SOURCE_FOLDER).click();
			internalCreateSourceFolder(projectName, folderName, exclusion);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create source folder using context menu (DLTK-603).
	 * 
	 * @see ProjectContentTests#testCreateSourceFolderByContextMenu001()
	 * @see ProjectContentTests#testCreateSourceFolderByContextMenu002()
	 * @see ProjectContentTests#testCreateSourceFolderByContextMenu003()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param folderName
	 * @param exclusion
	 */
	public void createSourceFolderByContextMenu(String projectName,
			String folderPath, String folderName, boolean exclusion) {
		try {
			SWTBotTreeItem treeBot = navigateToProjectElement(projectName,
					folderPath);
			clickContextSubMenu(treeBot, MENU_NEW, MENU_NEW_SOURCE_FOLDER);
			internalCreateSourceFolder(projectName, folderName, exclusion);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a TCL file (DLTK-518).
	 * 
	 * @see ProjectContentTests#testCreateFile001()
	 * 
	 * @param projectName
	 * @param fileName
	 */
	public void createFile001(String projectName, String fileName) {
		try {
			getProjectItem(projectName);
			getBot().menu(MENU_FILE).menu(MENU_NEW).menu(MENU_NEW_FILE).click();
			internalCreateFile(null, null, fileName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a TCL file (DLTK-518).<br>
	 * Create a Tcl file in source folder (DLTK-519).<br>
	 * Create a TCL file in folder. Folder not in build path. (DLTK-520)
	 * 
	 * @see ProjectContentTests#testCreateFile002()
	 * @see ProjectContentTests#testCreateFileInSourceFolder()
	 * @see ProjectContentTests#testCreateFileInFolder()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param fileName
	 */
	public void createFile002(String projectName, String folderPath,
			String fileName) {
		try {
			getBot().menu(MENU_FILE).menu(MENU_NEW).menu(MENU_NEW_FILE).click();
			internalCreateFile(projectName, folderPath, fileName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a Tcl file in project root using pop-up menu (DLTK-607).<br>
	 * Create a Tcl file in source folder by context menu (DLTK-609).<br>
	 * Create a TCL file in folder by context menu (DLTK-611)
	 * 
	 * @see ProjectContentTests#testCreateFileByContextMenu()
	 * @see ProjectContentTests#testCreateFileInSourceFolderByContextMenu()
	 * @see ProjectContentTests#testCreateFileInFolderByContextMenu()
	 * 
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param fileName
	 */
	public void createFileByContextMenu(String projectName, String folderPath,
			String fileName) {
		try {
			SWTBotTreeItem projectBot = getProjectItem(projectName);
			// projectBot.contextMenu(MENU_NEW).menu(MENU_NEW_FILE).click();
			clickContextSubMenu(projectBot, MENU_NEW, MENU_NEW_FILE);
			internalCreateFile(projectName, folderPath, fileName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a script in project root (DLTK-521).<br>
	 * Create a script in folder (DLTK-606).<br>
	 * Create a script in source folder (DLTK-605).
	 * 
	 * @see ProjectContentTests#testCreateScript001()
	 * @see ProjectContentTests#testCreateScript002()
	 * @see ProjectContentTests#testCreateScriptInFolder()
	 * @see ProjectContentTests#testCreateScriptInSourceFolder()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param scriptName
	 */
	public void createScript(String projectName, String folderPath,
			String scriptName) {
		try {
			getProjectItem(projectName);
			getBot().menu(MENU_FILE).menu(MENU_NEW).menu(MENU_TCL_FILE).click();
			internalCreateScript(scriptName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a script by context menu (DLTK-608).<br>
	 * Create a script in folder by context menu (DLTK-612)<br>
	 * Create a script in source folder by context menu (DLTK-610)
	 * 
	 * @see ProjectContentTests#testCreateScriptByContextMenu()
	 * @see ProjectContentTests#testCreateScriptInFolderByContextMenu()
	 * @see ProjectContentTests#testCreateScriptInSourceFolderByContextMenu()
	 * 
	 * @param projectName
	 * @param folderPath
	 * @param scriptName
	 */
	public void createScriptByContextMenu(String projectName,
			String folderPath, String scriptName) {
		try {
			SWTBotTreeItem projectBot = getProjectItem(projectName);
			// projectBot.contextMenu(MENU_NEW).menu(MENU_TCL_FILE).click();
			clickContextSubMenu(projectBot, MENU_NEW, MENU_TCL_FILE);
			internalCreateScript(scriptName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Copy file from a folder to another one (DLTK-523).<br>
	 * Copy files from project root to a folder (DLTK-712).<br>
	 * Copy file from a folder to project root (DLTK-524).<br>
	 * Copy script from source folder to project root (DLTK-527).<br>
	 * Copy files from a source folder to a source folder (DLTK-526).<br>
	 * Copy files from a source folder to a folder (DLTK-525).<br>
	 * Copy files from a folder to a source folder (DLTK-522).
	 * 
	 * @see ProjectContentTests#testCopyFileBetweenFolders()
	 * @see ProjectContentTests#testCopyFileToFolderFromProjectRoot()
	 * @see ProjectContentTests#testCopyFileFromFolderToProjectRoot()
	 * @see ProjectContentTests#testCopyScriptFromSourceFolderToProjectRoot()
	 * @see ProjectContentTests#testCopyScriptBetweenSourceFolders()
	 * @see ProjectContentTests#testCopyFileFromSourceFolderToFolder()
	 * @see ProjectContentTests#testCopyScriptFromFolderToSourceFolder()
	 * 
	 * @param projectName
	 * @param path
	 */
	public void copyElement(String projectName, String path) {
		try {
			navigateToProjectElement(projectName, path);
			getBot().menu(MENU_EDIT).menu(MENU_COPY).click();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Copy file from a folder to another one (DLTK-523).<br>
	 * Copy files from project root to a folder (DLTK-712).<br>
	 * Copy file from a folder to project root (DLTK-524).<br>
	 * Copy script from source folder to project root (DLTK-527).<br>
	 * Copy files from a source folder to a source folder (DLTK-526).<br>
	 * Copy files from a source folder to a folder (DLTK-525).<br>
	 * Copy files from a folder to a source folder (DLTK-522).
	 * 
	 * @see ProjectContentTests#testCopyFileBetweenFolders()
	 * @see ProjectContentTests#testCopyFileToFolderFromProjectRoot()
	 * @see ProjectContentTests#testCopyFileFromFolderToProjectRoot()
	 * @see ProjectContentTests#testCopyScriptFromSourceFolderToProjectRoot()
	 * @see ProjectContentTests#testCopyScriptBetweenSourceFolders()
	 * @see ProjectContentTests#testCopyFileFromSourceFolderToFolder()
	 * @see ProjectContentTests#testCopyScriptFromFolderToSourceFolder()
	 * 
	 * @param projectName
	 * @param path
	 */
	public void pasteElement(String projectName, String path) {
		try {
			navigateToProjectElement(projectName, path);
			getBot().menu(MENU_EDIT).menu(MENU_PASTE).click();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Copy file from a folder to another one (DLTK-523).<br>
	 * Copy files from project root to a folder (DLTK-712).<br>
	 * Copy file from a folder to project root (DLTK-524).<br>
	 * Copy script from source folder to project root (DLTK-527).<br>
	 * Copy files from a source folder to a source folder (DLTK-526).<br>
	 * Copy files from a source folder to a folder (DLTK-525).<br>
	 * Copy files from a folder to a source folder (DLTK-522).
	 * 
	 * @see ProjectContentTests#testCopyFileBetweenFoldersByContextMenu()
	 * @see ProjectContentTests#testCopyFileToFolderFromProjectRootByContextMenu()
	 * @see ProjectContentTests#testCopyFileFromFolderToProjectRootByContextMenu()
	 * @see ProjectContentTests#testCopyScriptFromSourceFolderToProjectRootByContextMenu()
	 * @see ProjectContentTests#testCopyScriptBetweenSourceFoldersByContextMenu()
	 * @see ProjectContentTests#testCopyFileFromSourceFolderToFolderByContextMenu()
	 * @see ProjectContentTests#testCopyScriptFromFolderToSourceFolderByContextMenu()
	 * 
	 * @param projectName
	 * @param path
	 */
	public void copyElementByContextMenu(String projectName, String path) {
		try {
			SWTBotTreeItem item = navigateToProjectElement(projectName, path);
			item.contextMenu(MENU_COPY).click();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Copy file from a folder to another one (DLTK-523).<br>*
	 * Copy files from project root to a folder (DLTK-712).<br>
	 * Copy file from a folder to project root (DLTK-524).<br>
	 * Copy script from source folder to project root (DLTK-527).<br>
	 * Copy files from a source folder to a source folder (DLTK-526).<br>
	 * Copy files from a source folder to a folder (DLTK-525).<br>
	 * Copy files from a folder to a source folder (DLTK-522).
	 * 
	 * @see ProjectContentTests#testCopyFileBetweenFoldersByContextMenu()
	 * @see ProjectContentTests#testCopyFileToFolderFromProjectRootByContextMenu()
	 * @see ProjectContentTests#testCopyFileFromFolderToProjectRootByContextMenu()
	 * @see ProjectContentTests#testCopyScriptFromSourceFolderToProjectRootByContextMenu()
	 * @see ProjectContentTests#testCopyScriptBetweenSourceFoldersByContextMenu()
	 * @see ProjectContentTests#testCopyFileFromSourceFolderToFolderByContextMenu()
	 * @see ProjectContentTests#testCopyScriptFromFolderToSourceFolderByContextMenu()
	 * 
	 * @param projectName
	 * @param path
	 */
	public void pasteElementByContextMenu(String projectName, String path) {
		try {
			SWTBotTreeItem item = navigateToProjectElement(projectName, path);
			item.contextMenu(MENU_PASTE).click();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Rename a folder (DLTK-538).<br>
	 * Rename a source folder (DLTK-539).<br>
	 * Rename Tcl file in project root (DLTK-543).<br>
	 * Rename a Tcl file in folder (DLTK-542).<br>
	 * Rename a Tcl file in source folder (DLTK-541).<br>
	 * Rename script in project root (DLTK-537).<br>
	 * Rename a project (DLTK-540).
	 * 
	 * @see ProjectContentTests#testRenameFolder()
	 * @see ProjectContentTests#testRenameSourceFolder()
	 * @see ProjectContentTests#testRenameFile()
	 * @see ProjectContentTests#testRenameFileInFolder()
	 * @see ProjectContentTests#testRenameFileInSourceFolder()
	 * @see ProjectContentTests#testRenameScript()
	 * @see ProjectContentTests#testRenameProject()
	 * 
	 * @param projectName
	 * @param path
	 * @param newName
	 */
	public void renameElement(String dialogName, String projectName,
			String path, String newName) {
		try {
			navigateToProjectElement(projectName, path);
			getBot().menu(MENU_REFACTOR).menu(MENU_RENAME).click();
			internalRename(dialogName, newName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Rename a folder using context menu (DLTK-613)<br>
	 * Rename a source folder using context menu (DLTK-617).<br>
	 * Rename a Tcl file in project root by context menu (DLTK-622).<br>
	 * Rename a Tcl file in folder by context menu (DLTK-621).<br>
	 * Rename a Tcl File in source folder by context menu (DLTK-620).<br>
	 * Rename script in project root using popup-menu (DLTK-714).<br>
	 * Rename a project by context menu (DLTK-618).
	 * 
	 * @see ProjectContentTests#testRenameFolderByContextMenu()
	 * @see ProjectContentTests#testRenameSourceFolderByContextMenu()
	 * @see ProjectContentTests#testRenameFileByContextMenu()
	 * @see ProjectContentTests#testRenameFileInFolderByContextMenu()
	 * @see ProjectContentTests#testRenameFileInSourceFolderByContextMenu()
	 * @see ProjectContentTests#testRenameScriptByContextMenu()
	 * @see ProjectContentTests#testRenameProjectByContextMenu()
	 * 
	 * @param projectName
	 * @param path
	 * @param newName
	 */
	public void renameElementByContextMenu(String dialogName,
			String projectName, String path, String newName) {
		try {
			SWTBotTreeItem item = navigateToProjectElement(projectName, path);
			// item.contextMenu(MENU_REFACTOR).menu(MENU_RENAME).click();
			clickContextSubMenu(item, MENU_REFACTOR, MENU_RENAME);
			internalRename(dialogName, newName);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete a TCL file from project root (DLTK-548).<br>
	 * Delete a script from project root (DLTK-549).<br>
	 * Delete a file from folder (DLTK-547).<br>
	 * Delete file from source folder (DLTK-546).<br>
	 * Delete folder (DLTK-545).<br>
	 * Delete source folder (DLTK-544).
	 * 
	 * @see ProjectContentTests#testDeleteScriptFromRoot()
	 * @see ProjectContentTests#testDeleteScriptFromFolder()
	 * @see ProjectContentTests#testDeleteScriptFromSourceFolder()
	 * @see ProjectContentTests#testDeleteFolder()
	 * @see ProjectContentTests#testDeleteNestedFolder()
	 * @see ProjectContentTests#testDeleteSourceFolder()
	 * 
	 * @param projectName
	 * @param path
	 */
	public void deleteElement(String projectName, String path) {
		try {
			navigateToProjectElement(projectName, path);
			getBot().menu(MENU_EDIT).menu(MENU_DELETE).click();
			internalDeleteElement();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete a TCL file from project root (DLTK-548).<br>
	 * Delete a script from project root (DLTK-549).<br>
	 * Delete a file from folder (DLTK-547).<br>
	 * Delete file from source folder (DLTK-546).<br>
	 * Delete folder (DLTK-545).<br>
	 * Delete source folder (DLTK-544).
	 * 
	 * @see ProjectContentTests#testDeleteScriptFromRootByContextMenu()
	 * @see ProjectContentTests#testDeleteScriptFromFolderByContextMenu()
	 * @see ProjectContentTests#testDeleteScriptFromSourceFolderByContextMenu()
	 * @see ProjectContentTests#testDeleteFolderByContextMenu()
	 * @see ProjectContentTests#testDeleteNestedFolderByContextMenu()
	 * @see ProjectContentTests#testDeleteSourceFolderByContextMenu()
	 * 
	 * @param projectName
	 * @param path
	 */
	public void deleteElementByContextMenu(String projectName, String path) {
		try {
			SWTBotTreeItem item = navigateToProjectElement(projectName, path);
			item.contextMenu(MENU_DELETE).click();
			internalDeleteElement();
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	// /////////////////////////////////////////////////////////////////////
	//
	// Internal
	//
	// /////////////////////////////////////////////////////////////////////
	private void internalCreateFolder(String project, String path, String name)
			throws WidgetNotFoundException, TimeoutException {
		getBot().waitUntil(Conditions.shellIsActive(DLG_NEW_FOLDER));
		SWTBotShell shell = getBot().shell(DLG_NEW_FOLDER);

		SWTBotTreeItem treeBot = null;
		if (project != null) {
			treeBot = getBot().tree().getTreeItem(project);
			treeBot.select();

			if (path != null) {
				navigateToProjectElement(treeBot, path);
			}
		}

		SWTBotText folderName = getBot().textWithLabel("Folder name:");
		folderName.setText(name);

		getBot().button(WIZARD_FINISH).click();
		getBot().waitUntil(Conditions.shellCloses(shell));
	}

	private void internalCreateSourceFolder(String projectName,
			String folderPath, boolean exclusion)
			throws WidgetNotFoundException, TimeoutException {
		getBot().waitUntil(Conditions.shellIsActive(DLG_NEW_SOURCE_FOLDER));
		SWTBotShell shell = getBot().shell(DLG_NEW_SOURCE_FOLDER);

		SWTBotText txtProjectName = getBot().textWithLabel("Project name:");
		SWTBotTestCase.assertText(projectName, txtProjectName);

		SWTBotTestCase.assertNotEnabled(getBot().button(WIZARD_FINISH));

		SWTBotText folderName = getBot().textWithLabel("Folder name:");
		folderName.setText(folderPath);

		if (exclusion) {
			SWTBotCheckBox check = getBot()
					.checkBox(
							"Update exclusion filters in other source folders to solve nesting");
			if (!check.isChecked())
				check.click();
		}
		getBot().button(WIZARD_FINISH).click();
		getBot().waitUntil(Conditions.shellCloses(shell));
	}

	private void internalCreateFile(String project, String path, String name)
			throws WidgetNotFoundException, TimeoutException {
		getBot().waitUntil(Conditions.shellIsActive(DLG_NEW_FILE));
		SWTBotShell shell = getBot().shell(DLG_NEW_FILE);

		SWTBotTreeItem treeBot = null;
		if (project != null) {
			treeBot = getBot().tree().getTreeItem(project);
			treeBot.select();

			if (path != null) {
				navigateToProjectElement(treeBot, path);
			}
		}

		SWTBotText fileName = getBot().textWithLabel("File name:");
		fileName.setText(name);
		getBot().button(WIZARD_FINISH).click();
		getBot().waitUntil(Conditions.shellCloses(shell));
	}

	private void internalCreateScript(String scriptName)
			throws WidgetNotFoundException, TimeoutException {
		getBot().waitUntil(Conditions.shellIsActive(DLG_CREATE_TCL_FILE));
		SWTBotShell shell = getBot().shell(DLG_CREATE_TCL_FILE);

		SWTBotText fileName = getBot().textWithLabel(FLD_FILE);
		fileName.setText(scriptName);
		getBot().button(WIZARD_FINISH).click();
		getBot().waitUntil(Conditions.shellCloses(shell));
	}

	private void internalRename(String dialogName, String newName)
			throws WidgetNotFoundException, TimeoutException {
		getBot().waitUntil(Conditions.shellIsActive(dialogName));
		SWTBotShell shell = getBot().shell(dialogName);
		SWTBotText fileName = getBot().textWithLabel("New name:");
		fileName.setText(newName);
		getBot().button("OK").click();
		getBot().waitUntil(Conditions.shellCloses(shell));
	}

	private void internalDeleteElement() throws WidgetNotFoundException,
			TimeoutException {
		getBot().waitUntil(Conditions.shellIsActive(DLG_CONFIRM_DELETE));
		SWTBotShell shell = getBot().shell(DLG_CONFIRM_DELETE);
		getBot().button("Yes").click();
		getBot().waitUntil(Conditions.shellCloses(shell));
	}
}
