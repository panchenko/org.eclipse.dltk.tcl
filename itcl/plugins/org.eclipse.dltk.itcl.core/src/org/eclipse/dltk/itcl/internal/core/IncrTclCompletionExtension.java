package org.eclipse.dltk.itcl.internal.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.codeassist.complete.CompletionNodeFound;
import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.Flags;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.mixin.IMixinRequestor;
import org.eclipse.dltk.itcl.internal.core.parser.IncrTclParseUtil;
import org.eclipse.dltk.itcl.internal.core.parser.ast.IncrTclExInstanceVariable;
import org.eclipse.dltk.itcl.internal.core.parser.ast.IncrTclInstanceVariable;
import org.eclipse.dltk.itcl.internal.core.parser.ast.IncrTclMethodCallStatement;
import org.eclipse.dltk.itcl.internal.core.parser.ast.IncrTclMethodDeclaration;
import org.eclipse.dltk.itcl.internal.core.search.mixin.model.IncrTclClass;
import org.eclipse.dltk.itcl.internal.core.search.mixin.model.IncrTclClassInstance;
import org.eclipse.dltk.itcl.internal.core.search.mixin.model.IncrTclInstProc;
import org.eclipse.dltk.itcl.internal.core.search.mixin.model.IncrTclProc;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.extensions.ICompletionExtension;
import org.eclipse.dltk.tcl.internal.core.codeassist.ITclCompletionProposalTypes;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclCompletionEngine;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclResolver;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.CompletionOnKeywordArgumentOrFunctionArgument;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.CompletionOnKeywordOrFunction;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.CompletionOnVariable;
import org.eclipse.dltk.tcl.internal.core.codeassist.completion.TclCompletionParser;

public class IncrTclCompletionExtension implements ICompletionExtension {

	private CompletionRequestor requestor;

	public boolean visit(Expression s, TclCompletionParser parser, int position) {
		List exprs = new ArrayList();
		if (s instanceof IncrTclMethodCallStatement) {
			IncrTclMethodCallStatement pcs = (IncrTclMethodCallStatement) s;

			exprs.add(pcs.getInstNameRef());
			exprs.add(pcs.getCallName());
			if (pcs.getArgs() != null) {
				exprs.addAll(pcs.getArgs().getChilds());
			}
			processArgumentCompletion(s, exprs, parser, position);
		}
		return false;
	}

	public boolean visit(Statement s, TclCompletionParser parser, int position) {
		return false;
	}

	private void processArgumentCompletion(Statement s, List exprs,
			TclCompletionParser parser, int position) {
		TclStatement statement = new TclStatement(exprs);
		statement.setStart(s.sourceStart());
		statement.setEnd(s.sourceEnd());

		ASTNode completionNode = null;
		for (int i = 0; i < exprs.size(); ++i) {
			ASTNode n = (ASTNode) exprs.get(i);
			if (n.sourceStart() <= position && n.sourceEnd() >= position) {
				completionNode = n;
			}
		}
		String token = "";
		if (completionNode != null && completionNode instanceof SimpleReference) {
			token = ((SimpleReference) completionNode).getName();
		}
		String[] keywords;
		if (token == null) {
			keywords = parser.checkKeywords("", TclCompletionParser.MODULE);
		} else {
			keywords = parser.checkKeywords(token, TclCompletionParser.MODULE);
		}
		if (completionNode != null) {
			ASTNode nde = new CompletionOnKeywordArgumentOrFunctionArgument(
					token, completionNode, statement, keywords);
			parser.setAssistNodeParent(TclParseUtil.getPrevParent(parser
					.getModule(), s));
			throw new CompletionNodeFound(nde, null);
			// ((TypeDeclaration)inNode).scope
		} else {
			ASTNode nde = new CompletionOnKeywordArgumentOrFunctionArgument(
					token, statement, keywords, position);
			parser.setAssistNodeParent(TclParseUtil.getPrevParent(parser
					.getModule(), s));
			throw new CompletionNodeFound(nde, null);
			// ((TypeDeclaration)inNode).scope
		}
	}

