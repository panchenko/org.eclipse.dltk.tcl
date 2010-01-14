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
package org.eclipse.dltk.tcl.ast;

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
 * @see org.eclipse.dltk.tcl.ast.AstFactory
 * @model kind="package"
 * @generated
 */
public interface AstPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "ast"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/dltk/tcl/ast.ecore"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.dltk.tcl.ast"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AstPackage eINSTANCE = org.eclipse.dltk.tcl.ast.impl.AstPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.NodeImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 0;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__START = 0;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__END = 1;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.TclArgumentImpl <em>Tcl Argument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.TclArgumentImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclArgument()
	 * @generated
	 */
	int TCL_ARGUMENT = 4;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT__START = NODE__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT__END = NODE__END;

	/**
	 * The number of structural features of the '<em>Tcl Argument</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT_FEATURE_COUNT = NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.ScriptImpl <em>Script</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.ScriptImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getScript()
	 * @generated
	 */
	int SCRIPT = 1;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT__START = TCL_ARGUMENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT__END = TCL_ARGUMENT__END;

	/**
	 * The feature id for the '<em><b>Commands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT__COMMANDS = TCL_ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Content Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT__CONTENT_START = TCL_ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Content End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT__CONTENT_END = TCL_ARGUMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Script</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT_FEATURE_COUNT = TCL_ARGUMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.StringArgumentImpl <em>String Argument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.StringArgumentImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getStringArgument()
	 * @generated
	 */
	int STRING_ARGUMENT = 2;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_ARGUMENT__START = TCL_ARGUMENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_ARGUMENT__END = TCL_ARGUMENT__END;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_ARGUMENT__VALUE = TCL_ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Raw Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_ARGUMENT__RAW_VALUE = TCL_ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>String Argument</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_ARGUMENT_FEATURE_COUNT = TCL_ARGUMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.SubstitutionImpl <em>Substitution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.SubstitutionImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getSubstitution()
	 * @generated
	 */
	int SUBSTITUTION = 3;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBSTITUTION__START = TCL_ARGUMENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBSTITUTION__END = TCL_ARGUMENT__END;

	/**
	 * The feature id for the '<em><b>Commands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBSTITUTION__COMMANDS = TCL_ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Substitution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBSTITUTION_FEATURE_COUNT = TCL_ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl <em>Tcl Command</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.TclCommandImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclCommand()
	 * @generated
	 */
	int TCL_COMMAND = 5;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__START = NODE__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__END = NODE__END;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__NAME = NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__ARGUMENTS = NODE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__DEFINITION = NODE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Matches</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__MATCHES = NODE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__QUALIFIED_NAME = NODE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Matched</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND__MATCHED = NODE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Tcl Command</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_COMMAND_FEATURE_COUNT = NODE_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.TclArgumentListImpl <em>Tcl Argument List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.TclArgumentListImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclArgumentList()
	 * @generated
	 */
	int TCL_ARGUMENT_LIST = 6;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT_LIST__START = TCL_ARGUMENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT_LIST__END = TCL_ARGUMENT__END;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT_LIST__ARGUMENTS = TCL_ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Definition Argument</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT_LIST__DEFINITION_ARGUMENT = TCL_ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT_LIST__KIND = TCL_ARGUMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Tcl Argument List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_ARGUMENT_LIST_FEATURE_COUNT = TCL_ARGUMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.ArgumentMatchImpl <em>Argument Match</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.ArgumentMatchImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getArgumentMatch()
	 * @generated
	 */
	int ARGUMENT_MATCH = 7;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT_MATCH__DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT_MATCH__ARGUMENTS = 1;

	/**
	 * The number of structural features of the '<em>Argument Match</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARGUMENT_MATCH_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.ComplexStringImpl <em>Complex String</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.ComplexStringImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getComplexString()
	 * @generated
	 */
	int COMPLEX_STRING = 8;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_STRING__START = TCL_ARGUMENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_STRING__END = TCL_ARGUMENT__END;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_STRING__ARGUMENTS = TCL_ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_STRING__KIND = TCL_ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Complex String</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_STRING_FEATURE_COUNT = TCL_ARGUMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.VariableReferenceImpl <em>Variable Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.VariableReferenceImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getVariableReference()
	 * @generated
	 */
	int VARIABLE_REFERENCE = 9;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE__START = TCL_ARGUMENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE__END = TCL_ARGUMENT__END;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE__NAME = TCL_ARGUMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Index</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE__INDEX = TCL_ARGUMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Variable Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_REFERENCE_FEATURE_COUNT = TCL_ARGUMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.ISubstitution <em>ISubstitution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.ISubstitution
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getISubstitution()
	 * @generated
	 */
	int ISUBSTITUTION = 10;

	/**
	 * The number of structural features of the '<em>ISubstitution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISUBSTITUTION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.TclModuleImpl <em>Tcl Module</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.TclModuleImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclModule()
	 * @generated
	 */
	int TCL_MODULE = 11;

	/**
	 * The feature id for the '<em><b>Statements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE__STATEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE__SIZE = 1;

	/**
	 * The feature id for the '<em><b>Code Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE__CODE_MODEL = 2;

	/**
	 * The number of structural features of the '<em>Tcl Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_MODULE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.TclCodeModelImpl <em>Tcl Code Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.TclCodeModelImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclCodeModel()
	 * @generated
	 */
	int TCL_CODE_MODEL = 12;

	/**
	 * The feature id for the '<em><b>Delimeters</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_CODE_MODEL__DELIMETERS = 0;

	/**
	 * The feature id for the '<em><b>Line Offsets</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_CODE_MODEL__LINE_OFFSETS = 1;

	/**
	 * The number of structural features of the '<em>Tcl Code Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_CODE_MODEL_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.TclProblemModelImpl <em>Tcl Problem Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.TclProblemModelImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclProblemModel()
	 * @generated
	 */
	int TCL_PROBLEM_MODEL = 13;

	/**
	 * The feature id for the '<em><b>Problems</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM_MODEL__PROBLEMS = 0;

	/**
	 * The number of structural features of the '<em>Tcl Problem Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl <em>Tcl Problem</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.dltk.tcl.ast.impl.TclProblemImpl
	 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclProblem()
	 * @generated
	 */
	int TCL_PROBLEM = 14;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__ARGUMENTS = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__ID = 1;

	/**
	 * The feature id for the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__MESSAGE = 2;

	/**
	 * The feature id for the '<em><b>Source Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__SOURCE_START = 3;

	/**
	 * The feature id for the '<em><b>Source End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__SOURCE_END = 4;

	/**
	 * The feature id for the '<em><b>Error</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__ERROR = 5;

	/**
	 * The feature id for the '<em><b>Warning</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__WARNING = 6;

	/**
	 * The feature id for the '<em><b>File Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__FILE_NAME = 7;

	/**
	 * The feature id for the '<em><b>Line Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM__LINE_NUMBER = 8;

	/**
	 * The number of structural features of the '<em>Tcl Problem</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TCL_PROBLEM_FEATURE_COUNT = 9;

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.Node#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Node#getStart()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Start();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.Node#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Node#getEnd()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_End();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.Script <em>Script</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Script</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Script
	 * @generated
	 */
	EClass getScript();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.Script#getCommands <em>Commands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Commands</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Script#getCommands()
	 * @see #getScript()
	 * @generated
	 */
	EReference getScript_Commands();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.Script#getContentStart <em>Content Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content Start</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Script#getContentStart()
	 * @see #getScript()
	 * @generated
	 */
	EAttribute getScript_ContentStart();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.Script#getContentEnd <em>Content End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content End</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Script#getContentEnd()
	 * @see #getScript()
	 * @generated
	 */
	EAttribute getScript_ContentEnd();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.StringArgument <em>String Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>String Argument</em>'.
	 * @see org.eclipse.dltk.tcl.ast.StringArgument
	 * @generated
	 */
	EClass getStringArgument();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.StringArgument#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.dltk.tcl.ast.StringArgument#getValue()
	 * @see #getStringArgument()
	 * @generated
	 */
	EAttribute getStringArgument_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.StringArgument#getRawValue <em>Raw Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Raw Value</em>'.
	 * @see org.eclipse.dltk.tcl.ast.StringArgument#getRawValue()
	 * @see #getStringArgument()
	 * @generated
	 */
	EAttribute getStringArgument_RawValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.Substitution <em>Substitution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Substitution</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Substitution
	 * @generated
	 */
	EClass getSubstitution();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.Substitution#getCommands <em>Commands</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Commands</em>'.
	 * @see org.eclipse.dltk.tcl.ast.Substitution#getCommands()
	 * @see #getSubstitution()
	 * @generated
	 */
	EReference getSubstitution_Commands();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.TclArgument <em>Tcl Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Argument</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclArgument
	 * @generated
	 */
	EClass getTclArgument();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.TclCommand <em>Tcl Command</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Command</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCommand
	 * @generated
	 */
	EClass getTclCommand();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.dltk.tcl.ast.TclCommand#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCommand#getName()
	 * @see #getTclCommand()
	 * @generated
	 */
	EReference getTclCommand_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.TclCommand#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCommand#getArguments()
	 * @see #getTclCommand()
	 * @generated
	 */
	EReference getTclCommand_Arguments();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.dltk.tcl.ast.TclCommand#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCommand#getDefinition()
	 * @see #getTclCommand()
	 * @generated
	 */
	EReference getTclCommand_Definition();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.TclCommand#getMatches <em>Matches</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Matches</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCommand#getMatches()
	 * @see #getTclCommand()
	 * @generated
	 */
	EReference getTclCommand_Matches();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclCommand#getQualifiedName <em>Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCommand#getQualifiedName()
	 * @see #getTclCommand()
	 * @generated
	 */
	EAttribute getTclCommand_QualifiedName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclCommand#isMatched <em>Matched</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Matched</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCommand#isMatched()
	 * @see #getTclCommand()
	 * @generated
	 */
	EAttribute getTclCommand_Matched();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.TclArgumentList <em>Tcl Argument List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Argument List</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclArgumentList
	 * @generated
	 */
	EClass getTclArgumentList();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.TclArgumentList#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclArgumentList#getArguments()
	 * @see #getTclArgumentList()
	 * @generated
	 */
	EReference getTclArgumentList_Arguments();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.dltk.tcl.ast.TclArgumentList#getDefinitionArgument <em>Definition Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition Argument</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclArgumentList#getDefinitionArgument()
	 * @see #getTclArgumentList()
	 * @generated
	 */
	EReference getTclArgumentList_DefinitionArgument();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclArgumentList#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclArgumentList#getKind()
	 * @see #getTclArgumentList()
	 * @generated
	 */
	EAttribute getTclArgumentList_Kind();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.ArgumentMatch <em>Argument Match</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Argument Match</em>'.
	 * @see org.eclipse.dltk.tcl.ast.ArgumentMatch
	 * @generated
	 */
	EClass getArgumentMatch();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.dltk.tcl.ast.ArgumentMatch#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see org.eclipse.dltk.tcl.ast.ArgumentMatch#getDefinition()
	 * @see #getArgumentMatch()
	 * @generated
	 */
	EReference getArgumentMatch_Definition();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.dltk.tcl.ast.ArgumentMatch#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.ast.ArgumentMatch#getArguments()
	 * @see #getArgumentMatch()
	 * @generated
	 */
	EReference getArgumentMatch_Arguments();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.ComplexString <em>Complex String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Complex String</em>'.
	 * @see org.eclipse.dltk.tcl.ast.ComplexString
	 * @generated
	 */
	EClass getComplexString();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.ComplexString#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.ast.ComplexString#getArguments()
	 * @see #getComplexString()
	 * @generated
	 */
	EReference getComplexString_Arguments();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.ComplexString#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.dltk.tcl.ast.ComplexString#getKind()
	 * @see #getComplexString()
	 * @generated
	 */
	EAttribute getComplexString_Kind();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.VariableReference <em>Variable Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Reference</em>'.
	 * @see org.eclipse.dltk.tcl.ast.VariableReference
	 * @generated
	 */
	EClass getVariableReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.VariableReference#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.dltk.tcl.ast.VariableReference#getName()
	 * @see #getVariableReference()
	 * @generated
	 */
	EAttribute getVariableReference_Name();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.dltk.tcl.ast.VariableReference#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Index</em>'.
	 * @see org.eclipse.dltk.tcl.ast.VariableReference#getIndex()
	 * @see #getVariableReference()
	 * @generated
	 */
	EReference getVariableReference_Index();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.ISubstitution <em>ISubstitution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ISubstitution</em>'.
	 * @see org.eclipse.dltk.tcl.ast.ISubstitution
	 * @generated
	 */
	EClass getISubstitution();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.TclModule <em>Tcl Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Module</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclModule
	 * @generated
	 */
	EClass getTclModule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.TclModule#getStatements <em>Statements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Statements</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclModule#getStatements()
	 * @see #getTclModule()
	 * @generated
	 */
	EReference getTclModule_Statements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclModule#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclModule#getSize()
	 * @see #getTclModule()
	 * @generated
	 */
	EAttribute getTclModule_Size();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.dltk.tcl.ast.TclModule#getCodeModel <em>Code Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Code Model</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclModule#getCodeModel()
	 * @see #getTclModule()
	 * @generated
	 */
	EReference getTclModule_CodeModel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.TclCodeModel <em>Tcl Code Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Code Model</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCodeModel
	 * @generated
	 */
	EClass getTclCodeModel();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.dltk.tcl.ast.TclCodeModel#getDelimeters <em>Delimeters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Delimeters</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCodeModel#getDelimeters()
	 * @see #getTclCodeModel()
	 * @generated
	 */
	EAttribute getTclCodeModel_Delimeters();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.dltk.tcl.ast.TclCodeModel#getLineOffsets <em>Line Offsets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Line Offsets</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclCodeModel#getLineOffsets()
	 * @see #getTclCodeModel()
	 * @generated
	 */
	EAttribute getTclCodeModel_LineOffsets();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.TclProblemModel <em>Tcl Problem Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Problem Model</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblemModel
	 * @generated
	 */
	EClass getTclProblemModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.dltk.tcl.ast.TclProblemModel#getProblems <em>Problems</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Problems</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblemModel#getProblems()
	 * @see #getTclProblemModel()
	 * @generated
	 */
	EReference getTclProblemModel_Problems();

	/**
	 * Returns the meta object for class '{@link org.eclipse.dltk.tcl.ast.TclProblem <em>Tcl Problem</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tcl Problem</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem
	 * @generated
	 */
	EClass getTclProblem();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.dltk.tcl.ast.TclProblem#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Arguments</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#getArguments()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_Arguments();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#getId()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#getMessage <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Message</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#getMessage()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_Message();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#getSourceStart <em>Source Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Start</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#getSourceStart()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_SourceStart();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#getSourceEnd <em>Source End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source End</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#getSourceEnd()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_SourceEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#isError <em>Error</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Error</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#isError()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_Error();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#isWarning <em>Warning</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Warning</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#isWarning()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_Warning();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#getFileName <em>File Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File Name</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#getFileName()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_FileName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.dltk.tcl.ast.TclProblem#getLineNumber <em>Line Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Line Number</em>'.
	 * @see org.eclipse.dltk.tcl.ast.TclProblem#getLineNumber()
	 * @see #getTclProblem()
	 * @generated
	 */
	EAttribute getTclProblem_LineNumber();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AstFactory getAstFactory();

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
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.NodeImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__START = eINSTANCE.getNode_Start();

		/**
		 * The meta object literal for the '<em><b>End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__END = eINSTANCE.getNode_End();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.ScriptImpl <em>Script</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.ScriptImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getScript()
		 * @generated
		 */
		EClass SCRIPT = eINSTANCE.getScript();

		/**
		 * The meta object literal for the '<em><b>Commands</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCRIPT__COMMANDS = eINSTANCE.getScript_Commands();

		/**
		 * The meta object literal for the '<em><b>Content Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCRIPT__CONTENT_START = eINSTANCE.getScript_ContentStart();

		/**
		 * The meta object literal for the '<em><b>Content End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCRIPT__CONTENT_END = eINSTANCE.getScript_ContentEnd();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.StringArgumentImpl <em>String Argument</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.StringArgumentImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getStringArgument()
		 * @generated
		 */
		EClass STRING_ARGUMENT = eINSTANCE.getStringArgument();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_ARGUMENT__VALUE = eINSTANCE.getStringArgument_Value();

		/**
		 * The meta object literal for the '<em><b>Raw Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_ARGUMENT__RAW_VALUE = eINSTANCE
				.getStringArgument_RawValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.SubstitutionImpl <em>Substitution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.SubstitutionImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getSubstitution()
		 * @generated
		 */
		EClass SUBSTITUTION = eINSTANCE.getSubstitution();

		/**
		 * The meta object literal for the '<em><b>Commands</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUBSTITUTION__COMMANDS = eINSTANCE
				.getSubstitution_Commands();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.TclArgumentImpl <em>Tcl Argument</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.TclArgumentImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclArgument()
		 * @generated
		 */
		EClass TCL_ARGUMENT = eINSTANCE.getTclArgument();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.TclCommandImpl <em>Tcl Command</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.TclCommandImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclCommand()
		 * @generated
		 */
		EClass TCL_COMMAND = eINSTANCE.getTclCommand();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_COMMAND__NAME = eINSTANCE.getTclCommand_Name();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_COMMAND__ARGUMENTS = eINSTANCE.getTclCommand_Arguments();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_COMMAND__DEFINITION = eINSTANCE
				.getTclCommand_Definition();

		/**
		 * The meta object literal for the '<em><b>Matches</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_COMMAND__MATCHES = eINSTANCE.getTclCommand_Matches();

		/**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_COMMAND__QUALIFIED_NAME = eINSTANCE
				.getTclCommand_QualifiedName();

		/**
		 * The meta object literal for the '<em><b>Matched</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_COMMAND__MATCHED = eINSTANCE.getTclCommand_Matched();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.TclArgumentListImpl <em>Tcl Argument List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.TclArgumentListImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclArgumentList()
		 * @generated
		 */
		EClass TCL_ARGUMENT_LIST = eINSTANCE.getTclArgumentList();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_ARGUMENT_LIST__ARGUMENTS = eINSTANCE
				.getTclArgumentList_Arguments();

		/**
		 * The meta object literal for the '<em><b>Definition Argument</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_ARGUMENT_LIST__DEFINITION_ARGUMENT = eINSTANCE
				.getTclArgumentList_DefinitionArgument();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_ARGUMENT_LIST__KIND = eINSTANCE
				.getTclArgumentList_Kind();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.ArgumentMatchImpl <em>Argument Match</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.ArgumentMatchImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getArgumentMatch()
		 * @generated
		 */
		EClass ARGUMENT_MATCH = eINSTANCE.getArgumentMatch();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ARGUMENT_MATCH__DEFINITION = eINSTANCE
				.getArgumentMatch_Definition();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ARGUMENT_MATCH__ARGUMENTS = eINSTANCE
				.getArgumentMatch_Arguments();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.ComplexStringImpl <em>Complex String</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.ComplexStringImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getComplexString()
		 * @generated
		 */
		EClass COMPLEX_STRING = eINSTANCE.getComplexString();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLEX_STRING__ARGUMENTS = eINSTANCE
				.getComplexString_Arguments();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_STRING__KIND = eINSTANCE.getComplexString_Kind();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.VariableReferenceImpl <em>Variable Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.VariableReferenceImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getVariableReference()
		 * @generated
		 */
		EClass VARIABLE_REFERENCE = eINSTANCE.getVariableReference();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIABLE_REFERENCE__NAME = eINSTANCE
				.getVariableReference_Name();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE_REFERENCE__INDEX = eINSTANCE
				.getVariableReference_Index();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.ISubstitution <em>ISubstitution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.ISubstitution
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getISubstitution()
		 * @generated
		 */
		EClass ISUBSTITUTION = eINSTANCE.getISubstitution();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.TclModuleImpl <em>Tcl Module</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.TclModuleImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclModule()
		 * @generated
		 */
		EClass TCL_MODULE = eINSTANCE.getTclModule();

		/**
		 * The meta object literal for the '<em><b>Statements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_MODULE__STATEMENTS = eINSTANCE.getTclModule_Statements();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_MODULE__SIZE = eINSTANCE.getTclModule_Size();

		/**
		 * The meta object literal for the '<em><b>Code Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_MODULE__CODE_MODEL = eINSTANCE.getTclModule_CodeModel();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.TclCodeModelImpl <em>Tcl Code Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.TclCodeModelImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclCodeModel()
		 * @generated
		 */
		EClass TCL_CODE_MODEL = eINSTANCE.getTclCodeModel();

		/**
		 * The meta object literal for the '<em><b>Delimeters</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_CODE_MODEL__DELIMETERS = eINSTANCE
				.getTclCodeModel_Delimeters();

		/**
		 * The meta object literal for the '<em><b>Line Offsets</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_CODE_MODEL__LINE_OFFSETS = eINSTANCE
				.getTclCodeModel_LineOffsets();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.TclProblemModelImpl <em>Tcl Problem Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.TclProblemModelImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclProblemModel()
		 * @generated
		 */
		EClass TCL_PROBLEM_MODEL = eINSTANCE.getTclProblemModel();

		/**
		 * The meta object literal for the '<em><b>Problems</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TCL_PROBLEM_MODEL__PROBLEMS = eINSTANCE
				.getTclProblemModel_Problems();

		/**
		 * The meta object literal for the '{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl <em>Tcl Problem</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.dltk.tcl.ast.impl.TclProblemImpl
		 * @see org.eclipse.dltk.tcl.ast.impl.AstPackageImpl#getTclProblem()
		 * @generated
		 */
		EClass TCL_PROBLEM = eINSTANCE.getTclProblem();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__ARGUMENTS = eINSTANCE.getTclProblem_Arguments();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__ID = eINSTANCE.getTclProblem_Id();

		/**
		 * The meta object literal for the '<em><b>Message</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__MESSAGE = eINSTANCE.getTclProblem_Message();

		/**
		 * The meta object literal for the '<em><b>Source Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__SOURCE_START = eINSTANCE
				.getTclProblem_SourceStart();

		/**
		 * The meta object literal for the '<em><b>Source End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__SOURCE_END = eINSTANCE
				.getTclProblem_SourceEnd();

		/**
		 * The meta object literal for the '<em><b>Error</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__ERROR = eINSTANCE.getTclProblem_Error();

		/**
		 * The meta object literal for the '<em><b>Warning</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__WARNING = eINSTANCE.getTclProblem_Warning();

		/**
		 * The meta object literal for the '<em><b>File Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__FILE_NAME = eINSTANCE.getTclProblem_FileName();

		/**
		 * The meta object literal for the '<em><b>Line Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TCL_PROBLEM__LINE_NUMBER = eINSTANCE
				.getTclProblem_LineNumber();

	}

} //AstPackage
