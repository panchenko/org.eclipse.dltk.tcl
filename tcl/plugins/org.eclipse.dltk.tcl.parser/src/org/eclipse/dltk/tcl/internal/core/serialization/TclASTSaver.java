package org.eclipse.dltk.tcl.internal.core.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.core.DLTKContentTypeManager;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.internal.core.util.Util;
import org.eclipse.dltk.tcl.ast.AstFactory;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCodeModel;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclProblem;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class TclASTSaver implements ITclASTConstants {
	private TclModule module;
	private OutputStream stream;
	private DataOutputStream out;

	public TclASTSaver(TclModule module, OutputStream stream)
			throws IOException {
		this.module = module;
		this.stream = stream;
		this.out = new DataOutputStream(this.stream);
	}

	public void store(ProblemCollector collector) throws IOException {
		out.writeInt(TAG_MODULE);
		out.writeInt(module.getSize());
		EList<TclCommand> statements = module.getStatements();
		out.writeInt(statements.size());
		for (TclCommand tclCommand : statements) {
			out(tclCommand);
		}
		TclCodeModel codeModel = module.getCodeModel();
		if (codeModel != null) {
			out.writeBoolean(true);
			EList<String> delimeters = codeModel.getDelimeters();
			out.writeInt(delimeters.size());
			for (String del : delimeters) {
				writeString(del);
			}
			EList<Integer> lineOffsets = codeModel.getLineOffsets();
			out.writeInt(lineOffsets.size());
			for (Integer integer : lineOffsets) {
				out.writeInt(integer.intValue());
			}
		} else {
			out.writeBoolean(false);
		}
		if (collector != null) {
			int size = collector.getErrors().size();
			out.writeInt(size);
			collector.copyTo(new IProblemReporter() {

				public Object getAdapter(Class adapter) {
					return null;
				}

				public void reportProblem(IProblem problem) {
					try {
						saveProblem(problem);
					} catch (IOException e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}
				}

			});
		} else {
			out.writeInt(0);
		}
	}

	private void saveProblem(IProblem problem) throws IOException {
		out.writeInt(TAG_PROBLEM);
		out.writeInt(problem.getID());
		writeString(problem.getMessage());
		out.writeInt(problem.getSourceStart());
		out.writeInt(problem.getSourceEnd());
		if (problem.getArguments() != null) {
			out.writeInt(problem.getArguments().length);
			for (String arg : problem.getArguments()) {
				writeString(arg);
			}
		} else {
			out.writeInt(0);
		}
		out.writeBoolean(problem.isError());
		out.writeBoolean(problem.isWarning());
		out.writeInt(problem.getSourceLineNumber());
	}

	public void out(TclArgument arg) throws IOException {
		if (arg instanceof StringArgument) {
			// Simple absolute or relative source'ing.
			StringArgument argument = (StringArgument) arg;
			String value = argument.getValue();
			out.writeInt(TAG_STRING_ARGUMENT);
			out.writeInt(arg.getStart());
			out.writeInt(arg.getEnd());
			writeString(value);
		} else if (arg instanceof ComplexString) {
			ComplexString carg = (ComplexString) arg;
			String cargValue = carg.getValue();
			out.writeInt(TAG_COMPLEX_STRING_ARGUMENT);
			out.writeInt(arg.getStart());
			out.writeInt(arg.getEnd());
			writeString(cargValue);
			EList<TclArgument> eList = carg.getArguments();
			out.writeInt(eList.size());
			for (TclArgument tclArgument : eList) {
				out(tclArgument);
			}
		} else if (arg instanceof Script) {
			Script st = (Script) arg;
			EList<TclCommand> eList = st.getCommands();
			out.writeInt(TAG_SCRIPT_ARGUMENT);
			out.writeInt(arg.getStart());
			out.writeInt(arg.getEnd());
			out.writeInt(st.getContentStart());
			out.writeInt(st.getContentEnd());
			out.writeInt(eList.size());
			for (TclCommand tclArgument : eList) {
				out(tclArgument);
			}
		} else if (arg instanceof VariableReference) {
			VariableReference var = (VariableReference) arg;
			out.writeInt(TAG_VARIABLE_ARGUMENT);
			out.writeInt(arg.getStart());
			out.writeInt(arg.getEnd());
			writeString(var.getName());
			TclArgument index = var.getIndex();
			if (index == null) {
				out.writeBoolean(false);
			} else {
				out.writeBoolean(true);
				out(index);
			}
		} else if (arg instanceof Substitution) {
			Substitution st = (Substitution) arg;
			EList<TclCommand> eList = st.getCommands();
			out.writeInt(TAG_SUBSTITUTION_ARGUMENT);
			out.writeInt(arg.getStart());
			out.writeInt(arg.getEnd());
			out.writeInt(eList.size());
			for (TclCommand tclArgument : eList) {
				out(tclArgument);
			}
		} else if (arg instanceof TclArgumentList) {
			TclArgumentList st = (TclArgumentList) arg;
			out.writeInt(TAG_ARGUMENT_LIST_ARGUMENT);
			out.writeInt(arg.getStart());
			out.writeInt(arg.getEnd());
			EList<TclArgument> arguments = st.getArguments();
			out.writeInt(arguments.size());
			for (TclArgument tclArgument : arguments) {
				out(tclArgument);
			}
			TclArgument originalArg = st.getOriginalArgument();
			if (originalArg == null) {
				out.writeBoolean(false);
			} else {
				out.writeBoolean(true);
				out(originalArg);
			}
			storeERef(st.getDefinitionArgument());
		}
	}

	private void writeString(String value) throws IOException {
		if (value == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			Util.writeUTF(out, value.toCharArray());
		}
	}

	public void out(TclCommand command) throws IOException {
		out.writeInt(TAG_COMMAND);
		out.writeInt(command.getStart());
		out.writeInt(command.getEnd());
		writeString(command.getQualifiedName());
		Command def = command.getDefinition();
		storeERef(def);
		out(command.getName());
		out.writeInt(command.getArguments().size());
		EList<TclArgument> eList = command.getArguments();
		for (TclArgument tclArgument : eList) {
			out(tclArgument);
		}
	}

	private void storeERef(EObject def) throws IOException {
		if (def == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			URI uri = EcoreUtil.getURI(def);
			String defURI = uri.fragment();
			writeString(defURI);
		}
	}
}
