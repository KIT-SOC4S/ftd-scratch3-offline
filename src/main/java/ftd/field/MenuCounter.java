package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class MenuCounter extends ScratchField {
	private List<String> COUNTER;
	private CounterSpecifier counterSpecifier;

	@JsonCreator()
	private MenuCounter(@JsonProperty(value = "COUNTER") List<String> counter) {
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
	public String toString() {
		return "MenuCounter [COUNTER=" + COUNTER + ", counterSpecifier=" + counterSpecifier + "]";
	}

	public CounterSpecifier getCounterSpecifier() {
		return counterSpecifier;
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

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
