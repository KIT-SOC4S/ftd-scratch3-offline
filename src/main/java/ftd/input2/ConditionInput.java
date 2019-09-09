package ftd.input2;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class ConditionInput extends ScratchInput {

	private int inputType;

	private String inputBlock_;
	private ScratchBlock inputBlock;

	@JsonCreator()
	private ConditionInput(@JsonProperty(index = 1) List<Object> times) {
		if (times == null)
			throw new IllegalStateException("unexpected");
		if (times.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (!(times.get(0) instanceof Integer)) {
			throw new IllegalStateException("unexpected");
		}
		this.inputType = (int) times.get(0);
		if (inputType != 2) {
			throw new IllegalStateException("expected 2");
		}
		if (!(times.get(1) instanceof String)) {
			throw new IllegalStateException("unexpected");
		}
		/*
		 * this.timesType = (int) ((List<Object>) times.get(1)).get(0); if (timesType !=
		 * 6) { throw new IllegalStateException("expected 6"); } this.times =
		 * Integer.valueOf((String) ((List<Object>) times.get(1)).get(1));
		 */
	}

	public void updateRelations(Map<String, ScratchBlock> blocks) {
		this.inputBlock = blocks.get(inputBlock_);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}