/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.CorrectionEngine;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IScriptModelMarker;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclProblems;
import org.eclipse.dltk.tcl.internal.core.packages.PackagesManager;
import org.eclipse.dltk.ui.editor.IScriptAnnotation;
import org.eclipse.dltk.ui.text.AnnotationResolutionProposal;
import org.eclipse.dltk.ui.text.IScriptCorrectionContext;
import org.eclipse.dltk.ui.text.IScriptCorrectionProcessor;
import org.eclipse.dltk.ui.text.MarkerResolutionProposal;
import org.eclipse.jface.text.source.Annotation;

public class TclRequireCorrectionProcessor implements
		IScriptCorrectionProcessor {

	public boolean canFix(IScriptAnnotation annotation) {
		return isFixable(annotation);
	}

	public static boolean isFixable(IScriptAnnotation annotation) {
		if (annotation.getId() == TclProblems.UNKNOWN_REQUIRED_PACKAGE) {
			final String[] args = annotation.getArguments();
			if (args != null && args.length != 0 && args[0] != null) {
				final ISourceModule module = annotation.getSourceModule();
				if (module != null) {
					final IScriptProject project = module.getScriptProject();
					if (project != null) {
						if (isFixable(args[0], project)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void computeQuickAssistProposals(IScriptAnnotation annotation,
			IScriptCorrectionContext context) {
		if (isFixable(annotation)) {
			final String pkgName = annotation.getArguments()[0];
			if (addPackageName(context, pkgName)) {
				context.addProposal(new AnnotationResolutionProposal(
						new TclRequirePackageMarkerResolution(pkgName, context
								.getProject()), (Annotation) annotation));
			}
		}
	}

	private static final String PACKAGES = TclRequireCorrectionProcessor.class
			.getName()
			+ "#PACKAGES"; //$NON-NLS-1$

	/**
	 * @param context
	 * @param pkgName
	 * @return
	 */
	private boolean addPackageName(IScriptCorrectionContext context,
			String pkgName) {
		Set packages = (Set) context.getAttribute(PACKAGES);
		if (packages != null) {
			return packages.add(pkgName);
		} else {
			packages = new HashSet();
			packages.add(pkgName);
			context.setAttribute(PACKAGES, packages);
			return true;
		}
	}

	public boolean canFix(IMarker marker) {
		return isFixable(marker);
	}

	public static boolean isFixable(IMarker marker) {
		if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_REQUIRED_PACKAGE) {
			final String[] args = CorrectionEngine.getProblemArguments(marker);
			if (args != null && args.length != 0 && args[0] != null) {
				IResource resource = marker.getResource();
				IProject project = resource.getProject();
				IScriptProject scriptProject = DLTKCore.create(project);
				if (isFixable(args[0], scriptProject)) {
					return true;
				}
			}
		}
		return false;
	}

	public void computeQuickAssistProposals(IMarker marker,
			IScriptCorrectionContext context) {
		if (isFixable(marker)) {
			final String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
			if (addPackageName(context, pkgName)) {
				context.addProposal(new MarkerResolutionProposal(
						new TclRequirePackageMarkerResolution(pkgName, context
								.getProject()), marker));
			}
		}
	}

	public static boolean isFixable(String pkgName, IScriptProject scriptProject) {
		IDLTKLanguageToolkit toolkit = null;
		toolkit = DLTKLanguageManager.getLanguageToolkit(scriptProject);
		if (toolkit != null
				&& toolkit.getNatureId().equals(TclNature.NATURE_ID)) {
			IInterpreterInstall install = null;
			try {
				install = ScriptRuntime.getInterpreterInstall(scriptProject);
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			if (install != null) {
				PackagesManager manager = PackagesManager.getInstance();
				IPath[] paths = manager.getPathsForPackage(install, pkgName);
				if (paths != null && paths.length > 0) {
					return true;
				}
				Map dependencies = manager.getDependencies(pkgName, install);
				for (Iterator iterator = dependencies.keySet().iterator(); iterator
						.hasNext();) {
					String pkg = (String) iterator.next();
					IPath[] paths2 = manager.getPathsForPackage(install, pkg);
					if (paths2 != null && paths2.length > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
