package ftd.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class OnOffStateField extends ScratchField {
	private List<String> ONOFFSTATE;
	public State state;

	@JsonCreator()
	private OnOffStateField(@JsonProperty(value = "ONOFFSTATE") List<String> onOffState) {
		if (onOffState.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (onOffState.get(1) != null) {
			throw new IllegalStateException("expected null");
		}
		this.ONOFFSTATE = onOffState;
		this.state = State.forValue(onOffState.get(0));
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	@Override
	public String toString() {
		return "OnOffStateField [ONOFFSTATE=" + ONOFFSTATE + ", state=" + state + "]";
	}

	public static enum State {

		ON, OFF;
		private static Map<String, State> namesMap = new HashMap<String, State>(3);

		static {
			namesMap.put("On", ON);
			namesMap.put("1", ON); // TODO correct?
			namesMap.put("0", OFF);
		}

		@JsonCreator
		public static State forValue(String value) {
			if (!namesMap.containsKey(value)) {
				throw new IllegalStateException("unknown value:" + value);
			}
			return namesMap.get(value);
		}

	}

}
