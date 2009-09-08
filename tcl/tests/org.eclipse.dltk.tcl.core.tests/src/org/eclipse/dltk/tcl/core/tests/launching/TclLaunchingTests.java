package org.eclipse.dltk.tcl.core.tests.launching;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.core.internal.environment.LocalEnvironment;
import org.eclipse.dltk.core.tests.launching.IFileVisitor;
import org.eclipse.dltk.core.tests.launching.PathFilesContainer;
import org.eclipse.dltk.core.tests.launching.ScriptLaunchingTests;
import org.eclipse.dltk.launching.AbstractScriptLaunchConfigurationDelegate;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerConstants;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerPlugin;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerRunner;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.debug.TclDebugConstants;
import org.eclipse.dltk.tcl.launching.TclLaunchConfigurationDelegate;

@SuppressWarnings("nls")
public class TclLaunchingTests extends ScriptLaunchingTests {
	private static final String DBGP_TCLDEBUG_PATH = "/home/dltk/apps/tcl_debug"; //$NON-NLS-1$
	private static final String DBGP_TCLDEBUG_SUFFIX = Platform.OS_WIN32
			.equals(Platform.getOS()) ? ".exe" : Util.EMPTY_STRING; //$NON-NLS-1$
	private static final String DBGP_TCLDEBUG_FILE = "dbgp_tcldebug" + DBGP_TCLDEBUG_SUFFIX; //$NON-NLS-1$

	class Searcher implements IFileVisitor {
		private String debuggingEnginePath = null;

		public boolean visit(IFileHandle file) {
			if (file.isFile() && file.getName().startsWith(DBGP_TCLDEBUG_FILE)) {
				debuggingEnginePath = file.toOSString();
			}

			if (file.isDirectory() && debuggingEnginePath == null) {
				return true;
			} else {
				return false;
			}
		}

		public String getPath() {
			return debuggingEnginePath;
		}
	};

	public TclLaunchingTests(String name) {
		super("org.eclipse.dltk.tcl.core.tests", name);
	}

	public TclLaunchingTests(String testProjectName, String name) {
		super(testProjectName, name);
	}

	public static Test suite() {
		return new Suite(TclLaunchingTests.class);
	}

	protected String getProjectName() {
		return "launching";
	}

	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

	protected String getDebugModelId() {
		return TclDebugConstants.DEBUG_MODEL_ID;
	}

	protected ILaunchConfiguration createLaunchConfiguration(String arguments) {
		return createTestLaunchConfiguration(getNatureId(), getProjectName(),
				"src/test.tcl", arguments);
	}

	protected void startLaunch(ILaunch launch, final IInterpreterInstall install)
			throws CoreException {
		final AbstractScriptLaunchConfigurationDelegate delegate = new TclLaunchConfigurationDelegate() {

			public IInterpreterInstall getInterpreterInstall(
					ILaunchConfiguration configuration) throws CoreException {
				return install;
			}
		};
		delegate.launch(launch.getLaunchConfiguration(),
				launch.getLaunchMode(), launch, null);
	}

	public void testDebugTclsh() throws Exception {
		initializeActiveStateDebugEngine();
		DebugEventStats stats = super.internalTestDebug("tclsh");
		int suspendCount = stats.getSuspendCount();
		assertTrue(suspendCount > 0);

		assertTrue(stats.getResumeCount() > 0);

		// Checking extended events count
		assertTrue(stats.getBeforeVmStarted() > 0);
		assertTrue(stats.getBeforeCodeLoaded() > 0);
		assertTrue(stats.getBeforeResumeCount() > 0);
		assertTrue(stats.getBeforeSuspendCount() > 0);
	}

	public void testDebugWish() throws Exception {
		initializeActiveStateDebugEngine();
		DebugEventStats stats = super.internalTestDebug("wish");
		int suspendCount = stats.getSuspendCount();
		assertTrue(suspendCount > 0);

		assertTrue(stats.getResumeCount() > 0);

		// Checking extended events count
		assertTrue(stats.getBeforeVmStarted() > 0);
		assertTrue(stats.getBeforeCodeLoaded() > 0);
		assertTrue(stats.getBeforeResumeCount() > 0);
		assertTrue(stats.getBeforeSuspendCount() > 0);
	}

