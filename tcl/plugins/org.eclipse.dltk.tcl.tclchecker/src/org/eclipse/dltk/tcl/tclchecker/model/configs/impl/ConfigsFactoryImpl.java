/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigsFactoryImpl.java,v 1.3 2009/02/19 10:41:53 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs.impl;

import java.util.Map;

import org.eclipse.dltk.tcl.tclchecker.model.configs.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigsFactoryImpl extends EFactoryImpl implements ConfigsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ConfigsFactory init() {
		try {
			ConfigsFactory theConfigsFactory = (ConfigsFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/dltk/tcl/tclchecker/configs"); //$NON-NLS-1$ 
			if (theConfigsFactory != null) {
				return theConfigsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ConfigsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ConfigsPackage.CHECKER_CONFIG: return createCheckerConfig();
			case ConfigsPackage.MESSAGE_STATE_MAP: return (EObject)createMessageStateMap();
			case ConfigsPackage.CHECKER_INSTANCE: return createCheckerInstance();
			case ConfigsPackage.CHECKER_FAVORITE: return createCheckerFavorite();
			case ConfigsPackage.ENVIRONMENT_INSTANCE_MAP: return (EObject)createEnvironmentInstanceMap();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ConfigsPackage.CHECKER_MODE:
				return createCheckerModeFromString(eDataType, initialValue);
			case ConfigsPackage.MESSAGE_STATE:
				return createMessageStateFromString(eDataType, initialValue);
			case ConfigsPackage.CHECKER_VERSION:
				return createCheckerVersionFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ConfigsPackage.CHECKER_MODE:
				return convertCheckerModeToString(eDataType, instanceValue);
			case ConfigsPackage.MESSAGE_STATE:
				return convertMessageStateToString(eDataType, instanceValue);
			case ConfigsPackage.CHECKER_VERSION:
				return convertCheckerVersionToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerConfig createCheckerConfig() {
		CheckerConfigImpl checkerConfig = new CheckerConfigImpl();
		return checkerConfig;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, MessageState> createMessageStateMap() {
		MessageStateMapImpl messageStateMap = new MessageStateMapImpl();
		return messageStateMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerInstance createCheckerInstance() {
		CheckerInstanceImpl checkerInstance = new CheckerInstanceImpl();
		return checkerInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerFavorite createCheckerFavorite() {
		CheckerFavoriteImpl checkerFavorite = new CheckerFavoriteImpl();
		return checkerFavorite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, CheckerInstance> createEnvironmentInstanceMap() {
		EnvironmentInstanceMapImpl environmentInstanceMap = new EnvironmentInstanceMapImpl();
		return environmentInstanceMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerMode createCheckerModeFromString(EDataType eDataType, String initialValue) {
		CheckerMode result = CheckerMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCheckerModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageState createMessageStateFromString(EDataType eDataType, String initialValue) {
		MessageState result = MessageState.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageStateToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerVersion createCheckerVersionFromString(EDataType eDataType, String initialValue) {
		CheckerVersion result = CheckerVersion.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCheckerVersionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigsPackage getConfigsPackage() {
		return (ConfigsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ConfigsPackage getPackage() {
		return ConfigsPackage.eINSTANCE;
	}

} //ConfigsFactoryImpl
