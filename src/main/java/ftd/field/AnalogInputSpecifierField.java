package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class AnalogInputSpecifierField extends ScratchField {
	private List<String> INPUT;
	private AnalogInputSpecifier inputSpecifier;

	@JsonCreator()
	private AnalogInputSpecifierField(@JsonProperty(value = "INPUT") List<String> input) {
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
	public String toString() {
		return "AnalogInputSpecifierField [INPUT=" + INPUT + ", inputSpecifier=" + inputSpecifier + "]";
	}

	public AnalogInputSpecifier getInputSpecifier() {
		return inputSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

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
