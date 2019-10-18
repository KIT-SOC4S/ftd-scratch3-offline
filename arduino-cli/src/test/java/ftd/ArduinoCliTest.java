package ftd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import ftd.exception.CompilationFailedException;

public class ArduinoCliTest {

	@Test
	public void testFailingCompilation() {
		InputStream failingFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("failing.c");
		try {
			ArduinoCLI.compileArduinoC(failingFile);
			fail("Compilation should have failed!");
		} catch (CompilationFailedException e) {
			// The compilation could also fail because arduino-cli itself failed.
			// Make sure that this is not the case
			// This can happen in some cases, when arduino-cli doesn't find some required
			// tools and than arduino-cli (written in Go) panics.
			assertFalse(e.getCompilationLog().contains("panic"));
		}
	}

	@Test
	public void testWorkingCompilation() throws FileNotFoundException {
		InputStream workingFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("working.c");
		try {
			ArduinoCLI.compileArduinoC(workingFile);
		} catch (CompilationFailedException e) {
			System.err.println(e.getCompilationLog());
			e.printStackTrace(System.err);
			fail("Compilation should not have failed!");
		}
	}
}
