package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

public class ftduino_motor_stop extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "MOTOR")
		public ScratchValue motor;

		@JsonProperty(value = "STOPMODE")
		public ScratchValue stopMode;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_motor_stop(" + inputs.motor.generateCode() + ", "
				+ inputs.stopMode.generateCode() + ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.motor.updateRelations(blocks);
		this.inputs.stopMode.updateRelations(blocks);
	}

}
