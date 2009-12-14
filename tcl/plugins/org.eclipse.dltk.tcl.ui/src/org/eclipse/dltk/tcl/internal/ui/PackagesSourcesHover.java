package org.eclipse.dltk.tcl.internal.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.dltk.internal.ui.text.hover.AbstractScriptEditorTextHover;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.dltk.tcl.indexing.PackageSourceCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Shell;

public class PackagesSourcesHover extends AbstractScriptEditorTextHover {
	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		final IModelElement inputModelElement = EditorUtility
				.getEditorInputModelElement(this.getEditor(), false);
		if (inputModelElement == null) {
			return null;
		}
		final IDLTKLanguageToolkit toolkit = DLTKLanguageManager
				.getLanguageToolkit(inputModelElement);
		if (toolkit == null) {
			return null;
		}
		if (!TclNature.NATURE_ID.equals(toolkit.getNatureId())) {
			return null;
		}

		// This is correct tcl module, so lets locate package at specified
		// location.
		final ISourceModule sourceModule = (ISourceModule) inputModelElement
				.getAncestor(IModelElement.SOURCE_MODULE);
		final TclModuleInfo info = extractPackageSourceInfo(sourceModule);
		if (info == null) {
			return null;
		}
		final TclModuleInfo moduleInfo = loadModuleInfo(sourceModule);
		if (moduleInfo == null) {
			return null;
		}
		for (TclSourceEntry entry : info.getSourced()) {
			if (entry.getStart() <= hoverRegion.getOffset()
					&& hoverRegion.getOffset() <= entry.getEnd()) {
				final String hover = describe(entry, moduleInfo
						.getSourceCorrections(), "Source information:");
				if (hover != null) {
					return hover;
				}
			}
		}
		for (TclSourceEntry entry : info.getRequired()) {
			if (entry.getStart() <= hoverRegion.getOffset()
					&& hoverRegion.getOffset() <= entry.getEnd()) {
				final String hover = describe(entry, moduleInfo
						.getPackageCorrections(), "Require information:");
				if (hover != null) {
					return null;
				}
			}
		}
		return null;
	}

	private String describe(TclSourceEntry entry,
			EList<UserCorrection> corrections, String caption) {
		StringBuilder buffer = new StringBuilder(256);
		buffer.append(caption);
		boolean added = false;
		for (UserCorrection userCorrection : corrections) {
			if (userCorrection.getOriginalValue().equals(entry.getValue())) {
				List<String> userValue = new ArrayList<String>(userCorrection
						.getUserValue());
				Collections.sort(userValue);
				buffer.append("<ul>"); //$NON-NLS-1$
				for (String value : userValue) {
					buffer.append("<li>").append(value).append("</li>"); //$NON-NLS-1$ //$NON-NLS-2$
					added = true;
				}
				buffer.append("</ul>"); //$NON-NLS-1$
			}
		}
		if (added) {
			return buffer.toString();
		}
		return null;
	}

	private TclModuleInfo extractPackageSourceInfo(ISourceModule sourceModule) {
		final TclModule module = parse(sourceModule);
		if (module != null) {
			final PackageSourceCollector collector = new PackageSourceCollector();
			collector.process(module.getStatements(), sourceModule);
			return collector.getCurrentModuleInfo();
		}
		return null;
	}

	private TclModule parse(final ISourceModule sourceModule) {
		final ModuleDeclaration declaration = SourceParserUtil
				.getModuleDeclaration(sourceModule);
		if (declaration instanceof TclModuleDeclaration) {
			return ((TclModuleDeclaration) declaration).getTclModule();
		} else {
			TclParser parser = new TclParser();
			try {
				return parser.parseModule(sourceModule.getSource(), null,
						DefinitionManager.getInstance().createProcessor());
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
				return null;
			}
		}
	}

	private TclModuleInfo loadModuleInfo(ISourceModule sourceModule) {
		final List<TclModuleInfo> projectModules = TclPackagesManager
				.getProjectModules(sourceModule.getScriptProject()
						.getElementName());
		final String handle = sourceModule.getHandleIdentifier();
		for (TclModuleInfo tclModuleInfo : projectModules) {
			if (tclModuleInfo.getHandle().equals(handle)) {
				return tclModuleInfo;
			}
		}
		return null;
	}

	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return new AbstractReusableInformationControlCreator() {
			@Override
			protected IInformationControl doCreateInformationControl(
					Shell parent) {
				DefaultInformationControl ctrl = new DefaultInformationControl(
						parent, false);
				return ctrl;
			}
		};
	}
}
