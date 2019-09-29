package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

/**
 * Implements the ftduino input operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that reads a digital input and returns a
 * boolean. It expects a digital input specifier or a String that can be
 * converted to one.
 */
public class ftduino_input extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_input(" + inputs.input.generateCode() + ")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
	}

}
