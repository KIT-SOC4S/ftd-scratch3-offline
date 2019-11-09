package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the COUNTER field. The counter specifier is used for the
 * ftudino_input_counter/ftudino_clear_counter blocks.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L709-L712"
 *      target="_top">ftduino COUNTER field</a>
 */
public class CounterSpecifierField extends ScratchField {
	private List<String> COUNTER;
	private CounterSpecifier counterSpecifier;

	@JsonCreator()
	private CounterSpecifierField(@JsonProperty(index = 1) List<String> counter) {
		if (counter.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (counter.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.COUNTER = counter;
		if (counter.get(0) != null) {
			counterSpecifier = CounterSpecifier.forValue(counter.get(0));
		}
	}

	@Override
	public String generateCode() {
		String code = "scratchString(\"" + counterSpecifier.name() + "\")";
		return code;
	}

	@Override
	public String toString() {
		return "CounterSpecifierField [COUNTER=" + COUNTER + ", counterSpecifier=" + counterSpecifier + "]";
	}

	/**
	 * Returns the parsed counter specifier.
	 * 
	 * @return the parsed counter specifier.
	 */
	public CounterSpecifier getCounterSpecifier() {
		return counterSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual counter specifier as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L709-L712"
	 * target="_top">here</a>.
	 */
	public static enum CounterSpecifier {

		C1, C2, C3, C4;

		private static Map<String, CounterSpecifier> namesMap = new HashMap<>(4);
		static {
			namesMap.put("C1", C1);
			namesMap.put("C2", C2);
			namesMap.put("C3", C3);
			namesMap.put("C4", C4);
		}

		@JsonCreator
		public static CounterSpecifier forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}
}
