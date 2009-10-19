package org.eclipse.dltk.tcl.core;

import org.eclipse.osgi.util.NLS;

/**
 * @since 2.0
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.core.messages"; //$NON-NLS-1$
	public static String TclInterpreterMessages_6;
	public static String TclInterpreterMessages_DeployingFileWithListOfPackages;
	public static String TclInterpreterMessages_DeployingPackageInformationScript;
	public static String TclInterpreterMessages_FetchInterpreterPackagesInfo;
	public static String TclInterpreterMessages_FetchInterpreterSources;
	public static String TclInterpreterMessages_ProcessingPackagesInfo;
	public static String TclInterpreterMessages_RetrieveListOfAvailablePackages;
	public static String TclInterpreterMessages_RetrievePackageInformationSources;
	public static String TclInterpreterMessages_RunningPackageInfoScript;
	public static String TclInterpreterMessages_SavePackagesInfo;
	
	public static String AddTclInterpreterDialog_RebuildJobName;
	public static String AddTclInterpreterDialog_RebuildProjectsTaskName;
	public static String AddTclInterpreterDialog_RebuildProjectTaskName;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
