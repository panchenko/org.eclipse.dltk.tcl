/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.tests;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.tests.model.Activator;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class AllParseTests extends TestCase {

	static final String CHARSET = "ISO-8859-1"; //$NON-NLS-1$

	private static final String SCRIPTS_ZIP = "scripts/scripts.zip"; //$NON-NLS-1$

	public static TestSuite suite() {
		final TestSuite suite = new TestSuite();
		final URL scripts = Activator.getDefault().getBundle().getEntry(
				SCRIPTS_ZIP);
		if (scripts == null) {
			suite.addTest(new TestCase("error") { //$NON-NLS-1$
						protected void runTest() throws Throwable {
							fail(SCRIPTS_ZIP + " is not found"); //$NON-NLS-1$
						}
					});
		} else {
			try {
				final ZipInputStream zipInputStream = new ZipInputStream(
						scripts.openStream());
				try {
					int count = 0;
					ZipEntry entry;
					while ((entry = zipInputStream.getNextEntry()) != null) {
						final InputStream entryStream = new FilterInputStream(
								zipInputStream) {
							public void close() throws IOException {
								// empty
							}
						};
						final char[] content = Util.getInputStreamAsCharArray(
								entryStream, (int) entry.getSize(), CHARSET);
						final String testName = ++count + "-" + entry.getName(); //$NON-NLS-1$
						suite.addTest(new AllParseTests(testName, content));
						zipInputStream.closeEntry();
					}
				} finally {
					try {
						zipInputStream.close();
					} catch (IOException e) {
						// 
					}
				}
			} catch (final IOException e) {
				suite.addTest(new TestCase("IOException") { //$NON-NLS-1$
							protected void runTest() throws Throwable {
								throw e;
							}
						});
			}
		}
		return suite;
	}

	private final char[] content;

	public AllParseTests(String name, char[] content) {
		super(name);
		this.content = content;
	}

	protected void runTest() throws Throwable {
		System.out.print("Test " + getName()); //$NON-NLS-1$
		final ProblemCollector collector = new ProblemCollector();
		final ISourceParser parser = DLTKLanguageManager
				.getSourceParser(TclNature.NATURE_ID);
		long s1 = System.currentTimeMillis();
		ModuleDeclaration module1 = parser.parse(null, content, collector);
		long e1 = System.currentTimeMillis();
		// if (collector.hasErrors()) {
		// fail(collector.getErrors().toString());
		// }
		// Parse with new parser and compare results.
		TclParser newParser = new TclParser();
		NamespaceScopeProcessor processor = DefinitionManager.getInstance()
				.createProcessor();
		NewTclSourceParser newSourceParser = new NewTclSourceParser();

		long s2 = System.currentTimeMillis();
		TclModule module = newParser.parseModule(new String(content), null,
				processor);
		ModuleDeclaration module2 = newSourceParser.parse(null, module,
				collector);
		long e2 = System.currentTimeMillis();
		// String str1 = module1.toString();
		// String str2 = module2.toString();
		// TestCase.assertEquals(str1, str2);
		System.out.println("Time comparison:" + (e1 - s1) + "%%" + (e2 - s2));
	}
}
