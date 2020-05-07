package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.DigitalInputSpecifierField;

/**
 * Implements the ftduino when input operator. This is a hat block that is
 * triggered when a specified input is high. Expects a digital input specifier
 * or a String that can be converted to one.
 */
@JsonIgnoreProperties(value = "inputs")
public class ftduino_when_input extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "INPUT")
		public DigitalInputSpecifierField input;
	}

	@Override
	protected String beginGen() {
		String loopCode = "void loop() {\n";
		String ifCode = "if(toBoolean(scratch_ftduino_input(" + fields.input.generateCode() + "))) {\n";
		return loopCode + ifCode;
	}

	@Override
	protected String afterGen() {
		String ifBody = super.afterGen();
		String ifConditionEnd = "while(1) {\n delay(100);\n}\n}\n";
		String loopCodeEnd = "}\n";
		return ifBody + ifConditionEnd + loopCodeEnd;
	}

	@Override
	public String gen() {
		return "";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.input.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.HAT;
	}

}
