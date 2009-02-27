/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerInstance.java,v 1.6 2009/02/27 15:44:39 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import org.eclipse.dltk.validators.configs.ValidatorInstance;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Checker Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getCommandLineOptions <em>Command Line Options</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironments <em>Environments</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getFavorite <em>Favorite</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getConfigs <em>Configs</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance()
 * @model
 * @generated
 */
public interface CheckerInstance extends ValidatorInstance {
	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion
	 * @see #setVersion(CheckerVersion)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_Version()
	 * @model
	 * @generated
	 */
	CheckerVersion getVersion();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(CheckerVersion value);

	/**
	 * Returns the value of the '<em><b>Command Line Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Command Line Options</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Command Line Options</em>' attribute.
	 * @see #setCommandLineOptions(String)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_CommandLineOptions()
	 * @model
	 * @generated
	 */
	String getCommandLineOptions();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getCommandLineOptions <em>Command Line Options</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Command Line Options</em>' attribute.
	 * @see #getCommandLineOptions()
	 * @generated
	 */
	void setCommandLineOptions(String value);

	/**
	 * Returns the value of the '<em><b>Environments</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getInstance <em>Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Environments</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Environments</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_Environments()
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getInstance
	 * @model opposite="instance" containment="true"
	 * @generated
	 */
	EList<CheckerEnvironmentInstance> getEnvironments();

	/**
	 * Returns the value of the '<em><b>Favorite</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Favorite</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Favorite</em>' reference.
	 * @see #setFavorite(CheckerConfig)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_Favorite()
	 * @model
	 * @generated
	 */
	CheckerConfig getFavorite();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getFavorite <em>Favorite</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Favorite</em>' reference.
	 * @see #getFavorite()
	 * @generated
	 */
	void setFavorite(CheckerConfig value);

	/**
	 * Returns the value of the '<em><b>Configs</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configs</em>' containment reference list.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_Configs()
	 * @model containment="true"
	 * @generated
	 */
	EList<CheckerConfig> getConfigs();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	CheckerEnvironmentInstance getEnvironment(String environmentId);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	CheckerEnvironmentInstance findEnvironment(String environmentId);

} // CheckerInstance
