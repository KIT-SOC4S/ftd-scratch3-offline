package com.github.intrigus.ftd.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtil {
	/**
	 * Converts a Throwable to a String. Uses
	 * {@link Throwable#printStackTrace(PrintWriter)} under the hood.
	 * 
	 * @param t the Throwable to convert to a String
	 * @return the Throwable converted to a String
	 */
	public static String throwableToString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
