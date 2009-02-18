package org.eclipse.dltk.tcl.internal.debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugException;
import org.eclipse.dltk.compiler.CharOperation;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.dbgp.commands.IDbgpExtendedCommands;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.debug.core.IHotCodeReplaceProvider;
import org.eclipse.dltk.debug.core.model.IScriptDebugTarget;
import org.eclipse.dltk.debug.core.model.IScriptThread;

public class TclHotCodeReplaceProvider implements IHotCodeReplaceProvider {

	public TclHotCodeReplaceProvider() {
	}

	public void performCodeReplace(IScriptDebugTarget target,
			IResource[] resources) throws DebugException {
		final IScriptThread[] threads = (IScriptThread[]) target.getThreads();
		if (threads.length > 0) {
			final IDbgpExtendedCommands extCmds = threads[0].getDbgpSession()
					.getExtendedCommands();
			for (int i = 0; i < resources.length; ++i) {
				final String code = getResourceReplacementCode(resources[i]);
				if (code != null) {
					try {
						extCmds.evaluate(code);
					} catch (DbgpException e) {
						TclDebugPlugin.log(e);
					}
				}
			}
		}
	}

	private String getResourceReplacementCode(IResource resource)
			throws DebugException {
		final ISourceModule module = (ISourceModule) DLTKCore
				.create((IFile) resource);
		if (module == null) {
			return null;
		}
		final List<IMethod> procList = new ArrayList<IMethod>();
		try {
			module.accept(new IModelElementVisitor() {

				public boolean visit(IModelElement element) {
					if (element.getElementType() == IModelElement.METHOD) {
						procList.add((IMethod) element);
						return false;
					}
					return true;
				}

			});
			if (procList.isEmpty()) {
				return null;
			}
			final char[] input = org.eclipse.dltk.internal.core.util.Util
					.getResourceContentsAsCharArray((IFile) resource);
			final StringBuffer sb = new StringBuffer();
			for (final IMethod method : procList) {
				final String[] types = collectNamespaces(method);
				if (types != null) {
					for (int j = 0; j < types.length; ++j) {
						sb.append("namespace eval " + types[j] + " {\n"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					sb.append("proc "); //$NON-NLS-1$
					sb.append(method.getElementName());
					final ISourceRange nameRange = method.getNameRange();
					final int nameEnd = nameRange.getLength()
							+ nameRange.getOffset();
					final ISourceRange sourceRange = method.getSourceRange();
					sb.append(input, nameEnd, sourceRange.getOffset()
							+ sourceRange.getLength() - nameEnd);
					sb.append("\n"); //$NON-NLS-1$
					for (int j = 0; j < types.length; ++j) {
						sb.append("}\n"); //$NON-NLS-1$
					}
				}
			}
			if (sb.length() == 0) {
				return null;
			}
			return sb.toString();
		} catch (ModelException e) {
			TclDebugPlugin.log(e);
			return null;
		}
	}

	/**
	 * @param method
	 * @return
	 */
	private String[] collectNamespaces(IMethod method) {
		final List<String> types = new ArrayList<String>();
		IModelElement parent = method.getParent();
		while (parent.getElementType() == IModelElement.TYPE) {
			types.add(parent.getElementName());
			parent = parent.getParent();
		}
		if (parent.getElementType() != IModelElement.SOURCE_MODULE) {
			return null;
		}
		if (types.isEmpty()) {
			return CharOperation.NO_STRINGS;
		}
		Collections.reverse(types);
		return types.toArray(new String[types.size()]);
	}
}
