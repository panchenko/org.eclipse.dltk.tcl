package org.eclipse.dltk.tcl.internal.core.packages;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.internal.core.packages.DLTKTclHelper.TclPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class persistently holds all required information for specified
 * interpreter package to path associations.
 */
public class PackagesManager {
	private static final String VERSION_ATTR = "version";
	private static final String LOCATION_ATTR = "location";
	private static final String DEPENDENCY_TAG = "dependency"; //$NON-NLS-1$
	private static final String INTERPRETER_TAG = "interpreter"; //$NON-NLS-1$
	private static final String VALUE_ATTR = "value"; //$NON-NLS-1$
	private static final String PACKAGES_FILE = "packages.txt"; //$NON-NLS-1$

	private static final String PACKAGES_TAG = "packages"; //$NON-NLS-1$
	private static final String PACKAGES_VERSION_ATTR = VERSION_ATTR; //$NON-NLS-1$
	private static final String PACKAGES_VERSION_NUMBER = "20090521T1353"; //$NON-NLS-1$

	private static final String PACKAGE_TAG = "package"; //$NON-NLS-1$
	private static final String PACKAGE_VERSION_ATTR = VERSION_ATTR; //$NON-NLS-1$

	private static final String PACKAGE_SOURCE = "source";

	private static final String INTERPRETER_ATTR = INTERPRETER_TAG;
	private static final String NAME_ATTR = "name"; //$NON-NLS-1$
	private static final String PATH_TAG = "path"; //$NON-NLS-1$

	public static class PackageInfo {
		private int start, end;
		private String packageName;
		private String packageVersion;
		private ISourceModule module;
		private String moduleHandle;

		public PackageInfo() {
		}

		public PackageInfo(String packageName, String packageVersion,
				ISourceModule module) {
			this.packageName = packageName;
			this.packageVersion = packageVersion;
			this.module = module;
		}

		public PackageInfo(String packageName, String packageVersion,
				String moduleHandle) {
			this.packageName = packageName;
			this.packageVersion = packageVersion;
			this.moduleHandle = moduleHandle;
		}

		public PackageInfo(String packageName) {
			this.packageName = packageName;
		}

		public String getPackageName() {
			return packageName;
		}

		public String getPackageVersion() {
			return packageVersion;
		}

