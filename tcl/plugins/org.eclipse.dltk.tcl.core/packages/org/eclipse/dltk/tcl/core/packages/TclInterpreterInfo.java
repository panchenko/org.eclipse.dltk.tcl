/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclInterpreterInfo.java,v 1.2 2009/05/05 11:16:30 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Interpreter Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getInstallLocation <em>Install Location</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getPackages <em>Packages</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#isFetched <em>Fetched</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getEnvironment <em>Environment</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclInterpreterInfo()
 * @model
 * @generated
 */
public interface TclInterpreterInfo extends EObject {
	/**
	 * Returns the value of the '<em><b>Install Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Install Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Install Location</em>' attribute.
	 * @see #setInstallLocation(String)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclInterpreterInfo_InstallLocation()
	 * @model
	 * @generated
	 */
	String getInstallLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getInstallLocation <em>Install Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Install Location</em>' attribute.
	 * @see #getInstallLocation()
	 * @generated
	 */
	void setInstallLocation(String value);

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
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclInterpreterInfo_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Packages</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Packages</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Packages</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclInterpreterInfo_Packages()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclPackageInfo> getPackages();

	/**
	 * Returns the value of the '<em><b>Fetched</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fetched</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fetched</em>' attribute.
	 * @see #setFetched(boolean)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclInterpreterInfo_Fetched()
	 * @model default="false"
	 * @generated
	 */
	boolean isFetched();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#isFetched <em>Fetched</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fetched</em>' attribute.
	 * @see #isFetched()
	 * @generated
	 */
	void setFetched(boolean value);

	/**
	 * Returns the value of the '<em><b>Environment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Environment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Environment</em>' attribute.
	 * @see #setEnvironment(String)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclInterpreterInfo_Environment()
	 * @model
	 * @generated
	 */
	String getEnvironment();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo#getEnvironment <em>Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Environment</em>' attribute.
	 * @see #getEnvironment()
	 * @generated
	 */
	void setEnvironment(String value);

} // TclInterpreterInfo
