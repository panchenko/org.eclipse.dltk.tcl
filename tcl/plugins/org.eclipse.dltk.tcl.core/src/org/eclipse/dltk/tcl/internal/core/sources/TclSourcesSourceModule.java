/**
 * 
 */
package org.eclipse.dltk.tcl.internal.core.sources;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.WorkingCopyOwner;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.core.ExternalSourceModule;
import org.eclipse.dltk.internal.core.ModelElement;

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
}