package org.eclipse.dltk.tcl.internal.ui.actions;

import org.eclipse.dltk.ui.actions.ScriptOpenSearchPageAction;

public class OpenTclSearchPageAction extends ScriptOpenSearchPageAction {

	private static final String TCL_SEARCH_PAGE_ID = "org.eclipse.dltk.ui.TclSearchPage";

	@Override
	protected String getSearchPageId() {
		return TCL_SEARCH_PAGE_ID;
	}
}
