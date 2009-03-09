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
 * $Id: ModelElementPatternImpl.java,v 1.1 2009/03/09 06:29:40 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.activestatedebugger.preferences.impl;

import org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Element Pattern</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ModelElementPatternImpl#getHandleIdentifier <em>Handle Identifier</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ModelElementPatternImpl#isInclude <em>Include</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelElementPatternImpl extends EObjectImpl implements ModelElementPattern {
	/**
	 * The default value of the '{@link #getHandleIdentifier() <em>Handle Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHandleIdentifier()
	 * @generated
	 * @ordered
	 */
	protected static final String HANDLE_IDENTIFIER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHandleIdentifier() <em>Handle Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHandleIdentifier()
	 * @generated
	 * @ordered
	 */
	protected String handleIdentifier = HANDLE_IDENTIFIER_EDEFAULT;

	/**
	 * The default value of the '{@link #isInclude() <em>Include</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInclude()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isInclude() <em>Include</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInclude()
	 * @generated
	 * @ordered
	 */
	protected boolean include = INCLUDE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelElementPatternImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PreferencesPackage.Literals.MODEL_ELEMENT_PATTERN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHandleIdentifier() {
		return handleIdentifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHandleIdentifier(String newHandleIdentifier) {
		String oldHandleIdentifier = handleIdentifier;
		handleIdentifier = newHandleIdentifier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.MODEL_ELEMENT_PATTERN__HANDLE_IDENTIFIER, oldHandleIdentifier, handleIdentifier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isInclude() {
		return include;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInclude(boolean newInclude) {
		boolean oldInclude = include;
		include = newInclude;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.MODEL_ELEMENT_PATTERN__INCLUDE, oldInclude, include));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__HANDLE_IDENTIFIER:
				return getHandleIdentifier();
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__INCLUDE:
				return isInclude() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__HANDLE_IDENTIFIER:
				setHandleIdentifier((String)newValue);
				return;
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__INCLUDE:
				setInclude(((Boolean)newValue).booleanValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__HANDLE_IDENTIFIER:
				setHandleIdentifier(HANDLE_IDENTIFIER_EDEFAULT);
				return;
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__INCLUDE:
				setInclude(INCLUDE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__HANDLE_IDENTIFIER:
				return HANDLE_IDENTIFIER_EDEFAULT == null ? handleIdentifier != null : !HANDLE_IDENTIFIER_EDEFAULT.equals(handleIdentifier);
			case PreferencesPackage.MODEL_ELEMENT_PATTERN__INCLUDE:
				return include != INCLUDE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (handleIdentifier: "); //$NON-NLS-1$
		result.append(handleIdentifier);
		result.append(", include: "); //$NON-NLS-1$
		result.append(include);
		result.append(')');
		return result.toString();
	}

} //ModelElementPatternImpl
