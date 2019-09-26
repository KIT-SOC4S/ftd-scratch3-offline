package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MenuMotor;
import ftd.field.MenuMotor.Motor;

public class ftduino_menu_MOTOR extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		Motor motorSpecifier = ((MenuMotor) fields).getMotorSpecifier();
		String code = "scratchString(\"" + motorSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
