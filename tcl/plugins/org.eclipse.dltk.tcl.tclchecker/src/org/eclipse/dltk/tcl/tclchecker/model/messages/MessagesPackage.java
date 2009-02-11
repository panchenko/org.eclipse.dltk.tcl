/**
 * <copyright>
 * </copyright>
 *
 * $Id: MessagesPackage.java,v 1.3 2009/02/11 10:32:20 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.messages;

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
 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesFactory
 * @model kind="package"
 * @generated
 */
public interface MessagesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "messages"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/dltk/tcl/tclchecker/messages"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "messages"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MessagesPackage eINSTANCE = org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.impl.CheckerMessageImpl <em>Checker Message</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.CheckerMessageImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl#getCheckerMessage()
	 * @generated
	 */
	int CHECKER_MESSAGE = 0;

	/**
	 * The feature id for the '<em><b>Message Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_MESSAGE__MESSAGE_ID = 0;

	/**
	 * The feature id for the '<em><b>Explanation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_MESSAGE__EXPLANATION = 1;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_MESSAGE__CATEGORY = 2;

	/**
	 * The feature id for the '<em><b>Group</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_MESSAGE__GROUP = 3;

	/**
	 * The number of structural features of the '<em>Checker Message</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHECKER_MESSAGE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessageGroupImpl <em>Message Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessageGroupImpl
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl#getMessageGroup()
	 * @generated
	 */
	int MESSAGE_GROUP = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_GROUP__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_GROUP__NAME = 1;

	/**
	 * The feature id for the '<em><b>Messages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_GROUP__MESSAGES = 2;

	/**
	 * The feature id for the '<em><b>Priority</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_GROUP__PRIORITY = 3;

	/**
	 * The number of structural features of the '<em>Message Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_GROUP_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory <em>Message Category</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl#getMessageCategory()
	 * @generated
	 */
	int MESSAGE_CATEGORY = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage <em>Checker Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Checker Message</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage
	 * @generated
	 */
	EClass getCheckerMessage();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getMessageId <em>Message Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Message Id</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getMessageId()
	 * @see #getCheckerMessage()
	 * @generated
	 */
	EAttribute getCheckerMessage_MessageId();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getExplanation <em>Explanation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Explanation</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getExplanation()
	 * @see #getCheckerMessage()
	 * @generated
	 */
	EAttribute getCheckerMessage_Explanation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getCategory()
	 * @see #getCheckerMessage()
	 * @generated
	 */
	EAttribute getCheckerMessage_Category();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Group</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage#getGroup()
	 * @see #getCheckerMessage()
	 * @generated
	 */
	EReference getCheckerMessage_Group();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup <em>Message Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message Group</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup
	 * @generated
	 */
	EClass getMessageGroup();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getId()
	 * @see #getMessageGroup()
	 * @generated
	 */
	EAttribute getMessageGroup_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getName()
	 * @see #getMessageGroup()
	 * @generated
	 */
	EAttribute getMessageGroup_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getMessages <em>Messages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Messages</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getMessages()
	 * @see #getMessageGroup()
	 * @generated
	 */
	EReference getMessageGroup_Messages();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getPriority <em>Priority</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Priority</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup#getPriority()
	 * @see #getMessageGroup()
	 * @generated
	 */
	EAttribute getMessageGroup_Priority();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory <em>Message Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Message Category</em>'.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory
	 * @generated
	 */
	EEnum getMessageCategory();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MessagesFactory getMessagesFactory();

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
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.impl.CheckerMessageImpl <em>Checker Message</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.CheckerMessageImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl#getCheckerMessage()
		 * @generated
		 */
		EClass CHECKER_MESSAGE = eINSTANCE.getCheckerMessage();

		/**
		 * The meta object literal for the '<em><b>Message Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_MESSAGE__MESSAGE_ID = eINSTANCE.getCheckerMessage_MessageId();

		/**
		 * The meta object literal for the '<em><b>Explanation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_MESSAGE__EXPLANATION = eINSTANCE.getCheckerMessage_Explanation();

		/**
		 * The meta object literal for the '<em><b>Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHECKER_MESSAGE__CATEGORY = eINSTANCE.getCheckerMessage_Category();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHECKER_MESSAGE__GROUP = eINSTANCE.getCheckerMessage_Group();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessageGroupImpl <em>Message Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessageGroupImpl
		 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl#getMessageGroup()
		 * @generated
		 */
		EClass MESSAGE_GROUP = eINSTANCE.getMessageGroup();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE_GROUP__ID = eINSTANCE.getMessageGroup_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE_GROUP__NAME = eINSTANCE.getMessageGroup_Name();

		/**
		 * The meta object literal for the '<em><b>Messages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE_GROUP__MESSAGES = eINSTANCE.getMessageGroup_Messages();

		/**
		 * The meta object literal for the '<em><b>Priority</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE_GROUP__PRIORITY = eINSTANCE.getMessageGroup_Priority();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory <em>Message Category</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory
		 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesPackageImpl#getMessageCategory()
		 * @generated
		 */
		EEnum MESSAGE_CATEGORY = eINSTANCE.getMessageCategory();

	}

} //MessagesPackage
