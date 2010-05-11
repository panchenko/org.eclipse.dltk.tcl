/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.core.codeassist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.codeassist.AssistParser;
import org.eclipse.dltk.codeassist.IAssistParser;
import org.eclipse.dltk.codeassist.ICompletionNameProvider;
import org.eclipse.dltk.codeassist.ScriptCompletionEngine;
import org.eclipse.dltk.codeassist.complete.CompletionNodeFound;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.compiler.env.lookup.Scope;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.CompletionContext;
import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.Flags;
import org.eclipse.dltk.core.IAccessRule;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.RuntimePerformanceMonitor;
import org.eclipse.dltk.core.RuntimePerformanceMonitor.PerformanceNode;
import org.eclipse.dltk.core.mixin.IMixinRequestor;
import org.eclipse.dltk.core.search.IDLTKSearchConstants;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.core.search.SearchMatch;
import org.eclipse.dltk.core.search.SearchParticipant;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.SearchRequestor;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.ast.TclPackageDeclaration;
import org.eclipse.dltk.tcl.core.extensions.ICompletionExtension;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.internal.core.TclExtensionManager;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.CompletionOnKeywordArgumentOrFunctionArgument;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.CompletionOnKeywordOrFunction;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.CompletionOnVariable;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.TclCompletionParser;
import org.eclipse.dltk.tcl.internal.core.packages.TclBuildPathPackageCollector;
import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinModel;
import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinUtils;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclNamespaceImport;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclProc;
import org.eclipse.dltk.tcl.internal.parser.OldTclParserUtils;
import org.eclipse.emf.common.util.EList;

public class TclCompletionEngine extends ScriptCompletionEngine {

	protected AssistParser parser;
	protected ISourceModule sourceModule;
	protected final static boolean TRACE_COMPLETION_TIME = false;
	private ICompletionExtension[] extensions;
	private TclBuildPathPackageCollector packageCollector;

	public TclCompletionEngine() {
		this.extensions = TclExtensionManager.getDefault()
				.getCompletionExtensions();
		this.parser = new AssistParser(new TclCompletionParser(this.extensions));
	}

	public void complete(IModuleSource sourceModule, int completionPosition,
			int pos) {
		PerformanceNode p = RuntimePerformanceMonitor.begin();
		this.sourceModule = (ISourceModule) sourceModule.getModelElement();
		if (VERBOSE) {
			System.out.print("COMPLETION IN "); //$NON-NLS-1$
			System.out.print(sourceModule.getFileName());
			System.out.print(" AT POSITION "); //$NON-NLS-1$
			System.out.println(completionPosition);
			System.out.println("COMPLETION - Source :"); //$NON-NLS-1$
			System.out.println(sourceModule.getSourceContents());
		}

		this.requestor.beginReporting();

		boolean contextAccepted = false;
		try {
			// this.fileName = sourceModule.getFileName();
			this.actualCompletionPosition = completionPosition;
			this.offset = pos;
			ModuleDeclaration parsedUnit = this.parser.parse(sourceModule);

			// Collect all packages information.
			packageCollector = new TclBuildPathPackageCollector();
			try {
				parsedUnit.traverse(packageCollector);
			} catch (Exception e1) {
				if (DLTKCore.DEBUG) {
					e1.printStackTrace();
				}
			}

			if (parsedUnit != null) {
				if (VERBOSE) {
					System.out.println("COMPLETION - Diet AST :"); //$NON-NLS-1$
					System.out.println(parsedUnit.toString());
				}
				try {

					this.source = sourceModule.getContentsAsCharArray();
					parser.parseBlockStatements(parsedUnit,
							this.actualCompletionPosition);
					if (VERBOSE) {
						System.out.println("COMPLETION - AST :"); //$NON-NLS-1$
						System.out.println(parsedUnit.toString());
					}

				} catch (CompletionNodeFound e) {
					// completionNodeFound = true;
					if (e.astNode != null) {
						if (VERBOSE) {
							System.out.print("COMPLETION - Completion node : "); //$NON-NLS-1$
							System.out.println(e.astNode.toString());
							if (this.parser.getAssistNodeParent() != null) {
								System.out.print("COMPLETION - Parent Node : "); //$NON-NLS-1$
								System.out.println(this.parser
										.getAssistNodeParent());
							}
						}
						// if null then we found a problem in the completion
						// node

						contextAccepted = this.complete(e.astNode, this.parser
								.getAssistNodeParent(), e.scope,
								e.insideTypeAnnotation);
					}
				}
			}

			if (this.noProposal && this.problem != null) {
				if (!contextAccepted) {
					contextAccepted = true;
					CompletionContext context = new CompletionContext();
					context.setOffset(completionPosition);
					context.setTokenKind(CompletionContext.TOKEN_KIND_UNKNOWN);

					this.requestor.acceptContext(context);
				}

				this.requestor.completionFailure(this.problem);

				if (DEBUG) {
					this.printDebug(this.problem);
				}
			}
		} catch (OperationCanceledException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} finally {
			if (!contextAccepted) {
				contextAccepted = true;
				CompletionContext context = new CompletionContext();
				context.setTokenKind(CompletionContext.TOKEN_KIND_UNKNOWN);
				context.setOffset(completionPosition);

				this.requestor.acceptContext(context);
			}
			this.requestor.endReporting();
			p.done(TclNature.NATURE_ID, "Completion time", 0);
		}
	}

