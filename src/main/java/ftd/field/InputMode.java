package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class InputMode extends ScratchField {
	private List<String> MODE;
	private Mode inputMode;

	@JsonCreator()
	private InputMode(@JsonProperty(value = "MODE") List<String> mode) {
		if (mode.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (mode.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.MODE = mode;
		if (mode.get(0) != null) {
			inputMode = Mode.forValue(mode.get(0));
		}
	}

	@Override
	public String toString() {
		return "InputMode [MODE=" + MODE + ", inputMode=" + inputMode + "]";
	}

	public Mode getInputMode() {
		return inputMode;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	public static enum Mode {

		VOLTAGE, RESISTANCE, SWITCH, COUNTER;
		private static Map<String, Mode> namesMap = new HashMap<String, Mode>(4);

		static {
			namesMap.put("voltage", VOLTAGE);
			namesMap.put("resistance", RESISTANCE);
			namesMap.put("switch", SWITCH);
			namesMap.put("counter", COUNTER);
		}

		@JsonCreator
		public static Mode forValue(String value) {
			if (!namesMap.containsKey(value)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(value);
		}

	}
}
