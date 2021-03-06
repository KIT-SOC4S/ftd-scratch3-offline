/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.github.intrigus.ftd.util;

/**
 * Used to identify the os and bitness of a platform. Taken from <a href=
 * "https://github.com/libgdx/libgdx/blob/3e32ba96dab989d42bf683418c96974d21e02612/gdx/src/com/badlogic/gdx/utils/SharedLibraryLoader.java">libgdx</a>
 * 
 * @author mzechner
 * @author Nathan Sweet
 */
public class OsUtil {
	static final public boolean IS_WINDOWS = System.getProperty("os.name").contains("Windows");
	static final public boolean IS_LINUX = System.getProperty("os.name").contains("Linux");
	static final public boolean IS_MAC = System.getProperty("os.name").contains("Mac");
	static final public boolean IS_ARM = System.getProperty("os.arch").startsWith("arm")
			|| System.getProperty("os.arch").startsWith("aarch64");
	static final public boolean IS_64_BIT = System.getProperty("os.arch").contains("64")
			|| System.getProperty("os.arch").startsWith("armv8");

	/** Maps a platform independent executable name to a platform dependent name. */
	public static String mapExecutableName(String executableName) {
		if (IS_WINDOWS) {
			return executableName + ".exe";
		} else {
			return executableName;
		}
	}

	/**
	 * Returns the name of this target.
	 * <p>
	 * <li>LINUX_64</li>
	 * <li>LINUX_32</li>
	 * <li>LINUX_ARM_64</li>
	 * <li>LINUX_ARM_32</li>
	 * <li>WINDOWS_64</li>
	 * <li>WINDOWS_32</li>
	 * <li>MACOS_64</li>
	 * </p>
	 * 
	 * @return
	 */
	public static String getTargetName() {
		String internalTarget;
		String bitness = OsUtil.IS_64_BIT ? "64" : "32";

		if (OsUtil.IS_WINDOWS) {
			internalTarget = "WINDOWS" + "_" + bitness;
		} else if (OsUtil.IS_MAC) {
			internalTarget = "MACOS" + "_" + bitness;
		} else if (OsUtil.IS_LINUX) {
			if (OsUtil.IS_ARM) {
				internalTarget = "LINUX_ARM" + "_" + bitness;
			} else {
				internalTarget = "LINUX" + "_" + bitness;
			}
		} else {
			throw new RuntimeException("Unsupported os. os.name: " + System.getProperty("os.name") + " os.arch: "
					+ System.getProperty("os.arch"));
		}
		return internalTarget;
	}
}
