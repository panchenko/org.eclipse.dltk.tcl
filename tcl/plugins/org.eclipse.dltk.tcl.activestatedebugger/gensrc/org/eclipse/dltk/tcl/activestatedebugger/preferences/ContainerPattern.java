/**
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 * 
 *
 * $Id: ContainerPattern.java,v 1.2 2009/10/26 13:41:25 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.activestatedebugger.preferences;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Container Pattern</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ContainerPattern#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getContainerPattern()
 * @model
 * @generated
 * @since 2.0
 */
public interface ContainerPattern extends Pattern {

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ContainerType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.ContainerType
	 * @see #setType(ContainerType)
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getContainerPattern_Type()
	 * @model
	 * @generated
	 */
	ContainerType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ContainerPattern#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.ContainerType
	 * @see #getType()
	 * @generated
	 */
	void setType(ContainerType value);
} // ContainerPattern
