package org.eclipse.dltk.tcl.internal.parser;

import org.eclipse.dltk.ast.parser.ISourceParserFactory;
import org.eclipse.dltk.tcl.core.ITclSourceParser;

/**
 * Returns instances of the Tcl source parser
 */
public class TclSourceParserFactory implements ISourceParserFactory {

	/*
	 * @see
	 * org.eclipse.dltk.ast.parser.ISourceParserFactory#createSourceParser()
	 */
	public ITclSourceParser createSourceParser() {
		return new NewTclSourceParser();
	}

}
