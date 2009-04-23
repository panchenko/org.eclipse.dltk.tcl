/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclSourceEntry.java,v 1.1 2009/04/23 10:58:25 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Source Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getStart <em>Start</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getEnd <em>End</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getCorrectedValue <em>Corrected Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclSourceEntry()
 * @model
 * @generated
 */
public interface TclSourceEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclSourceEntry_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(int)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclSourceEntry_Start()
	 * @model
	 * @generated
	 */
	int getStart();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(int value);

	/**
	 * Returns the value of the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End</em>' attribute.
	 * @see #setEnd(int)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclSourceEntry_End()
	 * @model
	 * @generated
	 */
	int getEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getEnd <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End</em>' attribute.
	 * @see #getEnd()
	 * @generated
	 */
	void setEnd(int value);

	/**
	 * Returns the value of the '<em><b>Corrected Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Corrected Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Corrected Value</em>' attribute.
	 * @see #setCorrectedValue(String)
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage#getTclSourceEntry_CorrectedValue()
	 * @model
	 * @generated
	 */
	String getCorrectedValue();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry#getCorrectedValue <em>Corrected Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Corrected Value</em>' attribute.
	 * @see #getCorrectedValue()
	 * @generated
	 */
	void setCorrectedValue(String value);

} // TclSourceEntry
