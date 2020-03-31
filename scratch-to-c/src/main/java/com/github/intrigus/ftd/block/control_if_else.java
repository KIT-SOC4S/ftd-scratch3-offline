package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control if else operator. It executes the specified
 * sub-blocks of subStack if the specified condition is true. Otherwise it
 * executes the specified sub-blocks of subStack2.
 */
@JsonIgnoreProperties(value = { "fields" })
public class control_if_else extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "SUBSTACK")
		public ScratchValue subStack;

		@JsonProperty(value = "SUBSTACK2")
		public ScratchValue subStack2;

		@JsonProperty(value = "CONDITION")
		public ScratchValue condition;
	}

	public String gen() {
		String condition = (inputs.condition != null ? inputs.condition.generateCode()
				: ScratchConstants.SCRATCH_FALSE);

		String code = "if(toBoolean(" + condition + ")) {\n";
		if (inputs.subStack != null) {
			code += inputs.subStack.generateCode();
		}
		code += "}\n";

		code += "else {\n";
		if (inputs.subStack2 != null) {
			code += inputs.subStack2.generateCode();
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
		if (inputs.subStack2 != null) {
			inputs.subStack2.updateRelations(blocks);
		}
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.C;
	}
}
