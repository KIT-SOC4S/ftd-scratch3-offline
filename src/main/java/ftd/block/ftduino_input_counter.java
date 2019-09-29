package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

/**
 * Implements the ftduino input counter operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block that reads a counter input and returns
 * the read value as a float. It expects a counter input specifier or a String
 * that can be converted to one.
 */
public class ftduino_input_counter extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_input_counter(" + inputs.input.generateCode() + ")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
	}

}
