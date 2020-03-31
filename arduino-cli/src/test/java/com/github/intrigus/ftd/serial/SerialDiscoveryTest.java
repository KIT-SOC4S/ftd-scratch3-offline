package com.github.intrigus.ftd.serial;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.github.intrigus.ftd.serial.SerialDisoveryMessage.ErrorMessage;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.QuitMessage;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SerialDiscovery.class })
public class SerialDiscoveryTest {

	private SerialDiscovery serialDiscovery;

	@BeforeEach
	private void setUp() throws Exception {
		serialDiscovery = Whitebox.invokeConstructor(SerialDiscovery.class, (Object[]) null);
	}

	@Test
	public void testFailingCommand() throws Exception {
		SerialDisoveryMessage result = Whitebox.invokeMethod(serialDiscovery, "issueCommand", "THISWILLFAIL");
		assertTrue(result instanceof ErrorMessage);
	}

	@Test
	public void testWorkingCommand() throws Exception {
		SerialDisoveryMessage result = Whitebox.invokeMethod(serialDiscovery, "issueCommand", "QUIT");
		assertTrue(result instanceof QuitMessage);
		String message = Whitebox.getInternalState(result, "message", QuitMessage.class);
		assertTrue(message.equals("OK"));
	}
}
