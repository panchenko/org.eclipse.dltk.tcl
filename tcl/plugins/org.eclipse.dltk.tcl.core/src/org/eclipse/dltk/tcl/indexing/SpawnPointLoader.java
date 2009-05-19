package org.eclipse.dltk.tcl.indexing;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.dltk.core.caching.AbstractDataLoader;
import org.eclipse.dltk.tcl.indexing.SpawnpointCollector.SpawnpointInfo;

public class SpawnPointLoader extends AbstractDataLoader {
	SpawnpointCollector collector;

	public SpawnPointLoader(InputStream input, SpawnpointCollector collector) {
		super(input);
		this.collector = collector;
	}

	public void process() throws IOException {
		readStrings();
		while (true) {
			try {
				int spawnPointsSize = in.readInt();
				for (int i = 0; i < spawnPointsSize; i++) {
					int key = in.readInt();
					int charEnd = in.readInt();
					int charStart = in.readInt();
					int cmds = in.readInt();
					Set<String> commands = new HashSet<String>();
					for (int j = 0; j < cmds; j++) {
						String str = readString();
						commands.add(str);
					}
					SpawnpointInfo info = new SpawnpointInfo();
					info.commands = commands;
					info.charStart = charStart;
					info.charEnd = charEnd;
					collector.spawnpoints.put(Integer.valueOf(key), info);
				}
			} catch (EOFException e) {
				break;

			} catch (IOException e) {
				break;
			}
		}
	}
}
