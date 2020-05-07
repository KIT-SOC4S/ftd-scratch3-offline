package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control forever operator. It repeats the specified
 * sub-blocks forever.
 */
@JsonIgnoreProperties(value = { "fields" })
public class control_forever extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "SUBSTACK")
		public ScratchValue subStack;
	}

	@Override
	public String gen() {
		if (next != null) {
			throw new IllegalStateException("nothing can be after forever block!");
		}
		String code = "while(1) {\n";
		if (inputs.subStack != null) {
			code += inputs.subStack.generateCode();
		}
		code += "}\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		if (inputs.subStack != null) {
			this.inputs.subStack.updateRelations(blocks);
		}
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.C;
	}
}
