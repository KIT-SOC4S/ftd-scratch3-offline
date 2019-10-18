package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MotorSpecifierField;
import ftd.field.MotorSpecifierField.MotorSpecifier;

/**
 * Not really a block but more like a wrapper for
 * {@link ftd.field.MotorSpecifierField}. Converts the wrapped field to a String
 * consumable by Scratch.
 */
public class ftduino_menu_MOTOR extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		MotorSpecifier motorSpecifier = ((MotorSpecifierField) fields).getMotorSpecifier();
		String code = "scratchString(\"" + motorSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
