/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/

package org.eclipse.dltk.tcl.parser.tests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.tcl.internal.tclchecker.Messages;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerHelper;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerProblem;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerProblemDescription;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.parser.tests.TestUtils.CodeModel;
import org.eclispe.dltk.tcl.parser.internal.tests.Activator;
import org.osgi.framework.Bundle;

public class TclCheckerDLTKErrorComparisonTests extends TestCase {
	IProject project;

	
	public void test001() throws Exception {
		String fileName = "main1.tcl";

		Bundle bundle = Platform
				.getBundle("org.eclipse.dltk.tcl.parser.tests.ats");
		IPath loc = Activator.getDefault().getStateLocation();
		TestUtils.exractFilesInto(loc.toOSString(), bundle
				.getEntry("/alfa.zip"), new String[] { fileName });

		String fileLoc = loc.append(fileName).toOSString();
		final String contents = TestUtils.getContents(new FileInputStream(
				fileLoc));
		CodeModel model = new CodeModel(contents);
		List<TclCheckerProblem> list = check(fileLoc);
		TclParser parser = new TclParser();
		NamespaceScopeProcessor processor = TestDefinitionManager
				.createProcessor();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, false);
		TclErrorCollector col = new TclErrorCollector();

		/* List<TclCommand> commands = */parser.parse(contents, col, processor);

		System.out.println("-----------------source----------------------\n");
		col.reportAll(new ITclErrorReporter() {
			public void report(int code, String message, int start, int end,
					int kind) {
				System.out.println((kind == ITclErrorReporter.ERROR ? "Error:"
						: "Warning/Info:")
						+ code
						+ " ("
						+ start
						+ ","
						+ end
						+ ") message:"
						+ message + "\n" + contents.substring(start, end));
			}
		});
		System.out.println("=============================================");
		for (TclCheckerProblem tclCheckerProblem : list) {
			System.out.println();
			TclCheckerProblemDescription descr = tclCheckerProblem
					.getDescription();
			int[] bounds = model
					.getBounds(tclCheckerProblem.getLineNumber() - 1);
			System.out
					.println(((descr.getCategory() == TclCheckerProblemDescription.ERROR) ? "Error:"
							: "Warning/Info:")
							+ descr.getMessage()
							+ " ("
							+ bounds[0]
							+ ","
							+ bounds[1]
							+ ") message:"
							+ descr.getExplanation()
							+ "\n" + contents.substring(bounds[0], bounds[1]));
		}
	}

	public List<TclCheckerProblem> check(String file) {
		List<TclCheckerProblem> problems = new ArrayList<TclCheckerProblem>();
		IExecutionEnvironment execEnvironment = (IExecutionEnvironment) EnvironmentManager
				.getLocalEnvironment().getAdapter(IExecutionEnvironment.class);
		IDeployment deployment = execEnvironment.createDeployment();

		Process process;
		BufferedReader input = null;

		int scanned = 0;
		int checked = 0;

		Map map = execEnvironment.getEnvironmentVariables(false);

		String[] env = new String[map.size()];
		int i = 0;
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) map.get(key);
			env[i] = key + "=" + value; //$NON-NLS-1$
			++i;
		}
		String[] cmdLine = new String[] { getTclChecker(), file };
		try {
			process = execEnvironment.exec(cmdLine, null, env);

			input = new BufferedReader(new InputStreamReader(process
					.getInputStream()));

			String line;
			while ((line = input.readLine()) != null) {
				TclCheckerProblem problem = TclCheckerHelper.parseProblem(line);
				if (problem != null) {
					problems.add(problem);
				}
			}
			StringBuffer errorMessage = new StringBuffer();
			// We need also read errors.
			input = new BufferedReader(new InputStreamReader(process
					.getErrorStream()));

			while ((line = input.readLine()) != null) {
				errorMessage.append(line).append('\n');
			}
			if (errorMessage.length() > 0) {
				TclCheckerPlugin.log(IStatus.ERROR,
						Messages.TclChecker_execution_error
								+ errorMessage.toString());
			}
		} catch (Exception e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} finally {
			deployment.dispose();
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
			}
		}
		return problems;
	}

	private String getTclChecker() {
		return "/home/dltk/apps/bin/tclchecker";
	}
}
