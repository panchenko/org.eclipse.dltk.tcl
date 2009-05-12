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
package org.eclipse.dltk.tcl.ast.impl;

import org.eclipse.dltk.tcl.ast.*;

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
public class AstFactoryImpl extends EFactoryImpl implements AstFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AstFactory init() {
		try {
			AstFactory theAstFactory = (AstFactory) EPackage.Registry.INSTANCE
					.getEFactory("http:///org/eclipse/dltk/tcl/ast.ecore");
			if (theAstFactory != null) {
				return theAstFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AstFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstFactoryImpl() {
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
		case AstPackage.NODE:
			return createNode();
		case AstPackage.SCRIPT:
			return createScript();
		case AstPackage.STRING_ARGUMENT:
			return createStringArgument();
		case AstPackage.SUBSTITUTION:
			return createSubstitution();
		case AstPackage.TCL_ARGUMENT:
			return createTclArgument();
		case AstPackage.TCL_COMMAND:
			return createTclCommand();
		case AstPackage.TCL_ARGUMENT_LIST:
			return createTclArgumentList();
		case AstPackage.ARGUMENT_MATCH:
			return createArgumentMatch();
		case AstPackage.COMPLEX_STRING:
			return createComplexString();
		case AstPackage.VARIABLE_REFERENCE:
			return createVariableReference();
		case AstPackage.TCL_MODULE:
			return createTclModule();
		case AstPackage.TCL_CODE_MODEL:
			return createTclCodeModel();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node createNode() {
		NodeImpl node = new NodeImpl();
		return node;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Script createScript() {
		ScriptImpl script = new ScriptImpl();
		return script;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringArgument createStringArgument() {
		StringArgumentImpl stringArgument = new StringArgumentImpl();
		return stringArgument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Substitution createSubstitution() {
		SubstitutionImpl substitution = new SubstitutionImpl();
		return substitution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclArgument createTclArgument() {
		TclArgumentImpl tclArgument = new TclArgumentImpl();
		return tclArgument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclCommand createTclCommand() {
		TclCommandImpl tclCommand = new TclCommandImpl();
		return tclCommand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclArgumentList createTclArgumentList() {
		TclArgumentListImpl tclArgumentList = new TclArgumentListImpl();
		return tclArgumentList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArgumentMatch createArgumentMatch() {
		ArgumentMatchImpl argumentMatch = new ArgumentMatchImpl();
		return argumentMatch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexString createComplexString() {
		ComplexStringImpl complexString = new ComplexStringImpl();
		return complexString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VariableReference createVariableReference() {
		VariableReferenceImpl variableReference = new VariableReferenceImpl();
		return variableReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclModule createTclModule() {
		TclModuleImpl tclModule = new TclModuleImpl();
		return tclModule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclCodeModel createTclCodeModel() {
		TclCodeModelImpl tclCodeModel = new TclCodeModelImpl();
		return tclCodeModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstPackage getAstPackage() {
		return (AstPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AstPackage getPackage() {
		return AstPackage.eINSTANCE;
	}

} //AstFactoryImpl
