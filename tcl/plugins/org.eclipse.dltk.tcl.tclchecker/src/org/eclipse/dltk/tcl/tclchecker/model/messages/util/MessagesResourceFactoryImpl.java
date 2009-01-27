/**
 * <copyright>
 * </copyright>
 *
 * $Id: MessagesResourceFactoryImpl.java,v 1.1 2009/01/27 18:43:45 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.messages.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource Factory</b> associated with the package.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.util.MessagesResourceImpl
 * @generated
 */
public class MessagesResourceFactoryImpl extends ResourceFactoryImpl {
	/**
	 * Creates an instance of the resource factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessagesResourceFactoryImpl() {
		super();
	}

	/**
	 * Creates an instance of the resource.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Resource createResource(URI uri) {
		MessagesResourceImpl result = new MessagesResourceImpl(uri);
		result.getDefaultLoadOptions().put(
				XMLResource.OPTION_EXTENDED_META_DATA,
				new BasicExtendedMetaData());
		return result;
	}

} //MessagesResourceFactoryImpl
