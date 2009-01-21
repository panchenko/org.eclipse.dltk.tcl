package org.eclipse.dltk.tcl.internal.debug;

import org.eclipse.dltk.debug.core.model.ComplexScriptType;
import org.eclipse.dltk.debug.core.model.IScriptType;
import org.eclipse.dltk.debug.core.model.IScriptTypeFactory;
import org.eclipse.dltk.debug.core.model.IScriptValue;
import org.eclipse.dltk.debug.core.model.StringScriptType;

public class TclTypeFactory implements IScriptTypeFactory {

	private static class TclStringScriptType extends StringScriptType {

		public TclStringScriptType() {
			super(STRING);
		}

		public String formatValue(IScriptValue value) {
			String string = value.getRawValue();
			if (string == null) {
				return null;
			}
			return escapeString(string, false);
		}

		public String formatDetails(IScriptValue value) {
			String string = value.getRawValue();
			if (string == null) {
				return null;
			}
			return escapeString(string, true);
		}

		private static String escapeString(String input, boolean allowWhitespace) {
			if (!isEscapeNeeded(input)) {
				return input;
			}
			// if (!allowWhitespace && canBraceEscape(input)) {
			//				return "{" + input + "}"; //$NON-NLS-1$ //$NON-NLS-2$
			// }
			final StringBuffer result = new StringBuffer(input.length() + 8);
			final int length = input.length();
			for (int i = 0; i < length; i++) {
				char c = input.charAt(i);
				if (isEscapeNeeded(c)) {
					if (allowWhitespace && (c == ' ' || c == '\n' || c == '\t')) {
						result.append(c);
					} else {
						result.append('\\');
						if (c < 32) {
							switch (c) {
							case 7:
								result.append('a');
								break;
							case 8:
								result.append('b');
								break;
							case 9:
								result.append('t');
								break;
							case 0xB:
								result.append('v');
								break;
							case 0xC:
								result.append('f');
								break;
							case 0xA:
								result.append('n');
								break;
							case 0xD:
								result.append('r');
								break;
							default:
								result.append(toHexChar(c / 16));
								result.append(toHexChar(c % 16));
								break;
							}
						} else {
							result.append(c);
						}
					}
				} else {
					result.append(c);
				}
			}
			return result.toString();
		}

		/**
		 * @param value
		 * @return
		 */
		private static boolean canBraceEscape(String value) {
			final int length = value.length();
			for (int i = 0; i < length; i++) {
				final char c = value.charAt(i);
				if (c < 32 || c == '{' || c == '}') {
					return false;
				}
			}
			return true;
		}

		private static char toHexChar(int value) {
			if (value <= 9) {
				return (char) ('0' + value);
			} else {
				return (char) ('A' + (value - 10));
			}
		}

		private static boolean isEscapeNeeded(String value) {
			final int length = value.length();
			for (int i = 0; i < length; i++) {
				if (isEscapeNeeded(value.charAt(i))) {
					return true;
				}
			}
			return false;
		}

		private static boolean isEscapeNeeded(char c) {
			if (c < 32) {
				return true;
			}
			switch (c) {
			case ' ':
			case '{':
			case '}':
			case '\"':
			case '[':
			case '\\':
			case '$':
				return true;
			}
			return false;
		}

	}

	private static class TclNamespaceType extends ComplexScriptType {

		public TclNamespaceType() {
			super(NAMESPACE);
		}

		public String formatValue(IScriptValue value) {
			return NAMESPACE;
		}

	}

	private final TclStringScriptType stringType = new TclStringScriptType();
	private final TclNamespaceType namespaceType = new TclNamespaceType();

	private static final String NAMESPACE = "namespace"; //$NON-NLS-1$

	public IScriptType buildType(String type) {
		if (STRING.equals(type)) {
			return stringType;
		} else if (NAMESPACE.equals(type)) {
			return namespaceType;
		} else {
			return new ComplexScriptType(type);
		}
	}

}
