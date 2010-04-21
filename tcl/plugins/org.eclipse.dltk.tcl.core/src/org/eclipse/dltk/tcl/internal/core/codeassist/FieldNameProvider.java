/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.core.codeassist;

import org.eclipse.dltk.codeassist.ICompletionNameProvider;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.tcl.internal.parser.OldTclParserUtils;

public class FieldNameProvider implements ICompletionNameProvider<IField> {

	private final String token;
	private final String prefix;

	public FieldNameProvider(String token) {
		this(token, Util.EMPTY_STRING);
	}

	public FieldNameProvider(String token, String prefix) {
		this.token = token;
		this.prefix = prefix;
	}

	public String getCompletion(IField field) {
		return OldTclParserUtils.processFieldName(field, token);
	}

	public String getName(IField field) {
		return prefix + OldTclParserUtils.processFieldName(field, token);
	}

}
