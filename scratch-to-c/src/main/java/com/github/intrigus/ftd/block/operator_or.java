package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch or operator. The actual C++ code is in operators.cpp.
 * Computes operand1 || operand2. Both inputs are converted to booleans if
 * necessary. The returned value is a boolean.
 */
public class operator_or extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "OPERAND1")
		public ScratchValue operand1;

		@JsonProperty(value = "OPERAND2")
		public ScratchValue operand2;
	}

	public String gen() {
		String operand1 = (inputs.operand1 != null ? inputs.operand1.generateCode() : ScratchConstants.SCRATCH_FALSE);
		String operand2 = (inputs.operand2 != null ? inputs.operand2.generateCode() : ScratchConstants.SCRATCH_FALSE);
		return "s_or((" + operand1 + "), (" + operand2 + "))";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.operand1.updateRelations(blocks);
		this.inputs.operand2.updateRelations(blocks);
	}
}
