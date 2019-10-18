package ftd.exception;

/**
 * Thrown when parsing the json failed.
 */
@SuppressWarnings("serial")
public class ScratchParseException extends Exception {

	public ScratchParseException(Throwable cause) {
		super(cause);
	}

}
