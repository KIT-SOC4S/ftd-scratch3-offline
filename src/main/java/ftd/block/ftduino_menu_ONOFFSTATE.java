package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.OnOffStateField;
import ftd.field.OnOffStateField.OnOffState;

/**
 * Not really a block but more like a wrapper for
 * {@link ftd.field.OnOffStateField}. Converts the wrapped field to a String
 * consumable by Scratch.
 */
public class ftduino_menu_ONOFFSTATE extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		OnOffState state = ((OnOffStateField) fields).getState();
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
