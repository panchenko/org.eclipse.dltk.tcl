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

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTEclipseBot;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

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
		System.out.println("Running swtbot test: " + getName());
		final Throwable[] exceptions = new Throwable[1];
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					superRunBare();
				} catch (Throwable e) {
					exceptions[0] = e;
					System.out.println("Exception catched:" + e.getMessage());
				}
			}
		};
		t.start();
		final long start = System.currentTimeMillis();
		final Display display = Display.getDefault();
		final Runnable timerRunnable = new Runnable() {
			public void run() {
				// EMPTY
			}
		};
		display.timerExec(50, timerRunnable);
		try {
			while (t.isAlive() && !display.isDisposed()) {
				try {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				} catch (SWTException e) {
					e.printStackTrace();
				}
				long cur = System.currentTimeMillis();
				if (cur - start > 600000) {
					throw new RuntimeException("Timeout in test");
				}
			}
		} finally {
			display.timerExec(-1, timerRunnable);
		}
		while (display.readAndDispatch()) {
			// NOP
		}
		if (exceptions[0] != null)
			throw exceptions[0];
	}

	public void superRunBare() throws Throwable {
		super.runBare();
	}

	public static void closeWelcome() {
		Display.getDefault().syncExec(new Runnable() {

			final String introId = "org.eclipse.ui.internal.introview"; //$NON-NLS-1$

			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IViewReference[] references = page.getViewReferences();
				for (int i = 0; i < references.length; i++) {
					final IViewReference reference = references[i];
					if (introId.equals(reference.getId())) {
						IWorkbenchPage p = reference.getPage();
						if (p != null) {
							p.hideView(reference);
						}
					}
				}
			}
		});
	}
}
