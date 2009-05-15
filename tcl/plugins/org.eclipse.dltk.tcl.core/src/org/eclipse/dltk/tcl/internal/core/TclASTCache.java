package org.eclipse.dltk.tcl.internal.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.IASTCache;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IExternalSourceModule;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.caching.IContentCache;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.internal.core.serialization.TclASTLoader;
import org.eclipse.dltk.tcl.internal.core.serialization.TclASTSaver;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;
import org.eclipse.dltk.tcl.internal.parser.TclSourceParserFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

public class TclASTCache implements IASTCache {
	public static final String TCL_AST_ATTRIBUTE = "_ast";

	public TclASTCache() {
	}

	public ASTCacheEntry restoreModule(ISourceModule module) {
		IFileHandle handle = EnvironmentPathUtils.getFile(module);
		if (handle == null) {
			return null;
		}
		IContentCache cache = ModelManager.getModelManager().getCoreCache();
		InputStream contentStream = cache.getCacheEntryAttribute(handle,
				"content");
		if (contentStream != null && module instanceof IExternalSourceModule) {
			ASTCacheEntry entry = new ASTCacheEntry();
			entry.problems = new ProblemCollector();
			char[] source = null;
			try {
				source = org.eclipse.dltk.compiler.util.Util
						.getInputStreamAsCharArray(contentStream, -1, null);
				TclSourceParserFactory fact = new TclSourceParserFactory();
				ISourceParser parser = fact.createSourceParser();
				entry.module = parser.parse(null, source, entry.problems);
				return entry;
			} catch (IOException e) {
			}
		}

		InputStream stream = cache.getCacheEntryAttribute(handle,
				TCL_AST_ATTRIBUTE);
		if (stream != null) {
			try {
				TclASTLoader loader = new TclASTLoader(stream);
				ProblemCollector collector = new ProblemCollector();
				TclModule tclModule = loader.getModule(collector);
				if (tclModule == null) {
					return null;
				}
				ASTCacheEntry entry = new ASTCacheEntry();
				NewTclSourceParser parser = new NewTclSourceParser();
				entry.problems = collector;
				entry.module = parser.parse(null, tclModule, null);
				if (entry.problems.isEmpty()) {
					entry.problems = null;
				}
				return entry;
			} catch (IOException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
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
		if (true) {
			return;
		}
		IFileHandle handle = EnvironmentPathUtils.getFile(module);
		if (handle == null) {
			return;
		}
		if (moduleDeclaration instanceof TclModuleDeclaration) {
			TclModuleDeclaration decl = (TclModuleDeclaration) moduleDeclaration;
			TclModule tclModule = decl.getTclModule();
			if (tclModule != null) {
				IContentCache cache = ModelManager.getModelManager()
						.getCoreCache();
				OutputStream stream = cache.getCacheEntryAttributeOutputStream(
						handle, TCL_AST_ATTRIBUTE);
				storeTclEntryInCache(problems, tclModule, stream);
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
			e.printStackTrace();
		}
	}
}
