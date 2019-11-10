package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch block that users can use to define their own
 * functions.
 */
@JsonIgnoreProperties(value = "fields")
public class procedures_definition extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "custom_block")
		public ScratchValue subStack;
	}

	@Override
	protected String beginGen() {
		String code = "void " + inputs.subStack.generateCode() + "() {\n";
		return code;
	}

	@Override
	public String gen() {
		return "";
	}

	@Override
	protected String afterGen() {
		return super.afterGen() + "}\n";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.subStack.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.CUSTOM_HAT;
	}
}
