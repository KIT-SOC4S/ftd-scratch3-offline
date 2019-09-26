package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

public class ftduino_clear_counter extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "INPUT")
		public ScratchValue input;
	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_clear_counter(" + inputs.input.generateCode() + ");\n";
		return code;
		/*
		 * String clearCounterCode = "ftduino.counter_clear(toCounterSpecifier(" +
		 * inputs.input.generateCode() + "));\n"; return clearCounterCode;
		 */
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.input.updateRelations(blocks);
	}

}
