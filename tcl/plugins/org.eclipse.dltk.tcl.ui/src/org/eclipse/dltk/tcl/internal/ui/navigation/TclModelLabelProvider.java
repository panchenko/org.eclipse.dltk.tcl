package org.eclipse.dltk.tcl.internal.ui.navigation;

import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageElement;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageSourceModule;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesSourceModule;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TclModelLabelProvider extends LabelProvider {
	public String getText(Object element) {
		if (element instanceof TclPackageFragment) {
			return "Packages";
		} else if (element instanceof TclSourcesFragment) {
			return "Sources";
		} else if (element instanceof TclPackageElement) {
			TclPackageElement pkg = (TclPackageElement) element;
			String result = pkg.getPackageName();
			if (pkg.getVersion() != null) {
				result += " " + pkg.getVersion();
			}
			return result;
		} else if (element instanceof TclPackageSourceModule) {
			return ((TclPackageSourceModule) element).getElementName();
		} else if (element instanceof TclSourcesSourceModule) {
			IEnvironment environment = EnvironmentManager
					.getEnvironment(((TclSourcesSourceModule) element)
							.getScriptProject());
			return environment
					.convertPathToString(((TclSourcesSourceModule) element)
							.getFullPath());
		}
		return null;
	}

	public Image getImage(Object element) {
		if (element instanceof TclSourcesFragment) {
			return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_EXTJAR_WSRC);
		}
		return null;
	}
}
