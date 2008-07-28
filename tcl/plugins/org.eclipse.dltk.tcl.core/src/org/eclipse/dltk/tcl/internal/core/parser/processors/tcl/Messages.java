package org.eclipse.dltk.tcl.internal.core.parser.processors.tcl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.core.parser.processors.tcl.messages"; //$NON-NLS-1$
	public static String TclIfProcessor_missingCondition;
	public static String TclIfProcessor_incorrectElse;
	public static String TclIfProcessor_incorrectElseBlock;
	public static String TclIfProcessor_missingThenBlock;
	public static String TclIfProcessor_incorrectCondition;
	public static String TclIfProcessor_incorrectThenBlock;
	public static String TclIfProcessor_unexpectedStatements;
	public static String TclProcProcessor_Empty_Proc_Name;
	public static String TclProcProcessor_Wrong_Number_of_Arguments;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
