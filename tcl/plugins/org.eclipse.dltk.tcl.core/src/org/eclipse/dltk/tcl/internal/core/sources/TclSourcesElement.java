package org.eclipse.dltk.tcl.internal.core.sources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
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
import org.eclipse.dltk.core.environment.IFileHandle;
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
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.utils.CorePrinter;

/**
 * Element to represent sourced files.
 */
@SuppressWarnings("restriction")
public class TclSourcesElement extends Openable implements IScriptFolder {
	private String name;

	protected TclSourcesElement(ModelElement parent) {
		super(parent);
		this.name = "Sourced files@" + getScriptProject().getElementName();
	}

	public String getElementName() {
		return name;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TclSourcesElement other = (TclSourcesElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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
			Map<IPath, String> originalNames = new HashMap<IPath, String>();
			Set<String> pseudoElements = new HashSet<String>();
			TclSourcesUtils.fillSources(install, scriptProject, sources,
					originalNames, pseudoElements);
			if (!sources.isEmpty()) {
				IPath[] paths = sources.toArray(new IPath[sources.size()]);
				IEnvironment environment = EnvironmentManager
						.getEnvironment(scriptProject);

				for (int i = 0; i < paths.length; i++) {
					IPath path = paths[i];
					IFileHandle file = EnvironmentPathUtils.getFile(
							environment, path);
					ExternalEntryFile storage = new ExternalEntryFile(file);
					String modulePath = environment.convertPathToString(path)
							.replace(environment.getSeparatorChar(), '_')
							.replace(':', '_');
					ExternalSourceModule module = new TclSourcesSourceModule(
							this, modulePath, DefaultWorkingCopyOwner.PRIMARY,
							storage, originalNames.get(path));
					vChildren.add(module);
				}
			}
			if (pseudoElements.size() > 0) {
				for (String name : pseudoElements) {
					vChildren.add(new TclSourcesPseudoSourceModule(this, name,
							DefaultWorkingCopyOwner.PRIMARY));
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
		List<IModelElement> list = getChildrenOfType(SOURCE_MODULE);
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

	/**
	 * @since 2.0
	 */
	@Override
	public boolean isReadOnly() {
		return true;
	}
}
