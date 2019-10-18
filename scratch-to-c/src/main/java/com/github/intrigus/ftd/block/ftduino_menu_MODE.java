package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.InputModeField;
import com.github.intrigus.ftd.field.InputModeField.InputMode;

/**
 * Not really a block but more like a wrapper for
 * {@link com.github.intrigus.ftd.field.InputModeField}. Converts the wrapped field to a String
 * consumable by Scratch.
 */
public class ftduino_menu_MODE extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		InputMode inputMode = ((InputModeField) fields).getInputMode();
		String code = "scratchString(\"" + inputMode.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
