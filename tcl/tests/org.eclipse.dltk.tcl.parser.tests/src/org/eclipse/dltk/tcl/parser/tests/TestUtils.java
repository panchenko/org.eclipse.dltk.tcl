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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;

public class TestUtils {
	public static class CodeModel {

		private int[] codeLineLengths;
		private int[] codeStarts;
		private int[] codeEnds;

		public CodeModel(String code) {
			String[] codeLines = code.split("\n");
			int count = codeLines.length;

			this.codeLineLengths = new int[count];
			this.codeStarts = new int[count];
			this.codeEnds = new int[count];

			int sum = 0;
			for (int i = 0; i < count; ++i) {
				this.codeLineLengths[i] = sum;
				String trim = codeLines[i].trim();
				int off = codeLines[i].indexOf(trim);
				this.codeStarts[i] = sum + off;
				this.codeEnds[i] = trim.length();
				sum += codeLines[i].length() + 1;
			}
		}

		public int[] getBounds(int lineNumber) {
			if (lineNumber < this.codeLineLengths.length) {
				int start = this.codeStarts[lineNumber];
				int end = this.codeEnds[lineNumber];
				return new int[] { start, start + end };
			}
			return new int[] { 0, 0 };
		}

		public int getLineNumber(int start, int end) {
			int len = codeLineLengths.length;
			for (int i = 0; i < len - 1; ++i) {
				int s = this.codeLineLengths[i];
				int e = start + this.codeLineLengths[i + 1] - 1;

				if (start <= s && end <= e) {
					return i + 1;
				}
			}
			return this.codeLineLengths.length;
		}
	}

	public static void outErrors(final String source, TclErrorCollector errors) {
		System.out.println("-----------------ERRORS----------------------\n");
		final CodeModel model = new CodeModel(source);
		errors.reportAll(new ITclErrorReporter() {
			public void report(int code, String message, String[] extraMessage, int start,
					int end, int kind) {
				System.out.println((kind == ITclErrorReporter.ERROR ? "Error:"
						: "Warning/Info:")
						+ code
						+ " ("
						+ start
						+ ","
						+ end
						+ ") message:"
						+ message);
				int line = model.getLineNumber(start, end);
				int[] bounds = model.getBounds(line - 1);
				String prefix = source.substring(bounds[0], bounds[1]);
				System.out.println(prefix);
				outBlock(start - bounds[0], end - bounds[0]);
			}
		});
		System.out.println("=============================================");
	}

	public static void outCode(String source, int startPos, int endPos) {
		System.out.println("------------------code-----------------------\n"
				+ source.replace('\t', ' '));
		outBlock(startPos, endPos);
//		System.out.println("=============================================");
	}

	private static void outBlock(int startPos, int endPos) {
		for (int i = 0; i < startPos; i++)
			System.out.print(" ");
		System.out.print("^");
		for (int i = 0; i < (endPos - startPos - 2); i++)
			System.out.print(" ");
		System.out.println("^");
	}

	public static String getContents(URL url) throws IOException {
		InputStream input = url.openStream();
		return getContents(input);
	}

	public static String getContents(InputStream input) throws IOException {
		StringBuffer result = new StringBuffer();
		input = new BufferedInputStream(input, 4096);
		try {
			// Simple copy
			int ch = -1;
			while ((ch = input.read()) != -1) {
				result.append((char) ch);
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return result.toString();
	}

	public static void exractZipInto(String location, URL entry,
			String[] skipFiles) throws IOException, CoreException {
		InputStream openStream = entry.openStream();
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
				openStream, 4096));
		File root = new File(location);
		root.mkdirs();
		while (true) {
			ZipEntry nextEntry = zis.getNextEntry();
			if (nextEntry != null) {
				String name = nextEntry.getName();
				if (!nextEntry.isDirectory()) {
					boolean skip = false;
					for (int i = 0; i < skipFiles.length; i++) {
						if (name.equals(skipFiles[i])) {
							skip = true;
							break;
						}
					}
					if (skip) {
						zis.closeEntry();
						continue;
					}
					FileOutputStream fileOutput = new FileOutputStream(
							new File(new Path(location).append(name)
									.toOSString()));

					byte[] buf = new byte[1024];
					int len;
					OutputStream arrayOut = new BufferedOutputStream(
							fileOutput, 4096);
					while ((len = zis.read(buf)) > 0) {
						arrayOut.write(buf, 0, len);
					}
					arrayOut.close();
				}
				zis.closeEntry();
			} else {
				break;
			}
		}
		openStream.close();
	}

	public static void exractFilesInto(String location, URL entry,
			String[] skipFiles) throws IOException, CoreException {
		InputStream openStream = entry.openStream();
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
				openStream, 4096));
		File root = new File(location);
		root.mkdirs();
		while (true) {
			ZipEntry nextEntry = zis.getNextEntry();
			if (nextEntry != null) {
				String name = nextEntry.getName();
				if (!nextEntry.isDirectory()) {
					boolean found = false;
					for (int i = 0; i < skipFiles.length; i++) {
						if (name.equals(skipFiles[i])) {
							found = true;
						}
					}
					if (!found) {
						zis.closeEntry();
						continue;
					}
					FileOutputStream fileOutput = new FileOutputStream(
							new File(new Path(location).append(name)
									.toOSString()));
					byte[] buf = new byte[1024];
					int len;
					OutputStream arrayOut = new BufferedOutputStream(
							fileOutput, 4096);
					while ((len = zis.read(buf)) > 0) {
						arrayOut.write(buf, 0, len);
					}
					arrayOut.close();
				}
				zis.closeEntry();
			} else {
				break;
			}
		}
		openStream.close();
	}
}
