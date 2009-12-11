/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.ast.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.ast.AstPackage;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Script</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.ScriptImpl#getCommands <em>Commands</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.ScriptImpl#getContentStart <em>Content Start</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.ScriptImpl#getContentEnd <em>Content End</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScriptImpl extends TclArgumentImpl implements Script {
	/**
	 * The cached value of the '{@link #getCommands() <em>Commands</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommands()
	 * @generated
	 * @ordered
	 */
	protected EList<TclCommand> commands;

	/**
	 * The default value of the '{@link #getContentStart() <em>Content Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContentStart()
	 * @generated
	 * @ordered
	 */
	protected static final int CONTENT_START_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getContentStart() <em>Content Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContentStart()
	 * @generated
	 * @ordered
	 */
	protected int contentStart = CONTENT_START_EDEFAULT;
	/**
	 * The default value of the '{@link #getContentEnd() <em>Content End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContentEnd()
	 * @generated
	 * @ordered
	 */
	protected static final int CONTENT_END_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getContentEnd() <em>Content End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContentEnd()
	 * @generated
	 * @ordered
	 */
	protected int contentEnd = CONTENT_END_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScriptImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AstPackage.Literals.SCRIPT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclCommand> getCommands() {
		if (commands == null) {
			commands = new EObjectContainmentEList<TclCommand>(
					TclCommand.class, this, AstPackage.SCRIPT__COMMANDS);
		}
		return commands;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getContentStart() {
		return contentStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContentStart(int newContentStart) {
		int oldContentStart = contentStart;
		contentStart = newContentStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.SCRIPT__CONTENT_START, oldContentStart,
					contentStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getContentEnd() {
		return contentEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContentEnd(int newContentEnd) {
		int oldContentEnd = contentEnd;
		contentEnd = newContentEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.SCRIPT__CONTENT_END, oldContentEnd, contentEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AstPackage.SCRIPT__COMMANDS:
			return ((InternalEList<?>) getCommands()).basicRemove(otherEnd,
					msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AstPackage.SCRIPT__COMMANDS:
			return getCommands();
		case AstPackage.SCRIPT__CONTENT_START:
			return getContentStart();
		case AstPackage.SCRIPT__CONTENT_END:
			return getContentEnd();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AstPackage.SCRIPT__COMMANDS:
			getCommands().clear();
			getCommands().addAll((Collection<? extends TclCommand>) newValue);
			return;
		case AstPackage.SCRIPT__CONTENT_START:
			setContentStart((Integer) newValue);
			return;
		case AstPackage.SCRIPT__CONTENT_END:
			setContentEnd((Integer) newValue);
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
		case AstPackage.SCRIPT__COMMANDS:
			getCommands().clear();
			return;
		case AstPackage.SCRIPT__CONTENT_START:
			setContentStart(CONTENT_START_EDEFAULT);
			return;
		case AstPackage.SCRIPT__CONTENT_END:
			setContentEnd(CONTENT_END_EDEFAULT);
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
		case AstPackage.SCRIPT__COMMANDS:
			return commands != null && !commands.isEmpty();
		case AstPackage.SCRIPT__CONTENT_START:
			return contentStart != CONTENT_START_EDEFAULT;
		case AstPackage.SCRIPT__CONTENT_END:
			return contentEnd != CONTENT_END_EDEFAULT;
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (contentStart: "); //$NON-NLS-1$
		result.append(contentStart);
		result.append(", contentEnd: "); //$NON-NLS-1$
		result.append(contentEnd);
		result.append(')');
		return result.toString();
	}

} //ScriptImpl
