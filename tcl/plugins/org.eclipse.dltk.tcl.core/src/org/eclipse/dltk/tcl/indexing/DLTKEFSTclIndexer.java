package org.eclipse.dltk.tcl.indexing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKContentTypeManager;
import org.eclipse.dltk.core.caching.ArchiveCacheIndexBuilder;
import org.eclipse.dltk.core.caching.ArchiveIndexContentChecker;
import org.eclipse.dltk.core.caching.MixinModelCollector;
import org.eclipse.dltk.core.caching.StructureModelCollector;
import org.eclipse.dltk.core.search.indexing.SourceIndexerRequestor;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.core.TclLanguageToolkit;
import org.eclipse.dltk.tcl.internal.core.TclASTCache;
import org.eclipse.dltk.tcl.internal.core.TclSourceIndexerRequestor;
import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinBuildVisitor;
import org.eclipse.dltk.tcl.internal.core.serialization.TclASTSaver;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;
import org.eclipse.dltk.tcl.internal.parser.TclSourceElementRequestVisitor;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.utils.TextUtils;

/**
 * @since 2.0
 */
public class DLTKEFSTclIndexer {
	private long totalSize = 0;
	private long totalIndexesSize = 0;
	private long totalASTIndexesSize = 0;
	public final static long VERSION = 200905291444l;

	public boolean isForceRebuild() {
		return false;
	}

