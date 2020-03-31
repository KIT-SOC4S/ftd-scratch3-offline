package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control repeat operator. It repeats the specified
 * sub-blocks the specified times.
 */
@JsonIgnoreProperties(value = { "fields" })
public class control_repeat extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "SUBSTACK")
		public ScratchValue subStack;

		@JsonProperty(value = "TIMES")
		public ScratchValue times;
	}

	@Override
	protected String beginGen() {
		String expression = (inputs.times != null ? inputs.times.generateCode() : ScratchConstants.SCRATCH_ZERO);
		return "for(uint32_t i = 0; toBoolean(s_lt(scratchNumber(i), (" + expression + "))); i++) {\n";
	}

	@Override
	protected String afterGen() {
		return super.afterGen() + "}\n";
	}

	@Override
	protected String gen() {
		if (inputs.subStack != null) {
			return inputs.subStack.generateCode();
		} else {
			return "";
		}
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		if (inputs.subStack != null) {
			inputs.subStack.updateRelations(blocks);
		}
		if (inputs.times != null) {
			inputs.times.updateRelations(blocks);
		}
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.C;
	}
}
