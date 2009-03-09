/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerPlugin;
import org.eclipse.dltk.ui.ScriptElementImageProvider;
import org.eclipse.dltk.ui.viewsupport.StorageLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class InstrumentationPatternLabelProvider extends LabelProvider {

	private final StorageLabelProvider storageLabelProvider = new StorageLabelProvider();
	private final ScriptElementImageProvider elementImageProvider = new ScriptElementImageProvider();

	@Override
	public String getText(Object element) {
		if (element instanceof ModelElementPattern) {
			final IModelElement me = DLTKCore.create(((IModelElement) element)
					.getHandleIdentifier());
			if (me != null) {
				return me.getPath().toString();
			}
		}
		return element.toString();
	}

	@Override
	public Image getImage(Object element) {
		final IModelElement me = DLTKCore.create(((IModelElement) element)
				.getHandleIdentifier());
		if (me != null) {
			return elementImageProvider.getImageLabel(me,
					ScriptElementImageProvider.SMALL_ICONS);
		}
		return super.getImage(element);
	}

	private Image globImage;

	private Image getGlobImage() {
		if (globImage == null) {
			ImageDescriptor descriptor = TclActiveStateDebuggerPlugin
					.imageDescriptorFromPlugin(
							TclActiveStateDebuggerPlugin.PLUGIN_ID,
							"icons/full/obj16/glob-pattern.gif"); //$NON-NLS-1$
			if (descriptor != null) {
				globImage = descriptor.createImage();
			}
		}
		return globImage;
	}

	@Override
	public void dispose() {
		if (globImage != null) {
			globImage.dispose();
			globImage = null;
		}
		elementImageProvider.dispose();
		storageLabelProvider.dispose();
		super.dispose();
	}

}
