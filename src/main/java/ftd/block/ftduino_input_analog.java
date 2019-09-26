package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

public class ftduino_input_analog extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;

		@JsonProperty(value = "MODE")
		public ScratchValue mode;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_input_analog(" + inputs.input.generateCode() + ", " + inputs.mode.generateCode()
				+ ")";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
		this.inputs.mode.updateRelations(blocks);
	}

}
