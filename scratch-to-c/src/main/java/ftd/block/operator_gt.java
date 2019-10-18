package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchConstants;
import ftd.ScratchValue;

/**
 * Implements the scratch greater than operator. The actual C++ code is in
 * operators.cpp. Computes operand1 > operand2. Both inputs are converted to
 * floats if necessary. If one argument could not be converted the inputs are
 * converted to strings and compared case insensitive. The returned value is a
 * boolean.
 */
public class operator_gt extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "OPERAND1")
		public ScratchValue operand1;

		@JsonProperty(value = "OPERAND2")
		public ScratchValue operand2;
	}

	public String gen() {
		String operand1 = (inputs.operand1 != null ? inputs.operand1.generateCode() : ScratchConstants.SCRATCH_FALSE);
		String operand2 = (inputs.operand2 != null ? inputs.operand2.generateCode() : ScratchConstants.SCRATCH_FALSE);
		return "s_gt((" + operand1 + "), (" + operand2 + "))";
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.operand1.updateRelations(blocks);
		this.inputs.operand2.updateRelations(blocks);
	}
}
