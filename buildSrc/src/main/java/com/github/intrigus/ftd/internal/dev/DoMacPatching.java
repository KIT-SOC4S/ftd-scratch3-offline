package com.github.intrigus.ftd.internal.dev;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

/**
 * The program that parcl (packager that creates .exe, .sh and .app files) uses
 * for bundling on macOS <a
 * href="https://github.com/TheInfiniteKind/appbundler>appbundler</a> sets the
 * working directory by default to <a href=
 * "https://github.com/TheInfiniteKind/appbundler/blob/master/appbundler/native/main.m#L162">the
 * user home</a> directory. This is not acceptable. Work around by doing the
 * following: See
 * <a href="https://de.wikipedia.org/wiki/Application_Bundle#Struktur">here</a>
 * for the structure of an mac app bundle.
 * <p>
 * <li>Creating a starter script (`fus`) that changes the working dir to the
 * folder where the executable file is located.</li>
 * <li>Patching the Info.plist file such that the key "WorkingDirectory" refers
 * to "../Resources/"</li>
 * <li>Patching the Info.plist file such that the key "CFBundleExecutable" (the
 * executable that will be executed when the app is opened) refers to our new
 * starter script, `fus`</li>
 * </p>
 *
 */
public class DoMacPatching extends DefaultTask {

	private String appBundleName;

	@Input
	public String getAppBundleName() {
		return appBundleName;
	}

	public void setAppBundleName(String name) {
		this.appBundleName = name;
	}

	private Path getFusStarterFile() {
		Project ftdUiServerProject = getProject().findProject(":ftd-ui-server");
		return ftdUiServerProject.getBuildDir().toPath()
				.resolve(Paths.get("mac/", getAppBundleName() + ".app", "Contents", "MacOS", "fus"));
	}

	private Path getInfoPlistFile() {
		Project ftdUiServerProject = getProject().findProject(":ftd-ui-server");
		return ftdUiServerProject.getBuildDir().toPath()
				.resolve(Paths.get("mac/", getAppBundleName() + ".app", "Contents", "Info.plist"));
	}

	@TaskAction
	public void patchMac() throws IOException, InterruptedException {
		if (Os.isFamily(Os.FAMILY_MAC)) {
			String patchedInfoPlist = Files.readString(getInfoPlistFile())
					.replaceFirst("<dict>", "<dict>\n<key>WorkingDirectory</key>\n<string>../Resources/</string>")
					.replaceFirst("<key>CFBundleExecutable</key>\\R<string>JavaAppLauncher</string>",
							"<key>CFBundleExecutable</key>\n<string>fus</string>");
			Files.writeString(getInfoPlistFile(), patchedInfoPlist, StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.WRITE);

			String fusStarterPath = "/com/github/intrigus/ftd/internal/dev/fus";
			InputStream fusStarterPathStream = ArduinoCliCreator.class.getResourceAsStream(fusStarterPath);
			Objects.requireNonNull(fusStarterPathStream,
					"Failed to get an InputStream for the ressource " + fusStarterPath);
			Files.copy(fusStarterPathStream, getFusStarterFile(), StandardCopyOption.REPLACE_EXISTING);
			Set<PosixFilePermission> executablePermissions = new HashSet<>();
			executablePermissions.add(PosixFilePermission.GROUP_EXECUTE);
			executablePermissions.add(PosixFilePermission.OTHERS_EXECUTE);
			executablePermissions.add(PosixFilePermission.OWNER_EXECUTE);
			Set<PosixFilePermission> currentPermissions = Files.getPosixFilePermissions(getFusStarterFile());
			executablePermissions.addAll(currentPermissions);
			Files.setPosixFilePermissions(getFusStarterFile(), executablePermissions);
		} else {
			return;
		}
	}

}
