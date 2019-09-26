package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.OnOffStateField;
import ftd.field.OnOffStateField.State;

public class ftduino_menu_ONOFFSTATE extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		State state = ((OnOffStateField) fields).state;
		if (state == State.ON) {
			return "scratchBoolean(true)";
		} else if (state == State.OFF) {
			return "scratchBoolean(false)";
		} else {
			throw new RuntimeException("HUH?");
		}
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
	}
}
