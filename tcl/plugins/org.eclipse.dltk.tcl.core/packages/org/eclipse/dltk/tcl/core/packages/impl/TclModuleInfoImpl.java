/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclModuleInfoImpl.java,v 1.1 2009/04/23 10:58:24 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;

import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tcl Module Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl#getRequired <em>Required</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl#getProvided <em>Provided</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl#getSourced <em>Sourced</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TclModuleInfoImpl extends EObjectImpl implements TclModuleInfo {
	/**
	 * The default value of the '{@link #getHandle() <em>Handle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHandle()
	 * @generated
	 * @ordered
	 */
	protected static final String HANDLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHandle() <em>Handle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHandle()
	 * @generated
	 * @ordered
	 */
	protected String handle = HANDLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRequired() <em>Required</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequired()
	 * @generated
	 * @ordered
	 */
	protected EList<TclSourceEntry> required;

	/**
	 * The cached value of the '{@link #getProvided() <em>Provided</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvided()
	 * @generated
	 * @ordered
	 */
	protected EList<TclSourceEntry> provided;

	/**
	 * The cached value of the '{@link #getSourced() <em>Sourced</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourced()
	 * @generated
	 * @ordered
	 */
	protected EList<TclSourceEntry> sourced;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TclModuleInfoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TclPackagesPackage.Literals.TCL_MODULE_INFO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclSourceEntry> getRequired() {
		if (required == null) {
			required = new EObjectContainmentEList<TclSourceEntry>(
					TclSourceEntry.class, this,
					TclPackagesPackage.TCL_MODULE_INFO__REQUIRED);
		}
		return required;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclSourceEntry> getProvided() {
		if (provided == null) {
			provided = new EObjectContainmentEList<TclSourceEntry>(
					TclSourceEntry.class, this,
					TclPackagesPackage.TCL_MODULE_INFO__PROVIDED);
		}
		return provided;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclSourceEntry> getSourced() {
		if (sourced == null) {
			sourced = new EObjectContainmentEList<TclSourceEntry>(
					TclSourceEntry.class, this,
					TclPackagesPackage.TCL_MODULE_INFO__SOURCED);
		}
		return sourced;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TclPackagesPackage.TCL_MODULE_INFO__REQUIRED:
			return ((InternalEList<?>) getRequired()).basicRemove(otherEnd,
					msgs);
		case TclPackagesPackage.TCL_MODULE_INFO__PROVIDED:
			return ((InternalEList<?>) getProvided()).basicRemove(otherEnd,
					msgs);
		case TclPackagesPackage.TCL_MODULE_INFO__SOURCED:
			return ((InternalEList<?>) getSourced())
					.basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHandle() {
		return handle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHandle(String newHandle) {
		String oldHandle = handle;
		handle = newHandle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.TCL_MODULE_INFO__HANDLE, oldHandle,
					handle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case TclPackagesPackage.TCL_MODULE_INFO__HANDLE:
			return getHandle();
		case TclPackagesPackage.TCL_MODULE_INFO__REQUIRED:
			return getRequired();
		case TclPackagesPackage.TCL_MODULE_INFO__PROVIDED:
			return getProvided();
		case TclPackagesPackage.TCL_MODULE_INFO__SOURCED:
			return getSourced();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case TclPackagesPackage.TCL_MODULE_INFO__HANDLE:
			setHandle((String) newValue);
			return;
		case TclPackagesPackage.TCL_MODULE_INFO__REQUIRED:
			getRequired().clear();
			getRequired().addAll(
					(Collection<? extends TclSourceEntry>) newValue);
			return;
		case TclPackagesPackage.TCL_MODULE_INFO__PROVIDED:
			getProvided().clear();
			getProvided().addAll(
					(Collection<? extends TclSourceEntry>) newValue);
			return;
		case TclPackagesPackage.TCL_MODULE_INFO__SOURCED:
			getSourced().clear();
			getSourced()
					.addAll((Collection<? extends TclSourceEntry>) newValue);
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
		case TclPackagesPackage.TCL_MODULE_INFO__HANDLE:
			setHandle(HANDLE_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_MODULE_INFO__REQUIRED:
			getRequired().clear();
			return;
		case TclPackagesPackage.TCL_MODULE_INFO__PROVIDED:
			getProvided().clear();
			return;
		case TclPackagesPackage.TCL_MODULE_INFO__SOURCED:
			getSourced().clear();
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
		case TclPackagesPackage.TCL_MODULE_INFO__HANDLE:
			return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT
					.equals(handle);
		case TclPackagesPackage.TCL_MODULE_INFO__REQUIRED:
			return required != null && !required.isEmpty();
		case TclPackagesPackage.TCL_MODULE_INFO__PROVIDED:
			return provided != null && !provided.isEmpty();
		case TclPackagesPackage.TCL_MODULE_INFO__SOURCED:
			return sourced != null && !sourced.isEmpty();
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
		result.append(" (handle: ");
		result.append(handle);
		result.append(')');
		return result.toString();
	}

} //TclModuleInfoImpl
