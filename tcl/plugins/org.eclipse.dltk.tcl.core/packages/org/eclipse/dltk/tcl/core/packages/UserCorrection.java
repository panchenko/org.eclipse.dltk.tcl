/**
 * <copyright>
 * </copyright>
 *
 * $Id: UserCorrection.java,v 1.3 2009/07/06 08:55:52 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Correction</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.UserCorrection#getOriginalValue <em>Original Value</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.UserCorrection#getUserValue <em>User Value</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.UserCorrection#isVariable <em>Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getUserCorrection()
 * @model
 * @generated
 */
public interface UserCorrection extends EObject {
	/**
	 * Returns the value of the '<em><b>Original Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Value</em>' attribute.
	 * @see #setOriginalValue(String)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getUserCorrection_OriginalValue()
	 * @model
	 * @generated
	 */
	String getOriginalValue();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.UserCorrection#getOriginalValue <em>Original Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Value</em>' attribute.
	 * @see #getOriginalValue()
	 * @generated
	 */
	void setOriginalValue(String value);

	/**
	 * Returns the value of the '<em><b>User Value</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Value</em>' attribute list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getUserCorrection_UserValue()
	 * @model
	 * @generated
	 */
	EList<String> getUserValue();

	/**
	 * Returns the value of the '<em><b>Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variable</em>' attribute.
	 * @see #setVariable(boolean)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getUserCorrection_Variable()
	 * @model
	 * @generated
	 */
	boolean isVariable();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.UserCorrection#isVariable <em>Variable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variable</em>' attribute.
	 * @see #isVariable()
	 * @generated
	 */
	void setVariable(boolean value);

} // UserCorrection
