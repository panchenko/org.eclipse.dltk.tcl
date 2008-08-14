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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Switch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.definitions.Switch#getGroups <em>Groups</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.definitions.Switch#isCheckPrefix <em>Check Prefix</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.definitions.DefinitionsPackage#getSwitch()
 * @model
 * @generated
 */
public interface Switch extends Argument {
	/**
	 * Returns the value of the '<em><b>Groups</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.definitions.Group}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Groups</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.definitions.DefinitionsPackage#getSwitch_Groups()
	 * @model containment="true"
	 * @generated
	 */
	EList<Group> getGroups();

	/**
	 * Returns the value of the '<em><b>Check Prefix</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Check Prefix</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Check Prefix</em>' attribute.
	 * @see #setCheckPrefix(boolean)
	 * @see org.eclipse.dltk.tcl.definitions.DefinitionsPackage#getSwitch_CheckPrefix()
	 * @model default="false"
	 * @generated
	 */
	boolean isCheckPrefix();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.definitions.Switch#isCheckPrefix <em>Check Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Check Prefix</em>' attribute.
	 * @see #isCheckPrefix()
	 * @generated
	 */
	void setCheckPrefix(boolean value);

} // Switch
