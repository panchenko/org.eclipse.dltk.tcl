package org.eclipse.dltk.xotcl.core.tests.parser;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.compiler.env.ModuleSource;
import org.eclipse.dltk.tcl.core.ITclSourceParser;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.internal.parser.TclSourceParserFactory;
import org.eclipse.dltk.utils.TextUtils;

public class XOTclParserUtilTests extends TestCase {

	public void testParseUtil001() throws Throwable {
		String[] content = {
				"namespace eval c  {",
				"}",
				"namespace eval a {",
				"	namespace eval b {",
				"		proc ::c::d { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(2, levelsTo.size());
	}

	public void testParseUtil002() throws Throwable {
		String[] content = {
				"namespace eval c  {",
				"}",
				"namespace eval a {",
				"	namespace eval b {",
				"		proc ::c::d { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(2, levelsTo.size());
	}

	public void testParseUtil003() throws Throwable {
		String[] content = {
				"namespace eval c  {",
				"	namespace eval d {",
				"	}",
				"}",
				"namespace eval a {",
				"	namespace eval b {",
				"		proc ::c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(3, levelsTo.size());
	}

	public void testParseUtil004() throws Throwable {
		String[] content = {
				"namespace eval c  {",
				"	namespace eval d {",
				"	}",
				"}",
				"namespace eval a {",
				"	namespace eval b {",
				"		proc ::c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(3, levelsTo.size());
	}

	public void testParseUtil005() throws Throwable {
		String[] content = {
				"namespace eval c  {",
				"	namespace eval d {",
				"	}",
				"}",
				"namespace eval a {",
				"	namespace eval c {",
				"	}",
				"	namespace eval b {",
				"		proc ::c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(3, levelsTo.size());
	}

	public void testParseUtil006() throws Throwable {
		String[] content = {
				"namespace eval c  {",
				"	namespace eval d {",
				"	}",
				"}",
				"namespace eval a {",
				"	namespace eval c {",
				"		namespace eval d {",
				"		}",
				"	}",
				"	namespace eval b {",
				"		proc ::c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(3, levelsTo.size());
	}

	public void testParseUtil007() throws Throwable {
		String[] content = {
				"namespace eval c::d  {",
				"}",
				"namespace eval a {",
				"	namespace eval b {",
				"		proc ::c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c::d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(2, levelsTo.size());
	}

	public void testParseUtil008() throws Throwable {
		String[] content = {
				"namespace eval c::d  {",
				"}",
				"namespace eval a {",
				"	namespace eval b {",
				"		namespace eval c::d {",
				"		}",
				"		proc c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c::d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(4, levelsTo.size());
	}

	public void testParseUtil009() throws Throwable {
		String[] content = {
				"namespace eval c::d  {",
				"}",
				"namespace eval a {",
				"	namespace eval c::d {",
				"	}",
				"	namespace eval b {",
				"		namespace eval c::d {",
				"		}",
				"		proc c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c::d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(4, levelsTo.size());
	}

	public void testParseUtil010() throws Throwable {
		String[] content = {
				"namespace eval c::d  {",
				"}",
				"namespace eval a {",
				"	namespace eval c {",
				"		namespace eval d {",
				"		}",
				"	}",
				"	namespace eval c::d {",
				"	}",
				"	namespace eval b {",
				"		namespace eval c::d {",
				"		}",
				"		proc c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c::d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(4, levelsTo.size());
	}

	public void testParseUtil011() throws Throwable {
		String[] content = {
				"namespace eval c::d  {" + "}",
				"namespace eval a {",
				"	namespace eval c {",
				"		namespace eval d {",
				"		}",
				"	}",
				"	namespace eval c::d {",
				"	}",
				"	namespace eval b {",
				"		namespace eval c::d {",
				"		}",
				"		proc ::c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c::d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(2, levelsTo.size());
	}

	/**
	 * @throws Throwable
	 */
	public void testParseUtil012() throws Throwable {
		String[] content = {
				"namespace eval c::d  {",
				"}",
				"namespace eval a {",
				"	namespace eval c {",
				"		namespace eval d {",
				"		}",
				"	}",
				"	namespace eval c::d {",
				"	}",
				"	namespace eval b {",
				"		namespace eval c::d {",
				"		}",
				"		proc ::a::c::d::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::a::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(4, levelsTo.size());
	}

	public void testParseUtil013() throws Throwable {
		String[] content = {
				"namespace eval c::d  {",
				"}",
				"namespace eval a {",
				"	namespace eval c {",
				"		namespace eval d {",
				"		}",
				"	}",
				"	namespace eval c::d {",
				"	}",
				"	namespace eval b {",
				"		namespace eval c::d {",
				"		}",
				"		proc ::a::b::c::d::q { } {",
				"		}",
				"	}" + "}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::a::b::c::d::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("c::d", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(4, levelsTo.size());
	}

	public void testParseUtil014() throws Throwable {
		String[] content = {
				"namespace eval c::d  {",
				"	namespace eval a {",
				"		namespace eval gamma::delta {",
				"		}",
				"	}",
				"}",
				"namespace eval a {",
				"		proc ::c::d::a::gamma::delta::q { } {",
				"		}",
				"	}",
				"}" };
		ModuleDeclaration module = this.parser(content);
		ASTNode nodes[] = findNodeByName(module, "::c::d::a::gamma::delta::q");
		assertEquals(1, nodes.length);
		ASTNode node = nodes[0];
		TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(module,
				node);
		assertNotNull(type);
		assertEquals("gamma::delta", type.getName());
		List levelsTo = TclParseUtil.findLevelsTo(module, type);
		assertEquals(4, levelsTo.size());
	}

	private ASTNode[] findNodeByName(ModuleDeclaration module, final String name)
			throws Exception {
		final List results = new ArrayList();
		module.traverse(new ASTVisitor() {
			public boolean endvisit(TypeDeclaration s) throws Exception {
				if (s.getName().equals(name)) {
					return results.add(s);
				}
				return super.endvisit(s);
			}

			public boolean visit(MethodDeclaration s) throws Exception {
				if (s.getName().equals(name)) {
					return results.add(s);
				}
				return super.visit(s);
			}
		});
		return (ASTNode[]) results.toArray(new ASTNode[results.size()]);
	}

	private ModuleDeclaration parser(String[] content) {
		ITclSourceParser parser = new TclSourceParserFactory()
				.createSourceParser();
		ModuleDeclaration module = parser.parse(new ModuleSource(join(content)),
				null);
		assertNotNull(module);
		return module;
	}

	public static String join(String[] content) {
		return TextUtils.join(content, '\n') + '\n';
	}
}
