package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch random operator. The actual C++ code is in
 * operators.cpp. Both inputs are converted to numbers if necessary. If both
 * inputs are ints a random int is returned. Otherwise a random float is
 * returned. The random value is from the range [number1, number2]. The returned
 * value is a float.
 */
@JsonIgnoreProperties(value = { "fields" })
public class operator_random extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "FROM")
		public ScratchValue number1;

		@JsonProperty(value = "TO")
		public ScratchValue number2;
	}

	@Override
	public String gen() {
		String number1 = (inputs.number1 != null ? inputs.number1.generateCode() : ScratchConstants.SCRATCH_ZERO);
		String number2 = (inputs.number2 != null ? inputs.number2.generateCode() : ScratchConstants.SCRATCH_ZERO);
		return "s_random(" + number1 + ", " + number2 + ")";

	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		if (inputs.number1 != null) {
			inputs.number1.updateRelations(blocks);
		}
		if (inputs.number2 != null) {
			inputs.number2.updateRelations(blocks);
		}
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.REPORTER;
	}
}
