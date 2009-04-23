/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackagesFactoryImpl.java,v 1.1 2009/04/23 10:58:24 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

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
