/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclProjectInfoImpl.java,v 1.4 2009/07/03 11:20:20 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;
import org.eclipse.dltk.tcl.core.packages.TclProjectInfo;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tcl Project Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl#getModules <em>Modules</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl#getVariables <em>Variables</em>}</li>
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
	 * The cached value of the '{@link #getVariables() <em>Variables</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariables()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, VariableValue> variables;

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
	public EMap<String, VariableValue> getVariables() {
		if (variables == null) {
			variables = new EcoreEMap<String, VariableValue>(
					TclPackagesPackage.Literals.VARIABLE_MAP_ENTRY,
					VariableMapEntryImpl.class, this,
					TclPackagesPackage.TCL_PROJECT_INFO__VARIABLES);
		}
		return variables;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TclModuleInfo findModule(String handle) {
		for (TclModuleInfo moduleInfo : getModules()) {
			if (handle.equals(moduleInfo.getHandle())) {
				return moduleInfo;
			}
		}
		return null;
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
		case TclPackagesPackage.TCL_PROJECT_INFO__VARIABLES:
			return ((InternalEList<?>) getVariables()).basicRemove(otherEnd,
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
		case TclPackagesPackage.TCL_PROJECT_INFO__NAME:
			return getName();
		case TclPackagesPackage.TCL_PROJECT_INFO__MODULES:
			return getModules();
		case TclPackagesPackage.TCL_PROJECT_INFO__VARIABLES:
			if (coreType)
				return getVariables();
			else
				return getVariables().map();
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
		case TclPackagesPackage.TCL_PROJECT_INFO__VARIABLES:
			((EStructuralFeature.Setting) getVariables()).set(newValue);
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
		case TclPackagesPackage.TCL_PROJECT_INFO__VARIABLES:
			getVariables().clear();
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
		case TclPackagesPackage.TCL_PROJECT_INFO__VARIABLES:
			return variables != null && !variables.isEmpty();
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
