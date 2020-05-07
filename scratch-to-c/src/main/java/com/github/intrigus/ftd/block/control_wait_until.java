package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control wait until operator. It waits until the
 * specified condition is true. The condition value is converted to a boolean if
 * necessary.
 */
@JsonIgnoreProperties(value = { "fields" })
public class control_wait_until extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {

		@JsonProperty(value = "CONDITION")
		public ScratchValue condition;
	}

	@Override
	public String gen() {
		String expression = (inputs.condition != null ? inputs.condition.generateCode()
				: ScratchConstants.SCRATCH_FALSE);
		String code = "while(toBoolean(s_not((" + expression + ")))) {\n";
		code += "}\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		if (inputs.condition != null) {
			inputs.condition.updateRelations(blocks);
		}
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.STACK;
	}
}
