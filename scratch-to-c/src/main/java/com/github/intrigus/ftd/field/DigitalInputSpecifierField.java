package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the INPUT_D field. The digital input specifier is used for
 * the ftudino_input/ftudino_when_input blocks.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L713-L720"
 *      target="_top">ftduino INPUT_D field</a>
 */
public class DigitalInputSpecifierField extends ScratchField {
	private List<String> INPUT_D;
	private DigitalInputSpecifier inputSpecifier;

	@JsonCreator()
	private DigitalInputSpecifierField(@JsonProperty(index = 1) List<String> inputD) {
		if (inputD.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (inputD.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.INPUT_D = inputD;
		if (inputD.get(0) != null) {
			inputSpecifier = DigitalInputSpecifier.forValue(inputD.get(0));
		}
	}

	@Override
	public String generateCode() {
		String code = "scratchString(\"" + inputSpecifier.name() + "\")";
		return code;
	}

	@Override
	public String toString() {
		return "DigitalInputSpecifierField [INPUT_D=" + INPUT_D + ", inputSpecifier=" + inputSpecifier + "]";
	}

	/**
	 * Returns the parsed digital output specifier.
	 * 
	 * @return the parsed digital output specifier.
	 */
	public DigitalInputSpecifier getInputSpecifier() {
		return inputSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual digital output specifier as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L713-L720"
	 * target="_top">here</a>.
	 */
	public static enum DigitalInputSpecifier {

		I1, I2, I3, I4, I5, I6, I7, I8, C1, C2, C3, C4;

		private static Map<String, DigitalInputSpecifier> namesMap = new HashMap<String, DigitalInputSpecifier>(12);
		static {
			namesMap.put("I1", I1);
			namesMap.put("I2", I2);
			namesMap.put("I3", I3);
			namesMap.put("I4", I4);
			namesMap.put("I5", I5);
			namesMap.put("I6", I6);
			namesMap.put("I7", I7);
			namesMap.put("I8", I8);
			namesMap.put("C1", C1);
			namesMap.put("C2", C2);
			namesMap.put("C3", C3);
			namesMap.put("C4", C4);
		}

		@JsonCreator
		public static DigitalInputSpecifier forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}
}
