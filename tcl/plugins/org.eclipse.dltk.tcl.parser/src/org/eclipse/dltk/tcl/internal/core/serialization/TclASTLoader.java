package org.eclipse.dltk.tcl.internal.core.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.dltk.compiler.problem.DefaultProblemFactory;
import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class TclASTLoader implements ITclASTConstants {
	private InputStream stream;
	private DataInputStream in;
	private Scope[] scopes;

	public TclASTLoader(InputStream stream) throws IOException {
		this.stream = stream;
		this.in = new DataInputStream(this.stream);
		scopes = DefinitionManager.getInstance().getScopes();
	}

	public TclModule getModule(ProblemCollector collector) throws IOException {
		int moduleTag = in.readInt(); // TAG_MODULE
		switch (moduleTag) {
		case TAG_MODULE:
			TclModule module = AstFactory.eINSTANCE.createTclModule();
			module.setSize(in.readInt());// module.getSize()
			EList<TclCommand> statements = module.getStatements();
			int statemetsSize = in.readInt(); // statements.size()
			for (int i = 0; i < statemetsSize; ++i) {
				TclCommand command = readCommand();
				if (command == null) {
					return null;// Error in restoring
				}
				statements.add(command);
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
					offList.add(Integer.valueOf(in.readInt()));
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
		int tag = in.readInt();// TAG_PROBLEM);
		if (tag != TAG_PROBLEM) {
			return;
		}
		int id = in.readInt();
		String message = readString();
		int start = in.readInt();
		int end = in.readInt();
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
		int lineNumber = in.readInt();
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
		int argType = in.readInt();
		switch (argType) {
		case TAG_STRING_ARGUMENT: {
			// Simple absolute or relative source'ing.
			StringArgument argument = AstFactory.eINSTANCE
					.createStringArgument();
			argument.setStart(in.readInt());
			argument.setEnd(in.readInt());
			argument.setValue(readString());
			return argument;
		}
		case TAG_COMPLEX_STRING_ARGUMENT: {
			ComplexString carg = AstFactory.eINSTANCE.createComplexString();
			carg.setStart(in.readInt());
			carg.setEnd(in.readInt());
			carg.setValue(readString());
			EList<TclArgument> eList = carg.getArguments();
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				TclArgument arg = readArgument();
				if (arg == null) {
					return null;
				}
				eList.add(arg);
			}
			return carg;
		}
		case TAG_SCRIPT_ARGUMENT: {
			Script st = AstFactory.eINSTANCE.createScript();
			EList<TclCommand> eList = st.getCommands();
			st.setStart(in.readInt());
			st.setEnd(in.readInt());
			st.setContentStart(in.readInt());
			st.setContentEnd(in.readInt());
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				TclCommand arg = readCommand();
				if (arg == null) {
					return null;
				}
				eList.add(arg);
			}
			return st;
		}
		case TAG_VARIABLE_ARGUMENT: {
			VariableReference var = AstFactory.eINSTANCE
					.createVariableReference();
			var.setStart(in.readInt());
			var.setEnd(in.readInt());
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
			st.setStart(in.readInt());
			st.setEnd(in.readInt());
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				TclCommand arg = readCommand();
				if (arg == null) {
					return null;
				}
				eList.add(arg);
			}
			return st;
		}
		case TAG_ARGUMENT_LIST_ARGUMENT: {
			TclArgumentList st = AstFactory.eINSTANCE.createTclArgumentList();
			st.setStart(in.readInt());
			st.setEnd(in.readInt());
			EList<TclArgument> arguments = st.getArguments();
			int size = in.readInt();
			for (int i = 0; i < size; ++i) {
				TclArgument arg = readArgument();
				if (arg == null) {
					return null;
				}
				arguments.add(arg);
			}
			boolean originalArg = in.readBoolean();
			if (originalArg) {
				st.setOriginalArgument(readArgument());
			}
			EObject def = restoreERef();
			if (def instanceof ComplexArgument) {
				st.setDefinitionArgument((ComplexArgument) def);
			}
			return st;
		}
		}
		return null;
	}

	public TclCommand readCommand() throws IOException {
		int tagCommand = in.readInt();
		if (tagCommand != TAG_COMMAND) {
			return null;
		}
		TclCommand command = AstFactory.eINSTANCE.createTclCommand();
		command.setStart(in.readInt());
		command.setEnd(in.readInt());
		command.setQualifiedName(readString());
		EObject def = restoreERef();
		if (def instanceof Command) {
			command.setDefinition((Command) def);
		}
		command.setName(readArgument());
		int size = in.readInt();// command.getArguments().size());
		EList<TclArgument> eList = command.getArguments();
		for (int i = 0; i < size; ++i) {
			TclArgument argument = readArgument();
			if (argument == null) {
				return null;
			}
			eList.add(argument);
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

	private String readString() throws IOException {
		boolean readBoolean = in.readBoolean();
		if (readBoolean == false) {
			return null;
		}
		return new String(Util.readUTF(in));
	}
}
