package org.eclipse.dltk.tcl.core.indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.tcl.indexing.DLTKEFSTclIndexer;
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
	boolean recursive = false;
	boolean help = false;
	boolean version = false;
	boolean verbose = true;
	boolean force = false;

	public Object start(IApplicationContext context) throws Exception {
		Map arguments = context.getArguments();
		String[] appArgs = (String[]) arguments
				.get(IApplicationContext.APPLICATION_ARGS);
		List<File> foldersToRebuildIndex = new ArrayList<File>();
		for (String arg : appArgs) {
			if ("-r".equals(arg) || "-recursive".equals(arg)
					|| "--recursive".equals(arg)) {
				recursive = true;
				continue;
			}
			if ("-f".equals(arg) || "-force".equals(arg)
					|| "--force".equals(arg)) {
				force = true;
				continue;
			}
			if ("-q".equals(arg) || "-quiet".equals(arg)
					|| "--quiet".equals(arg)) {
				verbose = false;
				continue;
			}
			if ("-h".equals(arg) || "-help".equals(arg) || "--h".equals(arg)) {
				help = true;
				break;
			}
			if ("-v".equals(arg) || "-version".equals(arg)
					|| "--version".equals(arg)) {
				version = true;
				break;
			}
			File f = new File(arg);
			foldersToRebuildIndex.add(f);
		}
		if (help) {
			return helpMsg();
		}
		if (version) {
			return versionMsg();
		}

		DLTKEFSTclIndexer indexer = new DLTKEFSTclIndexer() {
			@Override
			public void logBeginOfFolder(IFileStore folder) {
				if (verbose) {
					System.out.println("Building index file for folder:"
							+ folder.toURI().getPath());
				}
			}

			@Override
			public void logEntry(IFileStore indexFile, long filesSize) {
				if (verbose) {
					System.out
							.println("Indexing of folder is done: Original size:"
									+ filesSize
									+ " Index size:"
									+ indexFile.fetchInfo().getLength());
				}
			}

			@Override
			protected void reportUnknownError(IFileStore folder) {
				if (verbose) {
					System.out.println("ERROR: Failed to index folder:"
							+ folder.toURI().getPath());
				}
			}

			@Override
			protected void logIndexConsistent(IFileStore folder) {
				if (verbose) {
					System.out.println("Folder indexes are consistent:"
							+ folder.toURI().getPath());
				}
			}

			@Override
			public void logReadonlyFolder(IFileStore folder) {
				if (verbose) {
					System.out.println("Skipping readonly folder:"
							+ folder.toURI().getPath());
				}
			}

			@Override
			public boolean isForceRebuild() {
				return force;
			}
		};
		if (!foldersToRebuildIndex.isEmpty()) {
			if (verbose) {
				printHeader();
			}
			for (File path : foldersToRebuildIndex) {
				if (path.exists() && path.isDirectory() && path.canWrite()) {
					indexer.buildIndexFor(EFS.getLocalFileSystem().getStore(
							new Path(path.getAbsolutePath())), recursive);
				} else {
					if (verbose) {
						System.out.println("Path:" + path.getAbsolutePath()
								+ " is rejected for indexing.");
					}
				}
			}
			if (verbose) {
				System.out.println("Total indexed resources size:"
						+ indexer.getTotalSize());
				System.out.println("Basic indexes size:"
						+ indexer.getBasicIndexesSize());
				System.out.println("AST indexes size:"
						+ indexer.getASTIndexesSize());
				System.out.println("Total indexes size:"
						+ indexer.getTotalIndexesSize());
			}
		} else {
			helpMsg();
		}

		return IApplication.EXIT_OK;
	}

	private void printHeader() {
		System.out.println("DLTK TCL Offline index creation tool");
	}

	private Object versionMsg() {
		System.out.println("DLTK Tcl Offline index creation tool version:"
				+ DLTKTclIndexer.VERSION);
		return IApplication.EXIT_OK;
	}

	private Object helpMsg() {
		printHeader();

		System.out
				.println("Usage: dltk_tcl_indexer [options] folder1 ... [folderN]");
		System.out.println("Options:");
		System.out
				.println("\t -r, -rerursive, --rerursive - Build index recursivly for any subfolder of specified folder.");
		System.out
				.println("\t -f, -force, --force - Forces rebuilding of already created indexes. In other way indexes will be rebuilded only if folder contain changes.");
		System.out
				.println("\t -v, -version, --version - Prints Version string");
		System.out.println("\t -h, -help, --help - This message");
		System.out
				.println("\t -q, -quiet, --quiet - Run in quiet mode. No output to console will be performed.");

		return IApplication.EXIT_OK;
	}

	public void stop() {
	}
}
