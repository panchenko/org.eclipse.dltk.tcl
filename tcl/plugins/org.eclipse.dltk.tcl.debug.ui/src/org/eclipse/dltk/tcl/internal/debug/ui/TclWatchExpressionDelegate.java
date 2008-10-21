package org.eclipse.dltk.tcl.internal.debug.ui;

import org.eclipse.dltk.internal.debug.core.model.ScriptWatchExpressionDelegate;

public class TclWatchExpressionDelegate extends ScriptWatchExpressionDelegate {
	protected String prepareExpression(String expression) {
		if (expression.startsWith("$")) { //$NON-NLS-1$
			return "expr { " + expression + " }"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return expression;
	}
}
