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
package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.core.ast.TclPackageDeclaration;

public class TclBuildPathPackageCollector extends ASTVisitor {

	private final List requireDirectives = new ArrayList();
	private final Set packagesRequired = new HashSet();
	private final Set packagesProvided = new HashSet();

	public void process(ModuleDeclaration declaration) {
		try {
			declaration.traverse(this);
		} catch (Exception e) {
			TclPlugin.error(e);
		}
	}

	public boolean visit(Statement s) throws Exception {
		if (s instanceof TclPackageDeclaration) {
			final TclPackageDeclaration pkg = (TclPackageDeclaration) s;
			if (pkg.getStyle() == TclPackageDeclaration.STYLE_REQUIRE) {
				if (PackagesManager.isValidPackageName(pkg.getName())) {
					requireDirectives.add(new TclPackageDeclaration(pkg));
					packagesRequired.add(pkg.getName());
				}
			} else if (pkg.getStyle() == TclPackageDeclaration.STYLE_IFNEEDED
					|| pkg.getStyle() == TclPackageDeclaration.STYLE_PROVIDE) {
				packagesProvided.add(pkg.getName());
			}
			return false;
		}
		return super.visit(s);
	}

	/**
	 * @return the requireDirectives
	 */
	public List getRequireDirectives() {
		return requireDirectives;
	}

	/**
	 * @return the packagesRequired
	 */
	public Set getPackagesRequired() {
		return packagesRequired;
	}

	/**
	 * @return the packagesProvided
	 */
	public Set getPackagesProvided() {
		return packagesProvided;
	}

}
