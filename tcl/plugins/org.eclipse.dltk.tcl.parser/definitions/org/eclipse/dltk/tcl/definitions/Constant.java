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
package org.eclipse.dltk.tcl.definitions;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constant</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.definitions.Constant#isStrictMatch <em>Strict Match</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.definitions.DefinitionsPackage#getConstant()
 * @model
 * @generated
 */
public interface Constant extends Argument {
	/**
	 * Returns the value of the '<em><b>Strict Match</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Strict Match</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Strict Match</em>' attribute.
	 * @see #setStrictMatch(boolean)
	 * @see org.eclipse.dltk.tcl.definitions.DefinitionsPackage#getConstant_StrictMatch()
	 * @model default="false"
	 * @generated
	 */
	boolean isStrictMatch();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.definitions.Constant#isStrictMatch <em>Strict Match</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Strict Match</em>' attribute.
	 * @see #isStrictMatch()
	 * @generated
	 */
	void setStrictMatch(boolean value);

} // Constant
