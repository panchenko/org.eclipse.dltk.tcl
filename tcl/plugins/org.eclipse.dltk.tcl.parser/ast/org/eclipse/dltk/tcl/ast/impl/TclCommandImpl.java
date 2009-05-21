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

import org.eclipse.dltk.tcl.ast.ArgumentMatch;
import org.eclipse.dltk.tcl.ast.AstPackage;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Tcl Command</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl#getName <em>Name
 * </em>}</li>
 * <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl#getArguments <em>
 * Arguments</em>}</li>
 * <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl#getDefinition <em>
 * Definition</em>}</li>
 * <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl#getMatches <em>
 * Matches</em>}</li>
 * <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl#getQualifiedName <em>
 * Qualified Name</em>}</li>
 * <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl#isMatched <em>Matched
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TclCommandImpl extends NodeImpl implements TclCommand {
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected TclArgument name;

	/**
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<TclArgument> arguments;

	/**
	 * The cached value of the '{@link #getDefinition() <em>Definition</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefinition()
	 * @generated
	 * @ordered
	 */
	protected Command definition;

	/**
	 * The cached value of the '{@link #getMatches() <em>Matches</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMatches()
	 * @generated
	 * @ordered
	 */
	protected EList<ArgumentMatch> matches;

	/**
	 * The default value of the '{@link #getQualifiedName()
	 * <em>Qualified Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getQualifiedName()
	 * @generated
	 * @ordered
	 */
	protected static final String QUALIFIED_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getQualifiedName()
	 * <em>Qualified Name</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getQualifiedName()
	 * @generated
	 * @ordered
	 */
	protected String qualifiedName = QUALIFIED_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isMatched() <em>Matched</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isMatched()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MATCHED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMatched() <em>Matched</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isMatched()
	 * @generated
	 * @ordered
	 */
	protected boolean matched = MATCHED_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TclCommandImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AstPackage.Literals.TCL_COMMAND;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TclArgument getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetName(TclArgument newName,
			NotificationChain msgs) {
		TclArgument oldName = name;
		name = newName;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this,
					Notification.SET, AstPackage.TCL_COMMAND__NAME, oldName,
					newName);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(TclArgument newName) {
		if (newName != name) {
			NotificationChain msgs = null;
			if (name != null)
				msgs = ((InternalEObject) name).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AstPackage.TCL_COMMAND__NAME,
						null, msgs);
			if (newName != null)
				msgs = ((InternalEObject) newName).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AstPackage.TCL_COMMAND__NAME,
						null, msgs);
			msgs = basicSetName(newName, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_COMMAND__NAME, newName, newName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<TclArgument> getArguments() {
		if (arguments == null) {
			arguments = new EObjectContainmentEList<TclArgument>(
					TclArgument.class, this, AstPackage.TCL_COMMAND__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Command getDefinition() {
		if (definition != null && definition.eIsProxy()) {
			InternalEObject oldDefinition = (InternalEObject) definition;
			definition = (Command) eResolveProxy(oldDefinition);
			if (definition != oldDefinition) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							AstPackage.TCL_COMMAND__DEFINITION, oldDefinition,
							definition));
			}
		}
		return definition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Command basicGetDefinition() {
		return definition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDefinition(Command newDefinition) {
		Command oldDefinition = definition;
		definition = newDefinition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_COMMAND__DEFINITION, oldDefinition,
					definition));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<ArgumentMatch> getMatches() {
		if (matches == null) {
			matches = new EObjectContainmentEList<ArgumentMatch>(
					ArgumentMatch.class, this, AstPackage.TCL_COMMAND__MATCHES);
		}
		return matches;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setQualifiedName(String newQualifiedName) {
		String oldQualifiedName = qualifiedName;
		qualifiedName = newQualifiedName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_COMMAND__QUALIFIED_NAME, oldQualifiedName,
					qualifiedName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isMatched() {
		return matched;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMatched(boolean newMatched) {
		boolean oldMatched = matched;
		matched = newMatched;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_COMMAND__MATCHED, oldMatched, matched));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AstPackage.TCL_COMMAND__NAME:
			return basicSetName(null, msgs);
		case AstPackage.TCL_COMMAND__ARGUMENTS:
			return ((InternalEList<?>) getArguments()).basicRemove(otherEnd,
					msgs);
		case AstPackage.TCL_COMMAND__MATCHES:
			return ((InternalEList<?>) getMatches())
					.basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AstPackage.TCL_COMMAND__NAME:
			return getName();
		case AstPackage.TCL_COMMAND__ARGUMENTS:
			return getArguments();
		case AstPackage.TCL_COMMAND__DEFINITION:
			if (resolve)
				return getDefinition();
			return basicGetDefinition();
		case AstPackage.TCL_COMMAND__MATCHES:
			return getMatches();
		case AstPackage.TCL_COMMAND__QUALIFIED_NAME:
			return getQualifiedName();
		case AstPackage.TCL_COMMAND__MATCHED:
			return isMatched() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AstPackage.TCL_COMMAND__NAME:
			setName((TclArgument) newValue);
			return;
		case AstPackage.TCL_COMMAND__ARGUMENTS:
			getArguments().clear();
			getArguments().addAll((Collection<? extends TclArgument>) newValue);
			return;
		case AstPackage.TCL_COMMAND__DEFINITION:
			setDefinition((Command) newValue);
			return;
		case AstPackage.TCL_COMMAND__MATCHES:
			getMatches().clear();
			getMatches().addAll((Collection<? extends ArgumentMatch>) newValue);
			return;
		case AstPackage.TCL_COMMAND__QUALIFIED_NAME:
			setQualifiedName((String) newValue);
			return;
		case AstPackage.TCL_COMMAND__MATCHED:
			setMatched(((Boolean) newValue).booleanValue());
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case AstPackage.TCL_COMMAND__NAME:
			setName((TclArgument) null);
			return;
		case AstPackage.TCL_COMMAND__ARGUMENTS:
			getArguments().clear();
			return;
		case AstPackage.TCL_COMMAND__DEFINITION:
			setDefinition((Command) null);
			return;
		case AstPackage.TCL_COMMAND__MATCHES:
			getMatches().clear();
			return;
		case AstPackage.TCL_COMMAND__QUALIFIED_NAME:
			setQualifiedName(QUALIFIED_NAME_EDEFAULT);
			return;
		case AstPackage.TCL_COMMAND__MATCHED:
			setMatched(MATCHED_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case AstPackage.TCL_COMMAND__NAME:
			return name != null;
		case AstPackage.TCL_COMMAND__ARGUMENTS:
			return arguments != null && !arguments.isEmpty();
		case AstPackage.TCL_COMMAND__DEFINITION:
			return definition != null;
		case AstPackage.TCL_COMMAND__MATCHES:
			return matches != null && !matches.isEmpty();
		case AstPackage.TCL_COMMAND__QUALIFIED_NAME:
			return QUALIFIED_NAME_EDEFAULT == null ? qualifiedName != null
					: !QUALIFIED_NAME_EDEFAULT.equals(qualifiedName);
		case AstPackage.TCL_COMMAND__MATCHED:
			return matched != MATCHED_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (qualifiedName: ");
		result.append(qualifiedName);
		result.append(", matched: ");
		result.append(matched);
		result.append(')');
		result.append("\n{"
				+ SimpleCodePrinter.getCommandString(this, getStart()) + "}");
		return result.toString();
	}

} // TclCommandImpl
