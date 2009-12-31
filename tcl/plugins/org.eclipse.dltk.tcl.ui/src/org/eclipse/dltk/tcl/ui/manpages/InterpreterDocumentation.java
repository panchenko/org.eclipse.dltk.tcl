/**
 * <copyright>
 * </copyright>
 *
 * $Id: InterpreterDocumentation.java,v 1.1 2009/12/31 09:18:25 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ui.manpages;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interpreter Documentation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation#getDocumentationId <em>Documentation Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage#getInterpreterDocumentation()
 * @model
 * @generated
 * @since 2.0
 */
public interface InterpreterDocumentation extends EObject {
	/**
	 * Returns the value of the '<em><b>Documentation Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Documentation Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Documentation Id</em>' attribute.
	 * @see #setDocumentationId(String)
	 * @see org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage#getInterpreterDocumentation_DocumentationId()
	 * @model
	 * @generated
	 */
	String getDocumentationId();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation#getDocumentationId <em>Documentation Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Documentation Id</em>' attribute.
	 * @see #getDocumentationId()
	 * @generated
	 */
	void setDocumentationId(String value);

} // InterpreterDocumentation
