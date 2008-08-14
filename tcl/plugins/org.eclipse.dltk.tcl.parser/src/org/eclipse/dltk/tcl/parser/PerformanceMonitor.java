/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerformanceMonitor {
	Map<String, Long> values = new HashMap<String, Long>();
	Map<String, Long> operations = new HashMap<String, Long>();
	Map<String, Long> startTime = new HashMap<String, Long>();

	public static boolean PERFOMANCE_MONITORING_IS_ACTIVE = true;

	private static PerformanceMonitor mon = new PerformanceMonitor();

	public static PerformanceMonitor getDefault() {
		return mon;
	}

	public void begin(String id) {
		startTime.put(id, System.currentTimeMillis());
		if (!values.containsKey(id)) {
			values.put(id, 0L);
		}
	}

	public void end(String id) {
		long cur = System.currentTimeMillis();
		if (startTime.containsKey(id)) {
			long start = startTime.get(id);
			long ctime = values.get(id);
			values.put(id, ctime + (cur - start));
			incrOperations(id);
		}
	}

	private void incrOperations(String id) {
		if (operations.containsKey(id)) {
			operations.put(id, operations.get(id) + 1);
		} else {
			operations.put(id, 1L);
		}
	}

	public void print() {
		Set<String> okeys = this.values.keySet();
		List<String> keys = new ArrayList<String>();
		keys.addAll(okeys);
		java.util.Collections.sort(keys);
		for (String id : keys) {
			if (operations.containsKey(id) && operations.get(id) > 1) {
				System.out.println("(" + id + ") \t:" + to_(values.get(id))
						+ " /" + to_(values.get(id) / operations.get(id))
						+ " ops:" + operations.get(id));
			} else {
				System.out.println("(" + id + ") \t:" + to_(values.get(id)));
			}
		}
	}

	private String to_(long time) {
		return new Formatter().format("%d", time).toString();
	}

	public void add(String id, long value) {
		if (this.values.containsKey(id)) {
			long old = values.get(id);
			values.put(id, old + value);
			incrOperations(id);
		} else {
			this.values.put(id, value);
			incrOperations(id);
		}
	}
}
