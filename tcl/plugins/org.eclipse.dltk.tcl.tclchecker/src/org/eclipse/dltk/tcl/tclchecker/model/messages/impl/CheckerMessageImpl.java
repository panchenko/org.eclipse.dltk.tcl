/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerMessageImpl.java,v 1.1 2009/01/27 18:43:46 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.messages.impl;

import org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Checker Message</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.messages.impl.CheckerMessageImpl#getMessageId <em>Message Id</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.messages.impl.CheckerMessageImpl#getExplanation <em>Explanation</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.messages.impl.CheckerMessageImpl#getCategory <em>Category</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CheckerMessageImpl extends EObjectImpl implements CheckerMessage {
	/**
	 * The default value of the '{@link #getMessageId() <em>Message Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessageId()
	 * @generated
	 * @ordered
	 */
	protected static final String MESSAGE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMessageId() <em>Message Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessageId()
	 * @generated
	 * @ordered
	 */
	protected String messageId = MESSAGE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getExplanation() <em>Explanation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExplanation()
	 * @generated
	 * @ordered
	 */
	protected static final String EXPLANATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExplanation() <em>Explanation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExplanation()
	 * @generated
	 * @ordered
	 */
	protected String explanation = EXPLANATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected static final MessageCategory CATEGORY_EDEFAULT = MessageCategory.ERROR;

	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected MessageCategory category = CATEGORY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CheckerMessageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MessagesPackage.Literals.CHECKER_MESSAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMessageId(String newMessageId) {
		String oldMessageId = messageId;
		messageId = newMessageId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MessagesPackage.CHECKER_MESSAGE__MESSAGE_ID, oldMessageId, messageId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExplanation(String newExplanation) {
		String oldExplanation = explanation;
		explanation = newExplanation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MessagesPackage.CHECKER_MESSAGE__EXPLANATION, oldExplanation, explanation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageCategory getCategory() {
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategory(MessageCategory newCategory) {
		MessageCategory oldCategory = category;
		category = newCategory == null ? CATEGORY_EDEFAULT : newCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MessagesPackage.CHECKER_MESSAGE__CATEGORY, oldCategory, category));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MessagesPackage.CHECKER_MESSAGE__MESSAGE_ID:
				return getMessageId();
			case MessagesPackage.CHECKER_MESSAGE__EXPLANATION:
				return getExplanation();
			case MessagesPackage.CHECKER_MESSAGE__CATEGORY:
				return getCategory();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MessagesPackage.CHECKER_MESSAGE__MESSAGE_ID:
				setMessageId((String)newValue);
				return;
			case MessagesPackage.CHECKER_MESSAGE__EXPLANATION:
				setExplanation((String)newValue);
				return;
			case MessagesPackage.CHECKER_MESSAGE__CATEGORY:
				setCategory((MessageCategory)newValue);
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
			case MessagesPackage.CHECKER_MESSAGE__MESSAGE_ID:
				setMessageId(MESSAGE_ID_EDEFAULT);
				return;
			case MessagesPackage.CHECKER_MESSAGE__EXPLANATION:
				setExplanation(EXPLANATION_EDEFAULT);
				return;
			case MessagesPackage.CHECKER_MESSAGE__CATEGORY:
				setCategory(CATEGORY_EDEFAULT);
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
			case MessagesPackage.CHECKER_MESSAGE__MESSAGE_ID:
				return MESSAGE_ID_EDEFAULT == null ? messageId != null : !MESSAGE_ID_EDEFAULT.equals(messageId);
			case MessagesPackage.CHECKER_MESSAGE__EXPLANATION:
				return EXPLANATION_EDEFAULT == null ? explanation != null : !EXPLANATION_EDEFAULT.equals(explanation);
			case MessagesPackage.CHECKER_MESSAGE__CATEGORY:
				return category != CATEGORY_EDEFAULT;
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
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (messageId: "); //$NON-NLS-1$
		result.append(messageId);
		result.append(", explanation: "); //$NON-NLS-1$
		result.append(explanation);
		result.append(", category: "); //$NON-NLS-1$
		result.append(category);
		result.append(')');
		return result.toString();
	}

} //CheckerMessageImpl
