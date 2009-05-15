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
package org.eclipse.dltk.tcl.ast;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complex String</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.ComplexString#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.ComplexString#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getComplexString()
 * @model
 * @generated
 */
public interface ComplexString extends TclArgument, ISubstitution {
	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.ast.TclArgument}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getComplexString_Arguments()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclArgument> getArguments();

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see #setKind(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getComplexString_Kind()
	 * @model
	 * @generated
	 */
	int getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.ComplexString#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see #getKind()
	 * @generated
	 */
	void setKind(int value);

} // ComplexString
