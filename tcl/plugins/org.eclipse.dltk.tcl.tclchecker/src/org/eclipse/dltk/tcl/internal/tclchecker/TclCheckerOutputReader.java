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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public class TclCheckerOutputReader extends Thread {

	private final InputStream inputStream;

	private final IOutputProcessor processor;

	/**
	 * @param inputStream
	 * @param console
	 * @param processor
	 */
	public TclCheckerOutputReader(InputStream inputStream,
			IOutputProcessor processor) {
		super("TclChecker stdout reader"); //$NON-NLS-1$
		this.inputStream = inputStream;
		this.processor = processor;
	}

	public void run() {
		final BufferedReader input = new BufferedReader(new InputStreamReader(
				inputStream));
		try {
			String line;
			while ((line = input.readLine()) != null) {
				processor.processLine(line);
			}
		} catch (IOException e) {
			// ignore
		} catch (CoreException e) {
			TclCheckerPlugin.log(IStatus.ERROR,
					Messages.TclChecker_execution_error, e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

}
