package org.eclipse.dltk.tcl.internal.core.sources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.WorkingCopyOwner;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.core.DefaultWorkingCopyOwner;
import org.eclipse.dltk.internal.core.ExternalEntryFile;
import org.eclipse.dltk.internal.core.ExternalSourceModule;
import org.eclipse.dltk.internal.core.MementoModelElementUtil;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.ModelElementInfo;
import org.eclipse.dltk.internal.core.Openable;
import org.eclipse.dltk.internal.core.OpenableElementInfo;
import org.eclipse.dltk.internal.core.ScriptFolderInfo;
import org.eclipse.dltk.internal.core.util.MementoTokenizer;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.internal.packages.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageSourceModule;
import org.eclipse.dltk.utils.CorePrinter;
import org.eclipse.emf.common.util.EList;

/**
 * Element to represent sourced files.
 */
public class TclSourcesElement extends Openable implements IScriptFolder {

	protected TclSourcesElement(ModelElement parent) {
		super(parent);
	}

	public String getElementName() {
		return "Sourced files";
	}

	protected Object createElementInfo() {
		return new OpenableElementInfo();
	}

	public int getElementType() {
		return SCRIPT_FOLDER;
	}

	public int getKind() throws ModelException {
		return IProjectFragment.K_SOURCE;
	}

