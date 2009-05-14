/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclProblemModel.java,v 1.1 2009/05/14 16:06:33 asobolev Exp $
 */
package org.eclipse.dltk.tcl.ast;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Problem Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblemModel#getProblems <em>Problems</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblemModel()
 * @model
 * @generated
 */
public interface TclProblemModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Problems</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.ast.TclProblem}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Problems</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Problems</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblemModel_Problems()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclProblem> getProblems();

} // TclProblemModel
