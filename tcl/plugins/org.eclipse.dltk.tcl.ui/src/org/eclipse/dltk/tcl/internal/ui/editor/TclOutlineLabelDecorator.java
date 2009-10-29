/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.editor;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.ast.TclConstants;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.viewsupport.ImageDescriptorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.swt.graphics.Image;

public class TclOutlineLabelDecorator extends BaseLabelProvider implements
		ILabelDecorator {
	public TclOutlineLabelDecorator() {
	}

	public String decorateText(String text, Object element) {
		return text;
	}

	private static ImageDescriptor getDecoration(int flags) {
		if ((flags & Modifiers.AccGlobal) != 0) {
			return DLTKPluginImages.DESC_OVR_FIELD_GLOBAL;
		} else if ((flags == TclConstants.TCL_FIELD_TYPE_NAMESPACE)) {
			return DLTKPluginImages.DESC_OVR_FIELD_NAMESPACE;
		} else if ((flags & Modifiers.AccUpVar) != 0) {
			return DLTKPluginImages.DESC_OVR_FIELD_UPVAR;
		} else if ((flags == TclConstants.TCL_FIELD_TYPE_INDEX)) {
			return DLTKPluginImages.DESC_OVR_FIELD_INDEX;
		} else {
			return null;
		}
	}

	public Image decorateImage(Image image, Object obj) {
		try {
			if (obj instanceof IMember) {
				IMember member = (IMember) obj;
				ImageDescriptor decoration = getDecoration(member.getFlags());
				if (decoration != null) {
					return registry.get(new DecorationOverlayIcon(image,
							decoration, IDecoration.TOP_RIGHT));
				}
			}
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return image;
	}

	private final ImageDescriptorRegistry registry = new ImageDescriptorRegistry(
			false);

	@Override
	public void dispose() {
		super.dispose();
		registry.dispose();
	}
}
