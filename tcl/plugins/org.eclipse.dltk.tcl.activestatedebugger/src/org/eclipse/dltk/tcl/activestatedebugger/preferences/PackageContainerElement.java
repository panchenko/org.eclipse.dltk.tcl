package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Set;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.activestatedebugger.InstrumentationUtils;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageElement;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;

public class PackageContainerElement extends WorkbenchAdaptable {

	public PackageContainerElement(SelectionDialogInput input) {
		super(input);
	}

	public Object[] getChildren(Object o) {
		final Set<IScriptProject> projects = input.collectProjects();
		final Set<TclPackageElement> packages = InstrumentationUtils
				.collectPackages(projects);
		return packages.toArray();
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return DLTKPluginImages.DESC_OBJS_LIBRARY;
	}

	public String getLabel(Object o) {
		return PreferenceMessages.InstrumentationLabelProvider_Packages;
	}

}
