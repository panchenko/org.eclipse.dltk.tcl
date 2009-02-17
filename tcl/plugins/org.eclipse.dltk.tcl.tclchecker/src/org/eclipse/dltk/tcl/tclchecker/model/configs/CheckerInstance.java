/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerInstance.java,v 1.3 2009/02/17 11:52:10 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Checker Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironmentId <em>Environment Id</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getExecutablePath <em>Executable Path</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getPcxFileFolders <em>Pcx File Folders</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isUsePcxFiles <em>Use Pcx Files</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getCommandLineOptions <em>Command Line Options</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isAutomatic <em>Automatic</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance()
 * @model
 * @generated
 */
public interface CheckerInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Environment Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Environment Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Environment Id</em>' attribute.
	 * @see #setEnvironmentId(String)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_EnvironmentId()
	 * @model
	 * @generated
	 */
	String getEnvironmentId();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironmentId <em>Environment Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Environment Id</em>' attribute.
	 * @see #getEnvironmentId()
	 * @generated
	 */
	void setEnvironmentId(String value);

	/**
	 * Returns the value of the '<em><b>Executable Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Executable Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Executable Path</em>' attribute.
	 * @see #setExecutablePath(String)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_ExecutablePath()
	 * @model
	 * @generated
	 */
	String getExecutablePath();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getExecutablePath <em>Executable Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Executable Path</em>' attribute.
	 * @see #getExecutablePath()
	 * @generated
	 */
	void setExecutablePath(String value);

	/**
	 * Returns the value of the '<em><b>Pcx File Folders</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pcx File Folders</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pcx File Folders</em>' attribute list.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_PcxFileFolders()
	 * @model
	 * @generated
	 */
	EList<String> getPcxFileFolders();

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
	 * Returns the value of the '<em><b>Use Pcx Files</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Pcx Files</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Pcx Files</em>' attribute.
	 * @see #setUsePcxFiles(boolean)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_UsePcxFiles()
	 * @model default="true"
	 * @generated
	 */
	boolean isUsePcxFiles();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isUsePcxFiles <em>Use Pcx Files</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Pcx Files</em>' attribute.
	 * @see #isUsePcxFiles()
	 * @generated
	 */
	void setUsePcxFiles(boolean value);

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
	 * Returns the value of the '<em><b>Configuration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configuration</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configuration</em>' reference.
	 * @see #setConfiguration(CheckerConfig)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_Configuration()
	 * @model
	 * @generated
	 */
	CheckerConfig getConfiguration();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getConfiguration <em>Configuration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Configuration</em>' reference.
	 * @see #getConfiguration()
	 * @generated
	 */
	void setConfiguration(CheckerConfig value);

	/**
	 * Returns the value of the '<em><b>Automatic</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Automatic</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Automatic</em>' attribute.
	 * @see #setAutomatic(boolean)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerInstance_Automatic()
	 * @model
	 * @generated
	 */
	boolean isAutomatic();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#isAutomatic <em>Automatic</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Automatic</em>' attribute.
	 * @see #isAutomatic()
	 * @generated
	 */
	void setAutomatic(boolean value);

} // CheckerInstance
