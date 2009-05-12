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
 *   <li>{@link org.eclipse.dltk.tcl.ast.ComplexString#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getComplexString()
 * @model
 * @generated
 */
public interface ComplexString extends TclArgument, ISubstitution {
	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.ast.TclArgument}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' reference list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getComplexString_Arguments()
	 * @model
	 * @generated
	 */
	EList<TclArgument> getArguments();

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getComplexString_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.ComplexString#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // ComplexString
