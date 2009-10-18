/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclModuleImpl.java,v 1.3 2009/10/18 15:25:41 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ast.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.ast.AstPackage;
import org.eclipse.dltk.tcl.ast.TclCodeModel;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Tcl Module</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclModuleImpl#getStatements <em>Statements</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclModuleImpl#getSize <em>Size</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclModuleImpl#getCodeModel <em>Code Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TclModuleImpl extends EObjectImpl implements TclModule {
	/**
	 * The cached value of the '{@link #getStatements() <em>Statements</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getStatements()
	 * @generated
	 * @ordered
	 */
	protected EList<TclCommand> statements;

	/**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected static final int SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected int size = SIZE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCodeModel() <em>Code Model</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getCodeModel()
	 * @generated
	 * @ordered
	 */
	protected TclCodeModel codeModel;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected TclModuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AstPackage.Literals.TCL_MODULE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TclCommand> getStatements() {
		if (statements == null) {
			statements = new EObjectContainmentEList<TclCommand>(
					TclCommand.class, this, AstPackage.TCL_MODULE__STATEMENTS);
		}
		return statements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setSize(int newSize) {
		int oldSize = size;
		size = newSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_MODULE__SIZE, oldSize, size));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TclCodeModel getCodeModel() {
		return codeModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCodeModel(TclCodeModel newCodeModel,
			NotificationChain msgs) {
		TclCodeModel oldCodeModel = codeModel;
		codeModel = newCodeModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this,
					Notification.SET, AstPackage.TCL_MODULE__CODE_MODEL,
					oldCodeModel, newCodeModel);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setCodeModel(TclCodeModel newCodeModel) {
		if (newCodeModel != codeModel) {
			NotificationChain msgs = null;
			if (codeModel != null)
				msgs = ((InternalEObject) codeModel)
						.eInverseRemove(this, EOPPOSITE_FEATURE_BASE
								- AstPackage.TCL_MODULE__CODE_MODEL, null, msgs);
			if (newCodeModel != null)
				msgs = ((InternalEObject) newCodeModel)
						.eInverseAdd(this, EOPPOSITE_FEATURE_BASE
								- AstPackage.TCL_MODULE__CODE_MODEL, null, msgs);
			msgs = basicSetCodeModel(newCodeModel, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_MODULE__CODE_MODEL, newCodeModel,
					newCodeModel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AstPackage.TCL_MODULE__STATEMENTS:
			return ((InternalEList<?>) getStatements()).basicRemove(otherEnd,
					msgs);
		case AstPackage.TCL_MODULE__CODE_MODEL:
			return basicSetCodeModel(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AstPackage.TCL_MODULE__STATEMENTS:
			return getStatements();
		case AstPackage.TCL_MODULE__SIZE:
			return getSize();
		case AstPackage.TCL_MODULE__CODE_MODEL:
			return getCodeModel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AstPackage.TCL_MODULE__STATEMENTS:
			getStatements().clear();
			getStatements().addAll((Collection<? extends TclCommand>) newValue);
			return;
		case AstPackage.TCL_MODULE__SIZE:
			setSize((Integer) newValue);
			return;
		case AstPackage.TCL_MODULE__CODE_MODEL:
			setCodeModel((TclCodeModel) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case AstPackage.TCL_MODULE__STATEMENTS:
			getStatements().clear();
			return;
		case AstPackage.TCL_MODULE__SIZE:
			setSize(SIZE_EDEFAULT);
			return;
		case AstPackage.TCL_MODULE__CODE_MODEL:
			setCodeModel((TclCodeModel) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case AstPackage.TCL_MODULE__STATEMENTS:
			return statements != null && !statements.isEmpty();
		case AstPackage.TCL_MODULE__SIZE:
			return size != SIZE_EDEFAULT;
		case AstPackage.TCL_MODULE__CODE_MODEL:
			return codeModel != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (size: ");
		result.append(size);
		result.append(')');
		result.append(SimpleCodePrinter.getCommandsString(getStatements()));
		return result.toString();
	}

} // TclModuleImpl
