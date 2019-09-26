package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MenuInputD;
import ftd.field.MenuInputD.InputSpecifier;

public class ftduino_menu_INPUT_D extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		InputSpecifier inputSpecifier = ((MenuInputD) fields).getInputSpecifier();
		String code = "scratchString(\"" + inputSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update for fields?
	}

}
