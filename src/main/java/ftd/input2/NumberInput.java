package ftd.input2;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ftd.block.ScratchBlock;
import ftd.util.NumberUtil;

public class NumberInput extends ScratchInput {

	private int inputType;
	private int numberType;
	public int number;

	@JsonCreator()
	private NumberInput(@JsonProperty(index = 1) List<Object> numbers) {
		if (numbers == null)
			throw new IllegalStateException("unexpected");
		if (numbers.size() < 2) {
			throw new IllegalStateException("unexpected");
		}
		if (!(numbers.get(0) instanceof Integer)) {
			throw new IllegalStateException("unexpected");
		}
		this.inputType = (int) numbers.get(0);
		if (inputType != 1) {
			throw new IllegalStateException("expected 1");
		}
		if (!(numbers.get(1) instanceof List<?>)) {
			throw new IllegalStateException("unexpected");
		}
		if (!(((List<Object>) numbers.get(1)).get(0) instanceof Integer)) {
			throw new IllegalStateException("unexpected");
		}
		this.numberType = (int) ((List<Object>) numbers.get(1)).get(0);
		if (numberType != 4) {
			throw new IllegalStateException("expected 4");
		}
		this.number = NumberUtil.asInt(((List<Object>) numbers.get(1)).get(1));
	}

	@Override
	public String toString() {
		return "NumberInput [inputType=" + inputType + ", numberType=" + numberType + ", number=" + number + "]";
	}

	@Override
	public void updateRelations(Map<String, ScratchBlock> blocks) {
		// TODO Auto-generated method stub
		
	}

}
