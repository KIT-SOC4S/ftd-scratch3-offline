package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

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

	public MotorStopMode getMotorStopMode() {
		return motorStopMode;
	}

	@Override
	public String toString() {
		return "MotorStopModeField [STOPMODE=" + STOPMODE + ", motorStopMode=" + motorStopMode + "]";
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	public static enum MotorStopMode {

		STOP("OFF"), BRAKE("BRAKE");

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
