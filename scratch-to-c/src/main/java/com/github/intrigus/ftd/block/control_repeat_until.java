package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control repeat until operator. It repeats the
 * specified sub-blocks until the specified condition is true.
 */
@JsonIgnoreProperties(value = { "fields" })
public class control_repeat_until extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "SUBSTACK")
		public ScratchValue subStack;

		@JsonProperty(value = "CONDITION")
		public ScratchValue condition;
	}

	public String gen() {
		String expression = (inputs.condition != null ? inputs.condition.generateCode()
				: ScratchConstants.SCRATCH_FALSE);
		String code = "while(toBoolean(s_not(" + expression + "))) {\n";
		if (inputs.subStack != null) {
			code += inputs.subStack.generateCode();
		}
		code += "}\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		if (inputs.condition != null) {
			inputs.condition.updateRelations(blocks);
		}
		if (inputs.subStack != null) {
			inputs.subStack.updateRelations(blocks);
		}
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.C;
	}
}
