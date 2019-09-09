package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchConstants;
import ftd.ScratchValue;

public class operator_lt extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "OPERAND1")
		public ScratchValue number1;// FIX

		@JsonProperty(value = "OPERAND2")
		public ScratchValue number2; // FIX
	}

	public String gen() {
		String number1 = (inputs.number1 != null ? inputs.number1.generateCode() : ScratchConstants.SCRATCH_FALSE);
		String number2 = (inputs.number2 != null ? inputs.number2.generateCode() : ScratchConstants.SCRATCH_FALSE);
		return "s_lt((" + number1 + "), (" + number2 + "))";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.number1.updateRelations(blocks);
		this.inputs.number2.updateRelations(blocks);
	}
}
