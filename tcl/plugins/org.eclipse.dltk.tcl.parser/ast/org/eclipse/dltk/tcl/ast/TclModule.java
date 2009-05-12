/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclModule.java,v 1.1 2009/05/12 09:39:43 asobolev Exp $
 */
package org.eclipse.dltk.tcl.ast;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclModule#getStatements <em>Statements</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclModule#getSize <em>Size</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclModule#getCodeModel <em>Code Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclModule()
 * @model
 * @generated
 */
public interface TclModule extends EObject {
	/**
	 * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.ast.TclCommand}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Statements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Statements</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclModule_Statements()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclCommand> getStatements();

	/**
	 * Returns the value of the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size</em>' attribute.
	 * @see #setSize(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclModule_Size()
	 * @model
	 * @generated
	 */
	int getSize();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclModule#getSize <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size</em>' attribute.
	 * @see #getSize()
	 * @generated
	 */
	void setSize(int value);

	/**
	 * Returns the value of the '<em><b>Code Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Code Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Code Model</em>' containment reference.
	 * @see #setCodeModel(TclCodeModel)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclModule_CodeModel()
	 * @model containment="true"
	 * @generated
	 */
	TclCodeModel getCodeModel();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclModule#getCodeModel <em>Code Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code Model</em>' containment reference.
	 * @see #getCodeModel()
	 * @generated
	 */
	void setCodeModel(TclCodeModel value);

} // TclModule
