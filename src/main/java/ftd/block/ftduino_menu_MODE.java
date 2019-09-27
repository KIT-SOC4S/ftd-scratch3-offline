package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.InputModeField;
import ftd.field.InputModeField.InputMode;

public class ftduino_menu_MODE extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		InputMode inputMode = ((InputModeField) fields).getInputMode();
		String code = "scratchString(\"" + inputMode.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
