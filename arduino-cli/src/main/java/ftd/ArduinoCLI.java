package ftd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import ftd.exception.BinaryNotFoundException;
import ftd.exception.CompilationFailedException;
import ftd.util.OsUtil;

public class ArduinoCLI {

	public static void main(String[] args) {
		try {
			System.out.println(compileArduinoC(new FileInputStream(new File("arduino-src/arduino-src.ino"))));
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		} catch (CompilationFailedException e) {
			e.printStackTrace(System.err);
			System.exit(3);
		}
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
		String directory = null;
		String bitness = OsUtil.IS_64_BIT ? "64" : "32";

		if (OsUtil.IS_WINDOWS) {
			directory = "WINDOWS" + "_" + bitness;
		} else if (OsUtil.IS_MAC) {
			directory = "MACOS" + "_" + bitness;
		} else if (OsUtil.IS_LINUX) {
			if (OsUtil.IS_ARM) {
				directory = "LINUX_ARM" + "_" + bitness;
			} else {
				directory = "LINUX" + "_" + bitness;
			}
		} else {
			throw new RuntimeException("Unsupported os. os.name: " + System.getProperty("os.name") + " os.arch: "
					+ System.getProperty("os.arch"));
		}
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
