/**
 * <copyright>
 * </copyright>
 *
 * $Id: UserCorrectionImpl.java,v 1.1 2009/04/28 11:00:04 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User Correction</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.UserCorrectionImpl#getOriginalValue <em>Original Value</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.UserCorrectionImpl#getUserValue <em>User Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserCorrectionImpl extends EObjectImpl implements UserCorrection {
	/**
	 * The default value of the '{@link #getOriginalValue() <em>Original Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalValue()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGINAL_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginalValue() <em>Original Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalValue()
	 * @generated
	 * @ordered
	 */
	protected String originalValue = ORIGINAL_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getUserValue() <em>User Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserValue()
	 * @generated
	 * @ordered
	 */
	protected static final String USER_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUserValue() <em>User Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserValue()
	 * @generated
	 * @ordered
	 */
	protected String userValue = USER_VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UserCorrectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TclPackagesPackage.Literals.USER_CORRECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOriginalValue() {
		return originalValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalValue(String newOriginalValue) {
		String oldOriginalValue = originalValue;
		originalValue = newOriginalValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.USER_CORRECTION__ORIGINAL_VALUE,
					oldOriginalValue, originalValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUserValue() {
		return userValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUserValue(String newUserValue) {
		String oldUserValue = userValue;
		userValue = newUserValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.USER_CORRECTION__USER_VALUE,
					oldUserValue, userValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case TclPackagesPackage.USER_CORRECTION__ORIGINAL_VALUE:
			return getOriginalValue();
		case TclPackagesPackage.USER_CORRECTION__USER_VALUE:
			return getUserValue();
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
		case TclPackagesPackage.USER_CORRECTION__ORIGINAL_VALUE:
			setOriginalValue((String) newValue);
			return;
		case TclPackagesPackage.USER_CORRECTION__USER_VALUE:
			setUserValue((String) newValue);
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
		case TclPackagesPackage.USER_CORRECTION__ORIGINAL_VALUE:
			setOriginalValue(ORIGINAL_VALUE_EDEFAULT);
			return;
		case TclPackagesPackage.USER_CORRECTION__USER_VALUE:
			setUserValue(USER_VALUE_EDEFAULT);
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
		case TclPackagesPackage.USER_CORRECTION__ORIGINAL_VALUE:
			return ORIGINAL_VALUE_EDEFAULT == null ? originalValue != null
					: !ORIGINAL_VALUE_EDEFAULT.equals(originalValue);
		case TclPackagesPackage.USER_CORRECTION__USER_VALUE:
			return USER_VALUE_EDEFAULT == null ? userValue != null
					: !USER_VALUE_EDEFAULT.equals(userValue);
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (originalValue: ");
		result.append(originalValue);
		result.append(", userValue: ");
		result.append(userValue);
		result.append(')');
		return result.toString();
	}

} //UserCorrectionImpl
