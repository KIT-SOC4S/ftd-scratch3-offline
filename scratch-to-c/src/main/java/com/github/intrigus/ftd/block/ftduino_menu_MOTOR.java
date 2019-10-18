package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.MotorSpecifierField;
import com.github.intrigus.ftd.field.MotorSpecifierField.MotorSpecifier;

/**
 * Not really a block but more like a wrapper for
 * {@link com.github.intrigus.ftd.field.MotorSpecifierField}. Converts the wrapped field to a String
 * consumable by Scratch.
 */
public class ftduino_menu_MOTOR extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		MotorSpecifier motorSpecifier = ((MotorSpecifierField) fields).getMotorSpecifier();
		String code = "scratchString(\"" + motorSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
