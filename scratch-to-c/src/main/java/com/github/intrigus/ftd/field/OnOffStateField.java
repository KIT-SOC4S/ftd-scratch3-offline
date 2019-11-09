package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the ONOFFSTATE ftduino field. The on off state is used for
 * the ftudino_output/ftduino_led blocks.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L696-L699"
 *      target="_top">ftduino ONOFFSTATE field</a>
 */
public class OnOffStateField extends ScratchField {
	private List<String> ONOFFSTATE;
	private OnOffState state;

	@JsonCreator()
	private OnOffStateField(@JsonProperty(index = 1) List<String> onOffState) {
		if (onOffState.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (onOffState.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.ONOFFSTATE = onOffState;
		this.state = OnOffState.forValue(onOffState.get(0));
	}

	@Override
	public String generateCode() {
		if (state == OnOffState.ON) {
			return "scratchBoolean(true)";
		} else if (state == OnOffState.OFF) {
			return "scratchBoolean(false)";
		} else {
			throw new RuntimeException("HUH?");
		}
	}

	@Override
	public String toString() {
		return "OnOffStateField [ONOFFSTATE=" + ONOFFSTATE + ", state=" + state + "]";
	}

	/**
	 * Returns the parsed state.
	 * 
	 * @return the parsed state.
	 */
	public OnOffState getState() {
		return state;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual on off state as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L696-L699"
	 * target="_top">here</a>.
	 */
	public static enum OnOffState {

		ON, OFF;

		private static Map<String, OnOffState> namesMap = new HashMap<String, OnOffState>(4);

		static {
			namesMap.put("ON", ON);
			namesMap.put("1", ON);
			namesMap.put("0", OFF);
			namesMap.put("OFF", OFF);
		}

		@JsonCreator
		public static OnOffState forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}

}
