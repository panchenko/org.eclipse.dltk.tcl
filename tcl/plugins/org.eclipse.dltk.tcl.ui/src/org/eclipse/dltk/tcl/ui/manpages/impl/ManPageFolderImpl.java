/**
 * <copyright>
 * </copyright>
 *
 * $Id: ManPageFolderImpl.java,v 1.1 2009/12/31 09:18:23 apanchenk Exp $
 */
package org.eclipse.dltk.tcl.ui.manpages.impl;

import java.io.File;

import org.eclipse.dltk.tcl.ui.manpages.ManPageFolder;
import org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage;
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
 * An implementation of the model object '<em><b>Man Page Folder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.dltk.tcl.ui.manpages.impl.ManPageFolderImpl#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.dltk.tcl.ui.manpages.impl.ManPageFolderImpl#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ManPageFolderImpl extends EObjectImpl implements ManPageFolder {
	/**
	 * The default value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected static final String PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected String path = PATH_EDEFAULT;

	/**
	 * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeywords()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> keywords;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ManPageFolderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ManpagesPackage.Literals.MAN_PAGE_FOLDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPath() {
		return path;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPath(String newPath) {
		String oldPath = path;
		path = newPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManpagesPackage.MAN_PAGE_FOLDER__PATH, oldPath, path));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getKeywords() {
		if (keywords == null) {
			keywords = new EcoreEMap<String,String>(ManpagesPackage.Literals.STRING_TO_STRING_ENTRY, StringToStringEntryImpl.class, this, ManpagesPackage.MAN_PAGE_FOLDER__KEYWORDS);
		}
		return keywords;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void addPage(String keyword, String file) {
		getKeywords().put(keyword, file);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean verify() {
		if (path == null || path.length() == 0)
			return false;
		final File file = new File(path);
		return file.exists() && file.isDirectory();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ManpagesPackage.MAN_PAGE_FOLDER__KEYWORDS:
				return ((InternalEList<?>)getKeywords()).basicRemove(otherEnd, msgs);
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
			case ManpagesPackage.MAN_PAGE_FOLDER__PATH:
				return getPath();
			case ManpagesPackage.MAN_PAGE_FOLDER__KEYWORDS:
				if (coreType) return getKeywords();
				else return getKeywords().map();
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
			case ManpagesPackage.MAN_PAGE_FOLDER__PATH:
				setPath((String)newValue);
				return;
			case ManpagesPackage.MAN_PAGE_FOLDER__KEYWORDS:
				((EStructuralFeature.Setting)getKeywords()).set(newValue);
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
			case ManpagesPackage.MAN_PAGE_FOLDER__PATH:
				setPath(PATH_EDEFAULT);
				return;
			case ManpagesPackage.MAN_PAGE_FOLDER__KEYWORDS:
				getKeywords().clear();
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
			case ManpagesPackage.MAN_PAGE_FOLDER__PATH:
				return PATH_EDEFAULT == null ? path != null : !PATH_EDEFAULT.equals(path);
			case ManpagesPackage.MAN_PAGE_FOLDER__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
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
		result.append(" (path: "); //$NON-NLS-1$
		result.append(path);
		result.append(')');
		return result.toString();
	}

} //ManPageFolderImpl
