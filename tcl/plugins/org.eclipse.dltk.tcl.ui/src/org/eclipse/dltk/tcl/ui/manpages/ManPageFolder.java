/**
 * <copyright>
 * </copyright>
 *
 * $Id: ManPageFolder.java,v 1.1 2009/12/31 09:18:25 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ui.manpages;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Man Page Folder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ui.manpages.ManPageFolder#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ui.manpages.ManPageFolder#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage#getManPageFolder()
 * @model
 * @generated NOT
 * @since 2.0
 */
public interface ManPageFolder extends EObject {
	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage#getManPageFolder_Path()
	 * @model
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ui.manpages.ManPageFolder#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Keywords</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Keywords</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Keywords</em>' map.
	 * @see org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage#getManPageFolder_Keywords()
	 * @model mapType="org.eclipse.dltk.tcl.internal.ui.manpages.StringToStringEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 * @generated
	 */
	EMap<String, String> getKeywords();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void addPage(String keyword, String file);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean verify();

} // ManPageFolder