	public void completeOnKeywordOrFunction(CompletionOnKeywordOrFunction key,
			ASTNode astNodeParent, TclCompletionEngine engine) {
		if (!engine.getRequestor().isIgnored(CompletionProposal.TYPE_REF)) {
			Set methodNames = new HashSet();
			char[] token = key.getToken();
			token = engine.removeLastColonFromToken(token);
			findLocalIncrTclClasses(token, methodNames, astNodeParent, engine);
			// findLocalClasses
			findIncrTclClasses(token, methodNames, engine);
		}
		if (!engine.getRequestor().isIgnored(CompletionProposal.FIELD_REF)) {
			Set methodNames = new HashSet();
			char[] token = key.getToken();
			token = engine.removeLastColonFromToken(token);
			// findLocalClassInstances
			findLocalIncrTclClassInstances(token, methodNames, astNodeParent,
					engine);
			findIncrTclClassInstances(token, methodNames, engine);

			// Add $this
			ASTNode node = key.getInParent();
			if (node instanceof IncrTclMethodDeclaration) {
				engine.findKeywords(token,
						new char[][] { "$this".toCharArray() }, true);
				findVariables(key.getToken(), true, true, engine, node);
			}
		}
		if (!engine.getRequestor().isIgnored(CompletionProposal.METHOD_REF)) {
			char[] token = key.getToken();
			token = engine.removeLastColonFromToken(token);
			findClassMethods(token, astNodeParent, engine, true);

		}
	}

