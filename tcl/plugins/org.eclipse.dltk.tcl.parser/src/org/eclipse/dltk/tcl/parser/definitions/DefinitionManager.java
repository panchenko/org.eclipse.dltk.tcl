package org.eclipse.dltk.tcl.parser.definitions;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.Namespace;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DefinitionManager {
	private static DefinitionManager sInstance;
	private Map<String, Scope> scopes;
	private Map<URI, EObject> eobjectCache = new HashMap<URI, EObject>();

	private DefinitionManager() {
	}

	public Map<URI, EObject> getEobjectCache() {
		return eobjectCache;
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
					fillEObjects(scope);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void fillEObjects(Scope scope) {
		EList<Scope> children = scope.getChildren();
		for (Scope child : children) {
			fillEObjects(child);
		}
		if (scope instanceof Command) {
			Command command = (Command) scope;
			eobjectCache.put(EcoreUtil.getURI(command), command);
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
		return new NamespaceScopeProcessor(coreProcessor);
	}

	public static DefinitionManager getInstance() {
		if (sInstance == null) {
			sInstance = new DefinitionManager();
		}
		return sInstance;
	}
}
