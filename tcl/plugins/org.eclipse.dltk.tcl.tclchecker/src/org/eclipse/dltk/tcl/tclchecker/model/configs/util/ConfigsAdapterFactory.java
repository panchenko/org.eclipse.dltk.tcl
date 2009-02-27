/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigsAdapterFactory.java,v 1.5 2009/02/27 15:44:40 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs.util;

import java.util.Map;

import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState;
import org.eclipse.dltk.validators.configs.ValidatorConfig;
import org.eclipse.dltk.validators.configs.ValidatorEnvironmentInstance;
import org.eclipse.dltk.validators.configs.ValidatorInstance;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage
 * @generated
 */
public class ConfigsAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ConfigsPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigsAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ConfigsPackage.eINSTANCE;
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
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConfigsSwitch<Adapter> modelSwitch =
		new ConfigsSwitch<Adapter>() {
			@Override
			public Adapter caseCheckerConfig(CheckerConfig object) {
				return createCheckerConfigAdapter();
			}
			@Override
			public Adapter caseMessageStateMap(Map.Entry<String, MessageState> object) {
				return createMessageStateMapAdapter();
			}
			@Override
			public Adapter caseCheckerEnvironmentInstance(CheckerEnvironmentInstance object) {
				return createCheckerEnvironmentInstanceAdapter();
			}
			@Override
			public Adapter caseCheckerInstance(CheckerInstance object) {
				return createCheckerInstanceAdapter();
			}
			@Override
			public Adapter caseValidatorConfig(ValidatorConfig object) {
				return createValidatorConfigAdapter();
			}
			@Override
			public Adapter caseValidatorEnvironmentInstance(ValidatorEnvironmentInstance object) {
				return createValidatorEnvironmentInstanceAdapter();
			}
			@Override
			public Adapter caseValidatorInstance(ValidatorInstance object) {
				return createValidatorInstanceAdapter();
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
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig <em>Checker Config</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig
	 * @generated
	 */
	public Adapter createCheckerConfigAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Message State Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createMessageStateMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance <em>Checker Environment Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance
	 * @generated
	 */
	public Adapter createCheckerEnvironmentInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance <em>Checker Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance
	 * @generated
	 */
	public Adapter createCheckerInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.validators.configs.ValidatorInstance <em>Validator Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.validators.configs.ValidatorInstance
	 * @generated
	 */
	public Adapter createValidatorInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.validators.configs.ValidatorEnvironmentInstance <em>Validator Environment Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.validators.configs.ValidatorEnvironmentInstance
	 * @generated
	 */
	public Adapter createValidatorEnvironmentInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.dltk.validators.configs.ValidatorConfig <em>Validator Config</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.dltk.validators.configs.ValidatorConfig
	 * @generated
	 */
	public Adapter createValidatorConfigAdapter() {
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

} //ConfigsAdapterFactory
