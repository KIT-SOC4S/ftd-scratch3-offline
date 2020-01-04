package com.github.intrigus.ftd.internal.dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import com.github.intrigus.ftd.internal.util.FileUtil;
import com.github.intrigus.ftd.util.OsUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class ArduinoCliCreator extends DefaultTask {

	private static class UrlsForOS {
		String osName;
		String arduinoCliUrl;
		String arduinoCliTargetName;
		String avrGccUrl;
		String avrGccTargetName;
		String avrDudeUrl;
		String avrDudeTargetName;
		String ctagsUrl;
		String ctagsTargetName;
		String serialDiscoveryUrl;
		String serialDiscoveryTargetName;

		public UrlsForOS(String osName, String arduinoCliUrl, String arduinoCliTargetName, String avrGccUrl,
				String avrGccTargetName, String avrDudeUrl, String avrDudeTargetName, String ctagsUrl,
				String ctagsTargetName, String serialDiscoveryUrl, String serialDiscoveryTargetName) {
			this.osName = osName;
			this.arduinoCliUrl = arduinoCliUrl;
			this.arduinoCliTargetName = arduinoCliTargetName;
			this.avrGccUrl = avrGccUrl;
			this.avrGccTargetName = avrGccTargetName;
			this.avrDudeUrl = avrDudeUrl;
			this.avrDudeTargetName = avrDudeTargetName;
			this.ctagsUrl = ctagsUrl;
			this.ctagsTargetName = ctagsTargetName;
			this.serialDiscoveryUrl = serialDiscoveryUrl;
			this.serialDiscoveryTargetName = serialDiscoveryTargetName;
		}

	}

	private static final String ARDUINO_VERSION = "1.8.1";
	private static final String ARDUINO_CLI_VERSION = "0.7.1";
	private static final String FTDUINO_VERSION = "0.0.14";
	private static final String SERIAL_DISCOVERY_VERSION = "1.0.0";

	private static final UrlsForOS LINUX_64 = new UrlsForOS("LINUX_64",
			"https://downloads.arduino.cc/arduino-cli/arduino-cli_" + ARDUINO_CLI_VERSION + "_Linux_64bit.tar.gz",
			"cli_Linux_64bit.tar.gz",
			"http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-x86_64-pc-linux-gnu.tar.bz2",
			"7.3.0-atmel3.6.1-arduino5.tar.bz2",
			"http://downloads.arduino.cc/tools/avrdude-6.3.0-arduino17-x86_64-pc-linux-gnu.tar.bz2",
			"avrdude-6.3.0-arduino17.tar.bz2",
			"https://downloads.arduino.cc/tools/ctags-5.8-arduino11-pm-x86_64-pc-linux-gnu.tar.bz2",
			"ctags-5.8-arduino11-pm.tar.bz2",
			"https://downloads.arduino.cc/tools/serial-discovery-linux64-v" + SERIAL_DISCOVERY_VERSION + ".tar.bz2",
			"serial-discovery.tar.bz2");
	private static final UrlsForOS LINUX_32 = new UrlsForOS("LINUX_32",
			"https://downloads.arduino.cc/arduino-cli/arduino-cli_" + ARDUINO_CLI_VERSION + "_Linux_32bit.tar.gz",
			"cli_Linux_32bit.tar.gz",
			"http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-i686-pc-linux-gnu.tar.bz2",
			"7.3.0-atmel3.6.1-arduino5.tar.bz2",
			"http://downloads.arduino.cc/tools/avrdude-6.3.0-arduino17-i686-pc-linux-gnu.tar.bz2",
			"avrdude-6.3.0-arduino17.tar.bz2",
			"https://downloads.arduino.cc/tools/ctags-5.8-arduino11-pm-i686-pc-linux-gnu.tar.bz2",
			"ctags-5.8-arduino11-pm.tar.bz2",
			"https://downloads.arduino.cc/tools/serial-discovery-linux32-v" + SERIAL_DISCOVERY_VERSION + ".tar.bz2",
			"serial-discovery.tar.bz2");
	private static final UrlsForOS LINUX_ARM_64 = new UrlsForOS("LINUX_ARM_64",
			"https://downloads.arduino.cc/arduino-cli/arduino-cli_" + ARDUINO_CLI_VERSION + "_Linux_ARM64.tar.gz",
			"cli_Linux_ARM64.tar.gz",
			"http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-aarch64-pc-linux-gnu.tar.bz2",
			"7.3.0-atmel3.6.1-arduino5.tar.bz2",
			"http://downloads.arduino.cc/tools/avrdude-6.3.0-arduino17-aarch64-pc-linux-gnu.tar.bz2",
			"avrdude-6.3.0-arduino17.tar.bz2",
			"https://downloads.arduino.cc/tools/ctags-5.8-arduino11-pm-aarch64-linux-gnu.tar.bz2",
			"ctags-5.8-arduino11-pm.tar.bz2",
			"https://downloads.arduino.cc/tools/serial-discovery-linuxarm64-v" + SERIAL_DISCOVERY_VERSION + ".tar.bz2",
			"serial-discovery.tar.bz2");
	private static final UrlsForOS LINUX_ARM_32 = new UrlsForOS("LINUX_ARM_32",
			"https://downloads.arduino.cc/arduino-cli/arduino-cli_" + ARDUINO_CLI_VERSION + "_Linux_ARMv7.tar.gz",
			"cli_Linux_ARMv7.tar.gz",
			"http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-arm-linux-gnueabihf.tar.bz2",
			"7.3.0-atmel3.6.1-arduino5.tar.bz2",
			"http://downloads.arduino.cc/tools/avrdude-6.3.0-arduino17-armhf-pc-linux-gnu.tar.bz2",
			"avrdude-6.3.0-arduino17.tar.bz2",
			"https://downloads.arduino.cc/tools/ctags-5.8-arduino11-pm-armv6-linux-gnueabihf.tar.bz2",
			"ctags-5.8-arduino11-pm.tar.bz2",
			"https://downloads.arduino.cc/tools/serial-discovery-linuxarm-v" + SERIAL_DISCOVERY_VERSION + ".tar.bz2",
			"serial-discovery.tar.bz2");
	private static final UrlsForOS WINDOWS_64 = new UrlsForOS("WINDOWS_64",
			"https://downloads.arduino.cc/arduino-cli/arduino-cli_" + ARDUINO_CLI_VERSION + "_Windows_64bit.zip",
			"cli_Windows_64bit.zip",
			"http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-i686-w64-mingw32.zip",
			"7.3.0-atmel3.6.1-arduino5.zip",
			"http://downloads.arduino.cc/tools/avrdude-6.3.0-arduino17-i686-w64-mingw32.zip",
			"avrdude-6.3.0-arduino17.zip", "https://downloads.arduino.cc/tools/ctags-5.8-arduino11-pm-i686-mingw32.zip",
			"ctags-5.8-arduino11-pm.zip",
			"https://downloads.arduino.cc/tools/serial-discovery-windows-v" + SERIAL_DISCOVERY_VERSION + ".zip",
			"serial-discovery.zip");
	private static final UrlsForOS WINDOWS_32 = new UrlsForOS("WINDOWS_32",
			"https://downloads.arduino.cc/arduino-cli/arduino-cli_" + ARDUINO_CLI_VERSION + "_Windows_32bit.zip",
			"cli_Windows_32bit.zip",
			"http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-i686-w64-mingw32.zip",
			"7.3.0-atmel3.6.1-arduino5.zip",
			"http://downloads.arduino.cc/tools/avrdude-6.3.0-arduino17-i686-w64-mingw32.zip",
			"avrdude-6.3.0-arduino17.zip", "https://downloads.arduino.cc/tools/ctags-5.8-arduino11-pm-i686-mingw32.zip",
			"ctags-5.8-arduino11-pm.zip",
			"https://downloads.arduino.cc/tools/serial-discovery-windows-v" + SERIAL_DISCOVERY_VERSION + ".zip",
			"serial-discovery.zip");
	private static final UrlsForOS MACOS_64 = new UrlsForOS("MACOS_64",
			"https://downloads.arduino.cc/arduino-cli/arduino-cli_" + ARDUINO_CLI_VERSION + "_macOS_64bit.tar.gz",
			"cli_macOS_64bit.tar.gz",
			"http://downloads.arduino.cc/tools/avr-gcc-7.3.0-atmel3.6.1-arduino5-x86_64-apple-darwin14.tar.bz2",
			"7.3.0-atmel3.6.1-arduino5.tar.bz2",
			"http://downloads.arduino.cc/tools/avrdude-6.3.0-arduino17-x86_64-apple-darwin12.tar.bz2",
			"avrdude-6.3.0-arduino17.tar.bz2",
			"https://downloads.arduino.cc/tools/ctags-5.8-arduino11-pm-x86_64-apple-darwin.zip",
			"ctags-5.8-arduino11-pm.zip",
			"https://downloads.arduino.cc/tools/serial-discovery-macosx-v" + SERIAL_DISCOVERY_VERSION + ".tar.bz2",
			"serial-discovery.tar.bz2");

	// the shared client used for making http requests
	private final OkHttpClient client = new OkHttpClient();

	private static final List<UrlsForOS> URLS = Arrays.asList(LINUX_64, LINUX_32, LINUX_ARM_64, LINUX_ARM_32,
			WINDOWS_64, WINDOWS_32, MACOS_64);
	private static final String LIBRARY_INDEX_JSON_URL = "https://downloads.arduino.cc/libraries/library_index.json";
	private static final String PACAKGE_INDEX_JSON_URL = "https://downloads.arduino.cc/packages/package_index.json";
	private static final String PACKAGE_FTDUINO_INDEX_JSON_URL = "https://harbaum.github.io/ftduino/package_ftduino_index.json";

	private List<UrlsForOS> targets;

	private Path projectFolder = getProject().getProjectDir().toPath();

	@OutputDirectory
	private Path getBinariesFolder() {
		return projectFolder.resolve("arduino_cli");
	}

	private String target = "NATIVE";

	/**
	 * Determines which architecture will be targeted. Possible values:
	 * <p>
	 * <li>ALL - Builds all available architectures (default when
	 * {@code gradle distribute}ing.</li>
	 * <li>NATIVE - Only builds for the architecture of the current host
	 * (default).</li>
	 * <li>LINUX_64</li>
	 * <li>LINUX_32</li>
	 * <li>LINUX_ARM_64</li>
	 * <li>LINUX_ARM_32</li>
	 * <li>WINDOWS_64</li>
	 * <li>WINDOWS_32</li>
	 * <li>MACOS_64</li>
	 * </p>
	 * Other values are silently ignored!
	 * 
	 * @return
	 */
	@Input
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@InputDirectory
	private Path getScratchFtduinoLibraryFolder() {
		return getProject().findProject(":scratch-to-c").getProjectDir().toPath()
				.resolve(Paths.get("arduino-src", "Scratch_Ftduino_All"));
	}

	private static interface ExceptionWrappingConsumer<T> {
		void accept(T t) throws IOException;
	}

	@TaskAction
	public void createArduinoCli() throws IOException, InterruptedException {
		selectTargetArchitectures();
		fetchDependencies();

		ExecutorService otherTaskExecutor = Executors.newWorkStealingPool();
		for (UrlsForOS target : targets) {
			otherTaskExecutor.submit(() -> {
				try {
					stripUnnecessaryStuff(target);
					createFolderStructureForArduinoCli(target);
					copyScratchFtduinoLibraryToPackageFolder(target);
					patchFtduinoPlatformTxtDefinition(target);
					createArduinoCliConfigFile(target);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			});
		}
		otherTaskExecutor.shutdown();
		otherTaskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

	}

	private void fetchDependencies() throws InterruptedException {
		// execute all fetch task in parallel
		ExecutorService fetchTaskExecutor = Executors.newWorkStealingPool();

		ExceptionWrappingConsumer<UrlsForOS> fetchArduinoCli = (UrlsForOS target) -> fetchArduinoCli(target);
		ExceptionWrappingConsumer<UrlsForOS> fetchArduinoCores = (UrlsForOS target) -> fetchArduinoCores(target);
		ExceptionWrappingConsumer<UrlsForOS> fetchAvrGcc = (UrlsForOS target) -> fetchAvrGcc(target);
		ExceptionWrappingConsumer<UrlsForOS> fetchAvrDude = (UrlsForOS target) -> fetchAvrDude(target);
		ExceptionWrappingConsumer<UrlsForOS> fetchCTags = (UrlsForOS target) -> fetchCTags(target);
		ExceptionWrappingConsumer<UrlsForOS> fetchSerialDiscovery = (UrlsForOS target) -> fetchSerialDiscovery(target);
		ExceptionWrappingConsumer<UrlsForOS> fetchFtduinoLibs = (UrlsForOS target) -> fetchFtduinoLibs(target);
		ExceptionWrappingConsumer<UrlsForOS> fetchArduinoCliConfigFiles = (
				UrlsForOS target) -> fetchArduinoCliConfigFiles(target);
		List<ExceptionWrappingConsumer<UrlsForOS>> fetchFunctions = List.of(fetchArduinoCli, fetchArduinoCores,
				fetchAvrGcc, fetchAvrDude, fetchCTags, fetchSerialDiscovery, fetchFtduinoLibs,
				fetchArduinoCliConfigFiles);

		for (UrlsForOS target : targets) {
			for (ExceptionWrappingConsumer<UrlsForOS> fetchFunction : fetchFunctions) {
				fetchTaskExecutor.submit(() -> {
					try {
						fetchFunction.accept(target);
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				});
			}
		}
		fetchTaskExecutor.shutdown();
		fetchTaskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	}

	private void patchFtduinoPlatformTxtDefinition(UrlsForOS urls) throws IOException {
		Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
		Path packagesDir = targetDir.resolve("packages");

		Path ftduinoDir = packagesDir.resolve("ftduino");
		Path platformTxtFile = ftduinoDir.resolve(Paths.get("hardware/avr/" + FTDUINO_VERSION + "/platform.txt"));
		Files.writeString(platformTxtFile, "\n", StandardOpenOption.APPEND);
		Files.writeString(platformTxtFile, "recipe.output.tmp_file={build.project_name}.hex\n",
				StandardOpenOption.APPEND);
		Files.writeString(platformTxtFile, "recipe.output.save_file={build.project_name}.{build.variant}.hex\n",
				StandardOpenOption.APPEND);
	}

	private void selectTargetArchitectures() {
		String internalTarget = target;
		if (target.equals("ALL")) {
			targets = URLS;
			return;
		} else if (target.equals("NATIVE")) {
			internalTarget = OsUtil.getTargetName();
		}

		// Java requires realTarget to be final or effectively final which it can't
		// prove for internalTarget so we have to manually help it
		String realTarget = internalTarget;
		targets = URLS.stream().filter((it) -> it.osName.equals(realTarget)).collect(Collectors.toList());
	}

	private void copyScratchFtduinoLibraryToPackageFolder(UrlsForOS urls) throws IOException {
		Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
		Path packagesDir = targetDir.resolve("packages");

		Path ftduinoDir = targetDir.resolve("ftduino");
		Files.createDirectories(ftduinoDir);
		Path ftduinoTargetDir = packagesDir.resolve("ftduino").resolve("hardware").resolve("avr")
				.resolve(FTDUINO_VERSION);

		Path scratchFtduinoLibraryTargetDir = ftduinoTargetDir.resolve("libraries").resolve("Scratch_Ftduino_All");
		Files.createDirectories(scratchFtduinoLibraryTargetDir);

		FileUtil.copyDirectoryContent(getScratchFtduinoLibraryFolder(), scratchFtduinoLibraryTargetDir);
	}

	private void createArduinoCliConfigFile(UrlsForOS urls) throws IOException {
		Path targetFile = projectFolder.resolve("arduino_cli").resolve(urls.osName).resolve("arduino-cli.yaml");
		String arduinoCliConfigFilePath = "/com/github/intrigus/ftd/internal/dev/arduino-cli.yaml";
		InputStream arduinoCliConfigFileStream = ArduinoCliCreator.class.getResourceAsStream(arduinoCliConfigFilePath);
		Objects.requireNonNull(arduinoCliConfigFileStream,
				"Failed to get an InputStream for the ressource " + arduinoCliConfigFilePath);
		Files.copy(arduinoCliConfigFileStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
	}

	private void createFolderStructureForArduinoCli(UrlsForOS urls) throws IOException {
		Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
		Path packagesDir = targetDir.resolve("packages");
		Files.createDirectories(packagesDir);

		// packages/builtin/tools/
		Path builtinToolsDir = packagesDir.resolve("builtin").resolve("tools");
		Files.createDirectories(builtinToolsDir);
		Path ctagsDir = targetDir.resolve("ctags-5.8-arduino11");
		Path ctagsTargetDir = builtinToolsDir.resolve("ctags").resolve("5.8-arduino11");
		Files.createDirectories(ctagsTargetDir);
		FileUtil.moveDirectoryContent(ctagsDir, ctagsTargetDir);

		Path serialDiscoveryDir = targetDir.resolve("bin");
		Path serialDiscoveryTargetDir = builtinToolsDir.resolve("serial-discovery").resolve(SERIAL_DISCOVERY_VERSION);
		Files.createDirectories(serialDiscoveryTargetDir);
		FileUtil.moveDirectoryContent(serialDiscoveryDir, serialDiscoveryTargetDir);

		// packages/arduino/tools/
		Path arduinoToolsDir = packagesDir.resolve("arduino").resolve("tools");
		Files.createDirectories(arduinoToolsDir);
		Path avrDudeDir = targetDir.resolve("avrdude");
		Path avrDudeTargetDir = arduinoToolsDir.resolve("avrdude").resolve("6.3.0-arduino17");
		Files.createDirectories(avrDudeTargetDir);
		FileUtil.moveDirectoryContent(avrDudeDir, avrDudeTargetDir);

		Path avrGccDir = targetDir.resolve("avr-gcc");
		Path avrGccTargetDir = arduinoToolsDir.resolve("avr-gcc");
		Files.createDirectories(avrGccTargetDir);
		FileUtil.moveDirectoryContent(avrGccDir, avrGccTargetDir);

		// packages/arduino/hardware/
		Path arduinoHardwareDir = packagesDir.resolve("arduino").resolve("hardware");
		Files.createDirectories(arduinoHardwareDir);
		Path arduinoCoresDir = targetDir.resolve("avr");
		Path arduinoCoresTargetDir = arduinoHardwareDir.resolve("avr").resolve(ARDUINO_VERSION);
		Files.createDirectories(arduinoCoresTargetDir);
		FileUtil.moveDirectoryContent(arduinoCoresDir, arduinoCoresTargetDir);

		// packages/ftduino/
		Path ftduinoDir = targetDir.resolve("ftduino");
		Files.createDirectories(ftduinoDir);
		Path ftduinoTargetDir = packagesDir.resolve("ftduino").resolve("hardware").resolve("avr")
				.resolve(FTDUINO_VERSION);
		Files.createDirectories(ftduinoTargetDir);
		FileUtil.moveDirectoryContent(ftduinoDir, ftduinoTargetDir);
	}

	private void fetchArduinoCli(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(urls.arduinoCliUrl).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve(urls.arduinoCliTargetName);
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();

			FileUtil.extract(downloadedFile, targetDir);

			Files.delete(downloadedFile);
		}
	}

	private void fetchArduinoCliConfigFiles(UrlsForOS urls) throws IOException {
		fetchArduinoCliLibraryIndexJson(urls);
		fetchArduinoCliPackageIndexJson(urls);
		fetchArduinoCliFtduinoIndexJson(urls);
	}

	private void fetchArduinoCliLibraryIndexJson(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(LIBRARY_INDEX_JSON_URL).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve("library_index.json");
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();
		}
	}

	private void fetchArduinoCliPackageIndexJson(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(PACAKGE_INDEX_JSON_URL).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve("package_index.json");
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();
		}
	}

	private void fetchArduinoCliFtduinoIndexJson(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(PACKAGE_FTDUINO_INDEX_JSON_URL).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve("package_ftduino_index.json");
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();
		}
	}

	private void fetchArduinoCores(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder()
				.url("http://downloads.arduino.cc/cores/avr-" + ARDUINO_VERSION + ".tar.bz2").build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve("avr-" + ARDUINO_VERSION + ".tar.bz2");
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();

			FileUtil.extract(downloadedFile, targetDir);

			Files.delete(downloadedFile);
		}
	}

	private void fetchAvrDude(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(urls.avrDudeUrl).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve(urls.avrDudeTargetName);
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();

			FileUtil.extract(downloadedFile, targetDir);

			Files.delete(downloadedFile);
		}
	}

	private void fetchAvrGcc(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(urls.avrGccUrl).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName).resolve("avr-gcc");
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve(urls.avrGccTargetName);
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();

			FileUtil.extract(downloadedFile, targetDir);

			Path avrGccDir = targetDir.resolve("avr");
			Path avrGccDirTarget = targetDir.resolve("7.3.0-atmel3.6.1-arduino5");
			FileUtil.moveDirectoryContent(avrGccDir, avrGccDirTarget);

			Files.delete(downloadedFile);
		}
	}

	private void fetchCTags(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(urls.ctagsUrl).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve(urls.ctagsTargetName);
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();

			FileUtil.extract(downloadedFile, targetDir);

			Files.delete(downloadedFile);
		}
	}

	private void fetchFtduinoLibs(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url("https://github.com/harbaum/ftduino/releases/download/"
				+ FTDUINO_VERSION + "/ftduino-" + FTDUINO_VERSION + ".zip").build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve("ftduino-" + FTDUINO_VERSION + ".zip");
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();

			FileUtil.extract(downloadedFile, targetDir);

			Files.delete(downloadedFile);
		}
	}

	private void fetchSerialDiscovery(UrlsForOS urls) throws IOException {
		Request request = new Request.Builder().url(urls.serialDiscoveryUrl).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName);
			Files.createDirectories(targetDir);
			Path downloadedFile = targetDir.resolve(urls.serialDiscoveryTargetName);
			Files.createFile(downloadedFile);
			BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
			sink.writeAll(response.body().source());
			sink.close();

			FileUtil.extract(downloadedFile, targetDir);

			Files.delete(downloadedFile);
		}
	}

	private void stripFirstPart(UrlsForOS urls, Path targetDir) throws IOException {
		Path avrGccDir = targetDir.resolve("7.3.0-atmel3.6.1-arduino5");
		Path avrGccLibDir = avrGccDir
				.resolve("lib" + File.separator + "gcc" + File.separator + "avr" + File.separator + "7.3.0");
		File[] filesToDelete = avrGccLibDir.toFile().listFiles((dir, name) -> name.startsWith("tiny-stack")
				|| name.startsWith("avrxmega") || (name.startsWith("avr") && !name.equals("avr5")));
		for (File toDelete : filesToDelete) {
			FileUtil.deleteDirectory(toDelete.toPath());
		}
	}

	private void stripGdbObjdump(UrlsForOS urls, Path targetDir) {
		Path avrGccDir = targetDir.resolve("7.3.0-atmel3.6.1-arduino5");
		Path avrGccLibDir = avrGccDir.resolve("bin");
		File[] filesToDelete = avrGccLibDir.toFile()
				.listFiles((dir, name) -> name.startsWith("avr-gdb") || name.startsWith("avr-objdump"));
		for (File toDelete : filesToDelete) {
			toDelete.delete();
		}
	}

	private void stripSecondPart(UrlsForOS urls, Path targetDir) throws IOException {
		Path avrGccDir = targetDir.resolve("7.3.0-atmel3.6.1-arduino5");
		Path avrGccLibDir = avrGccDir.resolve("avr").resolve("lib");
		File[] filesToDelete = avrGccLibDir.toFile().listFiles((dir, name) -> name.startsWith("tiny-stack")
				|| name.startsWith("avrxmega") || (name.startsWith("avr") && !name.equals("avr5")));
		for (File toDelete : filesToDelete) {
			FileUtil.deleteDirectory(toDelete.toPath());
		}
	}

	private void stripUnnecessaryStuff(UrlsForOS urls) throws IOException {
		Path targetDir = projectFolder.resolve("arduino_cli").resolve(urls.osName).resolve("avr-gcc");
		stripFirstPart(urls, targetDir);
		stripSecondPart(urls, targetDir);
		stripGdbObjdump(urls, targetDir);
	}

}
