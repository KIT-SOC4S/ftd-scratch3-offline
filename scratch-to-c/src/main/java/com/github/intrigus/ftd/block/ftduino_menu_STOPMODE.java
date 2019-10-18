package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.MotorStopModeField;
import com.github.intrigus.ftd.field.MotorStopModeField.MotorStopMode;

/**
 * Not really a block but more like a wrapper for
 * {@link com.github.intrigus.ftd.field.MotorStopModeField}. Converts the wrapped field to a String
 * consumable by Scratch.
 */
public class ftduino_menu_STOPMODE extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		MotorStopMode stopMode = ((MotorStopModeField) fields).getMotorStopMode();
		String code = "scratchString(\"" + stopMode.getCRepresentation() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
