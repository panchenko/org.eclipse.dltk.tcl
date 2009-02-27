/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigsFactory.java,v 1.4 2009/02/27 09:16:01 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage
 * @generated
 */
public interface ConfigsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigsFactory eINSTANCE = org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Checker Config</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Checker Config</em>'.
	 * @generated
	 */
	CheckerConfig createCheckerConfig();

	/**
	 * Returns a new object of class '<em>Checker Environment Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Checker Environment Instance</em>'.
	 * @generated
	 */
	CheckerEnvironmentInstance createCheckerEnvironmentInstance();

	/**
	 * Returns a new object of class '<em>Checker Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Checker Instance</em>'.
	 * @generated
	 */
	CheckerInstance createCheckerInstance();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ConfigsPackage getConfigsPackage();

} //ConfigsFactory
