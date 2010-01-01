package org.eclipse.dltk.tcl.formatter.tests;

import java.util.Map;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.formatter.tests.AbstractFormatterTest;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;

public abstract class AbstractTclFormatterTest extends AbstractFormatterTest {

	@Override
	protected IScriptFormatter createFormatter(Map<String, Object> preferences) {
		return preferences != null ? new TestTclFormatter(Util.LINE_SEPARATOR,
				preferences) : new TestTclFormatter();
	}

}
