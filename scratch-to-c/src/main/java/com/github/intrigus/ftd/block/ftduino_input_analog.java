package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the ftduino input analog operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that reads an analog input and returns
 * the read value as a float. It expects an analog input specifier or a String
 * that can be converted to one. It also expects an input mode specifier or a
 * String that can be converted to one. The supported input modes are
 * {@link com.github.intrigus.ftd.field.InputModeField.InputMode#RESISTANCE} and
 * {@link com.github.intrigus.ftd.field.InputModeField.InputMode#VOLTAGE}
 */
public class ftduino_input_analog extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;

		@JsonProperty(value = "MODE")
		public ScratchValue mode;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_input_analog(" + inputs.input.generateCode() + ", " + inputs.mode.generateCode()
				+ ")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
		this.inputs.mode.updateRelations(blocks);
	}

}
