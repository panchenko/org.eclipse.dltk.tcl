package org.eclipse.dltk.tcl.indexing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.caching.AbstractDataSaver;
import org.eclipse.dltk.tcl.indexing.SpawnpointCollector.SpawnpointInfo;

public class SpawnPointSaver extends AbstractDataSaver {

	public SpawnPointSaver(SpawnpointCollector collector) throws IOException {
		super(new ByteArrayOutputStream());
		out.writeInt(collector.spawnpoints.size());
		for (Map.Entry<Integer, SpawnpointInfo> entry : collector.spawnpoints
				.entrySet()) {
			final SpawnpointInfo info = entry.getValue();
			out.writeInt(entry.getKey().intValue());
			out.writeInt(info.charEnd);
			out.writeInt(info.charStart);
			int cmds = info.commands.size();
			out.writeInt(cmds);
			for (String cmd : info.commands) {
				writeString(cmd);
			}
		}
	}

	public byte[] getBytes() {
		try {
			stream.flush();
			storeStringIndex();
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return ((ByteArrayOutputStream) stream).toByteArray();
	}
}
