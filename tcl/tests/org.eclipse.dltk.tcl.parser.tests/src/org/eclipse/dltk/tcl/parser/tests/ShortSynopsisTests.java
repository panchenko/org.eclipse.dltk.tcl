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
import org.eclipse.dltk.tcl.parser.definitions.SynopsisBuilder;

public class ShortSynopsisTests extends TestCase {
	NamespaceScopeProcessor processor = new NamespaceScopeProcessor();

	public void test001() throws Exception {
		String source = "after";
		String synopsis = "after ms ?script ...?\n" + "after cancel id\n"
				+ "after cancel script ?script ...?\n"
				+ "after idle script ?script ...?\n" + "after info ?id?";
		typedCheck(source, synopsis, "8.5");
	}

	public void test002() throws Exception {
		String source = "after cancel puts lala";
		String synopsis = "after cancel script ?script ...?";
		typedCheck(source, synopsis, "8.5");
	}

	public void test003() throws Exception {
		String source = "append";
		String synopsis = "append varName ?value ...?";
		typedCheck(source, synopsis, "8.5");
	}

	public void test004() throws Exception {
		String source = "array names arrayName -exact";
		String synopsis = "array names arrayName ?[-exact|-glob|-regexp]? ?pattern?";
		typedCheck(source, synopsis, "8.5");
	}

	public void test005() throws Exception {
		String source = "chan read channelId";
		String synopsis = "chan read channelId ?numChars?";
		typedCheck(source, synopsis, "8.5");
	}

	public void test006() throws Exception {
		String source = "apply {{a {b 4}} {puts $a;puts $b}} 3 7";
		String synopsis = "apply {args body ?namespace?} ?arg ...?";
		typedCheck(source, synopsis, "8.5");
	}

	public void test007() throws Exception {
		String source = "chan configure channelId -blocking 1";
		String synopsis = "chan configure channelId ?option? ?value? ?option value ...?";
		typedCheck(source, synopsis, "8.5");
	}

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
		SynopsisBuilder synopsis = new SynopsisBuilder(command);
		String actual = synopsis.toString();
		TestCase.assertNotNull(actual);
		System.out.println("===================" + version
				+ "===================");
		System.out.println(actual);
		System.out.println("-----------------------------------------");
		System.out.println(expected);
		TestCase.assertNotNull(expected);
		TestCase.assertFalse(expected.equals(""));
		TestCase.assertEquals(expected, actual);
	}
}
