/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackagesFactory.java,v 1.5 2009/07/08 10:53:04 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.core.packages;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage
 * @generated
 */
public interface TclPackagesFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TclPackagesFactory eINSTANCE = org.eclipse.dltk.tcl.core.packages.impl.TclPackagesFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Tcl Package Info</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tcl Package Info</em>'.
	 * @generated
	 */
	TclPackageInfo createTclPackageInfo();

	/**
	 * Returns a new object of class '<em>Tcl Interpreter Info</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tcl Interpreter Info</em>'.
	 * @generated
	 */
	TclInterpreterInfo createTclInterpreterInfo();

	/**
	 * Returns a new object of class '<em>Tcl Project Info</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tcl Project Info</em>'.
	 * @generated
	 */
	TclProjectInfo createTclProjectInfo();

	/**
	 * Returns a new object of class '<em>Tcl Module Info</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tcl Module Info</em>'.
	 * @generated
	 */
	TclModuleInfo createTclModuleInfo();

	/**
	 * Returns a new object of class '<em>Tcl Source Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tcl Source Entry</em>'.
	 * @generated
	 */
	TclSourceEntry createTclSourceEntry();

	/**
	 * Returns a new object of class '<em>User Correction</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>User Correction</em>'.
	 * @generated
	 */
	UserCorrection createUserCorrection();

	/**
	 * Returns a new object of class '<em>Variable Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variable Value</em>'.
	 * @generated
	 * @since 2.0
	 */
	VariableValue createVariableValue();

	/**
	 * Returns a new object of class '<em>Variable Map</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variable Map</em>'.
	 * @generated
	 */
	VariableMap createVariableMap();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	TclPackagesPackage getTclPackagesPackage();

} //TclPackagesFactory
