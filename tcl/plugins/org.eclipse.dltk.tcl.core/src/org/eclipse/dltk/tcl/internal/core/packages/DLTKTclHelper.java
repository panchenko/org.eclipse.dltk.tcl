package org.eclipse.dltk.tcl.internal.core.packages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.internal.core.packages.PackagesManager.PackageInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DLTKTclHelper {

	private static final String DLTK_TCL = "scripts/dltk.tcl"; //$NON-NLS-1$

	public static final String END_OF_STREAM = "DLTK-TCL-HELPER-9E7A168E-5EEF-4a46-A86D-0C82E90686E4-END-OF-STREAM";

	private static List<String> deployExecute(IExecutionEnvironment exeEnv,
			String installLocation, String[] arguments,
			EnvironmentVariable[] env) {
		IDeployment deployment = exeEnv.createDeployment();
		IFileHandle script = deploy(deployment);
		if (script == null) {
			return null;
		}

		IFileHandle workingDir = script.getParent();
		InterpreterConfig config = ScriptLaunchUtil.createInterpreterConfig(
				exeEnv, script, workingDir, env);
		// For wish
		config.removeEnvVar("DISPLAY"); //$NON-NLS-1$

		if (arguments != null) {
			config.addScriptArgs(arguments);
		}

		Process process = null;
		try {
			process = ScriptLaunchUtil.runScriptWithInterpreter(exeEnv,
					installLocation, config);
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		if (process == null) {
			return new ArrayList<String>();
		}
		List<String> output = ProcessOutputCollector.execute(process);
		deployment.dispose();
		return output;
	}

	private static IFileHandle deploy(IDeployment deployment) {
		IFileHandle script;
		try {
			IPath path = deployment.add(TclPlugin.getDefault().getBundle(),
					DLTK_TCL);
			script = deployment.getFile(path);
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
			return null;
		}
		return script;
	}

	public static String[] getDefaultPath(IFileHandle installLocation,
			EnvironmentVariable[] environment) {
		// Process process = deployExecute(installLocation.getAbsolutePath(),
		// new String[] { "get-paths" }, environment);
		// List content = getScriptOutput(process);
		// String[] autoPath = getAutoPath(content);
		// for (int i = 0; i < autoPath.length; i++) {
		// Path p = new Path(autoPath[i]);
		// if (p.lastSegment().startsWith("tcl8.")) {
		// return new String[] { autoPath[i] };
		// }
		// }
		// process.destroy();
		return new String[0];
		// return autoPath;
	}

	public static TclPackage[] getSrcs(IExecutionEnvironment exeEnv,
			IFileHandle installLocation, EnvironmentVariable[] environment,
			String packageName) {
		IDeployment deployment = exeEnv.createDeployment();
		IFileHandle script = deploy(deployment);
		if (script == null) {
			return null;
		}

		IFileHandle workingDir = script.getParent();
		InterpreterConfig config = ScriptLaunchUtil.createInterpreterConfig(
				exeEnv, script, workingDir, environment);
		String names = packageName;
		ByteArrayInputStream bais = new ByteArrayInputStream(names.getBytes());
		IPath packagesPath = null;
		try {
			packagesPath = deployment.add(bais, "packages.txt"); //$NON-NLS-1$
		} catch (IOException e1) {
			if (DLTKCore.DEBUG) {
				e1.printStackTrace();
			}
			return null;
		}
		IFileHandle file = deployment.getFile(packagesPath);
		// For wish
		config.removeEnvVar("DISPLAY"); //$NON-NLS-1$
		String[] arguments = new String[] { "get-srcs", "-fpkgs", //$NON-NLS-1$ //$NON-NLS-2$
				file.toOSString() };

		config.addScriptArgs(arguments);

		Process process = null;
		try {
			process = ScriptLaunchUtil.runScriptWithInterpreter(exeEnv,
					installLocation.toOSString(), config);
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		if (process == null) {
			return null;
		}
		List<String> output = ProcessOutputCollector.execute(process);
		deployment.dispose();
		return getPackagePath(output);
	}

	private static boolean isElementName(Node nde, String name) {
		if (nde != null) {
			if (nde.getNodeType() == Node.ELEMENT_NODE) {
				if (name.equalsIgnoreCase(nde.getNodeName())) {
					return true;
				}
			}
		}
		return false;
	}

//	private static String[] getAutoPath(List<String> content) {
//		String text = getXMLContent(content);
//		Document document = getDocument(text);
//
//		Set<String> paths = new HashSet<String>();
//		if (document != null) {
//			Element element = document.getDocumentElement();
//			NodeList childNodes = element.getChildNodes();
//			int len = childNodes.getLength();
//			for (int i = 0; i < len; i++) {
//				Node nde = childNodes.item(i);
//				if (isElementName(nde, "path")) { //$NON-NLS-1$
//					Element el = (Element) nde;
//					String path = el.getAttribute("name"); //$NON-NLS-1$
//					if (path.length() > 0) {
//						paths.add(path);
//					}
//				}
//			}
//		}
//		return paths.toArray(new String[paths.size()]);
//	}

	public static class TclPackage {
		private String name;
		private String version;

		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * @param version
		 *            the version to set
		 */
		public void setVersion(String version) {
			this.version = version;
		}

		private Set<IPath> paths = new HashSet<IPath>();
		private Set<String> dependencies = new HashSet<String>();
		private Set<String> sources = new HashSet<String>();

		public TclPackage(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Set<IPath> getPaths() {
			return paths;
		}

		public void setPaths(Set<IPath> paths) {
			this.paths = paths;
		}

		public Set<String> getDependencies() {
			return dependencies;
		}

		public void setDependencies(Set<String> dependencies) {
			this.dependencies = dependencies;
		}

		public Set<String> getSources() {
			return sources;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer(128);
			sb.append("TclPackage"); //$NON-NLS-1$
			sb.append('{');
			sb.append("name=").append(name); //$NON-NLS-1$
			sb.append(' ');
			sb.append("paths=").append(paths); //$NON-NLS-1$
			sb.append(' ');
			sb.append("dependencies=").append(dependencies); //$NON-NLS-1$
			sb.append('}');
			sb.append("sources").append(sources);//$NON-NLS-1$
			sb.append('}');//$NON-NLS-1$
			return sb.toString();
		}
	};

	public static TclPackage[] getPackagePath(List<String> content) {
		String text = getXMLContent(content);
		Document document = getDocument(text);

		Map<String, TclPackage> packages = new HashMap<String, TclPackage>();
		if (document != null) {
			Element element = document.getDocumentElement();
			NodeList childNodes = element.getChildNodes();
			int len = childNodes.getLength();
			for (int i = 0; i < len; i++) {
				Node nde = childNodes.item(i);
				if (isElementName(nde, "path")) { //$NON-NLS-1$
					Element el = (Element) nde;
					NodeList elChilds = el.getChildNodes();
					for (int j = 0; j < elChilds.getLength(); j++) {
						Node pkgNde = elChilds.item(j);
						if (isElementName(pkgNde, "package")) { //$NON-NLS-1$
							populatePackage(packages, pkgNde);
						}
					}
				}
			}
		}
		return packages.values().toArray(new TclPackage[packages.size()]);
	}

	private static void populatePackage(Map<String, TclPackage> packages,
			Node pkgNde) {
		Element pkg = (Element) pkgNde;
		final String pkgName = pkg.getAttribute("name"); //$NON-NLS-1$
		if (pkgName == null || pkgName.length() == 0) {
			return;
		}
		final TclPackage tclPackage;
		if (packages.containsKey(pkgName)) {
			tclPackage = packages.get(pkgName);
		} else {
			tclPackage = new TclPackage(pkgName);
			packages.put(pkgName, tclPackage);
		}
		tclPackage.setVersion(pkg.getAttribute("version"));
		NodeList childs = pkg.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node nde = childs.item(i);
			if (isElementName(nde, "source")) { //$NON-NLS-1$
				Element el = (Element) nde;
				String name = el.getAttribute("name"); //$NON-NLS-1$
				IPath path = new Path(name).removeLastSegments(1);
				tclPackage.getPaths().add(path);
				tclPackage.getSources().add(name);
			} else if (isElementName(nde, "require")) { //$NON-NLS-1$
				Element el = (Element) nde;
				String name = el.getAttribute("name"); //$NON-NLS-1$
				tclPackage.getDependencies().add(name);
			}
		}
	}

	private static Document getDocument(String text) {
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			InputSource source = new InputSource(new StringReader(text));
			Document document = parser.parse(source);
			return document;
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (SAXException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String getXMLContent(List<String> content) {
		StringBuffer newList = new StringBuffer();
		if (content != null) {
			for (Iterator<String> iterator = content.iterator(); iterator
					.hasNext();) {
				String line = iterator.next();
				if (line.trim().startsWith("<")) { //$NON-NLS-1$
					newList.append(line).append("\n"); //$NON-NLS-1$
				}
			}
		}
		return newList.toString();
	}

	public static Set<PackageInfo> getPackages(IInterpreterInstall install) {
		IExecutionEnvironment exeEnv = install.getExecEnvironment();
		List<String> content = deployExecute(exeEnv, install.getInstallLocation()
				.toOSString(), new String[] { "get-pkgs" }, install //$NON-NLS-1$
				.getEnvironmentVariables());
		Set<PackageInfo> packages = new HashSet<PackageInfo>();
		TclPackage[] packagePath = getPackagePath(content);
		for (int i = 0; i < packagePath.length; i++) {
			packages.add(new PackageInfo(packagePath[i].getName(),
					packagePath[i].getVersion(), (String)null));
		}
		return packages;
	}
}
