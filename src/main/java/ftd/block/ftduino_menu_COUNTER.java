package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.field.MenuCounter;
import ftd.field.MenuCounter.CounterSpecifier;

public class ftduino_menu_COUNTER extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	public String gen() {
		CounterSpecifier counterSpecifier = ((MenuCounter) fields).getCounterSpecifier();
		String code = "scratchString(\"" + counterSpecifier.name() + "\")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		// TODO update fields?
	}

}
