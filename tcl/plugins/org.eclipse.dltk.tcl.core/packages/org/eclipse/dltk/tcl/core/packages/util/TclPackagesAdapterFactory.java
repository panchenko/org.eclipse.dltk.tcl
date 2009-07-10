/**
 * <copyright>
 * </copyright>
 *
 * $Id: TclPackagesAdapterFactory.java,v 1.6 2009/07/10 07:49:49 asobolev Exp $
 */
package org.eclipse.dltk.tcl.core.packages.util;

import java.util.Map;
import org.eclipse.dltk.tcl.core.packages.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.core.packages.TclPackagesPackage
 * @generated
 */
public class TclPackagesAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static TclPackagesPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TclPackagesAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = TclPackagesPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TclPackagesSwitch<Adapter> modelSwitch = new TclPackagesSwitch<Adapter>() {
		@Override
		public Adapter caseTclPackageInfo(TclPackageInfo object) {
			return createTclPackageInfoAdapter();
		}

		@Override
		public Adapter caseTclInterpreterInfo(TclInterpreterInfo object) {
			return createTclInterpreterInfoAdapter();
		}

		@Override
		public Adapter caseTclProjectInfo(TclProjectInfo object) {
			return createTclProjectInfoAdapter();
		}

		@Override
		public Adapter caseTclModuleInfo(TclModuleInfo object) {
			return createTclModuleInfoAdapter();
		}

		@Override
		public Adapter caseTclSourceEntry(TclSourceEntry object) {
			return createTclSourceEntryAdapter();
		}

		@Override
		public Adapter caseUserCorrection(UserCorrection object) {
			return createUserCorrectionAdapter();
		}

		@Override
		public Adapter caseVariableMapEntry(
				Map.Entry<String, VariableValue> object) {
			return createVariableMapEntryAdapter();
		}

		@Override
		public Adapter caseVariableValue(VariableValue object) {
			return createVariableValueAdapter();
		}

		@Override
		public Adapter caseVariableMap(VariableMap object) {
			return createVariableMapAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.TclPackageInfo <em>Tcl Package Info</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.TclPackageInfo
	 * @generated
	 */
	public Adapter createTclPackageInfoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo <em>Tcl Interpreter Info</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo
	 * @generated
	 */
	public Adapter createTclInterpreterInfoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.TclProjectInfo <em>Tcl Project Info</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.TclProjectInfo
	 * @generated
	 */
	public Adapter createTclProjectInfoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.TclModuleInfo <em>Tcl Module Info</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.TclModuleInfo
	 * @generated
	 */
	public Adapter createTclModuleInfoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.TclSourceEntry <em>Tcl Source Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.TclSourceEntry
	 * @generated
	 */
	public Adapter createTclSourceEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.UserCorrection <em>User Correction</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.UserCorrection
	 * @generated
	 */
	public Adapter createUserCorrectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Variable Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 * @since 2.0
	 */
	public Adapter createVariableMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.VariableValue <em>Variable Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.VariableValue
	 * @generated
	 * @since 2.0
	 */
	public Adapter createVariableValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.core.packages.VariableMap <em>Variable Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.core.packages.VariableMap
	 * @generated
	 * @since 2.0
	 */
	public Adapter createVariableMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //TclPackagesAdapterFactory
