/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackagesSwitch.java,v 1.6 2009/07/10 07:49:49 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.util;

import java.util.List;

import java.util.Map;
import org.eclipse.dltk.tcl.core.packages.*;

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
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage
 * @generated
 */
public class TclPackagesSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static TclPackagesPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclPackagesSwitch() {
		if (modelPackage == null) {
			modelPackage = TclPackagesPackage.eINSTANCE;
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
		case TclPackagesPackage.TCL_PACKAGE_INFO: {
			TclPackageInfo tclPackageInfo = (TclPackageInfo) theEObject;
			T result = caseTclPackageInfo(tclPackageInfo);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.TCL_INTERPRETER_INFO: {
			TclInterpreterInfo tclInterpreterInfo = (TclInterpreterInfo) theEObject;
			T result = caseTclInterpreterInfo(tclInterpreterInfo);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.TCL_PROJECT_INFO: {
			TclProjectInfo tclProjectInfo = (TclProjectInfo) theEObject;
			T result = caseTclProjectInfo(tclProjectInfo);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.TCL_MODULE_INFO: {
			TclModuleInfo tclModuleInfo = (TclModuleInfo) theEObject;
			T result = caseTclModuleInfo(tclModuleInfo);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.TCL_SOURCE_ENTRY: {
			TclSourceEntry tclSourceEntry = (TclSourceEntry) theEObject;
			T result = caseTclSourceEntry(tclSourceEntry);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.USER_CORRECTION: {
			UserCorrection userCorrection = (UserCorrection) theEObject;
			T result = caseUserCorrection(userCorrection);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.VARIABLE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			Map.Entry<String, VariableValue> variableMapEntry = (Map.Entry<String, VariableValue>) theEObject;
			T result = caseVariableMapEntry(variableMapEntry);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.VARIABLE_VALUE: {
			VariableValue variableValue = (VariableValue) theEObject;
			T result = caseVariableValue(variableValue);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case TclPackagesPackage.VARIABLE_MAP: {
			VariableMap variableMap = (VariableMap) theEObject;
			T result = caseVariableMap(variableMap);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Package Info</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Package Info</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclPackageInfo(TclPackageInfo object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Interpreter Info</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Interpreter Info</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclInterpreterInfo(TclInterpreterInfo object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Project Info</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Project Info</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclProjectInfo(TclProjectInfo object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Module Info</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Module Info</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclModuleInfo(TclModuleInfo object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tcl Source Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tcl Source Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTclSourceEntry(TclSourceEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>User Correction</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User Correction</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUserCorrection(UserCorrection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 * @since 2.0
	 */
	public T caseVariableMapEntry(Map.Entry<String, VariableValue> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 * @since 2.0
	 */
	public T caseVariableValue(VariableValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 * @since 2.0
	 */
	public T caseVariableMap(VariableMap object) {
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

} //TclPackagesSwitch
