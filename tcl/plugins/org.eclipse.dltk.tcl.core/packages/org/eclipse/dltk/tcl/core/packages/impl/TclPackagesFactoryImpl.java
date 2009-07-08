/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackagesFactoryImpl.java,v 1.5 2009/07/08 10:53:09 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import java.util.Map;
import org.eclipse.dltk.tcl.core.packages.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TclPackagesFactoryImpl extends EFactoryImpl implements
		TclPackagesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TclPackagesFactory init() {
		try {
			TclPackagesFactory theTclPackagesFactory = (TclPackagesFactory) EPackage.Registry.INSTANCE
					.getEFactory("http://www.eclipse.org/dltk/tcl/packages");
			if (theTclPackagesFactory != null) {
				return theTclPackagesFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TclPackagesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclPackagesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case TclPackagesPackage.TCL_PACKAGE_INFO:
			return createTclPackageInfo();
		case TclPackagesPackage.TCL_INTERPRETER_INFO:
			return createTclInterpreterInfo();
		case TclPackagesPackage.TCL_PROJECT_INFO:
			return createTclProjectInfo();
		case TclPackagesPackage.TCL_MODULE_INFO:
			return createTclModuleInfo();
		case TclPackagesPackage.TCL_SOURCE_ENTRY:
			return createTclSourceEntry();
		case TclPackagesPackage.USER_CORRECTION:
			return createUserCorrection();
		case TclPackagesPackage.VARIABLE_MAP_ENTRY:
			return (EObject) createVariableMapEntry();
		case TclPackagesPackage.VARIABLE_VALUE:
			return createVariableValue();
		case TclPackagesPackage.VARIABLE_MAP:
			return createVariableMap();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclPackageInfo createTclPackageInfo() {
		TclPackageInfoImpl tclPackageInfo = new TclPackageInfoImpl();
		return tclPackageInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclInterpreterInfo createTclInterpreterInfo() {
		TclInterpreterInfoImpl tclInterpreterInfo = new TclInterpreterInfoImpl();
		return tclInterpreterInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclProjectInfo createTclProjectInfo() {
		TclProjectInfoImpl tclProjectInfo = new TclProjectInfoImpl();
		return tclProjectInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclModuleInfo createTclModuleInfo() {
		TclModuleInfoImpl tclModuleInfo = new TclModuleInfoImpl();
		return tclModuleInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclSourceEntry createTclSourceEntry() {
		TclSourceEntryImpl tclSourceEntry = new TclSourceEntryImpl();
		return tclSourceEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserCorrection createUserCorrection() {
		UserCorrectionImpl userCorrection = new UserCorrectionImpl();
		return userCorrection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @since 2.0
	 */
	public Map.Entry<String, VariableValue> createVariableMapEntry() {
		VariableMapEntryImpl variableMapEntry = new VariableMapEntryImpl();
		return variableMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @since 2.0
	 */
	public VariableValue createVariableValue() {
		VariableValueImpl variableValue = new VariableValueImpl();
		return variableValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableMap createVariableMap() {
		VariableMapImpl variableMap = new VariableMapImpl();
		return variableMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclPackagesPackage getTclPackagesPackage() {
		return (TclPackagesPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TclPackagesPackage getPackage() {
		return TclPackagesPackage.eINSTANCE;
	}

} //TclPackagesFactoryImpl
