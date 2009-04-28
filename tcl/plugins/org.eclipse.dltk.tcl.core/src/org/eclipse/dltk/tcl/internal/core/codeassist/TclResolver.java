package org.eclipse.dltk.tcl.internal.core.codeassist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.ast.TclPackageDeclaration;
import org.eclipse.dltk.tcl.internal.core.packages.TclBuildPathPackageCollector;
import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinModel;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclPackage;
import org.eclipse.dltk.tcl.internal.parser.OldTclParserUtils;

public class TclResolver {
	private IResolveElementParent resolver;
	private ModuleDeclaration moduleDeclaration;
	private ISourceModule sourceModule;

	public TclResolver(ISourceModule sourceModule,
			ModuleDeclaration moduleDeclaration, IResolveElementParent resolver) {
		this(sourceModule, moduleDeclaration);
		this.resolver = resolver;
	}

	public TclResolver(ISourceModule sourceModule,
			ModuleDeclaration moduleDeclaration) {
		this.sourceModule = sourceModule;
		this.moduleDeclaration = moduleDeclaration;
	}

	public IModelElement findModelElementFrom(ASTNode node) {
		List statements = moduleDeclaration.getStatements();
		List elements = new ArrayList();
		searchAddElementsTo(statements, node, sourceModule, elements);
		if (elements.size() == 1) {
			return (IModelElement) elements.get(0);
		}
		return null;
	}

	public interface IResolveElementParent {
		IModelElement findElementParent(ASTNode node, String name,
				IParent parent);
	}

