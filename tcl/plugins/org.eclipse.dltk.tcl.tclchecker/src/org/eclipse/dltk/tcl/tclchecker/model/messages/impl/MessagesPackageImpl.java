/**
 * <copyright>
 * </copyright>
 *
 * $Id: MessagesPackageImpl.java,v 1.5 2009/02/27 09:16:02 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.messages.impl;

import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsPackageImpl;
import org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesFactory;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesPackage;
import org.eclipse.dltk.validators.configs.ValidatorsPackage;
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
public class MessagesPackageImpl extends EPackageImpl implements MessagesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass checkerMessageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass messageGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum messageCategoryEEnum = null;

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
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MessagesPackageImpl() {
		super(eNS_URI, MessagesFactory.eINSTANCE);
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
	public static MessagesPackage init() {
		if (isInited) return (MessagesPackage)EPackage.Registry.INSTANCE.getEPackage(MessagesPackage.eNS_URI);

		// Obtain or create and register package
		MessagesPackageImpl theMessagesPackage = (MessagesPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof MessagesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new MessagesPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ValidatorsPackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		ConfigsPackageImpl theConfigsPackage = (ConfigsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ConfigsPackage.eNS_URI) instanceof ConfigsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ConfigsPackage.eNS_URI) : ConfigsPackage.eINSTANCE);

		// Create package meta-data objects
		theMessagesPackage.createPackageContents();
		theConfigsPackage.createPackageContents();

		// Initialize created meta-data
		theMessagesPackage.initializePackageContents();
		theConfigsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMessagesPackage.freeze();

		return theMessagesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCheckerMessage() {
		return checkerMessageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerMessage_MessageId() {
		return (EAttribute)checkerMessageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerMessage_Explanation() {
		return (EAttribute)checkerMessageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckerMessage_Category() {
		return (EAttribute)checkerMessageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCheckerMessage_Group() {
		return (EReference)checkerMessageEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMessageGroup() {
		return messageGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessageGroup_Id() {
		return (EAttribute)messageGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessageGroup_Name() {
		return (EAttribute)messageGroupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMessageGroup_Messages() {
		return (EReference)messageGroupEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessageGroup_Priority() {
		return (EAttribute)messageGroupEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getMessageCategory() {
		return messageCategoryEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessagesFactory getMessagesFactory() {
		return (MessagesFactory)getEFactoryInstance();
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
		checkerMessageEClass = createEClass(CHECKER_MESSAGE);
		createEAttribute(checkerMessageEClass, CHECKER_MESSAGE__MESSAGE_ID);
		createEAttribute(checkerMessageEClass, CHECKER_MESSAGE__EXPLANATION);
		createEAttribute(checkerMessageEClass, CHECKER_MESSAGE__CATEGORY);
		createEReference(checkerMessageEClass, CHECKER_MESSAGE__GROUP);

		messageGroupEClass = createEClass(MESSAGE_GROUP);
		createEAttribute(messageGroupEClass, MESSAGE_GROUP__ID);
		createEAttribute(messageGroupEClass, MESSAGE_GROUP__NAME);
		createEReference(messageGroupEClass, MESSAGE_GROUP__MESSAGES);
		createEAttribute(messageGroupEClass, MESSAGE_GROUP__PRIORITY);

		// Create enums
		messageCategoryEEnum = createEEnum(MESSAGE_CATEGORY);
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
		initEClass(checkerMessageEClass, CheckerMessage.class, "CheckerMessage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getCheckerMessage_MessageId(), ecorePackage.getEString(), "messageId", null, 0, 1, CheckerMessage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getCheckerMessage_Explanation(), ecorePackage.getEString(), "explanation", null, 0, 1, CheckerMessage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getCheckerMessage_Category(), this.getMessageCategory(), "category", null, 0, 1, CheckerMessage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getCheckerMessage_Group(), this.getMessageGroup(), this.getMessageGroup_Messages(), "group", null, 0, 1, CheckerMessage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(messageGroupEClass, MessageGroup.class, "MessageGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getMessageGroup_Id(), ecorePackage.getEString(), "id", null, 0, 1, MessageGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getMessageGroup_Name(), ecorePackage.getEString(), "name", null, 0, 1, MessageGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getMessageGroup_Messages(), this.getCheckerMessage(), this.getCheckerMessage_Group(), "messages", null, 0, -1, MessageGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getMessageGroup_Priority(), ecorePackage.getEInt(), "priority", "0", 0, 1, MessageGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		// Initialize enums and add enum literals
		initEEnum(messageCategoryEEnum, MessageCategory.class, "MessageCategory"); //$NON-NLS-1$
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.ERROR);
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.WARNING);
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.UPGRADE_ERROR);
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.UPGRADE_WARNING);
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.NON_PORTABLE_WARNING);
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.PERFORMANCE_WARNING);
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.USAGE_WARNING);
		addEEnumLiteral(messageCategoryEEnum, MessageCategory.STYLE);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$		
		addAnnotation
		  (checkerMessageEClass, 
		   source, 
		   new String[] {
			 "name", "message" //$NON-NLS-1$ //$NON-NLS-2$
		   });		
		addAnnotation
		  (getMessageGroup_Messages(), 
		   source, 
		   new String[] {
			 "name", "message" //$NON-NLS-1$ //$NON-NLS-2$
		   });
	}

} //MessagesPackageImpl
