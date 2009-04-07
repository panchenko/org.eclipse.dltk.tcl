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
package org.eclipse.dltk.tcl.activestatedebugger.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.core.internal.environment.LocalEnvironment;
import org.eclipse.dltk.tcl.activestatedebugger.InstrumentationSetup;

public class InstrumentationSetupTests extends TestCase {

	private static class Setup extends InstrumentationSetup {

		public Setup() {
			super(LocalEnvironment.getInstance());
		}

		@Override
		public void addFileHandle(IFileHandle file, boolean isDirectory,
				boolean include) {
			super.addFileHandle(file, isDirectory, include);
		}

		public Set<IPath> getRoots() {
			Set<IPath> result = new HashSet<IPath>();
			for (Entry e : root.children.values()) {
				result.add(e.getPath());
			}
			return result;
		}

	}

	private static class File implements IFileHandle {

		private final IPath path;
		private final Set<IPath> paths;

		public File(String path, Set<IPath> paths) {
			this(new Path(path), paths);
		}

		/**
		 * @param path
		 * @param paths
		 */
		public File(IPath path, Set<IPath> paths) {
			this.path = path;
			this.paths = paths;
		}

		public boolean exists() {
			return true;
		}

		public String getCanonicalPath() {
			return path.toString();
		}

		public IFileHandle getChild(String path) {
			IPath child = this.path.append(path);
			if (paths.contains(child)) {
				return new File(child, paths);
			}
			return null;
		}

		public IFileHandle[] getChildren() {
			final Map<IPath, IFileHandle> children = new HashMap<IPath, IFileHandle>();
			for (IPath p : paths) {
				if (path.isPrefixOf(p)
						&& p.segmentCount() > path.segmentCount()) {
					final IPath effective = p.removeLastSegments(p
							.segmentCount()
							- path.segmentCount() - 1);
					if (!children.containsKey(effective)) {
						children.put(effective, new File(effective, paths));
					}
				}
			}
			return children.values().toArray(new IFileHandle[children.size()]);
		}

		public IEnvironment getEnvironment() {
			return LocalEnvironment.getInstance();
		}

		public String getEnvironmentId() {
			return getEnvironment().getId();
		}

		public IPath getFullPath() {
			return EnvironmentPathUtils.getFullPath(getEnvironment(), path);
		}

		public String getName() {
			return path.lastSegment();
		}

		public IFileHandle getParent() {
			if (path.segmentCount() > 1) {
				return new File(path.removeLastSegments(1), paths);
			}
			return null;
		}

		public IPath getPath() {
			return path;
		}

		public boolean isDirectory() {
			return paths.contains(path.removeLastSegments(1));
		}

		public boolean isFile() {
			return !isDirectory();
		}

		public boolean isSymlink() {
			return false;
		}

		public long lastModified() {
			return 0;
		}

		public long length() {
			return 0;
		}

		public InputStream openInputStream(IProgressMonitor monitor)
				throws IOException {
			return null;
		}

		public OutputStream openOutputStream(IProgressMonitor monitor)
				throws IOException {
			return null;
		}

		public String toString() {
			return path.toString();
		}

		public String toOSString() {
			return path.toOSString();
		}

		public URI toURI() {
			return path.toFile().toURI();
		}

	}

	public void testABC() {
		Set<IPath> paths = new HashSet<IPath>();
		paths.add(new Path("A/B/C1")); //$NON-NLS-1$
		paths.add(new Path("A/B/C2")); //$NON-NLS-1$
		paths.add(new Path("A/B/C3")); //$NON-NLS-1$
		Setup setup = new Setup();
		setup.addFileHandle(new File("A/B", paths), true, false); //$NON-NLS-1$
		setup.addFileHandle(new File("A/B/C2", paths), false, true); //$NON-NLS-1$
		Set<IPath> roots = setup.getRoots();
		assertEquals(1, roots.size());
		assertTrue(roots.contains(new Path("A"))); //$NON-NLS-1$
		//
		IPath[] excludes = setup.getExcludes();
		assertEquals(2, excludes.length);
		assertEquals(new Path("A/B/C1"), excludes[0]); //$NON-NLS-1$
		assertEquals(new Path("A/B/C3"), excludes[1]); //$NON-NLS-1$
		//
		IPath[] includes = setup.getIncludes();
		assertEquals(1, includes.length);
		assertEquals(new Path("A/B/C2"), includes[0]); //$NON-NLS-1$		
	}

	public void testAB() {
		Set<IPath> paths = new HashSet<IPath>();
		paths.add(new Path("A/B/C1")); //$NON-NLS-1$
		paths.add(new Path("A/B/C2")); //$NON-NLS-1$
		paths.add(new Path("A/B/C3")); //$NON-NLS-1$
		Setup setup = new Setup();
		setup.addFileHandle(new File("A/B", paths), true, false); //$NON-NLS-1$
		IPath[] excludes = setup.getExcludes();
		assertEquals(1, excludes.length);
		assertEquals(new Path("A/B"), excludes[0]); //$NON-NLS-1$
		assertEquals(0, setup.getIncludes().length);
	}

	public void testXYABC() {
		Set<IPath> paths = new HashSet<IPath>();
		paths.add(new Path("X/Y/A/B/C1")); //$NON-NLS-1$
		paths.add(new Path("X/Y/A/B/C2")); //$NON-NLS-1$
		paths.add(new Path("X/Y/A/B/C3")); //$NON-NLS-1$
		Setup setup = new Setup();
		setup.addFileHandle(new File("X/Y/A/B", paths), true, false); //$NON-NLS-1$
		setup.addFileHandle(new File("X/Y/A/B/C2", paths), false, true); //$NON-NLS-1$
		Set<IPath> roots = setup.getRoots();
		assertEquals(1, roots.size());
		// FIXME probably it should return X/Y/A/B
		assertTrue(roots.contains(new Path("X"))); //$NON-NLS-1$
		//
		IPath[] excludes = setup.getExcludes();
		assertEquals(2, excludes.length);
		assertEquals(new Path("X/Y/A/B/C1"), excludes[0]); //$NON-NLS-1$
		assertEquals(new Path("X/Y/A/B/C3"), excludes[1]); //$NON-NLS-1$
		//
		IPath[] includes = setup.getIncludes();
		assertEquals(1, includes.length);
		assertEquals(new Path("X/Y/A/B/C2"), includes[0]); //$NON-NLS-1$		
	}

}
