/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclCodeModel.java,v 1.1 2009/05/12 09:39:43 asobolev Exp $
 */
package org.eclipse.dltk.tcl.ast;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Code Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCodeModel#getDelimeters <em>Delimeters</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclCodeModel#getLineOffsets <em>Line Offsets</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCodeModel()
 * @model
 * @generated
 */
public interface TclCodeModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Delimeters</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Delimeters</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Delimeters</em>' attribute list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCodeModel_Delimeters()
	 * @model
	 * @generated
	 */
	EList<String> getDelimeters();

	/**
	 * Returns the value of the '<em><b>Line Offsets</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Line Offsets</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Offsets</em>' attribute list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclCodeModel_LineOffsets()
	 * @model
	 * @generated
	 */
	EList<Integer> getLineOffsets();

} // TclCodeModel
