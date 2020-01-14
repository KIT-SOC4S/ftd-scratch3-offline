package com.github.intrigus.ftd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import com.github.intrigus.ftd.exception.BinaryNotFoundException;
import com.github.intrigus.ftd.exception.CompilationFailedException;
import com.github.intrigus.ftd.util.OsUtil;

public class ArduinoCLI {

	public static void main(String[] args) {
		try {
			System.out.println(compileArduinoC(System.in));
		} /*
			 * catch (IOException e) { e.printStackTrace(System.err); System.exit(2); }
			 */ catch (RuntimeException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		} catch (CompilationFailedException e) {
			e.printStackTrace(System.err);
			System.exit(3);
		}
	}

	/**
	 * Converts a String to an InputStream. The String is assumed to be encoded
	 * using UTF8.
	 * 
	 * @param string the String to convert to an InputStream
	 * @return the String converted to an InputStream
	 */
	private static InputStream toInputStream(String string) {
		return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * @see ArduinoCLI#uploadArduinoC(InputStream, String)
	 */
	public static String uploadArduinoC(String input, String portSpecifier) throws CompilationFailedException {
		return uploadArduinoC(toInputStream(input), portSpecifier);
	}

	/**
	 * Expects an input stream that represents the generated C++ arduino file. The
	 * file is compiled with the Ftduino library and the necessary files for the
	 * scratch runtime. It is then uploaded to the Ftduino.
	 * 
	 * @param is            the input stream that represents the generated C++
	 *                      arduino file.
	 * @param portSpecifier the port that will be used for uploading i.e. the port
	 *                      the Ftduino is connected to.
	 * @return The log of the execution.
	 * @throws CompilationFailedException when the compilation or upload failed.
	 */
	public static String uploadArduinoC(InputStream is, String portSpecifier) throws CompilationFailedException {
		try {
			Objects.requireNonNull(is);
			Objects.requireNonNull(portSpecifier);
			Path tempDir = Files.createTempDirectory("scratch");
			Path sketchFile = tempDir.resolve(tempDir.getFileName() + ".ino");
			Files.createFile(sketchFile);
			sketchFile.toFile().deleteOnExit();
			tempDir.toFile().deleteOnExit();
			Files.copy(is, sketchFile, StandardCopyOption.REPLACE_EXISTING);
			ProcessResult arduinoResult = new ProcessExecutor()
					.command(getArduinoCliBinary().toAbsolutePath().toString(), "compile", "--config-file",
							getArduinoCliBinary().resolveSibling("arduino-cli.yaml").toAbsolutePath().toString(),
							"--upload", "--fqbn", "ftduino:avr:ftduino", "--port", portSpecifier, "--verify",
							tempDir.toAbsolutePath().toString())
					.directory(getArduinoCliBinary().getParent().toAbsolutePath().toFile()).destroyOnExit()
					.exitValueNormal().readOutput(true).executeNoTimeout();
			return arduinoResult.outputUTF8();
		} catch (BinaryNotFoundException e) {
			throw new CompilationFailedException("Failed to locate the binary used for compilation and upload.", e);
		} catch (IOException e) {
			throw new CompilationFailedException("Failed to create the necessary files for compilation and upload.", e);
		} catch (InvalidExitValueException e) {
			throw new CompilationFailedException(
					"Compilation and upload did not exit with exit value 0. Exit value: " + e.getExitValue(), e,
					e.getResult().outputUTF8());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CompilationFailedException("Compilation and upload got interrupted.", e);
		}
	}

	/**
	 * @see ArduinoCLI#compileArduinoC(InputStream)
	 */
	public static String compileArduinoC(String string) throws CompilationFailedException {
		return compileArduinoC(toInputStream(string));
	}

	/**
	 * Expects an input stream that represents the generated C++ arduino file. The
	 * file is compiled with the Ftduino library and the necessary files for the
	 * scratch runtime.
	 * 
	 * @param is the input stream that represents the generated C++ arduino file.
	 * @return The (compilation) log of the execution.
	 * @throws CompilationFailedException when the compilation failed.
	 */
	public static String compileArduinoC(InputStream is) throws CompilationFailedException {
		try {
			Objects.requireNonNull(is);
			long currentTime = System.currentTimeMillis();
			Path tempDir = Files.createTempDirectory("scratch" + currentTime);
			Path sketchFile = tempDir.resolve("sketch.ino");
			Files.createFile(sketchFile);
			sketchFile.toFile().deleteOnExit();
			tempDir.toFile().deleteOnExit();
			Files.copy(is, sketchFile, StandardCopyOption.REPLACE_EXISTING);
			ProcessResult arduinoResult = new ProcessExecutor()
					.command(getArduinoCliBinary().toAbsolutePath().toString(), "compile", "--config-file",
							getArduinoCliBinary().resolveSibling("arduino-cli.yaml").toAbsolutePath().toString(),
							"--fqbn", "ftduino:avr:ftduino", sketchFile.toAbsolutePath().toString())
					.directory(getArduinoCliBinary().getParent().toAbsolutePath().toFile()).destroyOnExit()
					.exitValueNormal().readOutput(true).executeNoTimeout();
			return arduinoResult.outputUTF8();
		} catch (BinaryNotFoundException e) {
			throw new CompilationFailedException("Failed to locate the binary used for compilation.", e);
		} catch (IOException e) {
			throw new CompilationFailedException("Failed to create the necessary files for compilation.", e);
		} catch (InvalidExitValueException e) {
			throw new CompilationFailedException(
					"Compilation did not exit with exit value 0. Exit value: " + e.getExitValue(), e,
					e.getResult().outputUTF8());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CompilationFailedException("Compilation got interrupted.", e);
		}
	}

	/**
	 * Returns the platform specific path of the {@code arduino-cli} binary. For
	 * example it returns "[current_working_dir]/arduino_cli/LINUX_64/arduino-cli"
	 * if the OS is Linux 64 Bit. The path is relative to the current working
	 * directory. If you are running this from another you have to call
	 * {@link ArduinoCLI#getArduinoCliBinary(Path)} with the appropriate path.
	 * <p>
	 * Supported architectures:
	 * <li>Windows, 32 & 64 Bit
	 * <li>Linux, 32 & 64 Bit
	 * <li>Linux ARM, 32 & 64 Bit
	 * <li>macOS, 64 Bit
	 * </p>
	 * 
	 * @return the platform specific path of the {@code arduino-cli} binary
	 * @throws BinaryNotFoundException thrown when the appropriate binary for this
	 *                                 platform could not be found
	 */
	public static Path getArduinoCliBinary() throws BinaryNotFoundException {
		return getArduinoCliBinary(Paths.get(""));
	}

	/**
	 * Returns the platform specific path of the {@code arduino-cli} binary. For
	 * example it returns "[workingDir]/arduino_cli/LINUX_64/arduino-cli" if the OS
	 * is Linux 64 Bit. The path is relative to the working dir specified in the
	 * parameter.
	 * <p>
	 * Supported architectures:
	 * <li>Windows, 32 & 64 Bit
	 * <li>Linux, 32 & 64 Bit
	 * <li>Linux ARM, 32 & 64 Bit
	 * <li>macOS, 64 Bit
	 * </p>
	 * 
	 * @param workingDir the directory where the binaries are relative to
	 * @return the platform specific path of the {@code arduino-cli} binary
	 * @throws BinaryNotFoundException thrown when the appropriate binary for this
	 *                                 platform could not be found
	 */
	public static Path getArduinoCliBinary(Path workingDir) throws BinaryNotFoundException {
		Objects.requireNonNull(workingDir);
		String directory = OsUtil.getTargetName();
		Path path = workingDir.resolve(Paths.get("arduino_cli", directory, OsUtil.mapExecutableName("arduino-cli")))
				.toAbsolutePath();
		if (!Files.exists(path)) {
			throw new BinaryNotFoundException(
					"The binary for the os could not be found. os.name: " + System.getProperty("os.name") + " os.arch: "
							+ System.getProperty("os.arch") + ". Binary path: " + path.toString());
		}
		return path;
	}
}
