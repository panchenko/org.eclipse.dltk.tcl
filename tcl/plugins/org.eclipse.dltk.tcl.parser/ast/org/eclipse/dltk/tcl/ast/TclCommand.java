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
package org.eclipse.dltk.tcl.ast;

import org.eclipse.dltk.tcl.definitions.Command;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Command</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCommand#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCommand#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCommand#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCommand#getMatches <em>Matches</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCommand#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCommand#isMatched <em>Matched</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCommand()
 * @model
 * @generated
 */
public interface TclCommand extends Node {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' reference.
	 * @see #setName(TclArgument)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCommand_Name()
	 * @model
	 * @generated
	 */
	TclArgument getName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclCommand#getName <em>Name</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' reference.
	 * @see #getName()
	 * @generated
	 */
	void setName(TclArgument value);

	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.ast.TclArgument}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' reference list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCommand_Arguments()
	 * @model
	 * @generated
	 */
	EList<TclArgument> getArguments();

	/**
	 * Returns the value of the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' reference.
	 * @see #setDefinition(Command)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCommand_Definition()
	 * @model
	 * @generated
	 */
	Command getDefinition();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclCommand#getDefinition <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(Command value);

	/**
	 * Returns the value of the '<em><b>Matches</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.ast.ArgumentMatch}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Matches</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Matches</em>' reference list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCommand_Matches()
	 * @model
	 * @generated
	 */
	EList<ArgumentMatch> getMatches();

	/**
	 * Returns the value of the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Qualified Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Qualified Name</em>' attribute.
	 * @see #setQualifiedName(String)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCommand_QualifiedName()
	 * @model
	 * @generated
	 */
	String getQualifiedName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclCommand#getQualifiedName <em>Qualified Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Qualified Name</em>' attribute.
	 * @see #getQualifiedName()
	 * @generated
	 */
	void setQualifiedName(String value);

	/**
	 * Returns the value of the '<em><b>Matched</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Matched</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Matched</em>' attribute.
	 * @see #setMatched(boolean)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCommand_Matched()
	 * @model
	 * @generated
	 */
	boolean isMatched();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclCommand#isMatched <em>Matched</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Matched</em>' attribute.
	 * @see #isMatched()
	 * @generated
	 */
	void setMatched(boolean value);

} // TclCommand
