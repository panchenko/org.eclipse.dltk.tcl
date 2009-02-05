/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigInstance.java,v 1.1 2009/02/05 18:41:36 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Config Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#isSummary <em>Summary</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#getCommandLineOptions <em>Command Line Options</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#getMode <em>Mode</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#getMessageStates <em>Message States</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getConfigInstance()
 * @model
 * @generated
 */
public interface ConfigInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Summary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Summary</em>' attribute.
	 * @see #setSummary(boolean)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getConfigInstance_Summary()
	 * @model
	 * @generated
	 */
	boolean isSummary();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#isSummary <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Summary</em>' attribute.
	 * @see #isSummary()
	 * @generated
	 */
	void setSummary(boolean value);

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
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getConfigInstance_CommandLineOptions()
	 * @model
	 * @generated
	 */
	String getCommandLineOptions();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#getCommandLineOptions <em>Command Line Options</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Command Line Options</em>' attribute.
	 * @see #getCommandLineOptions()
	 * @generated
	 */
	void setCommandLineOptions(String value);

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
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getConfigInstance_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mode</em>' attribute.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode
	 * @see #setMode(CheckerMode)
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getConfigInstance_Mode()
	 * @model
	 * @generated
	 */
	CheckerMode getMode();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode</em>' attribute.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode
	 * @see #getMode()
	 * @generated
	 */
	void setMode(CheckerMode value);

	/**
	 * Returns the value of the '<em><b>Message States</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Message States</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Message States</em>' map.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getConfigInstance_MessageStates()
	 * @model mapType="org.eclipse.dltk.tcl.tclchecker.model.configs.MessageStateMap<org.eclipse.emf.ecore.EString, org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState>"
	 * @generated
	 */
	EMap<String, MessageState> getMessageStates();

} // ConfigInstance
