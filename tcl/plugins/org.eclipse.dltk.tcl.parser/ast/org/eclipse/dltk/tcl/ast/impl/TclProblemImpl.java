/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclProblemImpl.java,v 1.1 2009/05/14 16:06:34 asobolev Exp $
 */
package org.eclipse.dltk.tcl.ast.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.ast.AstPackage;
import org.eclipse.dltk.tcl.ast.TclProblem;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tcl Problem</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#getSourceStart <em>Source Start</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#getSourceEnd <em>Source End</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#isError <em>Error</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#isWarning <em>Warning</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#getFileName <em>File Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.impl.TclProblemImpl#getLineNumber <em>Line Number</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TclProblemImpl extends EObjectImpl implements TclProblem {
	/**
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<String> arguments;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final int ID_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected int id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getMessage() <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessage()
	 * @generated
	 * @ordered
	 */
	protected static final String MESSAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMessage() <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessage()
	 * @generated
	 * @ordered
	 */
	protected String message = MESSAGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSourceStart() <em>Source Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceStart()
	 * @generated
	 * @ordered
	 */
	protected static final int SOURCE_START_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSourceStart() <em>Source Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceStart()
	 * @generated
	 * @ordered
	 */
	protected int sourceStart = SOURCE_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getSourceEnd() <em>Source End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceEnd()
	 * @generated
	 * @ordered
	 */
	protected static final int SOURCE_END_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSourceEnd() <em>Source End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceEnd()
	 * @generated
	 * @ordered
	 */
	protected int sourceEnd = SOURCE_END_EDEFAULT;

	/**
	 * The default value of the '{@link #isError() <em>Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isError()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ERROR_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isError() <em>Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isError()
	 * @generated
	 * @ordered
	 */
	protected boolean error = ERROR_EDEFAULT;

	/**
	 * The default value of the '{@link #isWarning() <em>Warning</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWarning()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WARNING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWarning() <em>Warning</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWarning()
	 * @generated
	 * @ordered
	 */
	protected boolean warning = WARNING_EDEFAULT;

	/**
	 * The default value of the '{@link #getFileName() <em>File Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileName()
	 * @generated
	 * @ordered
	 */
	protected static final String FILE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFileName() <em>File Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileName()
	 * @generated
	 * @ordered
	 */
	protected String fileName = FILE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getLineNumber() <em>Line Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int LINE_NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLineNumber() <em>Line Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineNumber()
	 * @generated
	 * @ordered
	 */
	protected int lineNumber = LINE_NUMBER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TclProblemImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AstPackage.Literals.TCL_PROBLEM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getArguments() {
		if (arguments == null) {
			arguments = new EDataTypeUniqueEList<String>(String.class, this,
					AstPackage.TCL_PROBLEM__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(int newId) {
		int oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMessage(String newMessage) {
		String oldMessage = message;
		message = newMessage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__MESSAGE, oldMessage, message));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSourceStart() {
		return sourceStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceStart(int newSourceStart) {
		int oldSourceStart = sourceStart;
		sourceStart = newSourceStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__SOURCE_START, oldSourceStart,
					sourceStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSourceEnd() {
		return sourceEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceEnd(int newSourceEnd) {
		int oldSourceEnd = sourceEnd;
		sourceEnd = newSourceEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__SOURCE_END, oldSourceEnd, sourceEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setError(boolean newError) {
		boolean oldError = error;
		error = newError;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__ERROR, oldError, error));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWarning() {
		return warning;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWarning(boolean newWarning) {
		boolean oldWarning = warning;
		warning = newWarning;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__WARNING, oldWarning, warning));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFileName(String newFileName) {
		String oldFileName = fileName;
		fileName = newFileName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__FILE_NAME, oldFileName, fileName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLineNumber(int newLineNumber) {
		int oldLineNumber = lineNumber;
		lineNumber = newLineNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					AstPackage.TCL_PROBLEM__LINE_NUMBER, oldLineNumber,
					lineNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AstPackage.TCL_PROBLEM__ARGUMENTS:
			return getArguments();
		case AstPackage.TCL_PROBLEM__ID:
			return new Integer(getId());
		case AstPackage.TCL_PROBLEM__MESSAGE:
			return getMessage();
		case AstPackage.TCL_PROBLEM__SOURCE_START:
			return new Integer(getSourceStart());
		case AstPackage.TCL_PROBLEM__SOURCE_END:
			return new Integer(getSourceEnd());
		case AstPackage.TCL_PROBLEM__ERROR:
			return isError() ? Boolean.TRUE : Boolean.FALSE;
		case AstPackage.TCL_PROBLEM__WARNING:
			return isWarning() ? Boolean.TRUE : Boolean.FALSE;
		case AstPackage.TCL_PROBLEM__FILE_NAME:
			return getFileName();
		case AstPackage.TCL_PROBLEM__LINE_NUMBER:
			return new Integer(getLineNumber());
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
		case AstPackage.TCL_PROBLEM__ARGUMENTS:
			getArguments().clear();
			getArguments().addAll((Collection<? extends String>) newValue);
			return;
		case AstPackage.TCL_PROBLEM__ID:
			setId(((Integer) newValue).intValue());
			return;
		case AstPackage.TCL_PROBLEM__MESSAGE:
			setMessage((String) newValue);
			return;
		case AstPackage.TCL_PROBLEM__SOURCE_START:
			setSourceStart(((Integer) newValue).intValue());
			return;
		case AstPackage.TCL_PROBLEM__SOURCE_END:
			setSourceEnd(((Integer) newValue).intValue());
			return;
		case AstPackage.TCL_PROBLEM__ERROR:
			setError(((Boolean) newValue).booleanValue());
			return;
		case AstPackage.TCL_PROBLEM__WARNING:
			setWarning(((Boolean) newValue).booleanValue());
			return;
		case AstPackage.TCL_PROBLEM__FILE_NAME:
			setFileName((String) newValue);
			return;
		case AstPackage.TCL_PROBLEM__LINE_NUMBER:
			setLineNumber(((Integer) newValue).intValue());
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
		case AstPackage.TCL_PROBLEM__ARGUMENTS:
			getArguments().clear();
			return;
		case AstPackage.TCL_PROBLEM__ID:
			setId(ID_EDEFAULT);
			return;
		case AstPackage.TCL_PROBLEM__MESSAGE:
			setMessage(MESSAGE_EDEFAULT);
			return;
		case AstPackage.TCL_PROBLEM__SOURCE_START:
			setSourceStart(SOURCE_START_EDEFAULT);
			return;
		case AstPackage.TCL_PROBLEM__SOURCE_END:
			setSourceEnd(SOURCE_END_EDEFAULT);
			return;
		case AstPackage.TCL_PROBLEM__ERROR:
			setError(ERROR_EDEFAULT);
			return;
		case AstPackage.TCL_PROBLEM__WARNING:
			setWarning(WARNING_EDEFAULT);
			return;
		case AstPackage.TCL_PROBLEM__FILE_NAME:
			setFileName(FILE_NAME_EDEFAULT);
			return;
		case AstPackage.TCL_PROBLEM__LINE_NUMBER:
			setLineNumber(LINE_NUMBER_EDEFAULT);
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
		case AstPackage.TCL_PROBLEM__ARGUMENTS:
			return arguments != null && !arguments.isEmpty();
		case AstPackage.TCL_PROBLEM__ID:
			return id != ID_EDEFAULT;
		case AstPackage.TCL_PROBLEM__MESSAGE:
			return MESSAGE_EDEFAULT == null ? message != null
					: !MESSAGE_EDEFAULT.equals(message);
		case AstPackage.TCL_PROBLEM__SOURCE_START:
			return sourceStart != SOURCE_START_EDEFAULT;
		case AstPackage.TCL_PROBLEM__SOURCE_END:
			return sourceEnd != SOURCE_END_EDEFAULT;
		case AstPackage.TCL_PROBLEM__ERROR:
			return error != ERROR_EDEFAULT;
		case AstPackage.TCL_PROBLEM__WARNING:
			return warning != WARNING_EDEFAULT;
		case AstPackage.TCL_PROBLEM__FILE_NAME:
			return FILE_NAME_EDEFAULT == null ? fileName != null
					: !FILE_NAME_EDEFAULT.equals(fileName);
		case AstPackage.TCL_PROBLEM__LINE_NUMBER:
			return lineNumber != LINE_NUMBER_EDEFAULT;
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
		result.append(" (arguments: ");
		result.append(arguments);
		result.append(", id: ");
		result.append(id);
		result.append(", message: ");
		result.append(message);
		result.append(", sourceStart: ");
		result.append(sourceStart);
		result.append(", sourceEnd: ");
		result.append(sourceEnd);
		result.append(", error: ");
		result.append(error);
		result.append(", warning: ");
		result.append(warning);
		result.append(", fileName: ");
		result.append(fileName);
		result.append(", lineNumber: ");
		result.append(lineNumber);
		result.append(')');
		return result.toString();
	}

} //TclProblemImpl
