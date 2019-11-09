package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
		String expression = this.inputs.condition.generateCode();
		String code = "while(toBoolean(s_not(" + expression + "))) {\n";
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
