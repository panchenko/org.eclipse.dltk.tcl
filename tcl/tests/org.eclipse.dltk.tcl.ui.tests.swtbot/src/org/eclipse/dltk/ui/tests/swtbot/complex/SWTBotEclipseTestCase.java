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

import java.util.ConcurrentModificationException;

import junit.framework.TestCase;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
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
		System.out.println("Running swtbot test:" + getName());
		final Throwable[] exceptions = new Throwable[1];
		Thread t = new Thread() {
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
		long start = System.currentTimeMillis();
		// try {
		Display def = Display.getDefault();
		while (t.isAlive() && !def.isDisposed()) {
			try {
				if (def.readAndDispatch()) {
					def.sleep();
				}
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}

			if (exceptions[0] != null)
				throw exceptions[0];
			long cur = System.currentTimeMillis();
			if (cur - start > 600000) {
				throw new RuntimeException("Timeout in test");
			}
		}
		// } catch (Throwable tt) {
		// // tt.printStackTrace();
		// }
	}

	public void superRunBare() throws Throwable {
		super.runBare();
	}

	public static void closeWelcome() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IViewReference[] references = page.getViewReferences();
				for (int i = 0; i < references.length; i++) {
					if ("org.eclipse.ui.internal.introview"
							.equals(references[i].getId())) {
						IWorkbenchPart part = references[i].getPart(false);
						if (part != null) {
							part.dispose();
						}
					}
				}
			}
		});
	}
}