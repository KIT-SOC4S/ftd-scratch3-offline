package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the MODE ftduino field. The input mode is used for the
 * ftudino_input_analog block.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L700-L702"
 *      target="_top">ftduino MODE field</a>
 */
public class InputModeField extends ScratchField {
	private List<String> MODE;
	private InputMode inputMode;

	@JsonCreator()
	private InputModeField(@JsonProperty(index = 1) List<String> mode) {
		if (mode.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (mode.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.MODE = mode;
		if (mode.get(0) != null) {
			inputMode = InputMode.forValue(mode.get(0));
		}
	}

	@Override
	public String generateCode() {
		String code = "scratchString(\"" + inputMode.name() + "\")";
		return code;
	}

	@Override
	public String toString() {
		return "InputModeField [MODE=" + MODE + ", inputMode=" + inputMode + "]";
	}

	/**
	 * Returns the parsed input mode.
	 * 
	 * @return the parsed input mode.
	 */
	public InputMode getInputMode() {
		return inputMode;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual input mode as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L700-L702"
	 * target="_top">here</a>.
	 */
	public static enum InputMode {

		VOLTAGE, RESISTANCE/* , SWITCH, COUNTER */;

		private static Map<String, InputMode> namesMap = new HashMap<String, InputMode>(4);

		static { // TODO
			namesMap.put("VOLTAGE", VOLTAGE);
			namesMap.put("RESISTANCE", RESISTANCE);/*
													 * namesMap.put("switch", SWITCH); namesMap.put("counter", COUNTER);
													 */
		}

		@JsonCreator
		public static InputMode forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}
}
