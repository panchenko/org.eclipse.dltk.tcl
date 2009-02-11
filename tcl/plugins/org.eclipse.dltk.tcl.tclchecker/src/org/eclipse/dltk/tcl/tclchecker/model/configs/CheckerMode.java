/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerMode.java,v 1.2 2009/02/11 10:32:20 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Checker Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage#getCheckerMode()
 * @model
 * @generated
 */
public enum CheckerMode implements Enumerator {
	/**
	 * The '<em><b>DEFAULT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DEFAULT_VALUE
	 * @generated
	 * @ordered
	 */
	DEFAULT(-1, "DEFAULT", "DEFAULT"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>W0</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #W0_VALUE
	 * @generated
	 * @ordered
	 */
	W0(0, "W0", "W0"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>W1</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #W1_VALUE
	 * @generated
	 * @ordered
	 */
	W1(1, "W1", "W1"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>W2</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #W2_VALUE
	 * @generated
	 * @ordered
	 */
	W2(2, "W2", "W2"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>W3</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #W3_VALUE
	 * @generated
	 * @ordered
	 */
	W3(3, "W3", "W3"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>W4</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #W4_VALUE
	 * @generated
	 * @ordered
	 */
	W4(4, "W4", "W4"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>DEFAULT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DEFAULT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DEFAULT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DEFAULT_VALUE = -1;

	/**
	 * The '<em><b>W0</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>W0</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #W0
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int W0_VALUE = 0;

	/**
	 * The '<em><b>W1</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>W1</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #W1
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int W1_VALUE = 1;

	/**
	 * The '<em><b>W2</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>W2</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #W2
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int W2_VALUE = 2;

	/**
	 * The '<em><b>W3</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>W3</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #W3
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int W3_VALUE = 3;

	/**
	 * The '<em><b>W4</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>W4</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #W4
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int W4_VALUE = 4;

	/**
	 * An array of all the '<em><b>Checker Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final CheckerMode[] VALUES_ARRAY =
		new CheckerMode[] {
			DEFAULT,
			W0,
			W1,
			W2,
			W3,
			W4,
		};

	/**
	 * A public read-only list of all the '<em><b>Checker Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<CheckerMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Checker Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CheckerMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CheckerMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Checker Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CheckerMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CheckerMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Checker Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CheckerMode get(int value) {
		switch (value) {
			case DEFAULT_VALUE: return DEFAULT;
			case W0_VALUE: return W0;
			case W1_VALUE: return W1;
			case W2_VALUE: return W2;
			case W3_VALUE: return W3;
			case W4_VALUE: return W4;
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
	private CheckerMode(int value, String name, String literal) {
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
	
} //CheckerMode
