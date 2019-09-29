package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

/**
 * Representation for the STOPMODE ftduino field. The stop mode is used for the
 * ftudino_motor_stop block.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L734-L736"
 *      target="_top">ftduino STOPMODE field</a>
 */
public class MotorStopModeField extends ScratchField {
	private List<String> STOPMODE;
	private MotorStopMode motorStopMode;

	@JsonCreator()
	private MotorStopModeField(@JsonProperty(value = "STOPMODE") List<String> stopMode) {
		if (stopMode.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (stopMode.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.STOPMODE = stopMode;
		if (stopMode.get(0) != null) {
			motorStopMode = MotorStopMode.forValue(stopMode.get(0));
		}
	}

	@Override
	public String toString() {
		return "MotorStopModeField [STOPMODE=" + STOPMODE + ", motorStopMode=" + motorStopMode + "]";
	}

	/**
	 * Returns the parsed motor stop mode.
	 * 
	 * @return the parsed motor stop mode.
	 */
	public MotorStopMode getMotorStopMode() {
		return motorStopMode;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual stop mode as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L734-L736"
	 * target="_top">here</a>.
	 */
	public static enum MotorStopMode {
		/**
		 * Stops the motor, I guess. I did not find a specific explanation of this in
		 * the ftduino documentation.
		 */
		STOP("OFF"),
		/**
		 * Actively (?) brakes the motor, I guess. I did not find a specific explanation
		 * of this in the ftduino documentation.
		 */
		BRAKE("BRAKE");

		private String associatedCValue;

		private MotorStopMode(String associatedCValue) {
			this.associatedCValue = associatedCValue;
		}

		public String getCRepresentation() {
			return associatedCValue;
		}

		private static Map<String, MotorStopMode> namesMap = new HashMap<String, MotorStopMode>(2);
		static {
			namesMap.put("OFF", STOP);
			namesMap.put("BRAKE", BRAKE);
		}

		@JsonCreator
		public static MotorStopMode forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}
}
