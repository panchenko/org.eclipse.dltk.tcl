/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclModuleInfo.java,v 1.3 2009/05/19 17:46:51 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Module Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getHandle <em>Handle</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getRequired <em>Required</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getProvided <em>Provided</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getSourced <em>Sourced</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getPackageCorrections <em>Package Corrections</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getSourceCorrections <em>Source Corrections</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#isExternal <em>External</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo()
 * @model
 * @generated
 */
public interface TclModuleInfo extends EObject {
	/**
	 * Returns the value of the '<em><b>Required</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Required</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo_Required()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclSourceEntry> getRequired();

	/**
	 * Returns the value of the '<em><b>Provided</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provided</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo_Provided()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclSourceEntry> getProvided();

	/**
	 * Returns the value of the '<em><b>Sourced</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sourced</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sourced</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo_Sourced()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclSourceEntry> getSourced();

	/**
	 * Returns the value of the '<em><b>Package Corrections</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.UserCorrection}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Package Corrections</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Package Corrections</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo_PackageCorrections()
	 * @model containment="true"
	 * @generated
	 */
	EList<UserCorrection> getPackageCorrections();

	/**
	 * Returns the value of the '<em><b>Source Corrections</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.UserCorrection}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Corrections</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Corrections</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo_SourceCorrections()
	 * @model containment="true"
	 * @generated
	 */
	EList<UserCorrection> getSourceCorrections();

	/**
	 * Returns the value of the '<em><b>External</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>External</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>External</em>' attribute.
	 * @see #setExternal(boolean)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo_External()
	 * @model
	 * @generated
	 */
	boolean isExternal();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#isExternal <em>External</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>External</em>' attribute.
	 * @see #isExternal()
	 * @generated
	 */
	void setExternal(boolean value);

	/**
	 * Returns the value of the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Handle</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Handle</em>' attribute.
	 * @see #setHandle(String)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclModuleInfo_Handle()
	 * @model
	 * @generated
	 */
	String getHandle();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo#getHandle <em>Handle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Handle</em>' attribute.
	 * @see #getHandle()
	 * @generated
	 */
	void setHandle(String value);

} // TclModuleInfo
