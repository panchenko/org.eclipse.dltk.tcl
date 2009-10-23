package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Set;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.activestatedebugger.InstrumentationUtils;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesSourceModule;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;

public class SourceContainerElement extends WorkbenchAdaptable {

	public SourceContainerElement(SelectionDialogInput input) {
		super(input);
	}

	public Object[] getChildren(Object o) {
		final Set<IScriptProject> projects = input.collectProjects();
		final Set<TclSourcesSourceModule> sources = InstrumentationUtils
				.collectSources(projects);
		return sources.toArray();
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return DLTKPluginImages.DESC_OBJS_LIBRARY;
	}

	public String getLabel(Object o) {
		return PreferenceMessages.InstrumentationLabelProvider_Sources;
	}

}
