/**
 * <copyright>
 * </copyright>
 *
 * $Id: Documentation.java,v 1.1 2009/12/30 11:09:33 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.internal.ui.manpages;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Documentation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.internal.ui.manpages.Documentation#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.internal.ui.manpages.Documentation#getFolders <em>Folders</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.internal.ui.manpages.ManpagesPackage#getDocumentation()
 * @model
 * @generated
 * @since 2.0
 */
public interface Documentation extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.dltk.tcl.internal.ui.manpages.ManpagesPackage#getDocumentation_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.internal.ui.manpages.Documentation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Folders</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.internal.ui.manpages.ManPageFolder}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Folders</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Folders</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.internal.ui.manpages.ManpagesPackage#getDocumentation_Folders()
	 * @model containment="true"
	 * @generated
	 */
	EList<ManPageFolder> getFolders();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	ManPageFolder findFolder(String path);

} // Documentation
