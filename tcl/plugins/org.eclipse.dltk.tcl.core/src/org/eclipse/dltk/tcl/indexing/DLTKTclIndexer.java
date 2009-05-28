package org.eclipse.dltk.tcl.indexing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKContentTypeManager;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.caching.ArchiveCacheIndexBuilder;
import org.eclipse.dltk.core.caching.MixinModelCollector;
import org.eclipse.dltk.core.caching.StructureModelCollector;
import org.eclipse.dltk.core.search.indexing.SourceIndexerRequestor;
import org.eclipse.dltk.internal.core.SourceModuleStructureRequestor;
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

public class DLTKTclIndexer {
	private long totalSize = 0;
	private long totalIndexesSize = 0;
	private long totalASTIndexesSize = 0;

	public void buildIndexFor(File folder, boolean recursive) {
		if (!folder.isDirectory()) {
			return;
		}
		File[] files = folder.listFiles();
		List<File> toIndex = new ArrayList<File>();
		for (File file : files) {
			if (file.isDirectory() && recursive) {
				buildIndexFor(file, recursive);
			} else if (needIndexing(file)) {
				toIndex.add(file);
			}
		}
		if (!toIndex.isEmpty()) {
			logBeginOfFolder(folder);
			File indexFile = new File(folder, ".dltk.index");
			if (indexFile.exists()) {
				indexFile.delete();
			}
			File astIndexFile = new File(folder, ".dltk.index.ast");
			if (astIndexFile.exists()) {
				astIndexFile.delete();
			}
			long filesSize = 0;
			try {
				ArchiveCacheIndexBuilder builder = new ArchiveCacheIndexBuilder(
						new FileOutputStream(indexFile));
				ArchiveCacheIndexBuilder astIndexBuilder = new ArchiveCacheIndexBuilder(
						new FileOutputStream(astIndexFile));
				for (File file : toIndex) {
					String content = new String(Util.getFileByteContent(file));
					filesSize += content.length();

					ProblemCollector dltkProblems = new ProblemCollector();
					TclModule module = makeModule(content, dltkProblems);

					File canonicalFile = file.getCanonicalFile();
					long timestamp = file.lastModified();
					if (!canonicalFile.getAbsolutePath().equals(
							file.getAbsolutePath())) {
						// This is symlink
						timestamp = canonicalFile.lastModified();
					}

					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					TclASTSaver saver = new TclASTSaver(module, bout);
					saver.store(dltkProblems);
					astIndexBuilder.addEntry(file.getName(), timestamp,
							TclASTCache.TCL_AST_ATTRIBUTE,
							new ByteArrayInputStream(bout.toByteArray()));

					// Store indexing information.
					SourceIndexerRequestor req = new TclSourceIndexerRequestor();
					req.setIndexer(new NullIndexer());
					StructureModelCollector collector = new StructureModelCollector(
							req);
					NewTclSourceParser parser = new NewTclSourceParser();
					ModuleDeclaration ast = parser.parse(null, module, null);
					SourceElementRequestVisitor requestor = new TclSourceElementRequestVisitor(
							collector, null);
					try {
						ast.traverse(requestor);
					} catch (Exception e) {
						e.printStackTrace();
					}
					byte[] structure_index = collector.getBytes();
					builder.addEntry(file.getName(), timestamp,
							TclASTCache.TCL_STRUCTURE_INDEX,
							new ByteArrayInputStream(structure_index));

					// Store mixin index information.
					MixinModelCollector mixinCollector = new MixinModelCollector();
					TclMixinBuildVisitor mixinVisitor = new TclMixinBuildVisitor(
							ast, null, false, mixinCollector);
					try {
						ast.traverse(mixinVisitor);
					} catch (Exception e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}
					byte[] mixin_index = mixinCollector.getBytes();
					builder.addEntry(file.getName(), timestamp,
							TclASTCache.TCL_MIXIN_INDEX,
							new ByteArrayInputStream(mixin_index));
				}
				builder.done();
				astIndexBuilder.done();
				logEntry(indexFile, filesSize);
				totalSize += filesSize;
				totalIndexesSize += indexFile.length();
				totalASTIndexesSize += astIndexFile.length();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void logBeginOfFolder(File folder) {
		// System.out.println("Building index file for folder:"
		// + folder.getAbsolutePath());
	}

	public void logEntry(File indexFile, long filesSize) {
		// System.out.println("Indexing of folder is done: Original size:"
		// + filesSize + " Index size:" + indexFile.length());
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

	private boolean needIndexing(File file) {
		if (!file.isFile()) {
			return false;
		}
		return DLTKContentTypeManager.isValidFileNameForContentType(
				TclLanguageToolkit.getDefault(), new Path(file
						.getAbsolutePath()));
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
