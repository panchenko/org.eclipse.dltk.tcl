/**
 * <copyright>
 * </copyright>
 *
 * $Id: CheckerEnvironmentInstanceImpl.java,v 1.1 2009/02/27 09:16:01 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.tclchecker.model.configs.impl;

import java.util.Collection;

import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.validators.configs.impl.ValidatorEnvironmentInstanceImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Checker Environment Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerEnvironmentInstanceImpl#getPcxFileFolders <em>Pcx File Folders</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerEnvironmentInstanceImpl#isUsePcxFiles <em>Use Pcx Files</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.tclchecker.model.configs.impl.CheckerEnvironmentInstanceImpl#getInstance <em>Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CheckerEnvironmentInstanceImpl extends ValidatorEnvironmentInstanceImpl implements CheckerEnvironmentInstance {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CheckerEnvironmentInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigsPackage.Literals.CHECKER_ENVIRONMENT_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getPcxFileFolders() {
		if (pcxFileFolders == null) {
			pcxFileFolders = new EDataTypeUniqueEList<String>(String.class, this, ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__PCX_FILE_FOLDERS);
		}
		return pcxFileFolders;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__USE_PCX_FILES, oldUsePcxFiles, usePcxFiles));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckerInstance getInstance() {
		if (eContainerFeatureID != ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE) return null;
		return (CheckerInstance)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInstance(CheckerInstance newInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newInstance, ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInstance(CheckerInstance newInstance) {
		if (newInstance != eInternalContainer() || (eContainerFeatureID != ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE && newInstance != null)) {
			if (EcoreUtil.isAncestor(this, newInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newInstance != null)
				msgs = ((InternalEObject)newInstance).eInverseAdd(this, ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS, CheckerInstance.class, msgs);
			msgs = basicSetInstance(newInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE, newInstance, newInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetInstance((CheckerInstance)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE:
				return basicSetInstance(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID) {
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE:
				return eInternalContainer().eInverseRemove(this, ConfigsPackage.CHECKER_INSTANCE__ENVIRONMENTS, CheckerInstance.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__PCX_FILE_FOLDERS:
				return getPcxFileFolders();
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__USE_PCX_FILES:
				return isUsePcxFiles() ? Boolean.TRUE : Boolean.FALSE;
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE:
				return getInstance();
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
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__PCX_FILE_FOLDERS:
				getPcxFileFolders().clear();
				getPcxFileFolders().addAll((Collection<? extends String>)newValue);
				return;
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__USE_PCX_FILES:
				setUsePcxFiles(((Boolean)newValue).booleanValue());
				return;
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE:
				setInstance((CheckerInstance)newValue);
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
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__PCX_FILE_FOLDERS:
				getPcxFileFolders().clear();
				return;
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__USE_PCX_FILES:
				setUsePcxFiles(USE_PCX_FILES_EDEFAULT);
				return;
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE:
				setInstance((CheckerInstance)null);
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
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__PCX_FILE_FOLDERS:
				return pcxFileFolders != null && !pcxFileFolders.isEmpty();
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__USE_PCX_FILES:
				return usePcxFiles != USE_PCX_FILES_EDEFAULT;
			case ConfigsPackage.CHECKER_ENVIRONMENT_INSTANCE__INSTANCE:
				return getInstance() != null;
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
		result.append(" (pcxFileFolders: "); //$NON-NLS-1$
		result.append(pcxFileFolders);
		result.append(", usePcxFiles: "); //$NON-NLS-1$
		result.append(usePcxFiles);
		result.append(')');
		return result.toString();
	}

} //CheckerEnvironmentInstanceImpl
