package org.eclipse.dltk.tcl.internal.ui.wizards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.internal.ui.wizards.BuildpathDetector;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.core.packages.TclBuildPathPackageCollector;
import org.eclipse.osgi.util.NLS;

public class TclBuildpathDetector extends BuildpathDetector {
	private final Set packagesInBuild = new HashSet();
	private boolean useAnalysis;

	public TclBuildpathDetector(IProject project, IDLTKLanguageToolkit toolkit)
			throws CoreException {
		super(project, toolkit);
	}

	protected void addInterpreterContainer(ArrayList cpEntries) {
		cpEntries
				.add(InterpreterContainerHelper.createPackagesContainer(
						packagesInBuild, new Path(
								ScriptRuntime.INTERPRETER_CONTAINER)));
	}

	protected void processSources(final List correctFiles,
			final SubProgressMonitor sub) {
		if (useAnalysis) {
			sub.beginTask(TclWizardMessages.TclBuildpathDetector_AnalysingTask,
					correctFiles.size());
			final TclBuildPathPackageCollector collector = new TclBuildPathPackageCollector();
			int count = 0;
			for (Iterator i = correctFiles.iterator(); i.hasNext();) {
				final IFile file = (IFile) i.next();
				final String msg = TclWizardMessages.TclBuildpathDetector_AnalysingSubTask;
				sub.subTask(NLS.bind(msg, String.valueOf(correctFiles.size()
						- count), file.getName()));
				ISourceModule module = (ISourceModule) DLTKCore.create(file);
				// if (module.exists()) {
				char[] source;
				try {
					source = org.eclipse.dltk.internal.core.util.Util
							.getResourceContentsAsCharArray(file);
					ModuleDeclaration moduleDeclaration = SourceParserUtil
							.getModuleDeclaration(file.getName().toCharArray(),
									source, TclNature.NATURE_ID, null, null);
					try {
						collector.process(moduleDeclaration);
					} catch (Exception e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}
				} catch (ModelException e1) {
					e1.printStackTrace();
				}
				count++;
				// }
				sub.done();
			}
			/*
			 * TODO should we check that required packages are available in the
			 * selected interpreter ?
			 */
			packagesInBuild.addAll(collector.getPackagesRequired());
		}
	}

	public void setUseAnalysis(boolean useAnalysis) {
		this.useAnalysis = useAnalysis;
	}
}
