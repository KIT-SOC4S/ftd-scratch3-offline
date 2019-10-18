package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.MotorDirectionField;
import com.github.intrigus.ftd.field.MotorDirectionField.MotorDir;

/**
 * Not really a block but more like a wrapper for
 * {@link com.github.intrigus.ftd.field.MotorDirectionField}. Converts the wrapped field to a String
 * consumable by Scratch.
 */
public class ftduino_menu_DIR extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		MotorDir motorDirection = ((MotorDirectionField) fields).getMotorDirection();
		String code = "scratchString(\"" + motorDirection.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
