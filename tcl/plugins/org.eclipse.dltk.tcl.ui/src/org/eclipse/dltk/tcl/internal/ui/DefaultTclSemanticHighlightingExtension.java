package org.eclipse.dltk.tcl.internal.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.ast.ASTListNode;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.compiler.env.ISourceModule;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.internal.ui.text.TclAutoEditStrategy;
import org.eclipse.dltk.tcl.internal.ui.text.TclTextTools;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.semantilhighlighting.ISemanticHighlightingExtension;
import org.eclipse.dltk.ui.editor.highlighting.HighlightedPosition;
import org.eclipse.dltk.ui.editor.highlighting.ISemanticHighlightingRequestor;
import org.eclipse.dltk.ui.editor.highlighting.SemanticHighlighting;
import org.eclipse.dltk.ui.preferences.PreferencesMessages;
import org.eclipse.emf.common.util.EList;

public class DefaultTclSemanticHighlightingExtension implements
		ISemanticHighlightingExtension {

	private SemanticHighlighting[] highlightings = new SemanticHighlighting[] {
			new TclTextTools.SH(
					TclPreferenceConstants.EDITOR_PROCEDURES_COLOR,
					null,
					PreferencesMessages.DLTKEditorPreferencePage_function_colors),
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_ARGUMENTS_COLOR,
					null,
					PreferencesMessages.DLTKEditorPreferencePage_arguments),
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_CLASSES_COLOR,
					null,
					PreferencesMessages.DLTKEditorPreferencePage_class_colors),
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_BASE_CLASS_COLOR,
					null,
					PreferencesMessages.DLTKEditorPreferencePage_base_classes),
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_STRING_COLOR,
					null, PreferencesMessages.DLTKEditorPreferencePage_strings),
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_VARIABLE_COLOR,
					null,
					PreferencesMessages.DLTKEditorPreferencePage_variables) };

	private static final int HL_PROCEDURES = 0;
	private static final int HL_ARGUMENTS = 1;
	private static final int HL_CLASSES = 2;
	private static final int HL_BASE_CLASSES = 3;
	private static final int HL_STRINGS = 4;
	private static final int HL_VARIABLES = 5;

	public DefaultTclSemanticHighlightingExtension() {
	}

	public HighlightedPosition[] calculatePositions(ASTNode node,
			ISemanticHighlightingRequestor requestor) {

		// Check Tcl procedures
		if (node instanceof MethodDeclaration) {

			MethodDeclaration m = (MethodDeclaration) node;
			requestor.addPosition(m.getNameStart(), m.getNameEnd(),
					HL_PROCEDURES);

		}

		// Check procedure arguments
		if (node instanceof Argument) {
			Argument m = (Argument) node;
			requestor.addPosition(m.getNameStart(), m.getNameEnd(),
					HL_ARGUMENTS);

		}

		// Check IncrTcl and XOTcl classes
		if (node instanceof TypeDeclaration) {

			TypeDeclaration t = (TypeDeclaration) node;
			List children;

			// Handle base classes highlighting
			ASTListNode s = t.getSuperClasses();

			if (s != null && s.getChilds() != null) {
				children = s.getChilds();
				Iterator it = children.iterator();
				while (it.hasNext()) {
					ASTNode n = (ASTNode) it.next();

					requestor.addPosition(n.sourceStart(), n.sourceEnd(),
							HL_BASE_CLASSES);

				}
			}

			requestor.addPosition(t.getNameStart(), t.getNameEnd(), HL_CLASSES);
		}

		return null;

	}

	public void processNode(ASTNode node,
			ISemanticHighlightingRequestor requestor) {
		calculatePositions(node, requestor);

	}

	public SemanticHighlighting[] getHighlightings() {
		return highlightings;
	}

	/**
	 * Perform strings highlighting.
	 * 
	 * @since 2.0
	 */
	public void doOtherHighlighting(ISourceModule code,
			final ISemanticHighlightingRequestor semanticHighlightingRequestor) {
		ModuleDeclaration moduleDeclaration = SourceParserUtil
				.getModuleDeclaration((org.eclipse.dltk.core.ISourceModule) (code
						.getModelElement()));
		if (moduleDeclaration instanceof TclModuleDeclaration) {
			TclModuleDeclaration tclModule = (TclModuleDeclaration) moduleDeclaration;
			TclModule module = tclModule.getTclModule();
			if (module == null) {
				TclParser parser = new TclParser();
				module = parser.parseModule(code.getSourceContents(), null,
						DefinitionManager.getInstance().createNewProcessor());
			}
			if (module != null) {
				TclParserUtils.traverse(module.getStatements(),
						new TclVisitor() {
							@Override
							public boolean visit(StringArgument arg) {
								String value = arg.getValue();
								if (value.startsWith("\"")
										&& value.endsWith("\"")) {
									// addPosition(arg.getStart(), arg.getEnd(),
									// highlightingIndex)
									semanticHighlightingRequestor.addPosition(
											arg.getStart(), arg.getEnd(),
											HL_STRINGS);
								} else {
									// Look for sub strings
									int pos = value.indexOf('\"');
									while (pos != -1) {
										int pos2 = value.indexOf('\"', pos + 1);
										if (pos2 == -1) {
											break;
										}
										// Add region from pos to pos2
										semanticHighlightingRequestor
												.addPosition(arg.getStart()
														+ pos, arg.getStart()
														+ pos2 + 1, HL_STRINGS);
										pos = value.indexOf('\"', pos2 + 1);
									}
								}
								return super.visit(arg);
							}

							@Override
							public boolean visit(TclCommand command) {
								TclArgument name = command.getName();
								if (name instanceof StringArgument) {
									String value = ((StringArgument) name)
											.getValue();
									if (value.startsWith("\"")
											&& value.endsWith("\"")) {
										// addPosition(arg.getStart(),
										// arg.getEnd(),
										// highlightingIndex)
										semanticHighlightingRequestor
												.addPosition(name.getStart(),
														name.getEnd(),
														HL_STRINGS);
									}
								}
								return super.visit(command);
							}

							@Override
							public boolean visit(VariableReference list) {
								semanticHighlightingRequestor.addPosition(list
										.getStart(), list.getEnd(),
										HL_VARIABLES);
								return super.visit(list);
							}

							@Override
							public boolean visit(ComplexString list) {
								if (list.getKind() == 2) {
									EList<TclArgument> arguments = list
											.getArguments();
									//
									boolean addFirst = false;
									boolean addLast = false;
									for (int i = 0; i < arguments.size(); i++) {
										TclArgument tclArgument = arguments
												.get(i);
										if (tclArgument instanceof StringArgument) {
											semanticHighlightingRequestor
													.addPosition(tclArgument
															.getStart(),
															tclArgument
																	.getEnd(),
															HL_STRINGS);
										}
										if (tclArgument instanceof VariableReference) {
											semanticHighlightingRequestor
													.addPosition(tclArgument
															.getStart(),
															tclArgument
																	.getEnd(),
															HL_VARIABLES);
											if (i == 0) {
												addFirst = true;
											}
											if (i == arguments.size() - 1) {
												addLast = true;
											}
										}
										if (tclArgument instanceof ComplexString) {
											visit((ComplexString) tclArgument);
										}
									}
									if (addFirst) {
										semanticHighlightingRequestor
												.addPosition(list.getStart(),
														list.getStart() + 1,
														HL_STRINGS);
									}
									if (addLast) {
										semanticHighlightingRequestor
												.addPosition(list.getEnd() - 1,
														list.getEnd(),
														HL_STRINGS);
									}
									return false;
								}
								return super.visit(list);
							}
						});
			}
		}
	}
}
