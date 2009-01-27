/**
 * <copyright>
 * </copyright>
 *
 * $Id: MessageCategory.java,v 1.1 2009/01/27 18:43:47 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.messages;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Message Category</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesPackage#getMessageCategory()
 * @model
 * @generated
 */
public enum MessageCategory implements Enumerator {
	/**
	 * The '<em><b>ERROR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ERROR_VALUE
	 * @generated
	 * @ordered
	 */
	ERROR(0, "ERROR", "ERROR"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>WARNING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WARNING_VALUE
	 * @generated
	 * @ordered
	 */
	WARNING(1, "WARNING", "WARNING"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>UPGRADE ERROR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UPGRADE_ERROR_VALUE
	 * @generated
	 * @ordered
	 */
	UPGRADE_ERROR(2, "UPGRADE_ERROR", "UPGRADE_ERROR"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>UPGRADE WARNING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UPGRADE_WARNING_VALUE
	 * @generated
	 * @ordered
	 */
	UPGRADE_WARNING(3, "UPGRADE_WARNING", "UPGRADE_WARNING"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>NON PORTABLE WARNING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NON_PORTABLE_WARNING_VALUE
	 * @generated
	 * @ordered
	 */
	NON_PORTABLE_WARNING(4, "NON_PORTABLE_WARNING", "NON_PORTABLE_WARNING"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>PERFORMANCE WARNING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PERFORMANCE_WARNING_VALUE
	 * @generated
	 * @ordered
	 */
	PERFORMANCE_WARNING(5, "PERFORMANCE_WARNING", "PERFORMANCE_WARNING"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>USAGE WARNING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #USAGE_WARNING_VALUE
	 * @generated
	 * @ordered
	 */
	USAGE_WARNING(6, "USAGE_WARNING", "USAGE_WARNING"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>ERROR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ERROR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ERROR
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ERROR_VALUE = 0;

	/**
	 * The '<em><b>WARNING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WARNING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WARNING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int WARNING_VALUE = 1;

	/**
	 * The '<em><b>UPGRADE ERROR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>UPGRADE ERROR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #UPGRADE_ERROR
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int UPGRADE_ERROR_VALUE = 2;

	/**
	 * The '<em><b>UPGRADE WARNING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>UPGRADE WARNING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #UPGRADE_WARNING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int UPGRADE_WARNING_VALUE = 3;

	/**
	 * The '<em><b>NON PORTABLE WARNING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NON PORTABLE WARNING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NON_PORTABLE_WARNING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NON_PORTABLE_WARNING_VALUE = 4;

	/**
	 * The '<em><b>PERFORMANCE WARNING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PERFORMANCE WARNING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PERFORMANCE_WARNING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PERFORMANCE_WARNING_VALUE = 5;

	/**
	 * The '<em><b>USAGE WARNING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>USAGE WARNING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #USAGE_WARNING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int USAGE_WARNING_VALUE = 6;

	/**
	 * An array of all the '<em><b>Message Category</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final MessageCategory[] VALUES_ARRAY =
		new MessageCategory[] {
			ERROR,
			WARNING,
			UPGRADE_ERROR,
			UPGRADE_WARNING,
			NON_PORTABLE_WARNING,
			PERFORMANCE_WARNING,
			USAGE_WARNING,
		};

	/**
	 * A public read-only list of all the '<em><b>Message Category</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<MessageCategory> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Message Category</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MessageCategory get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MessageCategory result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Message Category</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MessageCategory getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MessageCategory result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Message Category</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MessageCategory get(int value) {
		switch (value) {
			case ERROR_VALUE: return ERROR;
			case WARNING_VALUE: return WARNING;
			case UPGRADE_ERROR_VALUE: return UPGRADE_ERROR;
			case UPGRADE_WARNING_VALUE: return UPGRADE_WARNING;
			case NON_PORTABLE_WARNING_VALUE: return NON_PORTABLE_WARNING;
			case PERFORMANCE_WARNING_VALUE: return PERFORMANCE_WARNING;
			case USAGE_WARNING_VALUE: return USAGE_WARNING;
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
	private MessageCategory(int value, String name, String literal) {
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

	public boolean isError() {
		return this == ERROR || this == UPGRADE_ERROR;
	}

	public boolean isWarning() {
		return this == WARNING || this == NON_PORTABLE_WARNING
				|| this == PERFORMANCE_WARNING || this == UPGRADE_WARNING
				|| this == USAGE_WARNING;
	}

} //MessageCategory
