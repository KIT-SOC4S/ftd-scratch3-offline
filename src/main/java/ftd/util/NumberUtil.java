package ftd.util;

public class NumberUtil {

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

	public static int asPosInt(Object object) {
		return Math.max(0, asInt(object));
	}

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

	public static float asPosFloat(Object object) {
		return Math.max(0f, asFloat(object));
	}
}
