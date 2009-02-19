/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerInstanceImpl.java,v 1.4 2009/02/19 10:41:53 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Checker Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getEnvironmentId <em>Environment Id</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getExecutablePath <em>Executable Path</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getPcxFileFolders <em>Pcx File Folders</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#isUsePcxFiles <em>Use Pcx Files</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#getCommandLineOptions <em>Command Line Options</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerInstanceImpl#isAutomatic <em>Automatic</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CheckerInstanceImpl extends EObjectImpl implements CheckerInstance {
	/**
	 * The default value of the '{@link #getEnvironmentId() <em>Environment Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnvironmentId()
	 * @generated
	 * @ordered
	 */
	protected static final String ENVIRONMENT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnvironmentId() <em>Environment Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnvironmentId()
	 * @generated
	 * @ordered
	 */
	protected String environmentId = ENVIRONMENT_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getExecutablePath() <em>Executable Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutablePath()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTABLE_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecutablePath() <em>Executable Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutablePath()
	 * @generated
	 * @ordered
	 */
	protected String executablePath = EXECUTABLE_PATH_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPcxFileFolders() <em>Pcx File Folders</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPcxFileFolders()
	 * @generated
	 * @ordered
	 */
	protected EList<String> pcxFileFolders;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final CheckerVersion VERSION_EDEFAULT = CheckerVersion.VERSION4;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected CheckerVersion version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #isUsePcxFiles() <em>Use Pcx Files</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsePcxFiles()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_PCX_FILES_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isUsePcxFiles() <em>Use Pcx Files</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsePcxFiles()
	 * @generated
	 * @ordered
	 */
	protected boolean usePcxFiles = USE_PCX_FILES_EDEFAULT;

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
	 * The default value of the '{@link #isAutomatic() <em>Automatic</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutomatic()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTOMATIC_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAutomatic() <em>Automatic</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutomatic()
	 * @generated
	 * @ordered
	 */
	protected boolean automatic = AUTOMATIC_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CheckerInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigsPackage.Literals.CHECKER_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEnvironmentId() {
		return environmentId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnvironmentId(String newEnvironmentId) {
		String oldEnvironmentId = environmentId;
		environmentId = newEnvironmentId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENT_ID, oldEnvironmentId, environmentId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecutablePath() {
		return executablePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecutablePath(String newExecutablePath) {
		String oldExecutablePath = executablePath;
		executablePath = newExecutablePath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__EXECUTABLE_PATH, oldExecutablePath, executablePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getPcxFileFolders() {
		if (pcxFileFolders == null) {
			pcxFileFolders = new EDataTypeUniqueEList<String>(String.class, this, ConfigsPackage.CHECKER_INSTANCE__PCX_FILE_FOLDERS);
		}
		return pcxFileFolders;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerVersion getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(CheckerVersion newVersion) {
		CheckerVersion oldVersion = version;
		version = newVersion == null ? VERSION_EDEFAULT : newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUsePcxFiles() {
		return usePcxFiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUsePcxFiles(boolean newUsePcxFiles) {
		boolean oldUsePcxFiles = usePcxFiles;
		usePcxFiles = newUsePcxFiles;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__USE_PCX_FILES, oldUsePcxFiles, usePcxFiles));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS, oldCommandLineOptions, commandLineOptions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAutomatic() {
		return automatic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAutomatic(boolean newAutomatic) {
		boolean oldAutomatic = automatic;
		automatic = newAutomatic;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_INSTANCE__AUTOMATIC, oldAutomatic, automatic));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENT_ID:
				return getEnvironmentId();
			case ConfigsPackage.CHECKER_INSTANCE__EXECUTABLE_PATH:
				return getExecutablePath();
			case ConfigsPackage.CHECKER_INSTANCE__PCX_FILE_FOLDERS:
				return getPcxFileFolders();
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				return getVersion();
			case ConfigsPackage.CHECKER_INSTANCE__USE_PCX_FILES:
				return isUsePcxFiles() ? Boolean.TRUE : Boolean.FALSE;
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				return getCommandLineOptions();
			case ConfigsPackage.CHECKER_INSTANCE__AUTOMATIC:
				return isAutomatic() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENT_ID:
				setEnvironmentId((String)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__EXECUTABLE_PATH:
				setExecutablePath((String)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__PCX_FILE_FOLDERS:
				getPcxFileFolders().clear();
				getPcxFileFolders().addAll((Collection<? extends String>)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				setVersion((CheckerVersion)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__USE_PCX_FILES:
				setUsePcxFiles(((Boolean)newValue).booleanValue());
				return;
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				setCommandLineOptions((String)newValue);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__AUTOMATIC:
				setAutomatic(((Boolean)newValue).booleanValue());
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
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENT_ID:
				setEnvironmentId(ENVIRONMENT_ID_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__EXECUTABLE_PATH:
				setExecutablePath(EXECUTABLE_PATH_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__PCX_FILE_FOLDERS:
				getPcxFileFolders().clear();
				return;
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__USE_PCX_FILES:
				setUsePcxFiles(USE_PCX_FILES_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				setCommandLineOptions(COMMAND_LINE_OPTIONS_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_INSTANCE__AUTOMATIC:
				setAutomatic(AUTOMATIC_EDEFAULT);
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
			case ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENT_ID:
				return ENVIRONMENT_ID_EDEFAULT == null ? environmentId != null : !ENVIRONMENT_ID_EDEFAULT.equals(environmentId);
			case ConfigsPackage.CHECKER_INSTANCE__EXECUTABLE_PATH:
				return EXECUTABLE_PATH_EDEFAULT == null ? executablePath != null : !EXECUTABLE_PATH_EDEFAULT.equals(executablePath);
			case ConfigsPackage.CHECKER_INSTANCE__PCX_FILE_FOLDERS:
				return pcxFileFolders != null && !pcxFileFolders.isEmpty();
			case ConfigsPackage.CHECKER_INSTANCE__VERSION:
				return version != VERSION_EDEFAULT;
			case ConfigsPackage.CHECKER_INSTANCE__USE_PCX_FILES:
				return usePcxFiles != USE_PCX_FILES_EDEFAULT;
			case ConfigsPackage.CHECKER_INSTANCE__COMMAND_LINE_OPTIONS:
				return COMMAND_LINE_OPTIONS_EDEFAULT == null ? commandLineOptions != null : !COMMAND_LINE_OPTIONS_EDEFAULT.equals(commandLineOptions);
			case ConfigsPackage.CHECKER_INSTANCE__AUTOMATIC:
				return automatic != AUTOMATIC_EDEFAULT;
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
		result.append(" (environmentId: "); //$NON-NLS-1$
		result.append(environmentId);
		result.append(", executablePath: "); //$NON-NLS-1$
		result.append(executablePath);
		result.append(", pcxFileFolders: "); //$NON-NLS-1$
		result.append(pcxFileFolders);
		result.append(", version: "); //$NON-NLS-1$
		result.append(version);
		result.append(", usePcxFiles: "); //$NON-NLS-1$
		result.append(usePcxFiles);
		result.append(", commandLineOptions: "); //$NON-NLS-1$
		result.append(commandLineOptions);
		result.append(", automatic: "); //$NON-NLS-1$
		result.append(automatic);
		result.append(')');
		return result.toString();
	}

} //CheckerInstanceImpl
