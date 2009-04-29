package org.eclipse.dltk.tcl.internal.ui.navigation;

import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.ui.IModelCompareCategories;
import org.eclipse.dltk.ui.IModelCompareProvider;

public class TclModelCompareProvider implements IModelCompareProvider {
	public CompareResult category(Object parentElement) {
		if (parentElement instanceof TclSourcesFragment) {
			return new CompareResult(IModelCompareCategories.CONTAINER);
		}
		return null;
	}

	public CompareResult compare(Object element1, Object element2, int cat1,
			int cat2) {
		if (cat1 == cat2 && cat1 == IModelCompareCategories.CONTAINER) {
			if (element1 instanceof TclSourcesFragment) {
				return LESS;
			} else if (element2 instanceof TclSourcesFragment) {
				return GREATER;
			}
		}
		return null;
	}
}
