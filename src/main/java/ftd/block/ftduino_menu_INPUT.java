package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MenuInput;
import ftd.field.MenuInput.InputSpecifier;

public class ftduino_menu_INPUT extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		InputSpecifier inputSpecifier = ((MenuInput) fields).getInputSpecifier();
		String code = "scratchString(\"" + inputSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
