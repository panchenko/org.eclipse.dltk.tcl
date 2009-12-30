/**
 * <copyright>
 * </copyright>
 *
 * $Id: ManpagesFactoryImpl.java,v 1.1 2009/12/30 11:09:34 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.internal.ui.manpages.impl;

import java.util.Map;

import org.eclipse.dltk.tcl.internal.ui.manpages.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ManpagesFactoryImpl extends EFactoryImpl implements ManpagesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ManpagesFactory init() {
		try {
			ManpagesFactory theManpagesFactory = (ManpagesFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/dltk/tcl/manpages"); //$NON-NLS-1$ 
			if (theManpagesFactory != null) {
				return theManpagesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ManpagesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ManpagesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ManpagesPackage.DOCUMENTATION: return createDocumentation();
			case ManpagesPackage.MAN_PAGE_FOLDER: return createManPageFolder();
			case ManpagesPackage.STRING_TO_STRING_ENTRY: return (EObject)createStringToStringEntry();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Documentation createDocumentation() {
		DocumentationImpl documentation = new DocumentationImpl();
		return documentation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ManPageFolder createManPageFolder() {
		ManPageFolderImpl manPageFolder = new ManPageFolderImpl();
		return manPageFolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, String> createStringToStringEntry() {
		StringToStringEntryImpl stringToStringEntry = new StringToStringEntryImpl();
		return stringToStringEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ManpagesPackage getManpagesPackage() {
		return (ManpagesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ManpagesPackage getPackage() {
		return ManpagesPackage.eINSTANCE;
	}

} //ManpagesFactoryImpl
