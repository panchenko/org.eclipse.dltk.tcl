/**
 * <copyright>
 * </copyright>
 *
 * $Id: InterpreterDocumentationImpl.java,v 1.1 2009/12/31 09:18:23 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ui.manpages.impl;

import org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation;
import org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpreter Documentation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ui.manpages.impl.InterpreterDocumentationImpl#getDocumentationId <em>Documentation Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpreterDocumentationImpl extends EObjectImpl implements InterpreterDocumentation {
	/**
	 * The default value of the '{@link #getDocumentationId() <em>Documentation Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDocumentationId()
	 * @generated
	 * @ordered
	 */
	protected static final String DOCUMENTATION_ID_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDocumentationId() <em>Documentation Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDocumentationId()
	 * @generated
	 * @ordered
	 */
	protected String documentationId = DOCUMENTATION_ID_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InterpreterDocumentationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ManpagesPackage.Literals.INTERPRETER_DOCUMENTATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDocumentationId() {
		return documentationId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocumentationId(String newDocumentationId) {
		String oldDocumentationId = documentationId;
		documentationId = newDocumentationId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManpagesPackage.INTERPRETER_DOCUMENTATION__DOCUMENTATION_ID, oldDocumentationId, documentationId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ManpagesPackage.INTERPRETER_DOCUMENTATION__DOCUMENTATION_ID:
				return getDocumentationId();
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
			case ManpagesPackage.INTERPRETER_DOCUMENTATION__DOCUMENTATION_ID:
				setDocumentationId((String)newValue);
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
			case ManpagesPackage.INTERPRETER_DOCUMENTATION__DOCUMENTATION_ID:
				setDocumentationId(DOCUMENTATION_ID_EDEFAULT);
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
			case ManpagesPackage.INTERPRETER_DOCUMENTATION__DOCUMENTATION_ID:
				return DOCUMENTATION_ID_EDEFAULT == null ? documentationId != null : !DOCUMENTATION_ID_EDEFAULT.equals(documentationId);
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
		result.append(" (documentationId: "); //$NON-NLS-1$
		result.append(documentationId);
		result.append(')');
		return result.toString();
	}

} //InterpreterDocumentationImpl
