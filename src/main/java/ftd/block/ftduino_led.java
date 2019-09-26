package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

public class ftduino_led extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "VALUE")
		public ScratchValue value;// FIX
	}

	@Override
	public String gen() {
		if (this.inputs.value != null) {
			return "digitalWrite(LED_BUILTIN, (toBoolean(" + inputs.value.generateCode() + ")) ? HIGH : LOW);\n";
		} else {
			throw new RuntimeException("HUH?");
		}
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.value.updateRelations(blocks);
	}

}
