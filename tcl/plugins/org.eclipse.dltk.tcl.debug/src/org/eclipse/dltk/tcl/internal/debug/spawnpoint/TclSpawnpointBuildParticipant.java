package org.eclipse.dltk.tcl.internal.debug.spawnpoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.debug.TclDebugPlugin;
import org.eclipse.dltk.tcl.internal.validators.TclBuildContext;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;

public class TclSpawnpointBuildParticipant implements IBuildParticipant {

	public static final String MARKER_TYPE = TclDebugPlugin.PLUGIN_ID
			+ ".spawnpoint";

	private final Set<String> spawnCommands = new HashSet<String>();

	public TclSpawnpointBuildParticipant() {
		spawnCommands.add("spawn");
	}

	private static class SpawnpointInfo {
		Set<String> commands;
		int charStart;
		int charEnd;
	}

	private static class SpawnpointCollector extends TclVisitor {

		private final IBuildContext buildContext;
		private final Set<String> spawnCommands;

		public SpawnpointCollector(IBuildContext buildContext,
				Set<String> spawnCommands) {
			this.buildContext = buildContext;
			this.spawnCommands = spawnCommands;
		}

		private ISourceLineTracker lineTracker;

		@Override
		public boolean visit(TclCommand command) {
			if (spawnCommands.contains(command.getQualifiedName())) {
				if (lineTracker == null) {
					lineTracker = buildContext.getLineTracker();
				}
				int lineNumber = lineTracker.getLineNumberOfOffset(command
						.getStart());
				addSpawnpoint(lineNumber, command.getQualifiedName(), command
						.getStart(), command.getEnd());
			}
			return true;
		}

		private final Map<Integer, SpawnpointInfo> spawnpoints = new HashMap<Integer, SpawnpointInfo>();

		private void addSpawnpoint(int lineNumber, String commandName,
				int start, int end) {
			Integer lineObj = Integer.valueOf(lineNumber);
			SpawnpointInfo info = spawnpoints.get(lineObj);
			if (info == null) {
				info = new SpawnpointInfo();
				info.commands = Collections.singleton(commandName);
				info.charStart = start;
				info.charEnd = end;
				spawnpoints.put(lineObj, info);
			} else {
				final Set<String> commandNames = new HashSet<String>();
				commandNames.addAll(info.commands);
				commandNames.add(commandName);
				info.commands = commandNames;
				if (start < info.charStart) {
					info.charStart = start;
				}
				if (end > info.charEnd) {
					info.charEnd = end;
				}
			}
		}
	}

	@Override
	public void build(IBuildContext context) throws CoreException {
		if (context.getBuildType() == IBuildContext.RECONCILE_BUILD) {
			return;
		}
		final IFile file = context.getFile();
		if (file == null) {
			return;
		}
		List<TclCommand> commands = TclBuildContext.getStatements(context);
		if (commands == null) {
			return;
		}
		SpawnpointCollector collector = new SpawnpointCollector(context,
				spawnCommands);
		TclParserUtils.traverse(commands, collector);
		if (!collector.spawnpoints.isEmpty()) {
			file.deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_ZERO);
			for (Map.Entry<Integer, SpawnpointInfo> entry : collector.spawnpoints
					.entrySet()) {
				System.out.println(entry.getKey() + ":"
						+ entry.getValue().commands.toString());
				IMarker marker = file.createMarker(MARKER_TYPE);
				marker.setAttributes(
						new String[] { IMarker.LINE_NUMBER, IMarker.CHAR_START,
								IMarker.CHAR_END, IMarker.MESSAGE },
						new Object[] { entry.getKey(),
								entry.getValue().charStart,
								entry.getValue().charEnd,
								entry.getValue().commands.toString() });
			}
		}
	}
}
