package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MotorDirectionField;
import ftd.field.MotorDirectionField.MotorDir;

public class ftduino_menu_DIR extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		MotorDir motorDirection = ((MotorDirectionField) fields).getMotorDirection();
		String code = "scratchString(\"" + motorDirection.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
