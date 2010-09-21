package org.eclipse.dltk.tcl.internal.core.serialization;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.RuntimePerformanceMonitor;
import org.eclipse.dltk.core.RuntimePerformanceMonitor.PerformanceNode;
import org.eclipse.dltk.core.caching.AbstractDataLoader;
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

public class TclASTLoader extends AbstractDataLoader implements
		ITclASTConstants {
	private Scope[] scopes;
	int moduleSize = 0;

	public TclASTLoader(InputStream stream) throws IOException {
		super(stream);
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

	public TclModule getModule(IProblemReporter collector) throws Exception {
		PerformanceNode p = RuntimePerformanceMonitor.begin();
		// Load strings
		readStrings();

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
			p.done("Tcl", "Load persisted AST", 0);
			in.close();
			return module;
		}
		p.done("Tcl", "Load persisted AST", 0);
		in.close();
		return null;
	}

	private void loadProblem(IProblemReporter collector) throws IOException {
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
		collector.reportProblem(new DefaultProblem(null, message, id, args,
				sev, start, end, lineNumber));
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
			final String value = readString();
			argument.setValue(value);
			argument.setRawValue(value);
			return argument;
		}
		case TAG_COMPLEX_STRING_ARGUMENT: {
			ComplexString carg = AstFactory.eINSTANCE.createComplexString();
			carg.setKind(in.readInt());
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
			st.setKind(in.readInt());
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
		PerformanceNode p = RuntimePerformanceMonitor.begin();
		EObject def = null;
		boolean has = in.readBoolean();
		if (!has) {
			return null;
		}
		String definitionURI = readString();
		if (definitionURI != null) {
			for (Scope scope : scopes) {
				Resource eResource = scope.eResource();
				URI frag = eResource.getURI().appendFragment(definitionURI);
				EObject eObject = DefinitionManager.getInstance()
						.getEobjectCache().get(frag);
				if (eObject != null) {
					return eObject;
				}
			}
		}
		p.done("Tcl", "Restore eREF", 0);
		return def;
	}
}
