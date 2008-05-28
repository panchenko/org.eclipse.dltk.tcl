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
package org.eclipse.dltk.tcl.internal.ui.text.completion;

import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.documentation.ScriptDocumentationAccess;
import org.eclipse.dltk.ui.text.completion.HTMLPrinter;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

public class TclKeywordLazyContextInformation implements IContextInformation {

	private final ICompletionProposal proposal;
	private boolean informationComputed = false;
	private String information;

	/**
	 * @param completionProposal
	 */
	public TclKeywordLazyContextInformation(ICompletionProposal proposal) {
		this.proposal = proposal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jface.text.contentassist.IContextInformation#
	 * getContextDisplayString()
	 */
	public String getContextDisplayString() {
		return proposal.getDisplayString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContextInformation#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jface.text.contentassist.IContextInformation#
	 * getInformationDisplayString()
	 */
	public String getInformationDisplayString() {
		if (!informationComputed) {
			informationComputed = true;
			information = computeInformation();
		}
		return information;
	}

	/**
	 * Returns all the documentation for this keyword
	 * 
	 * @return
	 */
	private String getInfo() {
		try {
			final Reader reader = ScriptDocumentationAccess
					.getHTMLContentReader(TclNature.NATURE_ID, proposal
							.getDisplayString());
			if (reader != null) {
				return HTMLPrinter.read(reader);
			}
		} catch (ModelException e) {
			DLTKUIPlugin.log(e);
		}
		return null;
	}

	/**
	 * @return
	 */
	private String computeInformation() {
		final String content = getInfo();
		if (content == null) {
			return proposal.getDisplayString();
		}
		final Matcher matcher = HEADER.matcher(content);
		int start = -1;
		while (matcher.find()) {
			if (start < 0) {
				if (matcher.group(1).toUpperCase().indexOf(SYNOPSIS) >= 0) {
					start = matcher.end();
				}
			} else {
				String synopsis = content.substring(start, matcher.start());
				return CUT_HTML_TAGS.matcher(synopsis).replaceAll("").trim(); //$NON-NLS-1$
			}
		}
		return proposal.getDisplayString();
	}

	private static final Pattern HEADER = Pattern.compile(
			"<h\\d>(.*?)</h\\d>", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	private static final String SYNOPSIS = "SYNOPSIS"; //$NON-NLS-1$

	private static final Pattern CUT_HTML_TAGS = Pattern.compile("<[^>]+>"); //$NON-NLS-1$

}