	public void buildIndexFor(IFileStore folder, boolean recursive) {
		IFileInfo folderInfo = folder.fetchInfo();
		if (!folderInfo.isDirectory()) {
			return;
		}
		IFileStore[] files;
		try {
			files = folder.childStores(EFS.NONE, new NullProgressMonitor());
		} catch (CoreException e1) {
			e1.printStackTrace();
			return;
		}
		List<IFileStore> toIndex = new ArrayList<IFileStore>();
		for (IFileStore file : files) {
			IFileInfo fileInfo = file.fetchInfo();
			if (fileInfo.isDirectory() && recursive) {
				buildIndexFor(file, recursive);
			} else if (needIndexing(file)) {
				toIndex.add(file);
			}
		}
		boolean readonly = folderInfo.getAttribute(EFS.ATTRIBUTE_READ_ONLY);
		if (readonly) {
			logReadonlyFolder(folder);
		}
		if (!toIndex.isEmpty() && !readonly) {
			// Check required rebuild or not
			boolean buildRequired = isForceRebuild();
			IFileStore indexFile = folder.getChild(".dltk.index");
			IFileStore astIndexFile = folder.getChild(".dltk.index.ast");
			IFileInfo indexFileInfo = indexFile.fetchInfo();
			IFileInfo astIndexFileInfo = astIndexFile.fetchInfo();
			if (!buildRequired && !indexFileInfo.exists()) {
				buildRequired = true;
			}
			if (!buildRequired && !astIndexFileInfo.exists()) {
				buildRequired = true;
			}
			if (!buildRequired) {
				// Check for existing index files
				File indexFileLocal;
				try {
					indexFileLocal = indexFile.toLocalFile(EFS.CACHE,
							new NullProgressMonitor());
					ArchiveIndexContentChecker checker = new ArchiveIndexContentChecker(
							indexFileLocal, VERSION, TclLanguageToolkit
									.getDefault());
					if (checker.containChanges(indexFile)) {
						buildRequired = true;
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			if (!buildRequired) {
				File astIndexFileLocal;
				try {
					astIndexFileLocal = indexFile.toLocalFile(EFS.CACHE,
							new NullProgressMonitor());
					ArchiveIndexContentChecker astChecker = new ArchiveIndexContentChecker(
							astIndexFileLocal, VERSION, TclLanguageToolkit
									.getDefault());
					if (astChecker.containChanges(astIndexFile)) {
						buildRequired = true;
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			if (!buildRequired) {
				logIndexConsistent(folder);
				return;
			}
			logBeginOfFolder(folder);
			deleteIndexFiles(indexFile, astIndexFile);
			long filesSize = 0;
			try {
				ArchiveCacheIndexBuilder builder = new ArchiveCacheIndexBuilder(
						indexFile.openOutputStream(EFS.NONE,
								new NullProgressMonitor()), VERSION);
				ArchiveCacheIndexBuilder astIndexBuilder = new ArchiveCacheIndexBuilder(
						astIndexFile.openOutputStream(EFS.NONE,
								new NullProgressMonitor()), VERSION);
				for (IFileStore file : toIndex) {
					filesSize += indexFile(builder, astIndexBuilder, file);
				}
				builder.done();
				astIndexBuilder.done();
				logEntry(indexFile, filesSize);
				totalSize += filesSize;
				indexFileInfo = indexFile.fetchInfo();
				astIndexFileInfo = astIndexFile.fetchInfo();
				totalIndexesSize += indexFileInfo.getLength();
				totalASTIndexesSize += astIndexFileInfo.getLength();
			} catch (FileNotFoundException e) {
				// e.printStackTrace();
				deleteIndexFiles(indexFile, astIndexFile);
			} catch (IOException e) {
				// e.printStackTrace();
				deleteIndexFiles(indexFile, astIndexFile);
			} catch (Exception e) {
				reportUnknownError(folder);
				deleteIndexFiles(indexFile, astIndexFile);
			}
		}
	}

	public void logReadonlyFolder(IFileStore folder) {
	}

	private long indexFile(ArchiveCacheIndexBuilder builder,
			ArchiveCacheIndexBuilder astIndexBuilder, IFileStore file)
			throws CoreException, IOException, Exception {
		String content = new String(Util.getFileByteContent(file));

		ProblemCollector dltkProblems = new ProblemCollector();
		TclModule module = makeModule(content, dltkProblems);

		IFileInfo fileInfo = file.fetchInfo();
		long timestamp = fileInfo.getLastModified();
		if (fileInfo.getAttribute(EFS.ATTRIBUTE_SYMLINK)) {
			String target = fileInfo
					.getStringAttribute(EFS.ATTRIBUTE_LINK_TARGET);
			IFileStore linkTarget = file.getFileStore(new Path(target));
			IFileInfo info = linkTarget.fetchInfo();
			timestamp = info.getLastModified();
		}

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		TclASTSaver saver = new TclASTSaver(module, bout);
		saver.store(dltkProblems);
		astIndexBuilder.addEntry(file.getName(), timestamp,
				TclASTCache.TCL_AST_ATTRIBUTE, new ByteArrayInputStream(bout
						.toByteArray()));

		// Store indexing information.
		SourceIndexerRequestor req = new TclSourceIndexerRequestor();
		req.setIndexer(new NullIndexer());
		StructureModelCollector collector = new StructureModelCollector(req);
		NewTclSourceParser parser = new NewTclSourceParser();
		ModuleDeclaration ast = parser.parse(null, module, null);
		SourceElementRequestVisitor requestor = new TclSourceElementRequestVisitor(
				collector, null);
		ast.traverse(requestor);

		byte[] structure_index = collector.getBytes();
		builder.addEntry(file.getName(), timestamp,
				TclASTCache.TCL_STRUCTURE_INDEX, new ByteArrayInputStream(
						structure_index));

		// Store mixin index information.
		MixinModelCollector mixinCollector = new MixinModelCollector();
		TclMixinBuildVisitor mixinVisitor = new TclMixinBuildVisitor(ast, null,
				false, mixinCollector);
		ast.traverse(mixinVisitor);
		byte[] mixin_index = mixinCollector.getBytes();
		builder.addEntry(file.getName(), timestamp,
				TclASTCache.TCL_MIXIN_INDEX, new ByteArrayInputStream(
						mixin_index));
		return content.length();
	}

	private void deleteIndexFiles(IFileStore indexFile, IFileStore astIndexFile) {
		try {
			IFileInfo indexFileInfo = indexFile.fetchInfo();
			if (indexFileInfo.exists()) {
				indexFile.delete(EFS.NONE, new NullProgressMonitor());
			}
			IFileInfo astIndexFileInfo = astIndexFile.fetchInfo();
			if (astIndexFileInfo.exists()) {
				astIndexFile.delete(EFS.NONE, new NullProgressMonitor());
			}
		} catch (Exception e) {

		}
	}

	protected void logIndexConsistent(IFileStore folder) {
	}

	public void logBeginOfFolder(IFileStore folder) {
	}

	public void logEntry(IFileStore indexFile, long filesSize) {
	}

	protected void reportUnknownError(IFileStore folder) {
	}

	private TclModule makeModule(String content, ProblemCollector dltkProblems) {
		TclParser parser = new TclParser();
		TclErrorCollector collector = new TclErrorCollector();
		TclModule module = parser.parseModule(content, collector,
				DefinitionManager.getInstance().getCoreProcessor());
		if (dltkProblems != null) {
			collector.reportAll(dltkProblems, TextUtils
					.createLineTracker(content));
		}
		return module;
	}

	private boolean needIndexing(IFileStore file) {
		IFileInfo fileInfo = file.fetchInfo();
		if (fileInfo.isDirectory()) {
			return false;
		}
		return DLTKContentTypeManager.isValidFileNameForContentType(
				TclLanguageToolkit.getDefault(), file.getName());
	}

	public long getTotalSize() {
		return this.totalSize;
	}

	public long getTotalIndexesSize() {
		return this.totalIndexesSize + this.totalASTIndexesSize;
	}

	public long getBasicIndexesSize() {
		return this.totalIndexesSize;
	}

	public long getASTIndexesSize() {
		return this.totalASTIndexesSize;
	}

}
