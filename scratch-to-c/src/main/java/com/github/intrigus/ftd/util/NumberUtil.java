package com.github.intrigus.ftd.util;

/**
 * Convert arbitrary objects to numbers. Using a number value, if it's a String
 * and can be converted to a number, 0 otherwise.
 *
 */
public class NumberUtil {

	/**
	 * Converts an object to an integer. If the object is a String its value
	 * converted to an integer is used. If the object is not a String 0 is returned.
	 * 
	 * @param object the object to convert to an integer.
	 * @return the object converted to an integer.
	 */
	public static int asInt(Object object) {
		if (!(object instanceof String)) {
			return 0;
		} else {
			return asInt((String) object);
		}
	}

	private static int asInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Converts an object to a positive integer. If the object is a String its value
	 * converted to a positive integer is used. If the object is not a String 0 is
	 * returned.
	 * 
	 * @param object the object to convert to a positive integer.
	 * @return the object converted to a positive integer, [0, Integer.MAX_VALUE].
	 */
	public static int asPosInt(Object object) {
		return Math.max(0, asInt(object));
	}

	/**
	 * Converts an object to a float. If the object is a String its value converted
	 * to a float is used. If the object is not a String 0f is returned.
	 * 
	 * @param object the object to convert to a float.
	 * @return the object converted to a float.
	 */
	public static float asFloat(Object object) {
		if (!(object instanceof String)) {
			return 0f;
		} else {
			return asFloat((String) object);
		}
	}

	private static float asFloat(String string) {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Converts an object to a positive float. If the object is a String its value
	 * converted to a positive float is used. If the object is not a String 0f is
	 * returned.
	 * 
	 * @param object the object to convert to a positive float.
	 * @return the object converted to a positive float, [0, Float.MAX_VALUE].
	 */
	public static float asPosFloat(Object object) {
		return Math.max(0f, asFloat(object));
	}
}
