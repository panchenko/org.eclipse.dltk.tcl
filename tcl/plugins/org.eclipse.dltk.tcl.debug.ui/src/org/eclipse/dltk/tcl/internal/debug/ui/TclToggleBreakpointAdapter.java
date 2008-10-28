package org.eclipse.dltk.tcl.internal.debug.ui;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.debug.core.model.IScriptVariable;
import org.eclipse.dltk.debug.core.model.IScriptWatchpoint;
import org.eclipse.dltk.debug.ui.DLTKDebugUIPlugin;
import org.eclipse.dltk.debug.ui.actions.ActionMessages;
import org.eclipse.dltk.debug.ui.breakpoints.BreakpointUtils;
import org.eclipse.dltk.debug.ui.breakpoints.IScriptBreakpointLineValidator;
import org.eclipse.dltk.debug.ui.breakpoints.Messages;
import org.eclipse.dltk.debug.ui.breakpoints.ScriptBreakpointLineValidatorFactory;
import org.eclipse.dltk.debug.ui.breakpoints.ScriptToggleBreakpointAdapter;
import org.eclipse.dltk.internal.debug.core.model.ScriptWatchpoint;
import org.eclipse.dltk.tcl.internal.debug.TclDebugConstants;
import org.eclipse.dltk.tcl.internal.debug.ui.actions.IToggleSpawnpointsTarget;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class TclToggleBreakpointAdapter extends ScriptToggleBreakpointAdapter
		implements IToggleSpawnpointsTarget {
	private static final IScriptBreakpointLineValidator validator = ScriptBreakpointLineValidatorFactory
			.createNonEmptyNoCommentValidator("#"); //$NON-NLS-1$

	protected String getDebugModelId() {
		return TclDebugConstants.DEBUG_MODEL_ID;
	}

	protected IScriptBreakpointLineValidator getValidator() {
		return validator;
	}

	public void toggleMethodBreakpoints(IWorkbenchPart part,
			ISelection selection) throws CoreException {
		// Not implemented for TCL yet
	}

	public boolean canToggleMethodBreakpoints(IWorkbenchPart part,
			ISelection selection) {
		return false;
	}

	public boolean canToggleWatchpoints(IWorkbenchPart part,
			ISelection selection) {
		if (isRemote(part, selection)) {
			return false;
		}
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			return isFields(ss);
		}
		return (selection instanceof ITextSelection)
				&& isField((ITextSelection) selection, part);
	}

	public void toggleWatchpoints(final IWorkbenchPart part,
			final ISelection finalSelection) throws CoreException {
		Job job = new Job("Toggle Watchpoints") { //$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				try {
					report(null, part);
					ISelection selection = finalSelection;
					int lineNumber = -1;
					IResource resource = ResourcesPlugin.getWorkspace()
							.getRoot();
					IBreakpoint[] breakpoints = DebugPlugin.getDefault()
							.getBreakpointManager().getBreakpoints(
									getDebugModelId());
					if (selection instanceof ITextSelection
							&& part instanceof ITextEditor) {
						// one based line number
						lineNumber = ((ITextSelection) selection)
								.getStartLine() + 1;
						resource = BreakpointUtils
								.getBreakpointResource((ITextEditor) part);
					}
					if (!(selection instanceof IStructuredSelection)) {
						selection = translateToMembers(part, finalSelection);
					}
					if (selection instanceof IStructuredSelection) {
						List fields = getFields((IStructuredSelection) selection);
						if (fields.isEmpty()) {
							report(ActionMessages.ToggleBreakpointAdapter_10,
									part);
							return Status.OK_STATUS;
						}
						for (Iterator i = fields.iterator(); i.hasNext();) {
							final Object element = i.next();
							int start = -1;
							int end = -1;
							final String watchExpression;
							if (element instanceof IField) {
								final IField field = (IField) element;
								final ISourceRange range = field.getNameRange();
								start = range.getOffset();
								end = range.getOffset() + range.getLength();
								watchExpression = field.getElementName();
							} else if (element instanceof IScriptVariable) {
								watchExpression = ((IScriptVariable) element)
										.getName();
							} else {
								continue;
							}
							boolean found = false;
							for (int j = 0; j < breakpoints.length; j++) {
								final IBreakpoint breakpoint = breakpoints[j];
								if (breakpoint instanceof IScriptWatchpoint
										&& breakpoint.getMarker() != null
										&& resource.equals(breakpoint
												.getMarker().getResource())) {
									IScriptWatchpoint wp = (IScriptWatchpoint) breakpoint;
									if (wp.getLineNumber() == lineNumber
											&& watchExpression.equals(wp
													.getFieldName())) {
										// delete existing breakpoint
										breakpoint.delete();
										found = true;
									}
								}
							}
							if (!found) {
								new ScriptWatchpoint(
										getDebugModelId(),
										resource,
										resource.getType() == IResource.FILE ? resource
												.getLocation()
												: null, lineNumber, start, end,
										watchExpression);
							}
						}
					} else {
						report(ActionMessages.ToggleBreakpointAdapter_2, part);
						return Status.OK_STATUS;
					}
				} catch (CoreException e) {
					return e.getStatus();
				}
				return Status.OK_STATUS;
			}
		};
		job.setSystem(true);
		job.schedule();
	}

	public void toggleBreakpoints(IWorkbenchPart part, ISelection selection)
			throws CoreException {
		toggleLineBreakpoints(part, selection);
	}

	public boolean canToggleBreakpoints(IWorkbenchPart part,
			ISelection selection) {
		return canToggleLineBreakpoints(part, selection);
	}

	public boolean canToggleSpawnpoints(IWorkbenchPart part,
			ITextSelection selection) {
		if (isRemote(part, selection)) {
			return false;
		}
		return true;
	}

	public void toggleSpawnpoints(final IWorkbenchPart part,
			final ITextSelection selection) throws CoreException {
		Job job = new Job("Script Toggle Spawnpoint") { //$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				final ITextEditor editor = getTextEditor(part);
				if (editor != null) {
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}

					try {
						report(null, part);

						int lineNumber = selection.getStartLine() + 1;

						final IBreakpoint breakpoint = BreakpointUtils
								.findLineBreakpoint(editor, lineNumber);

						if (breakpoint != null) {
							// if breakpoint already exists, delete it
							breakpoint.delete();
						} else {
							final IDocumentProvider documentProvider = editor
									.getDocumentProvider();
							if (documentProvider == null) {
								return Status.CANCEL_STATUS;
							}

							final IDocument document = documentProvider
									.getDocument(editor.getEditorInput());

							lineNumber = findBreakpointLine(document,
									lineNumber - 1, getValidator()) + 1;

							if (lineNumber != BREAKPOINT_LINE_NOT_FOUND) {
								// Check if already breakpoint set to the same
								// location
								if (BreakpointUtils.findLineBreakpoint(editor,
										lineNumber) == null) {
									BreakpointUtils.addSpawnpoint(editor,
											lineNumber);
								} else {
									report(
											MessageFormat
													.format(
															Messages.ScriptToggleBreakpointAdapter_breakpointAlreadySetAtLine,
															new Object[] { new Integer(
																	lineNumber) }),
											part);
								}
							} else {
								report(
										Messages.ScriptToggleBreakpointAdapter_invalidBreakpointPosition,
										part);
							}
						}
					} catch (CoreException e) {
						DLTKDebugUIPlugin.log(e);
					}
				}

				return Status.OK_STATUS;
			}

		};
		job.setSystem(true);
		job.schedule();
	}
}
