package ftd.exception;

import java.io.IOException;

/**
 * Thrown when a binary could not be found.
 */
@SuppressWarnings("serial")
public class BinaryNotFoundException extends IOException {

	public BinaryNotFoundException(String message) {
		super(message);
	}

}
