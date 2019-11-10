package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;
import com.github.intrigus.ftd.field.MotorDirectionField;
import com.github.intrigus.ftd.field.MotorSpecifierField;

//TODO
/**
 * Implements the ftduino motor operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that drives a specified motor in a
 * specified direction with a specified value. It expects a motor specifier or a
 * String that can be converted to one. It also expects a direction specifier or
 * a String that can be converted to one. It also expects a value to set. This
 * value will be converted to a number before it's set. The value should be in
 * the range [0, UNKOWN].
 */
public class ftduino_motor extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "MOTOR")
		public MotorSpecifierField motor;

		@JsonProperty(value = "DIR")
		public MotorDirectionField dir;
	}

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "VALUE")
		public ScratchValue value;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_motor(" + fields.motor.generateCode() + ", " + fields.dir.generateCode() + ", "
				+ inputs.value.generateCode() + ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.motor.updateRelations(blocks);
		this.fields.dir.updateRelations(blocks);
		this.inputs.value.updateRelations(blocks);
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.STACK;
	}

}
