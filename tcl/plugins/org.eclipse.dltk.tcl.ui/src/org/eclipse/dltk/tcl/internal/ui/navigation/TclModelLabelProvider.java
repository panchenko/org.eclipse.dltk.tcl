package org.eclipse.dltk.tcl.internal.ui.navigation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageElement;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageSourceModule;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesSourceModule;
import org.eclipse.dltk.tcl.internal.ui.TclImages;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TclModelLabelProvider extends LabelProvider {
	public String getText(Object element) {
		if (element instanceof TclPackageFragment) {
			return "Packages";
		} else if (element instanceof TclSourcesFragment) {
			return "Sources";
		} else if (element instanceof TclPackageElement) {
			TclPackageElement pkg = (TclPackageElement) element;
			String result = pkg.getPackageName();
			if (pkg.getVersion() != null) {
				result += " " + pkg.getVersion();
			}
			return result;
		} else if (element instanceof TclPackageSourceModule) {
			final TclPackageSourceModule module = (TclPackageSourceModule) element;
			return module.getElementName() + " ("
					+ module.getStorage().getFullPath().toString() + ")";
		} else if (element instanceof TclSourcesSourceModule) {
			TclSourcesSourceModule module = (TclSourcesSourceModule) element;
			IEnvironment environment = EnvironmentManager.getEnvironment(module
					.getScriptProject());
			String originalName = module.getOriginalName();
			String convertedPath = environment.convertPathToString(module
					.getFullPath());
			IFileHandle file = EnvironmentPathUtils.getFile(module);
			boolean exists = file != null && file.exists();
			String postFix = exists ? "" : " [Not available]";
			if (originalName != null && !originalName.equals(convertedPath)) {
				return originalName + " (" + convertedPath + ")" + postFix;
			}
			return convertedPath + postFix;
		}
		return null;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof TclSourcesFragment) {
			return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_LIBRARY_SRC);
		} else if (element instanceof IScriptFolder) {
			if (element instanceof TclPackageElement
					|| element instanceof TclSourcesFragment) {
				return null;
			}
			return getScriptFolderIcon();
		}
		return null;
	}

	/**
	 * @return
	 */
	private Image getScriptFolderIcon() {
		return getImageFor(TclImages.DESC_OBJS_FOLDER);
		// return PlatformUI.getWorkbench().getSharedImages().getImage(
		// ISharedImages.IMG_OBJ_FOLDER);
	}

	private Image getImageFor(ImageDescriptor descriptor) {
		Image image = registry.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			registry.put(descriptor, image);
		}
		return image;
	}

	private Map<ImageDescriptor, Image> registry = new HashMap<ImageDescriptor, Image>();

}
