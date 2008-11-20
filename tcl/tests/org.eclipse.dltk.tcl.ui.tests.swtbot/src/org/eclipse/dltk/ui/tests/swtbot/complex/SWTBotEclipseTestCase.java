/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation
 *******************************************************************************/
package org.eclipse.dltk.ui.tests.swtbot.complex;

import junit.framework.TestCase;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;

import org.eclipse.swt.widgets.Display;

public class SWTBotEclipseTestCase extends TestCase {
	protected SWTEclipseBot bot = new SWTEclipseBot();

	public SWTBotEclipseTestCase() {
		super();
	}

	public SWTBotEclipseTestCase(String name) {
		super(name);
	}

	@Override
	public void runBare() throws Throwable {
		final Throwable[] exceptions = new Throwable[1];
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					superRunBare();
				} catch (Throwable e) {
					exceptions[0] = e;
				}
			}
		};
		t.start();
		while (t.isAlive()) {
			Display.getDefault().readAndDispatch();
			if (exceptions[0] != null)
				throw exceptions[0];
		}
	}

	public void superRunBare() throws Throwable {
		super.runBare();
	}
}