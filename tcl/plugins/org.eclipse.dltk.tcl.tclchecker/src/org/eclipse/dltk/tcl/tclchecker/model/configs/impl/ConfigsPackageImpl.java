/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigsPackageImpl.java,v 1.2 2009/02/11 10:32:26 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs.impl;

import java.util.Map;

import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsFactory;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState;

import org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesPackage;

import org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigsPackageImpl extends EPackageImpl implements ConfigsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass configInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass messageStateMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass checkerInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum checkerModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum messageStateEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum checkerVersionEEnum = null;

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
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ConfigsPackageImpl() {
		super(eNS_URI, ConfigsFactory.eINSTANCE);
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
	public static ConfigsPackage init() {
		if (isInited) return (ConfigsPackage)EPackage.Registry.INSTANCE.getEPackage(ConfigsPackage.eNS_URI);

		// Obtain or create and register package
		ConfigsPackageImpl theConfigsPackage = (ConfigsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof ConfigsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new ConfigsPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		MessagesPackageImpl theMessagesPackage = (MessagesPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MessagesPackage.eNS_URI) instanceof MessagesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MessagesPackage.eNS_URI) : MessagesPackage.eINSTANCE);

		// Create package meta-data objects
		theConfigsPackage.createPackageContents();
		theMessagesPackage.createPackageContents();

		// Initialize created meta-data
		theConfigsPackage.initializePackageContents();
		theMessagesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theConfigsPackage.freeze();

		return theConfigsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConfigInstance() {
		return configInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigInstance_Summary() {
		return (EAttribute)configInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigInstance_CommandLineOptions() {
		return (EAttribute)configInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigInstance_Name() {
		return (EAttribute)configInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigInstance_Mode() {
		return (EAttribute)configInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConfigInstance_MessageStates() {
		return (EReference)configInstanceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigInstance_UseTclVer() {
		return (EAttribute)configInstanceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigInstance_IndividualMessageStates() {
		return (EAttribute)configInstanceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMessageStateMap() {
		return messageStateMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessageStateMap_Key() {
		return (EAttribute)messageStateMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessageStateMap_Value() {
		return (EAttribute)messageStateMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCheckerInstance() {
		return checkerInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerInstance_EnvironmentId() {
		return (EAttribute)checkerInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerInstance_ExecutablePath() {
		return (EAttribute)checkerInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerInstance_PcxFiles() {
		return (EAttribute)checkerInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerInstance_Version() {
		return (EAttribute)checkerInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerInstance_UsePcxFiles() {
		return (EAttribute)checkerInstanceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerInstance_CommandLineOptions() {
		return (EAttribute)checkerInstanceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getCheckerMode() {
		return checkerModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getMessageState() {
		return messageStateEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getCheckerVersion() {
		return checkerVersionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigsFactory getConfigsFactory() {
		return (ConfigsFactory)getEFactoryInstance();
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
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		configInstanceEClass = createEClass(CONFIG_INSTANCE);
		createEAttribute(configInstanceEClass, CONFIG_INSTANCE__SUMMARY);
		createEAttribute(configInstanceEClass, CONFIG_INSTANCE__COMMAND_LINE_OPTIONS);
		createEAttribute(configInstanceEClass, CONFIG_INSTANCE__NAME);
		createEAttribute(configInstanceEClass, CONFIG_INSTANCE__MODE);
		createEReference(configInstanceEClass, CONFIG_INSTANCE__MESSAGE_STATES);
		createEAttribute(configInstanceEClass, CONFIG_INSTANCE__USE_TCL_VER);
		createEAttribute(configInstanceEClass, CONFIG_INSTANCE__INDIVIDUAL_MESSAGE_STATES);

		messageStateMapEClass = createEClass(MESSAGE_STATE_MAP);
		createEAttribute(messageStateMapEClass, MESSAGE_STATE_MAP__KEY);
		createEAttribute(messageStateMapEClass, MESSAGE_STATE_MAP__VALUE);

		checkerInstanceEClass = createEClass(CHECKER_INSTANCE);
		createEAttribute(checkerInstanceEClass, CHECKER_INSTANCE__ENVIRONMENT_ID);
		createEAttribute(checkerInstanceEClass, CHECKER_INSTANCE__EXECUTABLE_PATH);
		createEAttribute(checkerInstanceEClass, CHECKER_INSTANCE__PCX_FILES);
		createEAttribute(checkerInstanceEClass, CHECKER_INSTANCE__VERSION);
		createEAttribute(checkerInstanceEClass, CHECKER_INSTANCE__USE_PCX_FILES);
		createEAttribute(checkerInstanceEClass, CHECKER_INSTANCE__COMMAND_LINE_OPTIONS);

		// Create enums
		checkerModeEEnum = createEEnum(CHECKER_MODE);
		messageStateEEnum = createEEnum(MESSAGE_STATE);
		checkerVersionEEnum = createEEnum(CHECKER_VERSION);
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
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(configInstanceEClass, ConfigInstance.class, "ConfigInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getConfigInstance_Summary(), ecorePackage.getEBoolean(), "summary", null, 0, 1, ConfigInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getConfigInstance_CommandLineOptions(), ecorePackage.getEString(), "commandLineOptions", null, 0, 1, ConfigInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getConfigInstance_Name(), ecorePackage.getEString(), "name", null, 0, 1, ConfigInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getConfigInstance_Mode(), this.getCheckerMode(), "mode", null, 0, 1, ConfigInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getConfigInstance_MessageStates(), this.getMessageStateMap(), null, "messageStates", null, 0, -1, ConfigInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getConfigInstance_UseTclVer(), ecorePackage.getEBoolean(), "useTclVer", "true", 0, 1, ConfigInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(getConfigInstance_IndividualMessageStates(), ecorePackage.getEBoolean(), "individualMessageStates", "true", 0, 1, ConfigInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass(messageStateMapEClass, Map.Entry.class, "MessageStateMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getMessageStateMap_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getMessageStateMap_Value(), this.getMessageState(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(checkerInstanceEClass, CheckerInstance.class, "CheckerInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getCheckerInstance_EnvironmentId(), ecorePackage.getEString(), "environmentId", null, 0, 1, CheckerInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getCheckerInstance_ExecutablePath(), ecorePackage.getEString(), "executablePath", null, 0, 1, CheckerInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getCheckerInstance_PcxFiles(), ecorePackage.getEString(), "pcxFiles", null, 0, -1, CheckerInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getCheckerInstance_Version(), this.getCheckerVersion(), "version", null, 0, 1, CheckerInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getCheckerInstance_UsePcxFiles(), ecorePackage.getEBoolean(), "usePcxFiles", "true", 0, 1, CheckerInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(getCheckerInstance_CommandLineOptions(), ecorePackage.getEString(), "commandLineOptions", null, 0, 1, CheckerInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum(checkerModeEEnum, CheckerMode.class, "CheckerMode"); //$NON-NLS-1$
		addEEnumLiteral(checkerModeEEnum, CheckerMode.DEFAULT);
		addEEnumLiteral(checkerModeEEnum, CheckerMode.W0);
		addEEnumLiteral(checkerModeEEnum, CheckerMode.W1);
		addEEnumLiteral(checkerModeEEnum, CheckerMode.W2);
		addEEnumLiteral(checkerModeEEnum, CheckerMode.W3);
		addEEnumLiteral(checkerModeEEnum, CheckerMode.W4);

		initEEnum(messageStateEEnum, MessageState.class, "MessageState"); //$NON-NLS-1$
		addEEnumLiteral(messageStateEEnum, MessageState.DEFAULT);
		addEEnumLiteral(messageStateEEnum, MessageState.CHECK);
		addEEnumLiteral(messageStateEEnum, MessageState.SUPPRESS);

		initEEnum(checkerVersionEEnum, CheckerVersion.class, "CheckerVersion"); //$NON-NLS-1$
		addEEnumLiteral(checkerVersionEEnum, CheckerVersion.VERSION4);
		addEEnumLiteral(checkerVersionEEnum, CheckerVersion.VERSION5);

		// Create resource
		createResource(eNS_URI);
	}

} //ConfigsPackageImpl
