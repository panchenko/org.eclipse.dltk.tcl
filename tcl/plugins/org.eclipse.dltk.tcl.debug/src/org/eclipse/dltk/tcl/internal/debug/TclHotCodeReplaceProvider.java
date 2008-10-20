package org.eclipse.dltk.tcl.internal.debug;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugException;
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
		try {
			return new String(org.eclipse.dltk.internal.core.util.Util
					.getResourceContentsAsCharArray((IFile) resource));
		} catch (ModelException e) {
			TclDebugPlugin.log(e);
			return null;
		}
	}

}