	public IResource getResource() {
		return null;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TclSourcesElement))
			return false;

		return true;
	}

	public int hashCode() {
		return 0;
	}

	private static Set<IPath> getBuildpath(IScriptProject project) {
		final IBuildpathEntry[] resolvedBuildpath;
		try {
			resolvedBuildpath = project.getResolvedBuildpath(true);
		} catch (ModelException e1) {
			return Collections.emptySet();
		}
		final Set<IPath> buildpath = new HashSet<IPath>();
		for (int i = 0; i < resolvedBuildpath.length; i++) {
			final IBuildpathEntry entry = resolvedBuildpath[i];
			if (entry.getEntryKind() == IBuildpathEntry.BPE_LIBRARY
					&& entry.isExternal()) {
				buildpath.add(entry.getPath());
			}
		}
		return buildpath;
	}

	private static Set<IPath> getPackages(IScriptProject project,
			IInterpreterInstall install) {
		Set<IPath> result = new HashSet<IPath>();
		Set<String> packages = InterpreterContainerHelper
				.getInterpreterContainerDependencies(project);

		List<TclPackageInfo> packageInfos = TclPackagesManager.getPackageInfos(
				install, packages, true);
		for (TclPackageInfo tclPackageInfo : packageInfos) {
			EList<String> sources = tclPackageInfo.getSources();
			for (String source : sources) {
				result.add(new Path(source));
			}
		}

		return result;
	}

	protected boolean buildStructure(OpenableElementInfo info,
			IProgressMonitor pm, Map newElements, IResource underlyingResource)
			throws ModelException {
		// check whether this folder can be opened
		IInterpreterInstall install = null;
		IScriptProject scriptProject = getScriptProject();
		try {
			install = ScriptRuntime.getInterpreterInstall(scriptProject);
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}

		if (install != null) {
			// add modules from resources
			Set<IModelElement> vChildren = new HashSet<IModelElement>();
			// Add external source module here.

			Set<IPath> sources = new HashSet<IPath>();

			Set<IPath> buildpath = getBuildpath(scriptProject);
			Set<IPath> packageFiles = getPackages(scriptProject, install);

			List<TclModuleInfo> modules = TclPackagesManager
					.getProjectModules(scriptProject.getElementName());
			for (TclModuleInfo tclModuleInfo : modules) {
				EList<TclSourceEntry> sourced = tclModuleInfo.getSourced();
				EList<UserCorrection> corrections = tclModuleInfo
						.getSourceCorrections();
				Map<String, String> correctionMap = new HashMap<String, String>();
				for (UserCorrection userCorrection : corrections) {
					correctionMap.put(userCorrection.getOriginalValue(),
							userCorrection.getUserValue());
				}
				for (TclSourceEntry source : sourced) {
					String value = null;
					if (correctionMap.containsKey(source.getValue())) {
						value = correctionMap.get(source.getValue());
					}

					if (value == null) {
						continue;
					}
					IPath path = Path.fromPortableString(value);
					IPath pathParent = path.removeLastSegments(1);
					if (buildpath.contains(pathParent)) {
						continue; // File are on buildpath
					}
					// Check for file in some package
					if (packageFiles.contains(path)) {
						continue; // File are on buildpath
					}
					sources.add(path);
				}
			}
			if (!sources.isEmpty()) {
				IPath[] paths = sources.toArray(new IPath[sources.size()]);
				IEnvironment environment = EnvironmentManager
						.getEnvironment(scriptProject);

				for (int i = 0; i < paths.length; i++) {
					IPath path = paths[i];
					ExternalEntryFile storage = new ExternalEntryFile(
							EnvironmentPathUtils.getFile(environment, path));
					ExternalSourceModule module = new TclSourcesSourceModule(
							this, environment.convertPathToString(path)
									.replace(environment.getSeparatorChar(),
											'_'),
							DefaultWorkingCopyOwner.PRIMARY, storage);
					vChildren.add(module);
				}
			}
			info.setChildren(vChildren.toArray(new IModelElement[vChildren
					.size()]));
		}
		return true;
	}

	protected char getHandleMementoDelimiter() {
		return JEM_USER_ELEMENT;
	}

	public IModelElement getHandleFromMemento(String token,
			MementoTokenizer memento, WorkingCopyOwner owner) {
		switch (token.charAt(0)) {
		case JEM_SOURCEMODULE:
			if (!memento.hasMoreTokens())
				return this;
			String classFileName = memento.nextToken();
			ModelElement classFile = (ModelElement) getSourceModule(classFileName);
			if (classFile != null) {
				return classFile.getHandleFromMemento(memento, owner);
			}
		case JEM_USER_ELEMENT:
			return MementoModelElementUtil.getHandleFromMemento(memento, this,
					owner);
		}
		return null;
	}

	public boolean containsScriptResources() throws ModelException {
		Object elementInfo = getElementInfo();
		if (!(elementInfo instanceof ScriptFolderInfo))
			return false;
		ScriptFolderInfo scriptElementInfo = (ScriptFolderInfo) elementInfo;
		return scriptElementInfo.containsScriptResources();
	}

	public boolean hasChildren() throws ModelException {
		return getChildren().length > 0;
	}

	public void printNode(CorePrinter output) {
		output.formatPrint("DLTK TCL Package:" + getElementName()); //$NON-NLS-1$
		output.indent();
		try {
			IModelElement modelElements[] = this.getChildren();
			for (int i = 0; i < modelElements.length; ++i) {
				IModelElement element = modelElements[i];
				if (element instanceof ModelElement) {
					((ModelElement) element).printNode(output);
				} else {
					output.print("Unknown element:" + element); //$NON-NLS-1$
				}
			}
		} catch (ModelException ex) {
			output.formatPrint(ex.getLocalizedMessage());
		}
		output.dedent();
	}

	public ISourceModule createSourceModule(String name, String contents,
			boolean force, IProgressMonitor monitor) throws ModelException {
		return null;
	}

	public Object[] getForeignResources() throws ModelException {
		return ModelElementInfo.NO_NON_SCRIPT_RESOURCES;
	}

	public boolean exists() {
		return true;
	}

	public ISourceModule[] getSourceModules() throws ModelException {
		ArrayList list = getChildrenOfType(SOURCE_MODULE);
		ISourceModule[] array = new ISourceModule[list.size()];
		list.toArray(array);
		return array;
	}

	public boolean hasSubfolders() throws ModelException {
		return false;
	}

	public boolean isRootFolder() {
		return false;
	}

	public IPath getPath() {
		return getParent().getPath().append(getElementName());
	}

	public void copy(IModelElement container, IModelElement sibling,
			String rename, boolean replace, IProgressMonitor monitor)
			throws ModelException {
	}

	public void delete(boolean force, IProgressMonitor monitor)
			throws ModelException {
	}

	public void move(IModelElement container, IModelElement sibling,
			String rename, boolean replace, IProgressMonitor monitor)
			throws ModelException {
	}

	public void rename(String name, boolean replace, IProgressMonitor monitor)
			throws ModelException {
	}

	public ISourceModule getSourceModule(String name) {
		IModelElement[] children = null;
		try {
			children = getChildren();
			for (int i = 0; i < children.length; i++) {
				IModelElement child = children[i];
				if (child instanceof ISourceModule) {
					if (name.equals(child.getElementName())) {
						return (ISourceModule) child;
					}
				}
			}
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
