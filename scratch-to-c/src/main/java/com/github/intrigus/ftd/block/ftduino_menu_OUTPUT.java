package com.github.intrigus.ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.intrigus.ftd.field.OutputSpecifierField;
import com.github.intrigus.ftd.field.OutputSpecifierField.OutputSpecifier;

/**
 * Not really a block but more like a wrapper for
 * {@link com.github.intrigus.ftd.field.OutputSpecifierField}. Converts the wrapped field to a
 * String consumable by Scratch.
 */
public class ftduino_menu_OUTPUT extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		OutputSpecifier outputSpecifier = ((OutputSpecifierField) fields).getOutputSpecifier();
		String code = "scratchString(\"" + outputSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update for fields?
	}

}
