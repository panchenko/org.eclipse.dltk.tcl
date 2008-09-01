package org.eclipse.dltk.tcl.internal.core.search.mixin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.mixin.MixinModel;
import org.eclipse.dltk.core.mixin.MixinModel.IMixinObjectInitializeListener;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.ITclMixinElement;

public class TclMixinModel {
	private static TclMixinModel instance;

	private final Map instances = new HashMap();

	public static TclMixinModel getInstance() {
		if (instance == null) {
			instance = new TclMixinModel();
		}
		return instance;
	}

	private TclMixinModel() {
	}

	private void bindObjectInitialization(MixinModel model) {
		model.addObjectInitializeListener(new IMixinObjectInitializeListener() {
			public void initialize(IMixinElement element, Object object,
					ISourceModule module) {
				if (object != null && object instanceof ITclMixinElement) {
					((ITclMixinElement) object).initialize(element, module,
							TclMixinModel.this);
				}
			}
		});
	}

	public MixinModel getMixin(IScriptProject project) {
		// Assert.isNotNull(project);
		synchronized (instances) {
			MixinModel mixinModel = (MixinModel) instances.get(project);
			if (mixinModel == null) {
				mixinModel = new MixinModel(DLTKLanguageManager
						.getLanguageToolkit(TclNature.NATURE_ID), project);
				instances.put(project, mixinModel);
				bindObjectInitialization(mixinModel);
			}
			return mixinModel;
		}
	}
}
