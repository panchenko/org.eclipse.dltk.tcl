/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackageInfo.java,v 1.1 2009/04/23 10:58:25 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Package Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getSources <em>Sources</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#isFetched <em>Fetched</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclPackageInfo()
 * @model
 * @generated
 */
public interface TclPackageInfo extends EObject {
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
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclPackageInfo_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclPackageInfo_Version()
	 * @model
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Sources</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sources</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sources</em>' attribute list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclPackageInfo_Sources()
	 * @model
	 * @generated
	 */
	EList<String> getSources();

	/**
	 * Returns the value of the '<em><b>Fetched</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fetched</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fetched</em>' attribute.
	 * @see #setFetched(boolean)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclPackageInfo_Fetched()
	 * @model
	 * @generated
	 */
	boolean isFetched();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo#isFetched <em>Fetched</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fetched</em>' attribute.
	 * @see #isFetched()
	 * @generated
	 */
	void setFetched(boolean value);

	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependencies</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependencies</em>' reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclPackageInfo_Dependencies()
	 * @model
	 * @generated
	 */
	EList<TclPackageInfo> getDependencies();

} // TclPackageInfo
