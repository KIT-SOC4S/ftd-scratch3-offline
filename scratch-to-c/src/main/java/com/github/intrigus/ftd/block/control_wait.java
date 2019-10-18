package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control wait operator. It waits the specified duration
 * (in seconds). The duration value is converted to a float if necessary.
 */
public class control_wait extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "DURATION")
		public ScratchValue duration;
	}

	public String gen() {
		if (inputs.duration != null) {
			return "delay(1000 * toNumber(" + inputs.duration.generateCode() + "));\n";
		} else {
			throw new RuntimeException("HUH?");
		}
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.duration.updateRelations(blocks);
	}
}
