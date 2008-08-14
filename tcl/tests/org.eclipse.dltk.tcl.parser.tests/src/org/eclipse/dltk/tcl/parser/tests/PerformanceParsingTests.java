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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.ITclErrorConstants;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.PerformanceMonitor;
import org.eclipse.dltk.tcl.parser.TclError;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.parser.tests.TestUtils.CodeModel;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

public class PerformanceParsingTests extends TestCase {
	private static final String NEW_PARSE_TIME = "NEW PARSE TIME:";
	private static final String GLOBAL_NEW_PARSE_TIME = "GLOBAL NEW PARSE TIME:";
	private long index = 0;
	IProject project;

	@Before
	public void setUp() throws Exception {
		String name = "test_project_name";
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject(name);
		Bundle bundle = Platform
				.getBundle("org.eclipse.dltk.tcl.parser.tests.ats");
		TestCase.assertNotNull(bundle);

		// TODO: Add CREATE some files.
		project.create(new NullProgressMonitor());
		project.open(new NullProgressMonitor());
	}

	@Test
	public void testOriginalParserPerformance() throws Exception {
		File file = new File(project.getLocation().toOSString());
		// FileOutputStream fout = new FileOutputStream(
		// "/home/dltk/sources/log.txt");
		processFiles(file, null/* fout */);
	}

	private void processFiles(File file, FileOutputStream fout)
			throws Exception, IOException {
		final BufferedWriter writer = fout != null ? new BufferedWriter(
				new OutputStreamWriter(fout)) : null;
		PerformanceMonitor.getDefault().begin(GLOBAL_NEW_PARSE_TIME);
		final TclErrorCollector collector = new TclErrorCollector();
		traverse(file, new IOperation() {
			@Override
			public void run(File file) {
				try {
					if (file.getName().endsWith(".tcl")
							|| file.getName().endsWith(".exp")
							|| file.getName().endsWith(".xotcl")
							|| file.getName().endsWith(".itcl")) {
						index++;
						long startTime = System.currentTimeMillis();
						TclParser parser = new TclParser();
						NamespaceScopeProcessor processor = TestDefinitionManager
								.createProcessor();
						parser.setOptionValue(
								ITclParserOptions.REPORT_UNKNOWN_AS_ERROR,
								false);
						FileInputStream stream = new FileInputStream(file);
						TclErrorCollector col = new TclErrorCollector();

						try {
							String source = TestUtils.getContents(stream);
							CodeModel model = new CodeModel(source);
							PerformanceMonitor.getDefault().begin(
									NEW_PARSE_TIME);
							List<TclCommand> commands = parser.parse(source,
									col, processor);
							PerformanceMonitor.getDefault().end(NEW_PARSE_TIME);
							// Write code fragments with errors
							if (writer != null) {
								writer
										.write("\n#==================================================================\n");
								writer
										.write("\n#==================================================================\n");
								writer
										.write("\n#==================================================================\n");
							}
							if (writer != null)
								writer.write("#file:" + file.getAbsolutePath()
										+ "\n");
							TclError[] errors = col.getErrors();
							for (int i = 0; i < errors.length; i++) {
								if (writer != null)
									writer
											.write("#-------------------------------------------------\n");
								String message = errors[i].getMessage();
								int start = errors[i].getStart();
								int end = errors[i].getEnd();
								String code = source.substring(start, end);
								if (writer != null)
									writer
											.write("#"
													+ (errors[i].getErrorKind() == ITclErrorConstants.ERROR ? "Error"
															: "Warning") + "\n");
								int line = model.getLineNumber(start, end) - 1;
								if (line < 0) {
									line = 0;
								}
								if (writer != null)
									writer.write("#" + message + " (" + start
											+ "," + end + ") line:" + line
											+ "\n");
								int[] bounds = model.getBounds(line - 1);
								if (writer != null)
									writer.write("#line:"
											+ source.substring(bounds[0],
													bounds[1]) + "\n");
								if (writer != null)
									writer.write(code + "\n");
							}
						} finally {
							stream.close();
						}
						collector.addAll(col);
						long endTime = System.currentTimeMillis();
						System.out.println(file.getAbsolutePath() + " index:"
								+ String.valueOf(index) + " time:"
								+ String.valueOf(endTime - startTime));
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
				}
			}
		});
		if (writer != null)
			writer.close();
		if (fout != null)
			fout.close();
		PerformanceMonitor.getDefault().end(GLOBAL_NEW_PARSE_TIME);
		PerformanceMonitor.getDefault().print();
		System.out.println("ERRORS count:" + collector.getCount());
	}

	private interface IOperation {
		void run(File file);
	}

	private void traverse(File file, IOperation op) throws Exception {
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				if (listFiles[i].isFile()) {
					op.run(listFiles[i]);
				} else if (listFiles[i].isDirectory()) {
					traverse(listFiles[i], op);
				}
			}
		} else {
			op.run(file);
		}
	}
}
