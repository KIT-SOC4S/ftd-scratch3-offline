package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class OutputSpecifierField extends ScratchField {
	private List<String> OUTPUT;
	private OutputSpecifier outputSpecifier;

	@JsonCreator()
	private OutputSpecifierField(@JsonProperty(value = "OUTPUT") List<String> output) {
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
	public String toString() {
		return "OutputSpecifierField [OUTPUT=" + OUTPUT + ", outputSpecifier=" + outputSpecifier + "]";
	}

	public OutputSpecifier getOutputSpecifier() {
		return outputSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

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
