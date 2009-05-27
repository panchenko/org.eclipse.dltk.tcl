package org.eclipse.dltk.tcl.internal.ui;

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
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;

public class PackagesSourcesHover extends AbstractScriptEditorTextHover {
	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		String nature = null;
		IModelElement inputModelElement = EditorUtility
				.getEditorInputModelElement(this.getEditor(), false);
		if (inputModelElement == null)
			return null;
		IDLTKLanguageToolkit toolkit = DLTKLanguageManager
				.getLanguageToolkit(inputModelElement);
		if (toolkit == null) {
			return null;
		}
		nature = toolkit.getNatureId();
		if (nature == null || !TclNature.NATURE_ID.equals(nature)) {
			return null;
		}

		// This is correct tcl module, so lets locate package at specified
		// location.
		ISourceModule sourceModule = (ISourceModule) inputModelElement
				.getAncestor(IModelElement.SOURCE_MODULE);
		ModuleDeclaration declaration = SourceParserUtil
				.getModuleDeclaration(sourceModule);
		TclModule module = null;
		if (declaration instanceof TclModuleDeclaration) {
			module = ((TclModuleDeclaration) declaration).getTclModule();
		} else {
			TclParser parser = new TclParser();
			try {
				module = parser.parseModule(sourceModule.getSource(), null,
						DefinitionManager.getInstance().getCoreProcessor());
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		if (module != null) {
			PackageSourceCollector collector = new PackageSourceCollector();
			collector.process(module.getStatements(), sourceModule);
			TclModuleInfo info = collector.getCurrentModuleInfo();
			EList<TclSourceEntry> sourced = info.getSourced();
			EList<TclSourceEntry> required = info.getRequired();
			List<TclModuleInfo> projectModules = TclPackagesManager
					.getProjectModules(sourceModule.getScriptProject()
							.getElementName());
			String handle = sourceModule.getHandleIdentifier();
			EList<UserCorrection> sourceCorrections = null;
			EList<UserCorrection> packageCorrections = null;
			for (TclModuleInfo tclModuleInfo : projectModules) {
				if (tclModuleInfo.getHandle().equals(handle)) {
					sourceCorrections = tclModuleInfo.getSourceCorrections();
					packageCorrections = tclModuleInfo.getPackageCorrections();
					break;
				}
			}
			if (sourceCorrections != null) {
				for (TclSourceEntry tclSourceEntry : sourced) {
					if (tclSourceEntry.getStart() <= hoverRegion.getOffset()
							&& hoverRegion.getOffset() <= tclSourceEntry
									.getEnd()) {
						return "Sourced:";
					}
				}
			}
			if (packageCorrections != null) {
				for (TclSourceEntry tclSourceEntry : sourced) {
					if (tclSourceEntry.getStart() <= hoverRegion.getOffset()
							&& hoverRegion.getOffset() <= tclSourceEntry
									.getEnd()) {
						return "Required:";
					}
				}
			}
		}
		return "";
	}
}
