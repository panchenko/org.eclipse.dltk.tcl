package org.eclipse.dltk.tcl.validators.tests.packages;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;

import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.validators.packages.PackageSourceCollector;

public class TclSourcePackagesTests extends AbstractModelTests {

	public TclSourcePackagesTests(String name) {
		super("org.eclipse.dltk.tcl.core.tests", name);
	}

	public TclSourcePackagesTests(String testProjectName, String name) {
		super(testProjectName, name);
	}

	public static Test suite() {
		return new Suite(TclSourcePackagesTests.class);
	}

	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

	public void testSource001() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("source ../alfa.tcl", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getSourced().size());
		TestCase.assertEquals("../alfa.tcl", info.getSourced().get(0)
				.getValue());
	}

	public void testSource002() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("source \"../alfa.tcl\"", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getSourced().size());
		TestCase.assertEquals("../alfa.tcl", info.getSourced().get(0)
				.getValue());
	}

	public void testSource003() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("source {../alfa.tcl}", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getSourced().size());
		TestCase.assertEquals("../alfa.tcl", info.getSourced().get(0)
				.getValue());
	}

	public void testSource004() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("source $dir/alfa.tcl", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getSourced().size());
		TestCase.assertEquals("$dir/alfa.tcl", info.getSourced().get(0)
				.getValue());
	}
	public void testSource005() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("source [file join $dir alfa.tcl]", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getSourced().size());
		TestCase.assertEquals("[file join $dir alfa.tcl]", info.getSourced().get(0)
				.getValue());
	}

	public void testPackage001() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("package require alfa", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getRequired().size());
		TestCase.assertEquals("alfa", info.getRequired().get(0).getValue());
	}

	public void testPackage002() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("package provide alfa", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getProvided().size());
		TestCase.assertEquals("alfa", info.getProvided().get(0).getValue());
	}

	public void testPackage003() throws Throwable {
		TclParser parser = new TclParser();
		List<TclCommand> decls = parser.parse("package ifneeded alfa", null,
				DefinitionManager.getInstance().createProcessor());
		PackageSourceCollector collector = new PackageSourceCollector();
		collector.process(decls, null);
		TclModuleInfo info = collector.getCurrentModuleInfo();
		TestCase.assertEquals(1, info.getProvided().size());
		TestCase.assertEquals("alfa", info.getProvided().get(0).getValue());
	}
}
