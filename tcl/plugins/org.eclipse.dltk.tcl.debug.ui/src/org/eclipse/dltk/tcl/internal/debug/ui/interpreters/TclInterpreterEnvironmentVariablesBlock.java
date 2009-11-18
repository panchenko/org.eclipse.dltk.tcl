/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterEnvironmentVariablesBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.viewsupport.ImageDescriptorRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Control used to edit the libraries associated with a Interpreter install
 */
public class TclInterpreterEnvironmentVariablesBlock extends
		AbstractInterpreterEnvironmentVariablesBlock {

	public TclInterpreterEnvironmentVariablesBlock(AddScriptInterpreterDialog d) {
		super(d);
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new DecoratingLabelProvider((ILabelProvider) super
				.getLabelProvider(), new EnvironmentVariableDecorator());
	}

	private class EnvironmentVariableDecorator extends BaseLabelProvider
			implements ILabelDecorator {

		public Image decorateImage(Image image, Object element) {
			if (element instanceof EnvironmentVariable) {
				final EnvironmentVariable env = (EnvironmentVariable) element;
				if (!isSafe(env) || !env.validate().isOK()) {
					return registry.get(new DecorationOverlayIcon(image,
							DLTKPluginImages.DESC_OVR_WARNING,
							IDecoration.BOTTOM_LEFT));
				}
			}
			return image;
		}

		public String decorateText(String text, Object element) {
			return text;
		}

		@Override
		public void dispose() {
			super.dispose();
			registry.dispose();
		}

		private final ImageDescriptorRegistry registry = new ImageDescriptorRegistry(
				false);

		private boolean isSafe(EnvironmentVariable env) {
			final IEnvironment environment = getEnvironment();
			if (environment != null) {
				final IExecutionEnvironment execEnvironment = (IExecutionEnvironment) environment
						.getAdapter(IExecutionEnvironment.class);
				if (execEnvironment != null) {
					return execEnvironment.isSafeEnvironmentVariable(env
							.getName());
				}
			}
			return true;
		}

	}
}
