/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.intrigus.ftd;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.intrigus.ftd.exception.ScratchParseException;

public class ConversionTest {

	@Test
	public void testNullConversion() {
		assertThrows(NullPointerException.class, () -> {
			Sb3ToArduinoC.convertToArduinoC(null);
		});
	}

	@Test
	public void testBiggerTestConversion() throws ScratchParseException, IOException {
		InputStream testFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("biggertest.sb3");
		Sb3ToArduinoC.convertToArduinoC(testFile);
	}

	@Test
	@Disabled
	public void testEinparkerConversion() throws ScratchParseException, IOException {
		InputStream testFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("Einparker_V1_0.sb3");
		Sb3ToArduinoC.convertToArduinoC(testFile);
	}

	@Test
	public void testMotorStopTestConversion() throws ScratchParseException, IOException {
		InputStream testFile = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("motor_stop_test.sb3");
		Sb3ToArduinoC.convertToArduinoC(testFile);
	}

	@Test
	public void testWhenInputTestConversion() throws ScratchParseException, IOException {
		InputStream testFile = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("when_input_test.sb3");
		Sb3ToArduinoC.convertToArduinoC(testFile);
	}

	@Test
	@Disabled
	public void testAllMilestone1BlocksConversion() throws ScratchParseException, IOException {
		InputStream testFile = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("all_milestone_1_blocks.sb3");
		Sb3ToArduinoC.convertToArduinoC(testFile);
	}
}