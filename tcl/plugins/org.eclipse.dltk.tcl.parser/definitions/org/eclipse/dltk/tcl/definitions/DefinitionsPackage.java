/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.definitions;

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
 * @see org.eclipse.dltk.tcl.definitions.DefinitionsFactory
 * @model kind="package"
 * @generated
 */
public interface DefinitionsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "definitions";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/dltk/tcl/parserules.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "tclparse";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DefinitionsPackage eINSTANCE = org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.Argument <em>Argument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.Argument
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getArgument()
	 * @generated
	 */
	int ARGUMENT = 0;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT__LOWER_BOUND = 0;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT__UPPER_BOUND = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT__NAME = 2;

	/**
	 * The number of structural features of the '<em>Argument</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.ScopeImpl <em>Scope</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.ScopeImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getScope()
	 * @generated
	 */
	int SCOPE = 2;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE__CHILDREN = 0;

	/**
	 * The number of structural features of the '<em>Scope</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.CommandImpl <em>Command</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.CommandImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getCommand()
	 * @generated
	 */
	int COMMAND = 1;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__CHILDREN = SCOPE__CHILDREN;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__NAME = SCOPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__ARGUMENTS = SCOPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__VERSION = SCOPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Scope</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__SCOPE = SCOPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Deprecated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND__DEPRECATED = SCOPE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Command</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMAND_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.ConstantImpl <em>Constant</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.ConstantImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getConstant()
	 * @generated
	 */
	int CONSTANT = 3;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT__LOWER_BOUND = ARGUMENT__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT__UPPER_BOUND = ARGUMENT__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT__NAME = ARGUMENT__NAME;

	/**
	 * The feature id for the '<em><b>Strict Match</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT__STRICT_MATCH = ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Constant</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTANT_FEATURE_COUNT = ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.GroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.GroupImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getGroup()
	 * @generated
	 */
	int GROUP = 4;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__LOWER_BOUND = ARGUMENT__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__UPPER_BOUND = ARGUMENT__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__NAME = ARGUMENT__NAME;

	/**
	 * The feature id for the '<em><b>Constant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__CONSTANT = ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP__ARGUMENTS = ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_COUNT = ARGUMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.SwitchImpl <em>Switch</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.SwitchImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getSwitch()
	 * @generated
	 */
	int SWITCH = 5;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH__LOWER_BOUND = ARGUMENT__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH__UPPER_BOUND = ARGUMENT__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH__NAME = ARGUMENT__NAME;

	/**
	 * The feature id for the '<em><b>Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH__GROUPS = ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Check Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH__CHECK_PREFIX = ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Switch</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH_FEATURE_COUNT = ARGUMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.TypedArgumentImpl <em>Typed Argument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.TypedArgumentImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getTypedArgument()
	 * @generated
	 */
	int TYPED_ARGUMENT = 6;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ARGUMENT__LOWER_BOUND = ARGUMENT__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ARGUMENT__UPPER_BOUND = ARGUMENT__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ARGUMENT__NAME = ARGUMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ARGUMENT__TYPE = ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Typed Argument</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPED_ARGUMENT_FEATURE_COUNT = ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.NamespaceImpl <em>Namespace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.NamespaceImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getNamespace()
	 * @generated
	 */
	int NAMESPACE = 7;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__CHILDREN = SCOPE__CHILDREN;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__NAME = SCOPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Namespace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMESPACE_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.impl.ComplexArgumentImpl <em>Complex Argument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.impl.ComplexArgumentImpl
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getComplexArgument()
	 * @generated
	 */
	int COMPLEX_ARGUMENT = 8;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_ARGUMENT__LOWER_BOUND = ARGUMENT__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_ARGUMENT__UPPER_BOUND = ARGUMENT__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_ARGUMENT__NAME = ARGUMENT__NAME;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_ARGUMENT__ARGUMENTS = ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Complex Argument</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_ARGUMENT_FEATURE_COUNT = ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.definitions.ArgumentType <em>Argument Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.definitions.ArgumentType
	 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getArgumentType()
	 * @generated
	 */
	int ARGUMENT_TYPE = 9;


	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.Argument <em>Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Argument</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Argument
	 * @generated
	 */
	EClass getArgument();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Argument#getLowerBound <em>Lower Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lower Bound</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Argument#getLowerBound()
	 * @see #getArgument()
	 * @generated
	 */
	EAttribute getArgument_LowerBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Argument#getUpperBound <em>Upper Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Upper Bound</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Argument#getUpperBound()
	 * @see #getArgument()
	 * @generated
	 */
	EAttribute getArgument_UpperBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Argument#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Argument#getName()
	 * @see #getArgument()
	 * @generated
	 */
	EAttribute getArgument_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.Command <em>Command</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Command</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Command
	 * @generated
	 */
	EClass getCommand();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Command#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Command#getName()
	 * @see #getCommand()
	 * @generated
	 */
	EAttribute getCommand_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.definitions.Command#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Command#getArguments()
	 * @see #getCommand()
	 * @generated
	 */
	EReference getCommand_Arguments();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Command#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Command#getVersion()
	 * @see #getCommand()
	 * @generated
	 */
	EAttribute getCommand_Version();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.dltk.tcl.definitions.Command#getScope <em>Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Scope</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Command#getScope()
	 * @see #getCommand()
	 * @generated
	 */
	EReference getCommand_Scope();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Command#getDeprecated <em>Deprecated</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Deprecated</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Command#getDeprecated()
	 * @see #getCommand()
	 * @generated
	 */
	EAttribute getCommand_Deprecated();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.Scope <em>Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scope</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Scope
	 * @generated
	 */
	EClass getScope();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.definitions.Scope#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Scope#getChildren()
	 * @see #getScope()
	 * @generated
	 */
	EReference getScope_Children();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.Constant <em>Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constant</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Constant
	 * @generated
	 */
	EClass getConstant();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Constant#isStrictMatch <em>Strict Match</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Strict Match</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Constant#isStrictMatch()
	 * @see #getConstant()
	 * @generated
	 */
	EAttribute getConstant_StrictMatch();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.Group <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Group
	 * @generated
	 */
	EClass getGroup();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Group#getConstant <em>Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constant</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Group#getConstant()
	 * @see #getGroup()
	 * @generated
	 */
	EAttribute getGroup_Constant();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.definitions.Group#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Group#getArguments()
	 * @see #getGroup()
	 * @generated
	 */
	EReference getGroup_Arguments();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.Switch <em>Switch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Switch</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Switch
	 * @generated
	 */
	EClass getSwitch();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.definitions.Switch#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Groups</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Switch#getGroups()
	 * @see #getSwitch()
	 * @generated
	 */
	EReference getSwitch_Groups();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Switch#isCheckPrefix <em>Check Prefix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Check Prefix</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Switch#isCheckPrefix()
	 * @see #getSwitch()
	 * @generated
	 */
	EAttribute getSwitch_CheckPrefix();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.TypedArgument <em>Typed Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Typed Argument</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.TypedArgument
	 * @generated
	 */
	EClass getTypedArgument();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.TypedArgument#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.TypedArgument#getType()
	 * @see #getTypedArgument()
	 * @generated
	 */
	EAttribute getTypedArgument_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.Namespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Namespace</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Namespace
	 * @generated
	 */
	EClass getNamespace();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.definitions.Namespace#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.Namespace#getName()
	 * @see #getNamespace()
	 * @generated
	 */
	EAttribute getNamespace_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.definitions.ComplexArgument <em>Complex Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Complex Argument</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.ComplexArgument
	 * @generated
	 */
	EClass getComplexArgument();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.definitions.ComplexArgument#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.ComplexArgument#getArguments()
	 * @see #getComplexArgument()
	 * @generated
	 */
	EReference getComplexArgument_Arguments();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.dltk.tcl.definitions.ArgumentType <em>Argument Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Argument Type</em>'.
	 * @see org.eclipse.dltk.tcl.definitions.ArgumentType
	 * @generated
	 */
	EEnum getArgumentType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DefinitionsFactory getDefinitionsFactory();

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
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.Argument <em>Argument</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.Argument
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getArgument()
		 * @generated
		 */
		EClass ARGUMENT = eINSTANCE.getArgument();

		/**
		 * The meta object literal for the '<em><b>Lower Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARGUMENT__LOWER_BOUND = eINSTANCE.getArgument_LowerBound();

		/**
		 * The meta object literal for the '<em><b>Upper Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARGUMENT__UPPER_BOUND = eINSTANCE.getArgument_UpperBound();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARGUMENT__NAME = eINSTANCE.getArgument_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.CommandImpl <em>Command</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.CommandImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getCommand()
		 * @generated
		 */
		EClass COMMAND = eINSTANCE.getCommand();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMAND__NAME = eINSTANCE.getCommand_Name();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMAND__ARGUMENTS = eINSTANCE.getCommand_Arguments();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMAND__VERSION = eINSTANCE.getCommand_Version();

		/**
		 * The meta object literal for the '<em><b>Scope</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMAND__SCOPE = eINSTANCE.getCommand_Scope();

		/**
		 * The meta object literal for the '<em><b>Deprecated</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMAND__DEPRECATED = eINSTANCE.getCommand_Deprecated();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.ScopeImpl <em>Scope</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.ScopeImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getScope()
		 * @generated
		 */
		EClass SCOPE = eINSTANCE.getScope();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCOPE__CHILDREN = eINSTANCE.getScope_Children();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.ConstantImpl <em>Constant</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.ConstantImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getConstant()
		 * @generated
		 */
		EClass CONSTANT = eINSTANCE.getConstant();

		/**
		 * The meta object literal for the '<em><b>Strict Match</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTANT__STRICT_MATCH = eINSTANCE.getConstant_StrictMatch();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.GroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.GroupImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getGroup()
		 * @generated
		 */
		EClass GROUP = eINSTANCE.getGroup();

		/**
		 * The meta object literal for the '<em><b>Constant</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROUP__CONSTANT = eINSTANCE.getGroup_Constant();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP__ARGUMENTS = eINSTANCE.getGroup_Arguments();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.SwitchImpl <em>Switch</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.SwitchImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getSwitch()
		 * @generated
		 */
		EClass SWITCH = eINSTANCE.getSwitch();

		/**
		 * The meta object literal for the '<em><b>Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWITCH__GROUPS = eINSTANCE.getSwitch_Groups();

		/**
		 * The meta object literal for the '<em><b>Check Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SWITCH__CHECK_PREFIX = eINSTANCE.getSwitch_CheckPrefix();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.TypedArgumentImpl <em>Typed Argument</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.TypedArgumentImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getTypedArgument()
		 * @generated
		 */
		EClass TYPED_ARGUMENT = eINSTANCE.getTypedArgument();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPED_ARGUMENT__TYPE = eINSTANCE.getTypedArgument_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.NamespaceImpl <em>Namespace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.NamespaceImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getNamespace()
		 * @generated
		 */
		EClass NAMESPACE = eINSTANCE.getNamespace();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMESPACE__NAME = eINSTANCE.getNamespace_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.impl.ComplexArgumentImpl <em>Complex Argument</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.impl.ComplexArgumentImpl
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getComplexArgument()
		 * @generated
		 */
		EClass COMPLEX_ARGUMENT = eINSTANCE.getComplexArgument();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLEX_ARGUMENT__ARGUMENTS = eINSTANCE.getComplexArgument_Arguments();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.definitions.ArgumentType <em>Argument Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.definitions.ArgumentType
		 * @see org.eclipse.dltk.tcl.definitions.impl.DefinitionsPackageImpl#getArgumentType()
		 * @generated
		 */
		EEnum ARGUMENT_TYPE = eINSTANCE.getArgumentType();

	}

} //DefinitionsPackage
