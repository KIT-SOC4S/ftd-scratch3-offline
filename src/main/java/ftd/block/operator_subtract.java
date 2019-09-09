package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchConstants;
import ftd.ScratchValue;

public class operator_subtract extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "NUM1")
		public ScratchValue number1;

		@JsonProperty(value = "NUM2")
		public ScratchValue number2;
	}

	public String gen() {
		String number1 = (inputs.number1 != null ? inputs.number1.generateCode() : ScratchConstants.SCRATCH_ZERO);
		String number2 = (inputs.number2 != null ? inputs.number2.generateCode() : ScratchConstants.SCRATCH_ZERO);
		return "s_subtract((" + number1 + "), (" + number2 + "))";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.number1.updateRelations(blocks);
		this.inputs.number2.updateRelations(blocks);
	}
}
