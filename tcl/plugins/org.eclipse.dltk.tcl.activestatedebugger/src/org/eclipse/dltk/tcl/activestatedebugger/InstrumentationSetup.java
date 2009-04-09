/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.activestatedebugger;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.IExternalSourceModule;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;

public class InstrumentationSetup {

	protected static class Entry {
		final IPath path;
		final IFileHandle fileHandle;
		boolean include;
		boolean exclude;
		Boolean leaf;
		public final Map<IPath, Entry> children = new HashMap<IPath, Entry>();
		public boolean directory;

		public Entry(IPath path, IFileHandle fileHandle) {
			this.path = path;
			this.fileHandle = fileHandle;
		}

		public IPath getPath() {
			return path;
		}

		public String toString() {
			return path.toString() + (include ? "[include]" : "") //$NON-NLS-1$ //$NON-NLS-2$
					+ (exclude ? "[exclude]" : ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	final IEnvironment environment;
	protected final Map<IPath, Entry> entries = new HashMap<IPath, Entry>();
	protected final Entry root = new Entry(Path.ROOT, null);

	public InstrumentationSetup(IEnvironment environment) {
		this.environment = environment;
	}

	/**
	 * @param pattern
	 * @return
	 */
	public void addWorkspace(IPath path, boolean isDirectory, boolean include) {
		final IResource resource = InstrumentationUtils.getWorkspaceRoot()
				.findMember(path);
		if (resource != null) {
			final URI uri = resource.getLocationURI();
			final IFileHandle file = environment.getFile(uri);
			addFileHandle(file, isDirectory, include);
		}
	}

	/**
	 * @param environment
	 * @param pattern
	 * @return
	 */
	public void addExternal(IPath path, boolean isDirectory, boolean include) {
		addFileHandle(environment.getFile(EnvironmentPathUtils
				.getLocalPath(path)), isDirectory, include);
	}

	protected void addFileHandle(IFileHandle file, boolean isDirectory,
			boolean include) {
		if (file != null) {
			IPath path = file.getPath();
			Entry entry = entries.get(path);
			if (entry != null) {
				entry.leaf = Boolean.valueOf(include);
				if (include) {
					entry.include = true;
				} else {
					entry.exclude = true;
				}
				return;
			}
			entry = new Entry(path, file);
			entry.leaf = Boolean.valueOf(include);
			entries.put(path, entry);
			entry.directory = true;
			if (include) {
				entry.include = true;
			} else {
				entry.exclude = true;
			}
			for (;;) {
				file = file.getParent();
				if (file == null) {
					root.children.put(entry.getPath(), entry);
					break;
				}
				path = file.getPath();
				Entry dirEntry = entries.get(path);
				if (dirEntry == null) {
					dirEntry = new Entry(path, file);
					entries.put(path, dirEntry);
				}
				dirEntry.children.put(entry.getPath(), entry);
				dirEntry.directory = true;
				if (include) {
					dirEntry.include = true;
				} else {
					dirEntry.exclude = true;
				}
				entry = dirEntry;
			}
		}
	}

	public static class PatternEntry {
		private final IPath path;
		private final boolean directory;
		private final boolean include;

		public PatternEntry(IPath path, boolean directory, boolean include) {
			this.path = path;
			this.directory = directory;
			this.include = include;
		}

		public boolean isInclude() {
			return include;
		}

		public String getPatternText() {
			return directory ? path.toString() + "/*" : path.toString(); //$NON-NLS-1$
		}

		@Override
		public String toString() {
			return (include ? "+" : "-") + getPatternText(); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	private static void walk(Entry entry, List<PatternEntry> patterns) {
		if (entry.fileHandle == null || entry.leaf == null) {
			for (Entry e : entry.children.values()) {
				walk(e, patterns);
			}
		} else if (entry.include && entry.exclude) {
			final IFileHandle[] children = entry.fileHandle.getChildren();
			for (IFileHandle child : children) {
				final IPath path = child.getPath();
				final Entry childEntry = entry.children.get(path);
				if (childEntry == null) {
					patterns.add(new PatternEntry(path, child.isDirectory(),
							entry.leaf.booleanValue()));
				} else {
					walk(childEntry, patterns);
				}
			}
		} else if (entry.exclude) {
			patterns.add(new PatternEntry(entry.getPath(), entry.directory,
					false));
		} else if (entry.include) {
			patterns.add(new PatternEntry(entry.getPath(), entry.directory,
					true));
		}
	}

	public IPath[] getExcludes() {
		final List<PatternEntry> patterns = new ArrayList<PatternEntry>();
		walk(root, patterns);
		Collections.sort(patterns, createPatternEntryComparator());
		final List<IPath> result = new ArrayList<IPath>();
		for (PatternEntry entry : patterns) {
			if (!entry.include) {
				result.add(entry.path);
			}
		}
		return result.toArray(new IPath[result.size()]);
	}

	public IPath[] getIncludes() {
		final List<PatternEntry> patterns = new ArrayList<PatternEntry>();
		walk(root, patterns);
		Collections.sort(patterns, createPatternEntryComparator());
		final List<IPath> result = new ArrayList<IPath>();
		for (PatternEntry entry : patterns) {
			if (entry.include) {
				result.add(entry.path);
			}
		}
		return result.toArray(new IPath[result.size()]);
	}

	private static Comparator<PatternEntry> createPatternEntryComparator() {
		return new Comparator<PatternEntry>() {
			public int compare(PatternEntry o1, PatternEntry o2) {
				return o1.path.toString().compareTo(o2.path.toString());
			}
		};
	}

	public void send(ActiveStateInstrumentCommands commands)
			throws DbgpException {
		final List<PatternEntry> patterns = new ArrayList<PatternEntry>();
		walk(root, patterns);
		Collections.sort(patterns, createPatternEntryComparator());
		for (PatternEntry entry : patterns) {
			if (entry.include) {
				commands.instrumentInclude(entry.getPatternText());
			} else {
				commands.instrumentExclude(entry.getPatternText());
			}
		}
	}

	/**
	 * @param module
	 */
	public void addSourceModule(ISourceModule module, boolean include) {
		if (module instanceof IExternalSourceModule) {
			if (!module.isBuiltin())
				return;
			addExternal(module.getPath(), false, include);
		} else {
			addWorkspace(module.getPath(), false, include);
		}
	}

	/**
	 * @param folder
	 * @param include
	 */
	public void addScriptFolder(IScriptFolder folder, boolean include) {
		if (folder.isReadOnly()) {
			addExternal(folder.getPath(), true, include);
		} else {
			addWorkspace(folder.getPath(), true, include);
		}
	}

	/**
	 * @param fragment
	 * @param include
	 */
	public void addProjectFragment(IProjectFragment fragment, boolean include) {
		if (fragment.isExternal()) {
			if (fragment.isBuiltin())
				return;
			addExternal(fragment.getPath(), true, include);
		} else {
			addWorkspace(fragment.getPath(), true, include);
		}
	}

	/**
	 * @param project
	 * @param include
	 */
	public void addProject(IScriptProject project, boolean include) {
		addWorkspace(project.getPath(), true, include);
	}
}