	protected boolean complete(ASTNode astNode, ASTNode astNodeParent,
			Scope scope, boolean insideTypeAnnotation) {
		this.setSourceRange(astNode.sourceStart(), astNode.sourceEnd());

		for (int i = 0; i < this.extensions.length; i++) {
			this.extensions[i].setRequestor(this.getRequestor());
		}
		if (astNode instanceof CompletionOnKeywordOrFunction) {
			CompletionOnKeywordOrFunction key = (CompletionOnKeywordOrFunction) astNode;
			this.processCompletionOnKeywords(key, key.getName());
			this.processCompletionOnFunctions(astNodeParent, key);

			for (int i = 0; i < this.extensions.length; i++) {
				this.extensions[i].completeOnKeywordOrFunction(key,
						astNodeParent, this);
			}
			if (key.getToken().length == 0) {
				this.findVariables(key.getToken(), key.getInParent(), true,
						astNode.sourceStart(), key.canCompleteEmptyToken(),
						null);
			}
		} else if (astNode instanceof CompletionOnVariable) {
			this.processCompletionOnVariables(astNode);
			for (int i = 0; i < this.extensions.length; i++) {
				this.extensions[i].completeOnVariables(
						(CompletionOnVariable) astNode, this);
			}
		} else if (astNode instanceof TclPackageDeclaration) {
			TclPackageDeclaration pkg = (TclPackageDeclaration) astNode;
			int len = this.actualCompletionPosition - pkg.getNameStart();
			String token = "";
			if (len <= pkg.getName().length()) {
				token = pkg.getName().substring(0, len);
			}
			this.setSourceRange(pkg.getNameStart(), pkg.getNameEnd());
			this.packageNamesCompletion(token.toCharArray(),
					new HashSet<String>());

		} else if (astNode instanceof CompletionOnKeywordArgumentOrFunctionArgument) {
			CompletionOnKeywordArgumentOrFunctionArgument compl = (CompletionOnKeywordArgumentOrFunctionArgument) astNode;
			TclStatement st = compl.getStatement();
			Set<String> methodNames = new HashSet<String>();
			if (st.getCount() >= 1) {
				// Completion on two argument keywords
				final Expression at = st.getAt(0);
				if (at instanceof SimpleReference) {
					final String name = ((SimpleReference) at).getName();
					if (compl.sourceStart() < at.sourceEnd()
							|| requestor.isContextInformationMode()) {
						this.processCompletionOnFunctions(astNodeParent, name
								.toCharArray(), false);
					}
					String token = null;
					if (compl.argumentIndex() == 1) {
						token = compl.getName();
					} else if (st.getCount() > 1) {
						final Expression at1 = st.getAt(1);
						if (at1 instanceof SimpleReference) {
							token = ((SimpleReference) at1).getName();
						}
					}
					if (DEBUG) {
						System.out.println("compl.argumentIndex="
								+ compl.argumentIndex());
						System.out.println("name='" + name + "'");
						System.out.println("token='" + token);
					}
					if (token != null) {
						this.processPartOfKeywords(st.sourceStart(), compl,
								name + " " + token, methodNames);
						for (int i = 0; i < this.extensions.length; i++) {
							this.extensions[i].completeOnKeywordArgumentsOne(
									name, token.toCharArray(), compl,
									methodNames, st, this);
						}
					}
				}
			}
			if (!requestor.isContextInformationMode()
					&& (compl.argumentIndex() == 3 || (compl.argumentIndex() == -1 && st
							.getCount() > 1))) {
				Expression at0 = st.getAt(0);
				Expression at1 = st.getAt(1);
				if (at1 instanceof SimpleReference
						&& at0 instanceof SimpleReference) {
					String sat0 = ((SimpleReference) at0).getName();
					String sat1 = ((SimpleReference) at1).getName();
					if ("package".equals(sat0) && "require".equals(sat1)) {
						this.packageNamesCompletion(compl.getToken(),
								methodNames);
						return true;
					}
				}
			}
			// Variables completion here.
			// char[] varToken = new char[0];
			boolean provideDollar = true;
			if (compl.getToken().length > 0 && compl.getToken()[0] == '$') {
				// varToken = compl.getToken();
				provideDollar = true;
				this.findVariables(compl.getToken(), astNodeParent, true,
						astNode.sourceStart(), provideDollar, null);
			}
		}
		return true;
	}

