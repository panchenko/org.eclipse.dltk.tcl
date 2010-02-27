package org.eclipse.dltk.tcl.indexing;

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
	public void addFieldDeclaration(String fieldName, String typeName) {
	}

	@Override
	public void addFieldReference(String fieldName) {
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
	public void addMethodReference(String methodName, int argCount) {
	}

	@Override
	public void addNameReference(String name) {
	}

	@Override
	public void addTypeDeclaration(int modifiers, char[] packageName,
			String name, char[][] enclosingTypeNames, String[] superclasss) {
	}

	@Override
	public void addTypeReference(String typeName) {
	}
}
