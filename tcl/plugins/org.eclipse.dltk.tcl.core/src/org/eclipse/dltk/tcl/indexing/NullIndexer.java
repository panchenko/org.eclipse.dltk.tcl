package org.eclipse.dltk.tcl.indexing;

import org.eclipse.dltk.core.search.SearchDocument;
import org.eclipse.dltk.core.search.indexing.AbstractIndexer;

public class NullIndexer extends AbstractIndexer {
	public NullIndexer() {
		super(null);
	}

	public void indexDocument() {
	}

	public void addConstructorDeclaration(String typeName,
			String[] parameterTypes, String[] exceptionTypes) {
	}

	@Override
	public void addConstructorReference(char[] typeName, int argCount) {
	}

	@Override
	public void addFieldDeclaration(char[] fieldName) {
	}

	@Override
	public void addFieldDeclaration(char[] typeName, char[] fieldName) {
	}

	@Override
	public void addFieldReference(char[] fieldName) {
	}

	@Override
	protected void addIndexEntry(char[] category, char[] key) {
	}

	@Override
	public void addMethodDeclaration(int modifiers, char[] packageName,
			char[][] enclosingTypeNames, String methodName,
			String[] parameterNames, String[] exceptionTypes) {
	}

	@Override
	public void addMethodReference(char[] methodName, int argCount) {
	}

	@Override
	public void addNameReference(char[] name) {
	}

	@Override
	public void addTypeDeclaration(int modifiers, char[] packageName,
			String name, char[][] enclosingTypeNames, String[] superclasss) {
	}

	@Override
	public void addTypeReference(char[] typeName) {
	}
}
