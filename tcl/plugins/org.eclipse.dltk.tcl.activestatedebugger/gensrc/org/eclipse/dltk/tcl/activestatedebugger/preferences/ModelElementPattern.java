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
 * $Id: ModelElementPattern.java,v 1.1 2009/03/09 06:29:41 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Element Pattern</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#getHandleIdentifier <em>Handle Identifier</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#isInclude <em>Include</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getModelElementPattern()
 * @model
 * @generated
 */
public interface ModelElementPattern extends EObject {
	/**
	 * Returns the value of the '<em><b>Handle Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Handle Identifier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Handle Identifier</em>' attribute.
	 * @see #setHandleIdentifier(String)
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getModelElementPattern_HandleIdentifier()
	 * @model required="true"
	 * @generated
	 */
	String getHandleIdentifier();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#getHandleIdentifier <em>Handle Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Handle Identifier</em>' attribute.
	 * @see #getHandleIdentifier()
	 * @generated
	 */
	void setHandleIdentifier(String value);

	/**
	 * Returns the value of the '<em><b>Include</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Include</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include</em>' attribute.
	 * @see #setInclude(boolean)
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getModelElementPattern_Include()
	 * @model id="true" required="true"
	 * @generated
	 */
	boolean isInclude();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#isInclude <em>Include</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include</em>' attribute.
	 * @see #isInclude()
	 * @generated
	 */
	void setInclude(boolean value);

} // ModelElementPattern
