package org.eclipse.dltk.tcl.core.tests.launching;

import junit.framework.Test;

import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclNature;

public class TclPackagesTests extends AbstractModelTests {

	public TclPackagesTests(String name) {
		super("org.eclipse.dltk.tcl.core.tests", name);
	}

	public TclPackagesTests(String testProjectName, String name) {
		super(testProjectName, name);
	}

	public static Test suite() {
		return new Suite(TclPackagesTests.class);
	}

	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

	protected IInterpreterInstall createInstall(String path, String id,
			IInterpreterInstallType type) {
		IFileHandle file = EnvironmentManager.getLocalEnvironment().getFile(
				new Path(path));
		if (!file.exists()) {
			return null;
		}
		IInterpreterInstall install = type.findInterpreterInstall(id);

		if (install == null)
			install = type.createInterpreterInstall(id);

		install.setName("");
		install.setInstallLocation(file);
		install.setLibraryLocations(null);
		install.setEnvironmentVariables(null);
		return install;
	}

	protected IInterpreterInstall createNewInterpreter() throws Exception {
		String atsRootPath = "/Develop/cisco/ats5.0.0/";

		IInterpreterInstallType[] installTypes = ScriptRuntime
				.getInterpreterInstallTypes(TclNature.NATURE_ID);
		int id = 0;
		for (int i = 0; i < installTypes.length; i++) {
			String installId = getNatureId() + "_";
			IInterpreterInstall install = createInstall(atsRootPath
					+ "bin/tclsh", installId + Integer.toString(++id),
					installTypes[i]);
			if (install != null) {
				EnvironmentVariable[] vars = {
						new EnvironmentVariable("AUTOTEST", atsRootPath),
						new EnvironmentVariable("ATS_EASY", atsRootPath
								+ "ats_easy") };
				install.setEnvironmentVariables(vars);
				return install;
			}
		}
		throw new Exception("Failed to create appropriate install");
	}

