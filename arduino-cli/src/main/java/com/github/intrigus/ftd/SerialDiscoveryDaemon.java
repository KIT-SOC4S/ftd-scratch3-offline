package com.github.intrigus.ftd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.intrigus.ftd.exception.BinaryNotFoundException;
import com.github.intrigus.ftd.exception.CompilationFailedException;
import com.github.intrigus.ftd.util.OsUtil;

/**
 * Gives access to the "serial-discovery" program that is included with
 * arduino-cli. "serial-discovery" (short: sd) supports a polling and an event
 * based mode. Currently only the polled mode is exposed via
 * {@link #getConnectedFtduinos()}. A good description of what sd is capable of,
 * can be found
 * <a href="https://www.pjrc.com/arduino-pluggable-discovery/">here</a>.
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
					.command(getSerialDiscoveryBinary().toAbsolutePath().toString())
					.directory(getSerialDiscoveryBinary().getParent().toAbsolutePath().toFile()).start();
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

	private synchronized String issueCommand(String command) {
		PrintStream stdIn = new PrintStream(serialDiscoveryProcess.getOutputStream());
		stdIn.println(command);
		stdIn.flush();
		InputStream inputStream = serialDiscoveryProcess.getInputStream();
		String result = "";
		// intentionally not closed. Closing it would also close the InputStream of the
		// process which we don't want
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
		return result;
	}

	/**
	 * Returns a list of devices that are connected to the pc that can be identified
	 * to be a ftduino.
	 * 
	 * @return list of ftduino devices
	 * @throws CompilationFailedException when device enumeration failed for any
	 *                                    reason
	 */
	public static List<SerialDevice> getConnectedFtduinos() throws CompilationFailedException {
		String jsonResult = SerialDiscoveryDaemon.get().issueCommand("LIST");
		try {
			ListMessage message = fromJson(jsonResult, ListMessage.class);
			List<SerialDevice> devices = message.ports;
			devices.removeIf((device) -> !device.isFtduino());
			return devices;
		} catch (JsonParseException | JsonMappingException e) {
			throw new CompilationFailedException("Failed to parse json output of device enumeration.", e);
		} catch (IOException e) {
			throw new CompilationFailedException("Impossible exception.", e);
		}

	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "eventType")
	@JsonSubTypes({ @Type(value = QuitMessage.class, name = "quit"), @Type(value = AddMessage.class, name = "add"),
			@Type(value = ListMessage.class, name = "list"), @Type(value = RemoveMessage.class, name = "remove") })
	private abstract static class SerialDisoveryMessage {
	}

	private static class AddMessage extends SerialDisoveryMessage {

	}

	private static class QuitMessage extends SerialDisoveryMessage {

	}

	private static class RemoveMessage extends SerialDisoveryMessage {

	}

	private static class ListMessage extends SerialDisoveryMessage {
		@JsonProperty(value = "ports")
		private List<SerialDevice> ports;
	}

	/**
	 * Returns the platform specific path of the {@code serial-discovery} binary.
	 * For example it returns
	 * "[current_working_dir]/arduino_cli/LINUX_64/packages/builtin/tools/serial-discovery/1.0.0/serial-discovery"
	 * if the OS is Linux 64 Bit. The path is relative to the current working
	 * directory. If you are running this from another you have to call
	 * {@link SerialDiscovery#getSerialDiscoveryBinary(Path)} with the appropriate
	 * path.
	 * <p>
	 * Supported architectures:
	 * <li>Windows, 32 & 64 Bit
	 * <li>Linux, 32 & 64 Bit
	 * <li>Linux ARM, 32 & 64 Bit
	 * <li>macOS, 64 Bit
	 * </p>
	 * 
	 * @return the platform specific path of the {@code serial-discovery} binary
	 * @throws BinaryNotFoundException thrown when the appropriate binary for this
	 *                                 platform could not be found
	 */
	public static Path getSerialDiscoveryBinary() throws BinaryNotFoundException {
		return getSerialDiscoveryBinary(Paths.get(""));
	}

	/**
	 * Returns the platform specific path of the {@code serial-discovery} binary.
	 * For example it returns
	 * "[workingDir]/arduino_cli/LINUX_64/packages/builtin/tools/serial-discovery/1.0.0/serial-discovery"
	 * if the OS is Linux 64 Bit. The path is relative to the working dir specified
	 * in the parameter.
	 * <p>
	 * Supported architectures:
	 * <li>Windows, 32 & 64 Bit
	 * <li>Linux, 32 & 64 Bit
	 * <li>Linux ARM, 32 & 64 Bit
	 * <li>macOS, 64 Bit
	 * </p>
	 * 
	 * @param workingDir the directory where the binaries are relative to
	 * @return the platform specific path of the {@code serial-discovery} binary
	 * @throws BinaryNotFoundException thrown when the appropriate binary for this
	 *                                 platform could not be found
	 */
	public static Path getSerialDiscoveryBinary(Path workingDir) throws BinaryNotFoundException {
		Objects.requireNonNull(workingDir);
		String directory = OsUtil.getTargetName();
		Path path = workingDir.resolve(Paths.get("arduino_cli", directory, "packages", "builtin", "tools",
				"serial-discovery", "1.0.0", OsUtil.mapExecutableName("serial-discovery"))).toAbsolutePath();
		if (!Files.exists(path)) {
			throw new BinaryNotFoundException(
					"The binary for the os could not be found. os.name: " + System.getProperty("os.name") + " os.arch: "
							+ System.getProperty("os.arch") + ". Binary path: " + path.toString());
		}
		return path;
	}

	public static void main(String[] args) throws CompilationFailedException {
		List<SerialDevice> ports = SerialDiscoveryDaemon.getConnectedFtduinos();
		System.out.println(ports);
	}

	private static <T> T fromJson(String input, Class<T> javaType)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(input, javaType);
	}
}
