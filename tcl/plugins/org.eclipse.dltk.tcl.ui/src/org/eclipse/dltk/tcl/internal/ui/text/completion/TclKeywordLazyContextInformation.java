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

import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.documentation.ScriptDocumentationAccess;
import org.eclipse.dltk.ui.text.completion.HTMLPrinter;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationExtension;
import org.eclipse.swt.graphics.Image;

public class TclKeywordLazyContextInformation implements IContextInformation,
		IContextInformationExtension {

	private final ICompletionProposal proposal;
	private final String target;
	private final ISourceModule module;
	private boolean informationComputed = false;
	private String information;

	/**
	 * @param iSourceModule
	 * @param completionProposal
	 */
	public TclKeywordLazyContextInformation(ICompletionProposal proposal,
			String target, ISourceModule module) {
		this.proposal = proposal;
		this.target = removeColons(target);
		this.module = module;
	}

	/**
	 * @param target2
	 * @return
	 */
	private static String removeColons(String s) {
		int i = 0;
		while (i < s.length() && s.charAt(i) == ':') {
			++i;
		}
		return s.substring(i);
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
					.getKeywordDocumentation(TclNature.NATURE_ID, module,
							target);
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
				return formatSynopsis(content.substring(start, matcher.start()));
			}
		}
		return proposal.getDisplayString();
	}

	/**
	 * @param synopsis
	 * @return
	 */
	private String formatSynopsis(String synopsis) {
		synopsis = CUT_HTML_TAGS.matcher(synopsis).replaceAll(EMPTY);
		final String[] parts = EOL.split(synopsis);
		final String keyword = target;
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < parts.length; ++i) {
			final String part = parts[i].trim();
			if (part.length() != 0 && part.startsWith(keyword)) {
				if (sb.length() != 0) {
					sb.append('\n');
				}
				sb.append(part);
			}
		}
		if (sb.length() == 0) {
			for (int i = 0; i < parts.length; ++i) {
				final String part = parts[i].trim();
				if (part.length() != 0) {
					if (sb.length() != 0) {
						sb.append('\n');
					}
					sb.append(part);
				}
			}
		}
		return sb.toString();
	}

	public boolean equals(Object obj) {
		if (obj instanceof TclKeywordLazyContextInformation) {
			final TclKeywordLazyContextInformation other = (TclKeywordLazyContextInformation) obj;
			final String displayString = proposal.getDisplayString();
			if (displayString != null) {
				if (!displayString.equals(other.proposal.getDisplayString())) {
					return false;
				}
			} else {
				if (other.proposal.getDisplayString() != null) {
					return false;
				}
			}
			return getContextInformationPosition() == other
					.getContextInformationPosition();
		}
		return false;
	}

	/*
	 * @see IContextInformationExtension#getContextInformationPosition()
	 */
	public int getContextInformationPosition() {
		if (proposal instanceof ICompletionProposalExtension) {
			return ((ICompletionProposalExtension) proposal)
					.getContextInformationPosition();
		}
		return -1;
	}

	private static final String EMPTY = ""; //$NON-NLS-1$

	private static final Pattern EOL = Pattern.compile("[\r\n]+"); //$NON-NLS-1$

	private static final Pattern HEADER = Pattern.compile(
			"<h\\d>(.*?)</h\\d>", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	private static final String SYNOPSIS = "SYNOPSIS"; //$NON-NLS-1$

	private static final Pattern CUT_HTML_TAGS = Pattern.compile("<[^>]+>"); //$NON-NLS-1$

}
