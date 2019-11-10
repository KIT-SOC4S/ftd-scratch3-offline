package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.CounterSpecifierField;

/**
 * Implements the ftduino input counter operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that reads a counter input and returns
 * the read value as a float. It expects a counter input specifier or a String
 * that can be converted to one.
 */
@JsonIgnoreProperties(value = "inputs")
public class ftduino_input_counter extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "INPUT")
		public CounterSpecifierField input;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_input_counter(" + fields.input.generateCode() + ")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.input.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.REPORTER;
	}

}