	public void searchAddElementsTo(List statements, final ASTNode node,
			IParent element, List selectionElements) {
		if (statements == null || element == null) {
			return;
		}
		Iterator i = statements.iterator();
		while (i.hasNext()) {
			ASTNode nde = (ASTNode) i.next();
			if (nde.equals(node)) {
				if (node instanceof MethodDeclaration) {
					String oName = ((MethodDeclaration) node).getName();
					if (oName.indexOf("::") != -1) {
						String pName = oName.substring(0, oName
								.lastIndexOf("::"));
						pName = pName.replaceAll("::", "\\$");

						if (pName.startsWith("$")) {
							if (pName.equals("$")) {
								element = sourceModule;
							} else {
								try {
									element = findTypeFrom(sourceModule
											.getChildren(), "", pName, '$');
								} catch (ModelException e) {
									if (DLTKCore.DEBUG) {
										e.printStackTrace();
									}
								}
							}
						} else {
							pName = "$" + pName;
							try {
								element = findTypeFrom(element.getChildren(),
										"", pName, '$');
								if (element == null) {
									return;
								}
							} catch (ModelException e) {
								e.printStackTrace();
								return;
							}
						}
					}
				}
				String nodeName = getNodeChildName(node);
				if (nodeName != null) {
					IModelElement e = null;
					if (nodeName.startsWith("::")) {
						nodeName = nodeName.substring(2);
						e = findChildrenByName(nodeName, sourceModule);
					} else {
						e = findChildrenByName(nodeName, element);
					}
					if (e == null && resolver != null) {
						e = resolver.findElementParent(node, nodeName, element);
					}
					if (e != null) {
						List toRemove = new ArrayList();
						for (int k = 0; k < selectionElements.size(); ++k) {
							IModelElement ke = (IModelElement) selectionElements
									.get(k);
							String keName = ke.getElementName();
							if (keName.equals(nodeName)) {
								toRemove.add(ke);
							}
						}
						for (int k = 0; k < toRemove.size(); ++k) {
							selectionElements.remove(toRemove.get(k));
						}
						selectionElements.add(e);
					}
				}
				return;
			}
			if (nde.sourceStart() <= node.sourceStart()
					&& node.sourceEnd() <= nde.sourceEnd()) {
				if (element instanceof IParent) {
					if (nde instanceof TypeDeclaration) {
						TypeDeclaration type = (TypeDeclaration) nde;
						String typeName = getNodeChildName(type);
						IModelElement e = findChildrenByName(typeName,
								(IParent) element);
						if (e == null && type.getName().startsWith("::")) {
							try {
								e = (IModelElement) findTypeFrom(sourceModule
										.getChildren(), "", type.getName()
										.replaceAll("::", "\\$"), '$');
							} catch (ModelException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (e instanceof IParent) {
							// was: if (e != null || e instanceof IParent)
							List stats = ((TypeDeclaration) nde)
									.getStatements();
							searchAddElementsTo(stats, node, (IParent) e,
									selectionElements);
						}
					} else if (nde instanceof MethodDeclaration) {
						searchInMethod(node, element, nde, selectionElements);
					} /*
					 * else if (nde instanceof TclStatement) { TclStatement s =
					 * (TclStatement) nde; Expression commandId = s.getAt(0);
					 * final IParent e = element; if (commandId != null &&
					 * commandId instanceof SimpleReference) { String qname =
					 * ((SimpleReference) commandId) .getName(); } }
					 */
					else {
						final IParent e = element;
						List statements2 = findExtractBlocks(nde);
						if (statements2.size() > 0) {
							searchAddElementsTo(statements2, node, e,
									selectionElements);
						}
					}
				}
				return;
			}
		}
	}

	public static IModelElement findChildrenByName(String childName,
			IParent element) {
		try {
			if (element == null) {
				return null;
			}
			String nextName = null;
			int pos;
			if ((pos = childName.indexOf("::")) != -1) {
				nextName = childName.substring(pos + 2);
				// childName = "";
				String[] split = childName.split("::");
				if (split.length > 0) {
					childName = split[0];
				}
			}
			IModelElement[] children = element.getChildren();
			if (children != null) {
				for (int i = 0; i < children.length; ++i) {
					String name = children[i].getElementName();
					if (children[i] instanceof IField
							&& name.indexOf('(') != -1) {
						name = name.substring(0, name.indexOf('('));
					}
					if (name.equals(childName)) {
						if (nextName == null) {
							return children[i];
						} else if (children[i] instanceof IParent) {
							return findChildrenByName(nextName,
									(IParent) children[i]);
						}
					}
				}
			}
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static IParent findTypeFrom(IModelElement[] childs, String name,
			String parentName, char delimiter) {
		try {
			for (int i = 0; i < childs.length; ++i) {
				if (childs[i] instanceof IType) {
					// if ((((IType) childs[i]).getFlags() &
					// Modifiers.AccNameSpace) == 0) {
					// continue;
					// }
					IType type = (IType) childs[i];
					String qname = name + delimiter + type.getElementName();
					if (qname.equals(parentName)) {
						return type;
					}
					IParent val = findTypeFrom(type.getChildren(), qname,
							parentName, delimiter);
					if (val != null) {
						return val;
					}
				}
			}
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getNodeChildName(ASTNode node) {
		if (node instanceof MethodDeclaration) {
			MethodDeclaration method = (MethodDeclaration) node;
			String name = method.getName();
			if (name.indexOf("::") != -1) {
				return name.substring(name.lastIndexOf("::") + 2);
			}
			return name;
		} else if (node instanceof TypeDeclaration) {
			TypeDeclaration type = (TypeDeclaration) node;
			String name = type.getName();
			/*
			 * if (name.startsWith("::")) { return name.substring(2); }
			 */
			return name;
		} else if (node instanceof TclStatement) {
			String[] var = OldTclParserUtils.returnVariable((TclStatement) node);
			if (var != null) {
				return var[0];
			}
		} else if (node instanceof FieldDeclaration) {
			return ((FieldDeclaration) node).getName();
		}
		return null;
	}

	public void searchInMethod(final ASTNode node, IParent element,
			ASTNode nde, List selectionElements) {
		MethodDeclaration method = (MethodDeclaration) nde;
		String methodName = method.getName();
		if (methodName.indexOf("::") != -1) {
			String pName = methodName
					.substring(0, methodName.lastIndexOf("::"));
			pName = pName.replaceAll("::", "\\$");
			if (pName.equals("$")) {
				element = sourceModule;
			} else {
				try {
					element = TclResolver.findTypeFrom(sourceModule
							.getChildren(), "", pName, '$');
					if (element == null) {
						return;
					}
				} catch (ModelException e) {
					e.printStackTrace();
					return;
				}
			}
			methodName = TclResolver.getNodeChildName(nde);
		} else {
			if (method.getDeclaringTypeName() != null) {
				String pName = method.getDeclaringTypeName();
				if (!pName.startsWith("::")) {
					pName = "$" + pName;
				}
				pName = pName.replaceAll("::", "\\$");
				if (pName.equals("$")) {
					element = sourceModule;
				} else {
					try {
						element = TclResolver.findTypeFrom(sourceModule
								.getChildren(), "", pName, '$');
						if (element == null) {
							return;
						}
					} catch (ModelException e) {
						e.printStackTrace();
						return;
					}
				}
				methodName = TclResolver.getNodeChildName(nde);
			}
		}
		IModelElement e = TclResolver.findChildrenByName(methodName,
				(IParent) element);
		if (e != null && e instanceof IParent) {
			List stats = ((MethodDeclaration) nde).getStatements();
			searchAddElementsTo(stats, node, (IParent) e, selectionElements);
		}
	}

	public static List findExtractBlocks(ASTNode node) {
		final List statements2 = new ArrayList();
		ASTVisitor visitor = new ASTVisitor() {
			public boolean visit(Expression s) throws Exception {
				if (s instanceof Block) {
					statements2.addAll(((Block) s).getStatements());
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
		return statements2;
	}

	public static List processReferenceModules(List packages,
			IScriptProject scriptProject) {
		Set allModules = new HashSet();
		List orderedModules = new ArrayList();
		// IInterpreterInstall install = null;
		// try {
		// install = ScriptRuntime.getInterpreterInstall(scriptProject);
		// } catch (CoreException e1) {
		// if (DLTKCore.DEBUG) {
		// e1.printStackTrace();
		// }
		// }
		// List required = null;
		// if (install != null) {
		// required = new ArrayList();
		// Set req = new HashSet(required);
		// for (Iterator iterator = packages.iterator(); iterator.hasNext();) {
		// String pkg = (String) iterator.next();
		// required.add(pkg);
		// Set dependencies = PackagesManager.getInstance()
		// .getDependencies(pkg, install).keySet();
		// for (Iterator iterator2 = dependencies.iterator(); iterator2
		// .hasNext();) {
		// String depPkg = (String) iterator2.next();
		// if (req.add(depPkg)) {
		// required.add(depPkg);
		// }
		// }
		// }
		// } else {
		// required = packages;
		// }
		List required = packages;
		// We need to look for all required packages from selected
		// module
		for (Iterator iterator2 = required.iterator(); iterator2.hasNext();) {
			String requiredPackage = (String) iterator2.next();
			String pattern = TclPackage.makeSearchRequest(TclPackage.PROVIDE,
					requiredPackage);
			org.eclipse.dltk.core.ISourceModule[] modules = TclMixinModel
					.getInstance().getMixin(scriptProject).findModules(pattern);
			for (int i = 0; i < modules.length; i++) {
				String name = modules[i].getElementName();
				if (name.equalsIgnoreCase("pkgindex.tcl")
						|| name.equalsIgnoreCase("tclindex")) {
					// allModules.add(modules[i]);
					// This is package we need to add all files from it.
					IParent parent = (IParent) modules[i].getParent();
					try {
						IModelElement[] children = parent.getChildren();
						for (int j = 0; j < children.length; j++) {
							IModelElement child = children[j];
							if (allModules.add(child)) {
								orderedModules.add(child);
							}
						}
					} catch (ModelException e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}

				} else {
					if (allModules.add(modules[i])) {
						orderedModules.add(modules[i]);
					}
				}
			}
		}
		return orderedModules;
	}

	/**
	 * Filter similar elements with "package require in current module"
	 * 
	 * @param elements
	 * @return
	 */
	public static IModelElement[] complexFilter(IModelElement[] elements,
			IScriptProject scriptProject,
			TclBuildPathPackageCollector packageCollector,
			boolean allVariantsOnFailed) {
		if (elements == null || elements.length == 0) {
			return new IModelElement[0];
		}
		Map similars = new HashMap();
		// Obtain duplicate named elements
		for (int i = 0; i < elements.length; i++) {
			IModelElement element = elements[i];
			String fullyQualifiedName = TclParseUtil.getFQNFromModelElement(
					element, "::");
			if (similars.containsKey(fullyQualifiedName)) {
				List similar = (List) similars.get(fullyQualifiedName);
				similar.add(element);
			} else {
				List similar = new ArrayList();
				similar.add(element);
				similars.put(fullyQualifiedName, similar);
			}
		}
		boolean processRequire = false;
		List result = new ArrayList();
		List allModules = null;
		// Filter similar elements
		for (Iterator iterator = similars.values().iterator(); iterator
				.hasNext();) {
			List similar = (List) iterator.next();

			if (similar.size() == 1) {
				result.add(similar.get(0));
			} else {
				if (processRequire == false) {
					processRequire = true;
					// We need to choose one element.
					List directives = packageCollector.getRequireDirectives();
					// We need a reordered list of packages
					List required = new ArrayList();
					for (Iterator iterator2 = directives.iterator(); iterator2
							.hasNext();) {
						TclPackageDeclaration decl = (TclPackageDeclaration) iterator2
								.next();
						required.add(decl.getName());
					}
					allModules = TclResolver.processReferenceModules(required,
							scriptProject);
				}
				if (allModules != null) {
					IModelElement found = null;
					int index = -1;
					for (Iterator iterator2 = similar.iterator(); iterator2
							.hasNext();) {
						IModelElement element = (IModelElement) iterator2
								.next();
						ISourceModule module = (ISourceModule) element
								.getAncestor(IModelElement.SOURCE_MODULE);
						int indexOf = allModules.indexOf(module);
						if (indexOf != -1) {
							// Found
							if (indexOf > index) {
								found = element;
								index = indexOf;
							}
						}
					}
					if (found == null) {
						if (allVariantsOnFailed) {
							result.addAll(similar);
						} else {
							result.add(similar.get(0));
						}
					} else {
						result.add(found);
					}
				}
			}
		}

		return (IModelElement[]) result
				.toArray(new IModelElement[result.size()]);
	}
}
