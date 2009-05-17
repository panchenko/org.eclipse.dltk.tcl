/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclInterpreterInfoImpl.java,v 1.3 2009/05/17 11:39:13 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import java.util.Collection;

import java.util.Date;
import org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tcl Interpreter Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl#getInstallLocation <em>Install Location</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl#getPackages <em>Packages</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl#isFetched <em>Fetched</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl#getFetchedAt <em>Fetched At</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl#getEnvironment <em>Environment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TclInterpreterInfoImpl extends EObjectImpl implements
		TclInterpreterInfo {
	/**
	 * The default value of the '{@link #getInstallLocation() <em>Install Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstallLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String INSTALL_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInstallLocation() <em>Install Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstallLocation()
	 * @generated
	 * @ordered
	 */
	protected String installLocation = INSTALL_LOCATION_EDEFAULT;

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
	 * The cached value of the '{@link #getPackages() <em>Packages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<TclPackageInfo> packages;

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
	 * The default value of the '{@link #getFetchedAt() <em>Fetched At</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFetchedAt()
	 * @generated
	 * @ordered
	 */
	protected static final Date FETCHED_AT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFetchedAt() <em>Fetched At</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFetchedAt()
	 * @generated
	 * @ordered
	 */
	protected Date fetchedAt = FETCHED_AT_EDEFAULT;

	/**
	 * The default value of the '{@link #getEnvironment() <em>Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnvironment()
	 * @generated
	 * @ordered
	 */
	protected static final String ENVIRONMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnvironment() <em>Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnvironment()
	 * @generated
	 * @ordered
	 */
	protected String environment = ENVIRONMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TclInterpreterInfoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TclPackagesPackage.Literals.TCL_INTERPRETER_INFO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInstallLocation() {
		return installLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInstallLocation(String newInstallLocation) {
		String oldInstallLocation = installLocation;
		installLocation = newInstallLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.TCL_INTERPRETER_INFO__INSTALL_LOCATION,
					oldInstallLocation, installLocation));
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
					TclPackagesPackage.TCL_INTERPRETER_INFO__NAME, oldName,
					name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclPackageInfo> getPackages() {
		if (packages == null) {
			packages = new EObjectContainmentEList<TclPackageInfo>(
					TclPackageInfo.class, this,
					TclPackagesPackage.TCL_INTERPRETER_INFO__PACKAGES);
		}
		return packages;
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
					TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED,
					oldFetched, fetched));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getFetchedAt() {
		return fetchedAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFetchedAt(Date newFetchedAt) {
		Date oldFetchedAt = fetchedAt;
		fetchedAt = newFetchedAt;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED_AT,
					oldFetchedAt, fetchedAt));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnvironment(String newEnvironment) {
		String oldEnvironment = environment;
		environment = newEnvironment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					TclPackagesPackage.TCL_INTERPRETER_INFO__ENVIRONMENT,
					oldEnvironment, environment));
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
		case TclPackagesPackage.TCL_INTERPRETER_INFO__PACKAGES:
			return ((InternalEList<?>) getPackages()).basicRemove(otherEnd,
					msgs);
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
		case TclPackagesPackage.TCL_INTERPRETER_INFO__INSTALL_LOCATION:
			return getInstallLocation();
		case TclPackagesPackage.TCL_INTERPRETER_INFO__NAME:
			return getName();
		case TclPackagesPackage.TCL_INTERPRETER_INFO__PACKAGES:
			return getPackages();
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED:
			return isFetched() ? Boolean.TRUE : Boolean.FALSE;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED_AT:
			return getFetchedAt();
		case TclPackagesPackage.TCL_INTERPRETER_INFO__ENVIRONMENT:
			return getEnvironment();
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
		case TclPackagesPackage.TCL_INTERPRETER_INFO__INSTALL_LOCATION:
			setInstallLocation((String) newValue);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__NAME:
			setName((String) newValue);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__PACKAGES:
			getPackages().clear();
			getPackages().addAll(
					(Collection<? extends TclPackageInfo>) newValue);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED:
			setFetched(((Boolean) newValue).booleanValue());
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED_AT:
			setFetchedAt((Date) newValue);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__ENVIRONMENT:
			setEnvironment((String) newValue);
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
		case TclPackagesPackage.TCL_INTERPRETER_INFO__INSTALL_LOCATION:
			setInstallLocation(INSTALL_LOCATION_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__NAME:
			setName(NAME_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__PACKAGES:
			getPackages().clear();
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED:
			setFetched(FETCHED_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED_AT:
			setFetchedAt(FETCHED_AT_EDEFAULT);
			return;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__ENVIRONMENT:
			setEnvironment(ENVIRONMENT_EDEFAULT);
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
		case TclPackagesPackage.TCL_INTERPRETER_INFO__INSTALL_LOCATION:
			return INSTALL_LOCATION_EDEFAULT == null ? installLocation != null
					: !INSTALL_LOCATION_EDEFAULT.equals(installLocation);
		case TclPackagesPackage.TCL_INTERPRETER_INFO__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case TclPackagesPackage.TCL_INTERPRETER_INFO__PACKAGES:
			return packages != null && !packages.isEmpty();
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED:
			return fetched != FETCHED_EDEFAULT;
		case TclPackagesPackage.TCL_INTERPRETER_INFO__FETCHED_AT:
			return FETCHED_AT_EDEFAULT == null ? fetchedAt != null
					: !FETCHED_AT_EDEFAULT.equals(fetchedAt);
		case TclPackagesPackage.TCL_INTERPRETER_INFO__ENVIRONMENT:
			return ENVIRONMENT_EDEFAULT == null ? environment != null
					: !ENVIRONMENT_EDEFAULT.equals(environment);
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
		result.append(" (installLocation: ");
		result.append(installLocation);
		result.append(", name: ");
		result.append(name);
		result.append(", fetched: ");
		result.append(fetched);
		result.append(", fetchedAt: ");
		result.append(fetchedAt);
		result.append(", environment: ");
		result.append(environment);
		result.append(')');
		return result.toString();
	}

} //TclInterpreterInfoImpl
