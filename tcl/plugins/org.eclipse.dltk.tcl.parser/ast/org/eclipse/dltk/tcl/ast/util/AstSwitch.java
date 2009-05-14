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
package org.eclipse.dltk.tcl.ast.util;

import java.util.List;

import org.eclipse.dltk.tcl.ast.*;
import org.eclipse.dltk.tcl.ast.AstPackage;
import org.eclipse.dltk.tcl.ast.Node;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.ast.AstPackage
 * @generated
 */
public class AstSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AstPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstSwitch() {
		if (modelPackage == null) {
			modelPackage = AstPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		} else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(
					eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case AstPackage.NODE: {
			Node node = (Node) theEObject;
			T result = caseNode(node);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.SCRIPT: {
			Script script = (Script) theEObject;
			T result = caseScript(script);
			if (result == null)
				result = caseTclArgument(script);
			if (result == null)
				result = caseNode(script);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.STRING_ARGUMENT: {
			StringArgument stringArgument = (StringArgument) theEObject;
			T result = caseStringArgument(stringArgument);
			if (result == null)
				result = caseTclArgument(stringArgument);
			if (result == null)
				result = caseNode(stringArgument);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.SUBSTITUTION: {
			Substitution substitution = (Substitution) theEObject;
			T result = caseSubstitution(substitution);
			if (result == null)
				result = caseTclArgument(substitution);
			if (result == null)
				result = caseISubstitution(substitution);
			if (result == null)
				result = caseNode(substitution);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.TCL_ARGUMENT: {
			TclArgument tclArgument = (TclArgument) theEObject;
			T result = caseTclArgument(tclArgument);
			if (result == null)
				result = caseNode(tclArgument);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.TCL_COMMAND: {
			TclCommand tclCommand = (TclCommand) theEObject;
			T result = caseTclCommand(tclCommand);
			if (result == null)
				result = caseNode(tclCommand);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.TCL_ARGUMENT_LIST: {
			TclArgumentList tclArgumentList = (TclArgumentList) theEObject;
			T result = caseTclArgumentList(tclArgumentList);
			if (result == null)
				result = caseTclArgument(tclArgumentList);
			if (result == null)
				result = caseNode(tclArgumentList);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.ARGUMENT_MATCH: {
			ArgumentMatch argumentMatch = (ArgumentMatch) theEObject;
			T result = caseArgumentMatch(argumentMatch);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.COMPLEX_STRING: {
			ComplexString complexString = (ComplexString) theEObject;
			T result = caseComplexString(complexString);
			if (result == null)
				result = caseTclArgument(complexString);
			if (result == null)
				result = caseISubstitution(complexString);
			if (result == null)
				result = caseNode(complexString);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.VARIABLE_REFERENCE: {
			VariableReference variableReference = (VariableReference) theEObject;
			T result = caseVariableReference(variableReference);
			if (result == null)
				result = caseTclArgument(variableReference);
			if (result == null)
				result = caseISubstitution(variableReference);
			if (result == null)
				result = caseNode(variableReference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.ISUBSTITUTION: {
			ISubstitution iSubstitution = (ISubstitution) theEObject;
			T result = caseISubstitution(iSubstitution);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.TCL_MODULE: {
			TclModule tclModule = (TclModule) theEObject;
			T result = caseTclModule(tclModule);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.TCL_CODE_MODEL: {
			TclCodeModel tclCodeModel = (TclCodeModel) theEObject;
			T result = caseTclCodeModel(tclCodeModel);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.TCL_PROBLEM_MODEL: {
			TclProblemModel tclProblemModel = (TclProblemModel) theEObject;
			T result = caseTclProblemModel(tclProblemModel);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case AstPackage.TCL_PROBLEM: {
			TclProblem tclProblem = (TclProblem) theEObject;
			T result = caseTclProblem(tclProblem);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNode(Node object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Script</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Script</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScript(Script object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>String Argument</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>String Argument</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStringArgument(StringArgument object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Substitution</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Substitution</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSubstitution(Substitution object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Argument</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Argument</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclArgument(TclArgument object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Command</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Command</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclCommand(TclCommand object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Argument List</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Argument List</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclArgumentList(TclArgumentList object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Argument Match</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Argument Match</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseArgumentMatch(ArgumentMatch object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Complex String</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Complex String</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComplexString(ComplexString object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVariableReference(VariableReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ISubstitution</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ISubstitution</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseISubstitution(ISubstitution object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Module</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Module</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclModule(TclModule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Code Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Code Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclCodeModel(TclCodeModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Problem Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Problem Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclProblemModel(TclProblemModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Problem</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Problem</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclProblem(TclProblem object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //AstSwitch
