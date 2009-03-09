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
 * $Id: InstrumentationConfig.java,v 1.1 2009/03/09 06:29:41 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Instrumentation Config</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig#getModelElements <em>Model Elements</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig#getMode <em>Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getInstrumentationConfig()
 * @model
 * @generated
 */
public interface InstrumentationConfig extends EObject {
	/**
	 * Returns the value of the '<em><b>Model Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Elements</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getInstrumentationConfig_ModelElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModelElementPattern> getModelElements();

	/**
	 * Returns the value of the '<em><b>Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mode</em>' attribute.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode
	 * @see #setMode(InstrumentationMode)
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getInstrumentationConfig_Mode()
	 * @model
	 * @generated
	 */
	InstrumentationMode getMode();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode</em>' attribute.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode
	 * @see #getMode()
	 * @generated
	 */
	void setMode(InstrumentationMode value);

} // InstrumentationConfig
