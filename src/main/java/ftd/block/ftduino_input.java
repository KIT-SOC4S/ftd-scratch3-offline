package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

public class ftduino_input extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;// FIX
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
