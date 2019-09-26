package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

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
		/*
		 * String counterGetCode =
		 * "scratchNumber(ftduino.counter_get(toCounterInputSpecifier(" +
		 * inputs.input.generateCode() + ")))"; return counterGetCode;
		 */
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
	}

}
