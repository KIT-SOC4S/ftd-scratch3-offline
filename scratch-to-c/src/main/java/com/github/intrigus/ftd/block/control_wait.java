package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control wait operator. It waits the specified duration
 * (in seconds). The duration value is converted to a float if necessary.
 */
@JsonIgnoreProperties(value = { "fields" })
public class control_wait extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "DURATION")
		public ScratchValue duration;
	}

	public String gen() {
		String duration = (inputs.duration != null ? inputs.duration.generateCode() : ScratchConstants.SCRATCH_ZERO);
		return "delay(1000 * toNumber(" + duration + "));\n";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		if (inputs.duration != null) {
			inputs.duration.updateRelations(blocks);
		}
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.STACK;
	}
}
