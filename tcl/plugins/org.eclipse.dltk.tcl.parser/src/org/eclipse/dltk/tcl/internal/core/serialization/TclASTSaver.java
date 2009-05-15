package org.eclipse.dltk.tcl.internal.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.internal.core.util.Util;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCodeModel;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
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
	public List<String> stringIndex = new ArrayList<String>();

	public TclASTSaver(TclModule module, OutputStream stream)
			throws IOException {
		this.module = module;
		this.stream = stream;
	}

	public void writeInt(int value) throws IOException {
		if (module.getSize() < Byte.MAX_VALUE) {
			out.writeByte(value);
		} else if (module.getSize() < Short.MAX_VALUE) {
			out.writeShort(value);
		} else {
			out.writeInt(value);
		}
	}

	public void store(ProblemCollector collector) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		this.out = new DataOutputStream(bout);
		out.writeByte(TAG_MODULE);
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
				writeInt(integer.intValue());
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
		// Store all string index
		this.out.flush();
		this.out = new DataOutputStream(this.stream);

		// Store strings
		out.writeInt(stringIndex.size());
		for (String s : this.stringIndex) {
			Util.writeUTF(out, s.toCharArray());
		}
		org.eclipse.dltk.compiler.util.Util.copy(new ByteArrayInputStream(bout
				.toByteArray()), this.out);

	}

	private void saveProblem(IProblem problem) throws IOException {
		out.writeByte(TAG_PROBLEM);
		out.writeInt(problem.getID());
		writeString(problem.getMessage());
		writeInt(problem.getSourceStart());
		writeInt(problem.getSourceEnd() - problem.getSourceStart());
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
		writeInt(problem.getSourceLineNumber());
	}

	public void out(TclArgument arg) throws IOException {
		if (arg instanceof StringArgument) {
			// Simple absolute or relative source'ing.
			StringArgument argument = (StringArgument) arg;
			String value = argument.getValue();
			out.writeByte(TAG_STRING_ARGUMENT);
			writeInt(arg.getStart());
			writeInt(arg.getEnd() - arg.getStart());
			writeString(value);
		} else if (arg instanceof ComplexString) {
			ComplexString carg = (ComplexString) arg;
			// String cargValue = carg.getValue();
			out.writeByte(TAG_COMPLEX_STRING_ARGUMENT);
			writeInt(arg.getStart());
			writeInt(arg.getEnd() - arg.getStart());
			// writeString(cargValue);
			EList<TclArgument> eList = carg.getArguments();
			out.writeInt(eList.size());
			for (TclArgument tclArgument : eList) {
				out(tclArgument);
			}
		} else if (arg instanceof Script) {
			Script st = (Script) arg;
			EList<TclCommand> eList = st.getCommands();
			out.writeByte(TAG_SCRIPT_ARGUMENT);
			writeInt(arg.getStart());
			writeInt(arg.getEnd() - arg.getStart());
			writeInt(st.getContentStart());
			writeInt(st.getContentEnd() - st.getContentStart());
			out.writeInt(eList.size());
			for (TclCommand tclArgument : eList) {
				out(tclArgument);
			}
		} else if (arg instanceof VariableReference) {
			VariableReference var = (VariableReference) arg;
			out.writeByte(TAG_VARIABLE_ARGUMENT);
			writeInt(arg.getStart());
			writeInt(arg.getEnd() - arg.getStart());
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
			out.writeByte(TAG_SUBSTITUTION_ARGUMENT);
			writeInt(arg.getStart());
			writeInt(arg.getEnd() - arg.getStart());
			out.writeInt(eList.size());
			for (TclCommand tclArgument : eList) {
				out(tclArgument);
			}
		} else if (arg instanceof TclArgumentList) {
			TclArgumentList st = (TclArgumentList) arg;
			out.writeByte(TAG_ARGUMENT_LIST_ARGUMENT);
			writeInt(arg.getStart());
			writeInt(arg.getEnd() - arg.getStart());
			EList<TclArgument> arguments = st.getArguments();
			out.writeInt(arguments.size());
			for (TclArgument tclArgument : arguments) {
				out(tclArgument);
			}
			storeERef(st.getDefinitionArgument());
		}
	}

	private void writeString(String value) throws IOException {
		if (value == null) {
			out.writeByte(0);
			return;
		}
		int indexOf = stringIndex.indexOf(value);
		if (indexOf != -1) {
			outNum(indexOf, 1, 2);
			return;
		} else {
			// Try to find part of word
			if (value.length() > 6) {
				for (String base : stringIndex) {
					if (base.contains(value)) {
						// Part of string
						int pos = base.indexOf(value);
						out.writeByte(3);
						int basePos = stringIndex.indexOf(base);
						outNum(basePos, 1, 2);
						outNum(pos, 1, 2);
						outNum(value.length(), 1, 2);
						return;
					}
				}
			}
			stringIndex.add(value);
			outNum(stringIndex.size() - 1, 1, 2);
			return;
		}
	}

	private void outNum(int indexOf, int id1, int id2) throws IOException {
		if (indexOf <= Byte.MAX_VALUE) {
			out.writeByte(id1);
			out.writeByte(indexOf);
		} else if (indexOf > Byte.MAX_VALUE) {
			out.writeByte(id2);
			out.writeInt(indexOf);
		}
	}

	public void out(TclCommand command) throws IOException {
		out.writeByte(TAG_COMMAND);
		writeInt(command.getStart());
		writeInt(command.getEnd());
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
