package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

/**
 * Implements the ftduino output operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that outputs either 0 (LOW) or 1 (HIGH).
 * It expects an output specifier or a String that can be converted to one. It
 * also expects a value to set. This value will be converted to a boolean before
 * it's set.
 */
public class ftduino_output extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "OUTPUT")
		public ScratchValue output;

		@JsonProperty(value = "VALUE")
		public ScratchValue value;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_output(" + inputs.output.generateCode() + ", " + inputs.value.generateCode()
				+ ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.output.updateRelations(blocks);
		this.inputs.value.updateRelations(blocks);
	}

}
