package org.eclipse.dltk.tcl.internal.ui.text.folding;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.ui.text.folding.DefaultElementCommentResolver;
import org.eclipse.jface.text.Document;

public class TclElementCommentResolver extends DefaultElementCommentResolver {

	/**
	 * @param module
	 */
	public TclElementCommentResolver(ISourceModule module) {
		super(module);
	}

	/**
	 * @param module
	 * @param contents
	 */
	public TclElementCommentResolver(ISourceModule module, String contents) {
		super(module, contents);
	}

	protected int getSourceRangeEnd(Document d, IModelElement el)
			throws ModelException {
		return d.getLength();
	}
}
