/**
 * <copyright>
 * </copyright>
 *
 * $Id: VariableMap.java,v 1.3 2009/10/23 11:26:10 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.VariableMap#getVariables <em>Variables</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getVariableMap()
 * @model
 * @generated
 */
public interface VariableMap extends EObject {
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
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getVariableMap_Variables()
	 * @model mapType="org.eclipse.dltk.tcl.core.packages.VariableMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.dltk.tcl.core.packages.VariableValue>"
	 * @generated
	 */
	EMap<String, VariableValue> getVariables();

} // VariableMap
