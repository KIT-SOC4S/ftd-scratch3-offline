package ftd.exception;

/**
 * Thrown when the compilation of a program failed, i.e. because the program did
 * not exit cleanly, temporary files for compilation could not be created or
 * because the program got interrupted.
 *
 */
@SuppressWarnings("serial")
public class CompilationFailedException extends Exception {

	private final String compilationOutput;

	public CompilationFailedException(String message, Throwable cause) {
		super(message, cause);
		compilationOutput = "";
	}

	public CompilationFailedException(Throwable cause) {
		super(cause);
		compilationOutput = "";
	}

	public CompilationFailedException(String message, Throwable cause, String compilationOutput) {
		super(message, cause);
		this.compilationOutput = compilationOutput;
	}

	/**
	 * Returns the log of the compilation if there is one.
	 * 
	 * @return the log of the compilation
	 */
	public String getCompilationLog() {
		return compilationOutput;
	}
}
