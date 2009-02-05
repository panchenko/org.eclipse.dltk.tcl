/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigInstanceImpl.java,v 1.1 2009/02/05 18:41:37 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs.impl;

import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Config Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigInstanceImpl#isSummary <em>Summary</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigInstanceImpl#getCommandLineOptions <em>Command Line Options</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigInstanceImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigInstanceImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.ConfigInstanceImpl#getMessageStates <em>Message States</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConfigInstanceImpl extends EObjectImpl implements ConfigInstance {
	/**
	 * The default value of the '{@link #isSummary() <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSummary()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SUMMARY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSummary() <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSummary()
	 * @generated
	 * @ordered
	 */
	protected boolean summary = SUMMARY_EDEFAULT;

	/**
	 * The default value of the '{@link #getCommandLineOptions() <em>Command Line Options</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommandLineOptions()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMAND_LINE_OPTIONS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCommandLineOptions() <em>Command Line Options</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommandLineOptions()
	 * @generated
	 * @ordered
	 */
	protected String commandLineOptions = COMMAND_LINE_OPTIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final CheckerMode MODE_EDEFAULT = CheckerMode.W0;

	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected CheckerMode mode = MODE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMessageStates() <em>Message States</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessageStates()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, MessageState> messageStates;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConfigInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigsPackage.Literals.CONFIG_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSummary() {
		return summary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSummary(boolean newSummary) {
		boolean oldSummary = summary;
		summary = newSummary;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CONFIG_INSTANCE__SUMMARY, oldSummary, summary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCommandLineOptions() {
		return commandLineOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCommandLineOptions(String newCommandLineOptions) {
		String oldCommandLineOptions = commandLineOptions;
		commandLineOptions = newCommandLineOptions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CONFIG_INSTANCE__COMMAND_LINE_OPTIONS, oldCommandLineOptions, commandLineOptions));
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
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CONFIG_INSTANCE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerMode getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMode(CheckerMode newMode) {
		CheckerMode oldMode = mode;
		mode = newMode == null ? MODE_EDEFAULT : newMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CONFIG_INSTANCE__MODE, oldMode, mode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, MessageState> getMessageStates() {
		if (messageStates == null) {
			messageStates = new EcoreEMap<String,MessageState>(ConfigsPackage.Literals.MESSAGE_STATE_MAP, MessageStateMapImpl.class, this, ConfigsPackage.CONFIG_INSTANCE__MESSAGE_STATES);
		}
		return messageStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigsPackage.CONFIG_INSTANCE__MESSAGE_STATES:
				return ((InternalEList<?>)getMessageStates()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigsPackage.CONFIG_INSTANCE__SUMMARY:
				return isSummary() ? Boolean.TRUE : Boolean.FALSE;
			case ConfigsPackage.CONFIG_INSTANCE__COMMAND_LINE_OPTIONS:
				return getCommandLineOptions();
			case ConfigsPackage.CONFIG_INSTANCE__NAME:
				return getName();
			case ConfigsPackage.CONFIG_INSTANCE__MODE:
				return getMode();
			case ConfigsPackage.CONFIG_INSTANCE__MESSAGE_STATES:
				if (coreType) return getMessageStates();
				else return getMessageStates().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConfigsPackage.CONFIG_INSTANCE__SUMMARY:
				setSummary(((Boolean)newValue).booleanValue());
				return;
			case ConfigsPackage.CONFIG_INSTANCE__COMMAND_LINE_OPTIONS:
				setCommandLineOptions((String)newValue);
				return;
			case ConfigsPackage.CONFIG_INSTANCE__NAME:
				setName((String)newValue);
				return;
			case ConfigsPackage.CONFIG_INSTANCE__MODE:
				setMode((CheckerMode)newValue);
				return;
			case ConfigsPackage.CONFIG_INSTANCE__MESSAGE_STATES:
				((EStructuralFeature.Setting)getMessageStates()).set(newValue);
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
			case ConfigsPackage.CONFIG_INSTANCE__SUMMARY:
				setSummary(SUMMARY_EDEFAULT);
				return;
			case ConfigsPackage.CONFIG_INSTANCE__COMMAND_LINE_OPTIONS:
				setCommandLineOptions(COMMAND_LINE_OPTIONS_EDEFAULT);
				return;
			case ConfigsPackage.CONFIG_INSTANCE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ConfigsPackage.CONFIG_INSTANCE__MODE:
				setMode(MODE_EDEFAULT);
				return;
			case ConfigsPackage.CONFIG_INSTANCE__MESSAGE_STATES:
				getMessageStates().clear();
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
			case ConfigsPackage.CONFIG_INSTANCE__SUMMARY:
				return summary != SUMMARY_EDEFAULT;
			case ConfigsPackage.CONFIG_INSTANCE__COMMAND_LINE_OPTIONS:
				return COMMAND_LINE_OPTIONS_EDEFAULT == null ? commandLineOptions != null : !COMMAND_LINE_OPTIONS_EDEFAULT.equals(commandLineOptions);
			case ConfigsPackage.CONFIG_INSTANCE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ConfigsPackage.CONFIG_INSTANCE__MODE:
				return mode != MODE_EDEFAULT;
			case ConfigsPackage.CONFIG_INSTANCE__MESSAGE_STATES:
				return messageStates != null && !messageStates.isEmpty();
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
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (summary: "); //$NON-NLS-1$
		result.append(summary);
		result.append(", commandLineOptions: "); //$NON-NLS-1$
		result.append(commandLineOptions);
		result.append(", name: "); //$NON-NLS-1$
		result.append(name);
		result.append(", mode: "); //$NON-NLS-1$
		result.append(mode);
		result.append(')');
		return result.toString();
	}

} //ConfigInstanceImpl
