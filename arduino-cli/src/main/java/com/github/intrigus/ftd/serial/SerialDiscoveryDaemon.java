package com.github.intrigus.ftd.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.intrigus.ftd.exception.BinaryNotFoundException;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.AddMessage;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.RemoveMessage;

/**
 * Gives access to the "serial-discovery" program that is included with
 * arduino-cli. "serial-discovery" (short: sd) supports a polling and an event
 * based mode. This class provides a poll based mode. I.e. you still have to
 * actively check for devices (as a user of this API), but we won't have to
 * create a new process every time, but can instead use only one continuously.
 * Also see {@link SerialDiscovery}.
 *
 */
public class SerialDiscoveryDaemon {
	private Map<String, SerialDevice> devices;
	Process serialDiscoveryProcess;

	private SerialDiscoveryDaemon() {
		this.devices = new HashMap<>();
		Thread thread = new Thread(() -> {
			startDaemon();
			startSync();
		});
		thread.setDaemon(true);
		thread.start();
	}

	public static synchronized SerialDiscoveryDaemon get() {
		return Holder.INSTANCE;
	}

	/**
	 * Use the holder idiom to do safe publication, see <a href=
	 * "https://shipilev.net/blog/2014/safe-public-construction/#_safe_publication">here</a>
	 * for more information.
	 *
	 */
	private static class Holder {
		public static final SerialDiscoveryDaemon INSTANCE = new SerialDiscoveryDaemon();
	}

	private synchronized void startDaemon() {
		try {
			serialDiscoveryProcess = new ProcessBuilder()
					.command(SerialDiscovery.getSerialDiscoveryBinary().toAbsolutePath().toString())
					.directory(SerialDiscovery.getSerialDiscoveryBinary().getParent().toAbsolutePath().toFile())
					.start();
		} catch (BinaryNotFoundException e) {
			throw new RuntimeException("Failed to locate the binary used for device enumeration.", e);
		} catch (IOException e) {
			throw new RuntimeException("Config files seem to be missing.", e);
		}
	}

	/**
	 * Returns a list of devices that are connected to the pc that can be identified
	 * to be a ftduino. This can be an empty list, as this is only a snapshot and it
	 * is possible that this snapshot is created BEFORE any device has been
	 * detected!
	 * 
	 * @return list of ftduino devices reason
	 */
	public synchronized static List<SerialDevice> getConnectedFtduinos() {
		SerialDiscoveryDaemon instance = SerialDiscoveryDaemon.get();
		ArrayList<SerialDevice> copy;
		synchronized (instance) {
			copy = new ArrayList<>(instance.devices.values());
		}
		return copy;
	}

	private SerialDisoveryMessage startSync() {
		PrintStream stdIn = new PrintStream(serialDiscoveryProcess.getOutputStream());
		stdIn.println("START_SYNC");
		stdIn.flush();
		InputStream inputStream = serialDiscoveryProcess.getInputStream();

		StringBuilder result = new StringBuilder(400);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
		try {
			String line;
			boolean isResult = false;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("{")) {
					isResult = true;
				}
				if (isResult) {
					result.append(line).append("\n");
				}
				if (line.startsWith("}")) {
					isResult = false;
					try {
						SerialDisoveryMessage message = fromJson(result.toString(), SerialDisoveryMessage.class);
						synchronized (this) {
							if (message instanceof AddMessage) {
								AddMessage addMessage = (AddMessage) message;
								SerialDevice device = addMessage.port;
								if (device.isFtduino()) {
									devices.put(device.getAddress(), device);
								}
							} else if (message instanceof RemoveMessage) {
								RemoveMessage removeMessage = (RemoveMessage) message;
								SerialDevice device = removeMessage.port;
								devices.remove(device.getAddress());
							}
						}
					} catch (JsonParseException | JsonMappingException e) {
						throw new RuntimeException("Failed to parse json output of serial-discovery.", e);
					} catch (IOException e) {
						throw new RuntimeException("Impossible exception.", e);
					}

					result.setLength(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("I/0 error while reading the output of serial-discovery.", e);
		}
		return null;
	}

	private static <T> T fromJson(String input, Class<T> javaType)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(input, javaType);
	}
}
