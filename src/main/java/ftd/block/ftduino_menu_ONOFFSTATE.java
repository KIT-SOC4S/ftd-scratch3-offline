package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.OnOffStateField;
import ftd.field.OnOffStateField.OnOffState;

public class ftduino_menu_ONOFFSTATE extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		OnOffState state = ((OnOffStateField) fields).state;
		if (state == OnOffState.ON) {
			return "scratchBoolean(true)";
		} else if (state == OnOffState.OFF) {
			return "scratchBoolean(false)";
		} else {
			throw new RuntimeException("HUH?");
		}
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
	}
}
