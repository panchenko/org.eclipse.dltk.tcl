package org.eclipse.dltk.tcl.internal.tclchecker.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.CorrectionEngine;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.core.BuiltinSourceModule;
import org.eclipse.dltk.internal.core.ExternalSourceModule;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerMarker;
import org.eclipse.dltk.tcl.internal.tclchecker.qfix.ITclCheckerQFixReporter;
import org.eclipse.dltk.tcl.internal.tclchecker.qfix.TclCheckerFixUtils;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class AutoCorrectTclCheckerProblemsActionDelegate implements
		IWorkbenchWindowActionDelegate {
	private ISelection selection;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {
		if (this.selection != null
				&& this.selection instanceof IStructuredSelection
				&& !this.selection.isEmpty()) {
			processSelectionToElements();
		}
	}

	private static class SourceModuleVisitor implements IModelElementVisitor {
		private Set<ISourceModule> elements = new HashSet<ISourceModule>();

		public boolean visit(IModelElement element) {
			if (element.getElementType() == IModelElement.PROJECT_FRAGMENT) {
				return !((IProjectFragment) element).isExternal();
			}
			if (element.getElementType() == IModelElement.SOURCE_MODULE) {
				if (!(element instanceof ExternalSourceModule || element instanceof BuiltinSourceModule)
						&& element.getResource() != null) {
					elements.add((ISourceModule) element);
				}
				return false; // do not enter into source module content.
			}
			return true;
		}
	}

	private static class ResourceVisitor implements IResourceVisitor {
		private Set<IFile> files = new HashSet<IFile>();

		public boolean visit(IResource resource) {
			if (resource.getType() == IResource.FILE) {
				files.add((IFile) resource);
				return false;
			}
			return true;
		}
	}

	private static class Collector {

		final ResourceVisitor resourceVisitor = new ResourceVisitor();
		final SourceModuleVisitor moduleVisitor = new SourceModuleVisitor();

		public void processResourcesToElements(Object o) {
			if (o instanceof IResource) {
				try {
					((IResource) o).accept(resourceVisitor);
				} catch (CoreException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
			} else if (o instanceof IModelElement) {
				if (o instanceof IParent) {
					try {
						((IModelElement) o).accept(moduleVisitor);
					} catch (ModelException e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}
				} else if (!(o instanceof ISourceModule)) {
					final ISourceModule module = (ISourceModule) ((IModelElement) o)
							.getAncestor(IModelElement.SOURCE_MODULE);
					moduleVisitor.elements.add(module);
				} else if (o instanceof ISourceModule) {
					moduleVisitor.elements.add((ISourceModule) o);
				}
			}
		}

		void convert() {
			for (ISourceModule module : moduleVisitor.elements) {
				IResource resource = module.getResource();
				if (resource.getType() == IResource.FILE) {
					resourceVisitor.files.add((IFile) resource);
				}
			}
		}

		/**
		 * @return
		 */
		public Set<IFile> getFiles() {
			return resourceVisitor.files;
		}

	}

	protected void processSelectionToElements() {
		final Collector collector = new Collector();
		for (Iterator<?> iterator = ((IStructuredSelection) this.selection)
				.iterator(); iterator.hasNext();) {
			collector.processResourcesToElements(iterator.next());
		}
		collector.convert();
		final Set<IFile> files = collector.getFiles();
		final Job job = new Job("Auto correct...") { //$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Processing", files.size()); //$NON-NLS-1$
				try {
					for (IFile file : files) {
						final ISourceModule module = (ISourceModule) DLTKCore
								.create(file);
						if (module == null) {
							continue;
						}
						int count = 0;
						while (count < 100 && processFile(file, module)) {
							++count;
						}
						monitor.worked(1);
					}
				} catch (CoreException e) {
					TclCheckerPlugin.error(e);
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}

			private boolean processFile(IFile file, ISourceModule module)
					throws CoreException {
				final IMarker[] markers = file.findMarkers(
						TclCheckerMarker.TYPE, true, IResource.DEPTH_ZERO);
				for (int i = 0; i < markers.length; ++i) {
					if ("1"	.equals(markers[i].getAttribute(TclCheckerMarker.AUTO_CORRECTABLE, Util.EMPTY_STRING))) { //$NON-NLS-1$
						final IMarker valid = TclCheckerFixUtils.verify(
								markers[i], module);
						if (valid != null) {
							final String[] corrections = CorrectionEngine
									.decodeArguments(valid
											.getAttribute(
													TclCheckerMarker.SUGGESTED_CORRECTIONS,
													null));
							if (corrections.length != 1) {
								return false;
							}
							/*
							 * FIXME instead of these hacks let's try to create
							 * Positions in document and replace all in single operation. 
							 */
							module.becomeWorkingCopy(null,
									new NullProgressMonitor());
							try {
								IDocument document = new Document(module
										.getSource());
								TclCheckerFixUtils.updateDocument(valid,
										document, corrections[0],
										new ITclCheckerQFixReporter() {
											public void showError(String message) {
												// NOP
											}
										});
								module.getBuffer().setContents(document.get());
								module.commitWorkingCopy(true,
										new NullProgressMonitor());
							} finally {
								module.discardWorkingCopy();
							}
						}
						return true;
					}
				}
				return false;
			}

		};
		job.setUser(true);
		job.schedule();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
}
