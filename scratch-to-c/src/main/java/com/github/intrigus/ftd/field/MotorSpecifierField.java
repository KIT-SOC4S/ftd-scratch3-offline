package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the MOTOR field. The motor specifier is used for the
 * ftudino_motor/ftudino_motor_stop blocks.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L727-L730"
 *      target="_top">ftduino MOTOR field</a>
 */
public class MotorSpecifierField extends ScratchField {
	private List<String> MOTOR;
	private MotorSpecifier motorSpecifier;

	@JsonCreator()
	private MotorSpecifierField(@JsonProperty(index = 1) List<String> motor) {
		if (motor.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (motor.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.MOTOR = motor;
		if (motor.get(0) != null) {
			motorSpecifier = MotorSpecifier.forValue(motor.get(0));
		}
	}

	@Override
	public String generateCode() {
		String code = "scratchString(\"" + motorSpecifier.name() + "\")";
		return code;
	}

	@Override
	public String toString() {
		return "MotorSpecifierField [MOTOR=" + MOTOR + ", motorSpecifier=" + motorSpecifier + "]";
	}

	/**
	 * Returns the parsed motor specifier.
	 * 
	 * @return the parsed motor specifier.
	 */
	public MotorSpecifier getMotorSpecifier() {
		return motorSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual motor specifier as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L727-L730"
	 * target="_top">here</a>.
	 */
	public static enum MotorSpecifier {

		M1, M2, M3, M4;

		private static Map<String, MotorSpecifier> namesMap = new HashMap<String, MotorSpecifier>(4);
		static {
			namesMap.put("M1", M1);
			namesMap.put("M2", M2);
			namesMap.put("M3", M3);
			namesMap.put("M4", M4);
		}

		@JsonCreator
		public static MotorSpecifier forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}

}
