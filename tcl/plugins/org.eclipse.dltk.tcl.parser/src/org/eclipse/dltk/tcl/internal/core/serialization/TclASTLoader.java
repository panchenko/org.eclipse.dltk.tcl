package org.eclipse.dltk.tcl.internal.core.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.compiler.problem.DefaultProblemFactory;
import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
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
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.ComplexArgument;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class TclASTLoader implements ITclASTConstants {
	private InputStream stream;
	private DataInputStream in;
	private Scope[] scopes;
	private List<String> stringIndex = new ArrayList<String>();
	int moduleSize = 0;

	public TclASTLoader(InputStream stream) throws IOException {
		this.stream = stream;
		this.in = new DataInputStream(this.stream);
		scopes = DefinitionManager.getInstance().getScopes();
	}

	public int readInt() throws IOException {
		if (moduleSize < Byte.MAX_VALUE) {
			return in.readByte();
		} else if (moduleSize < Short.MAX_VALUE) {
			return in.readShort();
		} else {
			return in.readInt();
		}
	}

	public TclModule getModule(ProblemCollector collector) throws IOException {
		// Load strings
		int stringCount = in.readInt();
		for (int i = 0; i < stringCount; ++i) {
			stringIndex.add(new String(Util.readUTF(in)));
		}

		int moduleTag = in.readByte(); // TAG_MODULE
		switch (moduleTag) {
		case TAG_MODULE:
			TclModule module = AstFactory.eINSTANCE.createTclModule();
			moduleSize = in.readInt();
			module.setSize(moduleSize);// module.getSize()
			EList<TclCommand> statements = module.getStatements();
			int statemetsSize = in.readInt(); // statements.size()
			for (int i = 0; i < statemetsSize; ++i) {
				statements.add(readCommand());
			}
			boolean hasCodeModel = in.readBoolean();
			if (hasCodeModel) {
				TclCodeModel codeModel = AstFactory.eINSTANCE
						.createTclCodeModel();
				module.setCodeModel(codeModel);
				int delimSize = in.readInt();
				EList<String> delimList = codeModel.getDelimeters();
				for (int i = 0; i < delimSize; i++) {
					delimList.add(readString());
				}
				int offsets = in.readInt();
				EList<Integer> offList = codeModel.getLineOffsets();
				for (int i = 0; i < offsets; i++) {
					offList.add(Integer.valueOf(readInt()));
				}
			}
			// Restore problems
			int problemsSize = in.readInt();
			for (int i = 0; i < problemsSize; i++) {
				loadProblem(collector);
			}
			return module;
		}
		return null;
	}

	private void loadProblem(ProblemCollector collector) throws IOException {
		int tag = in.readByte();// TAG_PROBLEM);
		if (tag != TAG_PROBLEM) {
			return;
		}
		int id = in.readInt();
		String message = readString();
		int start = readInt();
		int end = readInt() + start;
		int argsSize = in.readInt();
		String[] args = null;
		if (argsSize > 0) {
			args = new String[argsSize];
		}
		for (int i = 0; i < argsSize; i++) {
			args[i] = readString();
		}
		boolean error = in.readBoolean();
		boolean warning = in.readBoolean();
		int lineNumber = readInt();
		int sev = 0;
		if (error) {
			sev = ProblemSeverities.Error;
		} else if (warning) {
			sev = ProblemSeverities.Warning;
		}
		DefaultProblemFactory fact = new DefaultProblemFactory();
		IProblem problem = fact.createProblem(null, id, args,
				new String[] { message }, sev, start, end, lineNumber, 0);
		collector.reportProblem(problem);
	}

	public TclArgument readArgument() throws IOException {
		int argType = in.readByte();
		switch (argType) {
		case TAG_STRING_ARGUMENT: {
			// Simple absolute or relative source'ing.
			StringArgument argument = AstFactory.eINSTANCE
					.createStringArgument();
			argument.setStart(readInt());
			argument.setEnd(readInt() + argument.getStart());
			argument.setValue(readString());
			return argument;
		}
		case TAG_COMPLEX_STRING_ARGUMENT: {
			ComplexString carg = AstFactory.eINSTANCE.createComplexString();
			carg.setStart(readInt());
			carg.setEnd(readInt() + carg.getStart());
			// carg.setValue(readString());
			EList<TclArgument> eList = carg.getArguments();
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				eList.add(readArgument());
			}
			return carg;
		}
		case TAG_SCRIPT_ARGUMENT: {
			Script st = AstFactory.eINSTANCE.createScript();
			EList<TclCommand> eList = st.getCommands();
			st.setStart(readInt());
			st.setEnd(readInt() + st.getStart());
			st.setContentStart(readInt());
			st.setContentEnd(readInt() + st.getContentStart());
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				eList.add(readCommand());
			}
			return st;
		}
		case TAG_VARIABLE_ARGUMENT: {
			VariableReference var = AstFactory.eINSTANCE
					.createVariableReference();
			var.setStart(readInt());
			var.setEnd(readInt() + var.getStart());
			var.setName(readString());
			boolean index = in.readBoolean();
			if (index) {
				var.setIndex(readArgument());
			}
			return var;
		}
		case TAG_SUBSTITUTION_ARGUMENT: {
			Substitution st = AstFactory.eINSTANCE.createSubstitution();
			EList<TclCommand> eList = st.getCommands();
			st.setStart(readInt());
			st.setEnd(readInt() + st.getStart());
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				eList.add(readCommand());
			}
			return st;
		}
		case TAG_ARGUMENT_LIST_ARGUMENT: {
			TclArgumentList st = AstFactory.eINSTANCE.createTclArgumentList();
			st.setStart(readInt());
			st.setEnd(readInt() + st.getStart());
			EList<TclArgument> arguments = st.getArguments();
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				arguments.add(readArgument());
			}
			// boolean originalArg = in.readBoolean();
			// if (originalArg) {
			// st.setOriginalArgument(readArgument());
			// }
			EObject def = restoreERef();
			if (def instanceof ComplexArgument) {
				st.setDefinitionArgument((ComplexArgument) def);
			}
			return st;
		}
		}
		throw new IOException("Failed to load command argument.");
	}

	public TclCommand readCommand() throws IOException {
		int tagCommand = in.readByte();
		if (tagCommand != TAG_COMMAND) {
			throw new IOException("Incorrect command tag");
		}
		TclCommand command = AstFactory.eINSTANCE.createTclCommand();
		command.setStart(readInt());
		command.setEnd(readInt() + command.getStart());
		command.setQualifiedName(readString());
		EObject def = restoreERef();
		if (def instanceof Command) {
			command.setDefinition((Command) def);
		}
		command.setName(readArgument());
		int size = in.readInt();// command.getArguments().size());
		EList<TclArgument> eList = command.getArguments();
		for (int i = 0; i < size; ++i) {
			eList.add(readArgument());
		}
		return command;
	}

	private EObject restoreERef() throws IOException {
		EObject def = null;
		boolean has = in.readBoolean();
		if (!has) {
			return null;
		}
		String definitionURI = readString();
		if (definitionURI != null) {
			for (Scope scope : scopes) {
				Resource eResource = scope.eResource();
				return eResource.getEObject(definitionURI);
			}
		}
		return def;
	}

	public int readNum(int id1, int id2) throws IOException {
		byte b = in.readByte();
		if (b == id1) {
			return in.readByte();
		} else if (b == id2) {
			return in.readInt();
		}
		return 0;
	}

	private String readString() throws IOException {
		byte b = in.readByte();
		if (b == 0) {
			return null;
		}
		if (b == 1) {
			int pos = in.readByte();
			return stringIndex.get(pos);
		} else if (b == 2) {
			int pos = in.readInt();
			return stringIndex.get(pos);
		} else if (b == 3) {
			int basePos = readNum(1, 2);
			int pos = readNum(1, 2);
			int len = readNum(1, 2);
			String base = stringIndex.get(basePos);
			String str = base.substring(pos, pos + len);
			return str;
		}
		return "";
	}
}
