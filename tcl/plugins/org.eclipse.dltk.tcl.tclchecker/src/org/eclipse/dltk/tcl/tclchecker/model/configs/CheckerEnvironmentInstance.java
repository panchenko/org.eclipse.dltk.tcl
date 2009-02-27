/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerEnvironmentInstance.java,v 1.1 2009/02/27 09:16:01 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import org.eclipse.dltk.validators.configs.ValidatorEnvironmentInstance;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Checker Environment Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getPcxFileFolders <em>Pcx File Folders</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#isUsePcxFiles <em>Use Pcx Files</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getInstance <em>Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerEnvironmentInstance()
 * @model
 * @generated
 */
public interface CheckerEnvironmentInstance extends ValidatorEnvironmentInstance {
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
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerEnvironmentInstance_PcxFileFolders()
	 * @model
	 * @generated
	 */
	EList<String> getPcxFileFolders();

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
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerEnvironmentInstance_UsePcxFiles()
	 * @model default="true"
	 * @generated
	 */
	boolean isUsePcxFiles();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#isUsePcxFiles <em>Use Pcx Files</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Pcx Files</em>' attribute.
	 * @see #isUsePcxFiles()
	 * @generated
	 */
	void setUsePcxFiles(boolean value);

	/**
	 * Returns the value of the '<em><b>Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironments <em>Environments</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instance</em>' container reference.
	 * @see #setInstance(CheckerInstance)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerEnvironmentInstance_Instance()
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance#getEnvironments
	 * @model opposite="environments" transient="false"
	 * @generated
	 */
	CheckerInstance getInstance();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance#getInstance <em>Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instance</em>' container reference.
	 * @see #getInstance()
	 * @generated
	 */
	void setInstance(CheckerInstance value);

} // CheckerEnvironmentInstance
