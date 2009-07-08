package org.eclipse.dltk.tcl.internal.core.packages;

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
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.utils.CorePrinter;

public class TclPackageFragment extends Openable implements IProjectFragment,
		IProjectFragmentTimestamp {
	public static final IPath PATH = new Path(IBuildpathEntry.BUILDPATH_SPECIAL
			+ "packages#");
	private IPath currentPath;
	private String packageName;

	protected TclPackageFragment(ScriptProject project, String packageName) {
		super(project);
		this.packageName = packageName;
		this.currentPath = PATH.append("@").append(packageName);
	}

	public String getElementName() {
		return "Packages@" + packageName;
	}

	/**
	 * @since 2.0
	 */
	public String getPackageName() {
		return packageName;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TclPackageFragment))
			return false;

		TclPackageFragment other = (TclPackageFragment) o;
		return this.currentPath.equals(other.currentPath)
				&& this.parent.equals(other.parent);
	}

	protected boolean buildStructure(OpenableElementInfo info,
			IProgressMonitor pm, Map newElements, IResource underlyingResource)
			throws ModelException {
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
		List children = new ArrayList();

		TclPackageInfo packageInfo = TclPackagesManager.getPackageInfo(install,
				this.packageName, true);
		if (packageInfo != null) {
			children.add(new TclPackageElement(this, this.packageName,
					packageInfo.getVersion()));

			info.setChildren((IModelElement[]) children
					.toArray(new IModelElement[children.size()]));
		}
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
		// Check for package still pressent here or not.
		Set<String> reqs = new HashSet<String>();
		TclPackagesModelProvider.collectRealRequirements(getScriptProject(),
				reqs);
		IInterpreterInstall install = TclPackagesModelProvider
				.resolveInterpreterInstall(getScriptProject());
		if (install == null) {
			return 0;
		}

		List<TclPackageInfo> infos = TclPackagesManager.getPackageInfos(
				install, reqs, true);
		boolean found = false;
		for (TclPackageInfo packageName : infos) {
			if (packageName.getName().equals(this.packageName)) {
				found = true;
				break;
			}
		}
		if (!found) {
			return 0;
		}
		return currentPath.hashCode();
	}
}
