package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.DigitalInputSpecifierField;

/**
 * Implements the ftduino input operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that reads a digital input and returns a
 * boolean. It expects a digital input specifier or a String that can be
 * converted to one.
 */
@JsonIgnoreProperties(value = "inputs")
public class ftduino_input extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "INPUT")
		public DigitalInputSpecifierField input;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_input(" + fields.input.generateCode() + ")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.input.updateRelations(blocks);
	}

}
