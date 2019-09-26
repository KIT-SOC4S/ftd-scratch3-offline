package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MenuOutput;
import ftd.field.MenuOutput.OutputSpecifier;

public class ftduino_menu_OUTPUT extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		OutputSpecifier outputSpecifier = ((MenuOutput) fields).getOutputSpecifier();
		String code = "scratchString(\"" + outputSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update for fields?
	}

}
