package com.github.intrigus.ftd.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.intrigus.ftd.exception.BinaryNotFoundException;
import com.github.intrigus.ftd.exception.ComputationFailedException;

/**
 * Gives access to the "serial-discovery" program that is included with
 * arduino-cli. "serial-discovery" (short: sd) supports a polling and an event
 * based mode. This class provides and event based mode. Also see
 * {@link SerialDiscovery}.
 *
 */
public class SerialDiscoveryDaemon {

	private List<SerialDiscoveryListener> listeners;

	private boolean started;
	Process serialDiscoveryProcess;

	private SerialDiscoveryDaemon() {
		this.listeners = new ArrayList<>();
		this.started = false;
	}

	private synchronized SerialDiscoveryDaemon startDaemon() {
		if (started) {
			return this;
		}
		this.startInternal();
		this.started = true;
		return this;
	}

	private synchronized void startInternal() {
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

	// TODO Docs
	public static interface SerialDiscoveryListener {
		void added(SerialDevice device);

		void removed(SerialDevice device);
	}

	// TODO Docs
	public synchronized void addListener(SerialDiscoveryListener listener) {
		listeners.add(listener);
	}

	// TODO Docs
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
		public static final SerialDiscoveryDaemon INSTANCE = new SerialDiscoveryDaemon().startDaemon();
	}

	private synchronized SerialDisoveryMessage issueCommand(String command) throws ComputationFailedException {
		PrintStream stdIn = new PrintStream(serialDiscoveryProcess.getOutputStream());
		stdIn.println(command);
		stdIn.flush();
		stdIn.close();
		InputStream inputStream = serialDiscoveryProcess.getInputStream();
		String result = "";
		// intentionally not closed. Closing it would also close the InputStream of the
		// process which we don't want
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
		try {
			String line;
			boolean isResult = false;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("{")) {
					isResult = true;
				}
				if (isResult) {
					result += line + "\n";
				}
				if (line.startsWith("}")) {
					isResult = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			return fromJson(result, SerialDisoveryMessage.class);
		} catch (JsonParseException | JsonMappingException e) {
			throw new ComputationFailedException("Failed to parse json output of serial-discovery.", e);
		} catch (IOException e) {
			throw new ComputationFailedException("Impossible exception.", e);
		}
	}

	private static <T> T fromJson(String input, Class<T> javaType)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(input, javaType);
	}
}
