/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigsPackage.java,v 1.6 2009/02/19 10:41:52 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsFactory
 * @model kind="package"
 * @generated
 */
public interface ConfigsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "configs"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/dltk/tcl/tclchecker/configs"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "configs"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigsPackage eINSTANCE = org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerConfigImpl <em>Checker Config</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerConfigImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerConfig()
	 * @generated
	 */
	int CHECKER_CONFIG = 0;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__SUMMARY = 0;

	/**
	 * The feature id for the '<em><b>Command Line Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__COMMAND_LINE_OPTIONS = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__NAME = 2;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__MODE = 3;

	/**
	 * The feature id for the '<em><b>Message States</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__MESSAGE_STATES = 4;

	/**
	 * The feature id for the '<em><b>Use Tcl Ver</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__USE_TCL_VER = 5;

	/**
	 * The feature id for the '<em><b>Individual Message States</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__INDIVIDUAL_MESSAGE_STATES = 6;

	/**
	 * The feature id for the '<em><b>Read Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__READ_ONLY = 7;

	/**
	 * The number of structural features of the '<em>Checker Config</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.MessageStateMapImpl <em>Message State Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.MessageStateMapImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getMessageStateMap()
	 * @generated
	 */
	int MESSAGE_STATE_MAP = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_STATE_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_STATE_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Message State Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_STATE_MAP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl <em>Checker Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerInstance()
	 * @generated
	 */
	int CHECKER_INSTANCE = 2;

	/**
	 * The feature id for the '<em><b>Environment Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__ENVIRONMENT_ID = 0;

	/**
	 * The feature id for the '<em><b>Executable Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__EXECUTABLE_PATH = 1;

	/**
	 * The feature id for the '<em><b>Pcx File Folders</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__PCX_FILE_FOLDERS = 2;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__VERSION = 3;

	/**
	 * The feature id for the '<em><b>Use Pcx Files</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__USE_PCX_FILES = 4;

	/**
	 * The feature id for the '<em><b>Command Line Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__COMMAND_LINE_OPTIONS = 5;

	/**
	 * The feature id for the '<em><b>Automatic</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__AUTOMATIC = 6;

	/**
	 * The number of structural features of the '<em>Checker Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerFavoriteImpl <em>Checker Favorite</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerFavoriteImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerFavorite()
	 * @generated
	 */
	int CHECKER_FAVORITE = 3;

	/**
	 * The feature id for the '<em><b>Config</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_FAVORITE__CONFIG = 0;

	/**
	 * The feature id for the '<em><b>Environments</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_FAVORITE__ENVIRONMENTS = 1;

	/**
	 * The number of structural features of the '<em>Checker Favorite</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_FAVORITE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.EnvironmentInstanceMapImpl <em>Environment Instance Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.EnvironmentInstanceMapImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getEnvironmentInstanceMap()
	 * @generated
	 */
	int ENVIRONMENT_INSTANCE_MAP = 4;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_INSTANCE_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_INSTANCE_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Environment Instance Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVIRONMENT_INSTANCE_MAP_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode <em>Checker Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerMode()
	 * @generated
	 */
	int CHECKER_MODE = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState <em>Message State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getMessageState()
	 * @generated
	 */
	int MESSAGE_STATE = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion <em>Checker Version</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerVersion()
	 * @generated
	 */
	int CHECKER_VERSION = 7;


	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig <em>Checker Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Checker Config</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig
	 * @generated
	 */
	EClass getCheckerConfig();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isSummary <em>Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Summary</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isSummary()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EAttribute getCheckerConfig_Summary();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getCommandLineOptions <em>Command Line Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Command Line Options</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getCommandLineOptions()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EAttribute getCheckerConfig_CommandLineOptions();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getName()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EAttribute getCheckerConfig_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getMode <em>Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mode</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getMode()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EAttribute getCheckerConfig_Mode();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getMessageStates <em>Message States</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Message States</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#getMessageStates()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EReference getCheckerConfig_MessageStates();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isUseTclVer <em>Use Tcl Ver</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Tcl Ver</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isUseTclVer()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EAttribute getCheckerConfig_UseTclVer();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isIndividualMessageStates <em>Individual Message States</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Individual Message States</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isIndividualMessageStates()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EAttribute getCheckerConfig_IndividualMessageStates();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isReadOnly <em>Read Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Read Only</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig#isReadOnly()
	 * @see #getCheckerConfig()
	 * @generated
	 */
	EAttribute getCheckerConfig_ReadOnly();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Message State Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message State Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueDataType="org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState"
	 * @generated
	 */
	EClass getMessageStateMap();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getMessageStateMap()
	 * @generated
	 */
	EAttribute getMessageStateMap_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getMessageStateMap()
	 * @generated
	 */
	EAttribute getMessageStateMap_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance <em>Checker Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Checker Instance</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance
	 * @generated
	 */
	EClass getCheckerInstance();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironmentId <em>Environment Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Environment Id</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironmentId()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EAttribute getCheckerInstance_EnvironmentId();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getExecutablePath <em>Executable Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Executable Path</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getExecutablePath()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EAttribute getCheckerInstance_ExecutablePath();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getPcxFileFolders <em>Pcx File Folders</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Pcx File Folders</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getPcxFileFolders()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EAttribute getCheckerInstance_PcxFileFolders();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getVersion()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EAttribute getCheckerInstance_Version();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isUsePcxFiles <em>Use Pcx Files</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Pcx Files</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isUsePcxFiles()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EAttribute getCheckerInstance_UsePcxFiles();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getCommandLineOptions <em>Command Line Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Command Line Options</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getCommandLineOptions()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EAttribute getCheckerInstance_CommandLineOptions();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isAutomatic <em>Automatic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Automatic</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isAutomatic()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EAttribute getCheckerInstance_Automatic();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerFavorite <em>Checker Favorite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Checker Favorite</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerFavorite
	 * @generated
	 */
	EClass getCheckerFavorite();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerFavorite#getConfig <em>Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Config</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerFavorite#getConfig()
	 * @see #getCheckerFavorite()
	 * @generated
	 */
	EReference getCheckerFavorite_Config();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerFavorite#getEnvironments <em>Environments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Environments</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerFavorite#getEnvironments()
	 * @see #getCheckerFavorite()
	 * @generated
	 */
	EReference getCheckerFavorite_Environments();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Environment Instance Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Environment Instance Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance"
	 * @generated
	 */
	EClass getEnvironmentInstanceMap();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEnvironmentInstanceMap()
	 * @generated
	 */
	EAttribute getEnvironmentInstanceMap_Key();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEnvironmentInstanceMap()
	 * @generated
	 */
	EReference getEnvironmentInstanceMap_Value();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode <em>Checker Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Checker Mode</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode
	 * @generated
	 */
	EEnum getCheckerMode();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState <em>Message State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Message State</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState
	 * @generated
	 */
	EEnum getMessageState();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion <em>Checker Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Checker Version</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion
	 * @generated
	 */
	EEnum getCheckerVersion();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ConfigsFactory getConfigsFactory();

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
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerConfigImpl <em>Checker Config</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerConfigImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerConfig()
		 * @generated
		 */
		EClass CHECKER_CONFIG = eINSTANCE.getCheckerConfig();

		/**
		 * The meta object literal for the '<em><b>Summary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_CONFIG__SUMMARY = eINSTANCE.getCheckerConfig_Summary();

		/**
		 * The meta object literal for the '<em><b>Command Line Options</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_CONFIG__COMMAND_LINE_OPTIONS = eINSTANCE.getCheckerConfig_CommandLineOptions();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_CONFIG__NAME = eINSTANCE.getCheckerConfig_Name();

		/**
		 * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_CONFIG__MODE = eINSTANCE.getCheckerConfig_Mode();

		/**
		 * The meta object literal for the '<em><b>Message States</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_CONFIG__MESSAGE_STATES = eINSTANCE.getCheckerConfig_MessageStates();

		/**
		 * The meta object literal for the '<em><b>Use Tcl Ver</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_CONFIG__USE_TCL_VER = eINSTANCE.getCheckerConfig_UseTclVer();

		/**
		 * The meta object literal for the '<em><b>Individual Message States</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_CONFIG__INDIVIDUAL_MESSAGE_STATES = eINSTANCE.getCheckerConfig_IndividualMessageStates();

		/**
		 * The meta object literal for the '<em><b>Read Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_CONFIG__READ_ONLY = eINSTANCE.getCheckerConfig_ReadOnly();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.MessageStateMapImpl <em>Message State Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.MessageStateMapImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getMessageStateMap()
		 * @generated
		 */
		EClass MESSAGE_STATE_MAP = eINSTANCE.getMessageStateMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE_STATE_MAP__KEY = eINSTANCE.getMessageStateMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE_STATE_MAP__VALUE = eINSTANCE.getMessageStateMap_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl <em>Checker Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerInstance()
		 * @generated
		 */
		EClass CHECKER_INSTANCE = eINSTANCE.getCheckerInstance();

		/**
		 * The meta object literal for the '<em><b>Environment Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__ENVIRONMENT_ID = eINSTANCE.getCheckerInstance_EnvironmentId();

		/**
		 * The meta object literal for the '<em><b>Executable Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__EXECUTABLE_PATH = eINSTANCE.getCheckerInstance_ExecutablePath();

		/**
		 * The meta object literal for the '<em><b>Pcx File Folders</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__PCX_FILE_FOLDERS = eINSTANCE.getCheckerInstance_PcxFileFolders();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__VERSION = eINSTANCE.getCheckerInstance_Version();

		/**
		 * The meta object literal for the '<em><b>Use Pcx Files</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__USE_PCX_FILES = eINSTANCE.getCheckerInstance_UsePcxFiles();

		/**
		 * The meta object literal for the '<em><b>Command Line Options</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__COMMAND_LINE_OPTIONS = eINSTANCE.getCheckerInstance_CommandLineOptions();

		/**
		 * The meta object literal for the '<em><b>Automatic</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__AUTOMATIC = eINSTANCE.getCheckerInstance_Automatic();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerFavoriteImpl <em>Checker Favorite</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerFavoriteImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerFavorite()
		 * @generated
		 */
		EClass CHECKER_FAVORITE = eINSTANCE.getCheckerFavorite();

		/**
		 * The meta object literal for the '<em><b>Config</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_FAVORITE__CONFIG = eINSTANCE.getCheckerFavorite_Config();

		/**
		 * The meta object literal for the '<em><b>Environments</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_FAVORITE__ENVIRONMENTS = eINSTANCE.getCheckerFavorite_Environments();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.EnvironmentInstanceMapImpl <em>Environment Instance Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.EnvironmentInstanceMapImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getEnvironmentInstanceMap()
		 * @generated
		 */
		EClass ENVIRONMENT_INSTANCE_MAP = eINSTANCE.getEnvironmentInstanceMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENVIRONMENT_INSTANCE_MAP__KEY = eINSTANCE.getEnvironmentInstanceMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENVIRONMENT_INSTANCE_MAP__VALUE = eINSTANCE.getEnvironmentInstanceMap_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode <em>Checker Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerMode()
		 * @generated
		 */
		EEnum CHECKER_MODE = eINSTANCE.getCheckerMode();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState <em>Message State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getMessageState()
		 * @generated
		 */
		EEnum MESSAGE_STATE = eINSTANCE.getMessageState();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion <em>Checker Version</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerVersion()
		 * @generated
		 */
		EEnum CHECKER_VERSION = eINSTANCE.getCheckerVersion();

	}

} //ConfigsPackage
