package org.eclipse.dltk.tcl.indexing;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

public class DLTKTclIndexer {
	public final static long VERSION = DLTKEFSTclIndexer.VERSION;
	DLTKEFSTclIndexer realIndexer = new DLTKEFSTclIndexer() {
		public void logBeginOfFolder(
				org.eclipse.core.filesystem.IFileStore folder) {
			try {
				DLTKTclIndexer.this.logBeginOfFolder(folder.toLocalFile(
						EFS.NONE, new NullProgressMonitor()));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		};

		public void logEntry(org.eclipse.core.filesystem.IFileStore indexFile,
				long filesSize) {
			try {
				DLTKTclIndexer.this.logEntry(indexFile.toLocalFile(EFS.NONE,
						new NullProgressMonitor()), filesSize);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		};

		protected void logIndexConsistent(
				org.eclipse.core.filesystem.IFileStore folder) {
			try {
				DLTKTclIndexer.this.logIndexConsistent(folder.toLocalFile(
						EFS.NONE, new NullProgressMonitor()));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		};

		protected void reportUnknownError(
				org.eclipse.core.filesystem.IFileStore folder) {
			try {
				DLTKTclIndexer.this.reportUnknownError(folder.toLocalFile(
						EFS.NONE, new NullProgressMonitor()));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		};
	};

	/**
	 * @since 2.0
	 */
	public boolean isForceRebuild() {
		return realIndexer.isForceRebuild();
	}

	public void buildIndexFor(File folder, boolean recursive) {
		realIndexer.buildIndexFor(EFS.getLocalFileSystem().getStore(
				new Path(folder.getAbsolutePath())), recursive);
	}

	protected void logIndexConsistent(File folder) {
	}

	public void logBeginOfFolder(File folder) {
	}

	public void logEntry(File indexFile, long filesSize) {
	}

	protected void reportUnknownError(File folder) {
	}

	public long getTotalSize() {
		return realIndexer.getTotalSize();
	}

	public long getTotalIndexesSize() {
		return realIndexer.getTotalIndexesSize();
	}

	public long getBasicIndexesSize() {
		return realIndexer.getBasicIndexesSize();
	}

	public long getASTIndexesSize() {
		return realIndexer.getASTIndexesSize();
	}

}
