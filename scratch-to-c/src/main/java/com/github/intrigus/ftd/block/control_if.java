package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchConstants;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the scratch control if operator. It executes the specified
 * sub-blocks if the specified condition is true.
 */
@JsonIgnoreProperties(value = { "fields"})
public class control_if extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "SUBSTACK")
		public ScratchValue subStack;

		@JsonProperty(value = "CONDITION")
		public ScratchValue condition;
	}

	public String gen() {
		String condition = (inputs.condition != null ? inputs.condition.generateCode()
				: ScratchConstants.SCRATCH_FALSE);
		String code = "if(toBoolean(" + condition + ")) {\n";
		if (this.inputs.subStack != null) {
			code += inputs.subStack.generateCode();
		}
		code += "}\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.condition.updateRelations(blocks);
		this.inputs.subStack.updateRelations(blocks);
	}
}
