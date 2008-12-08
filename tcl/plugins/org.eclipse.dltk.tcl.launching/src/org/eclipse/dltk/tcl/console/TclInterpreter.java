/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.console;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.compiler.CharOperation;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.console.ConsoleRequest;
import org.eclipse.dltk.console.IScriptExecResult;
import org.eclipse.dltk.console.IScriptConsoleIO;
import org.eclipse.dltk.console.IScriptInterpreter;
import org.eclipse.dltk.console.InterpreterResponse;
import org.eclipse.dltk.console.ScriptExecResult;
import org.eclipse.dltk.console.ShellResponse;

public class TclInterpreter implements IScriptInterpreter, ConsoleRequest {

	private static final String COMPLETE_COMMAND = "complete"; //$NON-NLS-1$

	private static final String DESCRIBE_COMMAND = "describe"; //$NON-NLS-1$

	private static final String CLOSE_COMMAND = "close"; //$NON-NLS-1$

	private IScriptConsoleIO protocol;

	private int state;

	private List closeRunnables = new ArrayList();

	private List initialListeners = new ArrayList();

	// IScriptInterpreter
	public IScriptExecResult exec(String command) throws IOException {
		InterpreterResponse response = protocol.execInterpreter(command);
		if (response != null) {
			state = response.getState();
			return new ScriptExecResult(response.getContent(), response
					.isError());
		} else {
			return null;
		}
	}

	public boolean isValid() {
		return protocol != null;
	}

	public int getState() {
		return state;
	}

	// IScriptInterpreterShell
	public List getCompletions(String commandLine, int position)
			throws IOException {

		String[] args = new String[] { commandLine, Integer.toString(position) };

		ShellResponse response = protocol.execShell(COMPLETE_COMMAND, args);

		return response.getCompletions();
	}

	public String getDescription(String commandLine, int position)
			throws IOException {
		String[] args = new String[] { commandLine, Integer.toString(position) };

		ShellResponse response = protocol.execShell(DESCRIBE_COMMAND, args);

		return response != null ? response.getDescription() : Util.EMPTY_STRING;
	}

	public String[] getNames(String type) throws IOException {

		return null;
	}

	public void close() throws IOException {
		try {
			if (protocol != null) {
				protocol.execShell(CLOSE_COMMAND, CharOperation.NO_STRINGS);
				protocol.close();
			}
			// run all close runnables.
			for (Iterator iterator = this.closeRunnables.iterator(); iterator
					.hasNext();) {
				Runnable op = (Runnable) iterator.next();
				op.run();
			}
		} catch (SocketException e) {
		}
	}

	// IScriptConsoleProtocol
	public void consoleConnected(IScriptConsoleIO protocol) {
		this.protocol = protocol;
		for (Iterator iterator = this.initialListeners.iterator(); iterator
				.hasNext();) {
			Runnable op = (Runnable) iterator.next();
			Thread t = new Thread(op);
			t.run();
		}
		this.initialListeners.clear();
	}

	public void addCloseOperation(Runnable runnable) {
		this.closeRunnables.add(runnable);
	}

	public void addInitialListenerOperation(Runnable runnable) {
		if (this.protocol != null) {
			new Thread(runnable).start();
		} else {
			this.initialListeners.add(runnable);
		}
	}

	public InputStream getInitialOutputStream() {
		return protocol.getInitialResponseStream();
	}
}
