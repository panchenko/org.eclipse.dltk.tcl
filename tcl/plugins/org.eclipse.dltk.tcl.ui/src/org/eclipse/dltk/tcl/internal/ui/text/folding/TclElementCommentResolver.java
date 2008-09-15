package org.eclipse.dltk.tcl.internal.ui.text.folding;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.ui.text.folding.DefaultElementCommentResolver;
import org.eclipse.jface.text.Document;

public class TclElementCommentResolver extends DefaultElementCommentResolver {

	protected int getSourceRangeEnd(Document d, IModelElement el)
			throws ModelException {
		return d.getLength();
	}
}
