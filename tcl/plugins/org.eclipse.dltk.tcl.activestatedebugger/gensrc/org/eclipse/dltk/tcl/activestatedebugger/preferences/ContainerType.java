/**
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 * 
 *
 * $Id: ContainerType.java,v 1.2 2009/10/26 13:41:25 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Container Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferencesPackage#getContainerType()
 * @model
 * @generated
 * @since 2.0
 */
public enum ContainerType implements Enumerator {
	/**
	 * The '<em><b>LIBRARIES</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LIBRARIES_VALUE
	 * @generated
	 * @ordered
	 */
	LIBRARIES(0, "LIBRARIES", "LIBRARIES"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>PACKAGES</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PACKAGES_VALUE
	 * @generated
	 * @ordered
	 */
	PACKAGES(1, "PACKAGES", "PACKAGES"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>SOURCES</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOURCES_VALUE
	 * @generated
	 * @ordered
	 */
	SOURCES(2, "SOURCES", "SOURCES"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>LIBRARIES</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LIBRARIES</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LIBRARIES
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LIBRARIES_VALUE = 0;

	/**
	 * The '<em><b>PACKAGES</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PACKAGES</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PACKAGES
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PACKAGES_VALUE = 1;

	/**
	 * The '<em><b>SOURCES</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SOURCES</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOURCES
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SOURCES_VALUE = 2;

	/**
	 * An array of all the '<em><b>Container Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ContainerType[] VALUES_ARRAY =
		new ContainerType[] {
			LIBRARIES,
			PACKAGES,
			SOURCES,
		};

	/**
	 * A public read-only list of all the '<em><b>Container Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ContainerType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Container Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ContainerType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ContainerType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Container Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ContainerType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ContainerType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Container Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ContainerType get(int value) {
		switch (value) {
			case LIBRARIES_VALUE: return LIBRARIES;
			case PACKAGES_VALUE: return PACKAGES;
			case SOURCES_VALUE: return SOURCES;
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
	private ContainerType(int value, String name, String literal) {
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
	
} //ContainerType
