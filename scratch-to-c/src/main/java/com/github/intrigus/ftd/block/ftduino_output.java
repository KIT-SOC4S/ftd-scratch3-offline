package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.OnOffStateField;
import com.github.intrigus.ftd.field.OutputSpecifierField;

/**
 * Implements the ftduino output operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that outputs either 0 (LOW) or 1 (HIGH).
 * It expects an output specifier or a String that can be converted to one. It
 * also expects a value to set. This value will be converted to a boolean before
 * it's set.
 */
@JsonIgnoreProperties(value = "inputs")
public class ftduino_output extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "OUTPUT")
		public OutputSpecifierField output;

		@JsonProperty(value = "VALUE")
		public OnOffStateField value;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_output(" + fields.output.generateCode() + ", " + fields.value.generateCode()
				+ ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.output.updateRelations(blocks);
		this.fields.value.updateRelations(blocks);
	}

}
