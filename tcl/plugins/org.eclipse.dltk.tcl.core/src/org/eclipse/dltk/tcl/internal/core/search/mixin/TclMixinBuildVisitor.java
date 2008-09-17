package org.eclipse.dltk.tcl.internal.core.search.mixin;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.mixin.IMixinRequestor;
import org.eclipse.dltk.core.mixin.IMixinRequestor.ElementInfo;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.extensions.IMixinBuildVisitorExtension;
import org.eclipse.dltk.tcl.internal.core.TclExtensionManager;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclField;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclNamespace;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclNamespaceImport;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclProc;

public class TclMixinBuildVisitor extends ASTVisitor {
	protected boolean signature = false;
	protected ISourceModule sourceModule;
	protected ModuleDeclaration moduleDeclaration;
	protected IMixinRequestor requestor;
	protected Stack namespaceNames = new Stack();

	protected IMixinBuildVisitorExtension[] extensions;

	public TclMixinBuildVisitor(ModuleDeclaration moduleDeclaration,
			ISourceModule module, boolean signature, IMixinRequestor requestor) {
		this.signature = signature;
		this.sourceModule = module;
		this.moduleDeclaration = moduleDeclaration;
		this.requestor = requestor;
		this.extensions = TclExtensionManager.getDefault()
				.getMixinVisitorExtensions();
	}

	public boolean endvisit(TypeDeclaration s) throws Exception {
		// Skip type names with code execution
		if (s.getName().indexOf('[') != -1 || s.getName().indexOf('$') != -1) {
			return true;
		}
		this.namespaceNames.pop();
		return true;
	}

	public String getKeyFromLevels(List nodes) {
		return TclParseUtil.getElementFQN(nodes,
				IMixinRequestor.MIXIN_NAME_SEPARATOR, this.moduleDeclaration);
	}

	public String getNamespacePrefix() {
		StringBuffer prefix = new StringBuffer();
		for (Iterator iterator = this.namespaceNames.iterator(); iterator
				.hasNext();) {
			TypeDeclaration ns = (TypeDeclaration) iterator.next();
			String name = ns.getName();
			if (name.endsWith("::")) {
				name = name.substring(0, name.length() - 2);
			}
			if (ns.getName().startsWith("::")) {
				// Remove all previous
				prefix.delete(0, prefix.length());
				name = name.substring(2);
			}
			prefix.append(tclNameToKey(name)
					+ IMixinRequestor.MIXIN_NAME_SEPARATOR);
		}
		return prefix.toString();
	}

