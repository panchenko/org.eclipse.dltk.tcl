package org.eclipse.dltk.tcl.activestatedebugger.spawnpoint;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerConstants;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.indexing.SpawnpointCollector;
import org.eclipse.dltk.tcl.indexing.SpawnpointCollector.SpawnpointInfo;
import org.eclipse.dltk.tcl.internal.validators.TclBuildContext;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.osgi.util.NLS;

public class TclSpawnpointBuildParticipant implements IBuildParticipant {

	private final Set<String> spawnCommands = new HashSet<String>();

	public TclSpawnpointBuildParticipant() {
		spawnCommands.addAll(SpawnpointCommandManager.loadFromPreferences()
				.getSelectedCommands());
	}

	public void build(IBuildContext context) throws CoreException {
		if (context.getBuildType() == IBuildContext.RECONCILE_BUILD) {
			return;
		}
		final IFile file = context.getFile();
		if (file == null) {
			return;
		}
		ISourceModule module = context.getSourceModule();
		// if (module.getResource() == null) {
		// return;
		// }
		if (spawnCommands.isEmpty()) {
			return;
		}
		TclModule tclModule = TclBuildContext.getStatements(context);
		List<TclCommand> commands = tclModule.getStatements();
		if (commands == null) {
			return;
		}
		SpawnpointCollector collector = new SpawnpointCollector(context,
				spawnCommands);
		TclParserUtils.traverse(commands, collector);
		file.deleteMarkers(
				TclActiveStateDebuggerConstants.SPAWNPOINT_MARKER_TYPE, true,
				IResource.DEPTH_ZERO);
		if (!collector.spawnpoints.isEmpty()) {
			for (Map.Entry<Integer, SpawnpointInfo> entry : collector.spawnpoints
					.entrySet()) {
				final IMarker marker = file
						.createMarker(TclActiveStateDebuggerConstants.SPAWNPOINT_MARKER_TYPE);
				final SpawnpointInfo info = entry.getValue();
				marker.setAttributes(
						new String[] { IMarker.LINE_NUMBER, IMarker.CHAR_START,
								IMarker.CHAR_END, IMarker.MESSAGE },
						new Object[] { entry.getKey(), info.charStart,
								info.charEnd, buildMessage(info) });
			}
		}
	}

	/**
	 * @param info
	 * @return
	 */
	private String buildMessage(SpawnpointInfo info) {
		return NLS
				.bind(
						TclSpawnpointMessages.participantMarkerMessage_template,
						TextUtils.join(info.commands, ','),
						info.commands.size() == 1 ? TclSpawnpointMessages.participantMarkerMessage_commandSingular
								: TclSpawnpointMessages.participantMarkerMessage_commandPlurar);
	}
}
