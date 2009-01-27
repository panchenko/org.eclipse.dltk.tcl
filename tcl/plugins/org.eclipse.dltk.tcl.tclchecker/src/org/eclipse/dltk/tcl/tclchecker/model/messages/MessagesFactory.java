/**
 * <copyright>
 * </copyright>
 *
 * $Id: MessagesFactory.java,v 1.1 2009/01/27 18:43:47 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.messages;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesPackage
 * @generated
 */
public interface MessagesFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MessagesFactory eINSTANCE = org.eclipse.dltk.tcl.tclchecker.model.messages.impl.MessagesFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Checker Message</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Checker Message</em>'.
	 * @generated
	 */
	CheckerMessage createCheckerMessage();

	/**
	 * Returns a new object of class '<em>Message Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Message Group</em>'.
	 * @generated
	 */
	MessageGroup createMessageGroup();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MessagesPackage getMessagesPackage();

} //MessagesFactory
