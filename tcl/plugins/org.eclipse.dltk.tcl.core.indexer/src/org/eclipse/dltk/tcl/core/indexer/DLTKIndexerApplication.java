package org.eclipse.dltk.tcl.core.indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.indexing.DLTKTclIndexer;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * This class controls all aspects of the application's execution
 */
public class DLTKIndexerApplication implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("DLTK TCL Offline index cretion tool");
		Map arguments = context.getArguments();
		String[] appArgs = (String[]) arguments
				.get(IApplicationContext.APPLICATION_ARGS);
		boolean recursive = false;
		List<File> foldersToRebuildIndex = new ArrayList<File>();
		for (String arg : appArgs) {
			if ("-r".equals(arg)) {
				recursive = true;
				continue;
			}
			File f = new File(arg);
			if (f.exists() && f.isDirectory()) {
				System.out.println("Path:" + f.getAbsolutePath()
						+ " is accepted for indexing...");
				foldersToRebuildIndex.add(f.getAbsoluteFile());
			} else {
				System.out.println("Path:" + f.getAbsolutePath()
						+ " is rejected for indexing.");
			}
		}
		DLTKTclIndexer indexer = new DLTKTclIndexer() {
			public void logBeginOfFolder(File folder) {
				System.out.println("Building index file for folder:"
						+ folder.getAbsolutePath());
			}

			public void logEntry(File indexFile, long filesSize) {
				System.out.println("Indexing of folder is done: Original size:"
						+ filesSize + " Index size:" + indexFile.length());
			}

		};
		for (File path : foldersToRebuildIndex) {
			indexer.buildIndexFor(path, recursive);
		}

		System.out.println("Total indexed resources size:"
				+ indexer.getTotalSize());
		System.out.println("Basic indexes size:"
				+ indexer.getBasicIndexesSize());
		System.out.println("AST indexes size:" + indexer.getASTIndexesSize());
		System.out.println("Total indexes size:"
				+ indexer.getTotalIndexesSize());

		return IApplication.EXIT_OK;
	}

	public void stop() {
	}
}
