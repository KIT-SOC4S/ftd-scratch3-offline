package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the INPUT field. The analog input specifier is used for
 * the ftudino_input_analog block.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L703-L708"
 *      target="_top">ftduino INPUT field</a>
 */
public class AnalogInputSpecifierField extends ScratchField {
	private List<String> INPUT;
	private AnalogInputSpecifier inputSpecifier;

	@JsonCreator()
	private AnalogInputSpecifierField(@JsonProperty(index = 1) List<String> input) {
		if (input.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (input.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.INPUT = input;
		if (input.get(0) != null) {
			inputSpecifier = AnalogInputSpecifier.forValue(input.get(0));
		}
	}

	@Override
	public String generateCode() {
		String code = "scratchString(\"" + inputSpecifier.name() + "\")";
		return code;
	}

	@Override
	public String toString() {
		return "AnalogInputSpecifierField [INPUT=" + INPUT + ", inputSpecifier=" + inputSpecifier + "]";
	}

	/**
	 * Returns the parsed analog output specifier.
	 * 
	 * @return the parsed analog output specifier.
	 */
	public AnalogInputSpecifier getInputSpecifier() {
		return inputSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual analog output specifier as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L703-L708"
	 * target="_top">here</a>.
	 */
	public static enum AnalogInputSpecifier {

		I1, I2, I3, I4, I5, I6, I7, I8;

		private static Map<String, AnalogInputSpecifier> namesMap = new HashMap<String, AnalogInputSpecifier>(8);

		static {
			namesMap.put("I1", I1);
			namesMap.put("I2", I2);
			namesMap.put("I3", I3);
			namesMap.put("I4", I4);
			namesMap.put("I5", I5);
			namesMap.put("I6", I6);
			namesMap.put("I7", I7);
			namesMap.put("I8", I8);
		}

		@JsonCreator
		public static AnalogInputSpecifier forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}

}
