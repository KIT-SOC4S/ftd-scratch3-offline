package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MotorStopModeField;
import ftd.field.MotorStopModeField.MotorStopMode;

public class ftduino_menu_STOPMODE extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		MotorStopMode stopMode = ((MotorStopModeField) fields).getMotorStopMode();
		String code = "scratchString(\"" + stopMode.getCRepresentation() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
