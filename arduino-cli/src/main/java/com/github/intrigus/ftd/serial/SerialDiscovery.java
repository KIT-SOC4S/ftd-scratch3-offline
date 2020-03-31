package com.github.intrigus.ftd.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.intrigus.ftd.exception.BinaryNotFoundException;
import com.github.intrigus.ftd.exception.CompilationFailedException;
import com.github.intrigus.ftd.exception.ComputationFailedException;
import com.github.intrigus.ftd.serial.SerialDisoveryMessage.ListMessage;
import com.github.intrigus.ftd.util.OsUtil;

/**
 * Gives access to the "serial-discovery" program that is included with
 * arduino-cli. "serial-discovery" (short: sd) supports a polling and an event
 * based mode. This class exposes a polled mode via
 * {@link #getConnectedFtduinos()}. A good description of what sd is capable of,
 * can be found
 * <a href="https://www.pjrc.com/arduino-pluggable-discovery/">here</a>. Also
 * see {@link SerialDiscoveryDaemon}.
 *
 */
public class SerialDiscovery {

	private Process serialDiscoveryProcess;

	private SerialDiscovery() {
		startSerialDiscoveryProcess();
	}

	private void startSerialDiscoveryProcess() {
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

	private SerialDisoveryMessage issueCommand(String command) throws ComputationFailedException {
		PrintStream stdIn = new PrintStream(serialDiscoveryProcess.getOutputStream());
		stdIn.println(command);
		stdIn.flush();
		stdIn.close();
		InputStream inputStream = serialDiscoveryProcess.getInputStream();
		String result = "";
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1)) {
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
			if (!command.equals("QUIT")) {
				stdIn.println("QUIT");
				stdIn.flush();
				stdIn.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ComputationFailedException("I/0 error while reading the output of serial-discovery.", e);
		}
		try {
			return fromJson(result, SerialDisoveryMessage.class);
		} catch (JsonParseException | JsonMappingException e) {
			throw new ComputationFailedException("Failed to parse json output of serial-discovery.", e);
		} catch (IOException e) {
			throw new ComputationFailedException("Impossible exception.", e);
		}
	}

	/**
	 * Returns a list of devices that are connected to the pc that can be identified
	 * to be a ftduino.
	 * 
	 * @return list of ftduino devices
	 * @throws CompilationFailedException when device enumeration failed for any
	 *                                    reason
	 */
	public static List<SerialDevice> getConnectedFtduinos() throws ComputationFailedException {
		SerialDisoveryMessage message = new SerialDiscovery().issueCommand("LIST");
		if (message instanceof ListMessage) {
			List<SerialDevice> devices = ((ListMessage) message).ports;
			devices.removeIf((device) -> !device.isFtduino());
			return devices;
		} else {
			throw new ComputationFailedException(
					"Failed to get connected ftduinos. List type isn't ListMessage, but " + message.getClass());
		}
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

	private static <T> T fromJson(String input, Class<T> javaType)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(input, javaType);
	}
}
