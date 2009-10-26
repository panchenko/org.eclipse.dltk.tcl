package org.eclipse.dltk.tcl.indexing;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.TclVisitor;

public class SpawnpointCollector extends TclVisitor {
	public static class SpawnpointInfo {
		public Set<String> commands;
		public int charStart;
		public int charEnd;
	}

	private static final String PROC_COMMAND = "proc"; //$NON-NLS-1$

	private final IBuildContext buildContext;
	private final Set<String> spawnCommands;

	public SpawnpointCollector(IBuildContext buildContext,
			Set<String> spawnCommands) {
		this.buildContext = buildContext;
		this.spawnCommands = spawnCommands;
	}

	private ISourceLineTracker lineTracker;
	
	public void setLineTracker(ISourceLineTracker lineTracker) {
		this.lineTracker = lineTracker;
	}

	private StringArgument getStringArgument(TclCommand command, int index) {
		if (index < command.getArguments().size()) {
			TclArgument argument = command.getArguments().get(index);
			if (argument instanceof StringArgument) {
				return (StringArgument) argument;
			}
		}
		return null;
	}

	@Override
	public boolean visit(TclCommand command) {
		if (PROC_COMMAND.equals(command.getQualifiedName())
				&& command.getArguments().size() == 3) {
			final StringArgument procName = getStringArgument(command, 0);
			if (procName != null && spawnCommands.contains(procName.getValue())) {
				return false;
			}
		} else if (spawnCommands.contains(command.getQualifiedName())) {
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

	public final Map<Integer, SpawnpointInfo> spawnpoints = new HashMap<Integer, SpawnpointInfo>();

	private void addSpawnpoint(int lineNumber, String commandName, int start,
			int end) {
		Integer lineObj = Integer.valueOf(lineNumber);
		SpawnpointInfo info = spawnpoints.get(lineObj);
		if (info == null) {
			info = new SpawnpointInfo();
			info.commands = Collections.singleton(commandName);
			info.charStart = start;
			info.charEnd = end;
			spawnpoints.put(lineObj, info);
		} else {
			if (!info.commands.contains(commandName)) {
				final Set<String> commandNames = new HashSet<String>();
				commandNames.addAll(info.commands);
				commandNames.add(commandName);
				info.commands = commandNames;
			}
			if (start < info.charStart) {
				info.charStart = start;
			}
			if (end > info.charEnd) {
				info.charEnd = end;
			}
		}
	}
}