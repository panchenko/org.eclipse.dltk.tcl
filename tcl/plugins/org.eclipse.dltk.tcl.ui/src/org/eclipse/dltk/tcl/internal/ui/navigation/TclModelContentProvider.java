package org.eclipse.dltk.tcl.internal.ui.navigation;

import java.util.Arrays;
import java.util.List;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.scriptview.BuildPathContainer;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageElement;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesElement;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.ui.IModelContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

@SuppressWarnings("restriction")
public class TclModelContentProvider implements IModelContentProvider {

	public TclModelContentProvider() {
	}

	public void provideModelChanges(Object parentElement, List children,
			ITreeContentProvider iTreeContentProvider) {
		// System.out.println("CONTAINER:" + parentElement);
		if (parentElement instanceof BuildPathContainer) {
			BuildPathContainer container = (BuildPathContainer) parentElement;
			IBuildpathEntry entry = container.getBuildpathEntry();
			if (!entry.getPath().segment(0).equals(
					ScriptRuntime.INTERPRETER_CONTAINER)) {
				return;
			}

			IScriptProject project = container.getScriptProject();
			IProjectFragment[] fragments;
			try {
				fragments = project.getProjectFragments();
				for (int i = 0; i < fragments.length; i++) {
					if (fragments[i] instanceof TclPackageFragment) {
						TclPackageFragment fragment = (TclPackageFragment) fragments[i];
						children.addAll(Arrays.asList(fragment.getChildren()));
					}
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		if (parentElement instanceof IScriptProject) {
			// Show packages element
			IScriptProject prj = (IScriptProject) parentElement;
			try {
				IProjectFragment[] fragments = prj.getProjectFragments();
				for (int i = 0; i < fragments.length; i++) {
					if (fragments[i] instanceof TclSourcesFragment) {
						TclSourcesFragment fragment = (TclSourcesFragment) fragments[i];
						if (fragment.containChildrens()) {
							children.add(fragment);
						}
					}
				}
			} catch (ModelException e) {
				DLTKCore.error("Error resolving project fragments", e);
			}
		}
		if (parentElement instanceof TclSourcesFragment) {
			TclSourcesFragment fragment = (TclSourcesFragment) parentElement;
			children.clear();
			try {
				IModelElement[] elements = fragment.getChildren();
				TclSourcesElement element = (TclSourcesElement) elements[0];
				IModelElement[] modelElements = element.getChildren();
				for (int i = 0; i < modelElements.length; i++) {
					children.add(modelElements[i]);
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

	public Object getParentElement(Object element,
			ITreeContentProvider iTreeContentProvider) {
		if (element instanceof TclPackageFragment
				|| element instanceof TclPackageElement) {
			// We need to return buildpath container here.
			IScriptProject project = ((IModelElement) element)
					.getScriptProject();
			Object[] children = iTreeContentProvider.getChildren(project);
			for (int i = 0; i < children.length; i++) {
				if (children[i] instanceof BuildPathContainer) {
					BuildPathContainer container = (BuildPathContainer) children[i];
					IBuildpathEntry entry = container.getBuildpathEntry();
					if (entry.getPath().segment(0).equals(
							ScriptRuntime.INTERPRETER_CONTAINER)) {
						return container;
					}
				}
			}
		}
		return null;
	}
}
