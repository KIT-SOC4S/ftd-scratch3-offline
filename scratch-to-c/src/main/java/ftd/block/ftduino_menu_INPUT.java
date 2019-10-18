package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.AnalogInputSpecifierField;
import ftd.field.AnalogInputSpecifierField.AnalogInputSpecifier;

/**
 * Not really a block but more like a wrapper for
 * {@link ftd.field.AnalogInputSpecifierField}. Converts the wrapped field to a
 * String consumable by Scratch.
 */
public class ftduino_menu_INPUT extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		AnalogInputSpecifier inputSpecifier = ((AnalogInputSpecifierField) fields).getInputSpecifier();
		String code = "scratchString(\"" + inputSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
