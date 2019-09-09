package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchConstants;
import ftd.ScratchValue;

public class operator_not extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "OPERAND")
		public ScratchValue number1;// FIX
	}

	public String gen() {
		String condition = (inputs.number1 != null ? inputs.number1.generateCode() : ScratchConstants.SCRATCH_FALSE);
		return "s_not((" + condition + "))";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.number1.updateRelations(blocks);
	}
}
