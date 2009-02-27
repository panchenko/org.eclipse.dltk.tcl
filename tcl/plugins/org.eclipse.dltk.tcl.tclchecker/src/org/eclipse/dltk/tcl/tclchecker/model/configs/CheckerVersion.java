/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerVersion.java,v 1.3 2009/02/27 09:16:01 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Checker Version</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerVersion()
 * @model
 * @generated
 */
public enum CheckerVersion implements Enumerator {
	/**
	 * The '<em><b>VERSION4</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VERSION4_VALUE
	 * @generated
	 * @ordered
	 */
	VERSION4(400, "VERSION4", "VERSION4"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>VERSION5</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VERSION5_VALUE
	 * @generated
	 * @ordered
	 */
	VERSION5(500, "VERSION5", "VERSION5"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>VERSION4</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VERSION4</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VERSION4
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VERSION4_VALUE = 400;

	/**
	 * The '<em><b>VERSION5</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VERSION5</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VERSION5
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VERSION5_VALUE = 500;

	/**
	 * An array of all the '<em><b>Checker Version</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final CheckerVersion[] VALUES_ARRAY =
		new CheckerVersion[] {
			VERSION4,
			VERSION5,
		};

	/**
	 * A public read-only list of all the '<em><b>Checker Version</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<CheckerVersion> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Checker Version</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CheckerVersion get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CheckerVersion result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Checker Version</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CheckerVersion getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CheckerVersion result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Checker Version</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CheckerVersion get(int value) {
		switch (value) {
			case VERSION4_VALUE: return VERSION4;
			case VERSION5_VALUE: return VERSION5;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private CheckerVersion(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		if (value == VERSION4_VALUE) {
			return org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences.Messages.TclChecker_Version4;
		} else if (value == VERSION5_VALUE) {
			return org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences.Messages.TclChecker_Version5;
		} else {
			return toString();
		}
	}
	
} //CheckerVersion
