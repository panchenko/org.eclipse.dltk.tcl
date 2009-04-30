package org.eclipse.dltk.tcl.internal.core.sources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IProjectFragmentTimestamp;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.WorkingCopyOwner;
import org.eclipse.dltk.internal.core.MementoModelElementUtil;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.Openable;
import org.eclipse.dltk.internal.core.OpenableElementInfo;
import org.eclipse.dltk.internal.core.ScriptProject;
import org.eclipse.dltk.internal.core.util.MementoTokenizer;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.dltk.utils.CorePrinter;

public class TclSourcesFragment extends Openable implements IProjectFragment,
		IProjectFragmentTimestamp {
	public static final IPath PATH = new Path(IBuildpathEntry.BUILDPATH_SPECIAL
			+ "sources#");
	private IPath currentPath;

	public TclSourcesFragment(ScriptProject project) {
		super(project);
		this.currentPath = PATH.append(project.getElementName()).append("@");
	}

	public String getElementName() {
		return currentPath.toString();
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TclSourcesFragment))
			return false;

		TclSourcesFragment other = (TclSourcesFragment) o;
		return this.currentPath.equals(other.currentPath)
				&& this.parent.equals(other.parent);
	}

	protected boolean buildStructure(OpenableElementInfo info,
			IProgressMonitor pm, Map newElements, IResource underlyingResource)
			throws ModelException {
		List children = new ArrayList();

		children.add(new TclSourcesElement(this));

		info.setChildren((IModelElement[]) children
				.toArray(new IModelElement[children.size()]));
		return true;
	}

	public Object createElementInfo() {
		return new OpenableElementInfo();
	}

	public IModelElement getHandleFromMemento(String token,
			MementoTokenizer memento, WorkingCopyOwner owner) {
		switch (token.charAt(0)) {
		case JEM_SCRIPTFOLDER:
			if (!memento.hasMoreTokens())
				return this;
			String classFileName = memento.nextToken();
			ModelElement classFile = (ModelElement) getScriptFolder(classFileName);
			return classFile.getHandleFromMemento(memento, owner);
		case JEM_USER_ELEMENT:
			return MementoModelElementUtil.getHandleFromMemento(memento, this,
					owner);
		}
		return null;
	}

	protected char getHandleMementoDelimiter() {
		return JEM_USER_ELEMENT;
	}

	public void printNode(CorePrinter output) {
	}

	public void copy(IPath destination, int updateResourceFlags,
			int updateModelFlags, IBuildpathEntry sibling,
			IProgressMonitor monitor) throws ModelException {
	}

	public IScriptFolder createScriptFolder(String name, boolean force,
			IProgressMonitor monitor) throws ModelException {
		return null;
	}

	public void delete(int updateResourceFlags, int updateModelFlags,
			IProgressMonitor monitor) throws ModelException {
	}

	public Object[] getForeignResources() throws ModelException {
		return new Object[0];
	}

	public int getKind() throws ModelException {
		return IProjectFragment.K_SOURCE;
	}

	public IBuildpathEntry getRawBuildpathEntry() throws ModelException {
		// return DLTKCore.newSpecialEntry(getPath(), false, true);
		return null;
	}

	public IScriptFolder getScriptFolder(IPath path) {
		if (path.segmentCount() != 1) {
			return null;
		}
		String name = path.segment(0);
		return getScriptFolder(name);
	}

	public IScriptFolder getScriptFolder(String name) {
		try {
			IModelElement[] elements = getChildren();
			for (int i = 0; i < elements.length; i++) {
				if (elements[i].getElementName().equals(name)) {
					return (IScriptFolder) elements[i];
				}
			}
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean isArchive() {
		return false;
	}

	public boolean isBuiltin() {
		return false;
	}

	public boolean exists() {
		return true;
	}

	public boolean isExternal() {
		return true;
	}

	public void move(IPath destination, int updateResourceFlags,
			int updateModelFlags, IBuildpathEntry sibling,
			IProgressMonitor monitor) throws ModelException {
	}

	public int getElementType() {
		return IModelElement.PROJECT_FRAGMENT;
	}

	public IPath getPath() {
		return currentPath;
	}

	public IResource getResource() {
		return null;
	}

	public long getTimeStamp() {
		// We need to collect all sourced items here.
		long hash = 0;
		List<TclModuleInfo> modules = TclPackagesManager
				.getProjectModules(getScriptProject().getElementName());
		for (TclModuleInfo tclModuleInfo : modules) {
			for (TclSourceEntry info : tclModuleInfo.getSourced()) {
				if (info.getValue() != null) {
					hash += (hash * 31 + info.getValue().hashCode()) >> 2;
				}
			}
			for (UserCorrection correction : tclModuleInfo
					.getSourceCorrections()) {
				if (correction.getOriginalValue() != null) {
					hash += (hash * 29 + correction.getOriginalValue()
							.hashCode()) >> 2;
				}
				if (correction.getUserValue() != null) {
					hash += (hash * 29 + correction.getUserValue().hashCode()) >> 2;
				}
			}
		}
		return hash;
	}

	public boolean containChildrens() {
		IInterpreterInstall install = null;
		try {
			install = ScriptRuntime.getInterpreterInstall(getScriptProject());
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
		if (install == null) {
			return false;
		}
		Set<IPath> sources = new HashSet<IPath>();
		TclSourcesUtils.fillSources(install, getScriptProject(), sources);
		return !sources.isEmpty();
	}
}
