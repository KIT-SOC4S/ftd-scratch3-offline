package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;

/**
 * Implements the ftduino when input operator. This is a hat block that is
 * triggered when a specified input is high. Expects a digital input specifier
 * or a String that can be converted to one.
 */
public class ftduino_when_input extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;
	}

	@Override
	protected String beginGen() {
		String loopCode = "void loop() {\n";
		String ifCode = "if(toBoolean(scratch_ftduino_input(" + inputs.input.generateCode() + "))) {\n";
		return loopCode + ifCode;
	}

	@Override
	protected String afterGen() {
		String ifBody = super.afterGen();
		String ifConditionEnd = "while(1) {\n delay(100);\n}\n}\n";
		String loopCodeEnd = "}\n";
		return ifBody + ifConditionEnd + loopCodeEnd;
	}

	public String gen() {
		return "";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
	}

}
