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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PerformanceMonitor {

	private final Map<String, Entry> entries = new HashMap<String, Entry>();

	private static class Entry {
		long value;
		long operations;
		long startTime;
	}

	public static final boolean PERFOMANCE_MONITORING_IS_ACTIVE = true;

	private static final PerformanceMonitor mon = new PerformanceMonitor();

	public static PerformanceMonitor getDefault() {
		return mon;
	}

	public synchronized void begin(String id) {
		Entry entry = entries.get(id);
		if (entry == null) {
			entry = new Entry();
			entries.put(id, entry);
		}
		entry.startTime = System.currentTimeMillis();
	}

	public synchronized void end(String id) {
		final long cur = System.currentTimeMillis();
		Entry entry = entries.get(id);
		if (entry != null) {
			entry.value += cur - entry.startTime;
			++entry.operations;
		}
	}

	public void print() {
		final Map<String, Entry> copy;
		synchronized (this) {
			copy = new HashMap<String, Entry>(entries);
		}
		final String[] keys = copy.keySet().toArray(new String[copy.size()]);
		Arrays.sort(keys);
		for (String id : keys) {
			Entry entry = copy.get(id);
			if (entry.operations > 1) {
				System.out.println("(" + id + ") \t:" + entry.value + " /"
						+ (entry.value / entry.operations) + " ops:"
						+ entry.operations);
			} else {
				System.out.println("(" + id + ") \t:" + entry.value);
			}
		}
	}

	public synchronized void add(String id, long value) {
		Entry entry = entries.get(id);
		if (entry == null) {
			entry = new Entry();
			entries.put(id, entry);
		}
		entry.value += value;
		++entry.operations;
	}
}
