package org.eclipse.dltk.tcl.ui.tests.folding;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.tcl.internal.ui.text.folding.TclElementCommentResolver;
import org.eclipse.dltk.tcl.ui.tests.TclUITestsPlugin;
import org.eclipse.dltk.ui.text.folding.IElementCommentResolver;

public class TclCommentElementTests extends AbstractModelTests {

	private static final String PROJECT_NAME = "comments";

	public TclCommentElementTests(String name) {
		super(TclUITestsPlugin.PLUGIN_NAME, name);
	}

	public static Suite suite() {
		return new Suite(TclCommentElementTests.class);
	}

	public void setUpSuite() throws Exception {
		super.setUpSuite();
		setUpScriptProject(PROJECT_NAME);
	}

	public void tearDownSuite() throws Exception {
		deleteProject(PROJECT_NAME);
		super.tearDownSuite();
	}

	public void testCommentPositioning1() throws ModelException {
		ISourceModule module = getSourceModule(PROJECT_NAME, "src",
				"comments1.tcl");

		assertEquals("p1", getElementByComment(module, "p1_before"));
		assertEquals("p1", getElementByComment(module, "p1_inside"));

		assertNull(getElementByComment(module, "eof_comment"));
	}

	/**
	 * Gets a string pattern, searches for the comment that contains this
	 * pattern and returns IModelElement to which the comment corresponds
	 */
	protected String getElementByComment(ISourceModule module,
			String commentPattern) throws ModelException {
		final IElementCommentResolver resolver = new TclElementCommentResolver(
				module);
		IModelElement el = resolver.getElementByCommentPosition(module
				.getSource().indexOf(commentPattern), 0);
		if (el != null)
			return el.getElementName();
		else
			return null;
	}
}
