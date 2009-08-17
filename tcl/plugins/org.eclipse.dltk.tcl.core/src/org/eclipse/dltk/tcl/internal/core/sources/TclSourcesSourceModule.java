/**
 * 
 */
package org.eclipse.dltk.tcl.internal.core.sources;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelStatus;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.WorkingCopyOwner;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.core.ExternalSourceModule;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.internal.core.util.Util;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclProjectInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.emf.common.util.EList;

/**
 * Source module used in packages
 */
public class TclSourcesSourceModule extends ExternalSourceModule {
	private String originalName;

	public TclSourcesSourceModule(ModelElement parent, String name,
			WorkingCopyOwner owner, IStorage storage, String originalName) {
		super(parent, name, owner, storage);
		this.originalName = originalName;
	}

	public IPath getPath() {
		IPath parentPath = getParent().getPath();
		return parentPath.append(getElementName());
	}

	public IPath getFullPath() {
		return getStorage().getFullPath();
	}

	public IPath getBufferPath() {
		IEnvironment environment = EnvironmentManager.getEnvironment(this);
		if (environment != null) {
			return EnvironmentPathUtils.getFullPath(environment, getStorage()
					.getFullPath());
		} else {
			return getStorage().getFullPath();
		}
	}

	public String getOriginalName() {
		return this.originalName;
	}

	/**
	 * @since 2.0
	 */
	@Override
	protected IStatus validateSourceModule(IDLTKLanguageToolkit toolkit,
			IResource resource) {
		return IModelStatus.VERIFIED_OK;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void delete(boolean force, IProgressMonitor monitor) {
		// Remove correction for this module from all places.
		final IScriptProject scriptProject = getScriptProject();
		final IEnvironment environment = EnvironmentManager
				.getEnvironment(scriptProject);
		TclProjectInfo project = TclPackagesManager.getTclProject(scriptProject
				.getElementName());
		EList<TclModuleInfo> modules = project.getModules();
		String path = environment.convertPathToString(getFullPath()).toString();
		for (TclModuleInfo tclModuleInfo : modules) {
			EList<UserCorrection> corrections = tclModuleInfo
					.getSourceCorrections();
			EList<TclSourceEntry> sourced = tclModuleInfo.getSourced();
			EList<UserCorrection> sourceCorrections = tclModuleInfo
					.getSourceCorrections();
			for (TclSourceEntry tclSourceEntry : sourced) {
				String value = tclSourceEntry.getValue();
				if (value.contains("$") && value.equals(getOriginalName())) {
					for (UserCorrection userCorrection : sourceCorrections) {
						if (userCorrection.getOriginalValue().equals(value)) {
							userCorrection.getUserValue().remove(path);
						}
					}
				}
			}
		}
		TclPackagesManager.save();
		// Do delta refresh
		try {
			ModelManager.getModelManager().getDeltaProcessor()
					.checkExternalChanges(
							new IModelElement[] { getScriptProject() },
							new NullProgressMonitor());
		} catch (ModelException e) {
			DLTKCore.error("Failed to call for model update:", e);
		}
	}

	/**
	 * @since 2.0
	 */
	@Override
	public String getElementName() {
		return super.getElementName();
	}

	public int hashCode() {
		if (this.parent == null)
			return super.hashCode();
		return Util.combineHashCodes(super.getElementName().hashCode(),
				this.parent.hashCode());
	}
}