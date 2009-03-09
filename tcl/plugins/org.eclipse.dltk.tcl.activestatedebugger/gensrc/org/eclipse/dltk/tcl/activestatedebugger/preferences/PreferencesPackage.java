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
 * $Id: PreferencesPackage.java,v 1.3 2009/03/09 06:29:41 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ModelElementPatternImpl <em>Model Element Pattern</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ModelElementPatternImpl
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getModelElementPattern()
	 * @generated
	 */
	int MODEL_ELEMENT_PATTERN = 0;

	/**
	 * The feature id for the '<em><b>Handle Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_PATTERN__HANDLE_IDENTIFIER = 0;

	/**
	 * The feature id for the '<em><b>Include</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_PATTERN__INCLUDE = 1;

	/**
	 * The number of structural features of the '<em>Model Element Pattern</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_PATTERN_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.InstrumentationConfigImpl <em>Instrumentation Config</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.InstrumentationConfigImpl
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getInstrumentationConfig()
	 * @generated
	 */
	int INSTRUMENTATION_CONFIG = 1;

	/**
	 * The feature id for the '<em><b>Model Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTRUMENTATION_CONFIG__MODEL_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTRUMENTATION_CONFIG__MODE = 1;

	/**
	 * The number of structural features of the '<em>Instrumentation Config</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTRUMENTATION_CONFIG_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode <em>Instrumentation Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getInstrumentationMode()
	 * @generated
	 */
	int INSTRUMENTATION_MODE = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern <em>Model Element Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Element Pattern</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern
	 * @generated
	 */
	EClass getModelElementPattern();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#getHandleIdentifier <em>Handle Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle Identifier</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#getHandleIdentifier()
	 * @see #getModelElementPattern()
	 * @generated
	 */
	EAttribute getModelElementPattern_HandleIdentifier();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#isInclude <em>Include</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Include</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern#isInclude()
	 * @see #getModelElementPattern()
	 * @generated
	 */
	EAttribute getModelElementPattern_Include();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig <em>Instrumentation Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Instrumentation Config</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig
	 * @generated
	 */
	EClass getInstrumentationConfig();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig#getModelElements <em>Model Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Model Elements</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig#getModelElements()
	 * @see #getInstrumentationConfig()
	 * @generated
	 */
	EReference getInstrumentationConfig_ModelElements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig#getMode <em>Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mode</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig#getMode()
	 * @see #getInstrumentationConfig()
	 * @generated
	 */
	EAttribute getInstrumentationConfig_Mode();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode <em>Instrumentation Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Instrumentation Mode</em>'.
	 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode
	 * @generated
	 */
	EEnum getInstrumentationMode();

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
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ModelElementPatternImpl <em>Model Element Pattern</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.ModelElementPatternImpl
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getModelElementPattern()
		 * @generated
		 */
		EClass MODEL_ELEMENT_PATTERN = eINSTANCE.getModelElementPattern();

		/**
		 * The meta object literal for the '<em><b>Handle Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ELEMENT_PATTERN__HANDLE_IDENTIFIER = eINSTANCE.getModelElementPattern_HandleIdentifier();

		/**
		 * The meta object literal for the '<em><b>Include</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ELEMENT_PATTERN__INCLUDE = eINSTANCE.getModelElementPattern_Include();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.InstrumentationConfigImpl <em>Instrumentation Config</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.InstrumentationConfigImpl
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getInstrumentationConfig()
		 * @generated
		 */
		EClass INSTRUMENTATION_CONFIG = eINSTANCE.getInstrumentationConfig();

		/**
		 * The meta object literal for the '<em><b>Model Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INSTRUMENTATION_CONFIG__MODEL_ELEMENTS = eINSTANCE.getInstrumentationConfig_ModelElements();

		/**
		 * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSTRUMENTATION_CONFIG__MODE = eINSTANCE.getInstrumentationConfig_Mode();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode <em>Instrumentation Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode
		 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.impl.PreferencesPackageImpl#getInstrumentationMode()
		 * @generated
		 */
		EEnum INSTRUMENTATION_MODE = eINSTANCE.getInstrumentationMode();

	}

} //PreferencesPackage
