package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesElement;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesSourceModule;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @since 2.0
 */
public class SourceContainerElement extends WorkbenchAdaptable {

	public SourceContainerElement(SelectionDialogInput input) {
		super(input);
	}

	@Override
	public ContainerType getContainerType() {
		return ContainerType.SOURCES;
	}

	@Override
	public Object[] getChildren() {
		final Set<IScriptProject> projects = input.collectProjects();
		final Set<SourceElement> sources = new HashSet<SourceElement>();
		for (TclSourcesSourceModule module : collectSources(projects)) {
			sources.add(new SourceElement(module.getFullPath()));
		}
		return sources.toArray();
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return DLTKPluginImages.DESC_OBJS_LIBRARY;
	}

	public String getLabel(Object o) {
		return PreferenceMessages.InstrumentationLabelProvider_Sources;
	}

	private static Set<TclSourcesSourceModule> collectSources(
			Set<IScriptProject> projects) {
		final Set<TclSourcesSourceModule> sources = new HashSet<TclSourcesSourceModule>();
		for (IScriptProject project : projects) {
			try {
				for (IProjectFragment fragment : project.getProjectFragments()) {
					if (!(fragment instanceof TclSourcesFragment)) {
						continue;
					}
					for (IModelElement element : fragment.getChildren()) {
						if (element instanceof TclSourcesElement) {
							for (IModelElement ee : ((IParent) element)
									.getChildren()) {
								if (ee instanceof TclSourcesSourceModule) {
									sources.add((TclSourcesSourceModule) ee);
								}
							}
						}
					}
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return sources;
	}

}
