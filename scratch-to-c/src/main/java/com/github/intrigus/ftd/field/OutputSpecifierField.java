package com.github.intrigus.ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.block.ScratchBlock;

/**
 * Representation for the OUTPUT ftduino field. The output specifier is used for
 * the ftudino_output/ftduino_output_analog blocks.
 * 
 * @see <a href=
 *      "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L721-L726"
 *      target="_top">ftduino OUTPUT field</a>
 */
public class OutputSpecifierField extends ScratchField {
	private List<String> OUTPUT;
	private OutputSpecifier outputSpecifier;

	@JsonCreator()
	private OutputSpecifierField(@JsonProperty(index = 1) List<String> output) {
		if (output.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (output.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.OUTPUT = output;
		if (output.get(0) != null) {
			outputSpecifier = OutputSpecifier.forValue(output.get(0));
		}
	}

	@Override
	public String generateCode() {
		String code = "scratchString(\"" + outputSpecifier.name() + "\")";
		return code;
	}

	@Override
	public String toString() {
		return "OutputSpecifierField [OUTPUT=" + OUTPUT + ", outputSpecifier=" + outputSpecifier + "]";
	}

	/**
	 * Returns the parsed output specifier.
	 * 
	 * @return the parsed output specifier.
	 */
	public OutputSpecifier getOutputSpecifier() {
		return outputSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	/**
	 * The actual output specifiers as specified <a href=
	 * "https://github.com/harbaum/scratch-vm/blob/9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/index.js#L721-L726"
	 * target="_top">here</a>.
	 */
	public static enum OutputSpecifier {

		O1, O2, O3, O4, O5, O6, O7, O8;

		private static Map<String, OutputSpecifier> namesMap = new HashMap<>(8);

		static {
			namesMap.put("O1", O1);
			namesMap.put("O2", O2);
			namesMap.put("O3", O3);
			namesMap.put("O4", O4);
			namesMap.put("O5", O5);
			namesMap.put("O6", O6);
			namesMap.put("O7", O7);
			namesMap.put("O8", O8);
		}

		@JsonCreator
		public static OutputSpecifier forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}
	}
}