	private void packageNamesCompletion(char[] token, Set<String> methodNames) {
		IScriptProject project = this.sourceModule.getScriptProject();
		IInterpreterInstall install;
		try {
			install = ScriptRuntime.getInterpreterInstall(project);
			if (install != null) {
				List<TclPackageInfo> packageNames = TclPackagesManager
						.getPackageInfos(install);
				Set<String> added = new HashSet<String>();
				List<String> k = new ArrayList<String>();
				String prefix = new String(token);
				for (Iterator<TclPackageInfo> iterator = packageNames
						.iterator(); iterator.hasNext();) {
					String kkw = iterator.next().getName();
					if (kkw.indexOf('$') == -1) {
						if (kkw.startsWith(prefix)) {
							if (added.add(kkw)) {
								k.add(kkw);
							}
						}
					}
				}
				List<TclModuleInfo> packages = TclPackagesManager
						.getProjectModules(project.getElementName());
				if (packages != null) {
					for (TclModuleInfo tclModuleInfo : packages) {
						EList<TclSourceEntry> provided = tclModuleInfo
								.getProvided();
						for (TclSourceEntry userPkgs : provided) {
							if (added.add(userPkgs.getValue())) {
								k.add(userPkgs.getValue());
							}
						}
					}
				}
				String kw[] = k.toArray(new String[k.size()]);
				this.findKeywords(token, kw, true);
				if (methodNames != null) {
					methodNames.addAll(k);
				}
			}
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	protected void processPartOfKeywords(int sourceStart,
			CompletionOnKeywordArgumentOrFunctionArgument compl, String prefix,
			Set<String> methodNames) {
		if (!this.requestor.isIgnored(CompletionProposal.KEYWORD)) {
			List<String> k = processPartOfKeywords(compl.getPossibleKeywords(),
					prefix, sourceStart);
			if (methodNames != null) {
				methodNames.addAll(k);
			}
		}
	}

	protected List<String> processPartOfKeywords(String[] keywords,
			String prefix, int sourceStart) {
		if (prefix != null && prefix.length() != 0 && prefix.charAt(0) == ':') {
			String[] doubleColonPrependedKeywords = new String[keywords.length];
			for (int i = 0; i < keywords.length; ++i) {
				doubleColonPrependedKeywords[i] = "::" + keywords[i];
			}
			keywords = doubleColonPrependedKeywords;
		}
		List<String> k = new ArrayList<String>();
		if (prefix == null) {
			prefix = Util.EMPTY_STRING;
		}
		prefix = prefix.trim();
		selectKeywords(keywords, prefix, k, requestor
				.isContextInformationMode());
		if (k.isEmpty() && requestor.isContextInformationMode()) {
			final int p = prefix.indexOf(' ');
			if (p > 0) {
				selectKeywords(keywords, prefix.substring(0, p), k, requestor
						.isContextInformationMode());
			}
		}
		if (requestor.isContextInformationMode()
				|| !(k.size() == 1 && prefix.equals(k.get(0)))) {
			// do not report single exact match in completion mode.
			reportKeywords(prefix, k, sourceStart);
		}
		return k;
	}

	private void reportKeywords(String prefix, List<String> keywords,
			int sourceStart) {
		final char[] keyword = prefix.toCharArray();
		for (final String kw : keywords) {
			final String choice = kw;
			int relevance = computeBaseRelevance();

			relevance += computeRelevanceForInterestingProposal();
			relevance += computeRelevanceForCaseMatching(keyword, choice);
			relevance += computeRelevanceForRestrictions(IAccessRule.K_ACCESSIBLE); // no
			this.noProposal = false;
			if (!this.requestor.isIgnored(CompletionProposal.KEYWORD)) {
				CompletionProposal proposal = this.createProposal(
						CompletionProposal.KEYWORD,
						this.actualCompletionPosition);
				proposal.setName(choice);
				proposal.setCompletion(choice);
				proposal.setReplaceRange(sourceStart - this.offset,
						this.endPosition - this.offset);
				proposal.setRelevance(relevance);
				proposal.setExtraInfo(prefix);
				this.requestor.accept(proposal);
				if (DEBUG) {
					this.printDebug(proposal);
				}
			}
		}
	}

	private void selectKeywords(String[] possibleKeywords, String prefix,
			List<String> selection, boolean preferExactMatch) {
		if (preferExactMatch) {
			for (int i = 0; i < possibleKeywords.length; i++) {
				final String kkw = possibleKeywords[i];
				if (kkw.equals(prefix)) {
					selection.add(kkw);
					return;
				}
			}
		}
		for (int i = 0; i < possibleKeywords.length; i++) {
			final String kkw = possibleKeywords[i];
			if (kkw.startsWith(prefix)) {
				selection.add(kkw);
			}
		}
	}

	protected void processCompletionOnVariables(ASTNode astNode) {
		if (!this.requestor.isIgnored(CompletionProposal.LOCAL_VARIABLE_REF)
				&& !this.requestor
						.isIgnored(CompletionProposal.VARIABLE_DECLARATION)) {
			CompletionOnVariable completion = (CompletionOnVariable) astNode;
			this.findVariables(completion.getToken(), completion.getInNode(),
					completion.canHandleEmpty(), astNode.sourceStart(),
					completion.getProvideDollar(), null);
		}
	}

	protected void processCompletionOnFunctions(ASTNode astNodeParent,
			CompletionOnKeywordOrFunction key) {
		processCompletionOnFunctions(astNodeParent,
				removeLastColonFromToken(key.getToken()), key
						.canCompleteEmptyToken());
	}

	protected void processCompletionOnFunctions(ASTNode astNodeParent,
			char[] token, boolean canCompleteEmptyToken) {
		if (!this.requestor.isIgnored(CompletionProposal.METHOD_REF)) {
			List methodNames = new ArrayList();
			Set set = new HashSet();
			this.findLocalFunctions(token, canCompleteEmptyToken,
					astNodeParent, methodNames, set);
			set.addAll(methodNames);
			this.findNamespaceFunctions(token, set);
			if (astNodeParent instanceof TypeDeclaration) {
				if ((((TypeDeclaration) astNodeParent).getModifiers() & Modifiers.AccNameSpace) != 0) {
					String namespace = TclParseUtil.getElementFQN(
							astNodeParent,
							IMixinRequestor.MIXIN_NAME_SEPARATOR, this.parser
									.getModule());
					this.findSpecificNamespaceFunctions(token, set, namespace,
							false);
				}
			}
			// Check and process imported namespaces
			processImports(token, set, astNodeParent);
		}
	}

	private void processImports(char[] token, Set set, ASTNode astNodeParent) {
		String currentNamespace = TclParseUtil.getElementFQN(astNodeParent,
				"::", getAssistParser().getModule());
		if (token == null) {
			return;
		}
		String t = new String(token);
		if (t.startsWith("::")) {
			t = t.substring(2);
		}
		if (t.startsWith(":")) {
			t = t.substring(1);
		}
		Set processed = new HashSet();
		if (t.length() > 0) {
			processFindNamespace(token, set, currentNamespace, processed);
			// Also empty namespace should be processed
			if (!currentNamespace.equals("")) {
				processFindNamespace(token, set, "", processed);
			}
		}
	}

	private void processFindNamespace(char[] token, Set set,
			String currentNamespace, Set processed) {
		String pattern = "@" + currentNamespace + "|*";
		String[] findKeys = TclMixinModel.getInstance().getMixin(
				getSourceModule().getScriptProject()).findKeys(pattern);
		Set keys = new HashSet();
		for (int i = 0; i < findKeys.length; i++) {
			if (keys.add(findKeys[i])) {
				TclNamespaceImport importSt = TclNamespaceImport
						.parseKey(findKeys[i]);
				if (importSt != null
						&& importSt.getNamespace().equals(currentNamespace)) {
					if (processed.add(importSt.getImportNsName())) {
						this.findSpecificNamespaceFunctions(token, set,
								importSt.getImportNsName(), true);
					}
				}
			}
		}
		// IMixinElement[] find = TclMixinModel.getInstance().getMixin(
		// getSourceModule().getScriptProject()).find(pattern, 0);
		// for (int i = 0; i < find.length; i++) {
		// Object[] allObjects = find[i].getAllObjects();
		// for (int j = 0; j < allObjects.length; j++) {
		// if (allObjects[j] instanceof TclNamespaceImport) {
		// TclNamespaceImport importSt = (TclNamespaceImport) allObjects[j];
		// if (importSt.getNamespace().equals(currentNamespace)) {
		// if (processed.add(importSt.getImportNsName())) {
		// this.findSpecificNamespaceFunctions(token, set,
		// importSt.getImportNsName(), true);
		// }
		// }
		// }
		// }
		// }
	}

	protected void processCompletionOnKeywords(
			CompletionOnKeywordOrFunction key, String token) {
		if (!this.requestor.isIgnored(CompletionProposal.KEYWORD)) {
			processPartOfKeywords(key.getPossibleKeywords(), token,
					this.startPosition);
		}
	}

	/**
	 * Removes ':' on the end.
	 * 
	 * @param token
	 * @return
	 */
	public char[] removeLastColonFromToken(char[] token) {
		if (token.length > 2 && token[token.length - 1] == ':'
				&& token[token.length - 2] != ':') {
			char co2[] = new char[token.length - 1];
			System.arraycopy(token, 0, co2, 0, co2.length);
			return co2;
		}
		return token;
	}

	protected void findNamespaceFunctions(final char[] token,
			final Set methodNames) {

		final Set methods = new HashSet();
		String to_ = new String(token);
		String to = to_;
		if (to.startsWith("::")) {
			to = to.substring(2);
		}
		if (to.length() == 0) {
			return;
		}
		methods.addAll(methodNames);
		this.findMethodFromMixin(methods, to + "*");
		methods.removeAll(methodNames);
		// Remove all with same names
		this.removeSameFrom(methodNames, methods, to_);
		this.filterInternalAPI(methods, this.parser.getModule());
		// findTypes(token, true, toList(types));
		this.findMethods(token, false, this.toList(methods));
	}

	protected void findSpecificNamespaceFunctions(final char[] token,
			final Set methodNames, String namespace, boolean asImport) {
		if (namespace.endsWith(IMixinRequestor.MIXIN_NAME_SEPARATOR)) {
			namespace = namespace.substring(0, namespace.length() - 1);
		}
		Set methods = new HashSet();
		String to_ = new String(token);
		String to = to_;
		if (to.startsWith("::")) {
			to = to.substring(2);
		}
		methods.addAll(methodNames);
		this.findMethodFromMixin(methods, namespace
				+ IMixinRequestor.MIXIN_NAME_SEPARATOR + to + "*");
		methods.removeAll(methodNames);
		// Remove all with same names
		// removeSameFrom(methodNames, methods, to_);
		// filterAllPrivate(methodNames, methods, this.parser.getModule());
		// findTypes(token, true, toList(types));
		if (asImport) {
			filterInternalAPI(methods, getAssistParser().getModule());
			methods = filterSubNamespaces(methods, namespace);
		}
		List list = this.toList(methods);
		List mNames = this.removeNamespace(list, namespace, asImport);

		this.findMethods(token, true, list, mNames);
	}

	private Set filterSubNamespaces(Set methods, String namespace) {
		Set results = new HashSet();
		if (!namespace.startsWith("::")) {
			namespace = "::" + namespace;
		}
		if (!namespace.endsWith("::")) {
			namespace = namespace + "::";
		}
		for (Iterator iterator = methods.iterator(); iterator.hasNext();) {
			IModelElement element = (IMethod) iterator.next();

			String fqn = TclParseUtil.getFQNFromModelElement(element, "::");
			if (fqn.startsWith(namespace)) {
				String substring = fqn.substring(namespace.length());
				if (substring.indexOf("::") == -1) {
					results.add(element);
				}
			} else {
				results.add(element);
			}
		}
		return results;
	}

	private List removeNamespace(List methods, String namespace,
			boolean asImport) {
		List names = new ArrayList();
		if (!namespace.startsWith("::")) {
			namespace = "::" + namespace;
		}
		if (!namespace.endsWith("::")) {
			namespace = namespace + "::";
		}
		for (Iterator iterator = methods.iterator(); iterator.hasNext();) {
			IModelElement element = (IModelElement) iterator.next();
			String fqn = TclParseUtil.getFQNFromModelElement(element, "::");
			if (fqn.startsWith(namespace)) {
				String substring = fqn.substring(namespace.length());
				names.add(substring);
			} else {
				names.add(fqn);
			}
		}
		return names;
	}

	/**
	 * Filters all private methods not in current module namespaces.
	 * 
	 * @param methodNames
	 * @param methods
	 */
	private void filterInternalAPI(Set methods, final ModuleDeclaration module) {
		if (!this.getRequestor().isIgnored(
				ITclCompletionProposalTypes.FILTER_INTERNAL_API)) {
			return;
		}
		final Set namespaceNames = new HashSet();

		try {
			module.traverse(new ASTVisitor() {
				public boolean visit(TypeDeclaration s) throws Exception {
					String ns = TclParseUtil.getElementFQN(s, "::", module);
					if (!ns.startsWith("::")) {
						ns = "::" + ns;
					}
					namespaceNames.add(ns);
					// Also add all subnamespaces
					String ns2 = ns;
					while (true) {
						ns2 = TclCompletionEngine.this.getElementNamespace(ns2);
						if (ns2 == null || (ns2 != null && ns2.equals("::"))) {
							break;
						}
						namespaceNames.add(ns2);
					}
					return super.visit(s);
				}
			});
		} catch (Exception e1) {
			if (DLTKCore.DEBUG) {
				e1.printStackTrace();
			}
		}

		Set privateSet = new HashSet();
		for (Iterator iterator = methods.iterator(); iterator.hasNext();) {
			Object method = iterator.next();
			if (method instanceof IMethod) {
				IMethod m = (IMethod) method;
				try {
					boolean nsHere = false;
					String elemName = TclParseUtil.getFQNFromModelElement(m,
							"::");
					String elemNSName = this.getElementNamespace(elemName);
					if (elemNSName != null) {
						nsHere = namespaceNames.contains(elemNSName);
					}
					if (!nsHere && !Flags.isPublic(m.getFlags())) {
						privateSet.add(method);
					}
				} catch (ModelException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
			}
		}
		for (Iterator iterator = privateSet.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			methods.remove(object);
		}
	}

	private String getElementNamespace(String elemName) {
		int pos = elemName.lastIndexOf("::");
		if (pos != -1) {
			String rs = elemName.substring(0, pos);
			if (rs.length() == 0) {
				return null;
			}
			return rs;
		}
		return null;
	}

	protected void findMethods(char[] token, boolean canCompleteEmptyToken,
			List methods) {
		this.findMethods(token, canCompleteEmptyToken, methods,
				CompletionProposal.METHOD_REF);
	}

	public void removeSameFrom(final Set methodNames, final Set elements,
			String to_) {
		final Set namesToRemove = new HashSet();
		for (Iterator iterator = methodNames.iterator(); iterator.hasNext();) {
			Object name = iterator.next();
			if (name instanceof String) {
				namesToRemove.add(name);
			}
		}
		if (!namesToRemove.isEmpty()) {
			// We need to remove all elements with namesToRemove from methods.
			for (Iterator me = elements.iterator(); me.hasNext();) {
				Object m = me.next();
				if (m instanceof IMethod) {
					String qname = this.processMethodName((IMethod) m, to_);
					if (namesToRemove.contains(qname)) {
						me.remove();
					}
				} else if (m instanceof IType) {
					String qname = this.processTypeName((IType) m, to_);
					if (namesToRemove.contains(qname)) {
						me.remove();
					}
				}
			}
		}
		elements.removeAll(methodNames);
	}

	public void findMixinTclElement(final Set completions, String tok,
			Class mixinClass) {
		String pattern = tok.replaceAll("::",
				IMixinRequestor.MIXIN_NAME_SEPARATOR);
		IModelElement[] elements = TclMixinUtils.findModelElementsFromMixin(
				pattern, mixinClass, this.scriptProject, getProgressMonitor());
		elements = TclResolver.complexFilter(elements, this.scriptProject,
				this.packageCollector, false);
		final Set completionNames = calculateCompletionNames(completions);
		for (int i = 0; i < elements.length; i++) {
			// We should filter external source modules with same
			// external path.
			final IModelElement element = elements[i];
			if (this.moduleFilter(completions, element, completionNames)) {
				completions.add(element);
			}
		}
	}

	public void findMixinTclElementNoFilter(final Set completions, String tok,
			Class mixinClass) {
		String pattern = tok.replaceAll("::",
				IMixinRequestor.MIXIN_NAME_SEPARATOR);
		IModelElement[] elements = TclMixinUtils.findModelElementsFromMixin(
				pattern, mixinClass, this.scriptProject, getProgressMonitor());
		// long start = System.currentTimeMillis();
		for (int i = 0; i < elements.length; i++) {
			// We should filter external source modules with same
			// external path.
			// if (moduleFilter(completions, elements[i])) {
			completions.add(elements[i]);
			// }
			// if (System.currentTimeMillis() - start > 1000) {
			// return;
			// }
		}
	}

	protected void findMethodFromMixin(final Set methods, String tok) {
		this.findMixinTclElement(methods, tok, TclProc.class);
	}

	protected boolean moduleFilter(Set completions, IModelElement modelElement,
			Set completionNames) {
		for (int i = 0; i < this.extensions.length; i++) {
			if (!this.extensions[i].modelFilter(completions, modelElement)) {
				return false;
			}
		}
		return completionNames.add(getFQNFromModelElement(modelElement));
	}

	private Set calculateCompletionNames(Set completions) {
		final Set result = new HashSet();
		for (Iterator iterator = completions.iterator(); iterator.hasNext();) {
			Object o = iterator.next();
			if (!(o instanceof IModelElement)) {
				if (o instanceof String) {
					result.add(o);
				}
				continue;
			}
			final IModelElement element = (IModelElement) o;
			result.add(getFQNFromModelElement(element));
		}
		return result;
	}

	private static final String getFQNFromModelElement(IModelElement element) {
		return TclParseUtil.getFQNFromModelElement(element, "::");
	}

	public <T> List<T> toList(Set<T> types) {
		return new ArrayList<T>(types);
	}

	protected void search(String patternString, int searchFor, int limitTo,
			IDLTKSearchScope scope, SearchRequestor resultCollector)
			throws CoreException {
		this.search(patternString, searchFor, limitTo, EXACT_RULE, scope,
				resultCollector);
	}

	protected void search(String patternString, int searchFor, int limitTo,
			int matchRule, IDLTKSearchScope scope, SearchRequestor requestor)
			throws CoreException {
		if (patternString.indexOf('*') != -1
				|| patternString.indexOf('?') != -1) {
			matchRule |= SearchPattern.R_PATTERN_MATCH;
		}
		SearchPattern pattern = SearchPattern.createPattern(patternString,
				searchFor, limitTo, matchRule, scope.getLanguageToolkit());
		new SearchEngine().search(pattern,
				new SearchParticipant[] { SearchEngine
						.getDefaultSearchParticipant() }, scope, requestor,
				null);
	}

	protected void findLocalFunctions(char[] token,
			boolean canCompleteEmptyToken, ASTNode astNodeParent,
			List methodNames, Set set) {

		token = this.removeLastColonFromToken(token);
		List methods = new ArrayList();
		this.fillFunctionsByLevels(token, astNodeParent, methods, methodNames);
		List nodeMethods = new ArrayList();
		List nodeMethodNames = new ArrayList();
		List otherMethods = new ArrayList();
		List otherMethodNames = new ArrayList();
		TclResolver resolver = new TclResolver(this.sourceModule, this.parser
				.getModule());
		if (methods.size() > 0) {
			for (int i = 0; i < methods.size(); i++) {
				MethodDeclaration method = (MethodDeclaration) methods.get(i);
				IModelElement modelElement = resolver
						.findModelElementFrom(method);
				if (modelElement != null) {
					nodeMethods.add(modelElement);
					set.add(modelElement);
					nodeMethodNames.add(methodNames.get(i));

				} else {
					otherMethods.add(method);
					otherMethodNames.add(methodNames.get(i));
				}
				// TclParseUtil.fi
			}
			this.findMethods(token, canCompleteEmptyToken, nodeMethods,
					nodeMethodNames);
			this.findLocalMethods(token, canCompleteEmptyToken, otherMethods,
					otherMethodNames);
		}
	}

	private void fillFunctionsByLevels(char[] token, ASTNode parent,
			List methods, List gmethodNames) {
		List levels = TclParseUtil
				.findLevelsTo(this.parser.getModule(), parent);
		int len = levels.size();
		List visited = new ArrayList();
		List methodNames = new ArrayList();
		// visited.addAll(levels);
		for (int j = 0; j < len; ++j) {
			ASTNode astNodeParent = (ASTNode) levels.get(len - 1 - j);
			boolean topLevel = false;
			if (token != null && token.length > 0 && token[0] == ':') {
				topLevel = true;
			}
			if (astNodeParent instanceof TypeDeclaration && !topLevel) {
				// Add all method here.
				TypeDeclaration decl = (TypeDeclaration) astNodeParent;
				List statements = decl.getStatements();
				if (statements != null) {
					this.processMethods(methods, methodNames, statements, "",
							visited, parent);
				}
			} else if (astNodeParent instanceof ModuleDeclaration) {
				ModuleDeclaration decl = (ModuleDeclaration) astNodeParent;
				List statements = decl.getStatements();
				if (statements != null) {
					this.processMethods(methods, methodNames, statements,
							topLevel ? "::" : "", visited, parent);
				}
			}
		}
		gmethodNames.addAll(methodNames);
	}

	private void processMethods(List methods, List methodNames,
			List statements, String namePrefix, List visited, ASTNode realParent) {
		for (int i = 0; i < statements.size(); ++i) {
			ASTNode nde = (ASTNode) statements.get(i);
			if (nde instanceof MethodDeclaration) {
				if (!this.isTclMethod((MethodDeclaration) nde)) {
					continue;
				}
				String mName = ((MethodDeclaration) nde).getName();
				if (!mName.startsWith("::")) {
					mName = namePrefix + mName;
				}
				if (realParent instanceof MethodDeclaration) {
					String name = ((MethodDeclaration) realParent).getName();
					String prefix = namePrefix
							+ ((MethodDeclaration) nde).getName();
					if (name.startsWith("::") && !prefix.startsWith("::")) {
						prefix = "::" + prefix;
					}
					if (!name.equals(prefix)) {
						int i1 = name.lastIndexOf("::");
						int i2 = prefix.lastIndexOf("::");
						if (i1 != -1 && i2 != -1) {
							String p1 = name.substring(0, i1);
							String p2 = prefix.substring(0, i2);
							if (p1.startsWith(p2)) {
								// System.out.println("#");
								String nn = prefix.substring(i2 + 2);
								if (!methodNames.contains(nn)) {
									/* && !methods.contains(nde) */
									if (this.methodCanBeAdded(nde, realParent,
											mName)) {
										methods.add(nde);
										methodNames.add(nn);
									}
								}
							}
						}
					}
				}
				if (!methodNames.contains(mName) /* && !methods.contains(nde) */) {
					if (this.methodCanBeAdded(nde, realParent, mName)) {
						methods.add(nde);
						methodNames.add(mName);
					}
				}
			} else if (nde instanceof TypeDeclaration && !visited.contains(nde)) {
				List tStatements = ((TypeDeclaration) nde).getStatements();
				visited.add(nde);
				if (realParent instanceof MethodDeclaration) {
					String name = ((MethodDeclaration) realParent).getName();
					String prefix = namePrefix
							+ ((TypeDeclaration) nde).getName();
					if (name.startsWith("::") && !prefix.startsWith("::")) {
						prefix = "::" + prefix;
					}
					if (name.startsWith(namePrefix)) {
						this.processMethods2(methods, methodNames, tStatements,
								"", realParent);
					}
				}
				String nn = ((TypeDeclaration) nde).getName();
				final String nextPrefix;
				if (nn.startsWith("::")) {
					nextPrefix = nn;
				} else {
					nextPrefix = namePrefix + nn;
				}
				this.processMethods(methods, methodNames, tStatements,
						nextPrefix + "::", visited, realParent);

			}
			visited.add(nde);
		}
	}

	private boolean isTclMethod(MethodDeclaration nde) {
		int modifiers = nde.getModifiers();
		if (modifiers < (2 << Modifiers.USER_MODIFIER)) {
			return true;
		}
		return false;
	}

	private boolean isTclType(TypeDeclaration nde) {
		int modifiers = nde.getModifiers();
		if (modifiers < (2 << Modifiers.USER_MODIFIER)) {
			return true;
		}
		return false;
	}

	private boolean isTclField(FieldDeclaration nde) {
		int modifiers = nde.getModifiers();
		if (modifiers < (2 << Modifiers.USER_MODIFIER)) {
			return true;
		}
		return false;
	}

	protected boolean methodCanBeAdded(ASTNode nde, ASTNode realParent,
			String mName) {
		// Methods should have same prefix, to be added here.
		String realParentFQN = TclParseUtil.getElementFQN(TclParseUtil
				.getPrevParent(parser.getModule(), realParent), "::", parser
				.getModule());
		String ndeFQN = TclParseUtil.getElementFQN(TclParseUtil.getPrevParent(
				parser.getModule(), nde), "::", parser.getModule());
		if (mName.startsWith("::")) {
			mName = mName.substring(2);
		}
		if (ndeFQN.length() > 0 && mName.startsWith(ndeFQN)) {
			return true;
		}
		if (ndeFQN.equals(realParentFQN)) {
			return true;
		}
		return false;
	}

	private void processMethods2(List methods, List methodNames,
			List statements, String namePrefix, ASTNode realParent) {
		for (int i = 0; i < statements.size(); ++i) {
			ASTNode nde = (ASTNode) statements.get(i);
			if (nde instanceof MethodDeclaration) {
				String mName = namePrefix + ((MethodDeclaration) nde).getName();
				if (mName.startsWith("::::")) {
					mName = mName.substring(2);
				}
				if (isTclMethod((MethodDeclaration) nde)) {
					if (!methodNames.contains(mName) && !methods.contains(nde)) {
						if (this.methodCanBeAdded(nde, realParent, mName)) {
							methods.add(nde);
							methodNames.add(mName);
						}
					}
				}
			} else if (nde instanceof TypeDeclaration) {
				List tStatements = ((TypeDeclaration) nde).getStatements();
				this.processMethods2(methods, methodNames, tStatements,
						namePrefix + ((TypeDeclaration) nde).getName() + "::",
						realParent);
			}
		}
	}

	protected void findVariables(char[] token, ASTNode parent,
			boolean canCompleteEmptyToken, int beforePosition,
			boolean provideDollar, List cho) {
		List gChoices = new ArrayList();
		if (cho != null) {
			gChoices.addAll(cho);
		}
		if (token.length > 0 && token[0] != '$') {
			provideDollar = false;
		}
		token = this.removeLastColonFromToken(token);
		if (parent instanceof MethodDeclaration) {
			MethodDeclaration method = (MethodDeclaration) parent;
			List choices = new ArrayList();
			List statements = method.getArguments();
			if (statements != null) {
				for (int i = 0; i < statements.size(); ++i) {
					Argument a = (Argument) statements.get(i);
					if (a != null) {
						String n = a.getName();
						this.checkAddVariable(choices, n);
					}
				}
			}
			// Process variable setters.
			statements = method.getStatements();
			this.checkVariableStatements(beforePosition, choices, statements);
			String[] cc = new String[choices.size()];
			for (int i = 0; i < choices.size(); ++i) {
				cc[i] = (String) choices.get(i);
				gChoices.add(choices.get(i));
			}
			this.findLocalVariables(token, cc, canCompleteEmptyToken,
					provideDollar);
		} else if (parent instanceof ModuleDeclaration) {
			ModuleDeclaration module = (ModuleDeclaration) parent;
			this.checkVariables(token, canCompleteEmptyToken, beforePosition,
					module.getStatements(), provideDollar, gChoices);
		} else if (parent instanceof TypeDeclaration) {
			TypeDeclaration type = (TypeDeclaration) parent;
			this.checkVariables(token, canCompleteEmptyToken, beforePosition,
					type.getStatements(), provideDollar, gChoices);
			String prefix = "";
			if (provideDollar) {
				prefix = "$" + prefix;
			}
			List statements = type.getStatements();
			for (int l = 0; l < statements.size(); ++l) {
				this.findASTVariables((ASTNode) statements.get(l), prefix,
						token, canCompleteEmptyToken, gChoices);
			}
		}
		String prefix = "";
		List choices = new ArrayList();
		if ((token.length > 0 && (token[0] == ':' || token[0] == '$'))
				|| token.length == 0 || (token.length > 2 && token[1] == ':')) {
			prefix = "::";
			if (provideDollar) {
				prefix = "$" + prefix;
			}
			this.findASTVariables(this.parser.getModule(), prefix, token,
					canCompleteEmptyToken, choices);
		}
		// remove dublicates
		for (int i = 0; i < gChoices.size(); ++i) {
			String c = (String) gChoices.get(i);
			if (choices.contains(c)) {
				choices.remove(c);
			}
			if (c.startsWith("$")) {
				String cc = c.substring(1);
				if (choices.contains(cc)) {
					choices.remove(cc);
				}
			}

		}
		String[] cc = new String[choices.size()];
		for (int i = 0; i < choices.size(); ++i) {
			cc[i] = (String) choices.get(i);
			gChoices.add(choices.get(i));
		}
		this.findLocalVariables(token, cc, canCompleteEmptyToken, true);

		this.findGlobalVariables(token, gChoices, provideDollar);
		// Find one level up
		if (!(this.checkValidParetNode(parent))) {
			// Lets find scope parent
			List findLevelsTo = TclParseUtil.findLevelsTo(this.parser
					.getModule(), parent);
			ASTNode realParent = null;
			for (Iterator iterator = findLevelsTo.iterator(); iterator
					.hasNext();) {
				ASTNode nde = (ASTNode) iterator.next();
				if (this.checkValidParetNode(nde)) {
					realParent = nde;
				}
			}
			if (realParent != null && !realParent.equals(parent)) {
				this.findVariables(token, realParent, canCompleteEmptyToken,
						beforePosition, provideDollar, gChoices);
			}
		}
	}

	private boolean checkValidParetNode(ASTNode parent) {
		return parent instanceof MethodDeclaration
				|| parent instanceof ModuleDeclaration
				|| parent instanceof TypeDeclaration;
	}

	private void findGlobalVariables(char[] token, final List choices,
			final boolean provideDollar) {
		final List fields = new ArrayList();
		final List types = new ArrayList();
		boolean provideDots = false;
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match)
					throws CoreException {
				Object element = match.getElement();
				if (element instanceof IType) {
					IType type = (IType) element;
					if ((type.getFlags() & Modifiers.AccNameSpace) == 0) {
						return;
					}
					if (!(type.getParent() instanceof ISourceModule)) {
						return;
					}
					String tName = type.getTypeQualifiedName();
					if (!choices.contains(tName)) {
						types.add(type);
					}
					// if( token.length > 3 ) {
					this.processTypeFields(choices, fields, type);
					// }
				} else if (element instanceof IField) {
					IField field = (IField) element;
					if (!(field.getParent() instanceof ISourceModule)) {
						return;
					}
					String mn = field.getTypeQualifiedName("$", false)
							.replaceAll("\\$", "::");
					if (!mn.startsWith("::")) {
						mn = "::" + mn;
					}
					if (provideDollar) {
						mn = "$" + mn;
					}
					if (!choices.contains(mn)
							&& !choices.contains(mn.substring(2))) {
						fields.add(field);
						choices.add(mn);
					}
				}
			}

			private void processTypeFields(final List methodNames,
					final List methods, IType type) throws ModelException {
				IField[] tmethods = type.getFields();
				for (int i = 0; i < tmethods.length; ++i) {
					String mn = tmethods[i].getTypeQualifiedName("$", false)
							.replaceAll("\\$", "::");
					if (!mn.startsWith("::")) {
						mn = "::" + mn;
					}
					if (!methodNames.contains(mn)) {
						methods.add(tmethods[i]);
						methodNames.add(mn);
					}
				}
				IType[] types = type.getTypes();
				for (int i = 0; i < types.length; ++i) {
					this.processTypeFields(methodNames, methods, types[i]);
				}
			}
		};
		IDLTKLanguageToolkit toolkit = null;
		toolkit = DLTKLanguageManager.getLanguageToolkit(this.scriptProject);
		IDLTKSearchScope scope = SearchEngine.createWorkspaceScope(toolkit);
		if (token.length >= 1 && token[0] == '$') {
			char[] token2 = new char[token.length - 1];
			for (int i = 0; i < token.length - 1; ++i) {
				token2[i] = token[i + 1];
			}
			token = token2;
		}
		final String to = new String(token);
		if (token != null && token.length >= 3 && token[0] == ':') {
			// && token[1] == ':'
			provideDots = true;
			String[] tokens = TclParseUtil.tclSplit(to);
			if (tokens.length < 2) {
				return;
			}
			final String tok = tokens[1];
			try {
				this.search(tok + "*", IDLTKSearchConstants.TYPE,
						IDLTKSearchConstants.DECLARATIONS, scope, requestor);
				int nonNoneCount = 0;
				String mtok = null;
				for (int i = 0; i < tokens.length; ++i) {
					if (tokens[i].length() > 0) {
						nonNoneCount++;
						if (mtok == null) {
							mtok = tokens[i];
						}
					}
				}
				if (nonNoneCount == 1 && tok.length() >= 2) {
					this
							.search(mtok + "*", IDLTKSearchConstants.FIELD,
									IDLTKSearchConstants.DECLARATIONS, scope,
									requestor);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (DLTKCore.VERBOSE_COMPLETION) {
				System.out.println("Completion methods cound:" + fields.size());
			}
		} else if (token != null && token.length >= 1 && token[0] != ':') {
			try {
				String[] tokens = TclParseUtil.tclSplit(to);
				if (tokens.length == 0) {
					return;
				}
				String tok = tokens[0];
				this.search(tok + "*", IDLTKSearchConstants.TYPE,
						IDLTKSearchConstants.DECLARATIONS, scope, requestor);
				int nonNoneCount = 0;
				for (int i = 0; i < tokens.length; ++i) {
					if (tokens[i].length() > 0) {
						nonNoneCount++;
					}
				}
				if (nonNoneCount == 1 && tok.length() >= 2) {
					this
							.search(tok + "*", IDLTKSearchConstants.FIELD,
									IDLTKSearchConstants.DECLARATIONS, scope,
									requestor);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (DLTKCore.VERBOSE_COMPLETION) {
				System.out.println("Completion methods cound:" + fields.size());
			}
		}
		// this.findTypes(token, true, types);
		this.findFields(token, false, fields, new FieldNameProvider(to,
				provideDollar ? "$" : ""));
	}

	protected void findASTVariables(ASTNode node, String prefix, char[] token,
			boolean canCompleteEmptyToken, List choices) {
		List statements = null;
		String add = "";
		if (node instanceof ModuleDeclaration) {
			statements = ((ModuleDeclaration) node).getStatements();
		} else if (node instanceof TypeDeclaration) {
			if (!isTclType((TypeDeclaration) node)) {
				return;
			}
			statements = ((TypeDeclaration) node).getStatements();
			String nme = ((TypeDeclaration) node).getName();
			if (nme.startsWith("::")) {
				add = nme.substring(2) + "::";
			} else {
				add = nme + "::";
			}
		}
		if (statements != null) {
			for (int i = 0; i < statements.size(); ++i) {
				ASTNode nde = (ASTNode) statements.get(i);
				if (nde instanceof TclStatement) {
					String[] variable = OldTclParserUtils
							.returnVariable((TclStatement) nde);
					if (variable != null) {
						for (int u = 0; u < variable.length; ++u) {
							String prev = this.preProcessVariable(variable[u])
									.substring(1);
							String var = prefix + add;
							if (var.endsWith("::") && prev.startsWith("::")) {
								var = var + prev.substring(2);
							} else {
								var = var + prev;
							}
							if (!choices.contains(var)) {
								choices.add(var);
							}
						}
						continue;
					}
				} else if (nde instanceof FieldDeclaration) {
					FieldDeclaration field = (FieldDeclaration) nde;
					if (this.isTclField(field)) {
						this.checkAddVariable(choices, prefix + add
								+ field.getName());
					}
					continue;
				}
				this.findASTVariables(nde, prefix + add, token,
						canCompleteEmptyToken, choices);
			}
		}
	}

	private void checkVariables(char[] token, boolean canCompleteEmptyToken,
			int beforePosition, List statements, boolean provideDollar,
			List gChoices) {
		List choices = new ArrayList();
		// Process variable setters.
		this.checkVariableStatements(beforePosition, choices, statements);
		String[] cc = new String[choices.size()];
		for (int i = 0; i < choices.size(); ++i) {
			cc[i] = (String) choices.get(i);
			gChoices.add(choices.get(i));
		}
		this
				.findLocalVariables(token, cc, canCompleteEmptyToken,
						provideDollar);
	}

	protected void checkVariableStatements(int beforePosition,
			final List choices, List statements) {
		if (statements != null) {
			for (int i = 0; i < statements.size(); ++i) {
				ASTNode node = (ASTNode) statements.get(i);
				if (node instanceof FieldDeclaration) {
					FieldDeclaration decl = (FieldDeclaration) node;
					if (this.isTclField(decl)) {
						this.checkAddVariable(choices, decl.getName());
					}
				} else if (node instanceof TclStatement
						&& node.sourceEnd() < beforePosition) {
					TclStatement s = (TclStatement) node;
					this.checkTclStatementForVariables(choices, s);
				} else {
					ASTVisitor visitor = new ASTVisitor() {
						public boolean visit(Statement s) throws Exception {
							if (s instanceof FieldDeclaration) {
								String name = TclParseUtil.getElementFQN(s,
										"::", TclCompletionEngine.this.parser
												.getModule());
								ASTNode pp = TclParseUtil.getScopeParent(parser
										.getModule(), s);
								boolean isTcl = true;
								if (pp instanceof TypeDeclaration) {
									isTcl = isTclType((TypeDeclaration) pp);
								}
								if (pp instanceof MethodDeclaration) {
									isTcl = isTclMethod((MethodDeclaration) pp);
								}
								if (isTcl
										&& TclCompletionEngine.this
												.isTclField((FieldDeclaration) s)) {
									TclCompletionEngine.this.checkAddVariable(
											choices, name);
									// ((FieldDeclaration)s).getName()
								}
							} else if (s instanceof TclStatement) {
								TclCompletionEngine.this
										.checkTclStatementForVariables(choices,
												(TclStatement) s);
							}
							return super.visit(s);
						}

					};
					try {
						node.traverse(visitor);
					} catch (Exception e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private void checkTclStatementForVariables(final List choices,
			TclStatement s) {
		String[] variable = OldTclParserUtils.returnVariable(s);
		if (variable != null) {
			for (int u = 0; u < variable.length; ++u) {
				this.checkAddVariable(choices, variable[u]);
			}
		}
	}

	protected void checkAddVariable(List choices, String n) {
		String str = this.preProcessVariable(n);
		if (!choices.contains(str)) {
			choices.add(str);
		}
	}

	private String preProcessVariable(String n) {
		String str;
		if (n.startsWith("$")) {
			return n;
		}
		if (n.indexOf(' ') != -1) {
			str = "$" + '{' + n + '}';
		} else {
			str = "$" + n;
		}
		return str;
	}

	public IAssistParser getAssistParser() {
		return this.parser;
	}

	// TODO: Remove this. Actually all are done in extending of tcl statements
	// in TclCompletionParser.
	protected int getEndOfEmptyToken() {
		// TODO: Add more complicated code here...
		return this.actualCompletionPosition;
	}

	protected String processMethodName(IMethod method, String tok) {
		return OldTclParserUtils.processMethodName(method, tok);
	}

	public static final ICompletionNameProvider<IField> FIELD_NAME_PROVIDER = new ICompletionNameProvider<IField>() {

		public String getCompletion(IField t) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getName(IField t) {
			return t.getElementName();
		}
	};

	// protected String processFieldName(IField method, String tok) {
	// return OldTclParserUtils.processFieldName(method, tok);
	// }

	protected String processTypeName(IType method, String tok) {
		String name = OldTclParserUtils.processTypeName(method, tok);
		if (name.startsWith("::")
				&& ((tok.length() > 1 && tok.charAt(0) != ':' && tok.charAt(1) != ':') || tok
						.length() == 0)) {
			name = name.substring(2);
		}
		return name;

	}

	public CompletionRequestor getRequestor() {
		return this.requestor;
	}

	public ISourceModule getSourceModule() {
		return this.sourceModule;
	}

	public int getActualCompletionPosition() {
		return this.actualCompletionPosition;
	}
}
