package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class OnOffStateField extends ScratchField {
	private List<String> ONOFFSTATE;
	public OnOffState state;

	@JsonCreator()
	private OnOffStateField(@JsonProperty(value = "ONOFFSTATE") List<String> onOffState) {
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
	public String toString() {
		return "OnOffStateField [ONOFFSTATE=" + ONOFFSTATE + ", state=" + state + "]";
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	public static enum OnOffState {

		ON, OFF;
		private static Map<String, OnOffState> namesMap = new HashMap<String, OnOffState>(4);

		static {
			namesMap.put("On", ON);
			namesMap.put("1", ON);
			namesMap.put("0", OFF);
			namesMap.put("Off", OFF);
		}

		@JsonCreator
		public static OnOffState forValue(String value) {
			if (!namesMap.containsKey(value)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(value);
		}

	}

}
