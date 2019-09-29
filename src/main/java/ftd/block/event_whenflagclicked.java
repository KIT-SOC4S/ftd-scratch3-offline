package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Implements the scratch when flag clicked operator i.e. the when green flag
 * clicked hat. Since there are no graphics on the arduino the program will
 * automatically start after setup and not wait for any user action.
 */
public class event_whenflagclicked extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
	}

	@Override
	protected String beginGen() {
		return "void loop() {\n";
	}

	@Override
	protected String afterGen() {
		return super.afterGen() + "}\n";
	}

	public String gen() {
		return "";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
	}
}