	public void testDebugExpect() throws Exception {
		initializeActiveStateDebugEngine();
		DebugEventStats stats = super.internalTestDebug("expect");
		int suspendCount = stats.getSuspendCount();
		assertTrue(suspendCount > 0);

		assertTrue(stats.getResumeCount() > 0);

		// Checking extended events count
		assertTrue(stats.getBeforeVmStarted() > 0);
		assertTrue(stats.getBeforeCodeLoaded() > 0);
		assertTrue(stats.getBeforeResumeCount() > 0);
		assertTrue(stats.getBeforeSuspendCount() > 0);
	}

	private boolean initialized = false;

	protected String getTclDebuggerPath() {
		String path = DBGP_TCLDEBUG_PATH + "." + Platform.getOS() + "."
				+ Platform.getOSArch() + "/" + DBGP_TCLDEBUG_FILE;
		if (new File(path).exists()) {
			return path;
		}
		path = DBGP_TCLDEBUG_PATH + "/" + DBGP_TCLDEBUG_FILE;
		if (new File(path).exists()) {
			return path;
		}
		// Lets search if we could not found in default location.
		PathFilesContainer container = new PathFilesContainer(
				EnvironmentManager.getLocalEnvironment());
		Searcher searcher = new Searcher();
		container.accept(searcher);
		path = searcher.getPath();
		assertNotNull("Couldn't find ActiveState debugger", path);
		return path;
	}

	private void initializeActiveStateDebugEngine() {
		if (initialized) {
			return;
		}
		Preferences pluginPreferences = TclActiveStateDebuggerPlugin
				.getDefault().getPluginPreferences();
		pluginPreferences.setValue(TclDebugConstants.DEBUGGING_ENGINE_ID_KEY,
				TclActiveStateDebuggerRunner.ENGINE_ID);

		Map<IEnvironment, String> map = new HashMap<IEnvironment, String>();
		map.put(LocalEnvironment.getInstance(), getTclDebuggerPath());
		String keyValue = EnvironmentPathUtils.encodePaths(map);
		System.out.println(keyValue);
		pluginPreferences.setValue(
				TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PATH_KEY,
				keyValue);
		initialized = true;
	}

	protected IInterpreterInstall[] getPredefinedInterpreterInstalls() {
		IInterpreterInstallType[] installTypes = ScriptRuntime
				.getInterpreterInstallTypes(TclNature.NATURE_ID);
		int id = 0;
		List<IInterpreterInstall> installs = new ArrayList<IInterpreterInstall>();
		for (int i = 0; i < installTypes.length; i++) {
			String installId = getNatureId() + "_";
			createAddInstall(installs, "/usr/bin/tclsh", installId
					+ Integer.toString(++id), installTypes[i]);
			createAddInstall(installs, "/usr/bin/expect", installId
					+ Integer.toString(++id), installTypes[i]);
			createAddInstall(installs, "/usr/bin/wish", installId
					+ Integer.toString(++id), installTypes[i]);
		}
		if (installs.size() > 0) {
			return installs.toArray(new IInterpreterInstall[installs.size()]);
		}
		return searchInstalls(TclNature.NATURE_ID);
	}

	protected boolean hasPredefinedInterpreters() {
		return true;
	}

	public void testTclsh() throws Exception {
		String NAME = "tclsh";
		this.internalTestRequiredInterpreterAvailable(NAME);
		this.internalTestRun(NAME);
	}

	public void testWish() throws Exception {
		String NAME = "wish";
		this.internalTestRequiredInterpreterAvailable(NAME);
		this.internalTestRun(NAME);
	}

	public void testExpect() throws Exception {
		String NAME = "expect";
		this.internalTestRequiredInterpreterAvailable(NAME);
		this.internalTestRun(NAME, SKIP_STDOUT_TEST);
	}

	protected void configureEnvironment(Map env) {
		// This is required by wish to function correctly
		// env.put("DISPLAY", "");
	}
}
