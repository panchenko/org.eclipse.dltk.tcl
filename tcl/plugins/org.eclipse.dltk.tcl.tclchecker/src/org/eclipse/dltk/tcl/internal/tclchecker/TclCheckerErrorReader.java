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
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;

public class TclCheckerErrorReader extends Thread {

	private final InputStream inputStream;
	private final IOutputProcessor processor;

	public TclCheckerErrorReader(InputStream inputStream,
			IOutputProcessor processor) {
		super("TclChecker stderr reader"); //$NON-NLS-1$
		this.inputStream = inputStream;
		this.processor = processor;
	}

	public void run() {
		final StringBuffer errorMessage = new StringBuffer();
		final BufferedReader input = new BufferedReader(new InputStreamReader(
				inputStream));
		try {
			String line;
			while ((line = input.readLine()) != null) {
				processor.processErrorLine(line);
				errorMessage.append(line).append('\n');
			}
		} catch (IOException e) {
			// ignore
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// ignore
			}
		}
		if (errorMessage.length() > 0) {
			TclCheckerPlugin.log(IStatus.WARNING,
					Messages.TclChecker_execution_error
							+ errorMessage.toString());
		}
	}

}
