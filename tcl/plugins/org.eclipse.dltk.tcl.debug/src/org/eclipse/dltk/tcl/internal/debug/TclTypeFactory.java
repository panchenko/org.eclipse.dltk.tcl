package org.eclipse.dltk.tcl.internal.debug;

import org.eclipse.dltk.debug.core.model.ComplexScriptType;
import org.eclipse.dltk.debug.core.model.IScriptType;
import org.eclipse.dltk.debug.core.model.IScriptTypeFactory;
import org.eclipse.dltk.debug.core.model.StringScriptType;

public class TclTypeFactory implements IScriptTypeFactory {

	public IScriptType buildType(String type) {
		if (STRING.equals(type)) {
			return new StringScriptType(type);
		}
		return new ComplexScriptType(type);
	}

}
