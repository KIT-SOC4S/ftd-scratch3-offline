package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the DIR ftduino field. The motor direction is used for the
 * ftudino_motor block.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L731-L733"
 *      target="_top">ftduino DIR field</a>
 */
public class MotorDirectionField extends ScratchField {
	private List<String> DIR;
	private MotorDir motorDir;

	@JsonCreator()
	private MotorDirectionField(@JsonProperty(value = "DIR") List<String> dir) {
		if (dir.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (dir.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.DIR = dir;
		if (dir.get(0) != null) {
			motorDir = MotorDir.forValue(dir.get(0));
		}
	}

	@Override
	public String toString() {
		return "MotorDirectionField [DIR=" + DIR + ", motorDir=" + motorDir + "]";
	}

	/**
	 * Returns the parsed motor direction.
	 * 
	 * @return the parsed motor direction.
	 */
	public MotorDir getMotorDirection() {
		return motorDir;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual motor direction as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L731-L733"
	 * target="_top">here</a>.
	 */
	public static enum MotorDir {

		LEFT, RIGHT;
		private static Map<String, MotorDir> namesMap = new HashMap<String, MotorDir>(2);
		static {
			namesMap.put("LEFT", LEFT);
			namesMap.put("RIGHT", RIGHT);
		}

		@JsonCreator
		public static MotorDir forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}
}
