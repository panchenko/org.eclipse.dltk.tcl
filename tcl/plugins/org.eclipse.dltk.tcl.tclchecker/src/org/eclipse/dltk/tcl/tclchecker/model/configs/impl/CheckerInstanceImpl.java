/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerInstanceImpl.java,v 1.6 2009/02/27 15:44:40 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsFactory;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.validators.configs.ValidatorConfig;
import org.eclipse.dltk.validators.configs.ValidatorEnvironmentInstance;
import org.eclipse.dltk.validators.configs.impl.ValidatorInstanceImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.DelegatingEcoreEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Checker Instance</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getCommandLineOptions <em>Command Line Options</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getEnvironments <em>Environments</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getFavorite <em>Favorite</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getConfigs <em>Configs</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CheckerInstanceImpl extends ValidatorInstanceImpl implements
		CheckerInstance {
	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final CheckerVersion VERSION_EDEFAULT = CheckerVersion.VERSION4;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected CheckerVersion version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getCommandLineOptions() <em>Command Line Options</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getCommandLineOptions()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMAND_LINE_OPTIONS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCommandLineOptions() <em>Command Line Options</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #getCommandLineOptions()
	 * @generated
	 * @ordered
	 */
	protected String commandLineOptions = COMMAND_LINE_OPTIONS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEnvironments() <em>Environments</em>}
	 * ' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getEnvironments()
	 * @generated
	 * @ordered
	 */
	protected EList<CheckerEnvironmentInstance> environments;

	/**
	 * The cached value of the '{@link #getFavorite() <em>Favorite</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFavorite()
	 * @generated
	 * @ordered
	 */
	protected CheckerConfig favorite;

	/**
	 * The cached value of the '{@link #getConfigs() <em>Configs</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getConfigs()
	 * @generated
	 * @ordered
	 */
	protected EList<CheckerConfig> configs;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected CheckerInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigsPackage.Literals.CHECKER_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerVersion getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(CheckerVersion newVersion) {
		CheckerVersion oldVersion = version;
		version = newVersion == null ? VERSION_EDEFAULT : newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getCommandLineOptions() {
		return commandLineOptions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setCommandLineOptions(String newCommandLineOptions) {
		String oldCommandLineOptions = commandLineOptions;
		commandLineOptions = newCommandLineOptions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS, oldCommandLineOptions, commandLineOptions));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CheckerEnvironmentInstance> getEnvironments() {
		if (environments == null) {
			environments = new EObjectContainmentWithInverseEList<CheckerEnvironmentInstance>(CheckerEnvironmentInstance.class, this, ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS, ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE);
		}
		return environments;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerConfig getFavorite() {
		if (favorite != null && favorite.eIsProxy()) {
			InternalEObject oldFavorite = (InternalEObject)favorite;
			favorite = (CheckerConfig)eResolveProxy(oldFavorite);
			if (favorite != oldFavorite) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConfigsPackage.CHECKER_INSTANCE__FAVORITE, oldFavorite, favorite));
			}
		}
		return favorite;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerConfig basicGetFavorite() {
		return favorite;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setFavorite(CheckerConfig newFavorite) {
		CheckerConfig oldFavorite = favorite;
		favorite = newFavorite;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__FAVORITE, oldFavorite, favorite));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CheckerConfig> getConfigs() {
		if (configs == null) {
			configs = new EObjectContainmentEList<CheckerConfig>(CheckerConfig.class, this, ConfigsPackage.CHECKER_INSTANCE__CONFIGS);
		}
		return configs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public CheckerEnvironmentInstance getEnvironment(String environmentId) {
		final EList<CheckerEnvironmentInstance> envs = getEnvironments();
		for (CheckerEnvironmentInstance environmentInstance : envs) {
			if (environmentId.equals(environmentInstance.getEnvironmentId())) {
				return environmentInstance;
			}
		}
		CheckerEnvironmentInstance environmentInstance = ConfigsFactory.eINSTANCE
				.createCheckerEnvironmentInstance();
		environmentInstance.setEnvironmentId(environmentId);
		environmentInstance.setAutomatic(isAutomatic());
		envs.add(environmentInstance);
		return environmentInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public CheckerEnvironmentInstance findEnvironment(String environmentId) {
		final EList<CheckerEnvironmentInstance> envs = getEnvironments();
		for (CheckerEnvironmentInstance environmentInstance : envs) {
			if (environmentId.equals(environmentInstance.getEnvironmentId())) {
				return environmentInstance;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEnvironments()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS:
				return ((InternalEList<?>)getEnvironments()).basicRemove(otherEnd, msgs);
			case ConfigsPackage.CHECKER_INSTANCE__CONFIGS:
				return ((InternalEList<?>)getConfigs()).basicRemove(otherEnd, msgs);
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
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				return getVersion();
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				return getCommandLineOptions();
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS:
				return getEnvironments();
			case ConfigsPackage.CHECKER_INSTANCE__FAVORITE:
				if (resolve) return getFavorite();
				return basicGetFavorite();
			case ConfigsPackage.CHECKER_INSTANCE__CONFIGS:
				return getConfigs();
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
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				setVersion((CheckerVersion)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				setCommandLineOptions((String)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS:
				getEnvironments().clear();
				getEnvironments().addAll((Collection<? extends CheckerEnvironmentInstance>)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__FAVORITE:
				setFavorite((CheckerConfig)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__CONFIGS:
				getConfigs().clear();
				getConfigs().addAll((Collection<? extends CheckerConfig>)newValue);
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
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				setCommandLineOptions(COMMAND_LINE_OPTIONS_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS:
				getEnvironments().clear();
				return;
			case ConfigsPackage.CHECKER_INSTANCE__FAVORITE:
				setFavorite((CheckerConfig)null);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__CONFIGS:
				getConfigs().clear();
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
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				return version != VERSION_EDEFAULT;
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				return COMMAND_LINE_OPTIONS_EDEFAULT == null ? commandLineOptions != null : !COMMAND_LINE_OPTIONS_EDEFAULT.equals(commandLineOptions);
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS:
				return environments != null && !environments.isEmpty();
			case ConfigsPackage.CHECKER_INSTANCE__FAVORITE:
				return favorite != null;
			case ConfigsPackage.CHECKER_INSTANCE__CONFIGS:
				return configs != null && !configs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (version: "); //$NON-NLS-1$
		result.append(version);
		result.append(", commandLineOptions: "); //$NON-NLS-1$
		result.append(commandLineOptions);
		result.append(')');
		return result.toString();
	}

	@SuppressWarnings("serial")
	@Override
	public EList<ValidatorConfig> getValidatorConfigs() {
		return new DelegatingEcoreEList.Dynamic<ValidatorConfig>(
				eContainerFeatureID, this,
				ConfigsPackage.Literals.CHECKER_INSTANCE__CONFIGS) {

			@SuppressWarnings("unchecked")
			@Override
			protected List<ValidatorConfig> delegateList() {
				return (List<ValidatorConfig>) (List<?>) getConfigs();
			}

		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public EList<ValidatorEnvironmentInstance> getValidatorEnvironments() {
		return (EList<ValidatorEnvironmentInstance>) (List<?>) getEnvironments();
	}

	@Override
	public ValidatorConfig getValidatorFavoriteConfig() {
		return getFavorite();
	}

	@Override
	public void setValidatorFavoriteConfig(
			ValidatorConfig newValidatorFavoriteConfig) {
		setFavorite((CheckerConfig) newValidatorFavoriteConfig);
	}

} // CheckerInstanceImpl
