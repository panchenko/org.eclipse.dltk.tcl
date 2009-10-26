package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageElement;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @since 2.0
 */
public class PackageContainerElement extends WorkbenchAdaptable {

	public PackageContainerElement(SelectionDialogInput input) {
		super(input);
	}

	@Override
	public ContainerType getContainerType() {
		return ContainerType.PACKAGES;
	}

	@Override
	public Object[] getChildren() {
		final Set<IScriptProject> projects = input.collectProjects();
		final Set<PackageElement> packages = new HashSet<PackageElement>();
		for (TclPackageElement element : collectPackages(projects)) {
			packages.add(new PackageElement(element.getPackageName()));
		}
		return packages.toArray();
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return DLTKPluginImages.DESC_OBJS_LIBRARY;
	}

	public String getLabel(Object o) {
		return PreferenceMessages.InstrumentationLabelProvider_Packages;
	}

	private static Set<TclPackageElement> collectPackages(
			Set<IScriptProject> projects) {
		final Set<TclPackageElement> packages = new HashSet<TclPackageElement>();
		for (IScriptProject project : projects) {
			try {
				for (IProjectFragment fragment : project.getProjectFragments()) {
					if (!(fragment instanceof TclPackageFragment)) {
						continue;
					}
					for (IModelElement element : fragment.getChildren()) {
						if (element instanceof TclPackageElement) {
							packages.add((TclPackageElement) element);
						}
					}
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return packages;
	}

}
