package com.github.intrigus.ftd.util;

import java.util.stream.Collectors;

public class StringUtil {
	/**
	 * Maps an arbitrary scratch name to a suitable C identifier. "fd_" is prefixed,
	 * because scratch also allows empty(!) identifiers. It is checked if a
	 * character in the String is a valid JavaIdentifierPart if so, it is used as
	 * is. Otherwise the codepoint's (int) value converted to a String is used. E.g.
	 * <li>"%" -> "37"</li>
	 * <li>" " -> "32"</li>
	 * 
	 * @param input the input to convert
	 * @return the String suitable as a C identifier
	 */
	public static String convertToCIdentifier(String input) {
		String result = "fd_";
		result += input.codePoints().mapToObj((it) -> toValidPoint(it)).collect(Collectors.joining());
		return result;
	}

	private static String toValidPoint(int input) {
		if (Character.isJavaIdentifierPart(input)) {
			return Character.toString(input);
		} else {
			return Integer.toString(input);
		}
	}
}
