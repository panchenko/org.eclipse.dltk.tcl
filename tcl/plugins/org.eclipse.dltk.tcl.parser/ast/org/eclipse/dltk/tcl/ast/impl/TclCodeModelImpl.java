/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclCodeModelImpl.java,v 1.2 2009/10/18 15:25:41 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ast.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.ast.AstPackage;
import org.eclipse.dltk.tcl.ast.TclCodeModel;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tcl Code Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCodeModelImpl#getDelimeters <em>Delimeters</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclCodeModelImpl#getLineOffsets <em>Line Offsets</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TclCodeModelImpl extends EObjectImpl implements TclCodeModel {
	/**
	 * The cached value of the '{@link #getDelimeters() <em>Delimeters</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDelimeters()
	 * @generated
	 * @ordered
	 */
	protected EList<String> delimeters;

	/**
	 * The cached value of the '{@link #getLineOffsets() <em>Line Offsets</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineOffsets()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> lineOffsets;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TclCodeModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AstPackage.Literals.TCL_CODE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getDelimeters() {
		if (delimeters == null) {
			delimeters = new EDataTypeEList<String>(String.class, this,
					AstPackage.TCL_CODE_MODEL__DELIMETERS);
		}
		return delimeters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getLineOffsets() {
		if (lineOffsets == null) {
			lineOffsets = new EDataTypeEList<Integer>(Integer.class, this,
					AstPackage.TCL_CODE_MODEL__LINE_OFFSETS);
		}
		return lineOffsets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AstPackage.TCL_CODE_MODEL__DELIMETERS:
			return getDelimeters();
		case AstPackage.TCL_CODE_MODEL__LINE_OFFSETS:
			return getLineOffsets();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AstPackage.TCL_CODE_MODEL__DELIMETERS:
			getDelimeters().clear();
			getDelimeters().addAll((Collection<? extends String>) newValue);
			return;
		case AstPackage.TCL_CODE_MODEL__LINE_OFFSETS:
			getLineOffsets().clear();
			getLineOffsets().addAll((Collection<? extends Integer>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case AstPackage.TCL_CODE_MODEL__DELIMETERS:
			getDelimeters().clear();
			return;
		case AstPackage.TCL_CODE_MODEL__LINE_OFFSETS:
			getLineOffsets().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case AstPackage.TCL_CODE_MODEL__DELIMETERS:
			return delimeters != null && !delimeters.isEmpty();
		case AstPackage.TCL_CODE_MODEL__LINE_OFFSETS:
			return lineOffsets != null && !lineOffsets.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (delimeters: "); //$NON-NLS-1$
		result.append(delimeters);
		result.append(", lineOffsets: "); //$NON-NLS-1$
		result.append(lineOffsets);
		result.append(')');
		return result.toString();
	}

} //TclCodeModelImpl
