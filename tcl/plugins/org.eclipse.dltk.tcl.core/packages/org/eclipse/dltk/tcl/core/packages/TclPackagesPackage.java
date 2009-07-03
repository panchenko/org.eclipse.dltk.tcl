/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackagesPackage.java,v 1.7 2009/07/03 11:20:22 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesFactory
 * @model kind="package"
 * @generated
 */
public interface TclPackagesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "packages";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/dltk/tcl/packages";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "pkg";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TclPackagesPackage eINSTANCE = org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl
			.init();

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl <em>Tcl Package Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclPackageInfo()
	 * @generated
	 */
	int TCL_PACKAGE_INFO = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PACKAGE_INFO__NAME = 0;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PACKAGE_INFO__VERSION = 1;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PACKAGE_INFO__SOURCES = 2;

	/**
	 * The feature id for the '<em><b>Fetched</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PACKAGE_INFO__FETCHED = 3;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PACKAGE_INFO__DEPENDENCIES = 4;

	/**
	 * The number of structural features of the '<em>Tcl Package Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PACKAGE_INFO_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl <em>Tcl Interpreter Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclInterpreterInfo()
	 * @generated
	 */
	int TCL_INTERPRETER_INFO = 1;

	/**
	 * The feature id for the '<em><b>Install Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO__INSTALL_LOCATION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO__NAME = 1;

	/**
	 * The feature id for the '<em><b>Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO__PACKAGES = 2;

	/**
	 * The feature id for the '<em><b>Fetched</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO__FETCHED = 3;

	/**
	 * The feature id for the '<em><b>Fetched At</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO__FETCHED_AT = 4;

	/**
	 * The feature id for the '<em><b>Environment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO__ENVIRONMENT = 5;

	/**
	 * The feature id for the '<em><b>Variables</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO__VARIABLES = 6;

	/**
	 * The number of structural features of the '<em>Tcl Interpreter Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_INTERPRETER_INFO_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl <em>Tcl Project Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclProjectInfo()
	 * @generated
	 */
	int TCL_PROJECT_INFO = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROJECT_INFO__NAME = 0;

	/**
	 * The feature id for the '<em><b>Modules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROJECT_INFO__MODULES = 1;

	/**
	 * The feature id for the '<em><b>Variables</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROJECT_INFO__VARIABLES = 2;

	/**
	 * The number of structural features of the '<em>Tcl Project Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROJECT_INFO_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl <em>Tcl Module Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclModuleInfo()
	 * @generated
	 */
	int TCL_MODULE_INFO = 3;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO__HANDLE = 0;

	/**
	 * The feature id for the '<em><b>Required</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO__REQUIRED = 1;

	/**
	 * The feature id for the '<em><b>Provided</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO__PROVIDED = 2;

	/**
	 * The feature id for the '<em><b>Sourced</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO__SOURCED = 3;

	/**
	 * The feature id for the '<em><b>Package Corrections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO__PACKAGE_CORRECTIONS = 4;

	/**
	 * The feature id for the '<em><b>Source Corrections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO__SOURCE_CORRECTIONS = 5;

	/**
	 * The feature id for the '<em><b>External</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO__EXTERNAL = 6;

	/**
	 * The number of structural features of the '<em>Tcl Module Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_INFO_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclSourceEntryImpl <em>Tcl Source Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclSourceEntryImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclSourceEntry()
	 * @generated
	 */
	int TCL_SOURCE_ENTRY = 4;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_SOURCE_ENTRY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_SOURCE_ENTRY__START = 1;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_SOURCE_ENTRY__END = 2;

	/**
	 * The number of structural features of the '<em>Tcl Source Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_SOURCE_ENTRY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.UserCorrectionImpl <em>User Correction</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.UserCorrectionImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getUserCorrection()
	 * @generated
	 */
	int USER_CORRECTION = 5;

	/**
	 * The feature id for the '<em><b>Original Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_CORRECTION__ORIGINAL_VALUE = 0;

	/**
	 * The feature id for the '<em><b>User Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_CORRECTION__USER_VALUE = 1;

	/**
	 * The number of structural features of the '<em>User Correction</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_CORRECTION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.VariableMapEntryImpl <em>Variable Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.VariableMapEntryImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getVariableMapEntry()
	 * @generated
	 */
	int VARIABLE_MAP_ENTRY = 6;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Variable Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.core.packages.impl.VariableValueImpl <em>Variable Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.core.packages.impl.VariableValueImpl
	 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getVariableValue()
	 * @generated
	 */
	int VARIABLE_VALUE = 7;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_VALUE__VALUE = 0;

	/**
	 * The number of structural features of the '<em>Variable Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_VALUE_FEATURE_COUNT = 1;

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo <em>Tcl Package Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Package Info</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackageInfo
	 * @generated
	 */
	EClass getTclPackageInfo();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getName()
	 * @see #getTclPackageInfo()
	 * @generated
	 */
	EAttribute getTclPackageInfo_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getVersion()
	 * @see #getTclPackageInfo()
	 * @generated
	 */
	EAttribute getTclPackageInfo_Version();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getSources <em>Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Sources</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getSources()
	 * @see #getTclPackageInfo()
	 * @generated
	 */
	EAttribute getTclPackageInfo_Sources();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#isFetched <em>Fetched</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fetched</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackageInfo#isFetched()
	 * @see #getTclPackageInfo()
	 * @generated
	 */
	EAttribute getTclPackageInfo_Fetched();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getDependencies <em>Dependencies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Dependencies</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getDependencies()
	 * @see #getTclPackageInfo()
	 * @generated
	 */
	EReference getTclPackageInfo_Dependencies();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo <em>Tcl Interpreter Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Interpreter Info</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo
	 * @generated
	 */
	EClass getTclInterpreterInfo();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getInstallLocation <em>Install Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Install Location</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getInstallLocation()
	 * @see #getTclInterpreterInfo()
	 * @generated
	 */
	EAttribute getTclInterpreterInfo_InstallLocation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getName()
	 * @see #getTclInterpreterInfo()
	 * @generated
	 */
	EAttribute getTclInterpreterInfo_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getPackages <em>Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Packages</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getPackages()
	 * @see #getTclInterpreterInfo()
	 * @generated
	 */
	EReference getTclInterpreterInfo_Packages();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#isFetched <em>Fetched</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fetched</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#isFetched()
	 * @see #getTclInterpreterInfo()
	 * @generated
	 */
	EAttribute getTclInterpreterInfo_Fetched();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getFetchedAt <em>Fetched At</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fetched At</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getFetchedAt()
	 * @see #getTclInterpreterInfo()
	 * @generated
	 */
	EAttribute getTclInterpreterInfo_FetchedAt();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getEnvironment <em>Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Environment</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getEnvironment()
	 * @see #getTclInterpreterInfo()
	 * @generated
	 */
	EAttribute getTclInterpreterInfo_Environment();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getVariables <em>Variables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Variables</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getVariables()
	 * @see #getTclInterpreterInfo()
	 * @generated
	 */
	EReference getTclInterpreterInfo_Variables();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo <em>Tcl Project Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Project Info</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclProjectInfo
	 * @generated
	 */
	EClass getTclProjectInfo();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getName()
	 * @see #getTclProjectInfo()
	 * @generated
	 */
	EAttribute getTclProjectInfo_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getModules <em>Modules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modules</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getModules()
	 * @see #getTclProjectInfo()
	 * @generated
	 */
	EReference getTclProjectInfo_Modules();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getVariables <em>Variables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Variables</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getVariables()
	 * @see #getTclProjectInfo()
	 * @generated
	 */
	EReference getTclProjectInfo_Variables();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo <em>Tcl Module Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Module Info</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo
	 * @generated
	 */
	EClass getTclModuleInfo();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getRequired <em>Required</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Required</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getRequired()
	 * @see #getTclModuleInfo()
	 * @generated
	 */
	EReference getTclModuleInfo_Required();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getProvided <em>Provided</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Provided</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getProvided()
	 * @see #getTclModuleInfo()
	 * @generated
	 */
	EReference getTclModuleInfo_Provided();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getSourced <em>Sourced</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sourced</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getSourced()
	 * @see #getTclModuleInfo()
	 * @generated
	 */
	EReference getTclModuleInfo_Sourced();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getPackageCorrections <em>Package Corrections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Package Corrections</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getPackageCorrections()
	 * @see #getTclModuleInfo()
	 * @generated
	 */
	EReference getTclModuleInfo_PackageCorrections();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getSourceCorrections <em>Source Corrections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Source Corrections</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getSourceCorrections()
	 * @see #getTclModuleInfo()
	 * @generated
	 */
	EReference getTclModuleInfo_SourceCorrections();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#isExternal <em>External</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>External</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo#isExternal()
	 * @see #getTclModuleInfo()
	 * @generated
	 */
	EAttribute getTclModuleInfo_External();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry <em>Tcl Source Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Source Entry</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclSourceEntry
	 * @generated
	 */
	EClass getTclSourceEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getValue()
	 * @see #getTclSourceEntry()
	 * @generated
	 */
	EAttribute getTclSourceEntry_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getStart()
	 * @see #getTclSourceEntry()
	 * @generated
	 */
	EAttribute getTclSourceEntry_Start();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getEnd()
	 * @see #getTclSourceEntry()
	 * @generated
	 */
	EAttribute getTclSourceEntry_End();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.core.packages.UserCorrection <em>User Correction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>User Correction</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.UserCorrection
	 * @generated
	 */
	EClass getUserCorrection();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.UserCorrection#getOriginalValue <em>Original Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Original Value</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.UserCorrection#getOriginalValue()
	 * @see #getUserCorrection()
	 * @generated
	 */
	EAttribute getUserCorrection_OriginalValue();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.dltk.tcl.core.packages.UserCorrection#getUserValue <em>User Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>User Value</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.UserCorrection#getUserValue()
	 * @see #getUserCorrection()
	 * @generated
	 */
	EAttribute getUserCorrection_UserValue();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Variable Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="org.eclipse.dltk.tcl.core.packages.VariableValue" valueContainment="true"
	 * @generated
	 */
	EClass getVariableMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getVariableMapEntry()
	 * @generated
	 */
	EAttribute getVariableMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getVariableMapEntry()
	 * @generated
	 */
	EReference getVariableMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.core.packages.VariableValue <em>Variable Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Value</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.VariableValue
	 * @generated
	 */
	EClass getVariableValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.VariableValue#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.VariableValue#getValue()
	 * @see #getVariableValue()
	 * @generated
	 */
	EAttribute getVariableValue_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getHandle()
	 * @see #getTclModuleInfo()
	 * @generated
	 */
	EAttribute getTclModuleInfo_Handle();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TclPackagesFactory getTclPackagesFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl <em>Tcl Package Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackageInfoImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclPackageInfo()
		 * @generated
		 */
		EClass TCL_PACKAGE_INFO = eINSTANCE.getTclPackageInfo();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PACKAGE_INFO__NAME = eINSTANCE.getTclPackageInfo_Name();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PACKAGE_INFO__VERSION = eINSTANCE
				.getTclPackageInfo_Version();

		/**
		 * The meta object literal for the '<em><b>Sources</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PACKAGE_INFO__SOURCES = eINSTANCE
				.getTclPackageInfo_Sources();

		/**
		 * The meta object literal for the '<em><b>Fetched</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PACKAGE_INFO__FETCHED = eINSTANCE
				.getTclPackageInfo_Fetched();

		/**
		 * The meta object literal for the '<em><b>Dependencies</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_PACKAGE_INFO__DEPENDENCIES = eINSTANCE
				.getTclPackageInfo_Dependencies();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl <em>Tcl Interpreter Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclInterpreterInfoImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclInterpreterInfo()
		 * @generated
		 */
		EClass TCL_INTERPRETER_INFO = eINSTANCE.getTclInterpreterInfo();

		/**
		 * The meta object literal for the '<em><b>Install Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_INTERPRETER_INFO__INSTALL_LOCATION = eINSTANCE
				.getTclInterpreterInfo_InstallLocation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_INTERPRETER_INFO__NAME = eINSTANCE
				.getTclInterpreterInfo_Name();

		/**
		 * The meta object literal for the '<em><b>Packages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_INTERPRETER_INFO__PACKAGES = eINSTANCE
				.getTclInterpreterInfo_Packages();

		/**
		 * The meta object literal for the '<em><b>Fetched</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_INTERPRETER_INFO__FETCHED = eINSTANCE
				.getTclInterpreterInfo_Fetched();

		/**
		 * The meta object literal for the '<em><b>Fetched At</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_INTERPRETER_INFO__FETCHED_AT = eINSTANCE
				.getTclInterpreterInfo_FetchedAt();

		/**
		 * The meta object literal for the '<em><b>Environment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_INTERPRETER_INFO__ENVIRONMENT = eINSTANCE
				.getTclInterpreterInfo_Environment();

		/**
		 * The meta object literal for the '<em><b>Variables</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_INTERPRETER_INFO__VARIABLES = eINSTANCE
				.getTclInterpreterInfo_Variables();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl <em>Tcl Project Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclProjectInfoImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclProjectInfo()
		 * @generated
		 */
		EClass TCL_PROJECT_INFO = eINSTANCE.getTclProjectInfo();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROJECT_INFO__NAME = eINSTANCE.getTclProjectInfo_Name();

		/**
		 * The meta object literal for the '<em><b>Modules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_PROJECT_INFO__MODULES = eINSTANCE
				.getTclProjectInfo_Modules();

		/**
		 * The meta object literal for the '<em><b>Variables</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_PROJECT_INFO__VARIABLES = eINSTANCE
				.getTclProjectInfo_Variables();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl <em>Tcl Module Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclModuleInfoImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclModuleInfo()
		 * @generated
		 */
		EClass TCL_MODULE_INFO = eINSTANCE.getTclModuleInfo();

		/**
		 * The meta object literal for the '<em><b>Required</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_MODULE_INFO__REQUIRED = eINSTANCE
				.getTclModuleInfo_Required();

		/**
		 * The meta object literal for the '<em><b>Provided</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_MODULE_INFO__PROVIDED = eINSTANCE
				.getTclModuleInfo_Provided();

		/**
		 * The meta object literal for the '<em><b>Sourced</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_MODULE_INFO__SOURCED = eINSTANCE
				.getTclModuleInfo_Sourced();

		/**
		 * The meta object literal for the '<em><b>Package Corrections</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_MODULE_INFO__PACKAGE_CORRECTIONS = eINSTANCE
				.getTclModuleInfo_PackageCorrections();

		/**
		 * The meta object literal for the '<em><b>Source Corrections</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_MODULE_INFO__SOURCE_CORRECTIONS = eINSTANCE
				.getTclModuleInfo_SourceCorrections();

		/**
		 * The meta object literal for the '<em><b>External</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_MODULE_INFO__EXTERNAL = eINSTANCE
				.getTclModuleInfo_External();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.TclSourceEntryImpl <em>Tcl Source Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclSourceEntryImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getTclSourceEntry()
		 * @generated
		 */
		EClass TCL_SOURCE_ENTRY = eINSTANCE.getTclSourceEntry();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_SOURCE_ENTRY__VALUE = eINSTANCE
				.getTclSourceEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_SOURCE_ENTRY__START = eINSTANCE
				.getTclSourceEntry_Start();

		/**
		 * The meta object literal for the '<em><b>End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_SOURCE_ENTRY__END = eINSTANCE.getTclSourceEntry_End();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.UserCorrectionImpl <em>User Correction</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.UserCorrectionImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getUserCorrection()
		 * @generated
		 */
		EClass USER_CORRECTION = eINSTANCE.getUserCorrection();

		/**
		 * The meta object literal for the '<em><b>Original Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_CORRECTION__ORIGINAL_VALUE = eINSTANCE
				.getUserCorrection_OriginalValue();

		/**
		 * The meta object literal for the '<em><b>User Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_CORRECTION__USER_VALUE = eINSTANCE
				.getUserCorrection_UserValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.VariableMapEntryImpl <em>Variable Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.VariableMapEntryImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getVariableMapEntry()
		 * @generated
		 */
		EClass VARIABLE_MAP_ENTRY = eINSTANCE.getVariableMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIABLE_MAP_ENTRY__KEY = eINSTANCE
				.getVariableMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE_MAP_ENTRY__VALUE = eINSTANCE
				.getVariableMapEntry_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.core.packages.impl.VariableValueImpl <em>Variable Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.core.packages.impl.VariableValueImpl
		 * @see org.eclipse.dltk.tcl.core.packages.impl.TclPackagesPackageImpl#getVariableValue()
		 * @generated
		 */
		EClass VARIABLE_VALUE = eINSTANCE.getVariableValue();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIABLE_VALUE__VALUE = eINSTANCE.getVariableValue_Value();

		/**
		 * The meta object literal for the '<em><b>Handle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_MODULE_INFO__HANDLE = eINSTANCE
				.getTclModuleInfo_Handle();

	}

} //TclPackagesPackage
