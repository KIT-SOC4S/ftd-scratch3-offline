package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

public class ftduino_output extends ScratchBlock {

	@JsonProperty(value = "inputs")
	private Input inputs;

	private static class Input {
		@JsonProperty(value = "OUTPUT")
		public ScratchValue output;

		@JsonProperty(value = "VALUE")
		public ScratchValue value;

	}

	@Override
	public String gen() {
		String code = "scratch_ftduino_output(" + inputs.output.generateCode() + ", " + inputs.value.generateCode()
				+ ");\n";
		return code;
	}

	@Override
	protected void updateOtherRelations(Map<String, ScratchBlock> blocks) {
		this.inputs.output.updateRelations(blocks);
		this.inputs.value.updateRelations(blocks);
	}

}
