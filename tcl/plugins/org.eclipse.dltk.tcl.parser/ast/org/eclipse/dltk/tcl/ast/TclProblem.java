/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclProblem.java,v 1.1 2009/05/14 16:06:33 asobolev Exp $
 */
package org.eclipse.dltk.tcl.ast;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tcl Problem</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#getArguments <em>Arguments</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#getMessage <em>Message</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#getSourceStart <em>Source Start</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#getSourceEnd <em>Source End</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#isError <em>Error</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#isWarning <em>Warning</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#getFileName <em>File Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ast.TclProblem#getLineNumber <em>Line Number</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem()
 * @model
 * @generated
 */
public interface TclProblem extends EObject {
	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' attribute list.
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_Arguments()
	 * @model
	 * @generated
	 */
	EList<String> getArguments();

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_Id()
	 * @model
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Message</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Message</em>' attribute.
	 * @see #setMessage(String)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_Message()
	 * @model
	 * @generated
	 */
	String getMessage();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#getMessage <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Message</em>' attribute.
	 * @see #getMessage()
	 * @generated
	 */
	void setMessage(String value);

	/**
	 * Returns the value of the '<em><b>Source Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Start</em>' attribute.
	 * @see #setSourceStart(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_SourceStart()
	 * @model
	 * @generated
	 */
	int getSourceStart();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#getSourceStart <em>Source Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Start</em>' attribute.
	 * @see #getSourceStart()
	 * @generated
	 */
	void setSourceStart(int value);

	/**
	 * Returns the value of the '<em><b>Source End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source End</em>' attribute.
	 * @see #setSourceEnd(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_SourceEnd()
	 * @model
	 * @generated
	 */
	int getSourceEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#getSourceEnd <em>Source End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source End</em>' attribute.
	 * @see #getSourceEnd()
	 * @generated
	 */
	void setSourceEnd(int value);

	/**
	 * Returns the value of the '<em><b>Error</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Error</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Error</em>' attribute.
	 * @see #setError(boolean)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_Error()
	 * @model
	 * @generated
	 */
	boolean isError();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#isError <em>Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Error</em>' attribute.
	 * @see #isError()
	 * @generated
	 */
	void setError(boolean value);

	/**
	 * Returns the value of the '<em><b>Warning</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Warning</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Warning</em>' attribute.
	 * @see #setWarning(boolean)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_Warning()
	 * @model
	 * @generated
	 */
	boolean isWarning();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#isWarning <em>Warning</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Warning</em>' attribute.
	 * @see #isWarning()
	 * @generated
	 */
	void setWarning(boolean value);

	/**
	 * Returns the value of the '<em><b>File Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>File Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>File Name</em>' attribute.
	 * @see #setFileName(String)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_FileName()
	 * @model
	 * @generated
	 */
	String getFileName();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#getFileName <em>File Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>File Name</em>' attribute.
	 * @see #getFileName()
	 * @generated
	 */
	void setFileName(String value);

	/**
	 * Returns the value of the '<em><b>Line Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Line Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Number</em>' attribute.
	 * @see #setLineNumber(int)
	 * @see org.eclipse.dltk.tcl.ast.AstPackage#getTclProblem_LineNumber()
	 * @model
	 * @generated
	 */
	int getLineNumber();

	/**
	 * Sets the value of the '{@link org.eclipse.dltk.tcl.ast.TclProblem#getLineNumber <em>Line Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Number</em>' attribute.
	 * @see #getLineNumber()
	 * @generated
	 */
	void setLineNumber(int value);

} // TclProblem
