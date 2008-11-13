/**
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 * 
 *
 * $Id: PreferencesPackage.java,v 1.1 2008/11/13 11:10:41 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesFactory
 * @model kind="package"
 * @generated
 */
public interface PreferencesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "preferences"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/dltk/tcl/activestatedebugger.ecore"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.dltk.tcl.activestatedebugger"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PreferencesPackage eINSTANCE = org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PatternImpl <em>Pattern</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PatternImpl
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getPattern()
	 * @generated
	 */
	int PATTERN = 0;

	/**
	 * The feature id for the '<em><b>Include</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATTERN__INCLUDE = 0;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATTERN__PATH = 1;

	/**
	 * The number of structural features of the '<em>Pattern</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATTERN_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.WorkspacePatternImpl <em>Workspace Pattern</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.WorkspacePatternImpl
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getWorkspacePattern()
	 * @generated
	 */
	int WORKSPACE_PATTERN = 1;

	/**
	 * The feature id for the '<em><b>Include</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKSPACE_PATTERN__INCLUDE = PATTERN__INCLUDE;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKSPACE_PATTERN__PATH = PATTERN__PATH;

	/**
	 * The number of structural features of the '<em>Workspace Pattern</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WORKSPACE_PATTERN_FEATURE_COUNT = PATTERN_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ExternalPatternImpl <em>External Pattern</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ExternalPatternImpl
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getExternalPattern()
	 * @generated
	 */
	int EXTERNAL_PATTERN = 2;

	/**
	 * The feature id for the '<em><b>Include</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_PATTERN__INCLUDE = PATTERN__INCLUDE;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_PATTERN__PATH = PATTERN__PATH;

	/**
	 * The number of structural features of the '<em>External Pattern</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTERNAL_PATTERN_FEATURE_COUNT = PATTERN_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern <em>Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Pattern</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern
	 * @generated
	 */
	EClass getPattern();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern#isInclude <em>Include</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Include</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern#isInclude()
	 * @see #getPattern()
	 * @generated
	 */
	EAttribute getPattern_Include();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern#getPath()
	 * @see #getPattern()
	 * @generated
	 */
	EAttribute getPattern_Path();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.WorkspacePattern <em>Workspace Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Workspace Pattern</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.WorkspacePattern
	 * @generated
	 */
	EClass getWorkspacePattern();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ExternalPattern <em>External Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>External Pattern</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.ExternalPattern
	 * @generated
	 */
	EClass getExternalPattern();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PreferencesFactory getPreferencesFactory();

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
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PatternImpl <em>Pattern</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PatternImpl
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getPattern()
		 * @generated
		 */
		EClass PATTERN = eINSTANCE.getPattern();

		/**
		 * The meta object literal for the '<em><b>Include</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATTERN__INCLUDE = eINSTANCE.getPattern_Include();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATTERN__PATH = eINSTANCE.getPattern_Path();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.WorkspacePatternImpl <em>Workspace Pattern</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.WorkspacePatternImpl
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getWorkspacePattern()
		 * @generated
		 */
		EClass WORKSPACE_PATTERN = eINSTANCE.getWorkspacePattern();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ExternalPatternImpl <em>External Pattern</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ExternalPatternImpl
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getExternalPattern()
		 * @generated
		 */
		EClass EXTERNAL_PATTERN = eINSTANCE.getExternalPattern();

	}

} //PreferencesPackage
