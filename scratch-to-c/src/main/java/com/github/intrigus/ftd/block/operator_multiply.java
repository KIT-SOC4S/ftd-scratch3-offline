package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch multiply operator. The actual C++ code is in
 * operators.cpp. Computes number1 * number2. Both inputs are converted to
 * floats if necessary. The returned value is a float.
 */
@JsonIgnoreProperties(value = { "fields" })
public class operator_multiply extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "NUM1")
		public ScratchValue number1;

		@JsonProperty(value = "NUM2")
		public ScratchValue number2;
	}

	@Override
	public String gen() {
		String number1 = (inputs.number1 != null ? inputs.number1.generateCode() : ScratchConstants.SCRATCH_ZERO);
		String number2 = (inputs.number2 != null ? inputs.number2.generateCode() : ScratchConstants.SCRATCH_ZERO);
		return "s_multiply((" + number1 + "), (" + number2 + "))";
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
