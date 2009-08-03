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
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.TclProblems;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver;
import org.eclipse.dltk.ui.editor.IScriptAnnotation;
import org.eclipse.dltk.ui.text.AnnotationResolutionProposal;
import org.eclipse.dltk.ui.text.IScriptCorrectionContext;
import org.eclipse.dltk.ui.text.IScriptCorrectionProcessor;
import org.eclipse.dltk.ui.text.MarkerResolutionProposal;

public class TclSourcePackageCorrectionProcessor implements
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
		} else if (annotation.getId() == TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION) {
			return true;
		} else if (annotation.getId() == TclProblems.UNKNOWN_SOURCE_CORRECTION) {
			return true;
		} else if (annotation.getId() == TclProblems.UNKNOWN_SOURCE) {
			final String[] args = annotation.getArguments();
			if (args != null && args.length != 0 && args[0] != null) {
				final ISourceModule module = annotation.getSourceModule();
				if (module != null) {
					final IScriptProject project = module.getScriptProject();
					if (project != null) {
						if (TclSourceMarkerResolution.fixAvailable(module,
								args[0])) {
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
		if (annotation.getId() == TclProblems.UNKNOWN_REQUIRED_PACKAGE) {
			if (isFixable(annotation)) {
				final String pkgName = annotation.getArguments()[0];
				if (addPackageName(context, pkgName)) {
					context.addProposal(new AnnotationResolutionProposal(
							new TclRequirePackageMarkerResolution(pkgName,
									context.getProject()), annotation));
				}
			}
		} else if (annotation.getId() == TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION) {
			final String pkgName = annotation.getArguments()[0];
			context.addProposal(new AnnotationResolutionProposal(
					new TclRequirePackageCorrectionMarkerResolution(pkgName,
							context.getProject(), context.getModule()),
					annotation));
			addGlobalVariableProposals(annotation, context, pkgName);
		} else if (annotation.getId() == TclProblems.UNKNOWN_SOURCE_CORRECTION) {
			final String fName = annotation.getArguments()[0];
			context.addProposal(new AnnotationResolutionProposal(
					new TclSourceCorrectionMarkerResolution(fName, context
							.getProject(), context.getModule()), annotation));
			addGlobalVariableProposals(annotation, context, fName);
		} else if (annotation.getId() == TclProblems.UNKNOWN_SOURCE) {
			final String fName = annotation.getArguments()[0];
			if (TclSourceMarkerResolution.fixAvailable(context.getModule(),
					fName)) {
				context
						.addProposal(new AnnotationResolutionProposal(
								new TclSourceMarkerResolution(fName, context
										.getProject(), context.getModule()),
								annotation));
			}
		}
	}

	private void addGlobalVariableProposals(IScriptAnnotation annotation,
			IScriptCorrectionContext context, final String fName) {
		String[] names = TclVariableResolver.extractVariableNames(fName);
		Map<String, String> eMap = TclPackagesManager.getVariables(context
				.getProject().getElementName());
		if (names != null) {
			for (String var : names) {
				if (!eMap.containsKey(var)) {
					context
							.addProposal(new AnnotationResolutionProposal(
									new TclGlobalVariableSourceCorrectionMarkerResolution(
											var, context.getProject()),
									annotation));
				}
			}
		}
	}

	private static final String PACKAGES = TclSourcePackageCorrectionProcessor.class
			.getName()
			+ "#PACKAGES"; //$NON-NLS-1$
	private static final String SOURCES = TclSourcePackageCorrectionProcessor.class
			.getName()
			+ "#SOURCES"; //$NON-NLS-1$

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
		int idValue = marker.getAttribute(IScriptModelMarker.ID, 0);
		if (idValue == TclProblems.UNKNOWN_REQUIRED_PACKAGE) {
			final String[] args = CorrectionEngine.getProblemArguments(marker);
			if (args != null && args.length != 0 && args[0] != null) {
				IResource resource = marker.getResource();
				IProject project = resource.getProject();
				IScriptProject scriptProject = DLTKCore.create(project);
				if (isFixable(args[0], scriptProject)) {
					return true;
				}
			}
		} else if (idValue == TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION) {
			return true;
		} else if (idValue == TclProblems.UNKNOWN_SOURCE_CORRECTION) {
			return true;
		} else if (idValue == TclProblems.UNKNOWN_SOURCE) {
			final String[] args = CorrectionEngine.getProblemArguments(marker);
			if (args != null && args.length != 0 && args[0] != null) {
				IResource resource = marker.getResource();
				if (TclSourceMarkerResolution.fixAvailable(
						(ISourceModule) DLTKCore.create(resource), args[0])) {
					return true;
				}
			}
		}
		return false;
	}

	public void computeQuickAssistProposals(IMarker marker,
			IScriptCorrectionContext context) {
		if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_REQUIRED_PACKAGE) {
			if (isFixable(marker)) {
				final String pkgName = CorrectionEngine
						.getProblemArguments(marker)[0];
				if (addPackageName(context, pkgName)) {
					context.addProposal(new MarkerResolutionProposal(
							new TclRequirePackageMarkerResolution(pkgName,
									context.getProject()), marker));
				}
			}
		} else if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION) {
			final String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
			if (addPackageName(context, pkgName)) {
				context.addProposal(new MarkerResolutionProposal(
						new TclRequirePackageCorrectionMarkerResolution(
								pkgName, context.getProject(), context
										.getModule()), marker));
				addGlobalVariablesProposals(marker, context, pkgName);
			}
			addGlobalVariablesProposals(marker, context, pkgName);
		} else if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_SOURCE_CORRECTION) {
			final String fName = CorrectionEngine.getProblemArguments(marker)[0];
			context.addProposal(new MarkerResolutionProposal(
					new TclSourceCorrectionMarkerResolution(fName, context
							.getProject(), context.getModule()), marker));

		} else if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_SOURCE) {
			final String fName = CorrectionEngine.getProblemArguments(marker)[0];
			if (TclSourceMarkerResolution.fixAvailable(context.getModule(),
					fName)) {
				context.addProposal(new MarkerResolutionProposal(
						new TclSourceMarkerResolution(fName, context
								.getProject(), context.getModule()), marker));
			}
		}
	}

	private void addGlobalVariablesProposals(IMarker marker,
			IScriptCorrectionContext context, final String pkgName) {
		String[] names = TclVariableResolver.extractVariableNames(pkgName);
		Map<String, String> eMap = TclPackagesManager.getVariables(context
				.getProject().getElementName());
		if (names != null) {
			for (String var : names) {
				if (!eMap.containsKey(var)) {
					context
							.addProposal(new MarkerResolutionProposal(
									new TclGlobalVariableSourceCorrectionMarkerResolution(
											var, context.getProject()), marker));
				}
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
				TclPackageInfo info = TclPackagesManager.getPackageInfo(
						install, pkgName, true);
				if (info == null || !info.isFetched()) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

}
