package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver.IVariableRegistry;

/**
 * @since 2.0
 */
public class DefaultVariablesRegistry implements IVariableRegistry {
	private Map<String, VariableValue> variables;
	private Map<String, String> environmentVariablesMap;
	private EnvironmentVariable[] interpreterVariables;

	public DefaultVariablesRegistry(IScriptProject project) {
		variables = new HashMap<String, VariableValue>();
		IInterpreterInstall install = null;
		try {
			install = ScriptRuntime.getInterpreterInstall(project);
		} catch (CoreException e) {
			DLTKCore.error("Failed to get interpreter install", e);
		}
		if (install != null) {
			variables
					.putAll(TclPackagesManager.getVariablesEMap(install).map());
			IExecutionEnvironment execEnvironment = install
					.getExecEnvironment();
			if (execEnvironment != null) {
				environmentVariablesMap = execEnvironment
						.getEnvironmentVariables(true);
			}
			interpreterVariables = install.getEnvironmentVariables();
		}
		variables.putAll(TclPackagesManager.getVariablesEMap(
				project.getElementName()).map());
	}

	public String getValue(String name, String index) {
		if (name.equals("env") && index != null) {
			if (interpreterVariables != null) {
				for (EnvironmentVariable variable : interpreterVariables) {
					if (variable.getName().equals(index)) {
						return variable.getValue();
					}
				}
			}
			if (environmentVariablesMap != null) {
				return environmentVariablesMap.get(index);
			}
		}
		final VariableValue value = variables.get(name);
		if (value != null) {
			return value.getValue();
		} else {
			return null;
		}
	}
}
