/**
 * <copyright>
 * </copyright>
 *
 * $Id: ManpagesPackage.java,v 1.1 2009/12/31 09:18:25 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ui.manpages;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.ui.manpages.ManpagesFactory
 * @model kind="package"
 * @since 2.0
 */
public interface ManpagesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "manpages"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/dltk/tcl/manpages"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "manpages"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ManpagesPackage eINSTANCE = org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.DocumentationImpl <em>Documentation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.DocumentationImpl
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getDocumentation()
	 * @generated
	 */
	int DOCUMENTATION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Folders</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION__FOLDERS = 1;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION__DEFAULT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION__ID = 3;

	/**
	 * The number of structural features of the '<em>Documentation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.ManPageFolderImpl <em>Man Page Folder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManPageFolderImpl
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getManPageFolder()
	 * @generated
	 */
	int MAN_PAGE_FOLDER = 1;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAN_PAGE_FOLDER__PATH = 0;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAN_PAGE_FOLDER__KEYWORDS = 1;

	/**
	 * The number of structural features of the '<em>Man Page Folder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAN_PAGE_FOLDER_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.StringToStringEntryImpl <em>String To String Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.StringToStringEntryImpl
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getStringToStringEntry()
	 * @generated
	 */
	int STRING_TO_STRING_ENTRY = 2;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_STRING_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_STRING_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>String To String Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_STRING_ENTRY_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.InterpreterDocumentationImpl <em>Interpreter Documentation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.InterpreterDocumentationImpl
	 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getInterpreterDocumentation()
	 * @generated
	 */
	int INTERPRETER_DOCUMENTATION = 3;

	/**
	 * The feature id for the '<em><b>Documentation Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPRETER_DOCUMENTATION__DOCUMENTATION_ID = 0;

	/**
	 * The number of structural features of the '<em>Interpreter Documentation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPRETER_DOCUMENTATION_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ui.manpages.Documentation <em>Documentation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Documentation</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.Documentation
	 * @generated
	 */
	EClass getDocumentation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ui.manpages.Documentation#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.Documentation#getName()
	 * @see #getDocumentation()
	 * @generated
	 */
	EAttribute getDocumentation_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ui.manpages.Documentation#getFolders <em>Folders</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Folders</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.Documentation#getFolders()
	 * @see #getDocumentation()
	 * @generated
	 */
	EReference getDocumentation_Folders();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ui.manpages.Documentation#isDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.Documentation#isDefault()
	 * @see #getDocumentation()
	 * @generated
	 */
	EAttribute getDocumentation_Default();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ui.manpages.Documentation#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.Documentation#getId()
	 * @see #getDocumentation()
	 * @generated
	 */
	EAttribute getDocumentation_Id();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ui.manpages.ManPageFolder <em>Man Page Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Man Page Folder</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.ManPageFolder
	 * @generated
	 */
	EClass getManPageFolder();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ui.manpages.ManPageFolder#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.ManPageFolder#getPath()
	 * @see #getManPageFolder()
	 * @generated
	 */
	EAttribute getManPageFolder_Path();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.dltk.tcl.ui.manpages.ManPageFolder#getKeywords <em>Keywords</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Keywords</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.ManPageFolder#getKeywords()
	 * @see #getManPageFolder()
	 * @generated
	 */
	EReference getManPageFolder_Keywords();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>String To String Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>String To String Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueDataType="org.eclipse.emf.ecore.EString"
	 * @generated
	 */
	EClass getStringToStringEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getStringToStringEntry()
	 * @generated
	 */
	EAttribute getStringToStringEntry_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getStringToStringEntry()
	 * @generated
	 */
	EAttribute getStringToStringEntry_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation <em>Interpreter Documentation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interpreter Documentation</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation
	 * @generated
	 */
	EClass getInterpreterDocumentation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation#getDocumentationId <em>Documentation Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Documentation Id</em>'.
	 * @see org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation#getDocumentationId()
	 * @see #getInterpreterDocumentation()
	 * @generated
	 */
	EAttribute getInterpreterDocumentation_DocumentationId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ManpagesFactory getManpagesFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.DocumentationImpl <em>Documentation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.DocumentationImpl
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getDocumentation()
		 * @generated
		 */
		EClass DOCUMENTATION = eINSTANCE.getDocumentation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENTATION__NAME = eINSTANCE.getDocumentation_Name();

		/**
		 * The meta object literal for the '<em><b>Folders</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENTATION__FOLDERS = eINSTANCE.getDocumentation_Folders();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENTATION__DEFAULT = eINSTANCE.getDocumentation_Default();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENTATION__ID = eINSTANCE.getDocumentation_Id();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.ManPageFolderImpl <em>Man Page Folder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManPageFolderImpl
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getManPageFolder()
		 * @generated
		 */
		EClass MAN_PAGE_FOLDER = eINSTANCE.getManPageFolder();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAN_PAGE_FOLDER__PATH = eINSTANCE.getManPageFolder_Path();

		/**
		 * The meta object literal for the '<em><b>Keywords</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAN_PAGE_FOLDER__KEYWORDS = eINSTANCE.getManPageFolder_Keywords();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.StringToStringEntryImpl <em>String To String Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.StringToStringEntryImpl
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getStringToStringEntry()
		 * @generated
		 */
		EClass STRING_TO_STRING_ENTRY = eINSTANCE.getStringToStringEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_TO_STRING_ENTRY__KEY = eINSTANCE.getStringToStringEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_TO_STRING_ENTRY__VALUE = eINSTANCE.getStringToStringEntry_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ui.manpages.impl.InterpreterDocumentationImpl <em>Interpreter Documentation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.InterpreterDocumentationImpl
		 * @see org.eclipse.dltk.tcl.ui.manpages.impl.ManpagesPackageImpl#getInterpreterDocumentation()
		 * @generated
		 */
		EClass INTERPRETER_DOCUMENTATION = eINSTANCE.getInterpreterDocumentation();

		/**
		 * The meta object literal for the '<em><b>Documentation Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INTERPRETER_DOCUMENTATION__DOCUMENTATION_ID = eINSTANCE.getInterpreterDocumentation_DocumentationId();

	}

} //ManpagesPackage
