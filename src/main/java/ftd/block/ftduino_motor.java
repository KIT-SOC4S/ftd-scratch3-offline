package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

public class ftduino_motor extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "VALUE")
		public ScratchValue value;

		@JsonProperty(value = "MOTOR")
		public ScratchValue motor;

		@JsonProperty(value = "DIR")
		public ScratchValue dir;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_motor(" + inputs.motor.generateCode() + ", " + inputs.dir.generateCode() + ", "
				+ inputs.value.generateCode() + ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.motor.updateRelations(blocks);
		this.inputs.dir.updateRelations(blocks);
		this.inputs.value.updateRelations(blocks);
	}

}