	public void testPackages001() throws Exception {
		// IEnvironment environment = EnvironmentManager.getLocalEnvironment();
		// IExecutionEnvironment executionEnvironment = (IExecutionEnvironment)
		// environment
		// .getAdapter(IExecutionEnvironment.class);
		// IInterpreterInstall install = createNewInterpreter();
		// String[] pkgs = new String[] { "smtp", "crc32", "bench", "dom",
		// "page::util::norm::peg", "widget::scrolledwindow",
		// "page::parse::pegser", "$urlparts(scheme)::geturl",
		// "grammar::me::tcl", "transfer::data::destination", "TnmEther",
		// "Seclib", "logger::appender", "SOAP::http", "tile::theme::*",
		// "tcllib", "$args", "Test", "time", "choosefont", "SOAP::beep",
		// "doctools::idx", "Thread", "TclSimicsClient", "app-$command",
		// "textutil::expander", "$pkg_name_title",
		// "page::compiler::peg::mecpu", "style::as", "Tk",
		// "page::util::flow", "Csccon", "log", "vfs", "beepcore::peer",
		// "Diagrams", "Expect", "Autoeasy", "ldap", "domtext",
		// "page::parse::lemon", "textutil::repeat", "Easy",
		// "beepcore::mixer", "pluginmgr", "pref", "TkinedCommand",
		// "Parser", "term::ansi::code::ctrl", "aereport", "optcsv",
		// "snackogg", "mk4vfs", "math::fuzzy", "XOTcl",
		// "page::gen::peg::ser", "treeql", "SASL::NTLM", "sha1",
		// "ripemd160", "jlib", "img::bmp", "base32::core", "meta",
		// "$urlparts(scheme)", "md5cryptc", "xpath", "Control",
		// "textutil::string", "grammar::me::cpu::gasm", "Tablelist_tile",
		// "logger", "tclDES", "struct::stack", "Ibmcon", "comm",
		// "Winico", "Tclx", "pref::devkit", "dom::tclgeneric", "dns",
		// "dom::generic", "snack", "math", "Async",
		// "xotcl::comm::httpAccess", "xslt", "Img", "tile", "md5",
		// "xotcl::wafecompat", "md4", "vfs::${type}", "interp", "uevent",
		// "page::util::norm::lemon", "xotcl::comm::ftp", "AtsAuto",
		// "docstrip", "fileutil::multi::op", "TkinedTool",
		// "page::pluginmgr", "Cisco", "htmlparse", "Power_Cycler",
		// "doctools", "muc", "browse", "Wincon", "pda", "askleo",
		// "soapinterop::B", "TnmSnmp", "soapinterop::C",
		// "widget::menuentry", "otp", "picoirc", "beepcore::log",
		// "TkinedDialog", "control", "math::interpolate",
		// "term::receive", "mutl", "grammar::fa::op", "iou",
		// "struct::pool", "SOAP", "xml", "nntp", "Parallel", "uri",
		// "TnmScriptMib", "chatwidget", "term::send", "sound",
		// "doctools::changelog", "ftp", "as::style", "stooop",
		// "TclUtils", "ctext", "Atslog", "dde", "fileutil",
		// "term::ansi::code", "widget::toolbar", "Rsrcm",
		// "math::bigfloat", "ipMorec", "TkinedEditor", "Diagcon",
		// "Plotchart", "bibtex", "pas", "xotcl::trace", "struct",
		// "tablelist::common", "Itk", "TkinedObjects",
		// "fileutil::magic::cgen", "term::ansi::send", "doctools::toc",
		// "term::interact::pager", "autoproxy",
		// "page::analysis::peg::reachable", "globfind",
		// "term::interact::menu", "TnmMib", "topomap", "cmdline",
		// "Mk4tcl", "widget::panelframe", "wip", "struct::record",
		// "domtree", "struct::matrix", "base64", "$pkg", "irc", "$a",
		// "tooltip", "smtpd", "XMLRPC::Domain", "combobox", "Tnm", "tie",
		// "dom::libxml2", "BWidget", "struct::set", "TkinedMisc",
		// "wrapper", "parse_dashed_args", "struct::graph", "ldapx",
		// "TnmTerm", "report", "dom::tcl", "page::plugin", "Tcldot",
		// "opt", "nameserv", "$pkg_name", "Memchan", "SOAP::WSDL",
		// "transfer::data::source", "math::polynomials",
		// "tile::theme::$theme", "math::complexnumbers",
		// "struct::skiplist", "xotcl::comm::httpd", "widget::dialog",
		// "wsurf", "http", "sgmlparser", "page::util::peg", "asn",
		// "mime", "csv", "registry", "md4c", "optchecker", "des",
		// "Renicam", "tcltest", "snit", "struct::tree", "tclDESjr",
		// "ncgi", "khim", "snacksphere", "SOAP::CGI", "msgcat", "atshm",
		// "term::ansi::code::macros", "page::parse::peg", "Tkhtml",
		// "autoscroll", "xotcl::xodoc", "mapproj", "Tcl", "sgml",
		// "vfs::mkcl", "SOAP::Utils", "Tkined", "switched", "ntext",
		// "xotcl::mixinStrategy", "textutil::adjust", "nameserv::common",
		// "SOAP::dom", "tls", "term::receive::bind",
		// "page::gen::peg::me", "critcl", "tcltestUtils",
		// "doctools::__undefined__", "textutil::tabify", "${driver}vfs",
		// "idle", "transfer::copy", "xotcl::metadataAnalyzer",
		// "Tablelist", "grammar::me::util", "math::special",
		// "xotcl::package", "SOAP::xpath",
		// "xotcl::staticMetadataAnalyzer", "TnmMap", "starkit",
		// "TnmDialog", "textutil", "struct::prioqueue", "textutil::trim",
		// "TnmMonitor", "xotcl::comm::mime", "chat", "SASL",
		// "tkpiechart", "mbox", "xotcl::comm::pcache", "tools", "Catlib",
		// "grammar::peg", "Tktable", "Trf", "rc4c", "textutil::split",
		// "struct::queue", "tdom", "page::gen::tree::text",
		// "term::ansi::code::attr", "transfer::connect", "XMLRPC",
		// "img::jpeg", "Mentry", "TkinedHelp", "Mad", "TkinedEvent",
		// "vfs::template", "page::analysis::peg::realizable",
		// "SOAP::Domain", "fileutil::magic::rt", "TnmInet", "uri::urn",
		// "ftpd", "style::${newstyle}", "grammar::me::cpu::core",
		// "dtglue", "dict", "math::statistics", "page::gen::peg::hb",
		// "img::png", "term::ansi::ctrl::unix",
		// "xotcl::comm::connection", "xotcl::htmllib", "Itcl", "pats",
		// "activestate::teapot::link", "PatsCmd", "widget::screenruler",
		// "page::gen::peg::mecpu", "trsync", "math::bignum", "SimDev",
		// "widget", "rpcvar", "page::gen::peg::cpkg",
		// "soapinterop::base", "rssrdr", "page::analysis::peg::emodes",
		// "TkinedDiagram", "math::constants", "ip",
		// "math::linearalgebra", "struct::list", "xmldefs",
		// "page::util::quote", "Iwidgets", "tcllibc",
		// "page::gen::peg::canon", "page::parse::peghb" };
		// StringBuffer b = new StringBuffer();
		// for (int i = 0; i < pkgs.length; i++) {
		// if (i != 0) {
		// b.append(" " + pkgs[i]);
		// } else {
		// b.append(pkgs[i]);
		// }
		// }
		// TclPackage[] packages = DLTKTclHelper.getSrcs(executionEnvironment,
		// install.getInstallLocation(),
		// install.getEnvironmentVariables(), b.toString());
		// assertEquals(packages.length, 268);
	}

	public void testPackages002() throws Exception {
		// BufferedReader stream = new BufferedReader(new InputStreamReader(this
		// .getClass().getResourceAsStream("packages002.txt")));
		// final List output = new ArrayList();
		// String line;
		// while ((line = stream.readLine()) != null) {
		// output.add(line);
		// }
		// TclPackage[] packagePath = DLTKTclHelper.getPackagePath(output);
		// assertEquals(packagePath.length, 31);
		// assertEquals(packagePath[5].getName(), "ha");
		// assertEquals(packagePath[30].getName(), "tdom");
	}
}
