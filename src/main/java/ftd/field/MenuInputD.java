package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class MenuInputD extends ScratchField {
	private List<String> INPUT_D;
	private InputSpecifier inputSpecifier;

	@JsonCreator()
	private MenuInputD(@JsonProperty(value = "INPUT_D") List<String> inputD) {
		if (inputD.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (inputD.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.INPUT_D = inputD;
		if (inputD.get(0) != null) {
			inputSpecifier = InputSpecifier.forValue(inputD.get(0));
		}
	}

	@Override
	public String toString() {
		return "MenuInputD [INPUT_D=" + INPUT_D + ", inputSpecifier=" + inputSpecifier + "]";
	}

	public InputSpecifier getInputSpecifier() {
		return inputSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	public static enum InputSpecifier {

		I1, I2, I3, I4, I5, I6, I7, I8, C1, C2, C3, C4;
		private static Map<String, InputSpecifier> namesMap = new HashMap<String, InputSpecifier>(12);
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
		public static InputSpecifier forValue(String value) {
			String searchValue = value.toUpperCase(Locale.ROOT);
			if (!namesMap.containsKey(searchValue)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(searchValue);
		}

	}
}
