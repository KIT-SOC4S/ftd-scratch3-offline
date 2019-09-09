package ftd.input2;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;

public class TimesInput extends ScratchInput {

	private int inputType;
	private int timesType;
	public int times;

	@JsonCreator()
	private TimesInput(@JsonProperty(index = 1) List<Object> times) {
		if (times == null)
			throw new IllegalStateException("unexpected");
		if (times.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (!(times.get(0) instanceof Integer)) {
			throw new IllegalStateException("unexpected");
		}
		this.inputType = (int) times.get(0);
		if (inputType != 1) {
			throw new IllegalStateException("expected 1");
		}
		if (!(times.get(1) instanceof List<?>)) {
			throw new IllegalStateException("unexpected");
		}
		if (!(((List<Object>) times.get(1)).get(0) instanceof Integer)) {
			throw new IllegalStateException("unexpected");
		}
		this.timesType = (int) ((List<Object>) times.get(1)).get(0);
		if (timesType != 6) {
			throw new IllegalStateException("expected 6");
		}
		this.times = Integer.valueOf((String) ((List<Object>) times.get(1)).get(1));
	}

	public void updateRelations(Map<String, ScratchBlock> blocks) {
	}

	@Override
	public String toString() {
		return "TimesInput [inputType=" + inputType + ", timesType=" + timesType + ", times=" + times + "]";
	}

}
