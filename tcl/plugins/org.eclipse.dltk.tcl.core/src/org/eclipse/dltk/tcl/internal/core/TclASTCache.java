package org.eclipse.dltk.tcl.internal.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.IASTCache;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.caching.IContentCache;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.internal.core.serialization.TclASTLoader;
import org.eclipse.dltk.tcl.internal.core.serialization.TclASTSaver;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

public class TclASTCache implements IASTCache {
	public static final String TCL_AST_ATTRIBUTE = "_ast";
	public static final String TCL_PKG_INFO = "_pinf";
	public static final String TCL_STRUCTURE_INDEX = IContentCache.STRUCTURE_INDEX;
	public static final String TCL_MIXIN_INDEX = IContentCache.MIXIN_INDEX;

	private class StoreEntry {
		ProblemCollector problems;
		IFileHandle handle;
		TclModule module;
	}

	List<StoreEntry> entriesToStore = new ArrayList<StoreEntry>();

	private Thread storeASTThread = new Thread() {
		public void run() {
			while (true) {
				synchronized (entriesToStore) {
					if (entriesToStore.isEmpty()) {
						try {
							entriesToStore.wait(100);
						} catch (InterruptedException e) {
							if (DLTKCore.DEBUG) {
								e.printStackTrace();
							}
						}
					} else {
						StoreEntry entry = entriesToStore.remove(0);
						IContentCache cache = ModelManager.getModelManager()
								.getCoreCache();
						OutputStream stream = cache
								.getCacheEntryAttributeOutputStream(
										entry.handle, TCL_AST_ATTRIBUTE);

						storeTclEntryInCache(entry.problems, entry.module,
								stream);
						try {
							stream.close();
						} catch (IOException e) {
						}
					}
				}
			}
		};
	};

	public TclASTCache() {
		storeASTThread.start();
	}

	public ASTCacheEntry restoreModule(ISourceModule module) {
		IFileHandle handle = EnvironmentPathUtils.getFile(module);
		if (handle == null) {
			return null;
		}
		IContentCache cache = ModelManager.getModelManager().getCoreCache();

		ProblemCollector collector = new ProblemCollector();
		TclModule tclModule = null;
		tclModule = restoreTclModuleFromCache(handle, cache, collector);
		if (tclModule != null) {
			ASTCacheEntry entry = new ASTCacheEntry();
			NewTclSourceParser parser = new NewTclSourceParser();
			entry.problems = collector;
			entry.module = parser.parse(null, tclModule, null);
			if (entry.problems.isEmpty()) {
				entry.problems = null;
			}
			return entry;
		}
		return null;
	}

	public static TclModule restoreTclModuleFromCache(IFileHandle handle,
			IContentCache cache, IProblemReporter collector) {
		InputStream stream = cache.getCacheEntryAttribute(handle,
				TCL_AST_ATTRIBUTE);
		if (stream != null) {
			try {
				TclASTLoader loader = new TclASTLoader(stream);
				TclModule tclModule = loader.getModule(collector);
				if (tclModule != null) {
					return tclModule;
				}
			} catch (Exception e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static EObject copy(EObject eObject) {
		Copier copier = new Copier(true, false);
		EObject result = copier.copy(eObject);
		copier.copyReferences();
		return result;
	}

	public void storeModule(ISourceModule module,
			ModuleDeclaration moduleDeclaration, ProblemCollector problems) {
		IFileHandle handle = EnvironmentPathUtils.getFile(module, false);
		if (handle == null) {
			return;
		}
		if (moduleDeclaration instanceof TclModuleDeclaration) {
			TclModuleDeclaration decl = (TclModuleDeclaration) moduleDeclaration;
			TclModule tclModule = decl.getTclModule();
			if (tclModule != null) {
				StoreEntry entry = new StoreEntry();
				entry.handle = handle;
				entry.module = tclModule;
				if (problems != null) {
					entry.problems = new ProblemCollector();
					problems.copyTo(entry.problems);
				}
				synchronized (entriesToStore) {
					entriesToStore.add(entry);
					entriesToStore.notifyAll();
				}
			}
		}
	}

	public static void storeTclEntryInCache(ProblemCollector problems,
			TclModule tclModule, OutputStream stream) {
		TclASTSaver saver;
		try {
			saver = new TclASTSaver(tclModule, stream);
			saver.store(problems);
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}
}
