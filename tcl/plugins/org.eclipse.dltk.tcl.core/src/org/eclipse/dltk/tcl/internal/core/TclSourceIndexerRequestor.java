package org.eclipse.dltk.tcl.internal.core;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.search.indexing.SourceIndexerRequestor;

public class TclSourceIndexerRequestor extends SourceIndexerRequestor {
	public void acceptMethodReference(char[] methodName, int argCount,
			int sourcePosition, int sourceEndPosition) {
		// System.out.println("TclSourceIndexerRequestor:Add Method Reference: "
		// + new String(methodName));
		String mName = new String(methodName);
		String[] ns = mName.split("::");
		this.indexer.addMethodReference(ns[ns.length - 1].toCharArray(),
				argCount);
		for (int i = 0; i < ns.length - 1; ++i) {
			if (ns[i].length() > 0) {
				// System.out.println("TCLReferenceIndexing: Added namespace
				// reference:" + ns[i]);
				this.indexer.addTypeReference(ns[i].toCharArray());
			}
		}
	}

	public boolean enterTypeAppend(String fullName, String delimiter) {
		if (fullName.startsWith("::")) {
			if (DLTKCore.DEBUG) {
				System.out
						.println("We need to correct index global namespace append from other namespace..");
			}
		} else {
			String name = fullName;
			int pos = fullName.lastIndexOf("::");
			if (pos != -1) {
				name = fullName.substring(pos + 2);
			}
			// TODO: Need to support entering into complex name.
			this.indexer.addTypeDeclaration(Modifiers.AccNameSpace,
					this.pkgName, name, enclosingTypeNames(), null);
			this.pushTypeName(name.toCharArray());
		}
		return true;
	}

	public boolean enterTypeAppend(TypeInfo info, String fullName,
			String delimiter) {
		return false;
	}

	public void enterMethodRemoveSame(MethodInfo info) {
		enterMethod(info);
	}

	public boolean enterMethodWithParentType(MethodInfo info,
			String parentName, String delimiter) {
		return false;
	}
}
