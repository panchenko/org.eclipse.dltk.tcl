/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackagesPackageImpl.java,v 1.2 2009/04/28 11:00:04 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.impl;

import org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesFactory;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;
import org.eclipse.dltk.tcl.core.packages.TclProjectInfo;

import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TclPackagesPackageImpl extends EPackageImpl implements
		TclPackagesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tclPackageInfoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tclInterpreterInfoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tclProjectInfoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tclModuleInfoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tclSourceEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass userCorrectionEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TclPackagesPackageImpl() {
		super(eNS_URI, TclPackagesFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TclPackagesPackage init() {
		if (isInited)
			return (TclPackagesPackage) EPackage.Registry.INSTANCE
					.getEPackage(TclPackagesPackage.eNS_URI);

		// Obtain or create and register package
		TclPackagesPackageImpl theTclPackagesPackage = (TclPackagesPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI) instanceof TclPackagesPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI)
				: new TclPackagesPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theTclPackagesPackage.createPackageContents();

		// Initialize created meta-data
		theTclPackagesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTclPackagesPackage.freeze();

		return theTclPackagesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTclPackageInfo() {
		return tclPackageInfoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclPackageInfo_Name() {
		return (EAttribute) tclPackageInfoEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclPackageInfo_Version() {
		return (EAttribute) tclPackageInfoEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclPackageInfo_Sources() {
		return (EAttribute) tclPackageInfoEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclPackageInfo_Fetched() {
		return (EAttribute) tclPackageInfoEClass.getEStructuralFeatures()
				.get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclPackageInfo_Dependencies() {
		return (EReference) tclPackageInfoEClass.getEStructuralFeatures()
				.get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTclInterpreterInfo() {
		return tclInterpreterInfoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclInterpreterInfo_InstallLocation() {
		return (EAttribute) tclInterpreterInfoEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclInterpreterInfo_Name() {
		return (EAttribute) tclInterpreterInfoEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclInterpreterInfo_Packages() {
		return (EReference) tclInterpreterInfoEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTclProjectInfo() {
		return tclProjectInfoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclProjectInfo_Name() {
		return (EAttribute) tclProjectInfoEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclProjectInfo_Modules() {
		return (EReference) tclProjectInfoEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclProjectInfo_PackageCorrections() {
		return (EReference) tclProjectInfoEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTclModuleInfo() {
		return tclModuleInfoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclModuleInfo_Required() {
		return (EReference) tclModuleInfoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclModuleInfo_Provided() {
		return (EReference) tclModuleInfoEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclModuleInfo_Sourced() {
		return (EReference) tclModuleInfoEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTclModuleInfo_SourceCorrections() {
		return (EReference) tclModuleInfoEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclModuleInfo_External() {
		return (EAttribute) tclModuleInfoEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTclSourceEntry() {
		return tclSourceEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclSourceEntry_Value() {
		return (EAttribute) tclSourceEntryEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclSourceEntry_Start() {
		return (EAttribute) tclSourceEntryEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclSourceEntry_End() {
		return (EAttribute) tclSourceEntryEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUserCorrection() {
		return userCorrectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserCorrection_OriginalValue() {
		return (EAttribute) userCorrectionEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserCorrection_UserValue() {
		return (EAttribute) userCorrectionEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTclModuleInfo_Handle() {
		return (EAttribute) tclModuleInfoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclPackagesFactory getTclPackagesFactory() {
		return (TclPackagesFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		tclPackageInfoEClass = createEClass(TCL_PACKAGE_INFO);
		createEAttribute(tclPackageInfoEClass, TCL_PACKAGE_INFO__NAME);
		createEAttribute(tclPackageInfoEClass, TCL_PACKAGE_INFO__VERSION);
		createEAttribute(tclPackageInfoEClass, TCL_PACKAGE_INFO__SOURCES);
		createEAttribute(tclPackageInfoEClass, TCL_PACKAGE_INFO__FETCHED);
		createEReference(tclPackageInfoEClass, TCL_PACKAGE_INFO__DEPENDENCIES);

		tclInterpreterInfoEClass = createEClass(TCL_INTERPRETER_INFO);
		createEAttribute(tclInterpreterInfoEClass,
				TCL_INTERPRETER_INFO__INSTALL_LOCATION);
		createEAttribute(tclInterpreterInfoEClass, TCL_INTERPRETER_INFO__NAME);
		createEReference(tclInterpreterInfoEClass,
				TCL_INTERPRETER_INFO__PACKAGES);

		tclProjectInfoEClass = createEClass(TCL_PROJECT_INFO);
		createEAttribute(tclProjectInfoEClass, TCL_PROJECT_INFO__NAME);
		createEReference(tclProjectInfoEClass, TCL_PROJECT_INFO__MODULES);
		createEReference(tclProjectInfoEClass,
				TCL_PROJECT_INFO__PACKAGE_CORRECTIONS);

		tclModuleInfoEClass = createEClass(TCL_MODULE_INFO);
		createEAttribute(tclModuleInfoEClass, TCL_MODULE_INFO__HANDLE);
		createEReference(tclModuleInfoEClass, TCL_MODULE_INFO__REQUIRED);
		createEReference(tclModuleInfoEClass, TCL_MODULE_INFO__PROVIDED);
		createEReference(tclModuleInfoEClass, TCL_MODULE_INFO__SOURCED);
		createEReference(tclModuleInfoEClass,
				TCL_MODULE_INFO__SOURCE_CORRECTIONS);
		createEAttribute(tclModuleInfoEClass, TCL_MODULE_INFO__EXTERNAL);

		tclSourceEntryEClass = createEClass(TCL_SOURCE_ENTRY);
		createEAttribute(tclSourceEntryEClass, TCL_SOURCE_ENTRY__VALUE);
		createEAttribute(tclSourceEntryEClass, TCL_SOURCE_ENTRY__START);
		createEAttribute(tclSourceEntryEClass, TCL_SOURCE_ENTRY__END);

		userCorrectionEClass = createEClass(USER_CORRECTION);
		createEAttribute(userCorrectionEClass, USER_CORRECTION__ORIGINAL_VALUE);
		createEAttribute(userCorrectionEClass, USER_CORRECTION__USER_VALUE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(tclPackageInfoEClass, TclPackageInfo.class,
				"TclPackageInfo", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTclPackageInfo_Name(), ecorePackage.getEString(),
				"name", null, 0, 1, TclPackageInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTclPackageInfo_Version(), ecorePackage.getEString(),
				"version", null, 0, 1, TclPackageInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTclPackageInfo_Sources(), ecorePackage.getEString(),
				"sources", null, 0, -1, TclPackageInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTclPackageInfo_Fetched(), ecorePackage.getEBoolean(),
				"fetched", null, 0, 1, TclPackageInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getTclPackageInfo_Dependencies(), this
				.getTclPackageInfo(), null, "dependencies", null, 0, -1,
				TclPackageInfo.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tclInterpreterInfoEClass, TclInterpreterInfo.class,
				"TclInterpreterInfo", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTclInterpreterInfo_InstallLocation(), ecorePackage
				.getEString(), "installLocation", null, 0, 1,
				TclInterpreterInfo.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getTclInterpreterInfo_Name(), ecorePackage.getEString(),
				"name", null, 0, 1, TclInterpreterInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getTclInterpreterInfo_Packages(), this
				.getTclPackageInfo(), null, "packages", null, 0, -1,
				TclInterpreterInfo.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tclProjectInfoEClass, TclProjectInfo.class,
				"TclProjectInfo", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTclProjectInfo_Name(), ecorePackage.getEString(),
				"name", null, 0, 1, TclProjectInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getTclProjectInfo_Modules(), this.getTclModuleInfo(),
				null, "modules", null, 0, -1, TclProjectInfo.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getTclProjectInfo_PackageCorrections(), this
				.getUserCorrection(), null, "packageCorrections", null, 0, -1,
				TclProjectInfo.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tclModuleInfoEClass, TclModuleInfo.class, "TclModuleInfo",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTclModuleInfo_Handle(), ecorePackage.getEString(),
				"handle", null, 0, 1, TclModuleInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getTclModuleInfo_Required(), this.getTclSourceEntry(),
				null, "required", null, 0, -1, TclModuleInfo.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getTclModuleInfo_Provided(), this.getTclSourceEntry(),
				null, "provided", null, 0, -1, TclModuleInfo.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getTclModuleInfo_Sourced(), this.getTclSourceEntry(),
				null, "sourced", null, 0, -1, TclModuleInfo.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getTclModuleInfo_SourceCorrections(), this
				.getUserCorrection(), null, "sourceCorrections", null, 0, -1,
				TclModuleInfo.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTclModuleInfo_External(), ecorePackage.getEBoolean(),
				"external", null, 0, 1, TclModuleInfo.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(tclSourceEntryEClass, TclSourceEntry.class,
				"TclSourceEntry", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTclSourceEntry_Value(), ecorePackage.getEString(),
				"value", null, 0, 1, TclSourceEntry.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTclSourceEntry_Start(), ecorePackage.getEInt(),
				"start", null, 0, 1, TclSourceEntry.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTclSourceEntry_End(), ecorePackage.getEInt(), "end",
				null, 0, 1, TclSourceEntry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		initEClass(userCorrectionEClass, UserCorrection.class,
				"UserCorrection", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUserCorrection_OriginalValue(), ecorePackage
				.getEString(), "originalValue", null, 0, 1,
				UserCorrection.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getUserCorrection_UserValue(),
				ecorePackage.getEString(), "userValue", null, 0, 1,
				UserCorrection.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //TclPackagesPackageImpl
