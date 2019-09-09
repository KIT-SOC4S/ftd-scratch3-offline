package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

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
