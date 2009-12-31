/**
 * <copyright>
 * </copyright>
 *
 * $Id: ManpagesFactory.java,v 1.1 2009/12/31 09:18:24 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ui.manpages;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage
 * @since 2.0
 */
public interface ManpagesFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ManpagesFactory eINSTANCE = org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Documentation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Documentation</em>'.
	 * @generated
	 */
	Documentation createDocumentation();

	/**
	 * Returns a new object of class '<em>Man Page Folder</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Man Page Folder</em>'.
	 * @generated
	 */
	ManPageFolder createManPageFolder();

	/**
	 * Returns a new object of class '<em>Interpreter Documentation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Interpreter Documentation</em>'.
	 * @generated
	 */
	InterpreterDocumentation createInterpreterDocumentation();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ManpagesPackage getManpagesPackage();

} //ManpagesFactory
