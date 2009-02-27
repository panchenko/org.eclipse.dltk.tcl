/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigsPackage.java,v 1.8 2009/02/27 15:44:39 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import org.eclipse.dltk.validators.configs.ValidatorsPackage;
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
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__NAME = ValidatorsPackage.VALIDATOR_CONFIG__NAME;

	/**
	 * The feature id for the '<em><b>Read Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__READ_ONLY = ValidatorsPackage.VALIDATOR_CONFIG__READ_ONLY;

	/**
	 * The feature id for the '<em><b>Command Line Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__COMMAND_LINE_OPTIONS = ValidatorsPackage.VALIDATOR_CONFIG__COMMAND_LINE_OPTIONS;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__SUMMARY = ValidatorsPackage.VALIDATOR_CONFIG_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__MODE = ValidatorsPackage.VALIDATOR_CONFIG_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Message States</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__MESSAGE_STATES = ValidatorsPackage.VALIDATOR_CONFIG_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Use Tcl Ver</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__USE_TCL_VER = ValidatorsPackage.VALIDATOR_CONFIG_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Individual Message States</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG__INDIVIDUAL_MESSAGE_STATES = ValidatorsPackage.VALIDATOR_CONFIG_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Checker Config</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_CONFIG_FEATURE_COUNT = ValidatorsPackage.VALIDATOR_CONFIG_FEATURE_COUNT + 5;

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
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerEnvironmentInstanceImpl <em>Checker Environment Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerEnvironmentInstanceImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerEnvironmentInstance()
	 * @generated
	 */
	int CHECKER_ENVIRONMENT_INSTANCE = 2;

	/**
	 * The feature id for the '<em><b>Environment Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_ENVIRONMENT_INSTANCE__ENVIRONMENT_ID = ValidatorsPackage.VALIDATOR_ENVIRONMENT_INSTANCE__ENVIRONMENT_ID;

	/**
	 * The feature id for the '<em><b>Executable Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_ENVIRONMENT_INSTANCE__EXECUTABLE_PATH = ValidatorsPackage.VALIDATOR_ENVIRONMENT_INSTANCE__EXECUTABLE_PATH;

	/**
	 * The feature id for the '<em><b>Automatic</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_ENVIRONMENT_INSTANCE__AUTOMATIC = ValidatorsPackage.VALIDATOR_ENVIRONMENT_INSTANCE__AUTOMATIC;

	/**
	 * The feature id for the '<em><b>Pcx File Folders</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_ENVIRONMENT_INSTANCE__PCX_FILE_FOLDERS = ValidatorsPackage.VALIDATOR_ENVIRONMENT_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Use Pcx Files</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_ENVIRONMENT_INSTANCE__USE_PCX_FILES = ValidatorsPackage.VALIDATOR_ENVIRONMENT_INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_ENVIRONMENT_INSTANCE__INSTANCE = ValidatorsPackage.VALIDATOR_ENVIRONMENT_INSTANCE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Checker Environment Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_ENVIRONMENT_INSTANCE_FEATURE_COUNT = ValidatorsPackage.VALIDATOR_ENVIRONMENT_INSTANCE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl <em>Checker Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerInstance()
	 * @generated
	 */
	int CHECKER_INSTANCE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__NAME = ValidatorsPackage.VALIDATOR_INSTANCE__NAME;

	/**
	 * The feature id for the '<em><b>Automatic</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__AUTOMATIC = ValidatorsPackage.VALIDATOR_INSTANCE__AUTOMATIC;

	/**
	 * The feature id for the '<em><b>Validator Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__VALIDATOR_TYPE = ValidatorsPackage.VALIDATOR_INSTANCE__VALIDATOR_TYPE;

	/**
	 * The feature id for the '<em><b>Validator Nature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__VALIDATOR_NATURE = ValidatorsPackage.VALIDATOR_INSTANCE__VALIDATOR_NATURE;

	/**
	 * The feature id for the '<em><b>Validator Favorite Config</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__VALIDATOR_FAVORITE_CONFIG = ValidatorsPackage.VALIDATOR_INSTANCE__VALIDATOR_FAVORITE_CONFIG;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__VERSION = ValidatorsPackage.VALIDATOR_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Command Line Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__COMMAND_LINE_OPTIONS = ValidatorsPackage.VALIDATOR_INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Environments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__ENVIRONMENTS = ValidatorsPackage.VALIDATOR_INSTANCE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Favorite</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__FAVORITE = ValidatorsPackage.VALIDATOR_INSTANCE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Configs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE__CONFIGS = ValidatorsPackage.VALIDATOR_INSTANCE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Checker Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_INSTANCE_FEATURE_COUNT = ValidatorsPackage.VALIDATOR_INSTANCE_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode <em>Checker Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerMode()
	 * @generated
	 */
	int CHECKER_MODE = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState <em>Message State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getMessageState()
	 * @generated
	 */
	int MESSAGE_STATE = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion <em>Checker Version</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerVersion()
	 * @generated
	 */
	int CHECKER_VERSION = 6;


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
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance <em>Checker Environment Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Checker Environment Instance</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance
	 * @generated
	 */
	EClass getCheckerEnvironmentInstance();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getPcxFileFolders <em>Pcx File Folders</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Pcx File Folders</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getPcxFileFolders()
	 * @see #getCheckerEnvironmentInstance()
	 * @generated
	 */
	EAttribute getCheckerEnvironmentInstance_PcxFileFolders();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#isUsePcxFiles <em>Use Pcx Files</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Pcx Files</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#isUsePcxFiles()
	 * @see #getCheckerEnvironmentInstance()
	 * @generated
	 */
	EAttribute getCheckerEnvironmentInstance_UsePcxFiles();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getInstance <em>Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Instance</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getInstance()
	 * @see #getCheckerEnvironmentInstance()
	 * @generated
	 */
	EReference getCheckerEnvironmentInstance_Instance();

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
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironments <em>Environments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Environments</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironments()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EReference getCheckerInstance_Environments();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getFavorite <em>Favorite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Favorite</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getFavorite()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EReference getCheckerInstance_Favorite();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getConfigs <em>Configs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configs</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getConfigs()
	 * @see #getCheckerInstance()
	 * @generated
	 */
	EReference getCheckerInstance_Configs();

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
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerEnvironmentInstanceImpl <em>Checker Environment Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerEnvironmentInstanceImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl#getCheckerEnvironmentInstance()
		 * @generated
		 */
		EClass CHECKER_ENVIRONMENT_INSTANCE = eINSTANCE.getCheckerEnvironmentInstance();

		/**
		 * The meta object literal for the '<em><b>Pcx File Folders</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_ENVIRONMENT_INSTANCE__PCX_FILE_FOLDERS = eINSTANCE.getCheckerEnvironmentInstance_PcxFileFolders();

		/**
		 * The meta object literal for the '<em><b>Use Pcx Files</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_ENVIRONMENT_INSTANCE__USE_PCX_FILES = eINSTANCE.getCheckerEnvironmentInstance_UsePcxFiles();

		/**
		 * The meta object literal for the '<em><b>Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_ENVIRONMENT_INSTANCE__INSTANCE = eINSTANCE.getCheckerEnvironmentInstance_Instance();

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
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__VERSION = eINSTANCE.getCheckerInstance_Version();

		/**
		 * The meta object literal for the '<em><b>Command Line Options</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_INSTANCE__COMMAND_LINE_OPTIONS = eINSTANCE.getCheckerInstance_CommandLineOptions();

		/**
		 * The meta object literal for the '<em><b>Environments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_INSTANCE__ENVIRONMENTS = eINSTANCE.getCheckerInstance_Environments();

		/**
		 * The meta object literal for the '<em><b>Favorite</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_INSTANCE__FAVORITE = eINSTANCE.getCheckerInstance_Favorite();

		/**
		 * The meta object literal for the '<em><b>Configs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_INSTANCE__CONFIGS = eINSTANCE.getCheckerInstance_Configs();

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
