/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackageInfoImpl.java,v 1.2 2009/05/05 11:16:30 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tcl Package Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl#getSources <em>Sources</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl#isFetched <em>Fetched</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TclPackageInfoImpl extends EObjectImpl implements TclPackageInfo {
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
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSources() <em>Sources</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSources()
	 * @generated
	 * @ordered
	 */
	protected EList<String> sources;

	/**
	 * The default value of the '{@link #isFetched() <em>Fetched</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFetched()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FETCHED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFetched() <em>Fetched</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFetched()
	 * @generated
	 * @ordered
	 */
	protected boolean fetched = FETCHED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<TclPackageInfo> dependencies;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TclPackageInfoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TclPackagesPackage.Literals.TCL_PACKAGE_INFO;
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
					TclPackagesPackage.TCL_PACKAGE_INFO__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.TCL_PACKAGE_INFO__VERSION, oldVersion,
					version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getSources() {
		if (sources == null) {
			sources = new EDataTypeUniqueEList<String>(String.class, this,
					TclPackagesPackage.TCL_PACKAGE_INFO__SOURCES);
		}
		return sources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFetched() {
		return fetched;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFetched(boolean newFetched) {
		boolean oldFetched = fetched;
		fetched = newFetched;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.TCL_PACKAGE_INFO__FETCHED, oldFetched,
					fetched));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclPackageInfo> getDependencies() {
		if (dependencies == null) {
			dependencies = new EObjectResolvingEList<TclPackageInfo>(
					TclPackageInfo.class, this,
					TclPackagesPackage.TCL_PACKAGE_INFO__DEPENDENCIES);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case TclPackagesPackage.TCL_PACKAGE_INFO__NAME:
			return getName();
		case TclPackagesPackage.TCL_PACKAGE_INFO__VERSION:
			return getVersion();
		case TclPackagesPackage.TCL_PACKAGE_INFO__SOURCES:
			return getSources();
		case TclPackagesPackage.TCL_PACKAGE_INFO__FETCHED:
			return isFetched();
		case TclPackagesPackage.TCL_PACKAGE_INFO__DEPENDENCIES:
			return getDependencies();
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
		case TclPackagesPackage.TCL_PACKAGE_INFO__NAME:
			setName((String) newValue);
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__VERSION:
			setVersion((String) newValue);
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__SOURCES:
			getSources().clear();
			getSources().addAll((Collection<? extends String>) newValue);
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__FETCHED:
			setFetched((Boolean) newValue);
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__DEPENDENCIES:
			getDependencies().clear();
			getDependencies().addAll(
					(Collection<? extends TclPackageInfo>) newValue);
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
		case TclPackagesPackage.TCL_PACKAGE_INFO__NAME:
			setName(NAME_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__VERSION:
			setVersion(VERSION_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__SOURCES:
			getSources().clear();
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__FETCHED:
			setFetched(FETCHED_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_PACKAGE_INFO__DEPENDENCIES:
			getDependencies().clear();
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
		case TclPackagesPackage.TCL_PACKAGE_INFO__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case TclPackagesPackage.TCL_PACKAGE_INFO__VERSION:
			return VERSION_EDEFAULT == null ? version != null
					: !VERSION_EDEFAULT.equals(version);
		case TclPackagesPackage.TCL_PACKAGE_INFO__SOURCES:
			return sources != null && !sources.isEmpty();
		case TclPackagesPackage.TCL_PACKAGE_INFO__FETCHED:
			return fetched != FETCHED_EDEFAULT;
		case TclPackagesPackage.TCL_PACKAGE_INFO__DEPENDENCIES:
			return dependencies != null && !dependencies.isEmpty();
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
		result.append(", version: ");
		result.append(version);
		result.append(", sources: ");
		result.append(sources);
		result.append(", fetched: ");
		result.append(fetched);
		result.append(')');
		return result.toString();
	}

} //TclPackageInfoImpl
