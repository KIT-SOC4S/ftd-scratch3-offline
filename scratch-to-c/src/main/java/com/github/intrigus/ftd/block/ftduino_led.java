package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.OnOffStateField;

/**
 * Implements the ftduino led operator. Sets the built-in led to HIGH when the
 * input is true, otherwise to LOW. The input is converted to a boolean if
 * necessary.
 */
@JsonIgnoreProperties(value = "inputs")
public class ftduino_led extends ScratchBlock {

	@JsonProperty(value = "fields")
	private Field fields;

	private static class Field {
		@JsonProperty(value = "VALUE")
		public OnOffStateField value;
	}

	@Override
	public String gen() {
		if (this.fields.value != null) {
			return "digitalWrite(LED_BUILTIN, (toBoolean(" + fields.value.generateCode() + ")) ? HIGH : LOW);\n";
		} else {
			throw new RuntimeException("HUH?");
		}
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.fields.value.updateRelations(blocks);
	}

}
