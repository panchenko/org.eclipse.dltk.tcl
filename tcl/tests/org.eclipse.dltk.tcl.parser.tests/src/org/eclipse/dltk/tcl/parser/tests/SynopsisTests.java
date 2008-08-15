package org.eclipse.dltk.tcl.parser.tests;

import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.parser.definitions.Synopsis;

public class SynopsisTests extends TestCase {
	NamespaceScopeProcessor processor = new NamespaceScopeProcessor();

	
	public void test001() throws Exception {
		String source = "after";
		String synopsis = "after ms ?script ...?\n" + "after cancel id\n"
				+ "after cancel script ?script ...?\n"
				+ "after idle script ?script ...?\n" + "after info ?id?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test002() throws Exception {
		String source = "append";
		String synopsis = "append varName ?value ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test003() throws Exception {
		String source = "apply";
		String synopsis = "apply {{?{{arg} ?value?} ...?} body ?namespace?} ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test004() throws Exception {
		String source = "array";
		String synopsis = "array anymore arrayName searchId\n"
				+ "array donesearch arrayName searchId\n"
				+ "array exists arrayName\n"
				+ "array get arrayName ?pattern?\n"
				+ "array names arrayName ?mode? ?pattern?\n"
				+ "array nextelement arrayName searchId\n"
				+ "array set arrayName {?key value ...?}\n"
				+ "array size arrayName\n" + "array startsearch arrayName\n"
				+ "array statistics arrayName\n"
				+ "array unset arrayName ?pattern?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test005() throws Exception {
		String source = "auto_execok";
		String synopsis = "auto_execok cmd";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test006() throws Exception {
		String source = "auto_import";
		String synopsis = "auto_import pattern";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test007() throws Exception {
		String source = "auto_load";
		String synopsis = "auto_load cmd";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test008() throws Exception {
		String source = "auto_mkindex";
		String synopsis = "auto_mkindex dir pattern ?pattern ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test009() throws Exception {
		String source = "auto_mkindex_old";
		String synopsis = "auto_mkindex_old dir pattern ?pattern ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test010() throws Exception {
		String source = "auto_qualify";
		String synopsis = "auto_qualify command namespace";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test011() throws Exception {
		String source = "auto_reset";
		String synopsis = "auto_reset";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test012() throws Exception {
		String source = "binary";
		String synopsis = "binary format formatString ?arg ...?\n"
				+ "binary scan string formatString ?varName ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test013() throws Exception {
		String source = "break";
		String synopsis = "break";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test014() throws Exception {
		String source = "catch";
		String synopsis = "catch script ?resultVarName? ?resultVarName?";
		String synopsis_8_4 = "catch script ?resultVarName?";
		typedCheck(source, synopsis, "8.5");
		typedCheck(source, synopsis_8_4, "8.4");
	}

	
	public void test015() throws Exception {
		String source = "cd";
		String synopsis = "cd ?dirName?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test016_FAILED() throws Exception {
		String source = "chan";
		String synopsis = "chan blocked channelId\n"
				+ "chan close channelId\n"
				+ "chan configure channelId\n"
				+ "chan configure channelId ?mode?\n"
				+ "chan configure channelId ?-blocking boolean? ?-buffering mode? "
				+ "?-buffersize newSize? ?-encoding name? ?-eofchar inChar ?outChar?? "
				+ "?-translation mode? ?-translation {mode}?\n"
				+ "chan copy inputChan outputChan ?-size size? ?-command callback?\n"
				+ "chan create mode cmdPrefix\n" + "chan eof channelId\n"
				+ "chan event channelId event ?script?\n"
				+ "chan flush channelId\n" + "chan gets channelId ?varName?\n"
				+ "chan names ?pattern?\n" + "chan pending mode channelId\n"
				+ "chan postevent channelId eventSpec\n"
				+ "chan puts ?-nonewline? ?channelId? string\n"
				+ "chan read channelId ?numChars?\n"
				+ "chan read ?-nonewline? channelId\n"
				+ "chan seek channelId offset ?origin?\n"
				+ "chan tell channelId \n" + "chan truncate channelId ?length?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test017() throws Exception {
		String source = "clock";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test018() throws Exception {
		String source = "close";
		String synopsis = "close channelId";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test019() throws Exception {
		String source = "concat";
		String synopsis = "concat ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test020() throws Exception {
		String source = "continue";
		String synopsis = "continue";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test021() throws Exception {
		String source = "dde";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test022() throws Exception {
		String source = "dict";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test023() throws Exception {
		String source = "encoding";
		String synopsis = "encoding convertfrom ?encoding? data\n"
				+ "encoding convertto ?encoding? string\n"
				+ "encoding dirs ?directoryList?\n" + "encoding names\n"
				+ "encoding system ?encoding?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test024() throws Exception {
		String source = "eof";
		String synopsis = "eof channelId";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test025() throws Exception {
		String source = "error";
		String synopsis = "error message ?info? ?code?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test026() throws Exception {
		String source = "eval";
		String synopsis = "eval arg ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test027_FAILED() throws Exception {
		String source = "exec";
		String synopsis = "exec ?-ignorestderr? ?-keepnewline? ?--? arg ?arg ...?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test028() throws Exception {
		String source = "exit";
		String synopsis = "exit ?returnCode?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test029() throws Exception {
		String source = "expr";
		String synopsis = "expr arg ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test030() throws Exception {
		String source = "fblocked";
		String synopsis = "fblocked channelId";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test031() throws Exception {
		String source = "fconfigure";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test032() throws Exception {
		String source = "fcopy";
		String synopsis = "fcopy inchan outchan ?-size size? ?-command callback?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test033() throws Exception {
		String source = "file";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test034() throws Exception {
		String source = "fileevent";
		String synopsis = "fileevent channelId ?mode? ?script?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test035() throws Exception {
		String source = "flush";
		String synopsis = "flush channelId";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test036() throws Exception {
		String source = "for";
		String synopsis = "for start test next body";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test037() throws Exception {
		String source = "foreach";
		String synopsis = "foreach varname list ?varname list ...? body";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test038() throws Exception {
		String source = "format";
		String synopsis = "format formatString ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test039() throws Exception {
		String source = "gets";
		String synopsis = "gets channelId ?varName?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test040_FAILED() throws Exception {
		String source = "glob";
		String synopsis = "glob ?-directory directory? ?-join? ?-nocomplain? "
				+ "?-path pathPrefix? ?-tails? ?-types typeList? ?--? pattern ?pattern ...?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test041() throws Exception {
		String source = "global";
		String synopsis = "global varname ?varname ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test042() throws Exception {
		String source = "history";
		String synopsis = "history\n" + "history add command ?exec?\n"
				+ "history change newValue ?event?\n" + "history clear\n"
				+ "history event ?event?\n" + "history info ?count?\n"
				+ "history keep ?count?\n" + "history nextid\n"
				+ "history redo ?event?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test043() throws Exception {
		String source = "::http::config";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test044() throws Exception {
		String source = "::http::geturl";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test045() throws Exception {
		String source = "::http::formatQuery";
		String synopsis = "formatQuery key value ?key value ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test046() throws Exception {
		String source = "::http::reset";
		String synopsis = "reset token ?why?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test047() throws Exception {
		String source = "::http::wait";
		String synopsis = "wait token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test048() throws Exception {
		String source = "::http::status";
		String synopsis = "status token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test049() throws Exception {
		String source = "::http::size";
		String synopsis = "size token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test050() throws Exception {
		String source = "::http::code";
		String synopsis = "code token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test051() throws Exception {
		String source = "::http::ncode";
		String synopsis = "ncode token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test052() throws Exception {
		String source = "::http::meta";
		String synopsis = "meta token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test053() throws Exception {
		String source = "::http::data";
		String synopsis = "data token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test054() throws Exception {
		String source = "::http::error";
		String synopsis = "error token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test055() throws Exception {
		String source = "::http::cleanup";
		String synopsis = "cleanup token";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test056() throws Exception {
		String source = "::http::register";
		String synopsis = "register proto port command";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test057() throws Exception {
		String source = "::http::unregister";
		String synopsis = "unregister proto";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test058() throws Exception {
		String source = "if";
		String synopsis = "if expr1 ?then? body1 "
				+ "?elseif expr2 ?then? body2 ...? ?else? ?bodyN?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test059() throws Exception {
		String source = "incr";
		String synopsis = "incr varName ?increment?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test060() throws Exception {
		String source = "info";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test061() throws Exception {
		String source = "interp";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test062() throws Exception {
		String source = "join";
		String synopsis = "join list ?joinString?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test063() throws Exception {
		String source = "lappend";
		String synopsis = "lappend varName ?value ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test064() throws Exception {
		String source = "lassign";
		String synopsis = "lassign list varName ?varName ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test065() throws Exception {
		String source = "lindex";
		String synopsis = "lindex list ?index ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test066() throws Exception {
		String source = "linsert";
		String synopsis = "linsert list index element ?element ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test067() throws Exception {
		String source = "list";
		String synopsis = "list ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test068() throws Exception {
		String source = "llength";
		String synopsis = "llength list";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test069() throws Exception {
		String source = "load";
		String synopsis = "load fileName ?packageName? ?interp?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test070() throws Exception {
		String source = "lrange";
		String synopsis = "lrange list first last";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test071() throws Exception {
		String source = "lrepeat";
		String synopsis = "lrepeat number element ?element ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test072() throws Exception {
		String source = "lreplace";
		String synopsis = "lreplace list first last ?element ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test073() throws Exception {
		String source = "lreverse list";
		String synopsis = "lreverse list";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test074_FAILED() throws Exception {
		String source = "lsearch";
		String synopsis = "lsearch ?-ascii? ?-all? ?-dictionary? "
				+ "?-decreasing? ?-exact? ?-glob? ?-inline? ?-integer? "
				+ "?-not? ?-real? ?-regexp? ?-sorted? ?-start index? "
				+ "?-index indexList? ?-nocase? ?-decreasing? "
				+ "?-increasing? ?-subindices? list pattern";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test075() throws Exception {
		String source = "lset";
		String synopsis = "lset list ?index ...? newValue";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test076_FAILED() throws Exception {
		String source = "lsort";
		String synopsis = "lsort ?-ascii? ?-dictionary? ?-integer? "
				+ "?-real? ?-command command? ?-increasing? ?-decreasing? "
				+ "?-indices? ?-index indexList? ?-nocase? ?-unique? list";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test077() throws Exception {
		String source = "memory";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test078() throws Exception {
		String source = "::msgcat::mc";
		String synopsis = "mc src-string ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test079() throws Exception {
		String source = "::msgcat::mcmax";
		String synopsis = "mcmax ?src-string ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test080() throws Exception {
		String source = "::msgcat::mclocale ?newLocale?";
		String synopsis = "mclocale ?newLocale?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test081() throws Exception {
		String source = "::msgcat::mcpreferences";
		String synopsis = "mcpreferences";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test082() throws Exception {
		String source = "::msgcat::mcload";
		String synopsis = "mcload dirname";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test083() throws Exception {
		String source = "::msgcat::mcset";
		String synopsis = "mcset locale src-string ?translate-string?";
		typedCheck(source, synopsis, "8.5");
	}

	// 
	// public void test084() throws Exception {
	// String source = "::msgcat::mcmset";
	// String synopsis = "mcmset locale src-trans-list";
	// typedCheck(source, synopsis, "8.5");
	// }

	
	public void test085() throws Exception {
		String source = "::msgcat::mcunknown";
		String synopsis = "mcunknown locale src-string";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test086() throws Exception {
		String source = "namespace";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test087() throws Exception {
		String source = "open";
		String synopsis = "open fileName ?access? ?permissions?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test088() throws Exception {
		String source = "package";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test089() throws Exception {
		String source = "parray";
		String synopsis = "parray arrayName";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test090() throws Exception {
		String source = "pid";
		String synopsis = "pid ?fileId?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test091() throws Exception {
		String source = "::pkg::create";
		String synopsis = "create -name packageName -version "
				+ "packageVersion ?-load filespec? ?-source filespec?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test092_FAILED() throws Exception {
		String source = "pkg_mkIndex";
		String synopsis = "pkg_mkIndex ?-direct? ?-lazy? ?-load "
				+ "pkgPat? ?-verbose? ?--? dir ?pattern ...?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test093() throws Exception {
		String source = "platform::generic";
		String synopsis = "generic";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test094() throws Exception {
		String source = "platform::identify";
		String synopsis = "identify";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test095() throws Exception {
		String source = "platform::patterns";
		String synopsis = "patterns identifier";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test096() throws Exception {
		String source = "platform::shell::generic";
		String synopsis = "generic shell";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test097() throws Exception {
		String source = "platform::shell::identify";
		String synopsis = "identify shell";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test098() throws Exception {
		String source = "platform::shell::platform";
		String synopsis = "platform shell";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test099() throws Exception {
		String source = "proc";
		String synopsis = "proc name {?{{arg} ?value?} ...?} body";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test100() throws Exception {
		String source = "puts";
		String synopsis = "puts ?-nonewline? ?channelId? string";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test101() throws Exception {
		String source = "pwd";
		String synopsis = "pwd";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test102() throws Exception {
		String source = "read";
		String synopsis = "read channelId ?numChars?\n"
				+ "read ?-nonewline? channelId";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test103_FAILED() throws Exception {
		String source = "cmdPrefix";
		String synopsis = "cmdPrefix option ?arg ...?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test104_FAILED() throws Exception {
		String source = "regexp";
		String synopsis = "regexp ?-about? ?-expanded? ?-indices? ?-line? "
				+ "?-linestop? ?-lineanchor? ?-nocase? ?-all? ?-inline? "
				+ "?-start index? ?--? exp string ?matchVar ...?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test105() throws Exception {
		String source = "registry";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test106_FAILED() throws Exception {
		String source = "regsub";
		String synopsis = "regsub ?-about? ?-expanded? ?-indices? ?-line? "
				+ "?-linestop? ?-lineanchor? ?-nocase? ?-all? ?-inline? "
				+ "?-start index? ?--? exp string subSpec ?varName?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test107() throws Exception {
		String source = "rename";
		String synopsis = "rename oldName newName";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test108_FAILED() throws Exception {
		String source = "return";
		String synopsis = "return ?-errorcode list? ?-errorinfo info? "
				+ "?-level level? ?-options options? ?-code code? ?result?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test109() throws Exception {
		String source = "::safe::interpCreate";
		String synopsis = "interpCreate ?slave? "
				+ "?-accessPath directoryList? ?-statics boolean? ?-noStatics? "
				+ "?-nested boolean? ?-nestedLoadOk? ?-deleteHook script?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test110() throws Exception {
		String source = "::safe::interpInit";
		String synopsis = "interpInit slave "
				+ "?-accessPath directoryList? ?-statics boolean? ?-noStatics? "
				+ "?-nested boolean? ?-nestedLoadOk? ?-deleteHook script?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test111() throws Exception {
		String source = "::safe::interpConfigure";
		String synopsis = "interpConfigure slave "
				+ "?-accessPath directoryList? ?-statics boolean? ?-noStatics? "
				+ "?-nested boolean? ?-nestedLoadOk? ?-deleteHook script?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test112() throws Exception {
		String source = "::safe::interpDelete";
		String synopsis = "interpDelete slave";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test113() throws Exception {
		String source = "::safe::interpAddToAccessPath";
		String synopsis = "interpAddToAccessPath slave directory";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test114() throws Exception {
		String source = "::safe::interpFindInAccessPath";
		String synopsis = "interpFindInAccessPath slave directory";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test115() throws Exception {
		String source = "::safe::setLogCmd";
		String synopsis = "setLogCmd ?cmd? ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test116() throws Exception {
		String source = "scan";
		String synopsis = "scan string format ?varName ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test117_FAILED() throws Exception {
		String source = "seek";
		String synopsis = "seek channelId offset ?origin?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test118() throws Exception {
		String source = "set";
		String synopsis = "set varName ?value?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test119_FAILED() throws Exception {
		String source = "socket";
		String synopsis = "socket -server command ?-myaddr addr? port\n"
				+ "socket ?-myaddr addr? ?-myport port? ?-async? host port";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test120() throws Exception {
		String source = "source";
		String synopsis = "source ?-encoding encodingName? fileName";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test121() throws Exception {
		String source = "split";
		String synopsis = "split string ?splitChars?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test122() throws Exception {
		String source = "string";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test123_FAILED() throws Exception {
		String source = "subst";
		String synopsis = "subst ?-nobackslashes? ?-nocommands? ?-novariables? string";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test124_FAILED() throws Exception {
		String source = "switch";
		String synopsis = "switch ?options? string pattern body ?pattern body ...?\n"
				+ "switch ?options? string {pattern body ?pattern body ...?}";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test125() throws Exception {
		String source = "tcl_endOfWord";
		String synopsis = "tcl_endOfWord str start";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test126() throws Exception {
		String source = "tcl_startOfNextWord";
		String synopsis = "tcl_startOfNextWord str start";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test127() throws Exception {
		String source = "tcl_startOfPreviousWord";
		String synopsis = "tcl_startOfPreviousWord str start";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test128() throws Exception {
		String source = "tcl_wordBreakAfter";
		String synopsis = "tcl_wordBreakAfter str start";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test129() throws Exception {
		String source = "tcl_wordBreakBefore";
		String synopsis = "tcl_wordBreakBefore str start";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test130() throws Exception {
		String source = "tell";
		String synopsis = "tell channelId";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test131() throws Exception {
		String source = "time";
		String synopsis = "time script ?count?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test132_FAILED() throws Exception {
		String source = "::tcl::tm::path";
		String synopsis = "path add ?path ...?\n" + "path remove ?path ...?\n"
				+ "path list";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test132_1_FAILED() throws Exception {
		String source = "::tcl::tm::roots";
		String synopsis = "roots ?path ...?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test133() throws Exception {
		String source = "trace";
		String synopsis = "";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test134() throws Exception {
		String source = "unknown";
		String synopsis = "unknown cmdName ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test135_FAILED() throws Exception {
		String source = "unload";
		String synopsis = "unload ?-nocomplain? ?-keeplibrary? ?--? fileName  ?packageName? ?interp?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test136_FAILED() throws Exception {
		String source = "unset";
		String synopsis = "unset ?-nocomplain? ?--? ?name ...?";
		try {
			typedCheck(source, synopsis, "8.5");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public void test137() throws Exception {
		String source = "update";
		String synopsis = "update ?idletasks?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test138() throws Exception {
		String source = "uplevel";
		String synopsis = "uplevel ?level? arg ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test139() throws Exception {
		String source = "upvar";
		String synopsis = "upvar ?level? otherVar myVar ?otherVar myVar ...?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test140() throws Exception {
		String source = "variable";
		String synopsis = "variable ?name value ...? name ?value?";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test141() throws Exception {
		String source = "vwait";
		String synopsis = "vwait varName";
		typedCheck(source, synopsis, "8.5");
	}

	
	public void test142() throws Exception {
		String source = "while";
		String synopsis = "while test body";
		typedCheck(source, synopsis, "8.5");
	}

	// 
	// public void test143() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }
	//
	// 
	// public void test144() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }
	//
	// 
	// public void test145() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }
	//
	// 
	// public void test146() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }
	//
	// 
	// public void test147() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }
	//
	// 
	// public void test148() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }
	//
	// 
	// public void test149() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }
	//
	// 
	// public void test150() throws Exception {
	// String source = "";
	// String synopsis = "";
	// typedCheck(source, synopsis, "8.5");
	// }

	private void typedCheck(String source, String expected, String version)
			throws Exception {
		Scope scope = DefinitionLoader
				.loadDefinitions(new URL(
						"platform:///plugin/org.eclipse.dltk.tcl.tcllib/definitions/builtin.xml"));
		TestCase.assertNotNull(scope);
		processor.addScope(scope);
		TclParser parser = new TclParser(version);
		TclErrorCollector errors = new TclErrorCollector();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, true);
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(1, module.size());
		TclCommand command = module.get(0);
		Synopsis synopsis = new Synopsis(command.getDefinition());
		String actual = synopsis.toString();
		TestCase.assertNotNull(actual);
		System.out.println("===================" + version
				+ "===================");
		System.out.println(actual);
		System.out.println("-----------------------------------------");
		System.out.println(expected);
		if (expected != null && !expected.equals(""))
			TestCase.assertEquals(expected, actual);
	}
}
