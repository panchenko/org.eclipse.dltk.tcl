/**
 * 
 */
package org.eclipse.dltk.tcl.internal.core.packages;

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
public class TclPackageSourceModule extends ExternalSourceModule {
	TclPackageSourceModule(ModelElement parent, String name,
			WorkingCopyOwner owner, IStorage storage) {
		super(parent, name, owner, storage);
	}

	public IPath getPath() {
		IPath parentPath = getParent().getPath();
		return parentPath.append(getElementName());
	}

	public IPath getFullPath() {
		return getStorage().getFullPath();
	}

	protected IPath getBufferPath() {
		IEnvironment environment = EnvironmentManager.getEnvironment(this);
		if (environment != null) {
			return EnvironmentPathUtils.getFullPath(environment, getStorage()
					.getFullPath());
		} else {
			return getStorage().getFullPath();
		}
	}
}