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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Script</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.Script#getCommands <em>Commands</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.Script#getContentStart <em>Content Start</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.Script#getContentEnd <em>Content End</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getScript()
 * @model
 * @generated
 */
public interface Script extends TclArgument {
	/**
	 * Returns the value of the '<em><b>Commands</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.ast.TclCommand}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commands</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commands</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getScript_Commands()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclCommand> getCommands();

	/**
	 * Returns the value of the '<em><b>Content Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content Start</em>' attribute.
	 * @see #setContentStart(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getScript_ContentStart()
	 * @model
	 * @generated
	 */
	int getContentStart();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.Script#getContentStart <em>Content Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Start</em>' attribute.
	 * @see #getContentStart()
	 * @generated
	 */
	void setContentStart(int value);

	/**
	 * Returns the value of the '<em><b>Content End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content End</em>' attribute.
	 * @see #setContentEnd(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getScript_ContentEnd()
	 * @model
	 * @generated
	 */
	int getContentEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.Script#getContentEnd <em>Content End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content End</em>' attribute.
	 * @see #getContentEnd()
	 * @generated
	 */
	void setContentEnd(int value);

} // Script
