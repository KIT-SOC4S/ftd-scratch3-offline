package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.ScratchValue;
import com.github.intrigus.ftd.field.OutputSpecifierField;

// TODO
/**
 * Implements the ftduino output analog operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that outputs analog values in the range
 * [0, UNKOWN]. It expects an output specifier or a String that can be converted
 * to one. It also expects a value to set. This value will be converted to a
 * number before it's set.
 */
public class ftduino_output_analog extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "OUTPUT")
		public OutputSpecifierField output;
	}

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "VALUE")
		public ScratchValue value;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_output_analog(" + fields.output.generateCode() + ", "
				+ inputs.value.generateCode() + ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.output.updateRelations(blocks);
		this.inputs.value.updateRelations(blocks);
	}

}
