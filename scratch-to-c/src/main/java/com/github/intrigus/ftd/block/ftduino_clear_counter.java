package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.CounterSpecifierField;

/**
 * Implements the ftduino clear counter operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that clears a counter input to 0. It
 * expects a counter input specifier or a String that can be converted to one.
 */
@JsonIgnoreProperties(value = "inputs")
public class ftduino_clear_counter extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "INPUT")
		public CounterSpecifierField input;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_clear_counter(" + fields.input.generateCode() + ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.input.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.STACK;
	}

}
