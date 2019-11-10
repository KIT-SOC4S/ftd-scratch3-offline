package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.AnalogInputSpecifierField;
import com.github.intrigus.ftd.field.InputModeField;

/**
 * Implements the ftduino input analog operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that reads an analog input and returns
 * the read value as a float. It expects an analog input specifier or a String
 * that can be converted to one. It also expects an input mode specifier or a
 * String that can be converted to one. The supported input modes are
 * {@link com.github.intrigus.ftd.field.InputModeField.InputMode#RESISTANCE} and
 * {@link com.github.intrigus.ftd.field.InputModeField.InputMode#VOLTAGE}
 */
@JsonIgnoreProperties(value = "inputs")
public class ftduino_input_analog extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "INPUT")
		public AnalogInputSpecifierField input;

		@JsonProperty(value = "MODE")
		public InputModeField mode;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_input_analog(" + fields.input.generateCode() + ", " + fields.mode.generateCode()
				+ ")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.input.updateRelations(blocks);
		this.fields.mode.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.REPORTER;
	}

}
