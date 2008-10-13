package org.eclipse.dltk.tcl.parser.definitions;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.tcl.definitions.Scope;

public class DefinitionManager {
	private static DefinitionManager sInstance;
	private Map<String, Scope> scopes;

	private DefinitionManager() {
	}

	private void initialize() {
		if (scopes == null) {
			scopes = new HashMap<String, Scope>();
			Map<URL, String> extentions = DefinitionExtensionManager
					.getInstance().getExtentions();
			URL[] locations = DefinitionExtensionManager.getInstance()
					.getLocations();
			for (URL location : locations) {
				try {
					Scope scope = DefinitionLoader.loadDefinitions(location);
					scopes.put(extentions.get(location), scope);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Scope getScope(String name) {
		initialize();
		return scopes.get(name);
	}

	public Scope[] getScopes() {
		initialize();
		Collection<Scope> values = scopes.values();
		return values.toArray(new Scope[values.size()]);
	}

	public NamespaceScopeProcessor createProcessor(String name) {
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();
		Scope scope = getScope(name);
		if (scope != null)
			processor.addScope(scope);
		return processor;
	}

	public NamespaceScopeProcessor createProcessor() {
		return new NamespaceScopeProcessor(getCoreProcessor());
	}

	public NamespaceScopeProcessor createNewProcessor() {
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();
		Scope[] scopes = getScopes();
		for (Scope scope : scopes) {
			processor.addScope(scope);
		}
		return processor;
	}

	private NamespaceScopeProcessor coreProcessor = null;

	public NamespaceScopeProcessor getCoreProcessor() {
		if (coreProcessor == null) {
			coreProcessor = createNewProcessor();
		}
		return new NamespaceScopeProcessor(this.coreProcessor);
	}

	public static DefinitionManager getInstance() {
		if (sInstance == null) {
			sInstance = new DefinitionManager();
		}
		return sInstance;
	}
}
