/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclProjectInfoImpl.java,v 1.2 2009/04/28 11:00:04 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;
import org.eclipse.dltk.tcl.core.packages.TclProjectInfo;

import org.eclipse.dltk.tcl.core.packages.UserCorrection;
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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tcl Project Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl#getModules <em>Modules</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl#getPackageCorrections <em>Package Corrections</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TclProjectInfoImpl extends EObjectImpl implements TclProjectInfo {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getModules() <em>Modules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModules()
	 * @generated
	 * @ordered
	 */
	protected EList<TclModuleInfo> modules;

	/**
	 * The cached value of the '{@link #getPackageCorrections() <em>Package Corrections</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageCorrections()
	 * @generated
	 * @ordered
	 */
	protected EList<UserCorrection> packageCorrections;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TclProjectInfoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TclPackagesPackage.Literals.TCL_PROJECT_INFO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.TCL_PROJECT_INFO__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclModuleInfo> getModules() {
		if (modules == null) {
			modules = new EObjectContainmentEList<TclModuleInfo>(
					TclModuleInfo.class, this,
					TclPackagesPackage.TCL_PROJECT_INFO__MODULES);
		}
		return modules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UserCorrection> getPackageCorrections() {
		if (packageCorrections == null) {
			packageCorrections = new EObjectContainmentEList<UserCorrection>(
					UserCorrection.class, this,
					TclPackagesPackage.TCL_PROJECT_INFO__PACKAGE_CORRECTIONS);
		}
		return packageCorrections;
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
		case TclPackagesPackage.TCL_PROJECT_INFO__MODULES:
			return ((InternalEList<?>) getModules())
					.basicRemove(otherEnd, msgs);
		case TclPackagesPackage.TCL_PROJECT_INFO__PACKAGE_CORRECTIONS:
			return ((InternalEList<?>) getPackageCorrections()).basicRemove(
					otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case TclPackagesPackage.TCL_PROJECT_INFO__NAME:
			return getName();
		case TclPackagesPackage.TCL_PROJECT_INFO__MODULES:
			return getModules();
		case TclPackagesPackage.TCL_PROJECT_INFO__PACKAGE_CORRECTIONS:
			return getPackageCorrections();
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
		case TclPackagesPackage.TCL_PROJECT_INFO__NAME:
			setName((String) newValue);
			return;
		case TclPackagesPackage.TCL_PROJECT_INFO__MODULES:
			getModules().clear();
			getModules().addAll((Collection<? extends TclModuleInfo>) newValue);
			return;
		case TclPackagesPackage.TCL_PROJECT_INFO__PACKAGE_CORRECTIONS:
			getPackageCorrections().clear();
			getPackageCorrections().addAll(
					(Collection<? extends UserCorrection>) newValue);
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
		case TclPackagesPackage.TCL_PROJECT_INFO__NAME:
			setName(NAME_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_PROJECT_INFO__MODULES:
			getModules().clear();
			return;
		case TclPackagesPackage.TCL_PROJECT_INFO__PACKAGE_CORRECTIONS:
			getPackageCorrections().clear();
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
		case TclPackagesPackage.TCL_PROJECT_INFO__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case TclPackagesPackage.TCL_PROJECT_INFO__MODULES:
			return modules != null && !modules.isEmpty();
		case TclPackagesPackage.TCL_PROJECT_INFO__PACKAGE_CORRECTIONS:
			return packageCorrections != null && !packageCorrections.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //TclProjectInfoImpl
