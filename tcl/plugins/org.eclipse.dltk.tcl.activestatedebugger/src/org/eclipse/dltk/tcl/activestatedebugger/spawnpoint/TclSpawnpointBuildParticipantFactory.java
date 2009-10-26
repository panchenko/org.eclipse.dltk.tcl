package org.eclipse.dltk.tcl.activestatedebugger.spawnpoint;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.builder.AbstractBuildParticipantType;
import org.eclipse.dltk.core.builder.IBuildParticipant;

public class TclSpawnpointBuildParticipantFactory extends
		AbstractBuildParticipantType {

	@Override
	public IBuildParticipant createBuildParticipant(IScriptProject project)
			throws CoreException {
		return new TclSpawnpointBuildParticipant(project.getProject());
	}

}
