package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the ftduino clear counter operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that clears a counter input to 0. It
 * expects a counter input specifier or a String that can be converted to one.
 */
public class ftduino_clear_counter extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_clear_counter(" + inputs.input.generateCode() + ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
	}

}