	private void findClassMethods(char[] token, ASTNode astNodeParent,
			TclCompletionEngine engine, boolean addTop) {
		TclResolver resolver = new TclResolver(engine.getSourceModule(), engine
				.getParser().getModule());
		List methods = new ArrayList();
		List methodNames = new ArrayList();
		String prefix = "";
		if (token.length > 0 && token[0] == ':') {
			prefix = "::";
		}
		if (astNodeParent instanceof IncrTclMethodDeclaration) {
			IncrTclMethodDeclaration decl = (IncrTclMethodDeclaration) astNodeParent;
			MethodDeclaration[] statements = ((TypeDeclaration) decl
					.getDeclaringType()).getMethods();
			for (int i = 0; i < statements.length; i++) {
				ASTNode st = statements[i];
				if (st instanceof IncrTclMethodDeclaration) {
					IModelElement element = resolver.findModelElementFrom(st);
					methods.add(element);
					methodNames.add(element.getElementName());
					if (addTop) {
						if (element instanceof IMethod) {
							IMethod m = (IMethod) element;
							try {
								methodNames.add(prefix
										+ m.getTypeQualifiedName("::", false));
								methods.add(element);
							} catch (ModelException e) {
								if (DLTKCore.DEBUG) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		engine.findMethods(token, true, methods, methodNames);
	}

	private void findLocalIncrTclClasses(char[] token, Set methodNames,
			ASTNode astNodeParent, TclCompletionEngine engine) {
		ASTNode parent = TclParseUtil.getScopeParent(engine.getParser()
				.getModule(), astNodeParent);
		// We need to process all xotcl classes.

		Set classes = new HashSet();

		findIncrTclClassessIn(parent, classes, engine);
		engine.removeSameFrom(methodNames, classes, new String(token));
		engine.findTypes(token, true, engine.toList(classes));
		methodNames.addAll(classes);
	}

	private void findLocalIncrTclClassInstances(char[] token, Set methodNames,
			ASTNode astNodeParent, TclCompletionEngine engine) {
		ASTNode parent = TclParseUtil.getScopeParent(engine.getParser()
				.getModule(), astNodeParent);
		// We need to process all xotcl classes.

		Set classes = new HashSet();

		findIncrTclClassInstancesIn(parent, classes, engine);
		engine.removeSameFrom(methodNames, classes, new String(token));
		engine.findFields(token, true, engine.toList(classes), "");
		methodNames.addAll(classes);
		// Also use not fully qualified names
		// String elementFQN = TclParseUtil.getElementFQN(parent, "::",
		// parser.getModule());
	}

	private void findIncrTclClassessIn(ASTNode parent, Set classes,
			final TclCompletionEngine engine) {
		// List statements = TclASTUtil.getStatements(parent);
		final List result = new ArrayList();
		final TclResolver resolver = new TclResolver(engine.getSourceModule(),
				engine.getParser().getModule(), null);

		try {
			engine.getParser().getModule().traverse(new ASTVisitor() {
				// for (Iterator iterator = statements.iterator();
				// iterator.hasNext();) {
				// ASTNode nde = (ASTNode) iterator.next();
				public boolean visit(TypeDeclaration type) {
					if ((type.getModifiers() & IIncrTclModifiers.AccIncrTcl) != 0
							&& type.sourceStart() < engine
									.getActualCompletionPosition()) {
						// we need to find model element for selected type.
						resolver.searchAddElementsTo(engine.getParser()
								.getModule().getStatements(), type, engine
								.getSourceModule(), result);
					}
					return true;
				}
			});
		} catch (Exception e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		classes.addAll(result);
	}

	private void findIncrTclClassInstancesIn(ASTNode parent, Set classes,
			final TclCompletionEngine engine) {
		// List statements = TclASTUtil.getStatements(parent);
		final List result = new ArrayList();
		final TclResolver resolver = new TclResolver(engine.getSourceModule(),
				engine.getParser().getModule(), null);

		try {
			parent.traverse(new ASTVisitor() {
				public boolean visit(Statement st) {
					if (st instanceof IncrTclInstanceVariable
					/* || st instanceof XOTclExInstanceVariable */) {
						// we need to find model element for selected type.
						resolver.searchAddElementsTo(engine.getParser()
								.getModule().getStatements(), st, engine
								.getSourceModule(), result);
					}
					return true;
				}
			});
		} catch (Exception e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		classes.addAll(result);
	}

	private void findIncrTclClassInstances(char[] token, Set methodNames,
			TclCompletionEngine engine) {
		// IDLTKSearchScope scope = SearchEngine.createWorkspaceScope(toolkit);
		String to_ = new String(token);
		String to = to_;
		if (to.startsWith("::")) {
			to = to.substring(2);
		}
		if (to.length() == 0) {
			return;
		}
		Set elements = new HashSet();
		elements.addAll(methodNames);
		findClassesInstanceFromMixin(elements, to + "*", engine);
		engine.removeSameFrom(methodNames, elements, to);
		engine.findFields(token, true, engine.toList(elements), "");
		methodNames.addAll(elements);
	}

	private void completionForInstanceVariableMethods(FieldDeclaration var,
			char[] token, Set methodNames, TclCompletionEngine engine) {
		String keyPrefix = null;
		if (var instanceof IncrTclInstanceVariable) {
			IncrTclInstanceVariable ivar = (IncrTclInstanceVariable) var;
			TypeDeclaration declaringType = ivar.getDeclaringType();
			keyPrefix = TclParseUtil.getElementFQN(declaringType,
					IMixinRequestor.MIXIN_NAME_SEPARATOR, engine.getParser()
							.getModule());
			if (keyPrefix.startsWith(IMixinRequestor.MIXIN_NAME_SEPARATOR)) {
				keyPrefix = keyPrefix.substring(1);
			}

		} else if (var instanceof IncrTclExInstanceVariable) {
			IncrTclExInstanceVariable ivar = (IncrTclExInstanceVariable) var;
			String className = ivar.getDeclaringClassParameter().getClassName();
			if (className.startsWith("::")) {
				className = className.substring(2);
			}
			keyPrefix = className;
		}

		Set methods = new HashSet();
		findClassesFromMixin(methods, keyPrefix, engine);
		Set result = new HashSet();
		// replace class name with methods.
		while (methods.size() > 0) {
			IModelElement e = (IModelElement) methods.iterator().next();
			methods.remove(e);
			if (e instanceof IType) {
				IType type = (IType) e;
				try {
					IMethod[] ms = type.getMethods();
					for (int i = 0; i < ms.length; i++) {
						if (!Flags.isPublic(ms[i].getFlags())) {
							if (this.requestor != null
									&& this.requestor
											.isIgnored(ITclCompletionProposalTypes.FILTER_INTERNAL_API)) {
								continue;
							}
						}
						int flags = ((IMethod) ms[i]).getFlags();
						if ((flags & IIncrTclModifiers.AccConstructor) == 0
								&& (flags & IIncrTclModifiers.AccDestructor) == 0)
							result.add(ms[i]);
					}
				} catch (ModelException e1) {
					if (DLTKCore.DEBUG) {
						e1.printStackTrace();
					}
				}
				// Add super types information.
				try {
					String[] superClasses = type.getSuperClasses();
					if (superClasses != null) {
						for (int i = 0; i < superClasses.length; i++) {
							String key = superClasses[i];
							if (key.startsWith("::")) {
								key = key.substring(2);
							}
							if (key.length() > 0) {
								findClassesFromMixin(methods, key, engine);
							}
						}
					}
				} catch (ModelException e1) {
					if (DLTKCore.DEBUG) {
						e1.printStackTrace();
					}
				}
			} else if (e instanceof IMethod) {
				try {
					int flags = ((IMethod) e).getFlags();
					if (!Flags.isPublic(flags)) {
						if (this.requestor != null
								&& this.requestor
										.isIgnored(ITclCompletionProposalTypes.FILTER_INTERNAL_API)) {
							continue;
						}
					}
					if ((flags & IIncrTclModifiers.AccConstructor) == 0
							&& (flags & IIncrTclModifiers.AccDestructor) == 0)
						result.add(e);
				} catch (ModelException e1) {
					if (DLTKCore.DEBUG) {
						e1.printStackTrace();
					}
				}
			}

		}
		findMethodsShortName(token, result, methodNames, engine);
		// We need to handle supers

		// addKeywords(token, IncrTclKeywords., methodNames,
		// engine);
		// addKeywords(token, IncrTclKeywords.XOTclCommandObjectArgs,
		// methodNames,
		// engine);
	}

	// protected boolean methodCanBeAdded(ASTNode nde, TclCompletionEngine
	// engine) {
	// if (nde instanceof XOTclMethodDeclaration) {
	// return false;
	// }
	// return super.methodCanBeAdded(nde);
	// }

	private void completeClassMethods(String name, char[] cs, Set methodNames,
			TclCompletionEngine engine) {
		Set completions = new HashSet();

		if (name.startsWith("::")) {
			name = name.substring(2);
		}
		findClassesFromMixin(completions, name, engine);
		if (completions.size() >= 1) { // We found class with such name, so
			// this is method completion.
			Set methods = new HashSet();
			methods.addAll(methodNames);
			findProcsFromMixin(methods, name + "::*", engine);
			methods.removeAll(methodNames);
			findMethodsShortName(cs, methods, methodNames, engine);
			// We also need to add Object and Class methods
			// We need to add superclass methods
			// addKeywords(cs, IncrTclKeywords.XOTclCommandClassArgs,
			// methodNames,
			// engine);
			// addKeywords(cs, IncrTclKeywords.XOTclCommandObjectArgs,
			// methodNames,
			// engine);
		}
	}

	private void addKeywords(char[] cs, String[] keywords, Set methodNames,
			TclCompletionEngine engine) {
		List k = new ArrayList();
		String token = new String(cs);
		for (int i = 0; i < keywords.length; ++i) {
			String kkw = keywords[i];
			if (kkw.startsWith(token)) {
				if (!methodNames.contains(kkw)) {
					k.add(kkw);
					methodNames.add(kkw);
				}
			}
		}
		String kw[] = (String[]) k.toArray(new String[k.size()]);
		char[][] choices = new char[kw.length][];
		for (int i = 0; i < kw.length; ++i) {
			choices[i] = kw[i].toCharArray();
		}
		engine.findKeywords(cs, choices, true);
	}

	private void findMethodsShortName(char[] cs, Set methods, Set allMethods,
			TclCompletionEngine engine) {
		List methodsList = engine.toList(methods);
		List methodNames = new ArrayList();
		List remove = new ArrayList();
		for (Iterator iterator = methodsList.iterator(); iterator.hasNext();) {
			IMethod method = (IMethod) iterator.next();
			String methodName = method.getElementName();
			if (!methodNames.contains(methodName)) {
				methodNames.add(methodName);
				allMethods.add(methodName);
			} else {
				remove.add(method);
			}
		}
		for (Iterator iterator = remove.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			methodsList.remove(object);
		}
		engine.findMethods(cs, true, methodsList, methodNames);
	}

	protected void findProcsFromMixin(final Set methods, String tok,
			TclCompletionEngine engine) {
		engine.findMixinTclElement(methods, tok, IncrTclProc.class);
	}

	protected void findInstProcsFromMixin(final Set methods, String tok,
			TclCompletionEngine engine) {
		engine.findMixinTclElement(methods, tok, IncrTclInstProc.class);
	}

	private void findIncrTclClasses(char[] token, Set methodNames,
			TclCompletionEngine engine) {

		// IDLTKSearchScope scope = SearchEngine.createWorkspaceScope(toolkit);
		String to_ = new String(token);
		String to = to_;
		if (to.startsWith("::")) {
			to = to.substring(2);
		}
		if (to.length() == 0) {
			return;
		}
		Set methods = new HashSet();
		methods.addAll(methodNames);
		findClassesFromMixin(methods, to + "*", engine);
		methods.removeAll(methodNames);
		engine.removeSameFrom(methodNames, methods, to_);

		engine.findTypes(token, true, engine.toList(methods));
	}

	protected void findClassesFromMixin(final Set completions, String tok,
			TclCompletionEngine engine) {
		engine.findMixinTclElement(completions, tok, IncrTclClass.class);
	}

	protected void findClassesInstanceFromMixin(final Set completions,
			String tok, TclCompletionEngine engine) {
		engine
				.findMixinTclElement(completions, tok,
						IncrTclClassInstance.class);
	}

	private FieldDeclaration searchFieldFromMixin(String name,
			TclCompletionEngine engine) {
		return null;
	}

	public void completeOnKeywordArgumentsOne(String name, char[] token,
			CompletionOnKeywordArgumentOrFunctionArgument compl,
			Set methodNames, TclStatement st, TclCompletionEngine engine) {
		// Check for class and its methods.
		completeClassMethods(name, token, methodNames, engine);

		// Lets find instance with specified name.
		FieldDeclaration var = IncrTclParseUtil
				.findInstanceVariableDeclarationFrom(engine.getParser()
						.getModule(), TclParseUtil.getScopeParent(engine
						.getParser().getModule(), st), name);
		if (var == null) {
			var = searchFieldFromMixin(name, engine);
		}
		if (var != null) {
			completionForInstanceVariableMethods(var, token, methodNames,
					engine);
		}
		if ("$this".equals(name)) {
			if (compl.argumentIndex() == 1) {
				ASTNode inNode = TclParseUtil.getScopeParent(engine.getParser()
						.getModule(), st);
				findClassMethods(token, inNode, engine, false);
			}
		}
	}

	public void setRequestor(CompletionRequestor requestor) {
		this.requestor = requestor;
	}

	public void completeOnVariables(CompletionOnVariable astNode,
			TclCompletionEngine engine) {
		ASTNode inNode = astNode.getInNode();
		findVariables(astNode.getToken(), astNode.canHandleEmpty(), astNode
				.getProvideDollar(), engine, inNode);
	}

	private void findVariables(char[] token, boolean canHandleEmpty,
			boolean provideDollar, TclCompletionEngine engine, ASTNode inNode) {
		if (inNode instanceof IncrTclMethodDeclaration) {
			engine.findKeywords(token, new char[][] { "$this".toCharArray() },
					false);
			// Process all class public fields.
			IncrTclMethodDeclaration method = (IncrTclMethodDeclaration) inNode;
			TypeDeclaration type = (TypeDeclaration) method.getDeclaringType();
			List list = type.getStatements();
			List fields = new ArrayList();
			List fieldNames = new ArrayList();
			TclResolver resolver = new TclResolver(engine.getSourceModule(),
					engine.getParser().getModule());
			for (int i = 0; i < list.size(); i++) {
				ASTNode nde = (ASTNode) list.get(i);
				if (nde instanceof FieldDeclaration) {
					FieldDeclaration field = (FieldDeclaration) nde;
					IModelElement element = resolver
							.findModelElementFrom(field);
					if (Flags.isPublic(field.getModifiers())
							|| !engine
									.getRequestor()
									.isIgnored(
											ITclCompletionProposalTypes.FILTER_INTERNAL_API)) {
						fields.add(element);
						if (provideDollar) {
							fieldNames.add("$" + element.getElementName());
						} else {
							fieldNames.add(element.getElementName());
						}
					}
				}
			}
			if (fields.size() > 0) {
				engine.findFields(token, canHandleEmpty, fields,
						CompletionProposal.FIELD_REF, fieldNames);
			}
		}
	}

	public boolean modelFilter(Set completions, IModelElement modelElement) {
		if (modelElement.getElementType() == IModelElement.METHOD) {
			IMethod method = (IMethod) modelElement;
			try {
				if ((method.getFlags() & IIncrTclModifiers.AccConstructor) != 0) {
					return false;
				}
				if ((method.getFlags() & IIncrTclModifiers.AccDestructor) != 0) {
					return false;
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
