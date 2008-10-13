package org.eclipse.dltk.tcl.parser.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclError;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class ErrorReportingTests extends TestCase {
	NamespaceScopeProcessor processor = new NamespaceScopeProcessor();

	public void test001() throws Exception {
		String source = "namespace eval";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test002() throws Exception {
		String source = "eval";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test003() throws Exception {
		String source = "fcopy inchan";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test004() throws Exception {
		String source = "fcopy inchan outchan -size";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test005() throws Exception {
		String source = "fcopy inchan outchan -size -command callback";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.INVALID_ARGUMENT_VALUE);
		typedCheck(source, errorCodes);
	}

	public void test006() throws Exception {
		String source = "namespace eval nms {puts 3}";
		List<Integer> errorCodes = new ArrayList<Integer>();
		typedCheck(source, errorCodes);
	}

	public void test007() throws Exception {
		String source = "switch alpha c1 history c2 history c3";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.EXTRA_ARGUMENTS);
		typedCheck(source, errorCodes);
	}

	public void test008() throws Exception {
		String source = "namespace";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test009() throws Exception {
		String source = "foreach";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test010() throws Exception {
		String source = "read $in";
		List<Integer> errorCodes = new ArrayList<Integer>();
		typedCheck(source, errorCodes);
	}

	public void test011() throws Exception {
		String source = "string is . .";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.INVALID_ARGUMENT_VALUE);
		typedCheck(source, errorCodes);
	}

	public void test012() throws Exception {
		String source = "fcopy inchan outchan -size -size 34";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.EXTRA_ARGUMENTS);
		typedCheck(source, errorCodes);
	}

	public void test013() throws Exception {
		String source = "subst -nocommands -novariables $val";
		List<Integer> errorCodes = new ArrayList<Integer>();
		typedCheck(source, errorCodes);
	}

	public void test014() throws Exception {
		String source = "after";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test015() throws Exception {
		String source = "linsert list";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test016() throws Exception {
		String source = "foreach";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test017() throws Exception {
		String source = "after c";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	public void test018() throws Exception {
		String source = "after k";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.INVALID_ARGUMENT_VALUE);
		typedCheck(source, errorCodes);
	}

	public void test019() throws Exception {
		String source = "registry set $key $value $data $mod";
		List<Integer> errorCodes = new ArrayList<Integer>();
		typedCheck(source, errorCodes);
	}

	public void test020() throws Exception {
		String source = "linsert list f";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.INVALID_ARGUMENT_VALUE);
		typedCheck(source, errorCodes);
	}

	public void test021() throws Exception {
		String source = "fconfigure stdin";
		List<Integer> errorCodes = new ArrayList<Integer>();
		typedCheck(source, errorCodes);
	}

	public void test022() throws Exception {
		String source = "socket $host";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ITclErrorReporter.MISSING_ARGUMENT);
		typedCheck(source, errorCodes);
	}

	// public void test023() throws Exception {
	// String source = "switch a {#(v {puts lala}}";
	// List<Integer> errorCodes = new ArrayList<Integer>();
	// typedCheck(source, errorCodes);
	// }

	// public void test021() throws Exception {
	// String source = "proc {{arg def1 def2}} {puts $arg}";
	// List<Integer> errorCodes = new ArrayList<Integer>();
	// errorCodes.add(ITclErrorReporter.INVALID_ARGUMENT_VALUE);
	// typedCheck(source, errorCodes);
	// }

	// public void test022() throws Exception {
	// String source = "proc {{arg def1 def2}} {{arg def1 def2}}";
	// List<Integer> errorCodes = new ArrayList<Integer>();
	// errorCodes.add(ITclErrorReporter.INVALID_ARGUMENT_VALUE);
	// typedCheck(source, errorCodes);
	// }

	private void typedCheck(String source, List<Integer> errorCodes)
			throws Exception {
		processor = DefinitionManager.getInstance().createProcessor();
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[2];
		System.out.println("%%%%%%%%%%%%%%%%Test:" + element.getMethodName());
		TclParser parser = new TclParser("8.4");
		TclErrorCollector errors = new TclErrorCollector();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, true);
		parser.parse(source, errors, processor);
		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}
		TestCase.assertEquals(errorCodes.size(), errors.getCount());
		int count = 0;
		for (int code : errorCodes) {
			for (TclError tclError : errors.getErrors()) {
				if (tclError.getCode() == code) {
					count++;
					break;
				}
			}
		}
		TestCase.assertEquals(errorCodes.size(), count);
	}
}