		public ISourceModule getModule() {
			if (module == null && moduleHandle != null) {
				module = (ISourceModule) DLTKCore.create(moduleHandle);
			}
			return module;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getStart() {
			return start;
		}

		public void setEnd(int end) {
			this.end = end;
		}

		public int getEnd() {
			return end;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((packageName == null) ? 0 : packageName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PackageInfo other = (PackageInfo) obj;
			if (packageName == null) {
				if (other.packageName != null)
					return false;
			} else if (!packageName.equals(other.packageName))
				return false;
			return true;
		}
	}

	private static PackagesManager manager;

	/**
	 * Contains association of PackageKey to PackageInformation
	 */
	private Map<InterpreterPairKey, PackageInformation> packages = new HashMap<InterpreterPairKey, PackageInformation>();

	/**
	 * Contains set of interpreter to list of packages association.
	 */
	private Map<InterpreterPairKey, Set<PackageInfo>> interpreterToPackages = new HashMap<InterpreterPairKey, Set<PackageInfo>>();
	private Map<String, IPath[]> packsWithDeps = new HashMap<String, IPath[]>();

	public static PackagesManager getInstance() {
		if (manager == null) {
			manager = new PackagesManager();
		}
		return manager;
	}

	private PackagesManager() {
		initialize();
	}

	private static class InterpreterPairKey {
		private String value;
		private String interpreterPath;

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((interpreterPath == null) ? 0 : interpreterPath
							.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			InterpreterPairKey other = (InterpreterPairKey) obj;
			if (interpreterPath == null) {
				if (other.interpreterPath != null)
					return false;
			} else if (!interpreterPath.equals(other.interpreterPath))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String packageName) {
			this.value = packageName;
		}

		public String getInterpreterPath() {
			return interpreterPath;
		}

		public void setInterpreterPath(String interpreterPath) {
			this.interpreterPath = interpreterPath;
		}
	}

	public static class PackageInformation {
		private final Set<IPath> paths = new HashSet<IPath>();
		private final Set<String> dependencies = new HashSet<String>();
		private final Set<String> sources = new HashSet<String>();
		private String version;

		public Set<IPath> getPaths() {
			return paths;
		}

		public Set<String> getDependencies() {
			return dependencies;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public Set<String> getSources() {
			return sources;
		}
	}

	private void initialize() {
		IPath packagesPath = TclPlugin.getDefault().getStateLocation().append(
				PACKAGES_FILE);
		File packagesFile = packagesPath.toFile();
		if (packagesFile.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				Document document = builder.parse(new BufferedInputStream(
						new FileInputStream(packagesFile), 2048));
				populate(document.getDocumentElement());
			} catch (ParserConfigurationException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (SAXException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		cleanBrokenElements();
	}

	private void cleanBrokenElements() {
		Set<InterpreterPairKey> keysToRemove = new HashSet<InterpreterPairKey>();
		for (InterpreterPairKey key : this.interpreterToPackages.keySet()) {
			String path = key.getInterpreterPath();
			boolean found = false;
			String projectName = key.getValue();
			if (projectName.length() != 0) {
				IProject project = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(projectName);
				if (!project.isAccessible()) {
					keysToRemove.add(key);
					continue;
				}
			}
			// Check for install existance
			IInterpreterInstallType[] types = ScriptRuntime
					.getInterpreterInstallTypes();
			for (IInterpreterInstallType iInterpreterInstallType : types) {
				IInterpreterInstall[] installs = iInterpreterInstallType
						.getInterpreterInstalls();
				for (IInterpreterInstall iInterpreterInstall : installs) {
					if (path.equals(iInterpreterInstall.getInstallLocation()
							.toString())) {
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
			if (!found) {
				keysToRemove.add(key);
			}
		}
		for (InterpreterPairKey interpreterPairKey : keysToRemove) {
			this.interpreterToPackages.remove(interpreterPairKey);
		}
	}

	private void save() {
		cleanBrokenElements();
		IPath packagesPath = TclPlugin.getDefault().getStateLocation().append(
				PACKAGES_FILE);
		File packagesFile = packagesPath.toFile();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			save(document);

			FileOutputStream fos = new FileOutputStream(packagesFile, false);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);

			TransformerFactory serFactory = TransformerFactory.newInstance();
			Transformer transformer = serFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(document);
			StreamResult outputTarget = new StreamResult(bos);
			transformer.transform(source, outputTarget);
			bos.close();
			fos.close();

		} catch (ParserConfigurationException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (TransformerException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void save(Document doc) {
		Element packagesElement = doc.createElement(PACKAGES_TAG);
		packagesElement.setAttribute(PACKAGES_VERSION_ATTR,
				PACKAGES_VERSION_NUMBER);
		doc.appendChild(packagesElement);
		for (Iterator<InterpreterPairKey> iterator = this.packages.keySet()
				.iterator(); iterator.hasNext();) {
			InterpreterPairKey key = iterator.next();

			Element packageElement = doc.createElement(PACKAGE_TAG);
			packageElement.setAttribute(NAME_ATTR, key.getValue());
			packageElement.setAttribute(INTERPRETER_ATTR, key
					.getInterpreterPath());

			PackageInformation info = this.packages.get(key);
			if (info.getVersion() != null && info.getVersion().length() != 0) {
				packageElement.setAttribute(PACKAGE_VERSION_ATTR, info
						.getVersion());
			}
			Set<IPath> paths = info.getPaths();
			for (Iterator<IPath> iterator2 = paths.iterator(); iterator2
					.hasNext();) {
				IPath path = iterator2.next();
				Element pathElement = doc.createElement(PATH_TAG);
				pathElement.setAttribute(VALUE_ATTR, path.toOSString());
				packageElement.appendChild(pathElement);
			}
			Set<String> deps = info.getDependencies();
			for (Iterator<String> iterator2 = deps.iterator(); iterator2
					.hasNext();) {
				String pkgName = iterator2.next();
				Element pkgElement = doc.createElement(DEPENDENCY_TAG);
				pkgElement.setAttribute(NAME_ATTR, pkgName);
				packageElement.appendChild(pkgElement);
			}
			Set<String> sources = info.getSources();
			for (Iterator<String> iterator3 = sources.iterator(); iterator3
					.hasNext();) {
				String source = iterator3.next();
				Element pkgElement = doc.createElement(PACKAGE_SOURCE);
				pkgElement.setAttribute(LOCATION_ATTR, source);
				packageElement.appendChild(pkgElement);
			}
			packagesElement.appendChild(packageElement);
		}
		for (Iterator<InterpreterPairKey> iterator = this.interpreterToPackages
				.keySet().iterator(); iterator.hasNext();) {
			InterpreterPairKey key = iterator.next();

			Element interpreterElement = doc.createElement(INTERPRETER_TAG);
			interpreterElement.setAttribute(NAME_ATTR, key.getValue());
			interpreterElement.setAttribute(INTERPRETER_ATTR, key
					.getInterpreterPath());
			Set<PackageInfo> pkgs = this.interpreterToPackages.get(key);
			for (Iterator<PackageInfo> iterator2 = pkgs.iterator(); iterator2
					.hasNext();) {
				PackageInfo info = iterator2.next();
				Element pathElement = doc.createElement(PACKAGE_TAG);
				pathElement.setAttribute(VALUE_ATTR, info.getPackageName());
				if (info.getPackageVersion() != null) {
					pathElement.setAttribute(VERSION_ATTR, info
							.getPackageVersion());
				}
				if (info.getModule() != null) {
					pathElement.setAttribute("handle", info.getModule()
							.getHandleIdentifier());
				}
				interpreterElement.appendChild(pathElement);
			}
			packagesElement.appendChild(interpreterElement);
		}
	}

	private synchronized void populate(Element documentElement) {
		if (!PACKAGES_TAG.equals(documentElement.getNodeName())) {
			return;
		}
		if (!PACKAGES_VERSION_NUMBER.equals(documentElement
				.getAttribute(PACKAGES_VERSION_ATTR))) {
			return;
		}
		NodeList childNodes = documentElement.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equalsIgnoreCase(PACKAGE_TAG)) {
					Element e = (Element) child;
					String packageName = e.getAttribute(NAME_ATTR);
					String interpreter = e.getAttribute(INTERPRETER_ATTR);
					PackageInformation packageInfo = new PackageInformation();
					final String version = e.getAttribute(PACKAGE_VERSION_ATTR);
					if (version != null && version.length() != 0) {
						packageInfo.setVersion(version);
					}
					NodeList childrens = e.getChildNodes();
					for (int j = 0; j < childrens.getLength(); j++) {
						Node path = childrens.item(j);
						if (path.getNodeType() == Node.ELEMENT_NODE) {
							if (path.getNodeName().equalsIgnoreCase(PATH_TAG)) {
								String pathValue = ((Element) path)
										.getAttribute(VALUE_ATTR);
								packageInfo.getPaths().add(new Path(pathValue));
							} else if (path.getNodeName().equalsIgnoreCase(
									DEPENDENCY_TAG)) {
								String pkgName = ((Element) path)
										.getAttribute(NAME_ATTR);
								packageInfo.getDependencies().add(pkgName);
							} else if (path.getNodeName().equalsIgnoreCase(
									PACKAGE_SOURCE)) {
								String source = ((Element) path)
										.getAttribute(LOCATION_ATTR);
								packageInfo.getSources().add(source);
							}
						}
					}
					this.packages.put(makeKey(packageName, interpreter),
							packageInfo);
				} else if (child.getNodeName()
						.equalsIgnoreCase(INTERPRETER_TAG)) {
					Element e = (Element) child;
					String interpreterLocation = e
							.getAttribute(INTERPRETER_ATTR);
					String name = e.getAttribute(NAME_ATTR);
					InterpreterPairKey key = makeKey(name, interpreterLocation);
					NodeList paths = e.getChildNodes();
					Set<PackageInfo> packagesSet = new HashSet<PackageInfo>();
					for (int j = 0; j < paths.getLength(); j++) {
						Node packageNode = paths.item(j);
						if (packageNode.getNodeType() == Node.ELEMENT_NODE) {
							if (packageNode.getNodeName().equalsIgnoreCase(
									PACKAGE_TAG)) {
								Element element = (Element) packageNode;
								String packageNameValue = element
										.getAttribute(VALUE_ATTR);
								String version = element
										.getAttribute(VERSION_ATTR);
								if (version.trim().length() == 0) {
									version = null;
								}
								String handle = element.getAttribute("handle");
								if (handle.trim().length() == 0) {
									handle = null;
								}
								packagesSet.add(new PackageInfo(
										packageNameValue, version, handle));
							}
						}
					}
					this.interpreterToPackages.put(key, packagesSet);
				}
			}
		}
	}

	private InterpreterPairKey makeKey(String value, String interpreter) {
		InterpreterPairKey key = new InterpreterPairKey();
		key.setValue(value);
		key.setInterpreterPath(interpreter);
		return key;
	}

	/**
	 * Return paths specific only for selected package.
	 */
	public synchronized IPath[] getPathsForPackage(IInterpreterInstall install,
			String packageName) {
		InterpreterPairKey key = makeKey(packageName, install);
		if (this.packages.containsKey(key)) {
			PackageInformation info = this.packages.get(key);
			Set<IPath> els = info.getPaths();
			return els.toArray(new IPath[els.size()]);
		}
		// Retrieve paths from interpreter with all dependencies.
		TclPackage[] srcs = DLTKTclHelper.getSrcs(install.getExecEnvironment(),
				install.getInstallLocation(),
				install.getEnvironmentVariables(), packageName);

		PackageInformation resultInfo = null;
		for (int i = 0; i < srcs.length; i++) {
			Set<IPath> paths2 = srcs[i].getPaths();
			InterpreterPairKey okey = makeKey(srcs[i].getName(), install);
			PackageInformation info;
			if (this.packages.containsKey(okey)) {
				info = this.packages.get(okey);
			} else {
				info = new PackageInformation();
			}
			info.setVersion(srcs[i].getVersion());
			info.getPaths().addAll(paths2);
			info.getDependencies().addAll(srcs[i].getDependencies());
			this.packages.put(okey, info);
			if (okey.equals(key)) {
				resultInfo = info;
			}
		}
		// Follow all dependencies
		if (resultInfo == null) {
			this.packages.put(key, new PackageInformation());
			return new IPath[0];
		}

		Set<IPath> resultPaths = new HashSet<IPath>();
		resultPaths.addAll(resultInfo.getPaths());

		save();
		return resultPaths.toArray(new IPath[resultPaths.size()]);
	}

	public synchronized String getPackageVersion(IInterpreterInstall install,
			String packageName) {
		getPathsForPackage(install, packageName);
		final PackageInformation info = this.packages.get(makeKey(packageName,
				install));
		if (info != null) {
			return info.getVersion();
		} else {
			return null;
		}
	}

	public synchronized Map<String, PackageInformation> getDependencies(
			String pkgName, IInterpreterInstall install) {
		Set<String> checkedPackages = new HashSet<String>();
		Map<String, PackageInformation> packagesSet = new HashMap<String, PackageInformation>();
		InterpreterPairKey key = makeKey(pkgName, install);
		PackageInformation info = this.packages.get(key);
		if (info != null) {
			traverseDependencies(packagesSet, checkedPackages, info, install);
		}
		return packagesSet;
	}

	private InterpreterPairKey makeKey(String value, IInterpreterInstall install) {
		return makeKey(value, install.getInstallLocation().toString());
	}

	private synchronized void traverseDependencies(
			Map<String, PackageInformation> packagesSet,
			Set<String> checkedPackages, PackageInformation resultInfo,
			IInterpreterInstall install) {
		Set<String> dependencies = resultInfo.getDependencies();
		for (Iterator<String> iterator = dependencies.iterator(); iterator
				.hasNext();) {
			String pkgName = iterator.next();
			if (!checkedPackages.contains(pkgName)) {
				checkedPackages.add(pkgName);
				InterpreterPairKey pkgKey = makeKey(pkgName, install);
				if (this.packages.containsKey(pkgKey)) {
					PackageInformation depInfo = this.packages.get(pkgKey);
					packagesSet.put(pkgName, depInfo);
					traverseDependencies(packagesSet, checkedPackages, depInfo,
							install);
				}
			}
		}
	}

	public synchronized Set<PackageInfo> getPackageNames(
			IInterpreterInstall install) {
		InterpreterPairKey key = getInterpreterKey(install);
		if (this.interpreterToPackages.containsKey(key)) {
			return this.interpreterToPackages.get(key);
		}
		// Evaluate
		Set<PackageInfo> packs = DLTKTclHelper.getPackages(install);
		this.interpreterToPackages.put(key, packs);
		save();
		return packs;
	}

	private InterpreterPairKey getInterpreterKey(IInterpreterInstall install) {
		return makeKey("", install);
	}

	private InterpreterPairKey getInterpreterProjectKey(
			IInterpreterInstall install, String projectName) {
		return makeKey(projectName, install);
	}

	public Set<PackageInfo> getInternalPackageNames(
			IInterpreterInstall install, IScriptProject project) {
		return getInternalPackageNames(install, project.getElementName());
	}

	public Set<PackageInfo> getInternalPackageNames(
			IInterpreterInstall install, IProject project) {
		return getInternalPackageNames(install, project.getName());
	}

	public synchronized Set<PackageInfo> getInternalPackageNames(
			IInterpreterInstall install, String projectName) {
		final InterpreterPairKey key = getInterpreterProjectKey(install,
				projectName);
		if (this.interpreterToPackages.containsKey(key)) {
			return this.interpreterToPackages.get(key);
		}
		return new HashSet<PackageInfo>();
	}

	public synchronized void setInternalPackageNames(
			IInterpreterInstall install, IScriptProject project,
			Set<PackageInfo> names) {
		InterpreterPairKey key = getInterpreterProjectKey(install, project
				.getElementName());
		// TODO compare and save only if there are changes
		this.interpreterToPackages.put(key, new HashSet<PackageInfo>(names));
		save();
	}

	/**
	 * Return all packages paths in one call.
	 */
	public synchronized IPath[] getPathsForPackages(
			IInterpreterInstall install, Set<PackageInfo> packagesInBuild) {

		StringBuffer buf = new StringBuffer();
		PackageInfo[] pkgs = packagesInBuild
				.toArray(new PackageInfo[packagesInBuild.size()]);
		for (int i = 0; i < pkgs.length; i++) {
			buf.append(pkgs[i].getPackageName()).append(" "); //$NON-NLS-1$
		}
		InterpreterPairKey key = makeKey(buf.toString(), install);

		if (this.packages.containsKey(key)) {
			PackageInformation info = this.packages.get(key);
			Set<IPath> paths = info.getPaths();
			return paths.toArray(new IPath[paths.size()]);
		}
		// Retrieve paths from interpreter with all dependencies.
		TclPackage[] srcs = DLTKTclHelper.getSrcs(install.getExecEnvironment(),
				install.getInstallLocation(),
				install.getEnvironmentVariables(), buf.toString());
		Set<IPath> result = new HashSet<IPath>();
		if (srcs == null) {
			return new IPath[0];
		}
		for (int i = 0; i < srcs.length; i++) {
			Set<IPath> paths2 = srcs[i].getPaths();
			InterpreterPairKey okey = makeKey(srcs[i].getName(), install);
			PackageInformation info = null;
			if (this.packages.containsKey(okey)) {
				info = this.packages.get(okey);
			} else {
				info = new PackageInformation();
			}
			result.addAll(paths2);
			info.setVersion(srcs[i].getVersion());
			info.getPaths().addAll(paths2);
			info.getDependencies().addAll(srcs[i].getDependencies());
			info.getSources().addAll(srcs[i].getSources());
			this.packages.put(okey, info);
		}

		PackageInformation info = new PackageInformation();
		info.getPaths().addAll(result);
		this.packages.put(key, info);

		for (int i = 0; i < pkgs.length; i++) {
			InterpreterPairKey lkey = makeKey(pkgs[i].getPackageName(), install);
			if (!this.packages.containsKey(lkey)) {
				this.packages.put(lkey, new PackageInformation());
			}
		}
		save();

		return result.toArray(new IPath[result.size()]);
	}

	public IPath[] getAllPaths(String pkgName, IInterpreterInstall install) {
		Set<IPath> result = new HashSet<IPath>();
		IPath[] paths = this.getPathsForPackage(install, pkgName);
		result.addAll(Arrays.asList(paths));
		Map<String, PackageInformation> dependencies = this.getDependencies(
				pkgName, install);
		for (Iterator<String> iterator = dependencies.keySet().iterator(); iterator
				.hasNext();) {
			String packageName = iterator.next();
			PackageInformation info = dependencies.get(packageName);
			result.addAll(info.getPaths());
		}
		return result.toArray(new IPath[result.size()]);
	}

	/**
	 * This method removes all information about specified interpreter.
	 * 
	 * @param install
	 */
	public synchronized void removeInterprterInfo(IInterpreterInstall install) {
		// Remove interpreter to packages set
		InterpreterPairKey interpreterPath = getInterpreterKey(install);
		this.interpreterToPackages.remove(interpreterPath);
		// Remove all values stored for interpreter packages
		for (Iterator<InterpreterPairKey> iterator = this.packages.keySet()
				.iterator(); iterator.hasNext();) {
			InterpreterPairKey key = iterator.next();
			String path = key.getInterpreterPath();
			if (path.equals(interpreterPath)) {
				iterator.remove();
			}
		}
		save();
	}

	/**
	 * Clears all cached information.
	 */
	public synchronized void clearCache() {
		this.interpreterToPackages.clear();
		this.packages.clear();
		this.packsWithDeps.clear();
		save();
	}

	public IPath[] getPathsForPackageWithDeps(IInterpreterInstall install,
			String name) {
		Set<IPath> result = new HashSet<IPath>();
		IPath[] paths = this.getPathsForPackage(install, name);
		result.addAll(Arrays.asList(paths));

		Map<String, PackageInformation> dependencies = getDependencies(name,
				install);
		for (Iterator<String> iterator = dependencies.keySet().iterator(); iterator
				.hasNext();) {
			String pkgName = iterator.next();
			result.addAll(Arrays.asList(getPathsForPackage(install, pkgName)));
		}
		return result.toArray(new IPath[result.size()]);
	}

	public IPath[] getPathsForPackagesWithDeps(IInterpreterInstall install,
			Set<PackageInfo> packagesSet) {

		String pkey = makePKey(packagesSet);
		if (this.packsWithDeps.containsKey(pkey)) {
			return this.packsWithDeps.get(pkey);
		}

		Set<IPath> result = new HashSet<IPath>();
		IPath[] paths = this.getPathsForPackages(install, packagesSet);
		result.addAll(Arrays.asList(paths));

		for (Iterator<PackageInfo> jiterator = packagesSet.iterator(); jiterator
				.hasNext();) {
			PackageInfo info = jiterator.next();
			String name = info.getPackageName();
			Map<String, PackageInformation> dependencies = getDependencies(
					name, install);
			for (Iterator<String> iterator = dependencies.keySet().iterator(); iterator
					.hasNext();) {
				String pkgName = iterator.next();
				result.addAll(Arrays
						.asList(getPathsForPackage(install, pkgName)));
			}
		}
		IPath[] array = result.toArray(new IPath[result.size()]);
		this.packsWithDeps.put(pkey, array);
		return array;
	}

	private String makePKey(Set<PackageInfo> packagesSet) {
		StringBuffer buffer = new StringBuffer();
		List<PackageInfo> l = new ArrayList<PackageInfo>();
		l.addAll(packagesSet);
		Collections.sort(l, new Comparator<PackageInfo>() {
			public int compare(PackageInfo o1, PackageInfo o2) {
				return o1.getPackageName().compareTo(o2.getPackageName());
			}
		});
		for (Iterator<PackageInfo> iterator = l.iterator(); iterator.hasNext();) {
			PackageInfo object = iterator.next();
			buffer.append(object);
		}
		return buffer.toString();
	}

	/**
	 * Tests if the specified packageName has correct syntax.
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean isValidPackageName(String packageName) {
		return packageName != null && packageName.length() != 0
				&& packageName.indexOf('$') == -1
				&& packageName.indexOf('[') == -1
				&& packageName.indexOf(']') == -1;
	}

	public PackageInformation getPackageInfo(String packageName,
			IInterpreterInstall install) {
		InterpreterPairKey key = makeKey(packageName, install);
		PackageInformation info = this.packages.get(key);
		if (info != null) {
			return info;
		}
		return null;
	}

	/**
	 * Calculate all package dependencies
	 */
	public Set<String> getPackagesDeps(IInterpreterInstall install,
			Set<String> packages) {
		Set<String> result = new HashSet<String>();
		List<String> toProcess = new ArrayList<String>();
		toProcess.addAll(packages);
		while (!toProcess.isEmpty()) {
			String pkgName = toProcess.remove(0);
			if (result.add(pkgName)) {
				PackageInformation info = getPackageInfo(pkgName, install);
				if (info != null) {
					Set<String> dependencies = info.getDependencies();
					for (Iterator<String> iterator = dependencies.iterator(); iterator
							.hasNext();) {
						String dep = iterator.next();
						if (!result.contains(dep)) {
							toProcess.add(dep);
						}
					}
				}
			}
		}
		return result;
	}
}
