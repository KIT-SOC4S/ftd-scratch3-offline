package ftd.block;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.ScratchValue;

/**
 * Implements the ftduino motor stop operator. The actual C++ code is in
 * scratch_ftduino.cpp. This is a block stops a specified motor in a specified
 * stop mode. It expects a motor specifier or a String that can be converted to
 * one. It also expects a stop mode or a String that can be converted to one.
 * The supported stop modes are
 * {@link ftd.field.MotorStopModeField.MotorStopMode#STOP} and
 * {@link ftd.field.MotorStopModeField.MotorStopMode#BRAKE}
 */
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
