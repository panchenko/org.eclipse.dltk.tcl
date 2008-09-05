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
package org.eclipse.dltk.tcl.definitions.impl;

import org.eclipse.dltk.tcl.definitions.Argument;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.ComplexArgument;
import org.eclipse.dltk.tcl.definitions.Constant;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.DefinitionsPackage;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.Namespace;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.definitions.Switch;
import org.eclipse.dltk.tcl.definitions.TypedArgument;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DefinitionsPackageImpl extends EPackageImpl implements DefinitionsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass argumentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commandEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scopeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constantEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass switchEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typedArgumentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namespaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass complexArgumentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum argumentTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.dltk.tcl.definitions.DefinitionsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DefinitionsPackageImpl() {
		super(eNS_URI, DefinitionsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DefinitionsPackage init() {
		if (isInited) return (DefinitionsPackage)EPackage.Registry.INSTANCE.getEPackage(DefinitionsPackage.eNS_URI);

		// Obtain or create and register package
		DefinitionsPackageImpl theDefinitionsPackage = (DefinitionsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DefinitionsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DefinitionsPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theDefinitionsPackage.createPackageContents();

		// Initialize created meta-data
		theDefinitionsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDefinitionsPackage.freeze();

		return theDefinitionsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getArgument() {
		return argumentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArgument_LowerBound() {
		return (EAttribute)argumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArgument_UpperBound() {
		return (EAttribute)argumentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArgument_Name() {
		return (EAttribute)argumentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommand() {
		return commandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommand_Name() {
		return (EAttribute)commandEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommand_Arguments() {
		return (EReference)commandEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommand_Version() {
		return (EAttribute)commandEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommand_Scope() {
		return (EReference)commandEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommand_Deprecated() {
		return (EAttribute)commandEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScope() {
		return scopeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScope_Children() {
		return (EReference)scopeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstant() {
		return constantEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstant_Value() {
		return (EAttribute)constantEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstant_StrictMatch() {
		return (EAttribute)constantEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroup() {
		return groupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGroup_Constant() {
		return (EAttribute)groupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroup_Arguments() {
		return (EReference)groupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSwitch() {
		return switchEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSwitch_Groups() {
		return (EReference)switchEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSwitch_CheckPrefix() {
		return (EAttribute)switchEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypedArgument() {
		return typedArgumentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTypedArgument_Type() {
		return (EAttribute)typedArgumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamespace() {
		return namespaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamespace_Name() {
		return (EAttribute)namespaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComplexArgument() {
		return complexArgumentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComplexArgument_Arguments() {
		return (EReference)complexArgumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getArgumentType() {
		return argumentTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DefinitionsFactory getDefinitionsFactory() {
		return (DefinitionsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		argumentEClass = createEClass(ARGUMENT);
		createEAttribute(argumentEClass, ARGUMENT__LOWER_BOUND);
		createEAttribute(argumentEClass, ARGUMENT__UPPER_BOUND);
		createEAttribute(argumentEClass, ARGUMENT__NAME);

		commandEClass = createEClass(COMMAND);
		createEAttribute(commandEClass, COMMAND__NAME);
		createEReference(commandEClass, COMMAND__ARGUMENTS);
		createEAttribute(commandEClass, COMMAND__VERSION);
		createEReference(commandEClass, COMMAND__SCOPE);
		createEAttribute(commandEClass, COMMAND__DEPRECATED);

		scopeEClass = createEClass(SCOPE);
		createEReference(scopeEClass, SCOPE__CHILDREN);

		constantEClass = createEClass(CONSTANT);
		createEAttribute(constantEClass, CONSTANT__VALUE);
		createEAttribute(constantEClass, CONSTANT__STRICT_MATCH);

		groupEClass = createEClass(GROUP);
		createEAttribute(groupEClass, GROUP__CONSTANT);
		createEReference(groupEClass, GROUP__ARGUMENTS);

		switchEClass = createEClass(SWITCH);
		createEReference(switchEClass, SWITCH__GROUPS);
		createEAttribute(switchEClass, SWITCH__CHECK_PREFIX);

		typedArgumentEClass = createEClass(TYPED_ARGUMENT);
		createEAttribute(typedArgumentEClass, TYPED_ARGUMENT__TYPE);

		namespaceEClass = createEClass(NAMESPACE);
		createEAttribute(namespaceEClass, NAMESPACE__NAME);

		complexArgumentEClass = createEClass(COMPLEX_ARGUMENT);
		createEReference(complexArgumentEClass, COMPLEX_ARGUMENT__ARGUMENTS);

		// Create enums
		argumentTypeEEnum = createEEnum(ARGUMENT_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		commandEClass.getESuperTypes().add(this.getScope());
		constantEClass.getESuperTypes().add(this.getArgument());
		groupEClass.getESuperTypes().add(this.getArgument());
		switchEClass.getESuperTypes().add(this.getArgument());
		typedArgumentEClass.getESuperTypes().add(this.getArgument());
		namespaceEClass.getESuperTypes().add(this.getScope());
		complexArgumentEClass.getESuperTypes().add(this.getArgument());

		// Initialize classes and features; add operations and parameters
		initEClass(argumentEClass, Argument.class, "Argument", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArgument_LowerBound(), ecorePackage.getEInt(), "lowerBound", "1", 0, 1, Argument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArgument_UpperBound(), ecorePackage.getEInt(), "upperBound", "1", 0, 1, Argument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArgument_Name(), ecorePackage.getEString(), "name", null, 0, 1, Argument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(commandEClass, Command.class, "Command", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCommand_Name(), ecorePackage.getEString(), "name", null, 0, 1, Command.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommand_Arguments(), this.getArgument(), null, "arguments", null, 0, -1, Command.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommand_Version(), ecorePackage.getEString(), "version", "(-;-)", 0, 1, Command.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommand_Scope(), this.getCommand(), null, "scope", null, 0, -1, Command.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommand_Deprecated(), ecorePackage.getEString(), "deprecated", "", 0, 1, Command.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scopeEClass, Scope.class, "Scope", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScope_Children(), this.getScope(), null, "children", null, 0, -1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constantEClass, Constant.class, "Constant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstant_Value(), ecorePackage.getEString(), "value", null, 0, 1, Constant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConstant_StrictMatch(), ecorePackage.getEBoolean(), "strictMatch", "false", 0, 1, Constant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(groupEClass, Group.class, "Group", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGroup_Constant(), ecorePackage.getEString(), "constant", null, 0, 1, Group.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGroup_Arguments(), this.getArgument(), null, "arguments", null, 0, -1, Group.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(switchEClass, Switch.class, "Switch", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSwitch_Groups(), this.getGroup(), null, "groups", null, 0, -1, Switch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSwitch_CheckPrefix(), ecorePackage.getEBoolean(), "checkPrefix", "false", 0, 1, Switch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(typedArgumentEClass, TypedArgument.class, "TypedArgument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTypedArgument_Type(), this.getArgumentType(), "type", null, 0, 1, TypedArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namespaceEClass, Namespace.class, "Namespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamespace_Name(), ecorePackage.getEString(), "name", null, 0, 1, Namespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(complexArgumentEClass, ComplexArgument.class, "ComplexArgument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getComplexArgument_Arguments(), this.getArgument(), null, "arguments", null, 0, -1, ComplexArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(argumentTypeEEnum, ArgumentType.class, "ArgumentType");
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.ANY);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.INTEGER);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.INDEX);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.NOT_NEGATIVE);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.EXPRESSION);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.SCRIPT);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.VAR_NAME);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.CMD_NAME);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.LEVEL);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.PACKAGE);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.NAMESPACE);
		addEEnumLiteral(argumentTypeEEnum, ArgumentType.BOOLEAN);

		// Create resource
		createResource(eNS_URI);
	}

} //DefinitionsPackageImpl
