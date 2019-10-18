package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.DigitalInputSpecifierField;
import ftd.field.DigitalInputSpecifierField.DigitalInputSpecifier;

/**
 * Not really a block but more like a wrapper for
 * {@link ftd.field.DigitalInputSpecifierField}. Converts the wrapped field to a
 * String consumable by Scratch.
 */
public class ftduino_menu_INPUT_D extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		DigitalInputSpecifier inputSpecifier = ((DigitalInputSpecifierField) fields).getInputSpecifier();
		String code = "scratchString(\"" + inputSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update for fields?
	}

}
