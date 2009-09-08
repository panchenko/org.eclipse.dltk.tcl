/**
 * 
 */
package org.eclipse.dltk.tcl.internal.core.sources;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelStatus;
import org.eclipse.dltk.core.IProblemRequestor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.WorkingCopyOwner;
import org.eclipse.dltk.internal.core.AbstractSourceModule;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.OpenableElementInfo;
import org.eclipse.dltk.tcl.core.TclNature;

/**
 * Source module used in packages
 * 
 * @since 2.0
 */
public class TclSourcesPseudoSourceModule extends AbstractSourceModule {
	public TclSourcesPseudoSourceModule(ModelElement parent, String name,
			WorkingCopyOwner owner) {
		super(parent, name, owner);
	}

	/**
	 * @since 2.0
	 */
	@Override
	protected IStatus validateSourceModule(IDLTKLanguageToolkit toolkit,
			IResource resource) {
		return IModelStatus.VERIFIED_OK;
	}

	@Override
	protected boolean buildStructure(OpenableElementInfo info,
			IProgressMonitor pm, Map newElements, IResource underlyingResource)
			throws ModelException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public IModelElement[] getChildren() throws ModelException {
		return new IModelElement[0];
	}

	@Override
	public boolean hasChildren() throws ModelException {
		return false;
	}

	@Override
	public boolean isStructureKnown() throws ModelException {
		return true;
	}

	@Override
	protected char[] getBufferContent() throws ModelException {
		return "".toCharArray();
	}

	@Override
	protected String getModuleType() {
		return "Unknown source module";
	}

	@Override
	protected String getNatureId() throws CoreException {
		return TclNature.NATURE_ID;
	}

	@Override
	protected ISourceModule getOriginalSourceModule() {
		return this;
	}

	public void becomeWorkingCopy(IProblemRequestor problemRequestor,
			IProgressMonitor monitor) throws ModelException {
	}

	public void commitWorkingCopy(boolean force, IProgressMonitor monitor)
			throws ModelException {
	}

	public void discardWorkingCopy() throws ModelException {
	}

	public ISourceModule getWorkingCopy(WorkingCopyOwner owner,
			IProblemRequestor problemRequestor, IProgressMonitor monitor)
			throws ModelException {
		return this;
	}

	public boolean isWorkingCopy() {
		return true;
	}

	public void reconcile(boolean forceProblemDetection,
			WorkingCopyOwner owner, IProgressMonitor monitor)
			throws ModelException {
	}

	public IResource getResource() {
		return null;
	}

	public void delete(boolean force, IProgressMonitor monitor)
			throws ModelException {
	}

	public void move(IModelElement container, IModelElement sibling,
			String rename, boolean replace, IProgressMonitor monitor)
			throws ModelException {
	}

	public void rename(String name, boolean replace, IProgressMonitor monitor)
			throws ModelException {
	}

	public char[] getFileName() {
		return getElementName().toCharArray();
	}
}