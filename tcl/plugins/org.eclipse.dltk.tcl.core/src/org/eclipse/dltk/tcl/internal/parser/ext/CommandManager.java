package org.eclipse.dltk.tcl.internal.parser.ext;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.PriorityDLTKExtensionManager;
import org.eclipse.dltk.core.SimpleClassNewInstanceDLTKExtensionManager;
import org.eclipse.dltk.core.SimpleDLTKExtensionManager.ElementInfo;
import org.eclipse.dltk.tcl.core.ITclCommandDetector;
import org.eclipse.dltk.tcl.core.ITclCommandProcessor;
import org.eclipse.dltk.tcl.core.TclPlugin;

public class CommandManager {
	private static final String ID_ATTR = "id";
	private static final String EXTENSION_PROCESSOR = TclPlugin.PLUGIN_ID
			+ ".tclCommandProcessor";
	private static final String EXTENSION_DETECTOR = TclPlugin.PLUGIN_ID
			+ ".tclCommandDetector";
	private static final String CLASS_ATTR = "class";

	private static class SimpleExtensionManager extends
			PriorityDLTKExtensionManager {
		public SimpleExtensionManager(String extensionPoint) {
			super(extensionPoint, ID_ATTR);
		}

		public Object getInitObject(ElementInfo ext) {
			try {
				if (ext != null) {
					IConfigurationElement cfg = (IConfigurationElement) ext.config;
					return createObject(cfg);
				}
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected Object createObject(IConfigurationElement cfg)
				throws CoreException {
			return cfg.createExecutableExtension(CLASS_ATTR);
		}

		public Object get(String name) {
			return getInitObject(getElementInfo(name));
		}

	}

	private SimpleExtensionManager commands = new SimpleExtensionManager(
			EXTENSION_PROCESSOR);
	private SimpleClassNewInstanceDLTKExtensionManager detectors = new SimpleClassNewInstanceDLTKExtensionManager(
			EXTENSION_DETECTOR);

	public ITclCommandProcessor getProcessor(String name) {
		return (ITclCommandProcessor) commands.get(name);
	}

	public ITclCommandDetector[] getDetectors() {
		ElementInfo[] objects = detectors.getElementInfos();
		ITclCommandDetector[] results = new ITclCommandDetector[objects.length];
		for (int i = 0; i < objects.length; i++) {
			try {
				results[i] = (ITclCommandDetector) detectors
						.createObject(objects[i]);
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	private static CommandManager sInstance = null;

	public static CommandManager getInstance() {
		if (sInstance == null) {
			sInstance = new CommandManager();
		}
		return sInstance;
	}
}