	public String getRealNamespacePrefix() {
		StringBuffer prefix = new StringBuffer();
		for (Iterator iterator = this.namespaceNames.iterator(); iterator
				.hasNext();) {
			TypeDeclaration ns = (TypeDeclaration) iterator.next();
			String name = ns.getName();
			if (name.endsWith("::")) {
				name = name.substring(0, name.length() - 2);
			}
			if (ns.getName().startsWith("::")) {
				// Remove all previous
				prefix.delete(0, prefix.length());
				name = name.substring(2);
			}
			prefix.append(name + "::");
		}
		String result = prefix.toString();
		if (result.startsWith("::")) {
			result = result.substring(2);
		}
		if (result.endsWith("::")) {
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}

	public String tclNameToKey(String name) {
		return TclParseUtil.tclNameTo(name,
				IMixinRequestor.MIXIN_NAME_SEPARATOR);
	}

	public boolean visit(MethodDeclaration s) throws Exception {
		for (int i = 0; i < this.extensions.length; i++) {
			if (this.extensions[i].visit(s, this)) {
				return true;
			}
		}
		// Other methods visit
		ElementInfo info = new ElementInfo();
		String name = s.getName();
		if (name.startsWith("::")) {
			info.key = tclNameToKey(name.substring(2));
		} else {
			if (name.indexOf("::") == -1) {
				info.key = this.getNamespacePrefix() + tclNameToKey(name);
			} else {
				TypeDeclaration type = TclParseUtil.findTclTypeDeclarationFrom(
						this.moduleDeclaration, s);
				if (type != null) {
					List levels = TclParseUtil.findLevelsTo(
							this.moduleDeclaration, type);
					String[] mName = name.split("::");
					info.key = this.getKeyFromLevels(levels)
							+ IMixinRequestor.MIXIN_NAME_SEPARATOR
							+ mName[mName.length - 1];
				} else { // case of addint to unknown namespace.
					info.key = tclNameToKey(name);
				}
			}
		}
		if (info.key != null) {
			if (signature) {
				info.object = new TclProc();
			}
			this.requestor.reportElement(info);
		}
		// System.out.println("Report proc or instproc:" + info.key);
		return true;
	}

	public boolean visit(Statement s) throws Exception {
		for (int i = 0; i < this.extensions.length; i++) {
			if (this.extensions[i].visit(s, this)) {
				return false;
			}
		}
		if (s instanceof TclStatement) {
			processNamespaceImport(s);
		}
		if (s instanceof FieldDeclaration) {
			FieldDeclaration field = (FieldDeclaration) s;
			ElementInfo info = new ElementInfo();
			// We need to filter arrays.
			if (!TclParseUtil.isArrayVariable(field.getName())) {
				List levels = TclParseUtil.findLevelsTo(this.moduleDeclaration,
						s);
				info.key = TclParseUtil
						.getElementFQN(levels,
								IMixinRequestor.MIXIN_NAME_SEPARATOR,
								moduleDeclaration)// this.getNamespacePrefix()
						+ IMixinRequestor.MIXIN_NAME_SEPARATOR
						+ this.tclNameToKey(field.getName());
				if (info.key.startsWith("{")) {
					info.key = info.key.substring(1);
				}
			} else {
				info.key = this.getNamespacePrefix()
				// + IMixinRequestor.MIXIN_NAME_SEPARATOR
						+ this.tclNameToKey(TclParseUtil.extractArrayName(field
								.getName()));
			}
			if (signature) {
				info.object = new TclField();
			}
			this.requestor.reportElement(info);
		}

		return super.visit(s);
	}

	private void processNamespaceImport(Statement s) {
		TclStatement st = (TclStatement) s;
		if (st.getCount() > 2) {
			String cmdValue = getValue(st.getAt(0));
			if (!"namespace".equals(cmdValue)) {
				return;
			}
			String arg0 = getValue(st.getAt(1));
			if (!"import".equals(arg0)) {
				return;
			}
			String currentNamespace = getRealNamespacePrefix();
			for (int i = 2; i < st.getCount(); i++) {
				String value = getValue(st.getAt(i));
				if (value != null && !"-force".equals(value)) {
					ElementInfo info = new ElementInfo();
					value = TclNamespaceImport.processPattern(value);
					info.key = TclNamespaceImport.makeKey(currentNamespace,
							value);
					if (info.key != null) {
						if (signature) {
							info.object = new TclNamespaceImport(
									currentNamespace, value);
						}
						this.requestor.reportElement(info);
					}
				}
			}
		}
	}

	private String getValue(Expression cmd) {
		if (cmd instanceof SimpleReference) {
			String cmdValue = ((SimpleReference) cmd).getName();
			if (cmdValue.startsWith("::")) {
				cmdValue = cmdValue.substring(2);
			}
			return cmdValue;
		}
		return null;
	}

	public boolean visit(TypeDeclaration s) throws Exception {
		// Skip type names with code execution
		if (s.getName().indexOf('[') != -1 || s.getName().indexOf('$') != -1) {
			return true;
		}

		for (int i = 0; i < this.extensions.length; i++) {
			if (extensions[i].visit(s, this)) {
				return true;
			}
		}
		// This is Tcl namespaces
		ElementInfo info = new ElementInfo();

		info.key = this.getNamespacePrefix() + tclNameToKey(s.getName());
		if (info.key.startsWith("{")) {
			info.key = info.key.substring(1);
		}
		// System.out.println("Report XOTcl type:" + info.key);
		this.namespaceNames.push(s);
		if (signature) {
			info.object = new TclNamespace();
		}
		this.requestor.reportElement(info);

		return true;
	}

	public boolean getSignature() {
		return this.signature;
	}

	public IMixinRequestor getRequestor() {
		return this.requestor;
	}

	public ModuleDeclaration getModuleDeclaration() {
		return moduleDeclaration;
	}

	public void pushNamespaceName(TypeDeclaration s) {
		this.namespaceNames.push(s);
	}
}
