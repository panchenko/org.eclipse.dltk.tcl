package org.eclipse.dltk.tcl.internal.structure;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.PriorityDLTKExtensionManager;
import org.eclipse.dltk.core.SimpleClassNewInstanceDLTKExtensionManager;
import org.eclipse.dltk.core.SimpleDLTKExtensionManager.ElementInfo;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.structure.ITclModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuilderDetector;

public class ModelBuilderManager {
	private static final String ID_ATTR = "id";
	private static final String EXTENSION_PROCESSOR = TclPlugin.PLUGIN_ID
			+ ".tclModelBuilder";
	private static final String EXTENSION_DETECTOR = TclPlugin.PLUGIN_ID
			+ ".tclModelBuilderDetector";
	private static final String CLASS_ATTR = "class";

	private static class SimpleExtensionManager extends
			PriorityDLTKExtensionManager {
		public SimpleExtensionManager(String extensionPoint) {
			super(extensionPoint, ID_ATTR);
		}

		public Object getInitObject(ElementInfo ext) {
			try {
				if (ext != null) {
					if (ext.object == null) {
						ext.object = createObject(ext.config);
					}
					return ext.object;
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

	public ITclModelBuilder getModelBuilder(String name) {
		return (ITclModelBuilder) commands.get(name);
	}

	public ITclModelBuilderDetector[] getDetectors() {
		ElementInfo[] objects = detectors.getElementInfos();
		ITclModelBuilderDetector[] results = new ITclModelBuilderDetector[objects.length];
		for (int i = 0; i < objects.length; i++) {
			try {
				results[i] = (ITclModelBuilderDetector) detectors
						.createObject(objects[i]);
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	private static ModelBuilderManager sInstance = null;

	public static synchronized ModelBuilderManager getInstance() {
		if (sInstance == null) {
			sInstance = new ModelBuilderManager();
		}
		return sInstance;
	}
}
