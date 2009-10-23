/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclProjectInfo.java,v 1.6 2009/10/23 11:26:10 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Project Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getModules <em>Modules</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getVariables <em>Variables</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclProjectInfo()
 * @model
 * @generated
 */
public interface TclProjectInfo extends EObject {
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
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclProjectInfo_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Modules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modules</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modules</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclProjectInfo_Modules()
	 * @model containment="true"
	 * @generated
	 */
	EList<TclModuleInfo> getModules();

	/**
	 * Returns the value of the '<em><b>Variables</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.dltk.tcl.core.packages.VariableValue},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variables</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variables</em>' map.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclProjectInfo_Variables()
	 * @model mapType="org.eclipse.dltk.tcl.core.packages.VariableMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.dltk.tcl.core.packages.VariableValue>"
	 * @generated
	 */
	EMap<String, VariableValue> getVariables();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	TclModuleInfo findModule(String handle);

} // TclProjectInfo
